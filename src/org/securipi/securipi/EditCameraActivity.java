package org.securipi.securipi;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity de gestion d'une caméra
 */
public class EditCameraActivity extends Activity implements OnClickListener {

	private DAO dao = new DAO();
	private EditText editTextNom, editTextEmplacement, editTextIP, editTextComplement, editTextPort;
	private Button buttonSaveCam, buttonDelCam;
	int id_camera, id_client;
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
		setContentView(R.layout.activity_edit_camera);
		id_camera = this.getIntent().getExtras().getInt("id_camera");
		id_client = this.getIntent().getExtras().getInt("id_client");
        
        DbAdapter dbSecuriPi = new DbAdapter(this);
		dbSecuriPi.open();

		camera = dbSecuriPi.getCameraWithId(id_camera);

		dbSecuriPi.close();
		
		editTextNom = (EditText) findViewById(R.id.editTextNom);
		editTextEmplacement = (EditText) findViewById(R.id.editTextEmplacement);
		editTextIP = (EditText) findViewById(R.id.editTextIP);
		editTextComplement = (EditText) findViewById(R.id.editTextComplement);
		editTextPort = (EditText) findViewById(R.id.editTextPort);
		
		editTextNom.setText(camera.getNom());
		editTextEmplacement.setText(camera.getEmplacement());
		editTextIP.setText(camera.getIP());
		if(!camera.getComplement().matches("null")) {
			editTextComplement.setText(camera.getComplement());
		}
		if(camera.getPort() != 0) {
			editTextPort.setText(String.valueOf(camera.getPort()));
		}
		
		buttonSaveCam = (Button) findViewById(R.id.buttonSaveCam);
		buttonDelCam = (Button) findViewById(R.id.buttonDelCam);
		
		buttonSaveCam.setOnClickListener(this);
		buttonDelCam.setOnClickListener(this);
	}

    /**
     * Évenement : Click sur un objet
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.buttonSaveCam:
			if (!editTextNom.getText().toString().matches("") && !editTextEmplacement.getText().toString().matches("") && !editTextIP.getText().toString().matches("")) {
				camera.setNom(editTextNom.getText().toString());
				camera.setEmplacement(editTextEmplacement.getText().toString());
				camera.setIP(editTextIP.getText().toString());
				camera.setComplement(editTextComplement.getText().toString());
				
				int port = 0;
				try {
					port = Integer.parseInt(editTextPort.getText().toString());
				} catch(NumberFormatException e) {
					port = 0;
				}
				
				camera.setPort(port);

				dbSecuriPi = new DbAdapter(this);
				dbSecuriPi.open();

				dbSecuriPi.updateCamera(id_camera, camera);
				dbSecuriPi.close();

				setCameras = new Thread(new Runnable() {
					public void run() {
						dbSecuriPi = new DbAdapter(EditCameraActivity.this);
						dbSecuriPi.open();

						dao.setCameras(new ArrayList<Cameras>(dbSecuriPi.getListCameras(id_client)));
					}
				});
				setCameras.start();
				try { setCameras.join(); } catch(Exception e) {}
				dbSecuriPi.close();

				Toast.makeText(this, "Caméra modifiée", Toast.LENGTH_SHORT).show();
				
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
			break;

		case R.id.buttonDelCam:
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditCameraActivity.this);

			alertDialog.setTitle("Comfirmation");
			alertDialog.setMessage("Supprimer la caméra ?");

			// Oui
			alertDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
					dbSecuriPi = new DbAdapter(EditCameraActivity.this);
					dbSecuriPi.open();

					dbSecuriPi.removeCameraWithID(id_camera, id_client);
					dbSecuriPi.close();

					setCameras = new Thread(new Runnable() {
						public void run() {
							dbSecuriPi = new DbAdapter(EditCameraActivity.this);
							dbSecuriPi.open();

							dao.setCameras(new ArrayList<Cameras>(dbSecuriPi.getListCameras(id_client)));
						}
					});
					setCameras.start();
					try { setCameras.join(); } catch(Exception e) {}
					dbSecuriPi.close();

					Toast.makeText(EditCameraActivity.this, "Caméra supprimée", Toast.LENGTH_SHORT).show();
					
					setResult(RESULT_OK);
					finish();
				}
			});

			// Non
			alertDialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			alertDialog.show();
			break;
		}
	}
}
