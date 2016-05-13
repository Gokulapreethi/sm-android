package com.cg.account;

import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class getnewpassword extends Activity{
	private boolean isActivityPause = false;

	private Context context;

	private ProgressDialog progdialog;

	private TextView edsecqsn;

	private EditText edsecans;

	private EditText user;

	private EditText mailid;

	private EditText edpassword1;

	private EditText edpassword2;

	private TextView chngepswd;

	private Button cancelpswd;

	private String strTitle = "";

	private String strMessage = "";

	private CallDispatcher callDisp = null;

	String secretqustion;

	private Button btn_notification = null;
	private Button btn_secretquestion;

	private Handler handler = new Handler();

	private ProgressDialog pDialog = null;

	private boolean isShown = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.getnewpasswordscreen);

		initui();

		context = this;
		if (callDisp == null) {
			callDisp = CallDispatcher.getCallDispatcher(context);
		}
		WebServiceReferences.contextTable.put("getnewpassword", this);

		final String username = getIntent().getStringExtra("userid");
		Log.d("lg", ">>>>>>>>>>>>>" + username);
		final String emailid_val = getIntent().getStringExtra("email");
		final String response = getIntent().getStringExtra("question");
		Log.d("lg", ">>>>>>>>>>>>>" + emailid_val);

		// user.setText(username);
		// user.setFocusable(false);
		// user.setEnabled(false);
		// mailid.setText(emailid_val);
		// mailid.setFocusable(false);
		// mailid.setEnabled(false);

		cancelpswd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});
		// if (WebServiceReferences.contextTable.containsKey("forgotPassword"))
		// {
		// forgotPassword forgotPass = (forgotPassword)
		// WebServiceReferences.contextTable
		// .get("forgotPassword");
		// secretqustion=forgotPass.getSecretQues();
		// }
		edsecqsn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// secretqustion = getIntent().getStringExtra("question");
				Log.d("Test", "Inside SecrtQues Onclick!!!!!!!!"
						+ secretqustion);

				String[] parm = { user.getText().toString(),
						mailid.getText().toString() };
				showProgress();
				WebServiceReferences.webServiceClient.forgetPassword(parm);

			}
		});
		btn_secretquestion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] parm = { user.getText().toString(),
						mailid.getText().toString() };
				showProgress();
				WebServiceReferences.webServiceClient.forgetPassword(parm);

			}

		});

		edsecans.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence character, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				String characterString = edsecqsn.getText().toString().trim();
				if (characterString.trim().length() == 0
						|| characterString
								.equalsIgnoreCase("get secret question")) {
					if (!isShown) {
						showAlert1("Select secret question before entering the secret answer");
						isShown = true;
						edsecans.getText().clear();
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		chngepswd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stubu
				String user1 = user.getText().toString();
				String emailid = mailid.getText().toString();
				String secretqsn = edsecqsn.getText().toString();
				String answar = edsecans.getText().toString();
				String password1 = edpassword1.getText().toString();
				String password2 = edpassword2.getText().toString();

				if (user.getText().toString().trim().length() >= 4
						&& user.getText().toString().trim().length() < 34
						&& isAlphaNumeric(user.getText().toString().trim())
						&& validateEmail(mailid.getText().toString().trim())
						&& edpassword1.getText().toString().length() >= 6
						&& edpassword2.getText().toString().length() >= 6
						&& answar.length() != 0
						&& !edsecqsn.getText().toString()
								.equalsIgnoreCase("Get Your question")) {

					if (!WebServiceReferences.running) {
						callDisp.startWebService(
								getResources().getString(R.string.service_url),
								"80");
					}

				} else {

					if (user.getText().toString().trim().length() <= 4
							|| user.getText().toString().trim().length() > 33
							|| !isAlphaNumeric(user.getText().toString().trim())) {
						// showErrorMark();

						showErrorMark1();

					}
					if (!validateEmail(mailid.getText().toString().trim())) {

						showErrorMark2();

					}
					if (edsecqsn.getText().toString()
							.equalsIgnoreCase("Get Your question")) {
						showAlert("Select Secret Question before entering the secret answer");
					}

				}
				edpassword1.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				// edPassword.setSingleLine(true);

				edpassword1.addTextChangedListener(new TextWatcher() {
					public void afterTextChanged(Editable s) {
					}

					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						if (edpassword1.getText().toString().trim().length() > 0) {
							edpassword2.setEnabled(true);
						}
						if (edpassword1.getText().toString().length() > 32) {
							Toast.makeText(context,
									"Your Password maximum Limit reached",
									Toast.LENGTH_SHORT).show();
						}
						if (edpassword1.getText().toString().length() != edpassword2
								.getText().toString().length()) {
							edpassword2.setText("");
						}

					}
				});

				// edRePassword.sethi
				edpassword2.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				edpassword2.addTextChangedListener(new TextWatcher() {
					public void afterTextChanged(Editable s) {
					}

					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					public void onTextChanged(CharSequence s, int start,
							int before, int count) {

						if (edpassword2.getText().toString().length() > 32) {
							Toast.makeText(context,
									"Your Password maximum Limit reached",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

				if (user.getText().toString().trim().length() >= 4
						&& user.getText().toString().trim().length() < 34
						&& isAlphaNumeric(user.getText().toString().trim())
						&& validateEmail(mailid.getText().toString().trim())
						&& answar.length() != 0 && password1.length() >= 6
						&& password2.length() >= 6
						&& password1.equals(password2)) {
					// resetErrorMark();
					if (WebServiceReferences.running) {
						Log.d("message", "webservice is running");
						progdialog = ProgressDialog.show(context, "Progress ",
								"Please wait", true);
						progdialog
								.setOnDismissListener(new OnDismissListener() {

									@Override
									public void onDismiss(DialogInterface dialog) {
										// TODO Auto-generated method stub
										if (progdialog != null) {
											progdialog.dismiss();
										}
									}
								});

						progdialog.setCancelable(true);
						String[] parm = new String[4];
						parm[0] = user1;
						parm[1] = secretqsn;
						parm[2] = answar;
						parm[3] = password1;
						WebServiceReferences.webServiceClient
								.SecretAnswer(parm);

					}

				} else if (user.getText().toString().trim().length() < 4) {
					user.requestFocus();
					user.setText("");
					showAlert1("Enter User Id");

				} else if (user.getText().toString().trim().length() > 33) {
					user.requestFocus();
					showAlert1("Username must be less than 34 characters");

				} else if (mailid.getText().toString().trim().length() == 0) {
					user.requestFocus();
					showAlert1("Enter Email Address");

				} else if (!isAlphaNumeric(user.getText().toString().trim())) {
					showAlert1("Enter Alpha Numeric characters.The first letter must be Alpha value");
				} else if (!validateEmail(mailid.getText().toString().trim())) {

					mailid.requestFocus();
					showAlert1("Enter valid Email address");
				} else if ((password1.length() != 0) && password1.length() < 6) {
					showAlert1("Password should be Minimum 6 Characters");

				} else if (edsecqsn.getText().toString().trim().length() == 0) {
					showAlert1("Select Secret Question");

				} else if (answar.length() == 0) {
					showAlert1("Secret answer field should be filled");
				} else if (password1.length() == 0) {
					showAlert1("Enter New Password");
				} else if (password2.length() == 0 && password1.length() != 0) {
					showAlert1("Enter Confirm Password");
				} else if (!password1.equals(password2)) {
					showAlert1("Password must be same");
				}

			}
		});

		edpassword2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					if (edpassword1.getText().toString().length() > 0) {
						edpassword2.setEnabled(true);
					} else {
						edpassword2.setEnabled(false);
					}
				}
				return false;
			}
		});
		// if (WebServiceReferences.contextTable.containsKey("forgotPassword"))
		// {
		// forgotPassword fp = (forgotPassword)
		// WebServiceReferences.contextTable
		// .get("forgotPassword");
		// fp.finish();
		// }
	}

	private void initui() {
		user = (EditText) findViewById(R.id.eduser1);
		mailid = (EditText) findViewById(R.id.edemail1);
		edsecqsn = (TextView) findViewById(R.id.edsecqsn1);
		edsecans = (EditText) findViewById(R.id.edsecans1);
		edpassword1 = (EditText) findViewById(R.id.ednewpassword1);
		edpassword2 = (EditText) findViewById(R.id.edrepassword1);
		chngepswd = (TextView) findViewById(R.id.getpswd);
		cancelpswd = (Button) findViewById(R.id.cancelpswd);
		btn_secretquestion = (Button) findViewById(R.id.btn_secretquestion);
		edpassword1.setFilters(getpasswordFilter());
		edpassword2.setFilters(getpasswordFilter());

	}

	public void getResponseFromServer(final String response, final String result) {
		// TODO Auto-generated method stub
		progdialog.dismiss();
		if (result.trim().equals("0")) {

			if (!isActivityPause) {

				showAlertDialog("Forgot Password Error", response);

			} else {
				strTitle = "Forgot Password Error";
				strMessage = response;
				progdialog.dismiss();
			}
			// btnSendRequest.setEnabled(true);
		} else if (result.trim().equals("1")) {

			secretqustion = response;
		}

		else {
			showAlertDialog("check ur settings", response);
			progdialog.dismiss();
		}

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    public void getSecretQuestionResponse(final String strResponse,
			final String result) {

		progdialog.dismiss();

		if (result.trim().equals("0")) {

			if (!isActivityPause)
				showAlertDialog("Forgot Password  Error", strResponse);
			else {

			}
			// btnNewPasswordSubmit.setEnabled(true);
		} else if (result.trim().equals("1")) {

			if (!isActivityPause) {
				showAlert(strResponse);
				progdialog.dismiss();

			}

		}
	}

	private void showErrorMark1() {
		try {
			user.setTextColor(Color.BLACK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showErrorMark2() {
		try {
			mailid.setTextColor(Color.BLACK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void showAlertDialog(String title, String message) {
		// TODO Auto-generated method stub
		Log.d("call", "callDialScreen");
		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);

		alertCall.setTitle(title).setMessage(message).setCancelable(false)
				.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		alertCall.show();

	}

	protected void showAlert(String msg) {
		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
		alertCall.setMessage(msg).setCancelable(false)
				.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							// alertCall.
							dialog.dismiss();
							finish();

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		alertCall.show();

	}

	protected void showAlert1(String msg) {
		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
		alertCall.setMessage(msg).setCancelable(false)
				.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							// alertCall.
							isShown = false;
							dialog.dismiss();

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		alertCall.show();

	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str); // (converts the string to a double
										// variable)
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private boolean isAlphaNumeric(String x) {
		try {
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
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("getnewpassword");
		super.onDestroy();
	}

	private boolean validateEmail(String id) {
		if (id.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && id.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void notifyWebserviceResponse(Object object) {
		try {
			if (object instanceof WebServiceBean) {
				WebServiceBean wsBean = (WebServiceBean) object;
				if (wsBean.getResult().equals("1")) {
					edsecqsn.setText((String) wsBean.getText());
				} else if (wsBean.getResult().equals("0")) {
					showToast((String) wsBean.getText());
				}
			} else if (object instanceof String) {
				showToast((String) object);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cancelDialog();
		}
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

	private void showProgress() {
		try {
			pDialog = new ProgressDialog(context);
			if (pDialog != null) {
				pDialog.setCancelable(true);
				pDialog.setMessage("Progress ...");
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setProgress(0);
				pDialog.setMax(100);
				pDialog.show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public InputFilter[] getpasswordFilter() {
		InputFilter[] filters = null;
		try {
			filters = new InputFilter[1];
			filters[0] = new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					if (end > start) {

						char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
								'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
								'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
								'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
								'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
								'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
								'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5',
								'6', '7', '8', '9' };
						// , '@', '.', '_', '#', '$',
						// '%', '*', '-', '+', '(', ')', '!', '\'', ':',
						// ';', '/', '?', ',', '~', '`', '|', '\\', '^',
						// '{', '}', '[', ']', '=', '.', };

						for (int index = start; index < end; index++) {
							if (!new String(acceptedChars).contains(String
									.valueOf(source.charAt(index)))) {

								showToast(SingleInstance.mainContext.getResources().getString(R.string.special_characters_not_allowed));
								return "";
							}
						}
					}
					return null;
				}
			};

		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);

		}
		return filters;

	}

	private void cancelDialog() {
		try {
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
				pDialog = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
