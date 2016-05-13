package com.exchanges;

import java.util.Vector;

import org.lib.model.GroupBean;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

public class ExchangesAdapter extends ArrayAdapter<GroupBean> {

	private Vector<GroupBean> groupList = null;
	private Context context = null;
	private CallDispatcher callDisp;
	private ImageLoader imageLoader;
	private Typeface tf_regular = null;
	private static TextView lastMsg;
	private String setLastMsg;





	private Typeface tf_bold = null;

	public ExchangesAdapter(Context context, Vector<GroupBean> groupList) {

		super(context, R.layout.exchangesadapter, groupList);
		this.groupList = groupList;
		this.context = context;
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		imageLoader = new ImageLoader(context.getApplicationContext());

		// TODO Auto-generated constructor stub



}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.exchangesadapter, null,
					false);
		}
		tf_regular = Typeface.createFromAsset(context.getAssets(),
				"fonts/ARIAL.TTF");
		tf_bold = Typeface.createFromAsset(context.getAssets(),
				"fonts/ARIALBD.TTF");

		RelativeLayout container = (RelativeLayout) convertView
				.findViewById(R.id.listcontainer);
		TextView tv_exhangeName = (TextView) convertView
				.findViewById(R.id.exchangetitle);
		tv_exhangeName.setTypeface(tf_regular);
		ImageView exchangeImage = (ImageView) convertView
				.findViewById(R.id.exchangeicon);
		 lastMsg = (TextView) convertView
				.findViewById(R.id.exchangemsg);
		Button count1 = (Button) convertView.findViewById(R.id.count1);
		Button count2 = (Button) convertView.findViewById(R.id.count2);
		Button count3 = (Button) convertView.findViewById(R.id.count3);
		Button count4 = (Button) convertView.findViewById(R.id.count4);
		if (groupList.size() > 0) {
			Object obj = getItem(position);

			if (obj instanceof GroupBean) {
				final GroupBean groupBean = (GroupBean) getItem(position);
				tv_exhangeName.setText(groupBean.getGroupName());
				if (groupBean.getCategory().equalsIgnoreCase("G")) {
					imageLoader.DisplayImage("", exchangeImage,
							R.drawable.group_conf);
					lastMsg.setText(groupBean.getLastMsg());
				//	int groupCount = DBAccess.getdbHeler().getUnreadMsgCount(
					//		CallDispatcher.LoginUser);
//					if(groupCount>0){
					if (groupBean.getMessageCount() > 0) {
						count1.setVisibility(View.VISIBLE);
						count1.setText(String.valueOf(groupBean
								.getMessageCount()));
					} else {
						count1.setVisibility(View.INVISIBLE);
					}

					if (SingleInstance.deadLineMsgCount.containsKey(groupBean
							.getGroupId())) {
						count2.setVisibility(View.VISIBLE);
//						count2.setText("@");
					} else {
						count2.setVisibility(View.INVISIBLE);

					}
				} else if (groupBean.getCategory().equalsIgnoreCase("I")) {
					if (groupBean.getGroupProfilePic() != null
							&& groupBean.getGroupProfilePic().length() > 0)
						imageLoader.DisplayImage(
								groupBean.getGroupProfilePic(), exchangeImage,
								R.drawable.icon_buddy_aoffline);
					int groupCount = DBAccess.getdbHeler().getUnreadMsgCount(
							CallDispatcher.LoginUser);
					if(groupCount>0){
					if (groupBean.getMessageCount() > 0) {
						count1.setVisibility(View.VISIBLE);
						count1.setText(String.valueOf(groupBean
								.getMessageCount()));
					} 
					}else {
						count1.setVisibility(View.INVISIBLE);
					}
					
					if (SingleInstance.deadLineMsgCount.containsKey(groupBean
							.getGroupId())) {
						count2.setVisibility(View.VISIBLE);
//						count2.setText("@");
					} else {
						count2.setVisibility(View.INVISIBLE);

					}
				}
				if (groupBean.getLastMsg() != null
						&& !groupBean.getLastMsg().equalsIgnoreCase("null")) {
					lastMsg.setText(groupBean.getLastMsg());
				}
			}
			// else if (obj instanceof ChatBean) {
			// ChatBean chatBean = (ChatBean) getItem(position);
			// tv_exhangeName.setText(chatBean.getFromUser());
			// Log.i("groupchat", "image :: " + chatBean.getProfilePath());
			// if (chatBean.getProfilePath() != null
			// && chatBean.getProfilePath().length() > 0)
			// imageLoader.DisplayImage(chatBean.getProfilePath(),
			// exchangeImage, R.drawable.icon_buddy_aoffline);
			//
			// if (SingleInstance.individualMsgUnreadCount
			// .containsKey(chatBean.getFromUser())) {
			// count1.setVisibility(View.VISIBLE);
			// count1.setText(String
			// .valueOf(SingleInstance.individualMsgUnreadCount
			// .get(chatBean.getFromUser())));
			// } else {
			// count1.setVisibility(View.INVISIBLE);
			// }
			// if (CallDispatcher.message_map.containsKey(chatBean
			// .getFromUser())) {
			// lastMsg.setText(CallDispatcher.message_map.get(chatBean
			// .getFromUser()));
			// }
			// }
		}
		return convertView;

	}
	public static void lastMsgClear()
	{
		Log.i("clear123","method clear msg");
		lastMsg.setText("");
		lastMsg.setVisibility(View.GONE);
	}
}
