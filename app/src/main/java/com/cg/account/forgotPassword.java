package com.cg.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.main.AppMainActivity;
import com.util.SingleInstance;

import java.util.Arrays;

/**
 * @author Narayanan
 */

public class forgotPassword extends Activity {

	private boolean isActivityPause = false;
	private Context context = null;
	private CallDispatcher calldisp;
	private TextView sendrequest;
	private EditText ans1;
	private EditText ans2;
	private EditText ans3;
	private SharedPreferences preferences;
	private Handler handler = new Handler();
	private ProgressDialog progressDialog = null;
    private int count = 3;
	TextView question1,question2,question3;
	String ques1,ques2,ques3;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forgetpassword);
		calldisp = (CallDispatcher) WebServiceReferences.callDispatch.get("calldispatch");
		initUI();
		context = this;
		WebServiceReferences.contextTable.put("ForgotPassword", this);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		question1=(TextView)findViewById(R.id.question1);
		question2=(TextView)findViewById(R.id.question2);
		question3=(TextView)findViewById(R.id.question3);
		String[] questions = Arrays.copyOf(SingleInstance.mainContext.questions, SingleInstance.mainContext.questions.length);
		question1.setText(questions[0]);
		question2.setText(questions[1]);
		question3.setText(questions[2]);
			Button back = (Button) findViewById(R.id.cancel);
			back.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					finish();
				}
			});

			sendrequest.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if (ans1.getText().toString().trim().length() > 0
								&& ans2.getText().toString().trim().length() > 0
								&& ans3.getText().toString().trim().length() > 0) {
							if (!WebServiceReferences.running) {
								try {
									String url = preferences.getString("url", null);
									String port = preferences.getString("port",
											null);
									if (url != null && port != null)
										calldisp.startWebService(url, port);
									else
										calldisp.startWebService(getResources()
														.getString(R.string.service_url),
												"80");
									url = null;
									port = null;
								} catch (Exception e) {
									Log.d("RRRR", "ERROR FORGOT PASSWORD -> " + e.toString());
								}
							}
							String params[] = {question1.getText().toString().trim(),ans1.getText().toString().trim(),question2.getText().toString().trim(), ans2.getText().toString().trim(), question3.getText().toString().trim(),ans3.getText().toString().trim()};
							WebServiceReferences.webServiceClient.forgetPasswordSnazMed(params, context);
							showDialog();
						} else {
							showAlert("Please fill all the fields properly");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});


	}

	private void initUI() {
		// TODO Auto-generated method stub
		try {
			ans1 = (EditText) findViewById(R.id.ans1);
			ans2 = (EditText) findViewById(R.id.ans2);
            ans3 = (EditText) findViewById(R.id.ans3);
			sendrequest = (TextView) findViewById(R.id.sendrequest);

		} catch (Exception e) {

		}
	}

	public void getResponseFromServer(final Object obj) {
		// TODO Auto-generated method stub
		cancelDialog();
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (obj instanceof String) {
					String result = (String) obj;
					Log.i("AAAA", "FORGOT PASSWORD ____" + result);
					if (result.equals("success")) {
						Intent intentfp = new Intent(context, ResetPassword.class);
						startActivity(intentfp);
						finish();
					} else {
						if (--count > 0) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.error_dialogue);
                            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                            dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
							ImageView error_img = (ImageView) dialog.findViewById(R.id.error_img);
							TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
							TextView tv_firstLine = (TextView) dialog.findViewById(R.id.tv_firstLine);
							TextView tv_secondLine = (TextView) dialog.findViewById(R.id.tv_secondLine);
							TextView tv_note = (TextView) dialog.findViewById(R.id.tv_note);
							Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
							error_img.setVisibility(View.VISIBLE);
							tv_title.setVisibility(View.VISIBLE);
							tv_title.setText("INCORRECT ANSWER(S)");
							tv_firstLine.setVisibility(View.VISIBLE);
							tv_firstLine.setText("Please try again");
							tv_secondLine.setVisibility(View.GONE);
							tv_note.setVisibility(View.VISIBLE);
							tv_note.setText("you have " + count + " more attempts");
							btn_ok.setText("OK");
							btn_ok.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									ans1.setText("");
									ans2.setText("");
									ans3.setText("");
									dialog.dismiss();
								}
							});
							dialog.show();
						} else {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.error_dialogue);
                            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                            dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
							ImageView error_img = (ImageView) dialog.findViewById(R.id.error_img);
							TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
							TextView tv_firstLine = (TextView) dialog.findViewById(R.id.tv_firstLine);
							TextView tv_secondLine = (TextView) dialog.findViewById(R.id.tv_secondLine);
							TextView tv_note = (TextView) dialog.findViewById(R.id.tv_note);
							Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
							error_img.setVisibility(View.VISIBLE);
							tv_title.setVisibility(View.VISIBLE);
							tv_title.setText("INCORRECT ANSWER(S)");
							tv_firstLine.setVisibility(View.VISIBLE);
							tv_firstLine.setText("Please press the button below\nfor further verification");
							tv_secondLine.setVisibility(View.GONE);
							tv_note.setVisibility(View.GONE);
							btn_ok.setText("CONTINUE");
							btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
							dialog.show();
						}
					}
				}
			}
		});

	}

	protected void showAlert(String msg) {
		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
		alertCall.setMessage(msg).setTitle("Forget Password")
				.setCancelable(false)
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

	@Override
	protected void onDestroy() {
		try {
			if (WebServiceReferences.contextTable.containsKey("ForgotPassword")) {
				WebServiceReferences.contextTable.remove("ForgotPassword");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();

	}

	@Override
	protected void onResume() {
		try {
            super.onResume();
            AppMainActivity.inActivity = this;
            ans1.setText("");
            ans2.setText("");
            ans3.setText("");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected void onPause() {
		isActivityPause = true;
		super.onPause();

	}
	private void showDialog() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					progressDialog = new ProgressDialog(context);
					if (progressDialog != null) {
						progressDialog.setCancelable(false);
						progressDialog.setMessage("Progress ...");
						progressDialog
								.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.setProgress(0);
						progressDialog.setMax(100);
						progressDialog.show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}
	public void cancelDialog() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				Log.i("register", "--progress bar end-----");
				progressDialog.dismiss();
				progressDialog = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
