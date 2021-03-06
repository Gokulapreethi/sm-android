package com.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.account.FindPeople;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.WebServiceBean;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class SearchPeopleFragment extends Fragment {

    private static SearchPeopleFragment searchPeopleFragment;
    private static CallDispatcher calldisp = null;
    public static Context mainContext;
    public View _rootView;
    EditText lname,fname,nickname;
    Button search,back,advsearch;
    AutoCompleteTextView usertype,state,city,speciality,hospital,medical,residency,fellow;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    RadioGroup gender;
    RadioButton genderSelected;
    Boolean isClicked=true;
    AppMainActivity appMainActivity;
    private RadioButton radioFemale;
    private RadioButton radioMale ;

    ArrayList<String> stateList = new ArrayList<String>();
    ArrayList<String> specialityList = new ArrayList<String>();
    ArrayList<String> medicalschoolsList = new ArrayList<String>();
    ArrayList<String> hospitalList = new ArrayList<>();
    private boolean isUsertype=false,isState=false;
    private boolean isSpeciality=false,isMedical=false,isResidency=false;
    private boolean isFellow=false,isHospital=false,isCity=false;

    public static SearchPeopleFragment newInstance(Context context) {
        try {
            if (searchPeopleFragment == null) {
                searchPeopleFragment = new SearchPeopleFragment();
                mainContext = context;
                calldisp = CallDispatcher.getCallDispatcher(context);

            }
            return searchPeopleFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return searchPeopleFragment;
        }
    }

    public void setContext(Context cxt) {

        this.mainContext = cxt;
    }@Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        AppReference.bacgroundFragment=searchPeopleFragment;
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        title.setText("ADD CONTACTS");
        title.setTextSize(17);
        title.setGravity(Gravity.CENTER);
        appMainActivity = (AppMainActivity) SingleInstance.contextTable
                .get("MAIN");


        Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                appMainActivity.removeFragments(searchPeopleFragment.newInstance(SingleInstance.mainContext));
                ContactsFragment contactsFragment = ContactsFragment.getInstance(mainContext);
                FragmentManager fragmentManager = SingleInstance.mainContext
                        .getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(
                        R.id.activity_main_content_fragment, contactsFragment)
                        .commitAllowingStateLoss();
            }
        });
        _rootView = null;
        if (_rootView == null) {

            _rootView = inflater.inflate(R.layout.search_contact, null);
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            try {
                final TextView tv_nickname=(TextView)_rootView.findViewById(R.id.tv_nickname);
                final TextView tv_fname=(TextView)_rootView.findViewById(R.id.tv_fname);
                final TextView tv_lname=(TextView)_rootView.findViewById(R.id.tv_lname);

                final TextView tv_usertype=(TextView)_rootView.findViewById(R.id.tv_usertype);
                final TextView tv_state=(TextView)_rootView.findViewById(R.id.tv_state);
                final TextView tv_speciality=(TextView)_rootView.findViewById(R.id.tv_speciality);
                final TextView tv_city=(TextView)_rootView.findViewById(R.id.tv_city);
                final TextView tv_hospital=(TextView)_rootView.findViewById(R.id.tv_hospital);
                final TextView tv_medical=(TextView)_rootView.findViewById(R.id.tv_medical);
                final TextView tv_residency=(TextView)_rootView.findViewById(R.id.tv_residency);
                final TextView tv_fellow=(TextView)_rootView.findViewById(R.id.tv_fellow);

                LinearLayout advance=(LinearLayout)_rootView.findViewById(R.id.advance);
                final LinearLayout advance_lay=(LinearLayout)_rootView.findViewById(R.id.advance_lay);
                final Button arrow=(Button)_rootView.findViewById(R.id.arrow);
                lname = (EditText) _rootView.findViewById(R.id.lName);
                fname = (EditText) _rootView.findViewById(R.id.fName);
                nickname = (EditText) _rootView.findViewById(R.id.nicName);
                usertype = (AutoCompleteTextView) _rootView.findViewById(R.id.usertype);
                state = (AutoCompleteTextView) _rootView.findViewById(R.id.state_tilte);
                city = (AutoCompleteTextView) _rootView.findViewById(R.id.city_tilte);
                speciality = (AutoCompleteTextView) _rootView.findViewById(R.id.spec_tilte);
                hospital = (AutoCompleteTextView) _rootView.findViewById(R.id.hos_tilte);
                search = (Button) _rootView.findViewById(R.id.search);
                back = (Button) _rootView.findViewById(R.id.cancel);
                medical = (AutoCompleteTextView) _rootView.findViewById(R.id.medical_scl);
                residency = (AutoCompleteTextView) _rootView.findViewById(R.id.resi_pro);
                fellow = (AutoCompleteTextView) _rootView.findViewById(R.id.fellow);
                gender = (RadioGroup) _rootView.findViewById(R.id.gender);
                gender.setVisibility(View.GONE);
                TextView sex=(TextView)_rootView.findViewById(R.id.textview_sex);
                sex.setVisibility(View.GONE);
                View view_undersex=(View)_rootView.findViewById(R.id.view_undersexfield);
                view_undersex.setVisibility(View.GONE);
                final ImageView usertype_img=(ImageView)_rootView.findViewById(R.id.usertype_img);
                usertype_img.setVisibility(View.VISIBLE);
                final ImageView state_img=(ImageView)_rootView.findViewById(R.id.state_img);
                final ImageView speciality_img=(ImageView)_rootView.findViewById(R.id.specialty_img);
                final ImageView medical_img=(ImageView)_rootView.findViewById(R.id.medical_img);
                final ImageView residency_img=(ImageView)_rootView.findViewById(R.id.residency_img);
                final ImageView fellow_img=(ImageView)_rootView.findViewById(R.id.fellow_img);
                final ImageView hospital_img=(ImageView)_rootView.findViewById(R.id.hospital_img);
                final ImageView city_img=(ImageView)_rootView.findViewById(R.id.city_img);
                arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isClicked) {
                            isClicked=false;
                            advance_lay.setVisibility(View.VISIBLE);
                            arrow.setBackgroundResource(R.drawable.button_arrow_up);
                        }else {
                            isClicked=true;
                            advance_lay.setVisibility(View.GONE);
                            arrow.setBackgroundResource(R.drawable.input_arrow);
                        }
                    }
                });
                radioMale = (RadioButton) _rootView.findViewById(R.id.radioMale);
                radioFemale = (RadioButton) _rootView.findViewById(R.id.radioFemale);
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
                list.add("Student");
                list.add("Resident");
                list.add("Fellow");
                list.add("Attending");
                list.add("Other");
                ArrayAdapter<String> dataAdapter;

                dataAdapter = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list,list);
                dataAdapter.setDropDownViewResource
                        (R.layout.spinner_dropdown_list);

                usertype.setAdapter(dataAdapter);
                usertype.setThreshold(1);
                usertype_img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager)mainContext. getSystemService(mainContext.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isUsertype) {
                            isUsertype = false;
                            usertype.dismissDropDown();
                            usertype_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isUsertype = true;
                            usertype_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            usertype.showDropDown();
                        }
                    }
                });

                stateList= DBAccess.getdbHeler().getStateDetails();
                TreeSet treeSet_statelist=new TreeSet(stateList);
                ArrayList arrayList_statelist=new ArrayList(treeSet_statelist);
                dataAdapter = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list,arrayList_statelist);

                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

                state.setAdapter(dataAdapter);
                state.setThreshold(1);
                state_img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) mainContext.getSystemService(mainContext.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isState) {
                            isState = false;
                            state.dismissDropDown();
                            state_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isState = true;
                            state_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            state.showDropDown();
                        }
                    }
                });


                list = new ArrayList<String>();
                list.addAll(SingleInstance.mainContext.cityList);
                TreeSet treeSet_citylist=new TreeSet(list);
                ArrayList arrayList_citylist=new ArrayList(treeSet_citylist);
                dataAdapter = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list,arrayList_citylist);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

                city.setAdapter(dataAdapter);
                city.setThreshold(1);
                city_img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager)mainContext. getSystemService(mainContext.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isCity) {
                            isCity = false;
                            city.dismissDropDown();
                            city_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isCity = true;
                            city_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            city.showDropDown();
                        }
                    }
                });

                specialityList= DBAccess.getdbHeler().getSpecialityDetails();
                TreeSet treeSet_specialitylist=new TreeSet(specialityList);
                ArrayList arrayList_specialitylist=new ArrayList(treeSet_specialitylist);
                dataAdapter = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list,arrayList_specialitylist);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

                speciality.setAdapter(dataAdapter);
                speciality.setThreshold(1);
                speciality_img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager)mainContext. getSystemService(mainContext.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isSpeciality) {
                            isSpeciality = false;
                            speciality.dismissDropDown();
                            speciality_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isSpeciality = true;
                            speciality_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            speciality.showDropDown();
                        }
                    }
                });

                hospitalList= DBAccess.getdbHeler().getHospitalDetails();
                TreeSet treeSet_hospital=new TreeSet(hospitalList);
                ArrayList arrayList_hospital=new ArrayList(treeSet_hospital);
                dataAdapter = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list,arrayList_hospital);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

                hospital.setAdapter(dataAdapter);
                hospital.setThreshold(1);
                hospital_img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager)mainContext. getSystemService(mainContext.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isHospital) {
                            isHospital = false;
                            hospital.dismissDropDown();
                            hospital_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isHospital = true;
                            hospital_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            hospital.showDropDown();
                        }
                    }
                });

                medicalschoolsList= DBAccess.getdbHeler().getMedicalSchoolDetails();
                TreeSet treeSet_medicalschoollist=new TreeSet(medicalschoolsList);
                ArrayList arrayList_medicalschoollist=new ArrayList(treeSet_medicalschoollist);
                dataAdapter = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list,arrayList_medicalschoollist);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
                medical.setAdapter(dataAdapter);
                medical.setThreshold(1);
                medical_img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager)mainContext. getSystemService(mainContext.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isMedical) {
                            isMedical = false;
                            medical.dismissDropDown();
                            medical_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isMedical = true;
                            medical_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            medical.showDropDown();
                        }
                    }
                });

                list = new ArrayList<String>();
                Registration reg=new Registration();
                list.addAll(reg.loadResidencyFiles());
                TreeSet treeSet_residency=new TreeSet(list);
                ArrayList arrayList_residency=new ArrayList(treeSet_residency);
                dataAdapter = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list,arrayList_residency);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
                residency.setAdapter(dataAdapter);
                residency.setThreshold(1);
                residency_img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager)mainContext. getSystemService(mainContext.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isResidency) {
                            isResidency = false;
                            residency.dismissDropDown();
                            residency_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isResidency = true;
                            residency_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            residency.showDropDown();
                        }
                    }
                });
                list = new ArrayList<String>();
                list.addAll(reg.loadFellowship());
                TreeSet treeSet_fellow=new TreeSet(list);
                ArrayList arrayList_fellow=new ArrayList(treeSet_fellow);
                dataAdapter = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list,arrayList_fellow);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
                fellow.setAdapter(dataAdapter);
                fellow.setThreshold(1);
                fellow_img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager)mainContext. getSystemService(mainContext.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isFellow) {
                            isFellow = false;
                            fellow.dismissDropDown();
                            fellow_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isFellow = true;
                            fellow_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            fellow.showDropDown();
                        }
                    }
                });
                usertype.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            tv_usertype.setVisibility(View.VISIBLE);
                        } else {
                            tv_usertype.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        usertype_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                    }
                });
                state.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            tv_state.setVisibility(View.VISIBLE);
                        } else {
                            tv_state.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        state_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                    }
                });
                speciality.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            tv_speciality.setVisibility(View.VISIBLE);
                        } else {
                            tv_speciality.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        speciality_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                    }
                });
                city.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            tv_city.setVisibility(View.VISIBLE);
                        } else {
                            tv_city.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        city_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                    }
                });
                hospital.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            tv_hospital.setVisibility(View.VISIBLE);
                        } else {
                            tv_hospital.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        hospital_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                    }
                });
                medical.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            tv_medical.setVisibility(View.VISIBLE);
                        } else {
                            tv_medical.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        medical_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                    }
                });
                residency.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            tv_residency.setVisibility(View.VISIBLE);
                        } else {
                            tv_residency.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        residency_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                    }
                });
                fellow.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            tv_fellow.setVisibility(View.VISIBLE);
                        } else {
                            tv_fellow.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        fellow_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                    }
                });

                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedId = gender.getCheckedRadioButtonId();
                        if(selectedId > 0)
                            genderSelected = (RadioButton) _rootView.findViewById(selectedId);
