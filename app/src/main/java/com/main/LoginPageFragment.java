package com.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.account.NewUserRole;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.process.BGProcessor;
import com.thread.CommunicationBean;
import com.thread.SipCommunicator;
import com.util.SingleInstance;

import org.core.CommunicationEngine;
import org.core.EnumSignallingType;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;
import org.lib.model.SiginBean;
import org.lib.model.WebServiceBean;
import org.lib.webservice.WSRunner;
import org.lib.xml.XmlParser;
import org.net.AndroidInsecureKeepAliveHttpsTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginPageFragment extends Fragment {

    private static CallDispatcher calldisp;

    private SharedPreferences preferences;

    private Handler handler = new Handler();
    private AppMainActivity appMainActivity = null;

    // For Login

    private TextView title = null; // this Textview used fragment title change

    private EditText edPswd;
    RelativeLayout relativelayout;

    private EditText edUserName;

    private Typeface tf_regular = null;

    private Typeface tf_bold = null;


    private Button btnLogin = null;

    private static LoginPageFragment loginPageFragment;

    private static Context mainContext;

    private ProgressDialog progress = null;

    private boolean stop = false;
    private boolean isLogin = false;

    private View view = null;

    private boolean isBackGroundLogin = false;

//    private ImageView email_img, password_img;

    private TextView email_error, password_error;

    private String storedUsername = null;

    private String storedPassword = null;
    Button newuser;

    public static LoginPageFragment newInstance(Context context) {
        try {
            if (loginPageFragment == null) {
                mainContext = context;
                loginPageFragment = new LoginPageFragment();
                calldisp = CallDispatcher.getCallDispatcher(mainContext);
            }
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);

        }
        return loginPageFragment;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SingleInstance.printLog("LOGIN", "Login Fragment OnCreateView", "INFO",
                null);
//		getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            tf_regular = Typeface.createFromAsset(mainContext.getAssets(),
                    getResources().getString(R.string.fontfamily));
            tf_bold = Typeface.createFromAsset(mainContext.getAssets(),
                    getResources().getString(R.string.fontfamilybold));

            final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

			Button select = (Button) getActivity().findViewById(R.id.btn_brg);
			select.setVisibility(View.GONE);

            Button imVw = (Button) getActivity().findViewById(R.id.im_view);
            imVw.setVisibility(View.GONE);

            Button setting = (Button) getActivity().findViewById(
                    R.id.btn_settings);
            setting.setVisibility(View.GONE);

            Button plusBtn = (Button) getActivity()
                    .findViewById(R.id.add_group);
            plusBtn.setVisibility(View.GONE);

            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            view = inflater.inflate(R.layout.loginscreen, null);

//            email_img = (ImageView) view.findViewById(R.id.email_img);
//            password_img = (ImageView) view.findViewById(R.id.password_img);
            email_error = (TextView) view.findViewById(R.id.email_error);
            password_error = (TextView) view.findViewById(R.id.password_error);
            btnLogin = (Button) view.findViewById(R.id.btnLogin);

            Button selectall = (Button) getActivity()
                    .findViewById(R.id.btn_brg);
            selectall.setVisibility(View.GONE);

            preferences = PreferenceManager
                    .getDefaultSharedPreferences(getActivity()
                            .getApplicationContext());
            title = (TextView) getActivity().findViewById(
                    R.id.activity_main_content_title);
            title.setText(SingleInstance.mainContext.getResources().getString(R.string.login));
            RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
            mainHeader.setVisibility(View.GONE);
            LinearLayout contact_layout=(LinearLayout)getActivity().findViewById(R.id.contact_layout);
            contact_layout.setVisibility(View.GONE);
