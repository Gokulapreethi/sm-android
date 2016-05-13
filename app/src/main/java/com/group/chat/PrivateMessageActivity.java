package com.group.chat;

import java.util.Vector;

import org.lib.model.GroupBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bean.GroupChatPermissionBean;
import com.bean.SpecialMessageBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.CompleteListView;
import com.group.BuddyAdapter;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class PrivateMessageActivity extends Activity implements OnClickListener {

	private TextView message;
	private Button sendBtn;
	private Button cancelBtn;
	// private RadioGroup messageOptions;
	private RadioButton scheduleChk;
	private RadioButton privateChk;
	private RadioButton replybackChk;
	private RadioButton deadLineChk;
	private ListView membersListView;
	private BuddyAdapter adapter;
	private Context context;
	private CallDispatcher callDisp;
	private Vector<UserBean> membersList = new Vector<UserBean>();
	private String messages;
	private String localPath;
	private String type;
	private LinearLayout dateLayout;
	private TextView dateTime;
	private ImageView multimediaMsg;
	private ImageLoader imageLoader;
	private LinearLayout splMsgContainer;
	private RelativeLayout settingsLayout;
	private boolean isReplyback = false;
	private GroupChatActivity gChat = null;
	private String selectedOptions = null;
	Handler handler = new Handler();
	String groupId = null;
	private String buddyName;
	private TextView GroupText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.private_msg);
			context = this;
			this.setFinishOnTouchOutside(false);
			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			SingleInstance.contextTable.put("pmsg", context);
			messages = getIntent().getStringExtra("message");
			localPath = getIntent().getStringExtra("localpath");
			type = getIntent().getStringExtra("type");
			buddyName = getIntent().getStringExtra("buddyname");
			isReplyback = getIntent().getBooleanExtra("replyback", false);
			message = (TextView) findViewById(R.id.edMessage);
			message.setText(messages);
			sendBtn = (Button) findViewById(R.id.sendButton);
			sendBtn.setOnClickListener(this);
			cancelBtn = (Button) findViewById(R.id.cancelButton);
			cancelBtn.setOnClickListener(this);
			dateLayout = (LinearLayout) findViewById(R.id.date_layout);
			// messageOptions = (RadioGroup) findViewById(R.id.RadioGroup);
			scheduleChk = (RadioButton) findViewById(R.id.schedule);
			scheduleChk.setOnClickListener(this);
			privateChk = (RadioButton) findViewById(R.id.private_chk);
			privateChk.setOnClickListener(this);
			replybackChk = (RadioButton) findViewById(R.id.reply_back);
			replybackChk.setOnClickListener(this);
			deadLineChk = (RadioButton) findViewById(R.id.deadLine);
			deadLineChk.setOnClickListener(this);
			membersListView = (ListView) findViewById(R.id.listView);
