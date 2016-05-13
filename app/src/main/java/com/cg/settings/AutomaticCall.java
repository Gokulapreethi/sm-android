package com.cg.settings;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.adapter.AutoCallAdapter;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.group.AddGroupMembers;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

public class AutomaticCall extends Activity {
	public Button back, plusBtn,btnSave;
	private static Vector<UserBean> membersList;
	private AutoCallAdapter adapter = null;
	private String username;
	private ListView lv_addcontact = null;
	private Button autoAccept = null;
	Handler handler = new Handler();
	private Context context = null;

	// public CheckBox selectAll = null;

	// private CallDispatcher callDisp = null;

	Vector<UserBean> contactList = new Vector<UserBean>();

	public static synchronized Vector<UserBean> getMembersList() {
		return membersList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.autocall);
			context = this;
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(SingleInstance.mainContext);
			username = sharedPreferences.getString("uname", null);
			// if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			// callDisp = (CallDispatcher) WebServiceReferences.callDispatch
			// .get("calldisp");
			// else
			// callDisp = new CallDispatcher(context);

			WebServiceReferences.contextTable.put("Autocall", context);
			back = (Button) findViewById(R.id.btn_backaddcontact);
			btnSave = (Button) findViewById(R.id.btn_save);

			// done = (Button) findViewById(R.id.btn_done);
			plusBtn = (Button) findViewById(R.id.btn_done);
			autoAccept = (Button) findViewById(R.id.auto_accept_btn);
			lv_addcontact = (ListView) findViewById(R.id.lv_buddylist);
			lv_addcontact.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			// selectAll = (CheckBox) findViewById(R.id.btn_selectall);
			// selectAll.setVisibility(View.GONE);

			membersList = new Vector<UserBean>();

			if (DBAccess.getdbHeler(SingleInstance.mainContext)
					.Selectautpacceptcalls(CallDispatcher.LoginUser) != null) {

				membersList = DBAccess.getdbHeler(context)
						.Selectautpacceptcalls(CallDispatcher.LoginUser);

			}

			adapter = new AutoCallAdapter(this, getMembersList());
			lv_addcontact.setAdapter(adapter);
			plusBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (ContactsFragment.getBuddyList().size() > 0) {
						Intent intent = new Intent(getApplicationContext(),
								AddGroupMembers.class);
						ArrayList<String> buddylist = new ArrayList<String>();
						for (UserBean userBean : getMembersList()) {
							buddylist.add(userBean.getBuddyName());
						}
						intent.putStringArrayListExtra("buddylist", buddylist);
						startActivityForResult(intent, 3);
					} else {
						showToast("Sorry no members to add");
					}
				}
			});
			btnSave.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						// TODO Auto-generated method stub
						for (UserBean userBean : getMembersList()) {

							if (DBAccess.getdbHeler().isRecordExists(
									"select * from autoacceptcalls where username='"
											+ userBean.getBuddyName()
											+ "' and owner='"
											+ CallDispatcher.LoginUser
											+ "' and flag='0'")) {
								if (DBAccess.getdbHeler()
										.deleteBuddyFromAutomacticCall(
												userBean.getBuddyName(),
												CallDispatcher.LoginUser)) {
									getMembersList().remove(userBean);
								}
							}
						}
						showToast("Saved Successfully");

