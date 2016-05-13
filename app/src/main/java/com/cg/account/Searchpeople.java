package com.cg.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.main.AppMainActivity;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.WebServiceBean;

import java.util.ArrayList;
import java.util.List;

public class Searchpeople extends Activity {

    EditText lname,fname,nickname;
    Button search,back,advsearch;
    Context context;
    Spinner usertype,state,city,speciality,hospital,medical,residency,fellow;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    private CallDispatcher callDisp = null;
    RadioGroup gender;
    RadioButton genderSelected;
    Boolean isClicked=true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.search_contact);
        context = this;
        AppMainActivity.searchContext = context;
        final TextView tv_nickname=(TextView)findViewById(R.id.tv_nickname);
        final TextView tv_fname=(TextView)findViewById(R.id.tv_fname);
        final TextView tv_lname=(TextView)findViewById(R.id.tv_lname);
        LinearLayout advance=(LinearLayout)findViewById(R.id.advance);
        final LinearLayout advance_lay=(LinearLayout)findViewById(R.id.advance_lay);
        final Button arrow=(Button)findViewById(R.id.arrow);
        lname = (EditText) findViewById(R.id.lName);
        fname = (EditText) findViewById(R.id.fName);
        nickname = (EditText) findViewById(R.id.nicName);
        usertype = (Spinner) findViewById(R.id.usertype);
        state = (Spinner) findViewById(R.id.state_tilte);
        city = (Spinner) findViewById(R.id.city_tilte);
        speciality = (Spinner) findViewById(R.id.spec_tilte);
        hospital = (Spinner) findViewById(R.id.hos_tilte);
        search = (Button) findViewById(R.id.search);
