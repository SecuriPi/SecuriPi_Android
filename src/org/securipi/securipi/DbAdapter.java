package org.securipi.securipi;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Adapter de la base de données SQLite
 */
public class DbAdapter {
	private static final String NOM_BDD			= "db_securipi";
	
	static final int VERSION_BDD				= 1;
	
	static final String TABLE_CAMERAS			= "Cameras";
	
	static final String COL_ID_CAM				= "ID";
	static final int NUM_COL_ID_CAM				= 0;
	static final String COL_ID_CLIENT_CAM		= "ID_CLIENT";
	static final int NUM_COL_ID_CLIENT_CAM		= 1;
	static final String COL_NOM_CAM				= "NOM";
	static final int NUM_COL_NOM_CAM			= 2;
	static final String COL_EMPLACEMENT			= "EMPLACEMENT";
	static final int NUM_COL_EMPLACEMENT		= 3;
	static final String COL_IP_CAM				= "IP";
	static final int NUM_COL_IP_CAM				= 4;
	static final String COL_COMPLEMENT			= "COMPLEMENT";
	static final int NUM_COL_COMPLEMENT			= 5;
	static final String COL_PORT				= "PORT";
	static final int NUM_COL_PORT				= 6;
	
	static final String TABLE_CLIENTS			= "Clients";
	
	static final String COL_ID_CLIENTS			= "ID";
	static final int NUM_COL_ID_CLIENTS			= 0;
	static final String COL_NOM					= "NOM";
	static final int NUM_COL_NOM				= 1;
	static final String COL_PRENOM				= "PRENOM";
	static final int NUM_COL_PRENOM				= 2;
	static final String COL_LOGIN				= "LOGIN";
	static final int NUM_COL_LOGIN				= 3;
	static final String COL_PASSWORD			= "PASSWORD";
	static final int NUM_COL_PASSWORD			= 4;
	
	static final String TABLE_CONNEXIONS		= "Connexions";
	
	static final String COL_ID					= "ID";
	static final int NUM_COL_ID					= 0;
	static final String COL_ID_CLIENT			= "ID_CLIENT";
	static final int NUM_COL_ID_CLIENT			= 1;
	static final String COL_DATE				= "DATE";
	static final int NUM_COL_DATE				= 2;
	static final String COL_HEURE				= "HEURE";
	static final int NUM_COL_HEURE				= 3;
	static final String COL_IP					= "IP";
	static final int NUM_COL_IP					= 4;

	private CreateDbSecuriPi dbSecuriPi;
	private Context context;
	private SQLiteDatabase db;

	/**
	 * Contructeur par défaut
	 * @param context <Context>
	 */
	public DbAdapter(Context context) {
		this.context = context;
		dbSecuriPi = new CreateDbSecuriPi(context, NOM_BDD, null, VERSION_BDD);
	}

	/**
	 * Ouvre la base de données
	 * @return context <Context>
	 */
	public DbAdapter open() {
		db = dbSecuriPi.getWritableDatabase();
		return this;
	}

	/**
	 * Ferme la base de données
	 * @return null
	 */
	public DbAdapter close() {
		db.close();
		return null;
	}
	
	/**
	 * Vide la base de données
	 */
	public void clearDatabase() {
		db.execSQL("DELETE FROM Cameras");
		db.execSQL("DELETE FROM Connexions");
		db.execSQL("DELETE FROM Clients");
	}

	/**
	 * Insere une nouvelle caméra dans la base de données
	 * @param camera
	 * @return newID <long>
	 */
	public long insertCamera(Cameras camera) {
		ContentValues values = new ContentValues();

		values.put(COL_ID_CAM, camera.getIdentifiant());
		values.put(COL_ID_CLIENT_CAM, camera.getID_Client());
		values.put(COL_NOM_CAM, camera.getNom());
		values.put(COL_EMPLACEMENT, camera.getEmplacement());
		values.put(COL_IP, camera.getIP());
		values.put(COL_COMPLEMENT, camera.getComplement());
		
		if(camera.getPort() == 0) {
			values.putNull(COL_PORT);
		} else {
			values.put(COL_PORT, camera.getPort());
		}

		return db.insert(TABLE_CAMERAS, null, values);
	}
	
	/**
	 * Retourne la caméra correspondante à l'identifiant
	 * @param identifiant <int>
	 * @return camera <Cameras>
	 */
	public Cameras getCameraWithId(int identifiant){
		
		Cursor c = db.query(TABLE_CAMERAS, new String[] {
				COL_ID_CAM, COL_ID_CLIENT_CAM, COL_NOM_CAM, COL_EMPLACEMENT, COL_IP_CAM, COL_COMPLEMENT, COL_PORT}, COL_ID_CAM + " LIKE " + identifiant, null, null, null, null);
		
		return cursorToCamera(c);
	}

