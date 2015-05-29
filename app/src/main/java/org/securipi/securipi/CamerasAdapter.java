package org.securipi.securipi;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter de la liste des caméras
 */
public class CamerasAdapter extends BaseAdapter {
	
	/**
	 * Classe de déclaration des éléments de la vue
	 */
	private class ViewHolder {
		TextView vueNom;
		TextView vueEmplacement;
	}
	
	private List<Cameras> listCameras;
	private LayoutInflater layoutInflater;

	/**
	 * Constructeur
	 * @param context <Context>
	 * @param vListCameras <List<Cameras>>
	 */
	public CamerasAdapter(Context context, List<Cameras> vListCameras) {
		layoutInflater = LayoutInflater.from(context);
		listCameras = vListCameras;
	}

	/**
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.listCameras.size();
	}

	/**
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int id) {
		return this.listCameras.get(id);
	}

	/**
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int id) {
		return id;
	}

	/**
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = layoutInflater.inflate(R.layout.vuecameras, null);
			holder.vueNom = (TextView) convertView.findViewById(R.id.vueNom);
			holder.vueEmplacement = (TextView) convertView.findViewById(R.id.vueEmplacement);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.vueNom.setText(listCameras.get(position).getNom());
		holder.vueEmplacement.setText(listCameras.get(position).getEmplacement());
		
		if(position % 2 == 0) {
			convertView.setBackgroundColor(Color.rgb(230, 230, 230));
		}
		
		return convertView;
	}
}