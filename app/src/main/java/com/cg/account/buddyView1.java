package com.cg.account;

import java.util.ArrayList;

import org.lib.model.SubscribeBean;
import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.SlideMenu.SlideMenu;
import com.cg.SlideMenu.SlideMenuInterface;
import com.cg.SlideMenu.Slidebean;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.profiles.BusCard;
import com.cg.profiles.ViewProfiles;
import com.main.AppMainActivity;
import com.util.SingleInstance;

/**
 * This class is mainly used for Login and Registration. This class uses
 * webService for Login and Registration.
 * 
 * 
 * 
 */
public class buddyView1 extends Activity implements
		SlideMenuInterface.OnSlideMenuItemClickListener {

	private CallDispatcher calldisp;

	private SharedPreferences preferences;

	private Context context;

	private Handler service_handler;

	// For Login

	private EditText edPswd;

	private EditText edUserName;

	private boolean isfirst_launch = false;

	private SlideMenu slidemenu;

	private Button btn_menu;

	private Button btnLogin = null;

	private Button btn_component;

	private TextView tv_title;

	// Date Format

	private RadioButton dateFormat = null;

	private String dateString = "dd-MM-yyyy";

	// For Registration

	private RelativeLayout question1, question2, question3, secretques;

	private ImageView image1, image2, image3;

	private EditText etsecAns, etsecAnswer;

	private TextView tvques1, tvques2, tvques3, selectedquestion, tvdone;

	private String SecretQuestion;

	private Button btn_secretquestion, btn_back;

	private CustomViewFlipper vf;

	private Dialog dialogx = null;
	private boolean isTextClicked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title1);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		service_handler = new Handler();
		context = this;

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			calldisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			calldisp = new CallDispatcher(context);

		calldisp.setNoScrHeight(noScrHeight);
		calldisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;

		isfirst_launch = getIntent().getBooleanExtra("isFirstlogin", false);

		CallDispatcher.isConnected = calldisp.isOnLine(context);
		btn_menu = (Button) findViewById(R.id.settings);
		btn_component = (Button) findViewById(R.id.btncomp);
		tv_title = (TextView) findViewById(R.id.note_date);

		btn_menu.setBackgroundResource(R.drawable.icon_sidemenu);
		tv_title.setText("Sign in");
		btn_component.setVisibility(View.GONE);

		WebServiceReferences.contextTable.put("buddyView1", context);
		final LinearLayout ChangeView = new LinearLayout(this);
		ChangeView.setBackgroundColor(Color.parseColor("#E6E7E7"));
		ChangeView.setOrientation(LinearLayout.VERTICAL);
		ChangeView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		ChangeView.addView(Loginscreen1());
		setContentView(ChangeView);

	}

	/**
	 * This Method is Used to Generate Login Screen.It is also used to validate
	 * the InputFields.It also support for Login Process. It also used to save
	 * the userName and Password as per User Setings.
	 * 
	 * @return LoginScreen
	 */
	public LinearLayout Loginscreen1() {

		WebServiceReferences.contextTable.put("SIGNIN", this);
		final String username = preferences.getString("uname", null);
		String password = preferences.getString("pword", null);
		boolean rememberpassword = preferences.getBoolean("remember", false);
		boolean autologinstate = preferences.getBoolean("autologin", false);

		LayoutInflater inflateLayout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout callControls = (LinearLayout) inflateLayout.inflate(
				R.layout.loginscreen, null);

		edUserName = (EditText) callControls.findViewById(R.id.etLoginuser);
		if (username != null)
			edUserName.setText(username);
//		slidemenu = (SlideMenu) callControls.findViewById(R.id.login_slideMenu);
		ArrayList<Slidebean> menu_items = new ArrayList<Slidebean>();
		calldisp.composeList(menu_items);
		slidemenu.init(buddyView1.this, menu_items, this, 100);
		btn_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				slidemenu.show();
			}
		});
		edPswd = (EditText) callControls.findViewById(R.id.etloginpassword);
		if (password != null)
			edPswd.setText(password);
		edPswd.setFilters(getpasswordFilter());
		edPswd.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		edUserName.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					edPswd.setText("");
				}
				return false;
			}
		});
		edUserName.addTextChangedListener(new TextWatcher() {
			int start = 0, before = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				this.start = start;
				this.before = before;

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (start != 0 && before != 0)
					edPswd.setText("");

			}
		});

		if (rememberpassword || autologinstate) {
			{
				if (password != null)
					edPswd.setText(password.trim());
			}

			if (username != null) {
				edUserName.setText(username.trim());
			}

		}

		btnLogin = (Button) callControls.findViewById(R.id.btnLogin);

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doSignin();
			}
		});
		if (autologinstate && isfirst_launch) {
			if (edUserName.getText().toString().trim().length() > 0
					&& edPswd.getText().toString().trim().length() > 0) {
				login(username, password);
			}
		}

		Button Register = (Button) callControls.findViewById(R.id.btnRegisterOk);

		Register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CallDispatcher.isConnected) {
					registrationScreen1();
				} else {
					showAlert1("Could not connect to the server");

				}
			}
		});

		TextView forgetpassword = (TextView) callControls
				.findViewById(R.id.btnforgotpswd);
		forgetpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CallDispatcher.isConnected) {
					Intent i = new Intent(context, getnewpassword.class);
					startActivity(i);
				} else {
					Toast.makeText(context,
							"Please check your internet connection",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		TextView changeDateFormat = (TextView) callControls
				.findViewById(R.id.text);
		changeDateFormat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				String date = preferences
						.getString("dateformate", "MM/dd/yyyy");
				final String[] date_items = { "MM/dd/yyyy", "dd-MM-yyyy" };
				int pos = 0;
				if (date.equals("dd-MM-yyyy"))
					pos = 1;

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
				alertDialogBuilder.setSingleChoiceItems(date_items, pos,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Editor editor = preferences.edit();
								editor.putString("dateformate",
										date_items[which]);
								editor.commit();
								// WebServiceReferences.globaldateformat =
								// date_items[which];
								CallDispatcher.dateFormat = date_items[which];
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		});

		return callControls;
	}

	public void doSignin() {

		String urlx = preferences.getString("url", null);
		String portx = preferences.getString("port", null);
		Editor editor = preferences.edit();
		editor.putBoolean("isfirstlaunch", false);
		editor.commit();

		if (urlx != null && portx != null) {

			service_handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					try {

						String myPublicIp = CallDispatcher.getPublicipaddress();

						Log.d("call", "print public ip " + myPublicIp);
						// if (calldisp.mainbuddylist != null) {
						// calldisp.mainbuddylist.clear();
						// }

						String username = edUserName.getText().toString()
								.trim().toLowerCase();
						String password = edPswd.getText().toString().trim();

						login(username, password);

					} catch (Exception e) {
						Log.d("signn", "started progress " + e);
						e.printStackTrace();
					}
					//

				}
			});

			//
		} else {
			Toast.makeText(context, "Please check Settings", Toast.LENGTH_LONG)
					.show();

		}
		// a

	}

	@SuppressWarnings("deprecation")
	public void ShowError(String Title, String Message,
			final boolean hastofinish) {
		AlertDialog confirmation = new AlertDialog.Builder(context).create();
		confirmation.setTitle(Title);
		confirmation.setMessage(Message);
		confirmation.setCancelable(true);
		confirmation.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (hastofinish)
					finish();
			}
		});

		confirmation.show();
	}

	@Override
	protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
		if (CallDispatcher.LoginUser == null) {
			if (WebServiceReferences.contextTable.containsKey("buddiesList"))
				((buddiesList) WebServiceReferences.contextTable
						.get("buddiesList")).finish();
			if (WebServiceReferences.contextTable.containsKey("viewprofile"))
				((ViewProfiles) WebServiceReferences.contextTable
						.get("viewprofile")).finish();
			if (WebServiceReferences.contextTable.containsKey("buscard"))
				((BusCard) WebServiceReferences.contextTable
						.get("buscard")).finish();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("buddyView1");
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	private InputFilter[] getpasswordFilter() {
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (end > start) {

					char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
							'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
							'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
							'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
							'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
							'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
							'2', '3', '4', '5', '6', '7', '8', '9', '@', '.',
							'_', '#', '$', '%', '*', '-', '+', '(', ')', '!',
							'\'', ':', ';', '/', '?', ',', '~', '`', '|', '\\',
							'^', '{', '}', '[', ']', '=', '£', '¥', '€', '.',
							'¢', '•', '©' };

					for (int index = start; index < end; index++) {
						if (!new String(acceptedChars).contains(String
								.valueOf(source.charAt(index)))) {
							return "";
						}
					}
				}
				return null;
			}

		};
		return filters;

	}

	public void login(String username, String password)

	{
		// if (calldisp.mainbuddylist != null) {
		// calldisp.mainbuddylist.clear();
		// }
		boolean isCorrectSettings = checkSettings();
		if (username.length() != 0 && password.length() != 0
				&& isCorrectSettings) {
			ProgressDialog progress = new ProgressDialog(context);
			calldisp.showprogress(progress, context);
			progress.setCancelable(true);
			if (!WebServiceReferences.running) {
				String url = preferences.getString("url", null);
				String port = preferences.getString("port", null);
				if (url != null && port != null)
					calldisp.startWebService(url, port);
				else
					calldisp.startWebService(
							getResources().getString(R.string.service_url),
							"80");
				url = null;
				port = null;
			}
			// calldisp.SignIn(username, password);

			AppReference.isFormloaded = false;
		}

		else if (!isCorrectSettings) {
			calldisp.cancelDialog();
			Toast.makeText(context, "Please Check Settings", Toast.LENGTH_LONG)
					.show();
		} else {
			calldisp.cancelDialog();

			if (username.length() == 0 && password.length() != 0) {
				showAlert1("Enter User Id");
			} else if (username.length() != 0 && password.length() == 0) {
				showAlert1("Enter Password");
			} else {
				showAlert1("Please enter Username and Password");

			}
		}

	}

	@SuppressWarnings("finally")
	private boolean checkSettings() {
		boolean returnValue = true;
		String url = null;
		String loginIP = null;
		String urlPort = null;
		try {

			url = preferences.getString("url", null);
			loginIP = url.substring(url.indexOf("://") + 3);
			loginIP = loginIP.substring(0, loginIP.indexOf(":"));
			loginIP = loginIP.trim();
			urlPort = url.substring(url.indexOf("://") + 3);
			urlPort = urlPort.substring(urlPort.indexOf(":") + 1);
			urlPort = urlPort.substring(0, urlPort.indexOf("/"));
			if (urlPort.length() < 1 || loginIP.length() < 8)
				return false;

		} catch (Exception e) {
			returnValue = false;
			Log.d("error", "" + e);
		} finally {
			url = null;
			loginIP = null;
			urlPort = null;
			return returnValue;
		}

	}

	@Override
	public void onSlideMenuItemClick(int itemId, View v, Context context) {
		// TODO Auto-generated method stub
		switch (itemId) {
		case WebServiceReferences.CONTACTS:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.USERPROFILE:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.UTILITY:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.NOTES:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.APPS:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.CLONE:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.SETTINGS:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;

		case WebServiceReferences.QUICK_ACTION:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.FORMS:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.FEEDBACK:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.EXCHANGES:
			calldisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * This method is used to show the Registration Screen. The Registration
	 * screen is used to create an account. This method uses Server for
	 * Registration and also contains InBuild Validation of Fields.
	 */
	private void registrationScreen1() {
		dialogx = new Dialog(context, android.R.style.Theme_Black);
		dialogx.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflateLayout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout callControls = (RelativeLayout) inflateLayout.inflate(
				R.layout.registration, null);

//		question1 = (RelativeLayout) callControls.findViewById(R.id.question1);
//		question2 = (RelativeLayout) callControls.findViewById(R.id.question2);
//		question3 = (RelativeLayout) callControls.findViewById(R.id.question3);
		secretques = (RelativeLayout) callControls
				.findViewById(R.id.secretques);
//		tvdone = (TextView) callControls.findViewById(R.id.btnDone);
//		image1 = (ImageView) callControls.findViewById(R.id.tickone);
//		image2 = (ImageView) callControls.findViewById(R.id.ticktwo);
//		image3 = (ImageView) callControls.findViewById(R.id.tickthree);
//		etsecAns = (EditText) callControls.findViewById(R.id.etsecAns);
//		etsecAnswer = (EditText) callControls.findViewById(R.id.etsecAnswer);
//		etsecAnswer.setKeyListener(null);
//		tvques1 = (TextView) callControls.findViewById(R.id.tvques1);
//		tvques2 = (TextView) callControls.findViewById(R.id.tvques2);
//		tvques3 = (TextView) callControls.findViewById(R.id.tvques3);
		selectedquestion = (TextView) callControls
				.findViewById(R.id.selectedquestion);

//		vf = (CustomViewFlipper) callControls.findViewById(R.id.viewFlipper);

		btn_secretquestion = (Button) callControls
				.findViewById(R.id.btn_secretquestion);

//		btn_back = (Button) callControls.findViewById(R.id.btnsecreatanswer);

		final EditText edUser = (EditText) callControls
				.findViewById(R.id.etRegisuser);
		final EditText edPassword = (EditText) callControls
				.findViewById(R.id.etRegispassword);

		edUser.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (edUser.getText().toString().length() > 32) {
					Toast.makeText(context,
							"Your username maximum Limit reached",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		edPassword.setFilters(getpasswordFilter());
		edPassword.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		final EditText edRePassword = (EditText) callControls
				.findViewById(R.id.etRegisRepassword);

		edPassword.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (edPassword.getText().toString().length() > 32) {
					Toast.makeText(context,
							"Your Password maximum Limit reached",
							Toast.LENGTH_SHORT).show();
				}
				if (edPassword.getText().toString().length() != edRePassword
						.getText().toString().length()) {
					edRePassword.setText("");
				}

			}
		});

		edRePassword.setFilters(getpasswordFilter());
		edRePassword.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		edRePassword.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (edRePassword.getText().toString().length() > 32) {
					Toast.makeText(context,
							"Your Password maximum Limit reached",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		final EditText edPhone =null;

//		final EditText edPhone = (EditText) callControls
//				.findViewById(R.id.etphone);
//		edPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//		edPhone.addTextChangedListener(new TextWatcher() {
//			public void afterTextChanged(Editable s) {
//			}
//
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				if (edPhone.getText().toString().length() > 14) {
//					Toast.makeText(context,
//							"Your Phone Number maximum Limit reached",
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//		});

		final EditText edEmail = (EditText) callControls
				.findViewById(R.id.etemail);
		edEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

//		final EditText edAnswer = (EditText) callControls
//				.findViewById(R.id.etsecAnswer);

		question1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				image1.setVisibility(View.VISIBLE);
				tvques1.setTypeface(null, Typeface.BOLD);
				image2.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				tvques2.setTypeface(null, Typeface.NORMAL);
				tvques3.setTypeface(null, Typeface.NORMAL);
				isTextClicked = true;
				etsecAns.setInputType(InputType.TYPE_CLASS_TEXT);
				etsecAns.setText("");

			}
		});

		question2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				image2.setVisibility(View.VISIBLE);
				tvques2.setTypeface(null, Typeface.BOLD);
				image1.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				tvques1.setTypeface(null, Typeface.NORMAL);
				tvques3.setTypeface(null, Typeface.NORMAL);
				isTextClicked = true;
				etsecAns.setInputType(InputType.TYPE_CLASS_NUMBER);
				etsecAns.setText("");
			}
		});

		question3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				image3.setVisibility(View.VISIBLE);
				tvques3.setTypeface(null, Typeface.BOLD);
				image1.setVisibility(View.GONE);
				image2.setVisibility(View.GONE);
				tvques1.setTypeface(null, Typeface.NORMAL);
				tvques2.setTypeface(null, Typeface.NORMAL);
				isTextClicked = true;
				etsecAns.setInputType(InputType.TYPE_CLASS_TEXT);
				etsecAns.setText("");
			}
		});

		secretques.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				vf.setVisibility(View.VISIBLE);
				vf.showNext();
				return false;
			}
		});

		btn_secretquestion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				vf.setVisibility(View.VISIBLE);
				vf.showNext();
			}
		});

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				vf.showPrevious();
				image1.setVisibility(View.GONE);
				image2.setVisibility(View.GONE);
				image3.setVisibility(View.GONE);
				etsecAns.setText("");
				tvques1.setTypeface(null, Typeface.NORMAL);
				tvques2.setTypeface(null, Typeface.NORMAL);
				tvques3.setTypeface(null, Typeface.NORMAL);
				isTextClicked = false;
			}
		});

		tvdone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (isTextClicked) {
					if (!etsecAns.getText().toString().trim().equals("")) {

						if (image1.getVisibility() == View.VISIBLE) {
							SecretQuestion = tvques1.getText().toString();
						} else if (image2.getVisibility() == View.VISIBLE) {
							SecretQuestion = tvques2.getText().toString();
						}

						else if (image3.getVisibility() == View.VISIBLE) {
							SecretQuestion = tvques3.getText().toString();
						}

						String EtsecAnswer = etsecAns.getText().toString();

						vf.showPrevious();
						etsecAnswer.setText(EtsecAnswer);
						selectedquestion.setText(SecretQuestion);

					} else {
						Toast.makeText(context, "Please Enter the answer",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, "Please select the question",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		TextView save = (TextView) callControls
				.findViewById(R.id.btnRegisterOk);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					if (checkSettings()) {
						if (edUser.getText().toString().trim().length() > 0
								&& edPassword.getText().toString().trim()
										.length() > 0
								&& edRePassword.getText().toString().trim()
										.length() > 0
								&& edEmail.getText().toString().trim().length() > 0
//								&& edAnswer.getText().toString().trim()
//										.length() > 0
                                ) {
							if (edUser.getText().toString().trim().length() > 5
									&& edUser.getText().toString().trim()
											.length() < 34
									&& isAlphaNumeric(edUser.getText()
											.toString().trim())
									&& edPassword.getText().toString().trim()
											.length() > 5
									&& edPassword.getText().toString().trim()
											.length() < 34
									&& edRePassword.getText().toString().trim()
											.length() > 0
									&& edRePassword
											.getText()
											.toString()
											.trim()
											.equals(edPassword.getText()
													.toString().trim())
									&& (edPhone.getText().toString().trim()
											.length() == 0 || (edPhone
											.getText().toString().trim()
											.length() >= 10 && edPhone
											.getText().toString().trim()
											.length() <= 15))
									&& validateEmail(edEmail.getText()
											.toString().trim()))
//									&& edAnswer.getText().toString().trim()
//											.length() > 0)

							{
								SubscribeBean sb = new SubscribeBean();
								sb.setName(edUser.getText().toString());
								sb.setMobileNo(edPhone.getText().toString()
										.trim());
								sb.setPassword(edPassword.getText().toString()
										.trim());
								sb.setEmailId(edEmail.getText().toString()
										.trim());
								sb.setPhoto("NA");
								sb.setSecretQuestion(selectedquestion.getText()
										.toString().trim());
//								sb.setSecretAnswer(edAnswer.getText()
//										.toString().trim());
								if (!WebServiceReferences.running) {
									String url = preferences.getString("url",
											null);
									String port = preferences.getString("port",
											null);
									if (url != null && port != null)
										calldisp.startWebService(url, port);
									else
										calldisp.startWebService(
												getResources().getString(
														R.string.service_url),
												"80");
									url = null;
									port = null;
								}
								ProgressDialog progressDialog = new ProgressDialog(
										context);
								calldisp.showprogress(progressDialog, context);
								// WebServiceReferences.webServiceClient
								// .subscribe(sb);

							} else {
								if (edUser.getText().toString().trim().length() <= 5) {

									edUser.requestFocus();
									Toast.makeText(context,
											"Username must be 6-33 characters",
											Toast.LENGTH_SHORT).show();
								} else if (edUser.getText().toString().trim()
										.length() > 33) {

									edUser.requestFocus();
									Toast.makeText(
											context,
											"Username must be less than 34 characters",
											Toast.LENGTH_SHORT).show();

								} else if (!isAlphaNumeric(edUser.getText()
										.toString().trim())) {
									Toast.makeText(
											context,
											"Enter Alpha Numeric characters.The first letter must be Alpha value.",
											Toast.LENGTH_SHORT).show();

								}

								else if (!validateEmail(edEmail.getText()
										.toString().trim())) {
									edEmail.requestFocus();
									Toast.makeText(context,
											"Please enter valid Email address",
											Toast.LENGTH_SHORT).show();
								}

								else if (edPassword.getText().toString().trim()
										.length() <= 5) {

									edPassword.requestFocus();
									Toast.makeText(context,
											"Password must be 6 characters",
											Toast.LENGTH_SHORT).show();

								} else if (edPassword.getText().toString()
										.trim().length() > 33) {

									edUser.requestFocus();
									Toast.makeText(
											context,
											"Password must be less than 34 characters",
											Toast.LENGTH_SHORT).show();
								} else if (edRePassword.getText().toString()
										.trim().length() == 0) {
									edRePassword.requestFocus();
									Toast.makeText(context,
											"Kindly retype your password",
											Toast.LENGTH_SHORT).show();
								}

								else if (!edPassword
										.getText()
										.toString()
										.trim()
										.equals(edRePassword.getText()
												.toString().trim())) {

									edRePassword.requestFocus();
									Toast.makeText(context,
											"Password Mismatch",
											Toast.LENGTH_SHORT).show();

//								} else if (edAnswer.getText().toString().trim()
//										.length() == 0) {
//									edAnswer.requestFocus();
//									Toast.makeText(context,
//											"Please enter secret answer",
//											Toast.LENGTH_SHORT).show();
								} else if (edPhone.getText().toString()
										.length() < 10) {
									edPhone.requestFocus();
									Toast.makeText(
											context,
											"Phone Number must be 10-15 Digits",
											Toast.LENGTH_SHORT).show();

								} else if (edPhone.getText().toString()
										.length() > 15) {
									edPhone.requestFocus();
									Toast.makeText(
											context,
											"Phone Number must be 10-15 Digits",
											Toast.LENGTH_SHORT).show();

								}

							}
						} else {
							calldisp.showAlert("", "Enter The Mandatory Fields");
						}

					} else {
						Toast.makeText(context, "Please check settings",
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});

//		Button cancel = (Button) callControls.findViewById(R.id.btnBack);

//		cancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (edUser.getText().toString().trim().length() > 0
//						&& edPassword.getText().toString().trim().length() > 0
//						&& edRePassword.getText().toString().trim().length() > 0
//						&& edEmail.getText().toString().trim().length() > 0
//						&& etsecAnswer.getText().toString().trim().length() > 0) {
//					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//							context);
//					alertDialogBuilder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.info));
//					alertDialogBuilder
//							.setMessage(
//									SingleInstance.mainContext.getResources().getString(R.string.you_may_loss_the_data_are_you_sure_want_to_go_back))
//							.setCancelable(false)
//							.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.go_back),
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog, int id) {
//
//											dialogx.cancel();
//										}
//									})
//							.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no),
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog, int id) {
//
//										}
//									});
//					AlertDialog alertDialog = alertDialogBuilder.create();
//					alertDialog.show();
//				} else {
//					dialogx.cancel();
//				}
//
//			}
//		});

		dialogx.setContentView(callControls);
		dialogx.show();

	}

	@SuppressWarnings("deprecation")
	private void Showconfirmation(String title, String Alert) {
		Log.d("lg", "..... came to show alert");
		try {
			AlertDialog confirmation = new AlertDialog.Builder(this).create();
			confirmation.setTitle(title);
			confirmation.setMessage(Alert);
			confirmation.setCancelable(true);
			confirmation.setButton(SingleInstance.mainContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (dialogx != null)
						dialogx.dismiss();
					dialogx = null;
					dialog.dismiss();
				}
			});

			confirmation.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void showAlert1(String message) {

		Log.d("call", "callDialScreen");
		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
		alertCall.setMessage(message).setCancelable(false)
				.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		alertCall.show();
	}

	private boolean isAlphaNumeric(String x) {
		String test = x.substring(0, 1);

		if (isNumeric(test)) {
			return false;

		}

		if (x.matches("[a-zA-z0-9]*")) {
			System.out.println("alphanumeric");
			Log.d("AN", "alphanume");
			return true;
		} else {
			System.out.println("Non alpha numeric");
			Log.d("AN", "Non alphanume");
			return false;
		}

	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private boolean validateEmail(String id) {
        return id.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && id.length() > 0;
	}

	public void notifyRegistrationResponse(final Object obj) {
		service_handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				calldisp.cancelDialog();
				if (obj instanceof WebServiceBean) {
					WebServiceBean server_result = (WebServiceBean) obj;
					String text = server_result.getText();
					String res = server_result.getResult();
					if (text != null && res != null) {
						if (text.startsWith("User ID already")) {

							showAlert1(text);
						} else if (text.startsWith("Server Internal")) {
							showAlert1(text);
						} else {

							if (res.equals("1")) {
								Showconfirmation("Success", text);
							} else {
								showAlert1(text);
							}
						}
					}

					text = null;
					res = null;
					server_result = null;
				} else if (obj instanceof String)
					showAlert1((String) obj);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK))
			calldisp.showKeyDownAlert(context);

		return super.onKeyDown(keyCode, event);
	}

	public void notifyLoginResponse(String string) {
		// TODO Auto-generated method stub

	}

}
