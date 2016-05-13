package com.cg.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;

import java.util.ArrayList;
import java.util.List;


public class AdvanceSearchPeople extends Activity {

    EditText lname,fname,nickname;
    Button back,advsearch;
    Context context;
    Spinner usertype,state,city,speciality,hospital,mobileno,email,medical,residency,fellow,offc_info;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    private CallDispatcher callDisp = null;
    RadioGroup gender;
    RadioButton genderSelected;
    private Searchpeople searchpeople=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.advancesearch);
        context = this;
        lname = (EditText) findViewById(R.id.lName);
        fname = (EditText) findViewById(R.id.fName);
        nickname = (EditText) findViewById(R.id.nicName);
        usertype = (Spinner) findViewById(R.id.usertype);
        state = (Spinner) findViewById(R.id.stat_of_pract);
        city = (Spinner) findViewById(R.id.city_tilte);
        speciality = (Spinner) findViewById(R.id.spec_tilte);
        hospital = (Spinner) findViewById(R.id.hos_tilte);
        mobileno = (Spinner) findViewById(R.id.mbl_no);
        email = (Spinner) findViewById(R.id.ema_il);
        medical = (Spinner) findViewById(R.id.medical_scl);
        residency = (Spinner) findViewById(R.id.resi_pro);
        fellow = (Spinner) findViewById(R.id.fellow);
        offc_info = (Spinner) findViewById(R.id.off_info);
        advsearch = (Button) findViewById(R.id.advsearch);
        back = (Button) findViewById(R.id.back);
        gender = (RadioGroup) findViewById(R.id.gender);

        if (WebServiceReferences.callDispatch.containsKey("calldisp"))
            callDisp = (CallDispatcher) WebServiceReferences.callDispatch
                    .get("calldisp");
        else
            callDisp = new CallDispatcher(this);
        searchpeople=new Searchpeople();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        List<String> list = new ArrayList<String>();
        list.add("Physician");
        list.add("Student.");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        usertype.setAdapter(dataAdapter);
        usertype.setSelection(0);

        list = new ArrayList<String>();
        list.add("State of Practice");
        list.add("Attending Physician1");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(dataAdapter);
        state.setSelection(0);

        list = new ArrayList<String>();
        list.add("City");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(dataAdapter);
        city.setSelection(0);

        list = new ArrayList<String>();
        list.add("Speciality");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speciality.setAdapter(dataAdapter);
        speciality.setSelection(0);

        list = new ArrayList<String>();
        list.add("Hospital Affiliation");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospital.setAdapter(dataAdapter);
        hospital.setSelection(0);
        list = new ArrayList<String>();
        list.add("Hospital Affiliation");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        residency.setAdapter(dataAdapter);

        list = new ArrayList<String>();
        list.add("Hospital Affiliation");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medical.setAdapter(dataAdapter);

        list = new ArrayList<String>();
        list.add("Hospital Affiliation");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fellow.setAdapter(dataAdapter);

        list = new ArrayList<String>();
        list.add("Hospital Affiliation");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        email.setAdapter(dataAdapter);

        list = new ArrayList<String>();
        list.add("Hospital Affiliation");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mobileno.setAdapter(dataAdapter);

        list = new ArrayList<String>();
        list.add("Hospital Affiliation");
        list.add("Chennai");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        offc_info.setAdapter(dataAdapter);

        advsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nickname.getText().toString().trim().length() > 0
                        || fname.getText().toString().trim().length() > 0
                        || lname.getText().toString().trim().length() > 0 || usertype.getSelectedItemId() > 0
                        || state.getSelectedItemId() > 0 || speciality.getSelectedItemId() > 0
                        || hospital.getSelectedItemId() > 0 || medical.getSelectedItemId() > 0
                        || email.getSelectedItemId() > 0 || residency.getSelectedItemId() > 0
                        || fellow.getSelectedItemId() > 0 || mobileno.getSelectedItemId() > 0
                        || offc_info.getSelectedItemId() > 0) {

                    int selectedId = gender.getCheckedRadioButtonId();
                    genderSelected = (RadioButton) findViewById(selectedId);

                    String[] param = new String[15];
                    param[0] = CallDispatcher.LoginUser;
                    if(email.getSelectedItemId() > 0)
                        param[1] = email.getSelectedItem().toString();
                    else
                        param[1] = "";
                    param[2] = fname.getText().toString();
                    param[3] = lname.getText().toString();
                    param[4] = genderSelected.getText().toString();
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
                    if(offc_info.getSelectedItemId() > 0)
                        param[11] = offc_info.getSelectedItem().toString();
                    else
                        param[11] = "";
                    if(hospital.getSelectedItemId() > 0)
                        param[12] = hospital.getSelectedItem().toString();
                    else
                        param[12] = "";
                    if(mobileno.getSelectedItemId() > 0)
                        param[13] = mobileno.getSelectedItem().toString();
                    else
                        param[13] = "";

                    param[14] = nickname.getText().toString();
                    WebServiceReferences.webServiceClient.SearchPeopleByAccount(param, AppMainActivity.searchContext);
                    searchpeople.showDialog();
                    finish();
                } else {
                    Toast.makeText(context,
                            "Please enter any one field to search",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
