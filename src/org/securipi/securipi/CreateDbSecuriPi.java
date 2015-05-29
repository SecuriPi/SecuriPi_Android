package org.securipi.securipi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe de crétion de la base de données SQLite
 */
public class CreateDbSecuriPi extends SQLiteOpenHelper {
	static final String TABLE_CAMERAS			= "Cameras";
	static final String COL_ID_CAM				= "ID";
	static final String COL_ID_CLIENT_CAM		= "ID_CLIENT";
	static final String COL_NOM_CAM				= "NOM";
	static final String COL_EMPLACEMENT			= "EMPLACEMENT";
	static final String COL_IP_CAM				= "IP";
	static final String COL_COMPLEMENT			= "COMPLEMENT";
	static final String COL_PORT				= "PORT";
	
	static final String TABLE_CLIENTS			= "Clients";
	static final String COL_ID_CLIENTS			= "ID";
	static final String COL_NOM					= "NOM";
	static final String COL_PRENOM				= "PRENOM";
	static final String COL_LOGIN				= "LOGIN";
	static final String COL_PASSWORD			= "PASSWORD";
	
	static final String TABLE_CONNEXIONS		= "Connexions";
	static final String COL_ID					= "ID";
	static final String COL_ID_CLIENT			= "ID_CLIENT";
	static final String COL_DATE				= "DATE";
	static final String COL_HEURE				= "HEURE";
	static final String COL_IP					= "IP";
	
	private static final String CREATE_BDD_TABLE_CLIENTS = "CREATE TABLE " + TABLE_CLIENTS + " ("
			+ COL_ID_CLIENTS + " INTEGER PRIMARY KEY,"
			+ COL_NOM + " TEXT,"
			+ COL_PRENOM + " TEXT,"
			+ COL_LOGIN + " TEXT,"
			+ COL_PASSWORD + " TEXT);";
			
	private static final String CREATE_BDD_TABLE_CAMERAS = "CREATE TABLE " + TABLE_CAMERAS + " ("
			+ COL_ID_CAM + " INTEGER,"
			+ COL_ID_CLIENT_CAM + " INTEGER,"
			+ COL_NOM_CAM + " TEXT,"
			+ COL_EMPLACEMENT + " TEXT,"
			+ COL_IP_CAM + " TEXT,"
			+ COL_COMPLEMENT + " TEXT,"
			+ COL_PORT + " INT,"
			+ "PRIMARY KEY(" + COL_ID_CAM + ", " + COL_ID_CLIENT_CAM + "),"
			+ "FOREIGN KEY(" + COL_ID_CLIENT_CAM + ") REFERENCES " + TABLE_CLIENTS + "(" + COL_ID_CLIENTS + "));";
			
	private static final String CREATE_BDD_TABLE_CONNEXIONS = "CREATE TABLE " + TABLE_CONNEXIONS + " ("
			+ COL_ID + " INTEGER,"
			+ COL_ID_CLIENT + " INTEGER,"
			+ COL_DATE + " TEXT,"
			+ COL_HEURE + " TEXT,"
			+ COL_IP + " TEXT,"
			+ "PRIMARY KEY(" + COL_ID + ", " + COL_ID_CLIENT + "),"
			+ "FOREIGN KEY(" + COL_ID_CLIENT + ") REFERENCES " + TABLE_CLIENTS + "(" + COL_ID_CLIENTS + "));";

	/**
	 * Constructeur par défaut
	 * @param context <Context>
	 * @param name <String>
	 * @param factory <CursorFactory>
	 * @param version <int>
	 */
	public CreateDbSecuriPi (Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BDD_TABLE_CLIENTS);
		db.execSQL(CREATE_BDD_TABLE_CAMERAS);
		db.execSQL(CREATE_BDD_TABLE_CONNEXIONS);
	}

	/**
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMERAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONNEXIONS);
		onCreate(db);
	}
}