//			LinearLayout footer_layout =(LinearLayout)getActivity().findViewById(R.id.footer_layout);
//			footer_layout.setVisibility(View.GONE);

            title.setVisibility(View.VISIBLE);

            if (WebServiceReferences.callDispatch.containsKey("calldisp"))
                calldisp = (CallDispatcher) WebServiceReferences.callDispatch
                        .get("calldisp");
            else
                calldisp = new CallDispatcher(getActivity());

            CallDispatcher.isConnected = calldisp.isOnLine(getActivity());

            /**
             * This Method is Used to Generate Login Screen.It is also used to
             * validate the InputFields.It also support for Login Process. It
             * also used to save the userName and Password as per User Setings.
             *
             * @return LoginScreen
             */

            final String username = preferences.getString("uname", null);
            String password = preferences.getString("pword", null);
            Log.i("lpass", "pass1 : " + password);
            boolean rememberpassword = preferences
                    .getBoolean("remember", false);
            boolean autologinstate = preferences.getBoolean("autologin", false);
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
            int questionsCount=0;
            questionsCount=DBAccess.getdbHeler().countEntryDetails("SELECT  * FROM securityquestions");
            if(questionsCount!=15)
                WebServiceReferences.webServiceClient.getSecurityQuestions(SingleInstance.mainContext,
                        "",SingleInstance.mainContext.seccreatedDate);
            else {
                ArrayList<String> ids=DBAccess.getdbHeler().getSecurityQuestionsIds();
                String allIds = TextUtils.join(",", ids);
                Log.i("AAAA","LOGIN "+allIds);
                String[] id=allIds.split(",");
                if(id.length!=15)
                WebServiceReferences.webServiceClient.getSecurityQuestions(SingleInstance.mainContext,
                        allIds,SingleInstance.mainContext.seccreatedDate);
            }


            int stateId=0;
            stateId =DBAccess.getdbHeler().countEntryDetails("SELECT  * FROM statedetails");
            if(stateId<=0) {
                WebServiceReferences.webServiceClient.GetStates("", SingleInstance.mainContext);
                WebServiceReferences.webServiceClient.GetMedicalSocieties("", SingleInstance.mainContext);
                WebServiceReferences.webServiceClient.GetHospitalDetails("", SingleInstance.mainContext);
                WebServiceReferences.webServiceClient.GetSpecialities("", SingleInstance.mainContext);
                WebServiceReferences.webServiceClient.GetMedicalSchools("", SingleInstance.mainContext);
//                WebServiceReferences.webServiceClient.GetCities("", SingleInstance.mainContext);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String parse="";

                    String url=SingleInstance.mainContext.getResources().getString(R.string.service_url);

                    String loginIP = url.substring(url.indexOf("://") + 3);

                    loginIP = loginIP.substring(0, loginIP.indexOf(":"));

                    loginIP = loginIP.trim();

                    String urlPort = url.substring(url.indexOf("://") + 3);

                    urlPort = urlPort.substring(urlPort.indexOf(":") + 1);

                    urlPort = urlPort.substring(0, urlPort.indexOf("/"));


                    String server_ip = loginIP;
                    int connect_ort = Integer.parseInt(urlPort);
                    String namespace = "http://ltws.com/";
                    String wsdl_link = url.trim()+"?wsdl";
                    String quotes = "\"";


                    try {
                        Log.i("webservice","Getcities method called");
//                        Log.i("webservice","wsrunner obj-->"+wsRunner);
                        Log.i("webservice","wsrunner mWsdl_link-->"+wsdl_link);
                        parse= wsdl_link.substring(wsdl_link.indexOf("://") + 3);

                        parse = parse.substring(parse.indexOf(":") + 1);

                        parse = parse.substring(parse.indexOf("/"),
                                parse.indexOf("?"));

                    AndroidInsecureKeepAliveHttpsTransportSE androidHttpTransport = new AndroidInsecureKeepAliveHttpsTransportSE(
                            server_ip, connect_ort, parse, 1*60*30000);

                    SoapObject mRequest = new SoapObject(namespace, "GetCities");
                    String date="";
                    HashMap<String,String> propert_map = new HashMap<String,String>();
                    propert_map.put("date", date);

                    if (propert_map != null) {
                        for (Map.Entry<String, String> set : propert_map.entrySet()) {
                            mRequest.addProperty(set.getKey().trim(), set
                                    .getValue().trim());
                        }
//                        HashMap<String, String> map = propert_map;
//                        map = null;
                    }

                    Log.d("webservice", "My Server Requset  :" + mRequest);

                    SoapSerializationEnvelope mEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                    mEnvelope.setOutputSoapObject(mRequest);

                    androidHttpTransport.call(
                            quotes + namespace + "GetCities"
                                    + quotes, mEnvelope);
                    SoapPrimitive mSp = (SoapPrimitive) mEnvelope.getResponse();
                        XmlParser mParser = new XmlParser();
                        boolean mChk = false;
                        mChk = mParser.getResult(mSp.toString());
                        Log.d("AAAA", "loginpage Server response size   ---->" );
                        if(mChk){
                            ArrayList<String>cityList = mParser.parsecities(mSp.toString());
                            Log.d("AAAA", "loginpage Server response size---->" + cityList.size());
                            SingleInstance.mainContext.notifycityWebServiceResponse(cityList);
                        }

                    Log.d("webservice1", "loginpage Server response---->" + mSp.toString());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }).start();

            TextView forgetid = (TextView) view.findViewById(R.id.btnforgotuserid);
            forgetid.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    showToast("Please contact support desk www.snazmed.com");

//							Intent i = new Intent(getActivity()
//									.getApplicationContext(), GetEndorsement.class);
//							getActivity().startActivity(i);


                }
            });
            edUserName = (EditText) view.findViewById(R.id.etLoginuser);
            if (edUserName.getText().toString().length() > 0 && validateLogin()) {

            }
            newuser = (Button) view.findViewById(R.id.btnnewuser);
            newuser.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity()
                            .getApplicationContext(), NewUserRole.class);
                    getActivity().startActivity(intent);
                }
            });
            edPswd = (EditText) view.findViewById(R.id.etloginpassword);
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
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    // this.start = start;
                    // this.before = before;
                    // edPswd.setText("");
