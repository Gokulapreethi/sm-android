package com.group;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cg.snazmed.R;


public class ProfessionAdapter extends ArrayAdapter<String> {

	/*********** Declare Used Variables *********/
	private Context context;
	private List<String> userList;
	private LayoutInflater inflater = null;
	private int selectedIndex = -1;
	private String profession=null;

	/************* CustomAdapter Constructor *****************/
	public ProfessionAdapter(Context context, List<String> pList) {

		super(context, R.layout.profession_row,pList);
		/********** Take passed values **********/
		this.context = context;
		this.userList = pList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		/*********** Layout inflator to call external xml layout () ***********/

	}

	public String getItem(int position) {
		return userList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	public void setSelectedIndex(int index){
	    selectedIndex = index;
	}

	/******
	 * Depends upon data size called for each row , Create each ListView row
	 *****/
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			ViewHolder holder;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.profession_row,
						null, false);
				holder = new ViewHolder();
				holder.tv_buddyName = (TextView) convertView
						.findViewById(R.id.buddy_name);
				holder.tv_identity = (TextView) convertView
						.findViewById(R.id.tv_identity);
				holder.rbSelect = (RadioButton) convertView
						.findViewById(R.id.radioButton);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setVisibility(View.VISIBLE);
			profession = userList.get(position);
			holder.tv_buddyName.setText(profession);
			
                   if(selectedIndex == position){
                        holder.rbSelect.setChecked(true);
				   }
                  else{
                      holder.rbSelect.setChecked(false);
              }
			
			
	          
			
			return convertView;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	class ViewHolder {
		TextView tv_buddyName;
		TextView tv_identity;
		RadioButton rbSelect;
	}
}