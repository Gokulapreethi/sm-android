package com.chat;


//public class ChatAdapter extends ArrayAdapter<ChatBean> {
//
//	/*********** Declare Used Variables *********/
//	private Context context;
//	private Vector<ChatBean> chatList;
//	private LayoutInflater inflater = null;
//	ChatBean tempValues = null;
//	int i = 0;
//	private CallDispatcher callDisp = null;
//	private Bitmap senderImageBitmap = null;
//	private Bitmap receiverImageBitmap = null;
//
//	/************* CustomAdapter Constructor *****************/
//	public ChatAdapter(Context context, Vector<ChatBean> chatList) {
//
//		super(context, R.layout.chat_view, chatList);
//		/********** Take passed values **********/
//		this.context = context;
//		this.chatList = chatList;
//
//		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
//			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
//					.get("calldisp");
//		else
//			callDisp = new CallDispatcher(context);
//
//		/*********** Layout inflator to call external xml layout () ***********/
//
//	}
//
//	/******** What is the size of Passed Arraylist Size ************/
//	public int getCount() {
//
//		if (chatList.size() <= 0)
//			return 1;
//		return chatList.size();
//	}
//
//	public ChatBean getItem(int position) {
//		return chatList.get(position);
//	}
//
//	public long getItemId(int position) {
//		return position;
//	}
//
//	/****** Depends upon data size called for each row , Create each ListView row *****/
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		if (convertView == null) {
//			inflater = (LayoutInflater) context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = inflater.inflate(R.layout.chat_view, null);
//		}
//
//		if (chatList.size() != 0) {
//
//			final ChatBean chatBean = chatList.get(position);
//
//			TextView message, dateTime;
//			ImageView buddyIcon;
//			final ImageView multimediaIcon, locationIcon;
//			RelativeLayout senderLayout = (RelativeLayout) convertView
//					.findViewById(R.id.sender_view);
//			RelativeLayout receiverLayout = (RelativeLayout) convertView
//					.findViewById(R.id.receiver_view);
//			if (chatBean.isSender()) {
//				message = (TextView) convertView
//						.findViewById(R.id.sender_text_msg);
//				multimediaIcon = (ImageView) convertView
//						.findViewById(R.id.sender_multi_msg);
//				dateTime = (TextView) convertView
//						.findViewById(R.id.sender_datetime);
//				buddyIcon = (ImageView) convertView
//						.findViewById(R.id.sender_buddy_icon);
//				locationIcon = (ImageView) convertView
//						.findViewById(R.id.sender_loc_icon);
//				receiverLayout.setVisibility(View.GONE);
//				senderLayout.setVisibility(View.VISIBLE);
//
//			} else {
//				message = (TextView) convertView
//						.findViewById(R.id.receiver_text_msg);
//				dateTime = (TextView) convertView
//						.findViewById(R.id.receiver_datetime);
//				buddyIcon = (ImageView) convertView
//						.findViewById(R.id.receiver_buddy_icon);
//				multimediaIcon = (ImageView) convertView
//						.findViewById(R.id.receiver_multi_msg);
//				locationIcon = (ImageView) convertView
//						.findViewById(R.id.receiver_loc_icon);
//				senderLayout.setVisibility(View.GONE);
//				receiverLayout.setVisibility(View.VISIBLE);
//
//			}
//			if (chatBean.getMessageType().equalsIgnoreCase("MTP")) {
//				message.setVisibility(View.VISIBLE);
//				multimediaIcon.setVisibility(View.GONE);
//				message.setText(chatBean.getMessage());
//				if (chatBean.getMessage().startsWith("Latitude:")) {
//					locationIcon.setVisibility(View.VISIBLE);
//					locationIcon.setTag(chatBean.getMessage());
//				} else {
//					locationIcon.setVisibility(View.GONE);
//				}
//			} else {
//				multimediaIcon.setVisibility(View.VISIBLE);
//				Log.i("chat", "caht 123 file path " + chatBean.getMessage());
//				if (chatBean.getMessageType().equalsIgnoreCase("MPP")
//						|| chatBean.getMessageType().equalsIgnoreCase("MHP")) {
//					Bitmap bitMap = callDisp.ResizeImage(chatBean.getMessage());
//					multimediaIcon.setImageBitmap(bitMap);
//				} else {
//					Bitmap bitMap = callDisp.Resizeresource(
//							R.drawable.btn_play_new, context);
//					multimediaIcon.setImageBitmap(bitMap);
//					multimediaIcon.getLayoutParams().width = 100;
//					multimediaIcon.getLayoutParams().height = 100;
//				}
//				multimediaIcon.setTag(chatBean.getMessage());
//				multimediaIcon.setContentDescription(chatBean.getMessageType());
//				message.setVisibility(View.GONE);
//				locationIcon.setVisibility(View.GONE);
//				// Bitmap bmp = ((BitmapDrawable) multimediaIcon.getDrawable())
//				// .getBitmap();
//				// if (bmp != null && !bmp.isRecycled()) {
//				// Log.i("chat", "chat :: " + bmp);
//				// }
//				// bmp.recycle();
//			}
//
//			dateTime.setText(chatBean.getTimeDate());
//
//			multimediaIcon.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					File file = new File(v.getTag().toString());
//
//					if (file.exists()) {
//						if (v.getContentDescription().toString()
//								.equalsIgnoreCase("MAP")) {
//							Intent intent = new Intent(context,
//									MultimediaUtils.class);
//							intent.putExtra("filePath", v.getTag().toString());
//							intent.putExtra("action", "audio");
//							intent.putExtra("createOrOpen", "open");
//							SipNotificationListener.getCurrentContext()
//									.startActivity(intent);
//						} else if (v.getContentDescription().toString()
//								.equalsIgnoreCase("MVP")) {
//							Intent intentVPlayer = new Intent(context,
//									VideoPlayer.class);
//							intentVPlayer.putExtra("File_Path", v.getTag()
//									.toString());
//							intentVPlayer.putExtra("Player_Type",
//									"Video Player");
//							intentVPlayer.putExtra("time", 0);
//							SipNotificationListener.getCurrentContext()
//									.startActivity(intentVPlayer);
//						} else if (v.getContentDescription().toString()
//								.equalsIgnoreCase("MPP")
//								|| v.getContentDescription().toString()
//										.equalsIgnoreCase("MHP")) {
//							Intent in = new Intent(context,
//									PhotoZoomActivity.class);
//							in.putExtra("Photo_path", v.getTag().toString());
//							in.putExtra("type", "true");
//							SipNotificationListener.getCurrentContext()
//									.startActivity(in);
//						}
//					}
//				}
//			});
//
//			locationIcon.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (v.getTag().toString() != null) {
//						Intent intent = new Intent(context, buddyLocation.class);
//						String locs[] = callDisp.getBuddyLocation(v.getTag()
//								.toString());
//						intent.putExtra("latitude", locs[0]);
//						intent.putExtra("longitude", locs[1]);
//						SipNotificationListener.getCurrentContext()
//								.startActivity(intent);
//					}
//				}
//			});
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
//}