//                    email_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.input_mail));
//                    password_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.password));
                    email_error.setVisibility(View.GONE);
                    password_error.setVisibility(View.GONE);
					if (edUserName.getText().toString().length() == 0) {
						edPswd.setText("");
					}

                    if (edUserName.getText().toString().length() > 0
                            && validateLogin()) {
//						if (!validateEmail(edUserName.getText().toString().trim())) {
//							edUserName.requestFocus();
//							showAlert1("Enter valid Email address");
//						}
//						edUserName.setFilters(getpasswordFilter());
                    }
                    BtnEnabled();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // if (start != 0 && before != 0)
                    // edPswd.setText("");

                }
            });


            if (rememberpassword || autologinstate) {
                if (password != null) {
                    edPswd.setText(password.trim());
                }

                if (username != null) {
                    edUserName.setText(username.trim());
                }

            }else{
                clearTextFeilds();
            }
            edPswd = (EditText) view.findViewById(R.id.etloginpassword);
//			edPswd.setFilters(getpasswordFilter());
//			relativelayout = (RelativeLayout) view.findViewById(R.id.rl_loginview_container);
//
//
//
//			relativelayout.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					InputMethodManager im=(InputMethodManager) mainContext.getSystemService(mainContext.INPUT_METHOD_SERVICE);
//					im.hideSoftInputFromWindow(relativelayout.getWindowToken(),0);
//
//				}
//			});

//			edPswd = (EditText) view.findViewById(R.id.etloginpassword);
//			edPswd.setFilters(getpasswordFilter());
            edPswd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    email_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.input_mail));
//                    password_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.password));
                    email_error.setVisibility(View.GONE);
                    password_error.setVisibility(View.GONE);
                    BtnEnabled();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
//
            edPswd.setOnEditorActionListener(new OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId,
                                              KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                            || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        Log.d("Test", "Event Done Pressed");
                        isLogin = true;
                        if (edUserName.getText().toString().trim().length() == 0
                                && edPswd.getText().toString().trim().length() == 0) {
                            showAlert1("Enter User Id and Password");
                        } else {
                            doSignin();
                        }
                    }
                    return false;
                }
            });
            btnLogin.setTypeface (tf_regular);
            Log.i("lpass", "pass : " + edPswd.getText().toString());
            Log.i("lpass", "pass : " + edPswd.getText().toString());

            btnLogin.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {

                        InputMethodManager im=(InputMethodManager) mainContext.getSystemService(mainContext.INPUT_METHOD_SERVICE);
                        im.hideSoftInputFromWindow(btnLogin.getWindowToken(),0);
                        stop = true;
                        isLogin = true;
                        if (edUserName.getText().toString().trim().length() == 0
                                && edPswd.getText().toString().trim().length() == 0) {
                            showAlert1("Enter User Id and Password");
                        } else if (edUserName.getText().toString().trim()
                                .length() != 0
                                && edPswd.getText().toString().trim().length() == 0) {
                            showAlert1("Enter Password");

                        } else if (!validateEmail(edUserName.getText().toString().trim())) {
                            edUserName.requestFocus();
                            showAlert1("Enter valid Email address");
//                            email_img.setImageDrawable(null);
//                            email_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.input_mail_yellow));
                            email_error.setVisibility(View.VISIBLE);
                            password_error.setVisibility(View.GONE);
						}else {
							doSignin();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						SingleInstance.mainContext.hideKeyboard();
					}
				}
			});
			Log.i("login123", "SingleInstance.manualLogout outside class :" + SingleInstance.manualLogout);
			if (autologinstate && !SingleInstance.manualLogout) {
				Log.i("login123","SingleInstance.manualLogout :"+SingleInstance.manualLogout);
				if (edUserName.getText().toString().trim().length() > 0
						&& edPswd.getText().toString().trim().length() > 0) {
					login(username, password);
				}
			}


