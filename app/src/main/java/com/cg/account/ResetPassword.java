package com.cg.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.util.SingleInstance;

import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;


public class ResetPassword extends Activity {


    static Context context = null;
    EditText newPswrd = null;
    EditText repeatpassword = null;
    Button savepswd = null;

    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.resetpassword);
        context = this;
        WebServiceReferences.contextTable.put("ResetPassword", this);
        newPswrd = (EditText) findViewById(R.id.newPswrd);
        repeatpassword = (EditText) findViewById(R.id.repeatpassword);
        final LinearLayout title=(LinearLayout)findViewById(R.id.title);
        savepswd = (Button) findViewById(R.id.savepswd);
        setDone();

        final TextView tv_newPswd = (TextView) findViewById(R.id.tv_newPswd);

        newPswrd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    tv_newPswd.setVisibility(View.VISIBLE);
                } else {
                    tv_newPswd.setVisibility(View.GONE);
                }
                setDone();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final TextView cnfirmpswd = (TextView) findViewById(R.id.cnfirmpswd);

        repeatpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cnfirmpswd.setVisibility(View.VISIBLE);
                    title.setVisibility(View.INVISIBLE);
                } else {
                    cnfirmpswd.setVisibility(View.GONE);
                }
                setDone();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        savepswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (newPswrd.getText().toString().trim()
                            .equals(repeatpassword.getText().toString().trim())) {
                        String[] parm = {CallDispatcher.LoginUser,
                                newPswrd.getText().toString()};
                        WebServiceReferences.webServiceClient
                                .ResetPassword(parm, context);
                        showDialog();

                } else if (newPswrd.getText().toString().trim().length() == 0
                        && repeatpassword.getText().toString().trim().length() == 0) {
                    showToast(SingleInstance.mainContext.getResources().getString(R.string.enter_mandatory_fields));
                } else if (newPswrd.getText().toString().trim().length() == 0) {
                    newPswrd.setFocusable(true);
                    showToast(SingleInstance.mainContext.getResources().getString(R.string.enter_a_new_password));

                } else if (repeatpassword.getText().toString().trim().length() == 0) {
                    repeatpassword.setFocusable(true);
                    showToast(SingleInstance.mainContext.getResources().getString(R.string.retype_your_new_password));
                } else {
                    Toast.makeText(context, "Password Mismatch",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setDone(){
        if ((repeatpassword.getText().toString().length() > 0) && (newPswrd.getText().toString().length() > 0)){
            savepswd.setEnabled(true);
            savepswd.setBackgroundColor(getResources().getColor(R.color.blue2));
        }else{
            savepswd.setEnabled(false);
            savepswd.setBackgroundColor(getResources().getColor(R.color.black3));
        }
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }

    protected void onDestroy() {
        try {
            if (WebServiceReferences.contextTable.containsKey("ResetPassword")) {
                WebServiceReferences.contextTable.remove("ResetPassword");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    public void notifyWebServiceResponse(Servicebean servicebean) {
        cancelDialog();
        final WebServiceBean sbean = (WebServiceBean) servicebean.getObj();
        Log.d("message", "RESPONSE comes from dispatcher" + sbean.getText());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String Response = sbean.getText();
                try {
                    if (Response.contains("successfully")) {
                        showToast(Response);
                        finish();
                    } else {
                        showToast(Response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDialog() {
        handler.post(new Runnable() {

            @Override
            public void run() {
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
            e.printStackTrace();
        }

    }
}
