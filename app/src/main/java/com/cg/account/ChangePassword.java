package com.cg.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.main.AppMainActivity;
import com.main.SettingsFragment;
import com.util.SingleInstance;

import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;

public class ChangePassword extends Fragment {

	static Context context = null;
	private AppMainActivity appMainActivity = null;

	TextView oldpassword = null;
	EditText et_oldpassword = null;

	TextView newpassword = null;
	EditText et_newpassword = null;

	TextView repeatpassword = null;
	EditText et_repeatpassword = null;

	Button save = null;

	String loginuser;

	private boolean isActivityPause = false;
	private ProgressDialog progDailog = null;
	Handler handler = new Handler();
	private static CallDispatcher calldisp = null;

	private SharedPreferences preferences;
	private static ChangePassword changePassword = null;
	public View _rootView;
	public static ChangePassword newInstance(Context context) {
		try {
			if (changePassword == null) {
				changePassword = new ChangePassword();
				changePassword.setContext(context);
				calldisp = CallDispatcher.getCallDispatcher(context);

			}
			return changePassword;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return changePassword;
		}
	}
	public void setContext(Context cxt) {
		this.context = cxt;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Button select = (Button) getActivity().findViewById(R.id.btn_brg);
		select.setVisibility(View.GONE);
		RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
		mainHeader.setVisibility(View.VISIBLE);
		LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
		contact_layout.setVisibility(View.GONE);

		Button imVw = (Button) getActivity().findViewById(R.id.im_view);
		imVw.setVisibility(View.GONE);

		final Button search1 = (Button) getActivity().findViewById(R.id.btn_settings);
		search1.setVisibility(View.GONE);

		final Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
		plusBtn.setVisibility(View.GONE);

		TextView title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		title.setVisibility(View.VISIBLE);
		title.setText("PASSWORD");
		appMainActivity = (AppMainActivity) SingleInstance.contextTable.get("MAIN");

		Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
		backBtn.setVisibility(View.VISIBLE);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				appMainActivity.removeFragments(ChangePassword.newInstance(SingleInstance.mainContext));
				SettingsFragment settingsFragment = SettingsFragment.newInstance(context);
				FragmentManager fragmentManager = SingleInstance.mainContext
						.getSupportFragmentManager();
				fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				fragmentManager.beginTransaction().replace(
						R.id.activity_main_content_fragment, settingsFragment)
						.commitAllowingStateLoss();
			}
		});
		_rootView = null;
		if (_rootView == null) {

			_rootView = inflater.inflate(R.layout.changepassword, null);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			try {
				et_oldpassword = (EditText)_rootView. findViewById(R.id.et_oldpassword);
				et_newpassword = (EditText) _rootView.findViewById(R.id.et_newpassword);
				et_repeatpassword = (EditText)_rootView. findViewById(R.id.et_repeatpassword);
				oldpassword = (TextView)_rootView. findViewById(R.id.oldpassword);
				newpassword = (TextView)_rootView. findViewById(R.id.newpassword);
				repeatpassword = (TextView) _rootView.findViewById(R.id.repeatpassword);
				save = (Button)_rootView. findViewById(R.id.savepswd);

				setSave();
				preferences = PreferenceManager.getDefaultSharedPreferences(SingleInstance.mainContext);
				WebServiceReferences.contextTable.put("ChangePassword", context);
				et_oldpassword.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
					}

					@Override
					public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
						if (charSequence.length() > 0) {
							oldpassword.setVisibility(View.VISIBLE);
						} else {
							oldpassword.setVisibility(View.GONE);
						}
						setSave();
					}

					@Override
					public void afterTextChanged(Editable editable) {
					}
				});

				et_newpassword.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
					}

					@Override
					public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
						if (charSequence.length() > 0) {
							newpassword.setVisibility(View.VISIBLE);
						} else {
							newpassword.setVisibility(View.GONE);
						}
						setSave();
					}

					@Override
					public void afterTextChanged(Editable editable) {
					}
				});

				et_repeatpassword.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
					}

					@Override
					public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
						if (charSequence.length() > 0) {
							repeatpassword.setVisibility(View.VISIBLE);
						} else {
							repeatpassword.setVisibility(View.GONE);
						}
						setSave();
					}

					@Override
					public void afterTextChanged(Editable editable) {
					}
				});

				save.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (et_oldpassword.getText().toString().trim().length() > 1
								&& et_oldpassword.getText().toString().trim().length() < 12
								&& et_newpassword.getText().toString().trim().length() > 1
								&& et_newpassword.getText().toString().trim().length() < 12
								&& et_repeatpassword.getText().toString().trim().length() > 1
								&& et_repeatpassword.getText().toString().trim().length() < 12) {

							if (!et_oldpassword.getText().toString().trim()
									.equals(et_newpassword.getText().toString().trim())) {
								if (et_newpassword.getText().toString().trim()
										.equals(et_repeatpassword.getText().toString().trim())) {
									progDailog = ProgressDialog.show(context,"Progress ", "Please wait", true);
									progDailog.setOnDismissListener(new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											if (progDailog != null) {
												progDailog.dismiss();
											}
										}
									});
									progDailog.setCancelable(true);
									String[] parm = {CallDispatcher.LoginUser,
											et_oldpassword.getText().toString(),
											et_newpassword.getText().toString()};
									WebServiceReferences.webServiceClient
											.changePassword(parm, changePassword);
									Editor editor = preferences.edit();
									editor.putString("pword", et_newpassword.getText()
											.toString());
									editor.commit();
								} else {
									Toast.makeText(context,"Password Mismatch",Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(context,"Old Password and New Password are same",Toast.LENGTH_SHORT).show();
								showToast(SingleInstance.mainContext.getResources().getString(R.string.old_Password_and_new_password_are_same));
							}

						} else {
							Toast.makeText(context,"Must be atleast 2 char and maximum of 11char",Toast.LENGTH_SHORT).show();
						}
					}

					// TODO Auto-generated method stub

				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else
			((ViewGroup) _rootView.getParent()).removeView(_rootView);
		return _rootView;
	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public void setSave(){
		if ((et_newpassword.getText().toString().length() > 0) && (et_oldpassword.getText().toString().length() > 0) &&
				(et_repeatpassword.getText().toString().length() > 0)){
			save.setEnabled(true);
		}else{
			save.setEnabled(false);
		}
	}

	public View getParentView() {
		return _rootView;
	}
	public void notifyWebServiceResponse(Servicebean servicebean) {
		final WebServiceBean sbean = (WebServiceBean) servicebean.getObj();
		Log.d("message", "RESPONSE comes from dispatcher" + sbean.getText());
		handler.post(new Runnable() {
			@Override
			public void run() {
				String title = "";
				String Response = sbean.getText();
				try {
					if (Response.contains("successfully")) {
						if (progDailog != null) {
							progDailog.dismiss();
							if (AppReference.isWriteInFile)
								AppReference.logger
										.debug("LOGOUT : FROM CHANGEPASSWORD");
							showAlert1(
									"",
									"Password changed successfully, you will have to signout to activate the change",
									true);
							progDailog = null;

						}

					} else {
						title = "Change Password Error";
						if (progDailog != null) {
							progDailog.cancel();
							progDailog = null;

						}

						if (!isActivityPause) {
							showAlert1(title, Response, false);
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
	}

	public void showAlert1(String title, String message, final boolean isLogout) {
		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
		alertCall.setTitle(title).setMessage(message).setCancelable(false)
				.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							if (isLogout) {
								appMainActivity.showprogress();
								((AppMainActivity) SingleInstance.contextTable
										.get("MAIN")).logout(true);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		alertCall.show();
	}
}