//                        if (nickname.getText().toString().trim().length() > 0
//                                || fname.getText().toString().trim().length() > 0
//                                || lname.getText().toString().trim().length() > 0
//                                || usertype.getText().toString().trim().length() > 0
//                                || state.getText().toString().trim().length() > 0
//                                || speciality.getText().toString().trim().length() > 0
//                                || hospital.getText().toString().trim().length() > 0
//                                || medical.getText().toString().trim().length() > 0
//                                ||residency.getText().toString().trim().length() > 0
//                                || fellow.getText().toString().trim().length() > 0
//                                || (genderSelected!=null && genderSelected.getText().toString()!=null)) {

                            Log.i("AAAA","Search "+gender.getCheckedRadioButtonId());
                            String[] param = new String[15];
                            param[0] = CallDispatcher.LoginUser;
                            param[1] = "";
                            param[2] = fname.getText().toString();
                            param[3] = lname.getText().toString();

                            if(genderSelected!=null && genderSelected.getText().toString()!=null)
                                param[4] =genderSelected.getText().toString();
                            else
                                param[4] ="";
                                param[5] = usertype.getText().toString().trim();
                                param[6] = state.getText().toString().trim();
                                param[7] = speciality.getText().toString().trim();
                                param[8] = medical.getText().toString().trim();
                                param[9] = residency.getText().toString().trim();
                                param[10] = fellow.getText().toString().trim();
                                param[11] = "";
                                param[12] = hospital.getText().toString().trim();
                                param[13] = "";
                                param[14] = nickname.getText().toString();
                            WebServiceReferences.webServiceClient.SearchPeopleByAccount(param, searchPeopleFragment);
                            showDialog();

