package org.securipi.securipi;

/**
 * Objet Client
 */
public class Clients {
	// ATTRIBUTS

	private int identifiant;
	private String nom, prenom, login, password;

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
	 * Retourne le nom du client
	 * @return nom <String>
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * Modifie le nom du client
	 * @param nom <String>
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Retourne le prénom du client
	 * @return prenom <String>
	 */
	public String getPrenom() {
		return prenom;
	}
	/**
	 * Modifie le prénom du client
	 * @param prenom <String>
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	/**
	 * Retourne l'identifiant de connexion du client
	 * @return login <String>
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * Modifie le l'identifiant de connexion du client
	 * @param login <String>
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * Retourn le mot de passe du client
	 * @return password <String>
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Modifie le mot de passe du client
	 * @param password <String>
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	//-----------------------------------
	// CONSTRUCTEURS

	/**
	 * Constructeur par défaut
	 */
	public Clients() {}

	/**
	 * Constructeur avec paramètres
	 * @param identifiant <Integer>
	 * @param nom <String>
	 * @param prenom <String>
	 * @param login <String>
	 * @param password <String>
	 */
	public Clients(int identifiant, String nom, String prenom, String login, String password) {
		this.identifiant		= identifiant;
		this.nom				= nom;
		this.prenom				= prenom;
		this.login				= login;
		this.password			= password;
	}
}
