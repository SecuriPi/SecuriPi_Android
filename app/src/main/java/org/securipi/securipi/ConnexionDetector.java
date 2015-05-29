package org.securipi.securipi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Classe de dectection de l'état des connectivités réseaux de l'appreil mobile
 */
public class ConnexionDetector {
	private Context _context;

	/**
	 * Constructeur avec paramètres
	 * @param context <Contect>
	 */
    public ConnexionDetector(Context context) {
        this._context = context;
    }

    /**
     * Retourne l'état de la connexion internet
     * @return true si active / false sinon <boolean>
     */
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
