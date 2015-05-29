package org.securipi.securipi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity de connexion
 */
public class LoginActivity extends Activity {
	private int id_client;
	private Intent intentCameras;

	private Button buttonLogin;
	private EditText login, password;
	
	private HttpPost httpPost;
	private HttpResponse response;
	private HttpClient httpClient;
	private List<NameValuePair> nameValuePairs;
	private ProgressDialog dialog = null;

	/**
	 * Création de l'activity
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		login = (EditText) findViewById(R.id.login);
		password = (EditText) findViewById(R.id.password);

		ConnexionDetector cd = new ConnexionDetector(getApplicationContext());

		final Boolean isInternetPresent = cd.isConnectingToInternet();

		buttonLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isInternetPresent) {
					dialog = ProgressDialog.show(LoginActivity.this, "", "Authentification...", true);
					new Thread(new Runnable() {
						public void run() {
							login();
						}
					}).start();
					
				} else {
					Toast.makeText(LoginActivity.this, "Pas de connexion internet !", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}

	/**
	 * Permet la connexion à la base de donnée et la connexion d'un utilisateur
	 */
	void login() {
		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://172.16.69.21/Scripts/Android/connexionAndroid.php");

			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("login", login.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString().trim()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//Execute HTTP Post Request
			response = httpClient.execute(httpPost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);

			runOnUiThread(new Runnable() {
				public void run() {
					dialog.dismiss();
				}
			});
			
			if(response.equalsIgnoreCase("false")) {
				showAlert();
			} else {
				id_client = Integer.valueOf(response);
				
				runOnUiThread(new Runnable() {
					public void run() {
						intentCameras = new Intent(LoginActivity.this, CamerasActivity.class);
						intentCameras.putExtra("id_client", id_client);
						LoginActivity.this.startActivity(intentCameras);
					}
				});
			}
		} catch (Exception e) {
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
	}

	/**
	 * Affiche un message d'erreur en cas d'erreur d'authentification
	 */
	public void showAlert() {
		LoginActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				password.setText(null);
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setTitle("Erreur !");
				builder.setMessage("Erreur d'authentification").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
}