//			Button register = (Button) view.findViewById(R.id.btnRegister);
//			register.setTypeface (tf_regular);
//
//			register.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					SingleInstance.printLog("LOGIN", "-----------"
//							+ CallDispatcher.isConnected, "INFO", null);
//
//					if (CallDispatcher.isConnected) {
//						Intent intent = new Intent(getActivity()
//								.getApplicationContext(), Registration.class);
//						getActivity().startActivity(intent);
//					} else {
//						showAlert1("Check Internet Connection Unable to Connect Server");
//					}
//				}
//			});
            appMainActivity = (AppMainActivity) SingleInstance.contextTable
                    .get("MAIN");
            TextView forgetpassword = (TextView) view
                    .findViewById(R.id.btnforgotpswd);
            forgetpassword.setTypeface (tf_regular);
            forgetpassword.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(edUserName.getText().toString().trim().length()>0) {
                        CallDispatcher.LoginUser = edUserName.getText().toString().trim();
                        if (CallDispatcher.isConnected) {
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
                            WebServiceReferences.webServiceClient.GetMySecretQuestion(CallDispatcher.LoginUser, SingleInstance.mainContext);
                            appMainActivity.showDialog();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Please check your internet connection",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),
                                "Please enter the user id to proceed",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

//			TextView changeDateFormat = (TextView) view
//					.findViewById(R.id.btnDateFormat);
//			changeDateFormat.setTypeface (tf_regular);
//			changeDateFormat.setOnClickListener(new OnClickListener() {
//				private String buffKey = "";
//
//				@Override
//				public void onClick(View v) {
//
//					// TODO Auto-generated method stub
//					String date = preferences.getString("dateformate",
//							"MM/dd/yyyy");
//					final String[] date_items = { "MM/dd/yyyy", "dd-MM-yyyy" };
//					int pos = 0;
//					if (date.equals("dd-MM-yyyy"))
//						pos = 1;
//
//					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//							getActivity());
//					alertDialogBuilder.setTitle("Select Date");
//					alertDialogBuilder.setSingleChoiceItems(date_items, pos,
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//									Editor editor = preferences.edit();
//									editor.putString("dateformate",
//											date_items[which]);
//									editor.commit();
//									//CallDispatcher.dateFormat = date_items[which];
//									buffKey=date_items[which];
//									//dialog.cancel();
//								}
//							});
//					alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog,int id) {
//							// if this button is clicked,
//							// set date format
//							CallDispatcher.dateFormat=buffKey;
//							dialog.cancel();
//						}
//					  });
//					alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog,int id) {
//							// if this button is clicked, just close
//							// the dialog box and do nothing
//							dialog.cancel();
//						}
//					});
//
//					// create alert dialog
//					AlertDialog alertDialog = alertDialogBuilder.create();
//
//					// show it
//					alertDialog.show();
//				}
//			});
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
            e.printStackTrace();
        }
        return view;

    }

    private void doSignin() {

        try {
            Log.i("login123","inside dosignin");
            String urlx = preferences.getString("url", null);
            String portx = preferences.getString("port", null);
            Editor editor = preferences.edit();
            editor.putBoolean("isfirstlaunch", false);
            editor.commit();

            if (urlx != null && portx != null) {

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        try {

                            String myPublicIp = CallDispatcher
                                    .getPublicipaddress();

                            SingleInstance.printLog("LOGIN", "print public ip "
                                    + myPublicIp, "INFO", null);

                            String username = edUserName.getText().toString()
                                    .trim().toLowerCase();
                            String password = edPswd.getText().toString()
                                    .trim();

                            login(username, password);

                        } catch (Exception e) {
                            SingleInstance.printLog(null, e.getMessage(), null,
                                    e);
                        }

                    }
                });

            } else {
                Toast.makeText(getActivity(), "Please check Settings",
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
        }

    }

    public void backgroundLogin() {
        try {
            if (!SingleInstance.manualLogout && !isBackGroundLogin) {
                if (preferences == null) {
                    preferences = PreferenceManager
                            .getDefaultSharedPreferences(mainContext);
                }
                String username = edUserName.getText().toString().trim();
                String password = edPswd.getText().toString().trim();
                Log.i("login123", "username : " + username + "password : "
                        + password);
                if(!isLogin) {
                    if (username.length() > 0 && password.length() > 0) {
                        login(username, password);

                    }
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void silentLogin(String username, String password) {
        try {
            if (username != null && password != null) {
                storedUsername = username;
                storedPassword = password;
                if (storedUsername.length() > 0 && storedPassword.length() > 0) {
                    login(storedUsername, storedPassword);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {

        try {
            super.onResume();
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
        }
    }

	@Override
	public void onDestroy() {
		try {
			super.onDestroy();
            final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            super.onSaveInstanceState(outState);
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
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
                                '6', '7', '8', '9', '_' };
                        // , '@', '.', '_', '#', '$',
                        // '%', '*', '-', '+', '(', ')', '!', '\'', ':',
                        // ';', '/', '?', ',', '~', '`', '|', '\\', '^',
                        // '{', '}', '[', ']', '=', '.', };

                        for (int index = start; index < end; index++) {
                            if (!new String(acceptedChars).contains(String
                                    .valueOf(source.charAt(index)))) {
                                isLogin = false;
                                int edUserLength = edUserName.getText()
                                        .length();

                                if (edUserLength != 0) {
                                    Toast.makeText(mainContext,
                                            "Special Characters Not Allowed",
                                            Toast.LENGTH_SHORT).show();
                                }
                                if (edUserLength == 0 && (!isLogin)) {
                                    Toast.makeText(
                                            mainContext,
                                            "User ID Should be alphanumeric character",
                                            Toast.LENGTH_SHORT).show();
                                }

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

    public void login(String username, String password)

    {
        try {
            isLogin=true;
            // if (calldisp.mainbuddylist != null) {
            // calldisp.mainbuddylist.clear();
            // }
            Log.i("logservice", "inside login()");
            if (preferences == null) {
                preferences = PreferenceManager
                        .getDefaultSharedPreferences(mainContext);
            }
            boolean isCorrectSettings = checkSettings();
            if (validateLogin()) {
                if (isCorrectSettings) {
                    Log.i("login123","inside login if(validatelogin)");
                    showprogress();
                    if (!WebServiceReferences.running) {
                        String url = preferences.getString("url", null);
                        String port = preferences.getString("port", null);
                        if (url != null && port != null)
                            calldisp.startWebService(url, port);
                        else
                            calldisp.startWebService(
                                    getResources().getString(
                                            R.string.service_url), "80");
                        url = null;
                        port = null;
                    }
                    signIn(username, password);
                    if (!stop) {
                        // cancelDialog();
                    }
                    AppReference.isFormloaded = false;
                }

                else if (!isCorrectSettings) {
                    cancelDialognew(1);
                    Toast.makeText(getActivity(), "Please Check Settings",
                            Toast.LENGTH_LONG).show();
                } else {
                    cancelDialognew(2);

                }
            }
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
        }

    }

    @SuppressWarnings("finally")
    public boolean checkSettings() {
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
            SingleInstance.printLog(null, e.getMessage(), null, e);

        } finally {
            url = null;
            loginIP = null;
            urlPort = null;
            return returnValue;
        }

    }

    /**
     * This method is used to show the Registration Screen. The Registration
     * screen is used to create an account. This method uses Server for
     * Registration and also contains InBuild Validation of Fields.
     */

    public void showAlert1(String message) {

        try {
            AlertDialog.Builder alertCall = new AlertDialog.Builder(mainContext);
            alertCall
                    .setMessage(message)
                    .setCancelable(false)
                    .setNegativeButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    try {

                                    } catch (Exception e) {
                                        if (AppReference.isWriteInFile)
                                            AppReference.logger.error(
                                                    e.getMessage(), e);
                                        else
                                            e.printStackTrace();
                                    }
                                }
                            });
            alertCall.show();
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
        }
    }
    public void showAlert(String message) {

        try {
            AlertDialog.Builder alertCall = new AlertDialog.Builder(mainContext);
            alertCall
                    .setMessage(message)
                    .setCancelable(false)
                    .setNegativeButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    try {
                                        appMainActivity.logout(true);
                                    } catch (Exception e) {
                                        if (AppReference.isWriteInFile)
                                            AppReference.logger.error(
                                                    e.getMessage(), e);
                                        else
                                            e.printStackTrace();
                                    }
                                }
                            });
            alertCall.show();
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
        }
    }

    public void notifyLoginResponse(final Object obj) {
        try {

            handler.post(new Runnable() {

                @SuppressWarnings("rawtypes")
                @Override
                public void run() {
                    try {

                        // TODO Auto-generated method stub
                        //cancelDialog();
                        SingleInstance.printLog("LOGIN",
                                "Login Response Received", "INFO", null);
                        AppMainActivity.isLogin=true;
                        Log.d("ABCD","login True  ");

                        if (obj instanceof ArrayList || obj instanceof BuddyInformationBean) {

                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    Log.i("Test","inside sipRegister000000000000");
                                    // TODO Auto-generated method stub
								CommunicationBean cb = new CommunicationBean();
                                    String user=CallDispatcher.LoginUser.replace("@","_");
                                    Log.i("AAAA", "SIP CALL REGISTRATION " + user);
								cb.setUser_name(user);
								cb.setPassword(CallDispatcher.Password);
								cb.setRealm(AppMainActivity.relam);
                                Log.d("droid123", "LPF SIP relam ==> " + AppMainActivity.relam);
								cb.setRegistrar(AppMainActivity.registrar);
                                Log.d("droid123", "LPF SIP registrar ==> " + AppMainActivity.registrar);
								cb.setsipProxy("sip:"+AppMainActivity.proxy+";transport=tcp;hide");
                                Log.d("droid123", "LPF SIP proxy ==> " + AppMainActivity.proxy);
								cb.setOperationType(SipCommunicator.sip_operation_types.REGISTER_ACCOUNT);

								Log.d("droid123","=====>values"+cb.getUsername()+cb.getPassword()+cb.getRealm()+cb.getRegistrar()+cb.getSipProxy());

								AppReference.sipQueue.addMsg(cb);

//								CommunicationBean cbean = new CommunicationBean();
//								cbean.setAcc_id(AppReference.acc_id);
//								cbean.setOperationType(SipCommunicator.sip_operation_types.REREGISTER);
//								AppReference.sipQueue.addMsg(cbean);
//
//								CommunicationBean cb = new CommunicationBean();
//								cb.setUser_name("praveenkumar.cognitive_yahoo.com.ccglobal");
//								cb.setPassword("0be7ab982b624c50acd83ac8bd590dac");
//								cb.setRealm("10.100.140.70");
//								cb.setRegistrar("74.201.153.235");
//								cb.setsipProxy("sip:74.201.153.235;transport=tcp;hide");
//								cb.setOperationType(sip_operation_types.REGISTER_ACCOUNT);
//
//								Log.d("droid123","=====>values"+cb.getUsername()+cb.getPassword()+cb.getRealm()+cb.getRegistrar()+cb.getSipProxy());
//
//								AppReference.sipQueue.addMsg(cb);


//								AppMainActivity appMain = new AppMainActivity();


                                }
                            });
                        }else
                        {
                            Log.i("Test","inside else partsipRegister@@@@@@@@@@@@");

                        }





                        if (obj instanceof ArrayList) {

//                            email_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.input_mail));
//                            password_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.password));
                            email_error.setVisibility(View.GONE);
                            password_error.setVisibility(View.GONE);
                            if (!isBackGroundLogin) {
                                CallDispatcher.LoginUser = edUserName.getText()
                                        .toString().toLowerCase().trim();
                                CallDispatcher.Password = edPswd.getText()
                                        .toString().trim();
                            } else {
                                CallDispatcher.LoginUser = storedUsername;
                                CallDispatcher.Password = storedPassword;
                            }
                            ContactsFragment.getBuddyList().clear();
                            ArrayList list = (ArrayList) obj;

                            SingleInstance.printLog("LOGIN", "List Size :"
                                    + list.size(), "INFO", null);
                            ContactsFragment.buddyRequestCount.clear();
                            for (int i = 0; i < list.size(); i++) {
                                Object obj = list.get(i);
                                if (obj instanceof BuddyInformationBean) {
                                    BuddyInformationBean bib = (BuddyInformationBean) obj;
                                    // if
                                    // (bib.getStatus().equalsIgnoreCase("new"))
                                    // {
                                    // WebServiceReferences.reqbuddyList.put(
                                    // bib.getName(), bib);
                                    // } else if (bib.getStatus()
                                    // .equalsIgnoreCase("pending")) {
                                    // calldisp.showPendingBuddies(bib);
                                    // WebServiceReferences.buddyList.put(
                                    // bib.getName(), bib);
                                    // } else {
                                    // Log.d("NAME", "name" + bib.getName());
                                    // WebServiceReferences.buddyList.put(
                                    // bib.getName(), bib);
                                    // }
                                    if (!bib.getName().equalsIgnoreCase(
                                            CallDispatcher.LoginUser)) {
                                        bib.setMessageCount(DBAccess
                                                .getdbHeler()
                                                .getUnreadMsgCountById(
                                                        bib.getName(),
                                                        CallDispatcher.LoginUser));
                                        GroupBean gBean = DBAccess
                                                .getdbHeler()
                                                .getAllIndividualChatByBuddyName(
                                                        CallDispatcher.LoginUser,
                                                        bib.getName());
                                        if (gBean.getLastMsg() != null
                                                && !gBean.getLastMsg()
                                                .equalsIgnoreCase(
                                                        "null")) {
                                            bib.setLastMessage(gBean
                                                    .getLastMsg());
                                        }
                                        if (bib.getStatus().equalsIgnoreCase(
                                                "new")) {
                                            showToast(bib.getName()
                                                    + " sent request to you");
                                        }
                                        ContactsFragment.getBuddyList()
                                                .add(bib);


                                    } else {
                                        BGProcessor.ownerList.add(bib);
                                    }
                                }
                            }
                            SingleInstance.mainContext.notifyUI();
                            AppMainActivity main = (AppMainActivity) SingleInstance.contextTable
                                    .get("MAIN");
                            main.loginResponse(obj);

                            if (preferences != null) {
                                Editor editor = preferences.edit();
                                if (!isBackGroundLogin) {
                                    editor.putString("uname", edUserName
                                            .getText().toString().trim());
                                    editor.putString("pword", edPswd.getText()
                                            .toString().trim());
                                } else {
                                    if (storedUsername != null
                                            && storedPassword != null) {
                                        editor.putString("uname", edUserName
                                                .getText().toString().trim());
                                        editor.putString("pword", edPswd
                                                .getText().toString().trim());
                                    }
                                }
                                editor.commit();

                            }

                            AppReference.sipnotifier = new SipNotificationListener();

                            loadExchanges();
//							cancelDialognew(3);
                            // exchangesFragment.setLaunch(true);
                            // fragmentTransaction.replace(
                            // R.id.activity_main_content_fragment,
                            // exchangesFragment);

                            // fragmentTransaction.replace(
                            // R.id.activity_main_content_fragment,
                            // contactsFragment);
                            // fragmentTransaction.commit();
                            // fragmentManager.beginTransaction()
                            // .replace(R.id.activity_main_content_fragment,
                            // contactsFragment).commitAllowingStateLoss();
                            AppMainActivity.isPinEnable = true;
                            for (BuddyInformationBean bibBean : BGProcessor.ownerList) {
                                if (bibBean.getName().equalsIgnoreCase(
                                        CallDispatcher.LoginUser)) {
                                    SingleInstance.mainContext
                                            .resetMenuList(bibBean);

                                    ContactsFragment contactsFragment = ContactsFragment
                                            .getInstance(mainContext);
                                    DashBoardFragment dashBoardFragment = DashBoardFragment.newInstance(mainContext);
                                    FragmentManager fragmentManager = SingleInstance.mainContext
                                            .getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager
                                            .beginTransaction();
                                    fragmentManager
                                            .beginTransaction()
                                            .replace(
                                                    R.id.activity_main_content_fragment,
                                                    contactsFragment)
                                            .commitAllowingStateLoss();
                                    fragmentManager
                                            .beginTransaction()
                                            .replace(
                                                    R.id.activity_main_content_fragment,
                                                    dashBoardFragment)
                                            .commitAllowingStateLoss();

                                    break;
                                }
                            }
//							final TextView tv_namestatus = (TextView) SingleInstance.mainContext
//									.findViewById(R.id.menu_label);
                            // final ImageView profileImage = (ImageView)
                            // getActivity()
                            // .findViewById(R.id.userimageview);

                            CallDispatcher.myStatus = "1";
                            if (CallDispatcher.LoginUser != null) {
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
                                                .get("MAIN");
//										tv_namestatus.setText(CallDispatcher.LoginUser
//												+ "\n"
//												+ SingleInstance.mainContext
//														.loadCurrentStatus());
                                    }
                                });

                            }

                        } else if (obj instanceof WebServiceBean) {
                            if (AppMainActivity.commEngine != null)
                                AppMainActivity.commEngine.stop();
                            // SingleInstance.mainContext
                            // .ShowError(((WebServiceBean) obj).getText());
                            showAlert(((WebServiceBean) obj).getText());
//                            email_img.setImageDrawable(null);
//                            email_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.input_mail_yellow));
//                            password_img.setImageDrawable(null);
//                            password_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.password_yellow));
                            email_error.setVisibility(View.VISIBLE);
                            password_error.setVisibility(View.VISIBLE);


                            // String response = ((WebServiceBean)
                            // obj).getText();
                            // if(response.equalsIgnoreCase("Network unreachable")){
                            // showAlert1("Check Internet Connection Unable to Connect Server");
                            // }
                            Log.d("Test", "LoginpageFragment 877"
                                    + ((WebServiceBean) obj).getText());
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String message = "Please enter correct username,password";
                                    if (obj instanceof String)
                                        message = (String) obj;
                                    Log.d("Test", "LoginpageFragment885"
                                            + message);
                                    String response = message;
                                    showAlert1(message);

//                                    email_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.input_mail_yellow));
//                                    password_img.setImageDrawable(mainContext.getResources().getDrawable(R.drawable.password_yellow));
                                    email_error.setVisibility(View.VISIBLE);
                                    password_error.setVisibility(View.VISIBLE);
//									if (response
//											.equalsIgnoreCase("Network unreachable")) {
//										Log.d("Test", "LoginpageFragment 956"
//												+ ((WebServiceBean) obj).getText());
//										showAlert1("Check Internet Connection Unable to Connect Server");
//									}

                                    // Toast.makeText(mainContext, message,
                                    // Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        cancelDialognew(3);

                    } catch (Exception e) {
                        cancelDialognew(4);

                        SingleInstance.printLog(null, e.getMessage(), null, e);
                    }
                }
            });
        } catch (Exception e) {
            cancelDialognew(5);

            SingleInstance.printLog(null, e.getMessage(), null, e);
            e.printStackTrace();
        } finally {
            //cancelDialog();

        }

    }

    private void showToast(String msg) {
        Toast.makeText(mainContext, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * To sign in the buddy. At the time of sign in initialize the
     * CommunicationEngine instance.
     *
     * @param user
     *            - user id of the Buddy
     * @param password
     *            - password of the Buddy
     */
    @SuppressWarnings("deprecation")
    public void signIn(final String user, final String password) {
        try {
            SingleInstance.printLog("LOGIN", "Befor call signin", "INFO", null);

            if (WebServiceReferences.running) {
                String text1 = user;
                String text2 = password;

                AppMainActivity.commEngine = new CommunicationEngine(
                        EnumSignallingType.PROPRIETARY);

                AppMainActivity.commEngine.setLocalInetaddress(CallDispatcher
                        .getLocalipaddsress());
                AppMainActivity.commEngine.setPublicInetaddress(CallDispatcher
                        .getPublicipaddress());

                PackageManager pm = mainContext.getPackageManager();
                boolean hasFrontCamera = pm
                        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
                SingleInstance.printLog("CAMERA", "has front camera "
                        + hasFrontCamera, "INFO", null);

                if (Integer.parseInt(Build.VERSION.SDK) > 8) {
                    WebServiceReferences.CAMERA_ID = 1;
                    if (!hasFrontCamera) {
                        WebServiceReferences.CAMERA_ID = 0;
                    }

                } else {

                    WebServiceReferences.CAMERA_ID = 2;

                }
                AppMainActivity.commEngine.setVideoParameters(
                        WebServiceReferences.VIDEO_WIDTH,
                        WebServiceReferences.VIDEO_HEIGHT,
                        WebServiceReferences.CAMERA_ID);
                AppMainActivity.commEngine.start(3, 3000);
                AppMainActivity.commEngine.setCallSessionListener(calldisp,
                        calldisp);
                SiginBean siginBean = new SiginBean();
                siginBean.setName(text1);

                siginBean.setPassword(text2);
                siginBean.setMac(CallDispatcher.getDeviceId());
                siginBean.setLocalladdress(CallDispatcher.getLocalipaddsress());

                if (CallDispatcher.getPublicipaddress() == null) {
                    siginBean.setExternalipaddress(CallDispatcher
                            .getLocalipaddsress());
                } else {
                    siginBean.setExternalipaddress(CallDispatcher
                            .getPublicipaddress());
                }

                siginBean
                        .setSignalingPort(Integer
                                .toString(AppMainActivity.commEngine
                                        .getSignalingPort()));
                siginBean.setPstatus("1");
                calldisp.getMobileDetails();
                siginBean.setDtype(calldisp.returnDetails[0]);
                siginBean.setDos(calldisp.returnDetails[2]);

                siginBean.setLatitude(Double.toString(CallDispatcher.latitude));

                siginBean.setLongitude(Double
                        .toString(CallDispatcher.longitude));

                siginBean.setAver(mainContext.getResources().getString(
                        R.string.app_version));
                siginBean.setReleaseVersion(mainContext
                        .getString(R.string.app_date));
                siginBean.setDtype("Android");
                siginBean.setDeviceType("ANDROID");
                // if (calldisp.mainbuddylist != null)
                // calldisp.mainbuddylist.clear();

                siginBean.setCallBack(loginPageFragment);
                WebServiceReferences.webServiceClient
                        .UsersignIn(siginBean);

            } else {
                SharedPreferences preferences = PreferenceManager
                        .getDefaultSharedPreferences(mainContext);
                String ipaddress = preferences.getString("ipaddress", null);
                String port = preferences.getString("port", null);
                String namespace = preferences.getString("namespace", null);
                if (ipaddress != null && port != null && namespace != null) {
                } else {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(mainContext,
                                    "Please check settings", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
        } finally {

        }
    }

    private void showprogress() {

        try {
            Log.i("login123", "inside showProgressDialog");
            if (SingleInstance.mainContext.progress == null
                    && SingleInstance.mainContext.formprogress == null) {
                progress = new ProgressDialog(SingleInstance.mainContext);
                progress.setCancelable(false);
                progress.setMessage("Login in Progress ...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setProgress(0);
                progress.setMax(100);
                progress.show();
            }
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
        }

    }

    public void cancelDialognew(int count) {
        try {
            Log.i("Test", "inside cancelDialog"+count);
            if (progress != null && progress.isShowing()) {
                Log.i("login123", "inside cancelDialog if condition");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
            e.printStackTrace();
        }

    }

    private void loadExchanges() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                DBAccess.getdbHeler().deleteOldGroups();
                WebServiceReferences.webServiceClient.GetFileDetails(CallDispatcher.LoginUser, ""
                        , SingleInstance.mainContext);
                WebServiceReferences.webServiceClient.GetAllProfile(
                        CallDispatcher.LoginUser, "", mainContext);
                WebServiceReferences.webServiceClient.getGroupAndMembers(
                        CallDispatcher.LoginUser, "", SingleInstance.mainContext);
                WebServiceReferences.webServiceClient.getRoundingGroupAndMembers(
                        CallDispatcher.LoginUser, "", SingleInstance.mainContext);
                WebServiceReferences.webServiceClient.GetMypin(CallDispatcher.LoginUser);

            }
        });
    }

    public void setSilentLogin(boolean login) {
        isBackGroundLogin = login;
    }

    public boolean getSilentLogin() {
        return this.isBackGroundLogin;
    }

    private boolean validateLogin() {
        if (edPswd.getText().toString().trim().length() > 0) {
            if ((edUserName.getText().toString().length() == 0)) {
                // showAlert1("Please enter User ID before entering password");
                showToast("Please enter User ID before entering password");
                return false;
            }
        }

        if (edUserName.getText().toString().trim().length() == 0) {
            showToast("User ID should be alphanumeric character");
            return false;
        }

        return true;
    }

    public void clearTextFeilds() {
        boolean rememberpassword = preferences.getBoolean("remember", false);
        boolean autologinstate = preferences.getBoolean("autologin", false);
        Log.i("login098", "remeber : " + rememberpassword + " autologin : "
                + autologinstate);
        if (!rememberpassword || !autologinstate) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Log.i("login098", "inside handler");
//                    edUserName.setText("");
                    edPswd.setText("");
                }
            });

        }
    }
    public boolean validateEmail(String id) {
        if (id.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && id.length() > 0) {
            return true;
        } else {
            return false;
        }
    }
    private void BtnEnabled()
    {
        if(edUserName.getText().toString().length() > 0 && edPswd.getText().toString().length() > 0){
            btnLogin.setEnabled(true);
            btnLogin.setBackgroundColor(mainContext.getResources().getColor(R.color.blue2));
        }else{
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
        }
    }
}
