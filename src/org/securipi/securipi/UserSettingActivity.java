package org.securipi.securipi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Classe de gestion du compte utilisateur
 *
 */
public class UserSettingActivity extends Activity implements OnClickListener {

	private DAO dao = new DAO();
	private EditText editTextLogin, editTextPassword, editTextPasswordConfirm;
	private Button buttonSaveAccount;
	private Clients connected_client;
	private int id_client;
	private Intent intentModifyLogin, intentModifyPassword;
	
	/**
	 * Création de l'activity
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_setting);
		id_client = this.getIntent().getExtras().getInt("id_client");
		
		DbAdapter dbSecuriPi = new DbAdapter(this);
		dbSecuriPi.open();
		connected_client = dbSecuriPi.getClientWithId(id_client);
		dbSecuriPi.close();
		
		editTextLogin = (EditText) findViewById(R.id.editTextLogin);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextPasswordConfirm = (EditText) findViewById(R.id.editTextPasswordConfirm);
		
		buttonSaveAccount = (Button) findViewById(R.id.buttonConfirmAccount);
		
		editTextLogin.setText(connected_client.getLogin());
		
		buttonSaveAccount.setOnClickListener(this);
	}
	
	/**
	 * Évenement : Click sur un objet
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.buttonConfirmAccount:
			Boolean bModified = true;
			String login = editTextLogin.getText().toString();
			String password = editTextPassword.getText().toString();
			String passwordConfirm = editTextPasswordConfirm.getText().toString();

			if (!login.matches("")) {
				if(!password.matches("") && !passwordConfirm.matches("")) {
					if(password.matches(passwordConfirm)) {
						connected_client.setLogin(login);
						connected_client.setPassword(md5(password));
					} else {
						Toast.makeText(this, "Les deux mots de passes sont différents", Toast.LENGTH_SHORT).show();
						bModified = false;
					}
				} else {
					connected_client.setLogin(login);
				}
				
				if(bModified == true) {
				
					DbAdapter dbSecuriPi = new DbAdapter(this);
					dbSecuriPi.open();
					dbSecuriPi.updateClient(id_client, connected_client);
					dbSecuriPi.close();
					
					Thread setClients = new Thread(new Runnable() {
						public void run() {
							DbAdapter dbSecuriPi = new DbAdapter(UserSettingActivity.this);
							dbSecuriPi.open();
							
							dao.setClients(new ArrayList<Clients>(dbSecuriPi.getListClients(id_client)));
						}
					});
					setClients.start();
					try { setClients.join(); } catch(Exception e) {}
					dbSecuriPi.close();

	        		Toast.makeText(this, "Votre compte à été modifié", Toast.LENGTH_SHORT).show();
	        	}
				
				editTextPassword.setText(null);
	        	editTextPasswordConfirm.setText(null);
			}
			else {
				Toast.makeText(this, "L'identifiant ne peux pas être vide", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
	
	
	/**
	 * Encrypte une chaine de caractère en MD5
	 * @param s <String>
	 * @return cryptedString <String>
	 */
	private String md5(String s) {
	    try {
	 
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	 
	        StringBuffer hexString = new StringBuffer();
	        for(int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            if(h.length() < 2) {
	                h = "0" + h;
	            }
	            hexString.append(h);
	        }
	        return hexString.toString();
	 
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
}
