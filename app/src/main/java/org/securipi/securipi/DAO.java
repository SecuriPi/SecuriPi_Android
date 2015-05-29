package org.securipi.securipi;

import java.io.IOException;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.util.Log;

/**
 * Objet d'accès aux données
 */
public class DAO {
	
	private HttpPost httpPost;
	private HttpResponse response;
	private HttpClient httpClient;
	private ProgressDialog dialog = null;
	
	/**
	 * Constructeur par défaut
	 */
	public DAO() {}
	
	/**
	 * Retourne la liste de clients correspondant à la chaîne encodée en JSON
	 * @param jsonString <String>
	 * @return listClients <ArrayList<Client>>
	 */
	public ArrayList<Clients> getListClients(String jsonString) {
		//On créer une liste vide pour y ajouter des clients par la suite
		ArrayList<Clients> listClients = new ArrayList<Clients>();
		
		try {
			//On transforme la chaîne encodée en JSON, en une variable de type tableau JSON
			JSONArray tabJson = new JSONArray(jsonString);
			
			//Pour chaque ligne du tableau JSON, on va ajouter un client à la liste à partir des données du tableau
			for(int i = 0 ; i < tabJson.length() ; i++) {
				JSONObject objJson = tabJson.getJSONObject(i);
				
				listClients.add(new Clients(objJson.getInt("ID"), objJson.getString("Nom"), objJson.getString("Prenom"), objJson.getString("Login"), objJson.getString("Password")));
			}
		} catch (JSONException e){ }
		return listClients;
	}
	
	/**
	 * Retourne la chaine JSON de la liste des clients
	 * @param listClients <ArrayList<Clients>>
	 * @return strJson <String>
	 */
	public String getJsonClients(ArrayList<Clients> listClients) {
		
		JSONArray jsonArray = new JSONArray();
		
		for(Clients c : listClients) {
			try {
				JSONObject jsonObj = new JSONObject();

				jsonObj.put("ID", c.getIdentifiant());
				jsonObj.put("Nom", c.getNom());
				jsonObj.put("Prenom", c.getPrenom());
				jsonObj.put("Login", c.getLogin());
				jsonObj.put("Password", c.getPassword());

				jsonArray.put(jsonObj);
			} catch(Exception e) {}
		}
		
		Log.d("JSON", jsonArray.toString());
		return jsonArray.toString();
	}
	