//                        } else {
//                            Toast.makeText(mainContext,
//                                    "Please enter any one field to search",
//                                    Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//            ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        return _rootView;
    }
    public View getParentView() {

        return _rootView;
    }

    @Override
    public void onDestroyView() {
        _rootView =null;
        super.onDestroyView();
    }
    @Override
    public void onResume() {
        super.onResume();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("AAAA", "on destroy ");
                nickname.setText("");
                fname.setText("");
                lname.setText("");
                state.setText("");
                speciality.setText("");
                hospital.setText("");
                medical.setText("");
                residency.setText("");
                fellow.setText("");
                usertype.setText("");
                city.setText("");
                radioFemale.setChecked(false);
                radioMale.setChecked(false);
            }
        });
    }

    public void notifySearchPeople(Object obj)
    {
        Log.i("AAAA", "Search people ");
        if (obj instanceof ArrayList) {
            ArrayList<BuddyInformationBean> response = (ArrayList<BuddyInformationBean>) obj;
            cancelDialog();
            appMainActivity.removeFragments(searchPeopleFragment.newInstance(SingleInstance.mainContext));
            FindPeople findPeople = FindPeople.newInstance(SingleInstance.mainContext);
            FragmentManager fragmentManager = SingleInstance.mainContext
                    .getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(
                    R.id.activity_main_content_fragment, findPeople)
                    .commitAllowingStateLoss();
            SingleInstance.searchedResult.clear();
            SingleInstance.searchedResult.addAll(response);
        } else if(obj instanceof WebServiceBean){
            cancelDialog();
            final String result1 = ((WebServiceBean) obj).getText();
            handler.post(new Runnable() {

                @Override
                public void run() {
                    Log.i("AAAA", "Search people " + result1);
                    Toast.makeText(mainContext,
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
                    progressDialog = new ProgressDialog(mainContext);
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
