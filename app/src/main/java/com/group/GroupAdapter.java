package com.group;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.model.GroupBean;

import java.util.Vector;

public class GroupAdapter extends ArrayAdapter<GroupBean> {

	private Context context;
	private Typeface tf_regular = null;

	private Typeface tf_bold = null;
	ImageLoader imageLoader;
	private Vector<GroupBean> grouplist;
	private Vector<GroupBean> originalList;
	private  GroupFilter filter;

	public GroupAdapter(Context context, int textViewResourceId,
			Vector<GroupBean> groupList) {

		super(context, R.layout.grouplist, groupList);
		this.context = context;
		grouplist = new Vector<GroupBean>();
		grouplist.addAll(ContactsFragment.getGroupList());
		originalList = new Vector<GroupBean>();
		this.originalList.addAll(ContactsFragment.getGroupList());
		imageLoader=new ImageLoader(SingleInstance.mainContext);

	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		View row = view;
		
		try {

			final ViewHolder holder;
		

			if (row == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) this.context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.grouplist, null, false);
				holder.listContainer = (LinearLayout) row
						.findViewById(R.id.list_container);
				holder.accept_lay = (LinearLayout) row
						.findViewById(R.id.ll_accept);
				holder.reject_lay = (LinearLayout) row
						.findViewById(R.id.ll_reject);
				holder.grouplist = (TextView) row.findViewById(R.id.group_name);
				holder.header_title = (TextView) row.findViewById(R.id.header_title);
				holder.members = (TextView) row.findViewById(R.id.members);
				holder.contact_history = (LinearLayout) row.findViewById(R.id.contact_history);
				holder.inreq = (LinearLayout) row.findViewById(R.id.inreq);
				holder.buddy_icon = (ImageView) row.findViewById(R.id.iv_icon);
				row.setTag(holder);
			} else {
				holder = (ViewHolder) row.getTag();
			}
			final GroupBean groupBean = (GroupBean) ContactsFragment.getGroupList().get(position);
				TextView tv_groupName = (TextView) row
						.findViewById(R.id.group_name);
				tv_groupName.setText(groupBean.getGroupName());
				if (tv_groupName == null) {
					holder.listContainer.setVisibility(View.GONE);
				}
				tv_groupName.setTypeface(tf_regular);
			holder.header_title.setVisibility(View.VISIBLE);
			String name="";
			if(groupBean.getStatus().equalsIgnoreCase("request"))
				holder.header_title.setText("REQUEST");
			else {
				name=String.valueOf(groupBean.getGroupName().charAt(0));
				holder.header_title.setText(name.toUpperCase());
			}
			if(position>0){
				GroupBean gBean=(GroupBean) ContactsFragment.getGroupList().get(position - 1);
				if(groupBean.getStatus().equalsIgnoreCase("request"))
					holder.header_title.setVisibility(View.GONE);
				else {
					String name2=String.valueOf(gBean.getGroupName().charAt(0));
					if(name.equalsIgnoreCase(name2))
						holder.header_title.setVisibility(View.GONE);
					else
						holder.header_title.setVisibility(View.VISIBLE);
				}
			}

			final GroupBean gBean = DBAccess.getdbHeler() .getGroupAndMembers(
					"select * from groupdetails where groupid=" + groupBean.getGroupId());

			if(gBean.getActiveGroupMembers()!=null && !gBean.getActiveGroupMembers().equalsIgnoreCase("")) {
				String[] mlist = (gBean.getActiveGroupMembers())
						.split(",");
				Log.i("AAA","Adapter getActiveGroupMembers "+mlist.length);
				int membercount = mlist.length + 1;
				holder.members.setText(Integer.toString(membercount) + " members");
			}else
				holder.members.setText(Integer.toString(1) + " members");
			if (groupBean.getStatus().equalsIgnoreCase("request")) {
				holder.inreq.setVisibility(View.VISIBLE);
			} else {
				holder.inreq.setVisibility(View.GONE);
			}


				if (groupBean.getMessageCount() > 0 && !groupBean.getStatus().equalsIgnoreCase("request")) {
					holder.contact_history.setVisibility(View.VISIBLE);
				} else {
					holder.contact_history.setVisibility(View.GONE);
				}
					if(groupBean.getGroupIcon()!=null) {
						Log.i("AAA","Adapter icon "+groupBean.getGroupIcon());
						imageLoader.DisplayImage(
								Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" +
										groupBean.getGroupIcon(),
								holder.buddy_icon,
								R.drawable.group_usericon);
					}

				holder.listContainer.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							if(groupBean.getStatus().equalsIgnoreCase("request")){
								GroupRequestFragment groupRequestFragment = GroupRequestFragment.newInstance(SingleInstance.mainContext);
								groupRequestFragment.setGroupBean(gBean);
								groupRequestFragment.setGroupName(groupBean.getGroupName());
								FragmentManager fragmentManager = SingleInstance.mainContext
										.getSupportFragmentManager();
								fragmentManager.beginTransaction().replace(
										R.id.activity_main_content_fragment, groupRequestFragment)
										.commitAllowingStateLoss();
							}else {
								if(!DBAccess.getdbHeler().ChatEntryAvailableOrNot(groupBean.getGroupId())) {
									ContactsFragment contactsFragment = ContactsFragment
											.getInstance(context);
									contactsFragment.showprogress();
									contactsFragment.chatsync_grouplist=true;
									contactsFragment.chatsync_groupbean=groupBean;
									AppReference.Beginsync_chat = true;
									WebServiceReferences.webServiceClient.ChatSync(CallDispatcher.LoginUser, SingleInstance.mainContext,"0",groupBean.getGroupId(),"","");
								}else {
									ContactsFragment contactsFragment = ContactsFragment
											.getInstance(context);
									contactsFragment.showGroupChatDialog(groupBean);
								}
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			holder.accept_lay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					GroupRequestFragment.newInstance(SingleInstance.mainContext).showDialog();
					WebServiceReferences.webServiceClient.AcceptRejectGroupmember(groupBean.getGroupId(), CallDispatcher.LoginUser, "1",SingleInstance.mainContext);
				}
			});
			holder.reject_lay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					GroupRequestFragment.newInstance(SingleInstance.mainContext).showDialog();
					WebServiceReferences.webServiceClient.AcceptRejectGroupmember(groupBean.getGroupId(), CallDispatcher.LoginUser, "0",SingleInstance.mainContext);
				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
	}

	public static class ViewHolder {
		LinearLayout listContainer,accept_lay,reject_lay;
		TextView grouplist,members,header_title;
		LinearLayout contact_history;
		LinearLayout inreq;
		ImageView buddy_icon;
	}
	public Filter getFilter() {
		if (filter == null){
			filter  = new GroupFilter();
		}
		return filter;
	}
	private class GroupFilter extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			constraint = constraint.toString().toLowerCase();

			FilterResults result = new FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				Vector<GroupBean> gBeans = new Vector<GroupBean>();
				for(int i = 0, l = originalList.size(); i < l; i++)
				{
					GroupBean groupBean = originalList.get(i);
					if(groupBean.getGroupName().toLowerCase().startsWith(String.valueOf(constraint)))
						gBeans.add(groupBean);
				}
				result.count = gBeans.size();
				result.values = gBeans;
			} else {
				synchronized (this) {
					result.values = originalList;
					result.count = originalList.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
									  FilterResults results) {

			grouplist= (Vector<GroupBean>)results.values;
			notifyDataSetChanged();
			clear();
			for(int i = 0, l = grouplist.size(); i < l; i++)
				add(grouplist.get(i));
			notifyDataSetInvalidated();

		}

	}

}