	/**
	 * Upload la liste des clients dans la base distante
	 * @param listClients <ArrayList<Clients>>
	 */
	public void setClients(ArrayList<Clients> listClients) {
		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://172.16.69.21/Scripts/Android/setClients.php");
			
			// Add your data  
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
			nameValuePairs.add(new BasicNameValuePair("jsonString", getJsonClients(listClients))); 
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
			
			response = httpClient.execute(httpPost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);
		} catch (IOException e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
	
	/**
	 * Retourne la liste des cameras correspondant à la chaîne encodée en JSON
	 * @param jsonString <String>
	 * @return listCameras <ArrayList<Cameras>>
	 */
	public ArrayList<Cameras> getListCameras(String jsonString) {
		//On créer une liste vide pour y ajouter des clients par la suite
		ArrayList<Cameras> listCameras = new ArrayList<Cameras>();
		
		try {
			//On transforme la chaîne encodée en JSON, en une variable de type tableau JSON
			JSONArray tabJson = new JSONArray(jsonString);
			
			//Pour chaque ligne du tableau JSON, on va ajouter un client à la liste à partir des données du tableau
			for(int i = 0 ; i < tabJson.length() ; i++) {
				JSONObject objJson = tabJson.getJSONObject(i);
				
				if(objJson.isNull("Port")) {
					listCameras.add(new Cameras(objJson.getInt("ID"), objJson.getInt("ID_Client"), objJson.getString("Nom"), objJson.getString("Emplacement"), objJson.getString("IP"), objJson.getString("Complement"), 0));
				} else {
					listCameras.add(new Cameras(objJson.getInt("ID"), objJson.getInt("ID_Client"), objJson.getString("Nom"), objJson.getString("Emplacement"), objJson.getString("IP"), objJson.getString("Complement"), objJson.getInt("Port")));
				}
			}
		} catch (JSONException e){
			Log.d("Exception", e.getMessage()); 
		}
		return listCameras;
	}
	
	/**
	 * Retourne la chaine JSON de la liste des cameras
	 * @param listCameras <ArrayList<Cameras>>
	 * @return strJson <String>
	 */
	public String getJsonCameras(ArrayList<Cameras> listCameras) {
		JSONArray jsonArray = new JSONArray();
		
		for(Cameras c : listCameras) {
			try {
				JSONObject jsonObj = new JSONObject();

				jsonObj.put("ID", c.getIdentifiant());
				jsonObj.put("ID_Client", c.getID_Client());
				jsonObj.put("Nom", c.getNom());
				jsonObj.put("Emplacement", c.getEmplacement());
				jsonObj.put("IP", c.getIP());
				jsonObj.put("Complement", c.getComplement());
				jsonObj.put("Port", c.getPort());

				jsonArray.put(jsonObj);
			} catch(Exception e) {}
		}
		
		Log.d("JSON", jsonArray.toString());
		return jsonArray.toString();
	}
	
	/**
	 * Upload la liste des cameras dans la base distante
	 * @param listCameras <ArrayList<Cameras>>
	 */
	public void setCameras(ArrayList<Cameras> listCameras) {
		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://172.16.69.21/Scripts/Android/setCameras.php");
			
			// Add your data  
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
			nameValuePairs.add(new BasicNameValuePair("jsonString", getJsonCameras(listCameras))); 
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
			
			response = httpClient.execute(httpPost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);
		} catch (IOException e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
	
	/**
	 * Retourne la liste des connexions correspondant à la chaîne encodée en JSON
	 * @param jsonString <String>
	 * @return listConnexions <ArrayList<Connexion>>
	 */
	public ArrayList<Connexions> getListConnexions(String jsonString) {
		//On créer une liste vide pour y ajouter des clients par la suite
		ArrayList<Connexions> listConnexions = new ArrayList<Connexions>();
		
		try {
			//On transforme la chaîne encodée en JSON, en une variable de type tableau JSON
			JSONArray tabJson = new JSONArray(jsonString);
			
			//Pour chaque ligne du tableau JSON, on va ajouter un client à la liste à partir des données du tableau
			for(int i = 0 ; i < tabJson.length() ; i++) {
				JSONObject objJson = tabJson.getJSONObject(i);
				
				listConnexions.add(new Connexions(objJson.getInt("ID"), objJson.getInt("ID_Client"), objJson.getString("Date"), objJson.getString("Heure"), objJson.getString("IP")));
			}
		} catch (JSONException e){ }
		return listConnexions;
	}
	
	/**
	 * Retourne la chaine JSON de la liste des connexions
	 * @param listConnexions <ArrayList<Connexion>>
	 * @return strJson <String>
	 */
	public String getJsonConnexions(ArrayList<Connexions> listConnexions) {
		
		JSONArray jsonArray = new JSONArray();
		
		for(Connexions c : listConnexions) {
			try {
				JSONObject jsonObj = new JSONObject();

				jsonObj.put("ID", c.getIdentifiant());
				jsonObj.put("ID_Client", c.getID_Client());
				jsonObj.put("Date", c.getDate());
				jsonObj.put("Heure", c.getHeure());
				jsonObj.put("IP", c.getIP());

				jsonArray.put(jsonObj);
			} catch(Exception e) {}
		}
		
		return jsonArray.toString();
	}
	
	/**
	 * Upload la liste des connexions dans la base distante
	 * @param listConnexions <ArrayList<Connexion>>
	 */
	public void setConnexions(ArrayList<Connexions> listConnexions) {
		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://172.16.69.21/Scripts/Android/setConnexions.php");
			
			// Add your data  
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
			nameValuePairs.add(new BasicNameValuePair("jsonString", getJsonConnexions(listConnexions))); 
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
			
			response = httpClient.execute(httpPost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);
		} catch (IOException e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
}