	/**
	 * Retourne la caméra correspondante au curseur
	 * @param c <Cursor>
	 * @return camera <Cameras>
	 */
	private Cameras cursorToCamera(Cursor c) {
		if (c.getCount() == 0) {
			return null;
		}

		c.moveToFirst();

		Cameras camera = new Cameras();

		camera.setIdentifiant(c.getInt(NUM_COL_ID_CAM));
		camera.setID_Client(c.getInt(NUM_COL_ID_CLIENT_CAM));
		camera.setNom(c.getString(NUM_COL_NOM_CAM));
		camera.setEmplacement(c.getString(NUM_COL_EMPLACEMENT));
		camera.setIP(c.getString(NUM_COL_IP_CAM));
		camera.setComplement(c.getString(NUM_COL_COMPLEMENT));
		camera.setPort(c.getInt(NUM_COL_PORT));

		c.close();

		return camera;
	}

	/**
	 * Met à jour une caméra déjà existante dans la base de données
	 * @param identifiant <int>
	 * @param camera <Cameras>
	 * @return affectedRows <int>
	 */
	public int updateCamera(int identifiant, Cameras camera) {
		ContentValues values = new ContentValues();

		values.put(COL_ID_CLIENT_CAM, camera.getID_Client());
		values.put(COL_NOM_CAM, camera.getNom());
		values.put(COL_EMPLACEMENT, camera.getEmplacement());
		values.put(COL_IP, camera.getIP());
		values.put(COL_COMPLEMENT, camera.getComplement());
		values.put(COL_PORT, camera.getPort());

		return db.update(TABLE_CAMERAS, values, COL_ID_CAM + " = " + identifiant, null);
	}

	/**
	 * Supprime une caméra
	 * @param identifiant <int>
	 * @param id_client <int>
	 */
	public void removeCameraWithID(int identifiant, int id_client) {
		db.delete(TABLE_CAMERAS, COL_ID_CAM + " = " + identifiant, null);
		db.execSQL("UPDATE Cameras SET ID = ID - 1 WHERE ID > " + identifiant + " AND ID_Client = " + id_client + ";");
	}

	/**
	 * Retourne la liste de toutes les caméras de la base de données
	 * @param id_client <int>
	 * @return listCameras <List<Cameras>>
	 */
	public List<Cameras> getListCameras(int id_client) {
		List<Cameras> listCameras = new ArrayList<Cameras>();
		
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CAMERAS + " WHERE ID_Client = " + id_client, null);
		
		if(c.getCount() == 0) { return listCameras; }
		
		c.moveToFirst();
		
		do {
			listCameras.add(new Cameras(c.getInt(NUM_COL_ID_CAM),
				c.getInt(NUM_COL_ID_CLIENT_CAM),
				c.getString(NUM_COL_NOM_CAM),
				c.getString(NUM_COL_EMPLACEMENT),
				c.getString(NUM_COL_IP),
				c.getString(NUM_COL_COMPLEMENT),
				c.getInt(NUM_COL_PORT)));
		} while (c.moveToNext());
		
		c.close();

