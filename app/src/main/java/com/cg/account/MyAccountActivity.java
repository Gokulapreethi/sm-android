package com.cg.account;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.main.MyAccountFragment;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;

import org.lib.model.FileDetailsBean;
import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyAccountActivity extends Activity {
    private Context context;
    Boolean isClicked=true;
    AutoCompleteTextView title, usertype;
    AutoCompleteTextView state,hospital,state1;
    RadioButton genderSelected;
    RadioGroup gender;
    ImageView profile_pic, edit_pic,status_icon;
    Button img_cite;
    String strIPath;
     TextView nickname, fname,lname, offc ,edOffc,citations,statusTxt ,edcitations;
     EditText edNickname , edFname , edLname ,tv_cite ,tv_addr;
    private ImageLoader imageLoader;
    ArrayAdapter<String> stateAdapter;
    private LinearLayout cite_lay, cite_lay1;

    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    AutoCompleteTextView rlay_professional_org ,Speciality ,residency_pgm, fellowship_pgm;
    AutoCompleteTextView medical_schools ;
    AutoCompleteTextView association_membership;
    ArrayAdapter<String> hospitalDetailsAdapter;
    private String add_citation="", Addressline1,Addressline2, office_phone_no, office_fax, zip_code, city_value,office_address, state_value = "";
    ArrayAdapter<String> medicalDetailsAdapter;
    ArrayList<String> stateList = new ArrayList<String>();
    ArrayList<String> specialityList = new ArrayList<String>();
    ArrayList<String> medicalschoolsList = new ArrayList<String>();
    ArrayList<String> hospitalList = new ArrayList<String>();
    ArrayList<String> medicalSocietyList = new ArrayList<String>();
    ArrayList<String> states=new ArrayList<String>();
    private String pastingContentCopy;

    protected void onCreate(Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.my_account_1);
        context = this;
        WebServiceReferences.contextTable.put("myaccountactivity", this);
        String status=getIntent().getStringExtra("status");
        imageLoader = new ImageLoader(context);
          nickname=(TextView)findViewById(R.id.tv_nickname);
        statusTxt=(TextView)findViewById(R.id.statusTxt1);
          fname=(TextView)findViewById(R.id.tv_fname);
          lname=(TextView)findViewById(R.id.tv_lname);
          offc=(TextView)findViewById(R.id.tv_offc);
          citations=(TextView)findViewById(R.id.tv_citations);
//          tv_cite=(EditText)findViewById(R.id.tv_cite);
          tv_addr=(EditText)findViewById(R.id.tv_addr);


        gender = (RadioGroup) findViewById(R.id.gender);

          edNickname = (EditText) findViewById(R.id.etRegisuser);
          edFname = (EditText) findViewById(R.id.etfname);
          edLname = (EditText) findViewById(R.id.etlname);
          edOffc = (TextView) findViewById(R.id.offcAddress);
          edcitations = (TextView) findViewById(R.id.citation);
        final CheckBox chbox1 = (CheckBox) findViewById(R.id.chbox1);
        final CheckBox chbox2 = (CheckBox) findViewById(R.id.chbox2);
        Button cancel=(Button)findViewById(R.id.cancel);
        profile_pic = (ImageView)findViewById(R.id.riv1);
        edit_pic = (ImageView)findViewById(R.id.capture_image_view);
        status_icon = (ImageView)findViewById(R.id.dot);

        final LinearLayout optional = (LinearLayout) findViewById(R.id.optional);
        cite_lay = (LinearLayout) findViewById(R.id.cite_lay);
        cite_lay1 = (LinearLayout) findViewById(R.id.cite_lay1);
//        img_cite = (Button)findViewById(R.id.img_cite);
        final LinearLayout addr_lay = (LinearLayout) findViewById(R.id.addr_lay);
        final LinearLayout advance_lay=(LinearLayout)findViewById(R.id.optional_lay);
        final Button arrow=(Button)findViewById(R.id.arrow);
        final Button cite_plus=(Button)findViewById(R.id.cite_plus);
        final Button addr_plus=(Button)findViewById(R.id.addr_plus);
        Button btnRegisterOk = (Button) findViewById(R.id.btnRegisterOk);
        final TextView tv_hospital=(TextView)findViewById(R.id.tv_hospital);
        final TextView tv_state=(TextView)findViewById(R.id.tv_state);
        final TextView tv_medical=(TextView)findViewById(R.id.tv_medical);
//        final EditText tv_addr = (EditText)findViewById(R.id.tv_addr);
        final TextView tv_association=(TextView)findViewById(R.id.tv_association);
        statusTxt.setText(status);
        if(status.equalsIgnoreCase("online"))
            status_icon.setBackgroundResource(R.drawable.online_icon);
        else if(status.equalsIgnoreCase("busy"))
            status_icon.setBackgroundResource(R.drawable.busy_icon);
        else if(status.equalsIgnoreCase("invisible"))
            status_icon.setBackgroundResource(R.drawable.invisibleicon);
        else if(status.equalsIgnoreCase("offline"))
            status_icon.setBackgroundResource(R.drawable.offline_icon);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        cite_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.add_citation);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    Window window = dialog.getWindow();
                    dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    window.setAttributes(lp);
                    dialog.show();

                    final EditText citation_info = (EditText)dialog.findViewById(R.id.Edit_text1);


                    Button save = (Button)dialog.findViewById(R.id.save_button1);
                    Button cancel = (Button)dialog.findViewById(R.id.cancel_button1);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(add_citation==null) {
                                if(SingleInstance.myAccountBean.getCitationpublications()!=null)
                                add_citation = SingleInstance.myAccountBean.getCitationpublications();
                            }else
                                add_citation = add_citation+","+citation_info.getText().toString().trim();
                            String[] split = add_citation.split(",");
                            cite_lay.removeAllViews();
                            cite_lay1.removeAllViews();
                            for (int i = 0; i < split.length; i++) {
                                LinearLayout llay = new LinearLayout(context);
                                LinearLayout.LayoutParams dut = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                dut.leftMargin = 20;
                                dut.rightMargin = 20;
                                llay.setLayoutParams(dut);
                                llay.setWeightSum(1);


                                TextView dynamicText = new TextView(context);
                                LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                dim.leftMargin = 30;
                                dim.weight = 1;
                                dim.topMargin = 5;
                                dynamicText.setLayoutParams(dim);
                                Button button = new Button(context);
                                LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                button.setGravity(Gravity.END);
                                but.rightMargin = 15;


                                button.setLayoutParams(but);
                                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_delete));


                                if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                                    dynamicText.setText(split[i]);
                                    llay.addView(dynamicText);
                                    llay.addView(button);
                                    cite_lay1.addView(llay);
                                }
                            }

                            dialog.dismiss();

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addr_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    final Dialog dialog1 = new Dialog(context);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.office_address);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog1.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    Window window = dialog1.getWindow();
                    dialog1.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    window.setAttributes(lp);
                    dialog1.show();


                   final EditText Address1 = (EditText)dialog1.findViewById(R.id.address_edit1);
                    final EditText address2 = (EditText)dialog1.findViewById(R.id.address_edit2);
                    final EditText zip = (EditText)dialog1.findViewById(R.id.address_edit3);
                    final EditText city = (EditText)dialog1.findViewById(R.id.address_edit4);
                    final EditText office_phone_number = (EditText)dialog1.findViewById(R.id.address_edit5);
                    final EditText office_fax_number = (EditText)dialog1.findViewById(R.id.address_edit6);
                   state1 = (AutoCompleteTextView)dialog1.findViewById(R.id.state_of_practice);

                    Button save1 = (Button)dialog1.findViewById(R.id.save_button2);
                    Button cancel1 = (Button)dialog1.findViewById(R.id.cancel_button2);

                    cancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                    save1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Addressline1 = Address1.getText().toString();
                            Addressline2 = address2.getText().toString();
                            zip_code = zip.getText().toString();
                            city_value = city.getText().toString();
                            state_value = state1.getText().toString().trim();
                            office_phone_no = office_phone_number.getText().toString();
                            office_fax= office_fax_number.getText().toString();

                            String office_address = "";
                            if(Addressline1!= null && !Addressline1.trim().equals("")){
                                 office_address =Addressline1;
                            }
                            if(Addressline2!= null && !Addressline2.trim().equals("")){
                                office_address =office_address+","+Addressline2;
                            }
                            if(zip_code!= null && !zip_code.trim().equals("")){
                                office_address =office_address+","+zip_code;
                            }
                            if(city_value!= null && !city_value.trim().equals("")){
                                office_address =office_address+","+city_value;
                            }
                            if(state_value!= null && !state_value.trim().equals("")){
                                office_address =office_address+","+state_value;
                            }
                            if(office_phone_no!= null && !office_phone_no.trim().equals("")){
                                office_address =office_address+","+office_phone_no;
                            }
                            if(office_fax!= null && !office_fax.trim().equals("")){
                                office_address =office_address+","+office_fax;
                            }

