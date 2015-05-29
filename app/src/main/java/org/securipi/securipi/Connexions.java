package org.securipi.securipi;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Objet Connexion
 */
public class Connexions {
	// ATTRIBUTS

	private int identifiant, id_client;
	private String date, heure, ip;

	// ----------------------------------
	// ACCESSEURS

	/**
	 * Retourne l'identifiant
	 * @return identifiant <int>
	 */
	public int getIdentifiant() {
		return identifiant;
	}
	/**
	 * Modifie l'identifiant
	 * @param identifiant <int>
	 */
	public void setIdentifiant(int identifiant) {
		this.identifiant = identifiant;
	}
	
	/**
	 * Retourne l'identifiant du client
	 * @return id_client <int>
	 */
	public int getID_Client() {
		return id_client;
	}
	/**
	 * Modifie l'identifiant du client
	 * @param id_client <int>
	 */
	public void setID_Client(int id_client) {
		this.id_client = id_client;
	}
	
	/**
	 * Retourne la date de la connexion
	 * @return date <String>
	 */
	public String getDate() {
		return date;
	}
	/**
	 * Modifie la date de la connexion
	 * @param date <String>
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * Retourne l'heure de la connexion
	 * @return heure <String>
	 */
	public String getHeure() {
		return heure;
	}
	/**
	 * Modifie l'heure de la connexion
	 * @param heure <String>
	 */
	public void setHeure(String heure) {
		this.heure = heure;
	}
	
	/**
	 * Retourne l'adresse IP de la connexion
	 * @return ip <String>
	 */
	public String getIP() {
		return ip;
	}
	/**
	 * Modifie l'adresse IP de la connexion
	 * @param ip <String>
	 */
	public void setIP(String ip) {
		this.ip = ip;
	}
	
	//-----------------------------------
	// CONSTRUCTEURS

	/**
	 * Constructeurs avec paramètres (Heure et date automatique)
	 * @param identifiant <Integer>
	 * @param id_client <String>
	 * @param ip <String>
	 */
	public Connexions(int identifiant, int id_client, String ip) {
		Date now = new Date();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(now);
		String heure = new SimpleDateFormat("HH:mm:ss").format(now);
				
		this.identifiant		= identifiant;
		this.id_client			= id_client;
		this.date				= date;
		this.heure				= heure;
		this.ip					= ip;
	}
	/**
	 * Contructeur avec paramètres
	 * @param identifiant <Integer>
	 * @param id_client <String>
	 * @param date <String>
	 * @param heure <String>
	 * @param ip <String>
	 */
	public Connexions(int identifiant, int id_client, String date, String heure, String ip) {		
		this.identifiant		= identifiant;
		this.id_client			= id_client;
		this.date				= date;
		this.heure				= heure;
		this.ip					= ip;
	}
}
