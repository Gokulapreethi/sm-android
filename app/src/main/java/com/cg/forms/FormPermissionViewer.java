package com.cg.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.BuddyPermission;
import com.bean.DefaultPermission;
import com.bean.FormFieldBean;
import com.bean.IndividualPermission;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class FormPermissionViewer extends Activity implements OnClickListener {

	private Button btn_cancel = null;
	private Button btn_add, btn_viewcall = null;
	private LinearLayout lay_viewer, layout_query = null;
	private ArrayList<String[]> list = null;
	private ArrayList<String[]> rec_list = null;
	private String tbl_name = null;
	private Context cntxt;
	private String tbl_id = null;
	boolean isreject = false;
	private TextView txt_title = null;
	private String tbl_owner = null;
	private ProgressDialog dialog = null;
	private Handler wservice_handler = null;
	private CallDispatcher callDisp = null;
	private ArrayList<Object> bmp_container = null;
	private String updatequery, access, sync = null;
	private ArrayList<String> stringArrayList = null;
	private Button update, cancel = null;
	private Spinner accessspinner, syncspinner = null;
	private EditText query = null;
	private int selecteposition, syncselected;
	private String existquery, updatedaccess, updatedsync, codeaccess,
			codesync, value, value1 = null;
	private Button IMRequest;
	private TextView tv_update;
	private Button field_access_settings = null;
	private TextView formDescription = null;
	private ImageView formIcon = null;
	private Bitmap icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("ABCD","inside FormPermissionViewer");
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forms_viewer);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;
		cntxt = this;
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(cntxt);

		callDisp.setNoScrHeight(noScrHeight);
		callDisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;

		WebServiceReferences.contextTable.put("formpermissionviewer", cntxt);

		btn_cancel = (Button) findViewById(R.id.btn_viewrecback);
		btn_cancel.setOnClickListener(this);
		btn_add = (Button) findViewById(R.id.btn_viewaddanother);
		btn_viewcall = (Button) findViewById(R.id.btn_viewcall);
		btn_viewcall.setVisibility(View.GONE);
		txt_title = (TextView) findViewById(R.id.tv_viewrectilte);
		formDescription = (TextView) findViewById(R.id.tv_frmdescription);
		formIcon = (ImageView) findViewById(R.id.formicon_img);
		btn_add.setOnClickListener(this);

		field_access_settings = (Button) findViewById(R.id.viewnotification);
		field_access_settings.setVisibility(View.VISIBLE);
		field_access_settings.setOnClickListener(this);
		IMRequest = (Button) findViewById(R.id.viewim);

		IMRequest.setVisibility(View.INVISIBLE);
		lay_viewer = (LinearLayout) findViewById(R.id.llayout_viewholder);

		tbl_name = (String) getIntent().getStringExtra("name");

		tbl_id = (String) getIntent().getStringExtra("id");
		txt_title.setText(tbl_name);
		tbl_owner = getIntent().getStringExtra("owner");

		list = callDisp.getdbHeler(cntxt).getColumnofSettingtbl(tbl_id);
		tv_update = (TextView) findViewById(R.id.tv_update);
		rec_list = callDisp.getdbHeler(cntxt).getRecordsofSettingtbl(tbl_id);
		bmp_container = new ArrayList<Object>();
		wservice_handler = new Handler();
		String[] formDetails = callDisp.getdbHeler(cntxt)
				.getFormDetailsRecords(tbl_id);
		if (formDetails[0].length() > 0) {

			setIconImage(formDetails[0]);
		}
		if (formDetails[1].length() > 0) {

			formDescription.setText(formDetails[1]);
		} else {

			formDescription.setText("Description");

		}
		loadRecords();
	}

	void setIconImage(String name) {
		File extStore = Environment.getExternalStorageDirectory();
		File myFile = new File(extStore.getAbsolutePath() + "/COMMedia/" + name);

		if (!myFile.exists()) {

			downloadConfiguredNote(name);

		} else {

			setImage(name);
		}

	}

	private void downloadConfiguredNote(String path) {

		if (path != null) {

			if (CallDispatcher.LoginUser != null) {
				callDisp.downloadOfflineresponse(path, "", "forms", "");

			}
		}
	}

	private void setImage(String name) {

		String blob_path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/COMMedia/" + name;
		if (name.startsWith("FormIcon_")) {

			icon = callDisp.ResizeImage(blob_path);
			if (icon != null) {
				icon = Bitmap.createScaledBitmap(icon, 200, 150, false);
				formIcon.setImageBitmap(icon);
			} else {
				icon = BitmapFactory.decodeResource(getResources(),
						R.drawable.form_icon);
				formIcon.setImageBitmap(icon);

			}
		}

	}

	private void showdialog(final String[] records) {
		Log.i("ABCD","inside FormPermissionViewer showdialog");

		final String[] choiceList = { "View", "No Access", "Add", "Complete",
				"Read & Add Own" };
		final String[] synclist = { "Always sync", "Never Sync",
				"Sync Based on Rule" };

		final Dialog dialog = new Dialog(FormPermissionViewer.this);
		dialog.setContentView(R.layout.editdialog);
		dialog.setTitle("Update Access and sync ");
		layout_query = (LinearLayout) dialog.findViewById(R.id.layout_query);
		accessspinner = (Spinner) dialog.findViewById(R.id.access_spinner);
		syncspinner = (Spinner) dialog.findViewById(R.id.sync_spinner);
		query = (EditText) dialog.findViewById(R.id.query);

		for (int i = 0; i < records.length; i++) {

			access = records[5];
			sync = records[6];
			existquery = records[7];

		}
		if (access.contains("1")) {

			selecteposition = 0;

		}
		if (access.contains("2")) {

			selecteposition = 1;

		}
		if (access.contains("3")) {
			selecteposition = 2;

		}
		if (access.contains("4")) {
			selecteposition = 3;

		}
		if (access.contains("5")) {
			selecteposition = 4;

		}
		if (sync.contains("1")) {
			syncselected = 1;

		}
		if (sync.contains("2")) {
			syncselected = 0;

		}
		if (sync.contains("3")) {
			syncselected = 2;
			layout_query.setVisibility(View.VISIBLE);
			query.setText(existquery);

		}

		ArrayAdapter<String> aa = new ArrayAdapter<String>(
				this, R.layout.spinnerxml, choiceList);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accessspinner.setAdapter(aa);
		accessspinner.setSelection(selecteposition);
		accessspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Log.i("ABCD","inside FormPermissionViewer accessspinner");


				updatedaccess = choiceList[position].toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		ArrayAdapter<String> sync = new ArrayAdapter<String>(
				this, R.layout.spinnerxml, synclist);
		sync.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		syncspinner.setAdapter(sync);
		syncspinner.setSelection(syncselected);

		syncspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				updatedsync = synclist[position].toString();

				if (position == 2) {

					layout_query.setVisibility(View.VISIBLE);

				}

				else {

					layout_query.setVisibility(View.GONE);
				}

				updatequery = query.getText().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		update = (Button) dialog.findViewById(R.id.update);
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!CallDispatcher.isWifiClosed) {
					if (updatedsync.equalsIgnoreCase("Always sync")) {

						codesync = "S02";

					}
					if (updatedsync.equalsIgnoreCase("Never Sync")) {
						codesync = "S01";

					}
					if (updatedsync.equalsIgnoreCase("Sync Based on Rule")) {
						codesync = "S03";

					}
					if (updatedaccess.equalsIgnoreCase("No Access")) {

						codeaccess = "A01";

					}
					if (updatedaccess.equalsIgnoreCase("View")) {

						codeaccess = "A02";

					}
					if (updatedaccess.equalsIgnoreCase("Add")) {

						codeaccess = "A03";

					}
					if (updatedaccess.equalsIgnoreCase("Complete")) {

						codeaccess = "A04";

					}
					if (updatedaccess.equalsIgnoreCase("Read & Add Own")) {

						codeaccess = "A05";

					}

					if (codesync != null && codeaccess != null) {
						updatequery = query.getText().toString();

						if (WebServiceReferences.running) {
							WebServiceReferences.webServiceClient
									.SaveAccessForm(records[1], records[2],
											records[3], codeaccess, codesync,
											query.getText().toString(),
											records[0], "edit", cntxt,null);
							showprogress();
						} else {
							callDisp.startWebService(
									getResources().getString(
											R.string.service_url), "80");
							WebServiceReferences.webServiceClient
									.SaveAccessForm(records[1], records[2],
											records[3], codeaccess, codesync,
											query.getText().toString(),
											records[0], "edit", cntxt,null);
							showprogress();

						}

					}

				}

				else {
					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.network_err),
							SingleInstance.mainContext.getResources()
									.getString(R.string.network_unreachable));
				}

				dialog.dismiss();

			}

		});
		cancel = (Button) dialog.findViewById(R.id.cancel_update);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

			}

		});

		dialog.show();
	}

	public void ShowError(String Title, String Message) {
		AlertDialog confirmation = new AlertDialog.Builder(this).create();
		confirmation.setTitle(Title);
		confirmation.setMessage(Message);
		confirmation.setCancelable(true);
		confirmation.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		confirmation.show();
	}

	private void showprogress() {

		dialog = new ProgressDialog(cntxt);
		dialog.setCancelable(false);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();

	}

	public void cancelDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public void cancelDialog(boolean flag) {

		if (flag) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;

			}
		} else {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
				Toast.makeText(
						cntxt,
						SingleInstance.mainContext.getResources().getString(
								R.string.unable_to_connect_server),
						Toast.LENGTH_LONG).show();
			}

		}
	}

	@SuppressWarnings("unchecked")
	public void loadRecords() {

		if (rec_list.size() > 0) {
			tv_update.setVisibility(View.VISIBLE);
		} else {

			tv_update.setVisibility(View.GONE);

		}
		lay_viewer.removeAllViews();
		RelativeLayout.LayoutParams r_params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		r_params.topMargin = 10;
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < rec_list.size(); i++) {

			final String[] records = rec_list.get(i);
			final String[] clist = list.get(i);
			RelativeLayout relayout_parent = (RelativeLayout) layoutInflater
					.inflate(R.layout.from_viewerrow, null);
			relayout_parent.setId(i);
			LinearLayout rec_layout = (LinearLayout) relayout_parent
					.findViewById(R.id.llay_rwcontainer);

			ImageView iv_delete = (ImageView) relayout_parent
					.findViewById(R.id.iview_delrw);
			iv_delete.setBackgroundResource(R.drawable.row_delete);
			r_params.addRule(RelativeLayout.LEFT_OF, iv_delete.getId());
			rec_layout.setLayoutParams(r_params);
			rec_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					RelativeLayout rlayout = (RelativeLayout) v.getParent();
					Integer.toString(rlayout.getId());
					final String[] records = rec_list.get(rlayout.getId());

					showdialog(records);
				}

			});

			iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					RelativeLayout rlayout = (RelativeLayout) v.getParent();
					if (!CallDispatcher.isWifiClosed) {
						showDeleteAlert(rlayout.getId());

					} else {
						ShowError(
								SingleInstance.mainContext.getResources()
										.getString(R.string.network_err),
								SingleInstance.mainContext
										.getResources()
										.getString(R.string.network_unreachable));
					}
				}
			});

			if (clist.length == records.length) {
				stringArrayList = new ArrayList<String>();
				for (int idx = 0; idx < clist.length; idx++) {

					if (clist[idx].equalsIgnoreCase("buddyid")) {
						String rec = records[idx];
						stringArrayList.add(rec);
						TextView tview = new TextView(this);
						tview.setText(clist[idx] + " : " + records[idx]);

						tview.setTextColor(Color.BLACK);
						tview.setTextSize(13);
						tview.setPadding(10, 10, 0, 10);
						rec_layout.addView(tview);

					} else if (clist[idx].equalsIgnoreCase("Id")) {
						String rec = records[idx];
						stringArrayList.add(rec);
						TextView tview = new TextView(this);
						tview.setText(clist[idx] + " : " + records[idx]);
						tview.setVisibility(View.GONE);
						tview.setTextColor(Color.BLACK);
						tview.setTextSize(13);
						tview.setPadding(10, 10, 0, 10);
						rec_layout.addView(tview);

					}

					else if (clist[idx].equalsIgnoreCase("accesscode")) {

						if (records[idx].contains("1")) {

							value = "No Access";

						} else if (records[idx].contains("2")) {

							value = "View";

						} else if (records[idx].contains("3")) {

							value = "Add";
						} else if (records[idx].contains("4")) {

							value = "Complete";
						}

						else if (records[idx].contains("5")) {

							value = "Read & Add Own";

						} else {
							value = records[idx];

						}

						TextView tview = new TextView(this);
						tview.setText(clist[idx] + " : " + value);
						tview.setTextColor(Color.BLACK);
						tview.setTextSize(13);
						tview.setPadding(10, 10, 0, 10);
						rec_layout.addView(tview);

					} else if (clist[idx].equalsIgnoreCase("synccode")) {

						String rec = records[idx];

						if (records[idx].contains("1")) {

							value1 = "Never Sync";
						} else if (records[idx].contains("2")) {
							value1 = "Always Sync";
						} else if (records[idx].contains("3")) {
							value1 = "Sync Based on Rule";

						} else {
							value1 = rec;
						}
						TextView tview = new TextView(this);
						tview.setText(clist[idx] + " : " + value1);

						tview.setTextColor(Color.BLACK);
						tview.setTextSize(13);
						tview.setPadding(10, 10, 0, 10);
						rec_layout.addView(tview);
					}

					else {
						TextView tview = new TextView(this);
						tview.setText(clist[idx] + " : " + records[idx]);

						tview.setTextColor(Color.BLACK);
						tview.setTextSize(13);
						tview.setPadding(10, 10, 0, 10);
						rec_layout.addView(tview);
					}

				}
			}

			lay_viewer.addView(relayout_parent);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btn_cancel.getId()) {

			finish();
		} else if (v.getId() == btn_add.getId()) {

			final String elements[] = callDisp.getmyBuddysForAccess();

			if (elements.length + 1 == rec_list.size()) {
				showAlert("There are no contatcs");
			} else {
				Intent rec_intent = new Intent(cntxt, AccessAndSync.class);
				rec_intent.putExtra("owner", tbl_owner);
				rec_intent.putExtra("id", tbl_id);
				startActivity(rec_intent);
			}
		} else if (v.getId() == field_access_settings.getId()) {
			Intent rec_intent = new Intent(cntxt, FormFieldAccessActivity.class);
			rec_intent.putExtra("owner", tbl_owner);
			rec_intent.putExtra("formname", tbl_name);
			// rec_intent.putExtra("formname", "firm1");
			// rec_intent.putExtra("formid", "2");
			rec_intent.putExtra("formid", tbl_id);
			startActivity(rec_intent);
		}
	}

	public void refreshList() {

		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				rec_list.clear();
				list.clear();
				lay_viewer.removeAllViews();
				rec_list = callDisp.getdbHeler(cntxt).getRecordsofSettingtbl(
						tbl_id);
				list = callDisp.getdbHeler(cntxt).getColumnofSettingtbl(tbl_id);
				if (bmp_container.size() > 0) {
					for (Object bmp : bmp_container) {
						Bitmap b = (Bitmap) bmp;
						if (!b.isRecycled())
							b.recycle();
						b = null;

					}
					bmp_container.clear();
				}
				cancelDialog();
				loadRecords();
			}
		});

	}

	public boolean isShowingCurrentForm(String frm_id) {
		if (tbl_id.equals(frm_id))
			return true;
		else
			return false;
	}

	public String getId() {
		return this.tbl_id;
	}

	private void showDeleteAlert(final int id) {
		AlertDialog.Builder buider = new AlertDialog.Builder(cntxt);
		buider.setMessage(
				SingleInstance.mainContext.getResources().getString(
						R.string.are_you_sure_you_want_to_delete))
				.setPositiveButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								final String[] records = rec_list.get(id);
								Log.i("CALLL", "userID===>" + records[1]
										+ "===>" + records[3]);
								if (!records[1].equalsIgnoreCase(records[3])) {
									WebServiceReferences.webServiceClient
											.DeleteAccessForm(records[1],
													records[2], records[3],
													records[4], records[5], "",
													records[0], "delete", cntxt);
									showprogress();
								} else {
									Toast.makeText(
											cntxt,
											SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.cant_delete_users_permission),
											Toast.LENGTH_LONG).show();
								}

							}
						})
				.setNegativeButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
		AlertDialog alert = buider.create();
		alert.show();
	}

	private void showAlert(String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				FormPermissionViewer.this).create();

		alertDialog.setTitle("Response");
		alertDialog.setMessage(message);
		alertDialog.show();

	}

	public void notifyDeleteaccessWebServiceResponse(final Object obj) {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				if (obj instanceof String[]) {
					String[] response = (String[]) obj;

					String fsid = response[0];

					String del_row = "delete from formsettings where settingid='"
							+ fsid + "'";
					callDisp.getdbHeler(cntxt).ExecuteQuery(del_row);
					refreshList();
					cancelDialog();

				} else if (obj instanceof WebServiceBean) {
					WebServiceBean service_bean = (WebServiceBean) obj;

					showAlert(service_bean.getText());
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		WebServiceReferences.contextTable.remove("formpermissionviewer");

	}

	public void notifyWebServiceResponse(final Object object) {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				if (object instanceof Vector) {
					Vector<Object> resultObject = (Vector<Object>) object;
					for (int i = 0; i < resultObject.size(); i++) {
						if (resultObject.get(i) instanceof String[]) {
							String[] response = (String[]) resultObject.get(i);

							String buddy = response[0];
							String fsid = response[1];
							String formid = response[2];
							String createddate = response[3];
							String modifieddate = response[4];
							String syncquery = response[5];
							String permissionid = response[6];
							String syncid = response[7];
							String owner = response[8];

							int count = callDisp.getdbHeler(cntxt)
									.getreocrdcountUDP(formid, fsid, owner,
											buddy);

							if (count == 0) {

								String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
										+ "values('"
										+ fsid
										+ "','"
										+ owner
										+ "','"
										+ formid
										+ "','"
										+ buddy
										+ "','"
										+ permissionid
										+ "','"
										+ syncquery
										+ "','"
										+ ""
										+ "','"
										+ createddate
										+ "','"
										+ modifieddate
										+ "')";

								callDisp.getdbHeler(cntxt).ExecuteQuery(
										insertQuery1);

							} else {

								ContentValues cv = new ContentValues();

								cv.put("accesscode", permissionid);
								cv.put("synccode", syncid);
								cv.put("syncquery", syncquery);
								cv.put("datecreated", createddate);
								cv.put("datemodified", modifieddate);

								callDisp.getdbHeler(cntxt).updates(fsid, cv,
										formid);
								refreshList();
								WebServiceReferences.webServiceClient
										.Getcontent(CallDispatcher.LoginUser,
												formid, "", cntxt);

							}
						} else if (resultObject.get(i) instanceof Vector) {
							Vector<Vector<FormFieldBean>> fieldAccessList = (Vector<Vector<FormFieldBean>>) resultObject
									.get(i);
							Log.i("formfield123",
									"parse result totallist size : "
											+ fieldAccessList.size());
							Vector<FormFieldBean> ownerList = fieldAccessList
									.get(0);
							if (ownerList != null) {
								Log.i("formfield123",
										"parse result ownerlist size : "
												+ ownerList.size());
								for (FormFieldBean formFieldBean : ownerList) {
									if (formFieldBean
											.getDefaultPermissionList() != null) {
										Vector<DefaultPermission> dList = formFieldBean
												.getDefaultPermissionList();
										for (DefaultPermission defaultPermission : dList) {
											DBAccess.getdbHeler()
													.saveOrUpdateOwnerFormField(
															formFieldBean
																	.getFormId(),
															defaultPermission
																	.getAttributeId(),
															defaultPermission
																	.getDefaultPermission());
											Vector<BuddyPermission> bList = defaultPermission
													.getBuddyPermissionList();
											if (bList != null) {
												for (BuddyPermission buddyPermission : bList) {
													DBAccess.getdbHeler()
															.saveOrUpdateIndividualFormField(
																	formFieldBean
																			.getFormId(),
																	defaultPermission
																			.getAttributeId(),
																	buddyPermission
																			.getBuddyName(),
																	buddyPermission
																			.getBuddyAccess());
												}
											}

										}
									}

								}
							}
							Vector<FormFieldBean> individualList = fieldAccessList
									.get(1);
							if (individualList != null) {
								Log.i("formfield123",
										"parse result individuallist size : "
												+ individualList.size());
								for (FormFieldBean formFieldBean : individualList) {
									if (formFieldBean
											.getIndividualPermissionList() != null) {
										Vector<IndividualPermission> iBeanList = formFieldBean
												.getIndividualPermissionList();
										for (IndividualPermission iBean : iBeanList) {
											DBAccess.getdbHeler()
													.saveOrUpdateIndividualFormField(
															formFieldBean
																	.getFormId(),
															iBean.getAttributeId(),
															iBean.getUserName(),
															iBean.getPermission());
										}
									}
								}
							}
						}
					}
				} else if (object instanceof WebServiceBean) {
					WebServiceBean service_bean = (WebServiceBean) object;

					showAlert(service_bean.getText());
				}
			}
		});
	}

	public void notifyWebServiceResponseupdate(final Object obj) {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (obj instanceof String[]) {

					Log.i("xml", "inside activity==>");

					String[] response = (String[]) obj;

					String buddy = response[0];
					String fsid = response[1];
					String formid = response[2];
					String formcreatedtinme = response[3];
					String editedtime = response[4];
					String mode = response[5];
					if (mode.equals("edit")) {

						ContentValues cv = new ContentValues();

						cv.put("settingid", fsid);
						cv.put("buddyid", buddy);
						cv.put("accesscode", codeaccess);
						cv.put("synccode", codesync);
						cv.put("syncquery", updatequery);
						cv.put("datecreated", formcreatedtinme);
						cv.put("datemodified", editedtime);

						callDisp.getdbHeler(cntxt).updates(fsid, cv, formid);

						refreshList();
						cancelDialog();

					}

				} else if (obj instanceof WebServiceBean) {
					WebServiceBean service_bean = (WebServiceBean) obj;

					showAlert(service_bean.getText());
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
	}

}