//        advsearch = (Button) findViewById(R.id.advsearch);
        back = (Button) findViewById(R.id.cancel);
        medical = (Spinner) findViewById(R.id.medical_scl);
        residency = (Spinner) findViewById(R.id.resi_pro);
        fellow = (Spinner) findViewById(R.id.fellow);
        gender = (RadioGroup) findViewById(R.id.gender);

        if (WebServiceReferences.callDispatch.containsKey("calldisp"))
            callDisp = (CallDispatcher) WebServiceReferences.callDispatch
                    .get("calldisp");
        else
            callDisp = new CallDispatcher(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClicked) {
                    isClicked=false;
                    advance_lay.setVisibility(View.VISIBLE);
                    arrow.setBackgroundResource(R.drawable.up_arrow);
                }else {
                    isClicked=true;
                    advance_lay.setVisibility(View.GONE);
                    arrow.setBackgroundResource(R.drawable.input_arrow);
                }
            }
        });
        final RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
        final RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        radioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioMale.isChecked()) {
                    radioMale.setTextColor(Color.parseColor("#458EDB"));
                    radioFemale.setTextColor(Color.parseColor("#ffffff"));
                }


            }
        });
        radioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioFemale.setTextColor(Color.parseColor("#458EDB"));
                radioMale.setTextColor(Color.parseColor("#ffffff"));

            }
        });
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    tv_nickname.setVisibility(View.VISIBLE);
                } else {
                    tv_nickname.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    tv_fname.setVisibility(View.VISIBLE);
                } else {
                    tv_fname.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        lname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    tv_lname.setVisibility(View.VISIBLE);
                } else {
                    tv_lname.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        List<String> list = new ArrayList<String>();
        list.add("User Type");
        list.add("Physician");
        list.add("Student.");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_lay,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        usertype.setAdapter(dataAdapter);
        usertype.setSelection(0);

        list = new ArrayList<String>();
        list.add("State of Practice");

        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_lay,list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        state.setAdapter(dataAdapter);
        state.setSelection(0);

        list = new ArrayList<String>();
        list.add("City");

        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_lay,list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        city.setAdapter(dataAdapter);
        city.setSelection(0);

        list = new ArrayList<String>();
        list.add("Speciality");

        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_lay,list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        speciality.setAdapter(dataAdapter);
        speciality.setSelection(0);

        list = new ArrayList<String>();
        list.add("Hospital Affiliation");

        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_lay,list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hospital.setAdapter(dataAdapter);
        hospital.setSelection(0);
        list = new ArrayList<String>();
        list.add("Medical Schools");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_lay,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medical.setAdapter(dataAdapter);

        list = new ArrayList<String>();
        list.add("Residency Program");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_lay,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        residency.setAdapter(dataAdapter);

        list = new ArrayList<String>();
        list.add("Fellowship Program");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_lay,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fellow.setAdapter(dataAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nickname.getText().toString().trim().length() > 0
                        || fname.getText().toString().trim().length() > 0
                        || lname.getText().toString().trim().length() > 0 || usertype.getSelectedItemId() > 0
                        || state.getSelectedItemId() > 0 || speciality.getSelectedItemId() > 0
                        || hospital.getSelectedItemId() > 0 || medical.getSelectedItemId() > 0
                        ||residency.getSelectedItemId() > 0
                        || fellow.getSelectedItemId() > 0 ) {

                    int selectedId = gender.getCheckedRadioButtonId();
                    genderSelected = (RadioButton) findViewById(selectedId);

                    String[] param = new String[15];
                    param[0] = CallDispatcher.LoginUser;
                        param[1] = "";
                    param[2] = fname.getText().toString();
                    param[3] = lname.getText().toString();
                    if(genderSelected.getText().toString()!=null)
                        param[4] =genderSelected.getText().toString();
                    else
                        param[4] ="";
                    if(usertype.getSelectedItemId() > 0)
                        param[5] = usertype.getSelectedItem().toString();
                    else
                        param[5] = "";
                    if(state.getSelectedItemId() > 0)
                        param[6] = state.getSelectedItem().toString();
                    else
                        param[6]="";
                    if(speciality.getSelectedItemId() > 0)
                        param[7] = speciality.getSelectedItem().toString();
                    else
                        param[7] = "";
                    if(medical.getSelectedItemId() > 0)
                        param[8] = medical.getSelectedItem().toString();
                    else
                        param[8] = "";
                    if(residency.getSelectedItemId() > 0)
                        param[9] = residency.getSelectedItem().toString();
                    else
                        param[9] = "";
                    if(fellow.getSelectedItemId() > 0)
                        param[10] = fellow.getSelectedItem().toString();
                    else
                        param[10] = "";
                        param[11] = "";
                    if(hospital.getSelectedItemId() > 0)
                        param[12] = hospital.getSelectedItem().toString();
                    else
                        param[12] = "";
                        param[13] = "";

                    param[14] = nickname.getText().toString();
                    WebServiceReferences.webServiceClient.SearchPeopleByAccount(param, AppMainActivity.searchContext);
                    showDialog();

                } else {
                    Toast.makeText(context,
                            "Please enter any one field to search",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
//        advsearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Searchpeople.this, AdvanceSearchPeople.class);
//                startActivity(intent);
//            }
//        });

    }

    public void notifySearchPeople(Object obj)
    {
        Log.i("AAAA", "Search people ");
        cancelDialog();
        if (obj instanceof ArrayList) {
            ArrayList<BuddyInformationBean> response = (ArrayList<BuddyInformationBean>) obj;
            Intent intentadd = new Intent(context, FindPeople.class);
            intentadd.putExtra("fromprofile", "yes");
            SingleInstance.searchedResult.clear();
            SingleInstance.searchedResult.addAll(response);
            startActivity(intentadd);
            finish();
        } else if(obj instanceof WebServiceBean){
            final String result1 = ((WebServiceBean) obj).getText();
            handler.post(new Runnable() {

                @Override
                public void run() {
                    Log.i("AAAA", "Search people " + result1);
                    Toast.makeText(context,
                            result1, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void showDialog() {
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
