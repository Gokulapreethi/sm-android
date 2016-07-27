package com.group;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.rounding.RoundingGroupActivity;
import com.cg.snazmed.R;
import com.cg.commonclass.WebServiceReferences;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;

public class BuddyAdapter extends ArrayAdapter<UserBean> {

	/*********** Declare Used Variables *********/
	private Context context;
	private List<UserBean> userList;
	private Vector<UserBean> originallist;
	private LayoutInflater inflater = null;
	public int checkBoxCounter = 0;
	private int checkboxcount;
	private ImageLoader imageLoader;
	AddGroupMembers addGroupMembers;

	public void setCheckcount(int checkboxcount) {
		this.checkboxcount = checkboxcount;
	}

	public int getCheckcount() {

		return checkboxcount;
	}

    /************* CustomAdapter Constructor *****************/
	public BuddyAdapter(Context context, List<UserBean> userList) {

		super(context, R.layout.find_people_item, userList);
		/********** Take passed values **********/
		this.context = context;
		this.userList = new Vector<UserBean>();
		this.userList.addAll(userList);
		this.originallist = new Vector<UserBean>();
		this.originallist.addAll(userList);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(context);
		addGroupMembers = (AddGroupMembers) WebServiceReferences.contextTable
				.get("groupcontact");

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

			holder = new ViewHolder();
			if(convertView == null) {
				inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.find_people_item, null);
				holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy);
				holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
				holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
				holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
				holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
				holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
				holder.cancel_lay = (LinearLayout) convertView.findViewById(R.id.cancel_lay);
				convertView.setTag(holder);
			}else
				holder = (ViewHolder) convertView.getTag();
			final UserBean userBean = userList.get(position);
			if(userBean!=null) {
				if(userBean.getFirstname()!=null&&userBean.getFirstname().length()>1)
				holder.buddyName.setText(userBean.getFirstname());
				else
					holder.buddyName.setText(userBean.getBuddyName());
				if (userBean.isSelected()) {
					holder.selectUser.setChecked(true);
				} else {
					holder.selectUser.setChecked(false);
				}
//				holder.selectUser.setChecked(checkBoxState[position]);
				if(userBean.isFromchat())
					holder.statusIcon.setVisibility(View.GONE);
				else
					holder.statusIcon.setVisibility(View.VISIBLE);
				if (userBean.isOwner())
					holder.occupation.setText("Owner");
				else
					holder.occupation.setText("Prof.Designation");
				if(userBean.getInvite()){
					holder.selectUser.setVisibility(View.GONE);
					holder.cancel_lay.setVisibility(View.VISIBLE);
					holder.occupation.setText("invite Sent");
					holder.statusIcon.setVisibility(View.GONE);
					holder.header_title.setVisibility(View.GONE);
				}
				if (userBean.getProfilePic() != null) {
					Log.i("AAAA","Buddy adapter profile "+userBean.getProfilePic());
					String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
							+ "/COMMedia/" + userBean.getProfilePic();
					File pic = new File(pic_Path);
					if (pic.exists()) {
						imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
					}
				}
				if(userBean.getStatus()!=null) {
					Log.i("AAAA","Buddy adapter status "+userBean.getStatus());
					if (userBean.getStatus().equalsIgnoreCase("offline") || userBean.getStatus().equalsIgnoreCase("stealth")) {
						holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
					} else if (userBean.getStatus().equalsIgnoreCase("online")) {
						holder.statusIcon.setBackgroundResource(R.drawable.online_icon);
					} else if (userBean.getStatus().equalsIgnoreCase("busy")|| userBean.getStatus().equalsIgnoreCase("airport")) {
						holder.statusIcon.setBackgroundResource(R.drawable.busy_icon);
					} else if (userBean.getStatus().equalsIgnoreCase("away")) {
						holder.statusIcon.setBackgroundResource(R.drawable.invisibleicon);
					} else {
						holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
					}
				}
			}
			if (!userBean.isAllowChecking()) {
				holder.selectUser.setVisibility(View.GONE);
				holder.selectUser.setChecked(true);
				holder.selectUser.setEnabled(false);
			} else {

				holder.selectUser.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (((CheckBox) v).isChecked()) {
							userBean.setSelected(true);
							checkBoxCounter++;
							if (addGroupMembers != null) {
								addGroupMembers.countofcheckbox(checkBoxCounter);
							}
						} else {
							userBean.setSelected(false);
							checkBoxCounter--;
							if (addGroupMembers != null) {
								addGroupMembers.countofcheckbox(checkBoxCounter);
							}
						}
					}
				});
//
			}
			holder.cancel_lay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (userList.size() > 1) {
						if (userBean.getGroupid() != null) {
							GroupBean gBean = DBAccess.getdbHeler().getGroupAndMembers(
									"select * from groupdetails where groupid=" + userBean.getGroupid());
							gBean.setDeleteGroupMembers(userBean.getBuddyName());
							gBean.setGroupName(userBean.getGroupname());
							if (gBean.getGrouptype() != null) {
								Log.i("AAAA", "group -----" + gBean.getGrouptype());
								if (gBean.getGrouptype().equalsIgnoreCase("Rounding")) {
									RoundingGroupActivity roundingGroup = (RoundingGroupActivity) SingleInstance.contextTable
											.get("roundingGroup");
									if (roundingGroup != null)
										roundingGroup.showprogress();
									WebServiceReferences.webServiceClient.createRoundingGroup(gBean, roundingGroup);
								}
							} else {
								RoundingGroupActivity roundingGroup = (RoundingGroupActivity) SingleInstance.contextTable
										.get("roundingGroup");
								if (roundingGroup != null) {
									roundingGroup.membersList.remove(userBean);
									roundingGroup.refreshMembersList();
								} else {
									GroupActivity groupActivity = (GroupActivity) SingleInstance.contextTable
											.get("groupActivity");
									if (groupActivity != null)
										groupActivity.showprogress();
									WebServiceReferences.webServiceClient.createGroup(gBean, groupActivity);
								}

							}
						}
					}else
						Toast.makeText(context, "Minimum two members is needed to be in the group...", Toast.LENGTH_SHORT).show();
				}
			});

			return convertView;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}


	}

	class ViewHolder {
		CheckBox selectUser;
		ImageView buddyicon;
		ImageView statusIcon;
		TextView buddyName;
		TextView occupation;
		TextView header_title;
		LinearLayout cancel_lay;
	}
	private  GroupFilter filter;
	@Override
	public Filter getFilter() {
		if (filter == null){
			filter  = new GroupFilter();
		}
		return filter;
	}

	private class GroupFilter extends Filter
	{

		@Override
		protected Filter.FilterResults performFiltering(CharSequence constraint) {

			constraint = constraint.toString().toLowerCase();

			Filter.FilterResults result = new Filter.FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				Vector<UserBean> uBeans = new Vector<UserBean>();
				for(int i = 0, l = originallist.size(); i < l; i++)
				{
					UserBean gBean = originallist.get(i);
					if(gBean.getFirstname()!=null)
					if(gBean.getFirstname().toLowerCase().contains(String.valueOf(constraint)))
						uBeans.add(gBean);
				}

				result.count = uBeans.size();
				result.values = uBeans;
				if(uBeans.size()==0)
					addGroupMembers.showToast("No results found");
			} else {
				synchronized (this) {
					result.values = originallist;
					result.count = originallist.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
									  Filter.FilterResults results) {

			userList= (Vector<UserBean>)results.values;
			notifyDataSetChanged();
			clear();
			for(int i = 0, l = userList.size(); i < l; i++)
				add(userList.get(i));
			notifyDataSetInvalidated();

		}

	}

}