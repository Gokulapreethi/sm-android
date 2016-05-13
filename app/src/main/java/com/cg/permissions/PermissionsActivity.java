package com.cg.permissions;

import java.util.Vector;

import org.lib.model.PermissionBean;
import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;

public class PermissionsActivity extends Activity implements OnClickListener {

	private Context context;

	private CallDispatcher call_disp;

	private TextView tv_back;

	private TextView tv_savepermission;

	private ListView permission_list;

	private PermissionAdapter permissionAdapter;

	private String buddyname;

	private Vector<Permissions> permissionlist;

	private PermissionBean permission_bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		setContentView(R.layout.setpermission);
		WebServiceReferences.contextTable.put("permissionsactivity", context);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			call_disp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			call_disp = new CallDispatcher(context);

		call_disp.setNoScrHeight(noScrHeight);
		call_disp.setNoScrWidth(noScrWidth);
		displaymetrics = null;
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(this);
		tv_savepermission = (TextView) findViewById(R.id.tv_savepermission);
		tv_savepermission.setOnClickListener(this);
		permissionlist = new Vector<Permissions>();
		permission_list = (ListView) findViewById(R.id.permission_list);
		permission_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}
		});
		buddyname = getIntent().getStringExtra("buddy");
		getSettings();
		Log.d("permissions", "size()----->" + permissionlist.size());
		permissionAdapter = new PermissionAdapter(context,
				R.layout.permission_row, permissionlist);
		permission_list.setAdapter(permissionAdapter);
		permissionAdapter.notifyDataSetChanged();
	}
