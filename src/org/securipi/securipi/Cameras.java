package org.securipi.securipi;

/**
 * Objet Camera
 */
public class Cameras {
	
	// ATTRIBUTS
	private int identifiant, id_client, port;
	private String nom, emplacement, ip, complement;
	
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
	 * Retourne le nom
	 * @return nom <String>
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * Modifie le nom
	 * @param nom <String>
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Retourne l'emplacement
	 * @return emplacement <String>
	 */
	public String getEmplacement() {
		return emplacement;
	}
	/**
	 * Modifie l'emplacement
	 * @param emplacement <String>
	 */
	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}
	
	/**
	 * Retourne l'adresse IP
	 * @return ip <String>
	 */
	public String getIP() {
		return ip;
	}
	/**
	 * Modifie l'adresse IP
	 * @param ip <String>
	 */
	public void setIP(String ip) {
		this.ip = ip;
	}
	
	/**
	 * Retourne le complement de l'adresse
	 * @return complement <String>
	 */
	public String getComplement() {
		return complement;
	}
	/**
	 * Modifie le complement de l'adresse
	 * @param complement <String>
	 */
	public void setComplement(String complement) {
		this.complement = complement;
	}
	
	/**
	 * Retourne le port
	 * @return port <int>
	 */
	public int getPort() {
		return port;
	}
	/**
	 * Modifie le port
	 * @param port <int>
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	//-----------------------------------
	// CONSTRUCTEURS
	
	/**
	 * Constructeur par défaut
	 */
	public Cameras() {}
	
	/**
	 * Construteur avec paramètres
	 * @param identifiant <Integer>
	 * @param id_client <Integer>
	 * @param nom <String>
	 * @param emplacement <String>
	 * @param ip <String>
	 * @param complement <String>
	 * @param port <Integer>
	 */
	public Cameras(int identifiant, int id_client, String nom, String emplacement, String ip, String complement, int port) {
		this.identifiant		= identifiant;
		this.id_client			= id_client;
		this.nom				= nom;
		this.emplacement		= emplacement;
		this.ip					= ip;
		this.complement			= complement;
		this.port				= port;
	}
}
