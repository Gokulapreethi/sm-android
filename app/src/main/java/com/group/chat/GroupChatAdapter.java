package com.group.chat;
//
//import java.util.Vector;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bean.GroupChatBean;
//import com.cg.account.R;
//import com.cg.commonclass.CallDispatcher;
//import com.cg.commonclass.SipNotificationListener;
//import com.cg.commonclass.WebServiceReferences;
//import com.cg.locations.buddyLocation;
//import com.chat.ChatBean;
//
//public class GroupChatAdapter extends ArrayAdapter<GroupChatBean> {
//
//
//	/*********** Declare Used Variables *********/
//	private Context context;
//	private Vector<GroupChatBean> chatList;
//	private LayoutInflater inflater = null;
//	ChatBean tempValues = null;
//	int i = 0;
//
//	/************* CustomAdapter Constructor *****************/
//	public GroupChatAdapter(Context context, Vector<GroupChatBean> chatList) {
//
//		super(context, R.layout.group_chat_row, chatList);
//		/********** Take passed values **********/
//		this.context = context;
//		this.chatList = chatList;
//
//		/*********** Layout inflator to call external xml layout () ***********/
//	}
//
//
//	public GroupChatBean getItem(int position) {
//		return chatList.get(position);
//	}
//
//	public long getItemId(int position) {
//		return position;
//	}
//
//	/******
//	 * Depends upon data size called for each row , Create each ListView row
//	 *****/
//	public View getView(final int position, View convertView,
//			ViewGroup parent) {
//		if (convertView == null) {
//			inflater = (LayoutInflater) context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = inflater.inflate(R.layout.group_chat_row, null);
//		}
//
//		if (chatList.size() != 0) {
//
//			final GroupChatBean gcBean = chatList.get(position);
//
//			TextView tv_user,message, dateTime;
//			// ImageView buddyIcon;
//			final ImageView multimediaIcon, locationIcon;
//			RelativeLayout senderLayout = (RelativeLayout) convertView
//					.findViewById(R.id.sender_view);
//			RelativeLayout receiverLayout = (RelativeLayout) convertView
//					.findViewById(R.id.receiver_view);
//			
//			
//			
//			if (gcBean.getFrom().equals(CallDispatcher.LoginUser)) {
//				
//				tv_user = (TextView) convertView.findViewById(R.id.receiver_user);
//				tv_user.setVisibility(View.GONE);
//				message = (TextView) convertView
//						.findViewById(R.id.receiver_text_msg);
//				dateTime = (TextView) convertView
//						.findViewById(R.id.receiver_datetime);
//				// buddyIcon = (ImageView) convertView
//				// .findViewById(R.id.receiver_buddy_icon);
//				// if (receiverImageBitmap != null)
//				// buddyIcon.setImageBitmap(receiverImageBitmap);
//				multimediaIcon = (ImageView) convertView
//						.findViewById(R.id.receiver_multi_msg);
//				locationIcon = (ImageView) convertView
//						.findViewById(R.id.receiver_loc_icon);
//				senderLayout.setVisibility(View.GONE);
//				receiverLayout.setVisibility(View.VISIBLE);
//
//			} else {
//				
//				tv_user = (TextView) convertView.findViewById(R.id.sender_user);
//				message = (TextView) convertView
//						.findViewById(R.id.sender_text_msg);
//				multimediaIcon = (ImageView) convertView
//						.findViewById(R.id.sender_multi_msg);
//				dateTime = (TextView) convertView
//						.findViewById(R.id.sender_datetime);
//				
//				locationIcon = (ImageView) convertView
//						.findViewById(R.id.sender_loc_icon);
//				receiverLayout.setVisibility(View.GONE);
//				senderLayout.setVisibility(View.VISIBLE);
//				
//				tv_user.setText(gcBean.getFrom());
//
//			}
//			if (gcBean.getMimetype().equals("text")) {
//				message.setVisibility(View.VISIBLE);
//				multimediaIcon.setVisibility(View.GONE);
//				message.setText(gcBean.getMessage());
//				
//				Log.d("GROUP_CHAT","message :"+gcBean.getMessage());
//				
//				if (gcBean.getMessage().startsWith("Latitude:")) {
//					locationIcon.setVisibility(View.VISIBLE);
//					locationIcon.setTag(gcBean.getMessage());
//				} else {
//					locationIcon.setVisibility(View.GONE);
//				}
//				
//			}else {
//				message.setVisibility(View.GONE);
//				multimediaIcon.setVisibility(View.VISIBLE);
//				if(gcBean.getMimetype().equalsIgnoreCase("image")){
//					
//				}else{
//					Bitmap bitMap = null;
//					if (gcBean.getMimetype().equalsIgnoreCase("audio"))
//						bitMap = callDisp.Resizeresource(
//								R.drawable.btn_audio, context);
//					else
//						bitMap = callDisp.Resizeresource(
//								R.drawable.btn_video, context);
//					multimediaIcon.setImageBitmap(bitMap);
//					multimediaIcon.getLayoutParams().width = 100;
//					multimediaIcon.getLayoutParams().height = 100;
//				}
//			}
//
//			
//			dateTime.setText(gcBean.getSenttime());
//
//			locationIcon.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (v.getTag().toString() != null) {
//						Intent intent = new Intent(context,
//								buddyLocation.class);
//						String locs[] = ((CallDispatcher) WebServiceReferences.callDispatch
//								.get("calldisp")).getBuddyLocation(v
//								.getTag().toString());
//						intent.putExtra("latitude", locs[0]);
//						intent.putExtra("longitude", locs[1]);
//						SipNotificationListener.getCurrentContext()
//								.startActivity(intent);
//					}
//				}
//			});
//			
//
//		}
//		return convertView;
//	}
//
//	/********* Called when Item click in ListView ************/
//	private class OnItemClickListener implements OnClickListener {
//		private int mPosition;
//
//		OnItemClickListener(int position) {
//			mPosition = position;
//		}
//
//		@Override
//		public void onClick(View arg0) {
//
//		}
//	}
//
//
//}