@Override
 protected void onResume() {
  super.onResume();
  AppMainActivity.inActivity = this;
 }

	public void getSettings() {
		permissionlist.clear();
		PermissionBean bean = call_disp.getdbHeler(context).selectPermissions(
				"select * from setpermission where userid='"
						+ CallDispatcher.LoginUser + "' and buddyid='"
						+ buddyname + "'", CallDispatcher.LoginUser, buddyname);
		Log.d("permissions", "came to getsettings" + bean);
		if (bean != null) {

			Permissions ac_permissions = new Permissions();
			ac_permissions.setPermission_name("Audio Call");
			if (bean.getAudio_call().equals("1"))
				ac_permissions.setPermission(true);
			else
				ac_permissions.setPermission(false);
			permissionlist.add(ac_permissions);

			Permissions vc_permissions = new Permissions();
			vc_permissions.setPermission_name("Video Call");
			if (bean.getVideo_call().equals("1"))
				vc_permissions.setPermission(true);
			else
				vc_permissions.setPermission(false);
			permissionlist.add(vc_permissions);

			Permissions abc_permissions = new Permissions();
			abc_permissions.setPermission_name("Audio BroadCast");
			if (bean.getABC().equals("1"))
				abc_permissions.setPermission(true);
			else
				abc_permissions.setPermission(false);
			permissionlist.add(abc_permissions);

			Permissions vbc_permissions = new Permissions();
			vbc_permissions.setPermission_name("Video BroadCast");
			if (bean.getVBC().equals("1"))
				vbc_permissions.setPermission(true);
			else
				vbc_permissions.setPermission(false);
			permissionlist.add(vbc_permissions);

			Permissions auc_permissions = new Permissions();
			auc_permissions.setPermission_name("Audio UniCast");
			if (bean.getAUC().equals("1"))
				auc_permissions.setPermission(true);
			else
				auc_permissions.setPermission(false);
			permissionlist.add(auc_permissions);

			Permissions vuc_permissions = new Permissions();
			vuc_permissions.setPermission_name("Video UniCast");
			if (bean.getVUC().equals("1"))
				vuc_permissions.setPermission(true);
			else
				vuc_permissions.setPermission(false);
			permissionlist.add(vuc_permissions);

			Permissions mm_permissions = new Permissions();
			mm_permissions.setPermission_name("MM Chat");
			if (bean.getMMchat().equals("1"))
				mm_permissions.setPermission(true);
			else
				mm_permissions.setPermission(false);
			permissionlist.add(mm_permissions);

			Permissions tm_permissions = new Permissions();
			tm_permissions.setPermission_name("Text Message");
			if (bean.getTextMessage().equals("1"))
				tm_permissions.setPermission(true);
			else
				tm_permissions.setPermission(false);
			permissionlist.add(tm_permissions);

			Permissions am_permissions = new Permissions();
			am_permissions.setPermission_name("Audio Message");
			if (bean.getAudioMessage().equals("1"))
				am_permissions.setPermission(true);
			else
				am_permissions.setPermission(false);
			permissionlist.add(am_permissions);

			Permissions vm_permissions = new Permissions();
			vm_permissions.setPermission_name("Video Message");
			if (bean.getVideoMessage().equals("1"))
				vm_permissions.setPermission(true);
			else
				vm_permissions.setPermission(false);
			permissionlist.add(vm_permissions);

			Permissions ph_permissions = new Permissions();
			ph_permissions.setPermission_name("Photo Message");
			if (bean.getPhotoMessage().equals("1"))
				ph_permissions.setPermission(true);
			else
				ph_permissions.setPermission(false);

			permissionlist.add(ph_permissions);

			Permissions sk_permissions = new Permissions();
			sk_permissions.setPermission_name("Sketch Message");
			if (bean.getSketchMessage().equals("1"))
				sk_permissions.setPermission(true);
			else
				sk_permissions.setPermission(false);
			permissionlist.add(sk_permissions);

//			Permissions sp_permissions = new Permissions();
//			sp_permissions.setPermission_name("Share Profile");
//			if (bean.getShareProfile().equals("1"))
//				sp_permissions.setPermission(true);
//			else
//				sp_permissions.setPermission(false);
//			permissionlist.add(sp_permissions);

			Permissions vp_permissions = new Permissions();
			vp_permissions.setPermission_name("View Profile");
			if (bean.getViewProfile().equals("1"))
				vp_permissions.setPermission(true);
			else
				vp_permissions.setPermission(false);
			permissionlist.add(vp_permissions);

			Permissions clone_permissions = new Permissions();
			clone_permissions.setPermission_name("Avatar");
			if (bean.getAvtaar().equals("1"))
				clone_permissions.setPermission(true);
			else
				clone_permissions.setPermission(false);
			permissionlist.add(clone_permissions);

			// Permissions fs_permissions = new Permissions();
			// fs_permissions.setPermission_name("Form Share");
			// if (bean.getFormshare().equals("1"))
			// fs_permissions.setPermission(true);
			// else
			// fs_permissions.setPermission(false);
			// permissionlist.add(fs_permissions);
			//
			// Permissions hc_permissions = new Permissions();
			// hc_permissions.setPermission_name("Hosted Conference Call");
			// if (bean.getHostedconf().equals("1"))
			// hc_permissions.setPermission(true);
			// else
			// hc_permissions.setPermission(false);
			// permissionlist.add(hc_permissions);

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("permissionsactivity");
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_savepermission:
			savePermission();
			break;
		default:
			break;
		}
	}

	public class PermissionAdapter extends ArrayAdapter<Permissions> {

		private Vector<Permissions> p_list;

		public PermissionAdapter(Context context, int resource,
				Vector<Permissions> list) {
			super(context, resource, list);
			p_list = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null)
				convertView = inflater.inflate(R.layout.permission_row, parent,
						false);

			final Permissions permissions = p_list.get(position);

			TextView tv_name = (TextView) convertView
					.findViewById(R.id.tv_permissionname);
			tv_name.setText(permissions.getPermission_name());

			CheckBox cb_selectpermission = (CheckBox) convertView
					.findViewById(R.id.ch_setpermission);
			cb_selectpermission
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							permissions.setPermission(isChecked);
						}
					});

			cb_selectpermission.setChecked(permissions.isPermission());

			return convertView;
		}

	}

	public void savePermission() {
		if (permissionlist.size() > 0) {
			PermissionBean permissionBean = new PermissionBean();
			permissionBean.setUserId(CallDispatcher.LoginUser);
			permissionBean.setBuddyId(buddyname);
			for (Permissions permissions : permissionlist) {
				if (permissions.getPermission_name().equalsIgnoreCase(
						"audio call")) {
					if (permissions.isPermission())
						permissionBean.setAudio_call("1");
					else
						permissionBean.setAudio_call("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"video call")) {
					if (permissions.isPermission())
						permissionBean.setVideo_call("1");
					else
						permissionBean.setVideo_call("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"audio broadcast")) {
					if (permissions.isPermission())
						permissionBean.setABC("1");
					else
						permissionBean.setABC("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"video broadcast")) {
					if (permissions.isPermission())
						permissionBean.setVBC("1");
					else
						permissionBean.setVBC("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"audio unicast")) {
					if (permissions.isPermission())
						permissionBean.setAUC("1");
					else
						permissionBean.setAUC("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"video unicast")) {
					if (permissions.isPermission())
						permissionBean.setVUC("1");
					else
						permissionBean.setVUC("0");
				}
				if (permissions.getPermission_name()
						.equalsIgnoreCase("mm chat")) {
					if (permissions.isPermission())
						permissionBean.setMMchat("1");
					else
						permissionBean.setMMchat("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"text message")) {
					if (permissions.isPermission())
						permissionBean.setTextMessage("1");
					else
						permissionBean.setTextMessage("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"audio message")) {
					if (permissions.isPermission())
						permissionBean.setAudioMessage("1");
					else
						permissionBean.setAudioMessage("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"video message")) {
					if (permissions.isPermission())
						permissionBean.setVideoMessage("1");
					else
						permissionBean.setVideoMessage("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"photo message")) {
					if (permissions.isPermission())
						permissionBean.setPhotoMessage("1");
					else
						permissionBean.setPhotoMessage("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"sketch message")) {
					if (permissions.isPermission())
						permissionBean.setSketchMessage("1");
					else
						permissionBean.setSketchMessage("0");
				}
//				if (permissions.getPermission_name().equalsIgnoreCase(
//						"share profile")) {
//					if (permissions.isPermission())
//						permissionBean.setShareProfile("1");
//					else
//						permissionBean.setShareProfile("0");
//				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"view profile")) {
					if (permissions.isPermission())
						permissionBean.setViewProfile("1");
					else
						permissionBean.setViewProfile("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase("avatar")) {
					if (permissions.isPermission())
						permissionBean.setAvtaar("1");
					else
						permissionBean.setAvtaar("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"form share")) {
					if (permissions.isPermission())
						permissionBean.setFormshare("1");
					else
						permissionBean.setFormshare("0");
				}
				if (permissions.getPermission_name().equalsIgnoreCase(
						"hosted conference call")) {
					if (permissions.isPermission())
						permissionBean.setHostedconf("1");
					else
						permissionBean.setHostedconf("0");
				}

			}
			if (WebServiceReferences.running) {
				CallDispatcher.pdialog = new ProgressDialog(context);
				call_disp.showprogress(CallDispatcher.pdialog, context);
				this.permission_bean = permissionBean;
				WebServiceReferences.webServiceClient
						.setPermission(permissionBean);
			}
		}
	}

	public void notifySetPermissionResponse(Object obj) {
		call_disp.cancelDialog();
		if (obj instanceof String[]) {
			String[] result = (String[]) obj;
			if (result[0] != null) {
				if (result[0].trim().equalsIgnoreCase(
						"Permissions added successfully")) {
					ContentValues cv = new ContentValues();
					Log.d("permissions", "bean--->" + permission_bean);

					if (permission_bean != null) {
						cv.put("userid", CallDispatcher.LoginUser);
						cv.put("buddyid", buddyname);
						cv.put("audiocall", Integer.parseInt(permission_bean
								.getAudio_call()));
						cv.put("videocall", Integer.parseInt(permission_bean
								.getVideo_call()));
						cv.put("audiobroadcast",
								Integer.parseInt(permission_bean.getABC()));
						cv.put("videobroadcast",
								Integer.parseInt(permission_bean.getVBC()));
						cv.put("audiounicast",
								Integer.parseInt(permission_bean.getAUC()));
						cv.put("videounicast",
								Integer.parseInt(permission_bean.getVUC()));
						cv.put("mmchat",
								Integer.parseInt(permission_bean.getMMchat()));
						cv.put("textmessage", Integer.parseInt(permission_bean
								.getTextMessage()));
						cv.put("audiomessage", Integer.parseInt(permission_bean
								.getAudioMessage()));
						cv.put("videomessage", Integer.parseInt(permission_bean
								.getVideoMessage()));
						cv.put("photomessage", Integer.parseInt(permission_bean
								.getPhotoMessage()));
						cv.put("sketchmessage", Integer
								.parseInt(permission_bean.getSketchMessage()));
//						cv.put("shareprofile", Integer.parseInt(permission_bean
//								.getShareProfile()));
						cv.put("viewprofile", Integer.parseInt(permission_bean
								.getViewProfile()));
						cv.put("answeringmachine",
								Integer.parseInt(permission_bean.getAvtaar()));
						// cv.put("formshare", Integer.parseInt(permission_bean
						// .getFormshare()));
						// cv.put("hostedconf", Integer.parseInt(permission_bean
						// .getHostedconf()));
						cv.put("record_time", permission_bean.getRecordTime());
						if (!call_disp.getdbHeler(context).isRecordExists(
								"select * from setpermission where userid='"
										+ CallDispatcher.LoginUser
										+ "' and buddyid='" + buddyname + "'")) {
							int row_count = call_disp.getdbHeler(context)
									.insertPermission(cv);
							Log.d("permissions", "inserted Row id is--->"
									+ row_count);
						} else {
							int row_count = call_disp.getdbHeler(context)
									.updatePermission(
											cv,
											"userid='"
													+ CallDispatcher.LoginUser
													+ "' and buddyid='"
													+ buddyname + "'");
							Log.d("permissions", "updated Row id is--->"
									+ row_count);
						}
//						showAlert(result[0]+ "to " +buddyname +". It take sometimes to reflect over the recipient side", true);
						showAlert("Access modified for " +buddyname +", changes will be reflected in recipient side after sometime", true);

					}
				} else
					showAlert(result[0], true);
			} else
				showAlert("Unable to save permission", true);

		} else if (obj instanceof WebServiceBean) {
			showAlert(((WebServiceBean) obj).getText(), true);
		}
	}

	private void showAlert(String message, final boolean flag) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.setTitle("Response");
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				if (flag)
					finish();
			}
		});
		alertDialog.show();

	}
}
