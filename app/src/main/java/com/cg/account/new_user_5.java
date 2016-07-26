package com.cg.account;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;


public class new_user_5 extends Activity{

    private Context context;
    EditText et_invite;
    TextView tv_invite;
    boolean callWebservice = true;
    private CallDispatcher calldisp;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    boolean isOne = true;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_user_5);
        context = this;
        et_invite = (EditText) findViewById(R.id.et_invite);
        tv_invite = (TextView) findViewById(R.id.tv_invite);
        WebServiceReferences.contextTable.put("new_user_5", this);
        calldisp = (CallDispatcher) WebServiceReferences.callDispatch.get("calldispatch");
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        et_invite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    tv_invite.setVisibility(View.VISIBLE);
                } else {
                    tv_invite.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button savepswd = (Button) findViewById(R.id.savepswd);
        savepswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_invite.getText().toString().length()>0){
                Intent intent = new Intent(new_user_5.this, NewUser.class);
                intent.putExtra("role", "Physician");
                startActivity(intent);
                    finish();
                }else
                    Toast.makeText(getApplicationContext(), "Please enter invite code",
                            Toast.LENGTH_SHORT).show();
//                    final Dialog dialog1 = new Dialog(context);
//                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog1.setContentView(R.layout.error_dialogue);
//                dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                dialog1.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
//                    ImageView error_img = (ImageView) dialog1.findViewById(R.id.error_img);
//                    TextView tv_title = (TextView) dialog1.findViewById(R.id.tv_title);
//                    TextView tv_firstLine = (TextView) dialog1.findViewById(R.id.tv_firstLine);
//                    TextView tv_secondLine = (TextView) dialog1.findViewById(R.id.tv_secondLine);
//                    TextView tv_note = (TextView) dialog1.findViewById(R.id.tv_note);
//                    Button btn_ok = (Button) dialog1.findViewById(R.id.btn_ok);
//                    error_img.setVisibility(View.VISIBLE);
//                    tv_title.setVisibility(View.VISIBLE);
//                    tv_title.setText("INVALID INVITE CODE!");
//                    tv_firstLine.setVisibility(View.VISIBLE);
//                    tv_firstLine.setText("Please try again");
//                    tv_secondLine.setVisibility(View.GONE);
//                    tv_note.setVisibility(View.VISIBLE);
//                    tv_note.setText("You have 2 more attempts");
//                    btn_ok.setText("OK");
//                    btn_ok.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            final Dialog dialog2 = new Dialog(context);
//                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog2.setContentView(R.layout.error_dialogue);
//                            dialog2.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                            dialog2.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
//                            ImageView error_img1 = (ImageView) dialog2.findViewById(R.id.error_img);
//                            TextView tv_title1 = (TextView) dialog2.findViewById(R.id.tv_title);
//                            TextView tv_firstLine1 = (TextView) dialog2.findViewById(R.id.tv_firstLine);
//                            TextView tv_secondLine1 = (TextView) dialog2.findViewById(R.id.tv_secondLine);
//                            TextView tv_note1 = (TextView) dialog2.findViewById(R.id.tv_note);
//                            Button btn_ok1 = (Button) dialog2.findViewById(R.id.btn_ok);
//                            error_img1.setVisibility(View.VISIBLE);
//                            tv_title1.setVisibility(View.VISIBLE);
//                            tv_title1.setText("ERROR!");
//                            tv_firstLine1.setVisibility(View.VISIBLE);
//                            tv_firstLine1.setText("Maximum number of attempts\nreached for this Invite Code");
//                            tv_secondLine1.setVisibility(View.GONE);
//                            tv_note1.setVisibility(View.VISIBLE);
//                            tv_note1.setText("Please obtain a new valid Invite Code\nfrom a Registered SnazMed Physician");
//                            btn_ok1.setText("OK");
//                            btn_ok1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    final Dialog dialog3 = new Dialog(context);
//                                    dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                    dialog3.setContentView(R.layout.error_dialogue);
//                                    dialog3.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                                    dialog3.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
//                                    ImageView error_img1 = (ImageView) dialog3.findViewById(R.id.error_img);
//                                    TextView tv_title1 = (TextView) dialog3.findViewById(R.id.tv_title);
//                                    TextView tv_firstLine1 = (TextView) dialog3.findViewById(R.id.tv_firstLine);
//                                    TextView tv_secondLine1 = (TextView) dialog3.findViewById(R.id.tv_secondLine);
//                                    TextView tv_note1 = (TextView) dialog3.findViewById(R.id.tv_note);
//                                    Button btn_ok1 = (Button) dialog3.findViewById(R.id.btn_ok);
//                                    error_img1.setVisibility(View.VISIBLE);
//                                    tv_title1.setVisibility(View.VISIBLE);
//                                    tv_title1.setText("ERROR!");
//                                    tv_firstLine1.setVisibility(View.VISIBLE);
//                                    tv_firstLine1.setText("This Invite Code is\nalready in use");
//                                    tv_secondLine1.setVisibility(View.GONE);
//                                    tv_note1.setVisibility(View.VISIBLE);
//                                    tv_note1.setText("Please obtain a new valid Invite Code\nfrom a Registered SnazMed Physician");
//                                    btn_ok1.setText("OK");
//                                    btn_ok1.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            final Dialog dialog4 = new Dialog(context);
//                                            dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                            dialog4.setContentView(R.layout.error_dialogue);
//                                            dialog4.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                                            dialog4.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
//                                            ImageView error_img = (ImageView) dialog4.findViewById(R.id.error_img);
//                                            TextView tv_title = (TextView) dialog4.findViewById(R.id.tv_title);
//                                            TextView tv_firstLine = (TextView) dialog4.findViewById(R.id.tv_firstLine);
//                                            TextView tv_secondLine = (TextView) dialog4.findViewById(R.id.tv_secondLine);
//                                            TextView tv_note = (TextView) dialog4.findViewById(R.id.tv_note);
//                                            Button btn_ok = (Button) dialog4.findViewById(R.id.btn_ok);
//                                            error_img.setVisibility(View.VISIBLE);
//                                            tv_title.setVisibility(View.VISIBLE);
//                                            tv_title.setText("ERROR!");
//                                            tv_firstLine.setVisibility(View.VISIBLE);
//                                            tv_firstLine.setText("This Invite Code has expired");
//                                            tv_secondLine.setVisibility(View.VISIBLE);
//                                            tv_secondLine.setText("Invite code are only valid\nfor 24 hours");
//                                            tv_secondLine.setTextColor(getResources().getColor(R.color.pink));
//                                            tv_secondLine.setTextSize(12);
//                                            tv_note.setVisibility(View.VISIBLE);
//                                            tv_note.setText("Please obtain a new valid Invite Code\nfrom a Registered Snazmed Physician");
//                                            btn_ok.setText("OK");
//                                            btn_ok.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    final Dialog dialog5 = new Dialog(context);
//                                                    dialog5.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                                    dialog5.setContentView(R.layout.error_dialogue);
//                                                    dialog5.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                                                    dialog5.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
//                                                    ImageView error_img = (ImageView) dialog5.findViewById(R.id.error_img);
//                                                    TextView tv_title = (TextView) dialog5.findViewById(R.id.tv_title);
//                                                    TextView tv_firstLine = (TextView) dialog5.findViewById(R.id.tv_firstLine);
//                                                    TextView tv_secondLine = (TextView) dialog5.findViewById(R.id.tv_secondLine);
//                                                    TextView tv_note = (TextView) dialog5.findViewById(R.id.tv_note);
//                                                    Button btn_ok = (Button) dialog5.findViewById(R.id.btn_ok);
//                                                    error_img.setVisibility(View.VISIBLE);
//                                                    tv_title.setVisibility(View.VISIBLE);
//                                                    tv_title.setText("SORRY!");
//                                                    tv_firstLine.setVisibility(View.VISIBLE);
//                                                    tv_firstLine.setText("Unable to verify your details");
//                                                    tv_secondLine.setVisibility(View.GONE);
////                                                    tv_secondLine.setText("Invite code are only valid\nfor 24 hours");
////                                                    tv_secondLine.setTextColor(getResources().getColor(R.color.pink));
////                                                    tv_secondLine.setTextSize(14);
//                                                    tv_note.setVisibility(View.VISIBLE);
//                                                    tv_note.setText("Please proceed \nwith Document Verification\nto verify your credentials.");
//                                                    btn_ok.setText("DOCUMENT VERIFICATION");
//                                                    btn_ok.setOnClickListener(new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View view) {
//                                                            Intent intent = new Intent(new_user_5.this, NewUserVerification.class);
//                                                            startActivity(intent);
//                                                            dialog5.dismiss();
//                                                            finish();
//                                                        }
//                                                    });
//                                                    dialog5.show();
//                                                    dialog4.dismiss();
//                                                }
//                                            });
//                                            dialog4.show();
//                                            dialog3.dismiss();
//                                        }
//                                    });
//                                    dialog3.show();
//                                    dialog2.dismiss();
//                                }
//                            });
//                            dialog2.show();
//                            dialog1.dismiss();
//                        }
//                    });
//                    dialog1.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        try {
            if (WebServiceReferences.contextTable.containsKey("new_user_5")) {
                WebServiceReferences.contextTable.remove("new_user_5");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        super.onDestroy();

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
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
