package com.callHistory;

import java.util.ArrayList;

import org.lib.model.BuddyInformationBean;
import org.lib.model.RecordTransactionBean;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.TextNoteDatas;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.CompleteListBean;
import com.cg.files.CompleteListView;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.util.SingleInstance;

public class CallHistoryAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<RecordTransactionBean> p_list;
	public String[] text;
	private CallDispatcher callDisp;
	CallHistoryActivity OrderMenuActivity = null;
	ImageLoader imageLoader;
	private String ComponentPath = null;
	private CompleteListBean cbean = null;
	private CompleteListBean complBean = null;
	private static CallDispatcher calldisp = null;
	private ArrayList<String> uploadDatas = new ArrayList<String>();

	public CallHistoryAdapter(Context context,
			ArrayList<RecordTransactionBean> mlist) {
		this.context = context;
		this.p_list = mlist;
		calldisp = CallDispatcher.getCallDispatcher(context);
		imageLoader = new ImageLoader(context.getApplicationContext());
		OrderMenuActivity = (CallHistoryActivity) WebServiceReferences.contextTable
				.get("ordermenuactivity");
		}

	// public class ViewHolder {
	//
	// CollectionBean bean;
	// TextView date,center,process;
	// LayoutInflater inflater;
	// LinearLayout button;
	// }

	public View getView(int position, View view, ViewGroup parent) {
		View rowView = null;
		Log.d("Test","GetView CH Adapter ________+++++"+position);

		try {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.callhistoryadapter, null);

			TextView calltype = (TextView) rowView
					.findViewById(R.id.calltype);
//			TextView callcontent = (TextView) rowView
//					.findViewById(R.id.ccontent_title);
//			TextView from = (TextView) rowView.findViewById(R.id.from_title);
			TextView to = (TextView) rowView.findViewById(R.id.to_calluser);
			TextView date = (TextView) rowView.findViewById(R.id.datetime);
//			TextView duration = (TextView) rowView
//					.findViewById(R.id.duration_title);
//			TextView callstate = (TextView) rowView
//					.findViewById(R.id.cstate_title);

//			ImageView valueimage = (ImageView) rowView
//					.findViewById(R.id.value_img);
//			Button accept = (Button) rowView.findViewById(R.id.acceptbtn);
//			Button rejet = (Button) rowView.findViewById(R.id.rejectbtn);
//			Button reply = (Button) rowView.findViewById(R.id.replybtn);
//			ImageView preview = (ImageView) rowView
//					.findViewById(R.id.value_img);
		
			RecordTransactionBean recordTransactionBean = p_list.get(position);

			Log.i("CH", "recordTransactionBean.getCalltype() :"
					+ recordTransactionBean.getCalltype());
			Log.i("CH", "recordTransactionBean.getFromName() :"
					+ recordTransactionBean.getFromName());
			Log.i("CH", "recordTransactionBean.getToName() :"
					+ recordTransactionBean.getToName());
			Log.i("CH", "recordTransactionBean.getStartTime() :"
					+ recordTransactionBean.getStartTime());
			Log.d("CH", "@@@@@@@Duration@@@@@ :"
					+ recordTransactionBean.getCallDuration());

			if (recordTransactionBean.getCalltype() != null) {
				String callType = "";
				if (recordTransactionBean.getCalltype().equalsIgnoreCase("AC")) {
					callType = "Audio Call";
				} else if (recordTransactionBean.getCalltype()
						.equalsIgnoreCase("VC")) {
					callType = "Video Call";
				} else if (recordTransactionBean.getCalltype()
						.equalsIgnoreCase("ABC")) {
					callType = "Audio Broadcast";
				} else if (recordTransactionBean.getCalltype()
						.equalsIgnoreCase("VBC")) {
					callType = "Video Broadcast";
				}else if (recordTransactionBean.getCalltype()
						.equalsIgnoreCase("AP")) {
					callType = "Audio Unicast";
				}else if (recordTransactionBean.getCalltype()
						.equalsIgnoreCase("VP")) {
					callType = "Video Unicast";
				}
				calltype.setText(callType);
			}
//			if (recordTransactionBean.getCalltype() != null) {
//				callcontent.setText(recordTransactionBean.getCalltype());
//			}
//			if (recordTransactionBean.getFromName() != null) {
//				from.setText(recordTransactionBean.getFromName());
//			}
			if (recordTransactionBean.getToName() != null) {
				to.setText(recordTransactionBean.getToName());
			}
			if (recordTransactionBean.getStartTime() != null) {
				date.setText(recordTransactionBean.getStartTime());
			}
//			if (recordTransactionBean.getCallDuration() != null) {
//				duration.setText(recordTransactionBean.getCallDuration());
//			}
//			if (recordTransactionBean.getSessionid() != null) {
//				preview.setTag(recordTransactionBean.getSessionid());
//			}
//		
//			preview.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					try{
//					File file = new File(Environment
//							.getExternalStorageDirectory()
//							+ "/COMMedia/CallRecording/"
//							+ v.getTag().toString() + ".wav");
//					int CountFiles=new File(Environment
//							.getExternalStorageDirectory()
//							+ "/COMMedia/CallRecording/").listFiles().length;
//					Log.d("Test","Length of the files@@@----->"+CountFiles);
//
//					if (file.exists()) {
//						Intent intent = new Intent(context,
//								MultimediaUtils.class);
//						intent.putExtra("filePath", file.getPath());
//						intent.putExtra("requestCode", 4);
//						intent.putExtra("action", "audio");
//						intent.putExtra("createOrOpen", "open");
//						context.startActivity(intent);
//					} else {
//						// Toast.makeText(context, "Sorry file not available",
//						// Toast.LENGTH_LONG).show();
//					}
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//				}
//			});
//			if (recordTransactionBean.getFromName().equals(
//					CallDispatcher.LoginUser)) {
//				accept.setVisibility(View.GONE);
//				rejet.setVisibility(View.GONE);
//				reply.setVisibility(View.GONE);
//
//			} else {
//				accept.setVisibility(View.VISIBLE);
//				rejet.setVisibility(View.VISIBLE);
//				reply.setVisibility(View.VISIBLE);
//
//			}
//			accept.setTag(recordTransactionBean.getParentID());
//			rejet.setTag(recordTransactionBean.getParentID());
//			reply.setTag(recordTransactionBean.getParentID());
//			accept.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View view) {
//					SingleInstance.orderToastShow = true;
//					// TODO Auto-generated method stub
//					// createTextNote("Order is Accepted");
//					sendFile("Order is Accepted", (String) view.getTag());
//					Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT)
//							.show();
//				}
//			});
//			rejet.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View view) {
//					SingleInstance.orderToastShow = true;
//					// TODO Auto-generated method stub
//					// createTextNote("Order is Rejected");
//					sendFile("Order is Rejected", (String) view.getTag());
//					Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT)
//							.show();
//
//				}
//			});
//			reply.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(final View view) {
//					try {
//						SingleInstance.orderToastShow = false;
//						AlertDialog.Builder alert_builder = new AlertDialog.Builder(
//								context);
//						final CharSequence[] b_type = { "Audio call",
//								"Video call", "Text", "Sketch", "Photo",
//								"Audio", "Video" };
//
//						// alert_builder.setTitle("Select Buddy Info");
//						alert_builder.setSingleChoiceItems(b_type, 0,
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int pos) {
//										// TODO Auto-generated method stub
//										String heading = b_type[pos].toString();
//										if (pos == 0) {
//											if (SingleInstance.mainContext
//													.isNetworkConnectionAvailable()) {
//												processCallRequest(1,
//														(String) view.getTag());
//											}
//										} else if (pos == 1) {
//											if (SingleInstance.mainContext
//													.isNetworkConnectionAvailable()) {
//												processCallRequest(2,
//														(String) view.getTag());
//											}
//										} else if (pos == 2) {
//											Intent intentComponent = new Intent(
//													context.getApplicationContext(),
//													ComponentCreator.class);
//											Bundle bndl = new Bundle();
//											bndl.putString("type", "note");
//											bndl.putBoolean("action", true);
//											bndl.putBoolean("forms", false);
//											bndl.putString(
//													"buddyname",
//													CallHistoryActivity.selectedBuddy);
//											bndl.putBoolean("send", true);
//											bndl.putString("parentid",
//													(String) view.getTag());
//											intentComponent.putExtras(bndl);
//											context.startActivity(intentComponent);
//										} else if (pos == 3) {
//
//											Intent intentComponent = new Intent(
//													context.getApplicationContext(),
//													ComponentCreator.class);
//											Bundle bndl = new Bundle();
//											bndl.putString("type", "handsketch");
//											bndl.putBoolean("action", true);
//											bndl.putBoolean("forms", false);
//											bndl.putString(
//													"buddyname",
//													CallHistoryActivity.selectedBuddy);
//											bndl.putBoolean("send", true);
//											bndl.putString("parentid",
//													(String) view.getTag());
//											intentComponent.putExtras(bndl);
//											context.startActivity(intentComponent);
//										} else if (pos == 4) {
//											Intent intentComponent = new Intent(
//													context.getApplicationContext(),
//													ComponentCreator.class);
//											Bundle bndl = new Bundle();
//											bndl.putString("type", "photo");
//											bndl.putBoolean("action", true);
//											bndl.putBoolean("forms", false);
//											bndl.putBoolean("send", true);
//											bndl.putString(
//													"buddyname",
//													CallHistoryActivity.selectedBuddy);
//											bndl.putString("parentid",
//													(String) view.getTag());
//											intentComponent.putExtras(bndl);
//											context.startActivity(intentComponent);
//										} else if (pos == 5) {
//											Intent intentComponent = new Intent(
//													context.getApplicationContext(),
//													ComponentCreator.class);
//											Bundle bndl = new Bundle();
//											bndl.putString("type", "audio");
//											bndl.putBoolean("action", true);
//											bndl.putBoolean("forms", false);
//											bndl.putBoolean("send", true);
//											bndl.putString(
//													"buddyname",
//													CallHistoryActivity.selectedBuddy);
//											bndl.putString("parentid",
//													(String) view.getTag());
//											intentComponent.putExtras(bndl);
//											context.startActivity(intentComponent);
//										} else if (pos == 6) {
//											Intent intentComponent = new Intent(
//													context.getApplicationContext(),
//													ComponentCreator.class);
//											Bundle bndl = new Bundle();
//											bndl.putString("type", "video");
//											bndl.putBoolean("action", true);
//											bndl.putBoolean("forms", false);
//											bndl.putString(
//													"buddyname",
//													CallHistoryActivity.selectedBuddy);
//											bndl.putString("parentid",
//													(String) view.getTag());
//											bndl.putBoolean("send", true);
//											intentComponent.putExtras(bndl);
//											context.startActivity(intentComponent);
//										}
//
//										dialog.dismiss();
//
//									}
//								});
//						alert_builder.show();
//
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						if (AppReference.isWriteInFile)
//							AppReference.logger.error(e.getMessage(), e);
//						else
//							e.printStackTrace();
//					}
//				}
//			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rowView;

	};

	@Override
	public int getCount() {
		return p_list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		RecordTransactionBean bean = (RecordTransactionBean) p_list.get(arg0);
		return bean;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	private void sendFile(String result, String parentID2) {
		createTextNote(result);
		int GET_RESOURCES = 1;
		cbean = DBAccess.getdbHeler(context).putDBEntry(GET_RESOURCES,
				ComponentPath,
				WebServiceReferences.getNoteCreateTimeForFiles(),
				result.replaceAll("\n", ""), complBean,"");
		complBean = cbean;
		complBean.setBsstatus("Received");
		complBean.setBstype("Order");
		complBean.setDirection("In");
		complBean.setParent_id(parentID2);

		uploadDatas.add("note");
		uploadDatas.add("false");
		uploadDatas.add("");
		uploadDatas.add("");
		uploadDatas.add("auto");
		Log.d("sendershare", "" + uploadDatas);
		String comments = "";
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = CallDispatcher
					.getCallDispatcher(SingleInstance.mainContext);
		ArrayList<String> buddy = new ArrayList<String>();
		buddy.add(CallHistoryActivity.selectedBuddy);
		callDisp.sendshare(false, CallDispatcher.LoginUser,
				CallDispatcher.Password, CallHistoryActivity.selectedBuddy,
				buddy, uploadDatas, "note", comments, ComponentPath,
				"fileslist", null, null, null, null, null, false, complBean);

	}

	private void createTextNote(String result) {
		ComponentPath = "/sdcard/COMMedia/" + CompleteListView.getFileName()
				+ ".txt";
		// strIPath = ComponentPath;
		if (CompleteListView.textnotes == null)
			CompleteListView.textnotes = new TextNoteDatas();

		CompleteListView.textnotes.checkAndCreate(ComponentPath, result);
	}

	public void processCallRequest(int caseid, String parentId) {
		try {

			String state = null;
			BuddyInformationBean oldBuddyBean = null;
			for (BuddyInformationBean temp : ContactsFragment.getBuddyList()) {
				if (!temp.isTitle()) {
					if (temp.getName().equalsIgnoreCase(
							CallHistoryActivity.selectedBuddy)) {
						oldBuddyBean = temp;
						state = oldBuddyBean.getStatus();
						break;
					}
				}
			}
		

			
			Log.d("LM", "call status--->" + state);

			if (CallHistoryActivity.selectedBuddy != null
					&& state.equalsIgnoreCase("Offline")
					|| state.equals("Stealth")
					|| state.equalsIgnoreCase("pending")
					|| state.equalsIgnoreCase("Virtual")
					|| state.equalsIgnoreCase("airport")) {
				if (WebServiceReferences.running) {
					CallDispatcher.pdialog = new ProgressDialog(context);
					calldisp.showprogress(CallDispatcher.pdialog, context);

					String[] res_info = new String[3];
					res_info[0] = CallDispatcher.LoginUser;
					res_info[1] = CallHistoryActivity.selectedBuddy;
					if (state.equals("Offline") || state.equals("Stealth"))
						res_info[2] = calldisp
								.getdbHeler(context)
								.getwheninfo(
										"select cid from clonemaster where cdescription='Offline'");
					else
						res_info[2] = "";

					WebServiceReferences.webServiceClient
							.OfflineCallResponse(res_info);
				}

			} else {
				if (!state.equalsIgnoreCase("pending")) {
					SingleInstance.parentId = parentId;
					calldisp.MakeCall(caseid,
							CallHistoryActivity.selectedBuddy, context);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
