package org.securipi.securipi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

/**
 * Activity de la liste des caméras
 */
public class CamerasActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {
	private int id_client;
	private Intent intentVisualisation;
	private Intent intentEditCamera;

	private ListView listView;
	private List<Cameras> listeCamerasClient;
	private CamerasAdapter camerasAdapter;
	
	private static final int RESULT_SETTINGS = 1;
	
	private Thread getData = new Thread(new Runnable() {
		public void run() {
			getClients();
			getCameras();
			getConnexions();
			dialog.dismiss();
		}
	});
	private HttpPost httpPost;
	private HttpResponse response;
	private HttpClient httpClient;
	private ProgressDialog dialog = null;
	
	private DAO dao = new DAO();
	private ArrayList<Clients> listeClients = new ArrayList<Clients>();
	private ArrayList<Cameras> listeCameras = new ArrayList<Cameras>();
	private ArrayList<Connexions> listeConnexions = new ArrayList<Connexions>();

	/**
	 * Création de l'activity
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_cameras);
		id_client = this.getIntent().getExtras().getInt("id_client");
		
		dialog = ProgressDialog.show(CamerasActivity.this, "", "Récupération des données", true);
    	getData.start();
    	try {
    		getData.join();
    	} catch(Exception e) {
    		Log.d("", "Exeception : " + e.getMessage());
    	}
    	initDb();
    	
		
		DbAdapter dbSecuriPi = new DbAdapter(this);
		dbSecuriPi.open();

		listeCamerasClient = dbSecuriPi.getListCameras(id_client);

		dbSecuriPi.close();

		listView = (ListView)findViewById(R.id.listViewCameras);
		camerasAdapter = new CamerasAdapter(this, listeCamerasClient);
		listView.setAdapter(camerasAdapter);

		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}
	
	/**
	 * Initialise le contenu des menus
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cameras, menu);
		return true;
	}

	/**
	 * Évenement : Click sur un élément du menu
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings:
				Intent intentUserSettings = new Intent(this, UserSettingActivity.class);
				intentUserSettings.putExtra("id_client", id_client);
				startActivityForResult(intentUserSettings, RESULT_SETTINGS);
				break;
				
			case R.id.menu_addCam:
				Intent intentAddCam = new Intent(this, AddCameraActivity.class);
				intentAddCam.putExtra("id_client", id_client);
				startActivityForResult(intentAddCam, RESULT_SETTINGS);
				break;
		}

		return true;
	}

	/**
	 * Évenement : Click sur un élément de la liste
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		Intent intentVisualisation = new Intent(this, MjpegSampleActivity.class);
		intentVisualisation.putExtra("id_camera", listeCameras.get(position).getIdentifiant());
        startActivity(intentVisualisation);
	}
	
	/**
	 * Évenement : Click long sur un élément de la liste
	 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intentEditCamera = new Intent(this, EditCameraActivity.class);
		intentEditCamera.putExtra("id_client", id_client);
		intentEditCamera.putExtra("id_camera", listeCameras.get(position).getIdentifiant());
		this.startActivityForResult(intentEditCamera, 0);
		return true;
	}
	
	/**
	 * Évenement : Fermeture d'une activity lancée en attente d'un résultat
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int num, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			DbAdapter dbSecuriPi = new DbAdapter(this);
			dbSecuriPi.open();

			listeCamerasClient = dbSecuriPi.getListCameras(id_client);

			dbSecuriPi.close();
			
			listeCameras = new ArrayList<Cameras>(listeCamerasClient);

			listView = (ListView)findViewById(R.id.listViewCameras);
			camerasAdapter = new CamerasAdapter(this, listeCamerasClient);
			listView.setAdapter(camerasAdapter);
		}
	}
	
	/**
	 * Récupère les informations du client connecté depuis la base distante
	 */
	public void getClients() {
		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://172.16.69.21/Scripts/Android/getClients.php?id_client="+id_client);
			
			response = httpClient.execute(httpPost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);

			listeClients = dao.getListClients(response);
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
	
	/**
	 * Récupère les informations des caméras du client connecté depuis la base distante
	 */
	public void getCameras() {
		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://172.16.69.21/Scripts/Android/getCameras.php?id_client="+id_client);
			
			response = httpClient.execute(httpPost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);

			listeCameras = dao.getListCameras(response);
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}

	/**
	 * Récupère les connexions du client connecté depuis la base distante
	 */
	public void getConnexions() {
		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://172.16.69.21/Scripts/Android/getConnexions.php?id_client="+id_client);
			
			response = httpClient.execute(httpPost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);

			listeConnexions = dao.getListConnexions(response);
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}

	/**
	 * Initialise la base de données
	 */
	public void initDb() {
		DbAdapter dbSecuriPi = new DbAdapter(this);
		dbSecuriPi.open();
		
		dbSecuriPi.clearDatabase();

		for(Clients client : listeClients) {
			dbSecuriPi.insertClient(client);
		}
		for(Cameras camera : listeCameras) {
			dbSecuriPi.insertCamera(camera);
		}
		for(Connexions connexion : listeConnexions) {
			dbSecuriPi.insertConnexion(connexion);
		}
		
		dbSecuriPi.insertConnexion(new Connexions(dbSecuriPi.getMaxIDConnexion(id_client) + 1, id_client, getLocalIpAddress()));
		
		Thread setConnexions = new Thread(new Runnable() {
			public void run() {
				DbAdapter dbSecuriPi = new DbAdapter(CamerasActivity.this);
				dbSecuriPi.open();
				
				dao.setConnexions(new ArrayList<Connexions>(dbSecuriPi.getListConnexions(id_client)));
			}
		});
		setConnexions.start();
		try { setConnexions.join(); } catch(Exception e) {}
		
		dbSecuriPi.close();
		dialog.dismiss();
	}
	
	/**
	 * Retourne l'adresse IP de l'appreil mobile
	 * @return ip <String>
	 */
	public String getLocalIpAddress(){
		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
		
		return ip;
	}
}