//			ImageView dateTimeBtn = (ImageView) findViewById(R.id.date_icon);
//			dateTimeBtn.setOnClickListener(this);
			dateTime = (TextView) findViewById(R.id.date_time);
			ImageView sendOpt = (ImageView) findViewById(R.id.private_opt);
			sendOpt.setOnClickListener(this);
			multimediaMsg = (ImageView) findViewById(R.id.edImage);
			multimediaMsg.setOnClickListener(this);
			splMsgContainer = (LinearLayout) findViewById(R.id.splmsglay);
			splMsgContainer.setVisibility(View.GONE);
			settingsLayout = (RelativeLayout) findViewById(R.id.settings_lay);
			GroupText=(TextView) findViewById(R.id.GroupText);
			gChat = (GroupChatActivity) SingleInstance.contextTable
					.get("groupchat");
			if (gChat.isGroup) {
				privateChk.setVisibility(View.VISIBLE);
				GroupText.setVisibility(View.VISIBLE);
			} else {
				privateChk.setVisibility(View.GONE);
			}
			
			if (isReplyback)
				settingsLayout.setVisibility(View.GONE);
			adapter = new BuddyAdapter(context, membersList);
			groupId = getIntent().getStringExtra("groupid");
			loadGroupMembers(groupId);
			membersListView.setAdapter(adapter);
			setListViewHeightBasedOnChildren(membersListView);
			imageLoader = new ImageLoader(context.getApplicationContext());

			if (localPath != null) {
				multimediaMsg.setTag(localPath);
				multimediaMsg.setContentDescription(type);
			}
			if (type.equalsIgnoreCase("image")) {
				imageLoader.DisplayImage(localPath, multimediaMsg,
						R.drawable.refresh);
				multimediaMsg.setVisibility(View.VISIBLE);
			}else
			if (type.equalsIgnoreCase("sketch")) {
				imageLoader.DisplayImage(localPath, multimediaMsg,
						R.drawable.refresh);
				multimediaMsg.setVisibility(View.VISIBLE);
			} else if (type.equalsIgnoreCase("text")) {
				message.setVisibility(View.VISIBLE);
				if (gChat.isGroup && !isReplyback) {
					splMsgContainer.setVisibility(View.VISIBLE);
				}
			} else if (type.equalsIgnoreCase("audio")) {
				multimediaMsg.setVisibility(View.VISIBLE);
				multimediaMsg.setImageResource(R.drawable.audio2);
			} else if (type.equalsIgnoreCase("video")) {
				multimediaMsg.setVisibility(View.VISIBLE);
				multimediaMsg.setImageResource(R.drawable.videoview1);
			}
			else if (type.equalsIgnoreCase("document")) {
				multimediaMsg.setVisibility(View.VISIBLE);
				multimediaMsg.setImageResource(R.drawable.doc_chat);
			}
			// messageOptions
			// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//
			// @Override
			// public void onCheckedChanged(RadioGroup view,
			// int position) {
			// try {
			// // TODO Auto-generated method stub
			// int selectedOptions = view
			// .getCheckedRadioButtonId();
			// RadioButton rBtn = (RadioButton) findViewById(selectedOptions);
			// if (rBtn.getTag().toString()
			// .equalsIgnoreCase("gs")
			// || rBtn.getTag().toString()
			// .equalsIgnoreCase("gd")) {
			// dateLayout.setVisibility(View.VISIBLE);
			// } else {
			// dateLayout.setVisibility(View.GONE);
			// }
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// });

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (gChat != null)
			gChat.isReplyBack = false;
		super.onDestroy();

		SingleInstance.contextTable.remove("pmsg");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (gChat != null)
			gChat.isReplyBack = false;
		finish();
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
	}

	@Override
	public void onClick(View v) {
		try {
			// TODO Auto-generated method stub
			GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
					.get("groupchat");
			switch (v.getId()) {
			case R.id.sendButton:
				// if (splMsgContainer.getVisibility() == View.VISIBLE) {
				if(SingleInstance.mainContext.isNetworkConnectionAvailable()){

				SpecialMessageBean spBean = new SpecialMessageBean();
				if (gChat != null) {
					// int selectedOptions = messageOptions
					// .getCheckedRadioButtonId();

					// if (selectedOptions > 0) {
					// RadioButton rBtn = (RadioButton)
					// findViewById(selectedOptions);
					// }
					if (selectedOptions != null)
						spBean.setSubcategory(selectedOptions);
					else if (isReplyback) {
						String parentId = getIntent()
								.getStringExtra("parentid");
						if (parentId != null) {
							spBean.setParentId(parentId);
						}
						spBean.setSubcategory("grb");
					}
					String addMembers = "";
					if (isReplyback) {
						if (gChat.isGroup) {
							String members = getIntent().getStringExtra(
									"pMembers");
							if (members != null) {
								addMembers = members;
								spBean.setMembers(addMembers);
							}
						} else {
							if (buddyName != null) {
								spBean.setMembers(buddyName);
							}
						}
					} else {
						for (UserBean userBean : membersList) {
							if (!userBean.getBuddyName().equalsIgnoreCase(
									CallDispatcher.LoginUser)) {
								if (userBean.isSelected()) {
									addMembers = addMembers
											+ userBean.getBuddyName() + ",";
								}

							}
						}
						if (addMembers.length() > 0) {
							addMembers = addMembers.substring(0,
									addMembers.length() - 1);
							spBean.setMembers(addMembers);
						} else {
							if (!gChat.isGroup && buddyName != null) {
								spBean.setMembers(buddyName);
							}
						}
					}
					boolean validate = true;
					if (splMsgContainer.getVisibility() != View.GONE)
						validate = validateMessage(spBean);
					if (type.equalsIgnoreCase("text")) {
						if (message.getText().toString().length() > 700) {
							validate = false;
							Toast.makeText(context,
									"Text exceeds 700 characters", 1).show();
						}
					}
					if (validate) {
						gChat.sendMsg(messages, localPath, type, spBean);
						finish();
					}
				}
				}
				else
				{
					showAlert1("Check Internet Connection Unable to Connect Server");
				}
				break;
			// } else {
			// Toast.makeText(SingleInstance.mainContext,
			// "Please enable settings options", Toast.LENGTH_LONG)
			// .show();
			// }
			case R.id.cancelButton:
				showAlertBeforeCancel(gChat);
				break;
//			case R.id.date_icon:
//				generateDatePicker();
//				break;
			case R.id.edImage:
				if (gChat != null) {
					gChat.playMultimedia(multimediaMsg);
				}
				break;
			case R.id.private_opt:
				if (splMsgContainer.getVisibility() == View.VISIBLE) {
					splMsgContainer.setVisibility(View.GONE);
				} else
					splMsgContainer.setVisibility(View.VISIBLE);
				break;
			case R.id.deadLine:
				if (privateChk.isChecked()) {
					privateChk.setChecked(false);
				} else if (scheduleChk.isChecked()) {
					scheduleChk.setChecked(false);
				} else if (replybackChk.isChecked()) {
					replybackChk.setChecked(false);
				}
				
				deadLineChk.setChecked(true);
				selectedOptions = deadLineChk.getTag().toString();
				if (dateLayout.getVisibility() == View.GONE) {
					dateLayout.setVisibility(View.VISIBLE);
				}
				generateDatePicker();
				break;
			case R.id.private_chk:
				if (deadLineChk.isChecked()) {
					deadLineChk.setChecked(false);
				} else if (scheduleChk.isChecked()) {
					scheduleChk.setChecked(false);
				} else if (replybackChk.isChecked()) {
					replybackChk.setChecked(false);
				}
				privateChk.setChecked(true);
				selectedOptions = privateChk.getTag().toString();
				if (dateLayout.getVisibility() == View.VISIBLE) {
					dateLayout.setVisibility(View.GONE);
				}
				break;
			case R.id.schedule:

				if (deadLineChk.isChecked()) {
					deadLineChk.setChecked(false);
				} else if (privateChk.isChecked()) {
					privateChk.setChecked(false);
				} else if (replybackChk.isChecked()) {
					replybackChk.setChecked(false);
				}
				scheduleChk.setChecked(true);
				selectedOptions = scheduleChk.getTag().toString();
				generateDatePicker();
				if (dateLayout.getVisibility() == View.GONE) {
					dateLayout.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.reply_back:
				if (deadLineChk.isChecked()) {
					deadLineChk.setChecked(false);
				} else if (privateChk.isChecked()) {
					privateChk.setChecked(false);
				} else if (scheduleChk.isChecked()) {
					scheduleChk.setChecked(false);
				}
				replybackChk.setChecked(true);
				selectedOptions = replybackChk.getTag().toString();
				if (dateLayout.getVisibility() == View.VISIBLE) {
					dateLayout.setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/****
	 * Method for Setting the Height of the ListView dynamically. Hack to fix
	 * the issue of not showing all the items of the ListView when placed inside
	 * a ScrollView
	 ****/
	private void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	private boolean validateMessage(SpecialMessageBean spBean) {
		boolean validate = false;
		try {
			if (spBean.getSubcategory() != null && spBean.getMembers() != null
					&& spBean.getMembers().length() > 0) {
				if (permissionValidate(spBean)) {
					validate = true;
					if (!spBean.getSubcategory().equalsIgnoreCase("gp")
							|| !spBean.getSubcategory().equalsIgnoreCase("grb")
							|| !spBean.getSubcategory()
									.equalsIgnoreCase("grbi")) {
						if (spBean.getSubcategory().equalsIgnoreCase("gs")
								|| spBean.getSubcategory().equalsIgnoreCase(
										"gd")) {
							spBean.setRemindertime(dateTime.getText()
									.toString());
							if (spBean.getRemindertime() != null
									&& spBean.getRemindertime().length() > 0) {
								validate = true;
							} else {
								validate = false;
								Toast.makeText(context,
										"Please select date and time", 1)
										.show();
							}
						}
					} else {
						validate = true;
					}
				}
			} else {
				if (spBean.getSubcategory() == null) {
					Toast.makeText(context, "Please select message type", 1)
							.show();
				}
				if (spBean.getMembers() == null
						|| spBean.getMembers().length() == 0) {
					Toast.makeText(context, "Please select members", 1).show();
				}
			}
			return validate;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private boolean permissionValidate(SpecialMessageBean spBean) {
		if (gChat.isGroup) {
			GroupBean gBean = DBAccess.getdbHeler().getGroup(
					"select * from grouplist where groupid='" + groupId + "'");
			GroupChatPermissionBean gcpBean = SingleInstance.mainContext
					.getGroupChatPermission(gBean);
			if (spBean.getSubcategory().equalsIgnoreCase("GD")
					|| spBean.getSubcategory().equalsIgnoreCase("GDI")) {
				if (!gcpBean.getDeadLineMessage().equalsIgnoreCase("1")) {
					showToast("Sorry you dont have permission");
					return false;
				}
			}
			if (spBean.getSubcategory().equalsIgnoreCase("GS")) {
				if (!gcpBean.getScheduleMessage().equalsIgnoreCase("1")) {
					showToast("Sorry you dont have permission");
					return false;
				}
			}
			if (spBean.getSubcategory().equalsIgnoreCase("GP")) {
				if (!gcpBean.getPrivateMessage().equalsIgnoreCase("1")) {
					showToast("Sorry you dont have permission");
					return false;
				}
			}
			if (spBean.getSubcategory().equalsIgnoreCase("GRB")) {
				if (!gcpBean.getReplyBackMessage().equalsIgnoreCase("1")) {
					showToast("Sorry you dont have permission");
					return false;
				}
			}
		} else {
			return true;
		}
		return true;
	}

	private void generateDatePicker() {
		try {
			Button btnSet;
			final DatePicker dp;
			final TimePicker tp;
			final AlertDialog alertReminder = new AlertDialog.Builder(context)
					.create();
			ScrollView tblDTPicker = (ScrollView) View.inflate(context,
					R.layout.date_time_picker, null);
			btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
			dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
			tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);
			btnSet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub

						// final String strDateTime = WebServiceReferences
						// .setLength2(dp.getDayOfMonth())
						// + "/"
						// + WebServiceReferences.setLength2((dp
						// .getMonth() + 1))
						// + "/"
						// + dp.getYear()
						// + " "
						// + WebServiceReferences.setLength2(tp
						// .getCurrentHour())
						// + ":"
						// + WebServiceReferences.setLength2(tp
						// .getCurrentMinute());
						final String strDateTime = dp.getYear()
								+ "-"
								+ WebServiceReferences.setLength2((dp
										.getMonth() + 1))
								+ "-"
								+ WebServiceReferences.setLength2(dp
										.getDayOfMonth())
								+ " "
								+ WebServiceReferences.setLength2(tp
										.getCurrentHour())
								+ ":"
								+ WebServiceReferences.setLength2(tp
										.getCurrentMinute());

						Log.e("timemessage", "@@@@@@@" + strDateTime);
						/**
						 * Due to IOS compatibility
						 */
						// SimpleDateFormat inPutFormatter = new
						// SimpleDateFormat(
						// "dd/MM/yyyy HH:mm");
						// SimpleDateFormat outPutFormatter = new
						// SimpleDateFormat(
						// "dd/MM/yyyy hh:mm aa");
						// DateFormatSymbols dfSym = new DateFormatSymbols();
						// dfSym.setAmPmStrings(new String[] { "am", "pm" });
						// outPutFormatter.setDateFormatSymbols(dfSym);
						// Date date = null;
						// try {
						// date = inPutFormatter.parse(strDateTime);
						//
						// } catch (ParseException e) {
						// // TODO Auto-generated catch block
						// if (AppReference.isWriteInFile)
						// AppReference.logger.error(e.getMessage(), e);
						// else
						// e.printStackTrace();
						// }
						// String newDate = outPutFormatter.format(date);
						/**
						 * Ends here
						 */
						if (CompleteListView.CheckReminderIsValid(strDateTime)) {
							dateLayout.setVisibility(View.VISIBLE);
							dateTime.setText(strDateTime);
							alertReminder.cancel();
						} else {

							Toast.makeText(context,
									"Please assign future date and time",
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			alertReminder.setTitle("Date And Time");
			alertReminder.setView(tblDTPicker);
			alertReminder.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadGroupMembers(String groupId) {
		try {
			GroupBean gBean = callDisp.getdbHeler(context).getGroupAndMembers(
					"select * from groupdetails where groupid=" + groupId);
			if (gBean != null) {
				membersList.clear();
				UserBean selfUser = getSelfUserBean(gBean);
				if (selfUser != null)
					membersList.add(selfUser);
				if (gBean.getActiveGroupMembers() != null
						&& gBean.getActiveGroupMembers().length() > 0) {
					String[] list = (gBean.getActiveGroupMembers()).split(",");
					for (String tmp : list) {
						if (!tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
							UserBean userBean = new UserBean();
							userBean.setBuddyName(tmp);
							userBean.setSelected(false);
							membersList.add(userBean);
						}
					}
				}
				if (gBean.getInActiveGroupMembers() != null
						&& gBean.getInActiveGroupMembers().length() > 0) {
					String[] list = (gBean.getInActiveGroupMembers())
							.split(",");
					for (String tmp : list) {
						if (!tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
							UserBean userBean = new UserBean();
							userBean.setBuddyName(tmp);
							userBean.setSelected(false);
							membersList.add(userBean);
						}
					}
				}
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UserBean getSelfUserBean(GroupBean groupBean) {
		try {
			if (!groupBean.getOwnerName().equalsIgnoreCase(
					CallDispatcher.LoginUser)) {
				UserBean uBean = new UserBean();
				uBean.setBuddyName(groupBean.getOwnerName());
				uBean.setSelected(false);
				return uBean;
			} else
				return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private void showAlertBeforeCancel(final GroupChatActivity gChat) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			String message = "Are you sure to cancel?";

			builder.setMessage(message)
					.setCancelable(false)
					.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes)
,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (gChat != null)
										gChat.isReplyBack = false;
									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void showAlert1(String msg) {
		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
		alertCall.setMessage(msg).setCancelable(false)
				.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							// alertCall.
							dialog.dismiss();

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		alertCall.show();

	}
	private void showToast(final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
		});

	}

}
