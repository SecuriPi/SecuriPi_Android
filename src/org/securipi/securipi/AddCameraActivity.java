package org.securipi.securipi;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity d'ajout d'une caméra
 */
public class AddCameraActivity extends Activity implements OnClickListener {
	
	private DAO dao = new DAO();
	private EditText editTextNom, editTextEmplacement, editTextIP, editTextComplement, editTextPort;
	private Button buttonAddCam;
	private int id_client;
	private Cameras camera;
	private Thread setCameras;
	private DbAdapter dbSecuriPi;

	/**
	 * Création de l'activity
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_camera);

		id_client = this.getIntent().getExtras().getInt("id_client");
		
		editTextNom = (EditText) findViewById(R.id.editTextNom);
		editTextEmplacement = (EditText) findViewById(R.id.editTextEmplacement);
		editTextIP = (EditText) findViewById(R.id.editTextIP);
		editTextComplement = (EditText) findViewById(R.id.editTextComplement);
		editTextPort = (EditText) findViewById(R.id.editTextPort);
		
		buttonAddCam = (Button) findViewById(R.id.buttonAddCam);
		buttonAddCam.setOnClickListener(this);		
	}

	/**
	 * Évenement : Click sur un objet
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (!editTextNom.getText().toString().matches("") && !editTextEmplacement.getText().toString().matches("") && !editTextIP.getText().toString().matches("")) {
			dbSecuriPi = new DbAdapter(this);
			dbSecuriPi.open();
			
			int port;
			try {
				port = Integer.parseInt(editTextPort.getText().toString());
			} catch(NumberFormatException e) {
				port = 0;
			}
			
			dbSecuriPi.insertCamera(new Cameras(dbSecuriPi.getMaxIDCamera(id_client) + 1, id_client, editTextNom.getText().toString(), editTextEmplacement.getText().toString(), editTextIP.getText().toString(), editTextComplement.getText().toString(), port));
			
			dbSecuriPi.close();
			
			setCameras = new Thread(new Runnable() {
				public void run() {
					dbSecuriPi = new DbAdapter(AddCameraActivity.this);
					dbSecuriPi.open();

					dao.setCameras(new ArrayList<Cameras>(dbSecuriPi.getListCameras(id_client)));
				}
			});
			setCameras.start();
			try { setCameras.join(); } catch(Exception e) {}
			dbSecuriPi.close();

			Toast.makeText(this, "Caméra ajoutée", Toast.LENGTH_SHORT).show();
			
			setResult(RESULT_OK);
			finish();
		} else {
			if (editTextNom.getText().toString().matches("")) {
				Toast.makeText(this, "Le nom ne peux pas être vide", Toast.LENGTH_SHORT).show();
			}
			if (editTextEmplacement.getText().toString().matches("")) {
				Toast.makeText(this, "L'emplacement ne peux pas être vide", Toast.LENGTH_SHORT).show();
			}
			if (editTextIP.getText().toString().matches("")) {
				Toast.makeText(this, "L'IP ne peux pas être vide", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
