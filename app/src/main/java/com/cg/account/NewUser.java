package com.cg.account;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.main.EndorseFragment;
import com.main.LoginPageFragment;
import com.main.Registration;
import com.util.SingleInstance;

import org.lib.model.WebServiceBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewUser extends Activity {
    private Context context;
    private EditText ed_firstname=null;
    private EditText ed_lastname=null;
    private TextView ed_dob=null;
    private EditText ed_securityno=null;
    private EditText ed_hno=null;
    private EditText ed_zipcode=null;
    private EditText ed_email=null;
    private Button done=null;
    private SharedPreferences preferences;
    private CallDispatcher calldisp;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    final Calendar myCalendar = Calendar.getInstance();
     private AutoCompleteTextView state;
    ArrayList<String> states=new ArrayList<String>();
    ArrayAdapter<String> stateAdapter;

    protected void onCreate(Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.newuser);
        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        calldisp = new CallDispatcher(context);

        done = (Button) findViewById(R.id.donebtn);
        Button back = (Button) findViewById(R.id.cancel);
        ed_firstname = (EditText)findViewById(R.id.ed_firstname);
        ed_lastname = (EditText)findViewById(R.id.ed_lastname);
        ed_dob = (TextView)findViewById(R.id.ed_dob);
        ed_securityno = (EditText)findViewById(R.id.ed_securityno);
        ed_hno = (EditText)findViewById(R.id.ed_hno);
        ed_zipcode = (EditText)findViewById(R.id.ed_zipcode);
        ed_email = (EditText)findViewById(R.id.ed_email);
//        spin_state = (Spinner) findViewById(R.id.spin_state);

        final String role = getIntent().getStringExtra("role");
        setDone();

        final TextView firstname = (TextView) findViewById(R.id.firstname);

        states= DBAccess.getdbHeler().getStateDetails();
        state = (AutoCompleteTextView) findViewById(R.id.state);
        stateAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, states);
        state.setAdapter(stateAdapter);
        state.setThreshold(1);

        ed_firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    firstname.setVisibility(View.VISIBLE);
                } else {
                    firstname.setVisibility(View.GONE);
                }
                setDone();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final TextView lastname = (TextView) findViewById(R.id.lastname);

        ed_lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    lastname.setVisibility(View.VISIBLE);
                } else {
                    lastname.setVisibility(View.GONE);
                }
                setDone();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final TextView dob = (TextView) findViewById(R.id.dob);

        ed_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(NewUser.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Log.i("sss","====================>date"+i+i1+i2);
                        myCalendar.set(Calendar.YEAR, i);
                        myCalendar.set(Calendar.MONTH, i1);
                        myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                        dob.setVisibility(View.VISIBLE);
                        updateLabel();
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                Date min = new Date(2013-1916, 0, 11);
                dialog.getDatePicker().setMaxDate(min.getTime());
//                dialog.getDatePicker().setMinDate(min.getTime());
                dialog.show();
                setDone();
            }
        });

        final TextView sec_no = (TextView) findViewById(R.id.sec_no);

        ed_securityno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    sec_no.setVisibility(View.VISIBLE);
                } else {
                    sec_no.setVisibility(View.GONE);
                }
                setDone();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                TextView er_securityno = (TextView) findViewById(R.id.er_securityno);
                View line_securityno = (View) findViewById(R.id.line_securityno);
                if (ed_securityno.getText().toString().length() < 4) {
                    er_securityno.setVisibility(View.VISIBLE);
                    line_securityno.setBackgroundColor(getResources().getColor(R.color.yellow));
                }else{
                    er_securityno.setVisibility(View.GONE);
                    line_securityno.setBackgroundColor(getResources().getColor(R.color.grey3));
                }
            }
        });

        final TextView tr_pgm = (TextView) findViewById(R.id.tr_pgm);

        state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    tr_pgm.setVisibility(View.VISIBLE);
                } else {
                    tr_pgm.setVisibility(View.GONE);
                }
                setDone();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final TextView h_no = (TextView) findViewById(R.id.h_no);

        ed_hno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    h_no.setVisibility(View.VISIBLE);
                } else {
                    h_no.setVisibility(View.GONE);
                }
                setDone();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final TextView zipcode = (TextView) findViewById(R.id.zipcode);

        ed_zipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    zipcode.setVisibility(View.VISIBLE);
                } else {
                    zipcode.setVisibility(View.GONE);
                }
                setDone();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final TextView email = (TextView) findViewById(R.id.email);
        email.setVisibility(View.GONE);
        ed_email.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    if ((ed_firstname.getText().toString().length() > 0) && (ed_lastname.getText().toString().length() > 0) &&
                            (ed_dob.getText().toString().length() > 0) && (ed_securityno.getText().toString().length() > 0) &&
                            (state.getText().toString().length() > 0) &&
                            (ed_hno.getText().toString().length() > 0) && (ed_zipcode.getText().toString().length() > 0))
                    {

                        String[] param=new String[8];
                        param[0]=role;
                        param[1]=ed_firstname.getText().toString();
                        param[2]=ed_lastname.getText().toString();
                        param[3]=ed_dob.getText().toString();
                        param[4]=ed_securityno.getText().toString();
                        param[5]=state.getText().toString().trim();
                        param[6]=ed_hno.getText().toString();
                        param[7]=ed_zipcode.getText().toString();
                        if (!WebServiceReferences.running) {
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
                        }
                        WebServiceReferences.webServiceClient.NewVerification(param,context);
                        showDialog();

                    }else {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.error_dialogue);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                        dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
                        ImageView error_img = (ImageView) dialog.findViewById(R.id.error_img);
                        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                        tv_title.setText("SORRY!");
                        TextView tv_firstLine = (TextView) dialog.findViewById(R.id.tv_firstLine);
                        TextView tv_secondLine = (TextView) dialog.findViewById(R.id.tv_secondLine);
                        TextView tv_note = (TextView) dialog.findViewById(R.id.tv_note);
                        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                        error_img.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                        tv_firstLine.setVisibility(View.VISIBLE);
                        tv_firstLine.setText("Unable to verify your \nprofessional credentials");
                        tv_secondLine.setVisibility(View.GONE);
                        tv_note.setVisibility(View.VISIBLE);
                        tv_note.setText("Please contact the American Medical Association\nto establish your credentials");
                        btn_ok.setText("OK");
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        dialog.show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void setDone(){
        if ((ed_firstname.getText().toString().length() > 0) && (ed_lastname.getText().toString().length() > 0) &&
                (ed_dob.getText().toString().length() > 0) && (ed_securityno.getText().toString().length() > 0) &&
                (state.getText().toString().length() > 0) &&
                (ed_hno.getText().toString().length() > 0) && (ed_zipcode.getText().toString().length() > 0)){
//            done.setEnabled(true);
            done.setBackgroundColor(getResources().getColor(R.color.blue2));
        }else{
//            done.setEnabled(false);
            done.setBackgroundColor(getResources().getColor(R.color.black3));
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ed_dob.setText(sdf.format(myCalendar.getTime()));
    }

    public void notifyWebserviceResponse(Object obj)
    {
        cancelDialog();
        if (obj instanceof String[]) {
            String[] temp = (String[]) obj;
            if (temp[0].startsWith("verification")) {
                Intent intent = new Intent(NewUser.this, Registration.class);
                intent.putExtra("fname", ed_firstname.getText().toString());
                intent.putExtra("lname", ed_lastname.getText().toString());
                intent.putExtra("dob", ed_dob.getText().toString());
                intent.putExtra("ssn", ed_securityno.getText().toString());
                intent.putExtra("houseno", ed_hno.getText().toString());
                intent.putExtra("zipcode", ed_zipcode.getText().toString());
                startActivity(intent);
                finish();
            } else {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.error_dialogue);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
                ImageView error_img = (ImageView) dialog.findViewById(R.id.error_img);
                TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                tv_title.setText("SORRY!");
                TextView tv_firstLine = (TextView) dialog.findViewById(R.id.tv_firstLine);
                TextView tv_secondLine = (TextView) dialog.findViewById(R.id.tv_secondLine);
                TextView tv_note = (TextView) dialog.findViewById(R.id.tv_note);
                Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                error_img.setVisibility(View.VISIBLE);
                tv_title.setVisibility(View.VISIBLE);
                tv_firstLine.setVisibility(View.VISIBLE);
                tv_firstLine.setText("Unable to verify your details\nwith the American Medical\nAssociation (AMA)");
                tv_secondLine.setVisibility(View.GONE);
                tv_note.setVisibility(View.VISIBLE);
                tv_note.setText("Please Contact AMA to\nresolve this problem");
                btn_ok.setText("CONTACT AMA");
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }


    }
    private void showToast(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                 Toast.makeText(context, message, Toast.LENGTH_LONG).show();
             }
        });
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