//						finish();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						// TODO Auto-generated method stub
//						for (UserBean userBean : getMembersList()) {
//							if (DBAccess.getdbHeler().isRecordExists(
//									"select * from autoacceptcalls where username='"
//											+ userBean.getBuddyName()
//											+ "' and owner='"
//											+ CallDispatcher.LoginUser
//											+ "' and flag='0'")) {
//								if (DBAccess.getdbHeler()
//										.deleteBuddyFromAutomacticCall(
//												userBean.getBuddyName(),
//												CallDispatcher.LoginUser)) {
//                                    showToast("Saved Successfully");
//									getMembersList().remove(userBean);
//								}
//							}
//						}
						for (UserBean userBean : getMembersList()) {
							DBAccess.getdbHeler()
									.Updateautoacceptcalls(
											userBean, "1");
						}
						finish();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			lv_addcontact.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub

				}
			});
			lv_addcontact
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							// TODO Auto-generated method stub
							UserBean userBean = adapter.getItem(position);
							doDeleteContact(userBean);
							return true;
						}
					});
			final SharedPreferences sPreferences = PreferenceManager
					.getDefaultSharedPreferences(SingleInstance.mainContext
							.getApplicationContext());
			boolean isAutoAccept = sPreferences.getBoolean("autoaccept", false);
			if (isAutoAccept) {
				autoAccept.setText(SingleInstance.mainContext
						.getString(R.string.on));
			} else {
				autoAccept.setText(SingleInstance.mainContext
						.getString(R.string.off));
			}
			autoAccept.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Editor editor = sPreferences.edit();
					if (autoAccept
							.getText()
							.toString()
							.equalsIgnoreCase(
									SingleInstance.mainContext
											.getString(R.string.on))) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								autoAccept.setText(SingleInstance.mainContext
										.getString(R.string.off));
							}
						});
						editor.putBoolean("autoaccept", false);
					} else {
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								autoAccept.setText(SingleInstance.mainContext
										.getString(R.string.on));
							}
						});

						editor.putBoolean("autoaccept", true);
					}
					editor.commit();
				}
			});
			// selectAll.setTag(true);
			// selectAll.setOnCheckedChangeListener(new
			// OnCheckedChangeListener() {
			//
			// @Override
			// public void onCheckedChanged(CompoundButton buttonView,
			// boolean isChecked) {
			// // TODO Auto-generated method stub
			//
			// try {
			// if ((Boolean) selectAll.getTag()) {
			// for (UserBean userBean : membersList) {
			// userBean.setSelected(true);
			// }
			// selectAll.setTag(false);
			// } else {
			// for (UserBean userBean : membersList) {
			// userBean.setSelected(false);
			// }
			// selectAll.setTag(true);
			// }
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// if (AppReference.isWriteInFile)
			// AppReference.logger.error(e.getMessage(), e);
			// else
			// e.printStackTrace();
			// }
			// adapter.notifyDataSetChanged();
			//
			// }
			// });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private void refreshMembersList() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// memberCount.setText("Members ("
				// + String.valueOf(membersList.size()) + ")");
				adapter.notifyDataSetChanged();
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);

			// check if the request code is same as what is passed here it is 2
			if (requestCode == 3) {
				if (data != null) {
					Bundle bundle = data.getExtras();
					ArrayList<UserBean> list = (ArrayList<UserBean>) bundle
							.get("list");
					for (UserBean userBean : list) {
						getMembersList().add(userBean);
						String flag = "0";
						if (userBean.isSelected()) {
							flag = "1";
						}
						DBAccess.getdbHeler().insertautoacceptcalls(userBean,
								flag);

						// String insertQuery =
						// "insert into autoacceptcalls(owner,username,flag)"
						// + "values('"
						// + CallDispatcher.LoginUser
						// + "','"
						// + userBean.getBuddyName()
						// + "','"
						// + flag
						// + "')";
						// if (DBAccess.getdbHeler().ExecuteQuery(insertQuery))
						// {
						//
						// }
					}
					refreshMembersList();

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("Autocall");
		super.onDestroy();
	}

	void doDeleteContact(final UserBean userBean) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle(SingleInstance.mainContext.getResources()
							.getString(R.string.warning));
					builder.setMessage(
							SingleInstance.mainContext
									.getResources()
									.getString(
											R.string.are_you_sure_you_want_to_delete)+" "
									+ userBean.getBuddyName() + " ?")
							.setCancelable(false)
							.setPositiveButton(
									SingleInstance.mainContext.getResources()
											.getString(R.string.yes),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											getMembersList().remove(userBean);
											if (DBAccess
													.getdbHeler()
													.isRecordExists(
															"select * from autoacceptcalls where username='"
																	+ userBean
																			.getBuddyName()
																	+ "' and owner='"
																	+ CallDispatcher.LoginUser
																	+ "'")) {
												if (DBAccess
														.getdbHeler()
														.deleteBuddyFromAutomacticCall(
																userBean.getBuddyName(),
																CallDispatcher.LoginUser)) {
													getMembersList()
															.remove(userBean);
													adapter.notifyDataSetChanged();
												}
											}
										}

									})
							.setNegativeButton(
									SingleInstance.mainContext.getResources()
											.getString(R.string.no),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert1 = builder.create();
					alert1.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private void showToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
}