//                            office_address = (Addressline1 + ", "+ Addressline2+ ", "+ zip_code+ ", "+city_value+ ", "+state_value+","+office_phone_no+ ", "+office_fax);

                            if (Address1.length()> 0) {
                                Address1.append(",");
                            }
                            tv_addr.setText(office_address);
                            dialog1.dismiss();

                        }
                    });
                    states= DBAccess.getdbHeler().getStateDetails();

                    stateAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, states);
                    state1.setAdapter(stateAdapter);
                    state1.setThreshold(1);

                    addr_lay.setVisibility(View.VISIBLE);
//                    tv_addr.setText(edOffc.getText().toString());
                    offc.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        optional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(optional.getWindowToken(), 0);
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
        edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strIPath = Environment.getExternalStorageDirectory()
                        + "/COMMedia/MPD_" + getFileName()
                        + ".jpg";
                Intent intent = new Intent(MyAccountActivity.this, CustomVideoCamera.class);
                intent.putExtra("filePath", strIPath);
                intent.putExtra("isPhoto", true);
                startActivityForResult(intent, 1);
            }
        });
        edNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    nickname.setVisibility(View.VISIBLE);
                } else {
                    nickname.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edFname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    fname.setVisibility(View.VISIBLE);
                } else {
                    fname.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edLname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    lname.setVisibility(View.VISIBLE);
                } else {
                    lname.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edOffc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    offc.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edcitations.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    citations.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        title = (AutoCompleteTextView) findViewById(R.id.rlay_tilte);
        usertype = (AutoCompleteTextView) findViewById(R.id.rlay_user_type);
        state = (AutoCompleteTextView) findViewById(R.id.rlay_state_of_practice);
        hospital = (AutoCompleteTextView) findViewById(R.id.rlay_hospital_affiliation);

          rlay_professional_org = (AutoCompleteTextView) findViewById(R.id.prof_desgn);
          Speciality = (AutoCompleteTextView) findViewById(R.id.speciality);
          medical_schools = (AutoCompleteTextView) findViewById(R.id.medical_scl);
          residency_pgm = (AutoCompleteTextView) findViewById(R.id.resi_pro);
          fellowship_pgm = (AutoCompleteTextView)findViewById(R.id.fellow);
         association_membership = (AutoCompleteTextView)findViewById(R.id.association_membership);


        stateList= DBAccess.getdbHeler().getStateDetails();
        stateAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, stateList);
        state.setAdapter(stateAdapter);
        state.setThreshold(1);

        hospitalList=DBAccess.getdbHeler().getHospitalDetails();
        hospitalDetailsAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, hospitalList);
        hospital.setAdapter(hospitalDetailsAdapter);
        hospital.setThreshold(1);

        medicalSocietyList=DBAccess.getdbHeler().getMedicalSocietiesDetails();
        medicalDetailsAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, medicalSocietyList);
        association_membership.setAdapter(medicalDetailsAdapter);
        association_membership.setThreshold(1);



        List<String> list = new ArrayList<String>();
        list.add("Mr.");
        list.add("Ms.");
        list.add("Miss.");
        list.add("Mrs.");
        list.add("Dr.");
        list.add("Prof.");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown_list,list);

        dataAdapter.setDropDownViewResource
                (R.layout.spinner_dropdown_list);

        title.setAdapter(dataAdapter);
        title.setSelection(0);

        list = new ArrayList<String>();
        list.add("Student");
        list.add("Resident");
        list.add("Fellow");
        list.add("Attending");
        list.add("Other");

        dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        usertype.setAdapter(dataAdapter);
        usertype.setThreshold(1);

        state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    tv_state.setVisibility(View.VISIBLE);

                } else
                    tv_state.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        association_membership.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    tv_association.setVisibility(View.VISIBLE);
                } else {
                    tv_association.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        medicalschoolsList=DBAccess.getdbHeler().getMedicalSchoolDetails();
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list,medicalschoolsList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        medical_schools.setAdapter(dataAdapter);
        medical_schools.setSelection(0);

        list = new ArrayList<String>();
        list.add("Residency Program");
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list,list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        residency_pgm.setAdapter(dataAdapter);
        residency_pgm.setSelection(0);

        list = new ArrayList<String>();
        list.add("Fellowship Program");
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list,list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        fellowship_pgm.setAdapter(dataAdapter);
        fellowship_pgm.setSelection(0);

        list = new ArrayList<String>();
        list.add("MD");
        list.add("DO");
        list.add("Othr");
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list,list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        rlay_professional_org.setAdapter(dataAdapter);
        rlay_professional_org.setSelection(0);

        specialityList=DBAccess.getdbHeler().getSpecialityDetails();
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list,specialityList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        Speciality.setAdapter(dataAdapter);
        Speciality.setThreshold(1);


        hospital.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    tv_hospital.setVisibility(View.VISIBLE);

                }else
                    tv_hospital.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        btnRegisterOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (edNickname.getText().toString().trim().length() > 0
//                        && fName.getText().toString().trim().length() > 0
//                        && lName.getText().toString().trim().length() > 0
//                        && strIPath != null) {
                ProfileBean pBean = new ProfileBean();
                pBean.setUsername(CallDispatcher.LoginUser);
                pBean.setNickname(edNickname.getText().toString().trim());
                if (strIPath != null) {
                    File f = new File(strIPath);
                    if (f.exists())
                        pBean.setPhoto(f.getName());
                }
                pBean.setTitle(title.getText().toString().trim());
                pBean.setFirstname(edFname.getText().toString().trim());
                pBean.setLastname(edLname.getText().toString().trim());
                int selectedId = gender.getCheckedRadioButtonId();
                if (selectedId > 0) {
                    genderSelected = (RadioButton) findViewById(selectedId);
                    pBean.setSex(genderSelected.getText().toString());
                } else
                    pBean.setSex("");
                pBean.setUsertype(usertype.getText().toString().trim());
                pBean.setState(state.getText().toString().trim());
                pBean.setProfession(rlay_professional_org.getText().toString().trim());
                pBean.setSpeciality(Speciality.getText().toString().trim());
                pBean.setMedicalschool(medical_schools.getText().toString().trim());
                pBean.setResidencyprogram(residency_pgm.getText().toString().trim());
                pBean.setFellowshipprogram(fellowship_pgm.getText
                        ().toString().trim());
                pBean.setOfficeaddress(tv_addr.getText().toString().trim());
                pBean.setHospitalaffiliation(hospital.getText().toString().trim());
                pBean.setCitationpublications(add_citation);
                pBean.setOrganizationmembership(association_membership.getText().toString());
                pBean.setTos("1");
                pBean.setBaa("1");
                SingleInstance.myAccountBean = pBean;
                WebServiceReferences.webServiceClient.SetMyAccount(pBean, MyAccountActivity.this);
                showDialog();

//                } else {
//                    Toast.makeText(mainContext,
//                            "Please fill all the fields properly",
//                            Toast.LENGTH_SHORT).show();
//                }
            }
        });
        loadFields();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            File new_file = new File(strIPath);
            if(new_file.exists()) {
                ImageLoader imageLoader;
                imageLoader = new ImageLoader(MyAccountActivity.this);
                imageLoader.DisplayImage(strIPath, profile_pic, R.drawable.userphoto);
                String[] param=new String[7];
                param[0]= CallDispatcher.LoginUser;
                param[1]= CallDispatcher.Password;
                param[2]="image";
                File file=new File(strIPath);
                param[3]=file.getName();
                long length = (int) file.length();
                length = length/1024;
                param[5]="other";
                param[6]= String.valueOf(length);
                if(file.exists()) {
                    param[4] = encodeTobase64(BitmapFactory.decodeFile(file.getPath()));
                    WebServiceReferences.webServiceClient.FileUpload(param,MyAccountActivity.this);
                    FileDetailsBean fBean=new FileDetailsBean();
                    fBean.setFilename(param[3]);
                    fBean.setFiletype("image");
                    fBean.setFilecontent(param[4]);
                    fBean.setServicetype("Upload");
                    SingleInstance.fileDetailsBean=fBean;
                }
            }
        }
    }
    public String getFileName() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strFilename;
    }
    private String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }
    private void showDialog() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    progressDialog = new ProgressDialog(MyAccountActivity.this);
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
    public void getResponseFromServer(Servicebean servicebean)
    {
        cancelDialog();
        final WebServiceBean sbean = (WebServiceBean) servicebean.getObj();
        handler.post(new Runnable() {

            @Override
            public void run() {
                String result = sbean.getText();
                Log.i("AAAA", "MYACCOUNT " + result);
                if(result.equalsIgnoreCase("User details updated")) {
                    Log.i("AAAA", "MYACCOUNT");
                    Toast.makeText(MyAccountActivity.this,
                            result, Toast.LENGTH_SHORT).show();
                    MyAccountFragment.newInstance(SingleInstance.mainContext).loadFields();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            SingleInstance.mainContext.notifyUI();
                        }
                    });

                }else
                    Toast.makeText(MyAccountActivity.this,
                            result, Toast.LENGTH_SHORT).show();


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
    private void loadFields()
    {
        try {
            ProfileBean bean= SingleInstance.myAccountBean;
            Log.i("AAAA", "MYACCOUNT nickname " + bean.getNickname());
            if(bean.getNickname()!=null)
                edNickname.setText(bean.getNickname());
            if(bean.getFirstname()!=null)
                edFname.setText(bean.getFirstname());
            if(bean.getLastname()!=null)
                edLname.setText(bean.getLastname());
            if(bean.getOfficeaddress()!=null)
                tv_addr.setText(bean.getOfficeaddress());
            if(bean.getPhoto()!=null){
                String profilePic=bean.getPhoto();
                Log.i("AAAA", "MYACCOUNT "+profilePic);
                if (profilePic != null && profilePic.length() > 0) {
                    if (!profilePic.contains("COMMedia")) {
                        profilePic = Environment
                                .getExternalStorageDirectory()
                                + "/COMMedia/" + profilePic;
                        strIPath = profilePic;
                    }
                    Log.i("AAAA","MYACCOUNT "+profilePic);
                    imageLoader.DisplayImage(profilePic, profile_pic,
                            R.drawable.icon_buddy_aoffline);
                }
            }
            ArrayAdapter<String> dataAdapter;
            List<String> list;
            if(bean.getTitle()!=null){
                title.setText(bean.getTitle());
            }
            if(bean.getSex()!=null) {
                if (bean.getSex().equalsIgnoreCase("male")) {
                    RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);
                    radioFemale.setChecked(false);
                    RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
                    radioMale.setChecked(true);
                } else if (bean.getSex().equalsIgnoreCase("female")) {
                    RadioButton radioFemale = (RadioButton)findViewById(R.id.radioFemale);
                    radioFemale.setChecked(true);
                    RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
                    radioMale.setChecked(false);
                }
            }
            if(bean.getUsertype()!=null){
                usertype.setText(bean.getUsertype());
            }
            if(bean.getState()!= null){
                state.setText(bean.getState());
            }
            if(bean.getProfession()!= null){
                rlay_professional_org.setText(bean.getProfession());
            }
            if(bean.getSpeciality()!= null){
                Speciality.setText(bean.getSpeciality());
            }
            if(bean.getMedicalschool()!= null){
                medical_schools.setText(bean.getMedicalschool());
            }
            if(bean.getResidencyprogram()!= null){
                residency_pgm.setText(bean.getResidencyprogram());
            }
            if(bean.getFellowshipprogram()!= null){
                fellowship_pgm.setText(bean.getFellowshipprogram());
            }
            if(bean.getHospitalaffiliation()!= null){
                hospital.setText(bean.getHospitalaffiliation());
            }
            if(bean.getCitationpublications()!= null){
//                tv_cite.setText(bean.getCitationpublications());
                add_citation=bean.getCitationpublications();
                String[] split = add_citation.split(",");
                cite_lay.removeAllViews();
                cite_lay1.removeAllViews();
                for (int i = 0; i < split.length; i++) {
                    LinearLayout llay = new LinearLayout(context);
                    LinearLayout.LayoutParams dut = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dut.leftMargin = 20;
                    dut.rightMargin = 20;
                    llay.setLayoutParams(dut);
                    llay.setWeightSum(1);

                    TextView dynamicText = new TextView(context);
                    LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dim.leftMargin = 30;
                    dim.topMargin = 5;
                    dim.weight = 1;
                    dynamicText.setLayoutParams(dim);
                    Button button = new Button(context);
                    LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    button.setGravity(Gravity.END);

                    but.rightMargin = 15;

                    button.setLayoutParams(but);
                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_delete));


                    if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                        dynamicText.setText(split[i]);
                        llay.addView(dynamicText);
                        llay.addView(button);
                        cite_lay1.addView(llay);
                    }
                }
            }
            if(bean.getOrganizationmembership()!= null){
                association_membership.setText(bean.getOrganizationmembership());
            }
        } catch (Exception e){
            e.printStackTrace();
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
}
