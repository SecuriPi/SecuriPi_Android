package org.securipi.securipi;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * Activity de visualisation d'une caméra
 */
public class MjpegSampleActivity extends Activity {
	
	private static final String TAG = "MjpegActivity";
	private int id_camera;
    private String URL = "";
    private MjpegView mv;
    private Cameras camera;

    /**
     * Création de l'activity
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_camera = this.getIntent().getExtras().getInt("id_camera");
        
        DbAdapter dbSecuriPi = new DbAdapter(this);
		dbSecuriPi.open();

		camera = dbSecuriPi.getCameraWithId(id_camera);

		dbSecuriPi.close();
		
		URL = "http://" + camera.getIP();
		if(camera.getPort() >= 1 && camera.getPort() <= 65536) { URL += ":" + camera.getPort(); }
		if(!camera.getComplement().isEmpty()) { URL += camera.getComplement(); }

        mv = new MjpegView(this);
        setContentView(mv);

        new DoRead().execute(URL);
    }

    /**
     * Méthode onPause lorsque l'application est en pause
     */
    public void onPause() {
        super.onPause();
        mv.stopPlayback();
        finish();
    }

    /**
     * Classe d'envoi de la requête en asynchrone
     */
    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
            HttpResponse res = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            Log.d(TAG, "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url[0])));
                Log.d(TAG, "2. Request finished, status = "
                        + res.getStatusLine().getStatusCode());
                if (res.getStatusLine().getStatusCode() == 401) {
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-ClientProtocolException", e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-IOException", e);
            }

            return null;
        }

        /**
         * Récupère le flux et l'adapte à la taille de l'écran de l'appareil
         * @param result <MjpegInputStream>
         */
        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv.showFps(true);
        }
    }
}
