package com.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.WebServiceReferences;

public class AutoCallAdapter extends ArrayAdapter<UserBean> {

	/*********** Declare Used Variables *********/
	private List<UserBean> userList;
	private LayoutInflater inflater = null;

	/************* CustomAdapter Constructor *****************/
	public AutoCallAdapter(Context context, List<UserBean> userList) {

		super(context, R.layout.contactconference, userList);
		/********** Take passed values **********/
		this.userList = userList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		/*********** Layout inflator to call external xml layout () ***********/

	}

	public UserBean getItem(int position) {
		return userList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/******
	 * Depends upon data size called for each row , Create each ListView row
	 *****/
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			ViewHolder holder;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.contactconference,
						null, false);
				holder = new ViewHolder();
				holder.tv_buddyName = (TextView) convertView
						.findViewById(R.id.buddy_name);
				holder.tv_identity = (TextView) convertView
						.findViewById(R.id.tv_identity);
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.buddycheck);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setVisibility(View.VISIBLE);
			final UserBean userBean = userList.get(position);

			holder.tv_buddyName.setText(userBean.getBuddyName());

			if (userBean.isOwner())
				holder.tv_identity.setVisibility(View.VISIBLE);
			else
				holder.tv_identity.setVisibility(View.GONE);

			holder.checkbox.setOnCheckedChangeListener(null);

			holder.checkbox.setChecked(userBean.isSelected());
			if (WebServiceReferences.contextTable.containsKey("Autocall")) {
				if (userBean != null) {
					if (userBean.getFlag().equals("0")) {
						holder.checkbox.setChecked(false);
					} else {
						holder.checkbox.setChecked(true);
					}
				}

			}
			if (!userBean.isAllowChecking()) {
				holder.checkbox.setChecked(true);
				// holder.checkbox.setEnabled(false);
			} else {
				holder.checkbox
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton arg0,
									boolean isChecked) {

								if (isChecked) {
									userBean.setSelected(true);
									if (WebServiceReferences.contextTable
											.containsKey("Autocall")) {

										DBAccess.getdbHeler()
												.Updateautoacceptcalls(
														userBean, "1");
									}
								} else {
									userBean.setSelected(false);
									if (WebServiceReferences.contextTable
											.containsKey("Autocall")) {
										final UserBean userBean = userList
												.get(position);
										DBAccess.getdbHeler()
												.Updateautoacceptcalls(
														userBean, "0");

									}

								}
							}

						});
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
		CheckBox checkbox;
	}
}