		return listCameras;
	}
	
	/**
	 * Retourne l'id maximum dans la table cameras
	 * @param id_client <int>
	 * @return mexID <int>
	 */
	public int getMaxIDCamera(int id_client) {
		Cursor c = db.rawQuery("SELECT MAX(" + COL_ID_CAM + ") AS max_id FROM " + TABLE_CAMERAS + " WHERE " + COL_ID_CLIENT + " = " + id_client, null);
		c.moveToFirst();
		
		return c.getInt(0);
	}
	
	/**
	 * Insère un nouveau client dans la base de doonées
	 * @param client <Clients>
	 * @return newID <long>
	 */
	public long insertClient(Clients client) {
		ContentValues values = new ContentValues();

		values.put(COL_ID_CLIENTS, client.getIdentifiant());
		values.put(COL_NOM, client.getNom());
		values.put(COL_PRENOM, client.getPrenom());
		values.put(COL_LOGIN, client.getLogin());
		values.put(COL_PASSWORD, client.getPassword());

		return db.insert(TABLE_CLIENTS, null, values);
	}
	
	/**
	 * Met à jour un client déjà existant dans la base de données
	 * @param identifiant <int>
	 * @param client <Clients>
	 * @return affectedRows <int>
	 */
	public int updateClient(int identifiant, Clients client) {
		ContentValues values = new ContentValues();

		values.put(COL_ID_CLIENTS, client.getIdentifiant());
		values.put(COL_NOM, client.getNom());
		values.put(COL_PRENOM, client.getPrenom());
		values.put(COL_LOGIN, client.getLogin());
		values.put(COL_PASSWORD, client.getPassword());

		return db.update(TABLE_CLIENTS, values, COL_ID + " = '" + identifiant + "'", null);
	}
	
	/**
	 * Supprime un client
	 * @param identifiant <int>
	 */
	public void removeClientWithID(int identifiant) {
		db.delete(TABLE_CLIENTS, COL_ID_CLIENTS + " = " + identifiant, null);
	}
	
	/**
	 * Retourne le client correspondant à l'identifiant
	 * @param identifiant <int>
	 * @return Clients <Clients>
	 */
	public Clients getClientWithId(int identifiant){
		
		Cursor c = db.query(TABLE_CLIENTS, new String[] {
				COL_ID_CLIENTS, COL_NOM, COL_PRENOM, COL_LOGIN, COL_PASSWORD}, COL_ID_CLIENTS + " LIKE " + identifiant, null, null, null, null);
		
		return cursorToClient(c);
	}

	/**
	 * Retourne le client correspondant au curseur
	 * @param c <Context>
	 * @return client <Clients>
	 */
	private Clients cursorToClient(Cursor c) {
		if (c.getCount() == 0) {
			return null;
		}

		c.moveToFirst();

		Clients client = new Clients();

		client.setIdentifiant(c.getInt(NUM_COL_ID_CLIENTS));
		client.setNom(c.getString(NUM_COL_NOM));
		client.setPrenom(c.getString(NUM_COL_PRENOM));
		client.setLogin(c.getString(NUM_COL_LOGIN));
		client.setPassword(c.getString(NUM_COL_PASSWORD));

		c.close();

		return client;
	}
	
	/**
	 * Retourne la liste de tous les clients dans la base de données
	 * @param id_client <int>
	 * @return listClients <List<Clients>>
	 */
	public List<Clients> getListClients(int id_client) {
		List<Clients> listClients = new ArrayList<Clients>();
		
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CLIENTS + " WHERE ID = " + id_client, null);
		
		if(c.getCount() == 0) { return listClients; }
		
		c.moveToFirst();
		
		do {
			listClients.add(new Clients(c.getInt(NUM_COL_ID_CLIENTS),
				c.getString(NUM_COL_NOM),
				c.getString(NUM_COL_PRENOM),
				c.getString(NUM_COL_LOGIN),
				c.getString(NUM_COL_PASSWORD)));
		} while (c.moveToNext());
		
		c.close();

		return listClients;
	}

	/**
	 * Insère une nouvelle connexion dans la base de données
	 * @param connexion <Connexions>
	 * @return newID <long>
	 */
	public long insertConnexion(Connexions connexion) {
		ContentValues values = new ContentValues();

		values.put(COL_ID, connexion.getIdentifiant());
		values.put(COL_ID_CLIENT, connexion.getID_Client());
		values.put(COL_DATE, connexion.getDate());
		values.put(COL_HEURE, connexion.getHeure());
		values.put(COL_IP, connexion.getIP());

		return db.insert(TABLE_CONNEXIONS, null, values);
	}
	
	/**
	 * Retourne la liste de toutes les connexions de la base de données
	 * @param id_client <int>
	 * @return listConnexions <List<Connexions>>
	 */
	public List<Connexions> getListConnexions(int id_client) {
		List<Connexions> listConnexions = new ArrayList<Connexions>();
		
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CONNEXIONS + " WHERE ID_Client = " + id_client, null);
		
		if(c.getCount() == 0) { return listConnexions; }
		
		c.moveToFirst();
		
		do {
			listConnexions.add(new Connexions(c.getInt(NUM_COL_ID),
				c.getInt(NUM_COL_ID_CLIENT),
				c.getString(NUM_COL_DATE),
				c.getString(NUM_COL_HEURE),
				c.getString(NUM_COL_IP)));
		} while (c.moveToNext());
		
		c.close();

		return listConnexions;
	}
	
	/**
	 * Retourne l'id maximum de la table connexions
	 * @param id_client <int>
	 * @return maxID <int>
	 */
	public int getMaxIDConnexion(int id_client) {
		Cursor c = db.rawQuery("SELECT MAX(" + COL_ID + ") AS max_id FROM " + TABLE_CONNEXIONS + " WHERE " + COL_ID_CLIENT + " = " + id_client, null);
		c.moveToFirst();
		
		return c.getInt(0);
	}
}
