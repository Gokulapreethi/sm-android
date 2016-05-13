package com.cg.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.adapters.CutomContactAdapter;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class AccessAndSync extends Activity implements OnClickListener {

	private ArrayList<String[]> field_list = null;
	private List<accessSyncBean> listArray;
	private Context context = null;
	private EditText sql, query = null;
	private String Caccess, Cpermission = null;
	private String Finalaccess, Finalpermission, FinalQuery = null;
	private accessSyncBean objItem;
	private ProgressDialog dialog = null;
	private String querys = null;
	private Button IMRequest = null;
	private Button btn_cancel = null;
	private Button btn_delete = null;
	private CallDispatcher callDisp;
	private AlertDialog.Builder builder;
	private TextView title = null;
	private ArrayList<String[]> SAVEACCESS = null;
	private Handler wservice_handler = null;
	private ListView buddy_list = null;
	private String tableid = null;
	private String owener = null;
	private CutomContactAdapter adapter;
	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();
	private Button save = null;
	private LinearLayout layout_query = null;
	private String updatequery = null;
	private Button update, cancel = null;
	private Spinner accessspinner, syncspinner = null;
	private int selecteposition, syncselected;
	private String updatedaccess, updatedsync;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title1);

		context = this;

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		callDisp.setNoScrHeight(noScrHeight);
		callDisp.setNoScrWidth(noScrWidth);

		displaymetrics = null;
		WebServiceReferences.contextTable.put("formpermission", this);

		wservice_handler = new Handler();
		builder = new AlertDialog.Builder(this);
		title = (TextView) findViewById(R.id.note_date);
		title.setText("Access & Sync");
		SAVEACCESS = new ArrayList<String[]>();
		SAVEACCESS.clear();

		IMRequest = (Button) findViewById(R.id.im);
		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.one);
		save = (Button) findViewById(R.id.btncomp);
		save.setBackgroundResource(R.drawable.ic_action_save);
		save.setOnClickListener(this);
		btn_cancel = (Button) findViewById(R.id.settings);
		btn_cancel.setBackgroundResource(R.drawable.ic_action_back);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});
		btn_delete = (Button) findViewById(R.id.trash);
		btn_delete.setVisibility(View.INVISIBLE);

		setContentView(R.layout.acesssync);

		tableid = (String) getIntent().getStringExtra("id");

		owener = getIntent().getStringExtra("owner");

		field_list = callDisp.getdbHeler(context).getRecordsofSettingtbl(
				tableid);

		listArray = new ArrayList<accessSyncBean>();
		ArrayList<String> arraylist = new ArrayList<String>();

		final String buddys[] = callDisp.getmyBuddysForAccess();
		if (field_list.size() > 0) {
			List<String> wordList = Arrays.asList(buddys);
			arraylist.addAll(wordList);
			for (int i = 0; i < field_list.size(); i++) {

				final String[] records = field_list.get(i);

				for (int j = 0; j < wordList.size(); j++) {
					if (records[3].equalsIgnoreCase(wordList.get(j))) {

						arraylist.remove(wordList.get(j));

					}

				}

			}

		} else {
			List<String> wordList = Arrays.asList(buddys);
			arraylist.addAll(wordList);
		}

		for (int i = 0; i < arraylist.size(); i++) {
			objItem = new accessSyncBean();
			objItem.setId(i);
			objItem.setName(arraylist.get(i));
			objItem.setAccess("View");
			objItem.setsync("Always sync");
			listArray.add(objItem);

		}
		adapter = new CutomContactAdapter(this, listArray);

		buddy_list = (ListView) findViewById(R.id.mybuddies);

		buddy_list.setAdapter(adapter);

		buddy_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long arg3) {

				CheckBox chk = (CheckBox) v.findViewById(R.id.select_checkBox);
				accessSyncBean bean = listArray.get(position);
				if (bean.isSelected()) {

					bean.setSelected(true);
					chk.setChecked(true);

				} else {
					bean.setSelected(false);
					chk.setChecked(false);

				}

				if (v.getId() == R.id.permission) {
					showdialog(position);
				}

			}
		});

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		Menu mnu = menu;
		mnu.add(0, 1, Menu.NONE, "Delete");

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// if (WebServiceReferences.contextTable
		// .containsKey("formpermissionviewer")) {
		// WebServiceReferences.contextTable.remove("formpermissionviewer");
		// }
		if (WebServiceReferences.contextTable.containsKey("formpermission")) {
			WebServiceReferences.contextTable.remove("formpermission");
		}

	}

	private void showAlert(String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				AccessAndSync.this).create();

		alertDialog.setTitle("Response");

		alertDialog.setMessage(message);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				alertDialog.dismiss();
				finish();
			}
		});

		alertDialog.show();

	}

	private void showdialog(final int pos) {
		final String[] choiceList = { "No Access", "View", "Add", "Complete",
				"Read & Add Own" };
		final String[] synclist = { "Always sync", "Never Sync",
				"Sync Based on Rule" };

		final Dialog dialog = new Dialog(AccessAndSync.this);
		dialog.setContentView(R.layout.editdialog);
		dialog.setTitle(SingleInstance.mainContext.getResources().getString(R.string.access_and_sync));
		layout_query = (LinearLayout) dialog.findViewById(R.id.layout_query);
		accessspinner = (Spinner) dialog.findViewById(R.id.access_spinner);
		syncspinner = (Spinner) dialog.findViewById(R.id.sync_spinner);
		query = (EditText) dialog.findViewById(R.id.query);

		ArrayAdapter<String> aa = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinnerxml, choiceList);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accessspinner.setAdapter(aa);
		accessspinner.setSelection(selecteposition);

		accessspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				updatedaccess = choiceList[position].toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		ArrayAdapter<String> sync = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinnerxml, synclist);
		sync.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		syncspinner.setAdapter(sync);
		syncspinner.setSelection(syncselected);

		syncspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

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
				// TODO Auto-generated method stub

			}

		});

		update = (Button) dialog.findViewById(R.id.update);
		update.setText("Submit");
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.i("XML", "pos==>" + pos + "===>" + updatedaccess);
				listArray.get(pos).setAccess(updatedaccess);
				listArray.get(pos).setsync(updatedsync);
				listArray.get(pos).setQuery(updatequery);
				adapter.notifyDataSetChanged();
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

	public void notifyWebServiceResponse(final Object obj) {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (obj instanceof String[]) {
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
						cv.put("datecreated", formcreatedtinme);
						cv.put("datemodified", editedtime);

						callDisp.getdbHeler(context).updates(formid, cv, buddy);
						// Intent intentfrms = new Intent(context,
						// FormSettings.class);
						// startActivity(intentfrms);
						if (WebServiceReferences.contextTable
								.containsKey("formpermissionviewer")) {
							FormPermissionViewer frm_activity = (FormPermissionViewer) WebServiceReferences.contextTable
									.get("formpermissionviewer");
							frm_activity.refreshList();
						}
						finish();

					}

					else if (mode.equals("new"))

					{
						if (FinalQuery == null || FinalQuery.isEmpty()) {

							FinalQuery = "";

							String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
									+ "values('"
									+ fsid
									+ "','"
									+ owener
									+ "','"
									+ formid
									+ "','"
									+ buddy
									+ "','"
									+ Finalaccess
									+ "','"
									+ Finalpermission
									+ "','"
									+ FinalQuery
									+ "','"
									+ formcreatedtinme
									+ "','"
									+ editedtime
									+ "')";

							if (callDisp.getdbHeler(context).ExecuteQuery(
									insertQuery1)) {

								if (SAVEACCESS.size() > 0) {

									SAVEACCESS.remove(0);
									friendsbuddy();

								}

								else {
									cancelDialog();

								}
							}

						} else {

							String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
									+ "values('"
									+ fsid
									+ "','"
									+ owener
									+ "','"
									+ formid
									+ "','"
									+ buddy
									+ "','"
									+ Finalaccess
									+ "','"
									+ Finalpermission
									+ "','"
									+ FinalQuery
									+ "','"
									+ formcreatedtinme
									+ "','"
									+ editedtime
									+ "')";

							if (callDisp.getdbHeler(context).ExecuteQuery(
									insertQuery1)) {

								if (SAVEACCESS.size() > 0) {

									SAVEACCESS.remove(0);

									friendsbuddy();

								}

								else {
									cancelDialog();

								}

							}

						}

					}

				} else if (obj instanceof WebServiceBean) {
					WebServiceBean service_bean = (WebServiceBean) obj;
					showAlert(service_bean.getText());
				}
			}
		});
	}

	private void friendsbuddy() {

		if (SAVEACCESS.size() > 0) {
			String[] accesslist = SAVEACCESS.get(0);
			Finalaccess = accesslist[3].toString();
			Finalpermission = accesslist[4].toString();
			FinalQuery = accesslist[5].toString();
			WebServiceReferences.webServiceClient.SaveAccessForm(owener,
					tableid, accesslist[2], accesslist[3], accesslist[4],
					accesslist[5], "", "new", context,null);
			showprogress();
		}

		else {
			cancelDialog();
		}
	}

	private void showprogress() {

		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();
	}

	private void cancelDialog() {
		if (dialog != null) {
			dialog.dismiss();
			if (WebServiceReferences.contextTable
					.containsKey("formpermissionviewer")) {
				FormPermissionViewer frm_activity = (FormPermissionViewer) WebServiceReferences.contextTable
						.get("formpermissionviewer");
				frm_activity.refreshList();
			}
			finish();

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
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.unable_to_connect_server),
						Toast.LENGTH_LONG).show();

			}

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        AppMainActivity.inActivity = this;
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btncomp) {
			if (!CallDispatcher.isWifiClosed) {
				for (accessSyncBean bean : listArray) {
					String[] recs = new String[8];

					if (bean.isSelected()) {
						recs[0] = owener;
						recs[1] = tableid;
						recs[2] = bean.getName();
						recs[6] = "";
						recs[7] = "new";

						if (bean.getsync().equalsIgnoreCase("Always sync")) {

							Cpermission = "S02";
							recs[4] = Cpermission;
							querys = "";
							recs[5] = querys;

						}
						if (bean.getsync().equalsIgnoreCase("Never Sync")) {
							Cpermission = "S01";
							recs[4] = Cpermission;

							querys = "";
							recs[5] = querys;

						}
						if (bean.getsync().equalsIgnoreCase(
								"Sync Based on Rule")) {
							Cpermission = "S03";
							recs[4] = Cpermission;

							querys = sql.getText().toString();
							recs[5] = querys;

						}
						if (bean.getaccess().equalsIgnoreCase("No Access")) {

							Caccess = "A01";
							recs[3] = Caccess;

						}
						if (bean.getaccess().equalsIgnoreCase("View")) {

							Caccess = "A02";
							recs[3] = Caccess;

						}
						if (bean.getaccess().equalsIgnoreCase("Add")) {

							Caccess = "A03";
							recs[3] = Caccess;

						}
						if (bean.getaccess().equalsIgnoreCase("Complete")) {

							Caccess = "A04";
							recs[3] = Caccess;

						}
						if (bean.getaccess().equalsIgnoreCase("Read & Add Own")) {

							Caccess = "A05";
							recs[3] = Caccess;

						}

						SAVEACCESS.add(recs);

					}

				}
				friendsbuddy();
			} else {
				ShowError(
						SingleInstance.mainContext.getResources().getString(
								R.string.network_err),
						SingleInstance.mainContext.getResources().getString(
								R.string.network_unreachable));
			}

		}
	}

}
