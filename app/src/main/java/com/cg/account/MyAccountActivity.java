package com.cg.account;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.AdapterView;
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

import com.Fingerprint.MainActivity;
import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.MyAccountFragment;
import com.main.Registration;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;

import org.lib.model.FileDetailsBean;
import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import bsh.StringUtil;


public class MyAccountActivity extends Activity {
    private Context context;
    Boolean isClicked=true;
    AutoCompleteTextView title, usertype;
    AutoCompleteTextView state,state1;
    RadioButton genderSelected;
    RadioGroup gender;
    ImageView profile_pic, edit_pic,status_icon;
    Button img_cite;
    String strIPath, add_hospital;
    TextView nickname, fname,lname, offc ,edOffc,citations,statusTxt ,edcitations;
    EditText edNickname , edFname , edLname ,tv_cite ,tv_addr;
    private ImageLoader imageLoader;
    ArrayAdapter<String> stateAdapter;
    private LinearLayout cite_lay, cite_lay1,member_lay1,member_lay, hospital_lay1,addr_lay;
    private boolean isEdit=false;
    private boolean isTitle=false,isUsertype=false,isProff=false,isState=false;
    private boolean isSpeciality=false,isMedical=false,isResidency=false;
    private boolean isFellow=false,isHospital=false,isAssociation=false;
    boolean isOffcState=false,isCity=false;
    String[] address = new String[7];

    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    AutoCompleteTextView rlay_professional_org ,Speciality ,residency_pgm, fellowship_pgm;
    AutoCompleteTextView medical_schools ;
    Spinner association_membership,hospital;
    ArrayAdapter<String> hospitalDetailsAdapter;
    private String add_citation="",organisation = "",add_officeaddress = "",add_association_mem = "", Addressline1,Addressline2, office_phone_no, office_fax, zip_code, city_value,office_address, state_value = "";
    ArrayAdapter<String> medicalDetailsAdapter;
    ArrayList<String> stateList = new ArrayList<String>();
    ArrayList<String> specialityList = new ArrayList<String>();

    ArrayList<String> medicalschoolsList = new ArrayList<String>();
    ArrayList<String> hospitalList = new ArrayList<String>();
    ArrayList<String> medicalSocietyList = new ArrayList<String>();
    ArrayList<String> states=new ArrayList<String>();
    ArrayList<String> cityList=new ArrayList<String>();
    private String pastingContentCopy, spliting_address;
    Button button, members_button,addr_plus;
    boolean iszipchar=true;
    boolean isfaxNochar=true,isfaxNochar2=true;
    boolean isphNochar=true,isphNochar2=true;
    private String Ac_name_Changed;

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
//        tv_addr=(EditText)findViewById(R.id.tv_addr);


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
        member_lay1 = (LinearLayout) findViewById(R.id.associatio_lay1);
        hospital_lay1 =(LinearLayout)findViewById(R.id.hospital_lay1);

//        img_cite = (Button)findViewById(R.id.img_cite);
        addr_lay = (LinearLayout) findViewById(R.id.addr_lay);
        final LinearLayout advance_lay=(LinearLayout)findViewById(R.id.optional_lay);
        final Button arrow=(Button)findViewById(R.id.arrow);
        final Button cite_plus=(Button)findViewById(R.id.cite_plus);
        addr_plus=(Button)findViewById(R.id.addr_plus);
        Button btnRegisterOk = (Button) findViewById(R.id.btnRegisterOk);
        final TextView tv_hospital=(TextView)findViewById(R.id.tv_hospital);
        final TextView tv_state=(TextView)findViewById(R.id.tv_state);
        final TextView tv_medical=(TextView)findViewById(R.id.tv_medical);
        final TextView tv_residency=(TextView)findViewById(R.id.tv_residency);
        final TextView tv_fellow=(TextView)findViewById(R.id.tv_fellow);
        final TextView tv_usertype=(TextView)findViewById(R.id.usertype);
        final TextView profDesc=(TextView)findViewById(R.id.profDesc);
        final TextView tv_speciality=(TextView)findViewById(R.id.tv_speciality);
        final TextView tv_association=(TextView)findViewById(R.id.tv_association);
        final ImageView titel_img=(ImageView)findViewById(R.id.title_img);
        final ImageView usertype_img=(ImageView)findViewById(R.id.usertype_img);
        final ImageView prof_img=(ImageView)findViewById(R.id.prof_img);
        final ImageView state_img=(ImageView)findViewById(R.id.state_img);
        final ImageView speciality_img=(ImageView)findViewById(R.id.speciality_img);
        final ImageView medical_img=(ImageView)findViewById(R.id.medical_img);
        final ImageView residency_img=(ImageView)findViewById(R.id.residency_img);
        final ImageView fellow_img=(ImageView)findViewById(R.id.fellow_img);
//        final ImageView hospital_img=(ImageView)findViewById(R.id.hospital_img);
//        final ImageView association_img=(ImageView)findViewById(R.id.association_img);
//        Button img_edit=(Button)findViewById(R.id.addr_edit);
//       Button img_close=(Button)findViewById(R.id.img_close);
        if(status!=null) {
            statusTxt.setText(status);
        }
        if(status!=null) {
            if (status.equalsIgnoreCase("online"))
                status_icon.setBackgroundResource(R.drawable.online_icon);
            else if (status.equalsIgnoreCase("busy"))
                status_icon.setBackgroundResource(R.drawable.busy_icon);
            else if (status.equalsIgnoreCase("invisible"))
                status_icon.setBackgroundResource(R.drawable.invisibleicon);
            else if (status.equalsIgnoreCase("offline"))
                status_icon.setBackgroundResource(R.drawable.offline_icon);
        }


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
                    dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
                    window.setAttributes(lp);
                    dialog.show();

                    final EditText citation_info = (EditText) dialog.findViewById(R.id.Edit_text1);


                    Button save = (Button) dialog.findViewById(R.id.save_button1);
                    Button cancel = (Button) dialog.findViewById(R.id.cancel_button1);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            dialog.dismiss();
                        }
                    });
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            if (add_citation.length()<=0) {
                                add_citation=citation_info.getText().toString().trim();
                            } else
                                add_citation = add_citation + "###" + citation_info.getText().toString().trim();
                            String[] split = add_citation.split("###");
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
                                dim.bottomMargin = 7;
                                dynamicText.setLayoutParams(dim);
                                button = new Button(context);
                                LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                button.setGravity(Gravity.END);
                                but.rightMargin = 15;
                                button.setLayoutParams(but);
                                button.setTag(split[i]);
                                button.setOnClickListener(onclicklistener);
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
                    dialog1.getWindow().setBackgroundDrawableResource(R.color.black2);
                    window.setAttributes(lp);
                    dialog1.show();

                    final TextView Addresstext = (TextView) dialog1.findViewById(R.id.address_text1);
                    final TextView addresstext = (TextView) dialog1.findViewById(R.id.address_text2);
                    final TextView ziptext = (TextView) dialog1.findViewById(R.id.address_text3);
                    final TextView citytext = (TextView) dialog1.findViewById(R.id.address_text4);
                    final TextView office_phone_numbertext = (TextView) dialog1.findViewById(R.id.address_text5);
                    final TextView office_fax_numbertext = (TextView) dialog1.findViewById(R.id.address_text6);


                    final EditText Address1 = (EditText) dialog1.findViewById(R.id.address_edit1);
                    final EditText address2 = (EditText) dialog1.findViewById(R.id.address_edit2);
                    final EditText zip = (EditText) dialog1.findViewById(R.id.address_edit3);
                    final AutoCompleteTextView city = (AutoCompleteTextView) dialog1.findViewById(R.id.address_edit4);
                    final EditText office_phone_number = (EditText) dialog1.findViewById(R.id.address_edit5);
                    final EditText office_fax_number = (EditText) dialog1.findViewById(R.id.address_edit6);
                    state1 = (AutoCompleteTextView) dialog1.findViewById(R.id.state_of_practice);
                    final ImageView offcstate_img=(ImageView)dialog1.findViewById(R.id.offcstate_img);
                    final ImageView city_img=(ImageView)dialog1.findViewById(R.id.city_img);

                    Button save1 = (Button) dialog1.findViewById(R.id.save_button2);
                    Button cancel1 = (Button) dialog1.findViewById(R.id.cancel_button2);


                    if(isEdit){

                        if(spliting_address!=null) {
                            Addresstext.setVisibility(View.VISIBLE);
                            addresstext.setVisibility(View.VISIBLE);
                            ziptext.setVisibility(View.VISIBLE);
                            citytext.setVisibility(View.VISIBLE);
                            office_phone_numbertext.setVisibility(View.VISIBLE);
                            office_fax_numbertext.setVisibility(View.VISIBLE);

                            address = spliting_address.split(",");
                            Log.d("Address", "spliting_address"+spliting_address);
                            Address1.setText(address[0]);
                            address2.setText(address[1]);
                            zip.setText(address[2]);
                            city.setText(address[3]);
                            state1.setText(address[4]);
                            office_phone_number.setText(address[5]);
                            office_fax_number.setText(address[6]);
                        }
                    }
                    office_phone_number.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() > 0) {
                                office_phone_numbertext.setVisibility(View.VISIBLE);
                            } else {
                                office_phone_numbertext.setVisibility(View.GONE);
                            }
                            if (charSequence.length()==3&& isNumeric(charSequence.toString()) && isphNochar){
                                office_phone_number.setText(charSequence+"-");
                                office_phone_number.setSelection(4);
                            }else if(charSequence.length()>3 )
                                isphNochar=false;
                            if(charSequence.length()>7)
                                isphNochar2=false;
                            else if(charSequence.length()<3 )
                                isphNochar=true;
                            else if(charSequence.length()<7)
                                isphNochar2=true;
                            int counter = charSequence.toString().split("-", -1).length - 1;
                            if(charSequence.length()==7&&counter==1 && isphNochar2 ){
                                office_phone_number.setText(charSequence+"-");
                                office_phone_number.setSelection(8);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });

                    office_fax_number.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() > 0) {
                                office_fax_numbertext.setVisibility(View.VISIBLE);
                            } else {
                                office_fax_numbertext.setVisibility(View.GONE);
                            }
                            if (charSequence.length()==3&& isNumeric(charSequence.toString())&& isfaxNochar){
                                office_fax_number.setText(charSequence+"-");
                                office_fax_number.setSelection(4);
                            }else if(charSequence.length()>3 )
                                isfaxNochar=false;
                             if(charSequence.length()>7)
                                isfaxNochar2=false;
                             else if(charSequence.length()<3 )
                                isfaxNochar=true;
                            else if(charSequence.length()<7)
                                isfaxNochar2=true;
                            int counter1 = charSequence.toString().split("-", -1).length - 1;
                            if(charSequence.length()==7&&counter1==1 && isfaxNochar2 ){
                                office_fax_number.setText(charSequence+"-");
                                office_fax_number.setSelection(8);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });

                    Address1.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() > 0) {
                                Addresstext.setVisibility(View.VISIBLE);
                            } else {
                                Addresstext.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                    address2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() > 0) {
                                addresstext.setVisibility(View.VISIBLE);
                            } else {
                                addresstext.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });

                    zip.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() > 0) {
                                ziptext.setVisibility(View.VISIBLE);
                            } else {
                                ziptext.setVisibility(View.GONE);
                            }
                            if (charSequence.length()==5&& isNumeric(charSequence.toString()) && iszipchar){
                                zip.setText(charSequence+"-");
                                zip.setSelection(6);
                            }else if(charSequence.length()>5)
                                iszipchar=false;
                            else if(charSequence.length()<5)
                                iszipchar=true;
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                    city.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() > 0) {
                                citytext.setVisibility(View.VISIBLE);
                            } else {
                                citytext.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });


                    cancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            dialog1.dismiss();
                            isEdit = false;
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
                            office_fax = office_fax_number.getText().toString();
                            if (Address1.getText().toString().length() > 0 && address2.getText().toString().length() > 0 && zip.getText().toString().length() > 0
                                    && city.getText().toString().length() > 0 && state1.getText().toString().length() > 0
                                    && office_phone_number.getText().toString().length() > 0 && office_fax_number.getText().toString().length() > 0) {

                                String office_address = "";
                                if (Addressline1 != null && !Addressline1.trim().equals("")) {
                                    office_address = Addressline1;
                                }
                                if (Addressline2 != null && !Addressline2.trim().equals("")) {
                                    office_address = office_address + ", " + Addressline2;
                                }
                                if (zip_code != null && !zip_code.trim().equals("")) {
                                    office_address = office_address + ", " + zip_code;
                                }
                                if (city_value != null && !city_value.trim().equals("")) {
                                    office_address = office_address + ", " + city_value;
                                }
                                if (state_value != null && !state_value.trim().equals("")) {
                                    office_address = office_address + ", " + state_value;
                                }
                                if (office_phone_no != null && !office_phone_no.trim().equals("")) {
                                    office_address = office_address + ", " + office_phone_no;
                                }
                                if (office_fax != null && !office_fax.trim().equals("")) {
                                    office_address = office_address + ", " + office_fax;
                                }

                                String full_officeaddress = office_address;
                                if (isEdit) {
                                    isEdit = false;
                                    Log.d("officeaddress", "vale1" + office_address);
                                    Log.d("officeaddress", "vale2" + spliting_address);
                                    add_officeaddress = add_officeaddress.replace(spliting_address, "");
                                    add_officeaddress += "###" + office_address;
                                } else if (add_officeaddress.length() < 0) {
                                    add_officeaddress = full_officeaddress;
                                } else
                                    add_officeaddress = add_officeaddress + "###" + full_officeaddress;
                                String[] split = add_officeaddress.split("###");
                                addr_lay.removeAllViews();
                                for (int i = 0; i < split.length; i++) {
                                    LinearLayout address_layout = new LinearLayout(context);
                                    LinearLayout.LayoutParams dut = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    dut.leftMargin = 20;
                                    dut.rightMargin = 20;

                                    address_layout.setLayoutParams(dut);
                                    address_layout.setWeightSum(1);

                                    TextView dynamicText = new TextView(context);
                                    LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    dim.leftMargin = 30;
                                    dim.topMargin = 5;
                                    dim.bottomMargin = 7;
                                    dim.weight = 1;
                                    dynamicText.setLayoutParams(dim);

                                    Button button1 = new Button(context);
                                    LinearLayout.LayoutParams buton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    button1.setGravity(Gravity.END);
                                    buton.rightMargin = 15;
                                    button1.setLayoutParams(buton);
                                    button1.setTag(split[i]);
                                    button1.setOnClickListener(onclicklistener3);
                                    button1.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_edit));

                                    Button button = new Button(context);
                                    LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    button.setGravity(Gravity.END);
                                    but.rightMargin = 15;
                                    button.setLayoutParams(but);
                                    button.setTag(split[i]);
                                    button.setOnClickListener(onclicklistener4);
                                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_delete));

                                    if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                                        dynamicText.setText(split[i]);
                                        address_layout.addView(dynamicText);
                                        address_layout.addView(button1);
                                        address_layout.addView(button);
                                        addr_lay.addView(address_layout);
                                    }

                                }
                                dialog1.dismiss();
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            } else
                                showToast("Please enter mandatory fields");

                        }
                    });
                    cityList.addAll(SingleInstance.mainContext.cityList);
                    TreeSet treeSet_citylist=new TreeSet(cityList);
                    ArrayList arrayList_citylist=new ArrayList(treeSet_citylist);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list,arrayList_citylist);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

                    city.setAdapter(dataAdapter);
                    city.setThreshold(30);
                    city_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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
                    city.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            city_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));

                        }
                    });
                    states = DBAccess.getdbHeler().getStateDetails();
                    TreeSet treeset_states=new TreeSet(states);
                    ArrayList arraylist_states=new ArrayList(treeset_states);
                    stateAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, arraylist_states);
                    state1.setAdapter(stateAdapter);
                    state1.setThreshold(30);
                    offcstate_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            if(isOffcState) {
                                isOffcState=false;
                                state1.dismissDropDown();
                                offcstate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                            }else {
                                isOffcState=true;
                                offcstate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                                state1.showDropDown();
                            }
                        }
                    });
                    state1.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        offcstate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        }
                    });

                    addr_lay.setVisibility(View.VISIBLE);
//                    tv_addr.setText(edOffc.getText().toString());
                    offc.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (isClicked) {
                    isClicked = false;
                    advance_lay.setVisibility(View.VISIBLE);
                    arrow.setBackgroundResource(R.drawable.button_arrow_up);
                } else {
                    isClicked = true;
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
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                strIPath = Environment.getExternalStorageDirectory()
                        + "/COMMedia/MPD_" + getFileName()
                        + ".jpg";
                Intent intent = new Intent(MyAccountActivity.this, CustomVideoCamera.class);
                intent.putExtra("filePath", strIPath);
                intent.putExtra("isPhoto", true);
                startActivityForResult(intent, 1);
            }
        });
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_addr.setText("");
//            }
//        });
//        img_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (tv_addr.getText().length() > 0) {
//                    isEdit = true;
//                    addr_plus.performClick();
//                }
//            }
//        });
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
        hospital = (Spinner) findViewById(R.id.rlay_hospital_affiliation);

        rlay_professional_org = (AutoCompleteTextView) findViewById(R.id.prof_desgn);
        Speciality = (AutoCompleteTextView) findViewById(R.id.speciality);
        medical_schools = (AutoCompleteTextView) findViewById(R.id.medical_scl);
        residency_pgm = (AutoCompleteTextView) findViewById(R.id.resi_pro);
        fellowship_pgm = (AutoCompleteTextView)findViewById(R.id.fellow);
        association_membership = (Spinner)findViewById(R.id.association_membership);


        stateList= DBAccess.getdbHeler().getStateDetails();
        TreeSet treeset_statelist=new TreeSet(stateList);
        ArrayList arraylist_statelist=new ArrayList(treeset_statelist);
        stateAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, arraylist_statelist);
        state.setAdapter(stateAdapter);
        state.setThreshold(30);
        state_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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

        hospitalList.add("Add Hospital Affiliation");
        hospitalList.addAll(DBAccess.getdbHeler().getHospitalDetails());
        TreeSet treeset_hospitallist=new TreeSet(hospitalList);
        ArrayList arraylist_hospitallist=new ArrayList(treeset_hospitallist);
        hospitalDetailsAdapter = new ArrayAdapter<String>(context, R.layout.spinner_lay, arraylist_hospitallist);
        hospitalDetailsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        hospital.setAdapter(hospitalDetailsAdapter);

        hospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (hospital != null&& !hospital.getSelectedItem().toString().equalsIgnoreCase("Add Hospital Affiliation")) {
                    if (add_hospital.length()<=0 ) {
                        add_hospital=hospital.getSelectedItem().toString().trim();
                    } else
                        add_hospital = add_hospital + "###" + hospital.getSelectedItem().toString().trim();

                    String[] split = add_hospital.split("###");
                    hospital_lay1.removeAllViews();
                    for (int i = 0; i < split.length; i++) {

                        LinearLayout hospital_layout = new LinearLayout(context);
                        LinearLayout.LayoutParams dut = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        dut.leftMargin = 20;
                        dut.rightMargin = 20;
                        hospital_layout.setLayoutParams(dut);
                        hospital_layout.setWeightSum(1);

                        TextView dynamicText = new TextView(context);
                        LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        dim.leftMargin = 30;
                        dim.topMargin = 5;
                        dim.weight = 1;
                        dynamicText.setLayoutParams(dim);

                        button = new Button(context);
                        LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        button.setGravity(Gravity.END);
                        but.rightMargin = 15;
                        button.setLayoutParams(but);
                        button.setTag(split[i]);
                        button.setOnClickListener(onclicklistener2);
                        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_delete));

                        if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                            dynamicText.setText(split[i]);
                            hospital_layout.addView(dynamicText);
                            hospital_layout.addView(button);
                            hospital_lay1.addView(hospital_layout);
                        }

                    }
                }
            }
        @Override
              public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//        hospital.setThreshold(30);
//        hospital_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                if (isHospital) {
//                    isHospital = false;
//                    hospital.dismissDropDown();
//                    hospital_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
//                } else {
//                    isHospital = true;
//                    hospital_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
//                    hospital.showDropDown();
//                }
//            }
//        });

        medicalSocietyList.add("Choose Association");
        medicalSocietyList.addAll(DBAccess.getdbHeler().getMedicalSocietiesDetails());
        TreeSet treeset_medicalsocietylist=new TreeSet(medicalSocietyList);
        ArrayList arraylist_medicalsocietylist=new ArrayList(treeset_medicalsocietylist);
        medicalDetailsAdapter = new ArrayAdapter<String>(context, R.layout.spinner_lay, arraylist_medicalsocietylist);
        medicalDetailsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        association_membership.setAdapter(medicalDetailsAdapter);
        association_membership.setPrompt("Choose Association");
        association_membership.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (association_membership != null && !association_membership.getSelectedItem().toString().equalsIgnoreCase("Choose Association")) {
                    if (add_association_mem.length()<0) {
                        add_association_mem=association_membership.getSelectedItem().toString().trim();
                    } else
                        add_association_mem = add_association_mem + "###" + association_membership.getSelectedItem().toString().trim();

                    String[] split = add_association_mem.split("###");
                    member_lay1.removeAllViews();
                    for (int i = 0; i < split.length; i++) {

                        LinearLayout hospital_layout = new LinearLayout(context);
                        LinearLayout.LayoutParams dut = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        dut.leftMargin = 20;
                        dut.rightMargin = 20;
                        hospital_layout.setLayoutParams(dut);
                        hospital_layout.setWeightSum(1);

                        TextView dynamicText = new TextView(context);
                        LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        dim.leftMargin = 30;
                        dim.topMargin = 5;
                        dim.weight = 1;
                        dynamicText.setLayoutParams(dim);

                        button = new Button(context);
                        LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        button.setGravity(Gravity.END);
                        but.rightMargin = 15;
                        button.setLayoutParams(but);
                        button.setTag(split[i]);
                        button.setOnClickListener(onclicklistener1);
                        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_delete));

                        if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                            dynamicText.setText(split[i]);
                            hospital_layout.addView(dynamicText);
                            hospital_layout.addView(button);
                            member_lay1.addView(hospital_layout);
                        }

                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        association_img.setOnClickListener(new View.OnClickListener() {
//                @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                if(isAssociation) {
//                    isAssociation=false;
//                    association_membership.dismissDropDown();
//                    association_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
//                }else {
//                    isAssociation=true;
//                    association_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
//                    association_membership.showDropDown();
//                }
//            }
//        });

                medical_schools.addTextChangedListener(new TextWatcher() {
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
                    }
                });
                residency_pgm.addTextChangedListener(new TextWatcher() {
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
                    }
                });
                fellowship_pgm.addTextChangedListener(new TextWatcher() {
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
                    }
                });
                Speciality.addTextChangedListener(new TextWatcher() {
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
                    }
                });
                rlay_professional_org.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            profDesc.setVisibility(View.VISIBLE);
                        } else {
                            profDesc.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        prof_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        if(rlay_professional_org.getText().toString().equalsIgnoreCase("Other")){
                            rlay_professional_org.setText("");
                        }
                    }
                });


                List<String> list = new ArrayList<String>();
                list.add("Mr.");
                list.add("Ms.");
                list.add("Miss.");
                list.add("Mrs.");
                list.add("Dr.");
                list.add("Prof.");
        TreeSet treeset_title=new TreeSet(list);
        ArrayList arraylist_title=new ArrayList(treeset_title);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (this, R.layout.spinner_dropdown_list, arraylist_title);

                dataAdapter.setDropDownViewResource
                        (R.layout.spinner_dropdown_list);

                title.setAdapter(dataAdapter);
                title.setThreshold(10);
                titel_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isTitle) {
                            isTitle = false;
                            title.dismissDropDown();
                            titel_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isTitle = true;
                            titel_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            title.showDropDown();
                        }
                    }
                });
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
titel_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
            }
        });

                list = new ArrayList<String>();
                list.add("Student");
                list.add("Resident");
                list.add("Fellow");
                list.add("Attending");
                list.add("Other");
        TreeSet treeset_usertype=new TreeSet(list);
        ArrayList arraylist_usertype=new ArrayList(treeset_usertype);
                dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, arraylist_usertype);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

                usertype.setAdapter(dataAdapter);
                usertype.setThreshold(10);
                usertype_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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
        usertype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
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

                        } else
                            tv_state.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        state_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                    }
                });
//        association_membership.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() > 0) {
//                    tv_association.setVisibility(View.VISIBLE);
//                } else {
//                    tv_association.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });


                medicalschoolsList = DBAccess.getdbHeler().getMedicalSchoolDetails();
        TreeSet settree2=new TreeSet(medicalschoolsList);
        ArrayList arraylist3=new ArrayList(settree2);
                dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list, arraylist3);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
                medical_schools.setAdapter(dataAdapter);
                medical_schools.setThreshold(30);
                medical_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isMedical) {
                            isMedical = false;
                            medical_schools.dismissDropDown();
                            medical_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isMedical = true;
                            medical_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            medical_schools.showDropDown();
                        }
                    }
                });
        medical_schools.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
       medical_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
            }
        });

                list = new ArrayList<String>();
                Registration reg=new Registration();
                list.addAll(reg.loadResidencyFiles());
        TreeSet treeset_residency=new TreeSet(list);
        ArrayList arraylist_residency=new ArrayList(treeset_residency);
                dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list,arraylist_residency);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
                residency_pgm.setAdapter(dataAdapter);
                residency_pgm.setThreshold(30);
                residency_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isResidency) {
                            isResidency = false;
                            residency_pgm.dismissDropDown();
                            residency_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isResidency = true;
                            residency_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            residency_pgm.showDropDown();
                        }
                    }
                });

        residency_pgm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            residency_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
            }
        });
                list = new ArrayList<String>();
                list.addAll(reg.loadFellowship());
        TreeSet treeset_fellowship=new TreeSet(list);
        ArrayList arraylist_fellowship=new ArrayList(treeset_fellowship);
                dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list, arraylist_fellowship);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
                fellowship_pgm.setAdapter(dataAdapter);
                fellowship_pgm.setThreshold(30);
                fellow_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isFellow) {
                            isFellow = false;
                            fellowship_pgm.dismissDropDown();
                            fellow_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isFellow = true;
                            fellow_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            fellowship_pgm.showDropDown();
                        }
                    }
                });
        fellowship_pgm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
             fellow_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
            }
        });

                list = new ArrayList<String>();
                list.add("MD");
                list.add("DO");
                list.add("Other");
        TreeSet treeset_list=new TreeSet(list);
        ArrayList arrayList_treeset=new ArrayList(treeset_list);
                dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list, arrayList_treeset);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
                rlay_professional_org.setAdapter(dataAdapter);
                rlay_professional_org.setThreshold(30);
                prof_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isProff) {
                            isProff = false;
                            rlay_professional_org.dismissDropDown();
                            prof_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isProff = true;
                            prof_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            rlay_professional_org.showDropDown();
                        }
                    }
                });

                specialityList = DBAccess.getdbHeler().getSpecialityDetails();
        TreeSet treeset_specialitylist=new TreeSet(specialityList);
        ArrayList arraylist_specialitylist=new ArrayList(treeset_specialitylist);
                dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list, arraylist_specialitylist);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
                Speciality.setAdapter(dataAdapter);
                Speciality.setThreshold(30);
                speciality_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (isSpeciality) {
                            isSpeciality = false;
                            Speciality.dismissDropDown();
                            speciality_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                        } else {
                            isSpeciality = true;
                            speciality_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                            Speciality.showDropDown();
                        }
                    }
                });
        Speciality.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
speciality_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
            }
        });


//                hospital.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        if (charSequence.length() > 0) {
//                            tv_hospital.setVisibility(View.VISIBLE);
//
//                        } else
//                            tv_hospital.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//                    }
//                });
                btnRegisterOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edFname.getText().toString().trim().length() > 0
                                && edLname.getText().toString().trim().length() > 0) {
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
                            pBean.setOfficeaddress(add_officeaddress);
                            pBean.setHospitalaffiliation(add_hospital);
                            pBean.setCitationpublications(add_citation);
                            pBean.setOrganizationmembership(add_association_mem);
                            pBean.setTos("1");
                            pBean.setBaa("1");
                            SingleInstance.myAccountBean = pBean;
                            Ac_name_Changed= pBean.getUsername();
                            WebServiceReferences.webServiceClient.SetMyAccount(pBean, MyAccountActivity.this);

                            showDialog();

                        } else {
                            if (edFname.getText().toString().length() == 0)
                                Toast.makeText(context,
                                        "Please enter First name", Toast.LENGTH_SHORT).show();
                            else if (edLname.getText().toString().length() == 0)
                                Toast.makeText(context,
                                        "Please enter Last name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                loadFields();
            }

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                try {
                    if (requestCode == 1) {

                        File new_file = new File(strIPath);
                        if (new_file.exists()) {
                            ImageLoader imageLoader;
                            imageLoader = new ImageLoader(MyAccountActivity.this);
                            imageLoader.DisplayImage(strIPath, profile_pic, R.drawable.userphoto);
                            String[] param = new String[7];
                            param[0] = CallDispatcher.LoginUser;
                            param[1] = CallDispatcher.Password;
                            param[2] = "image";
                            File file = new File(strIPath);
                            param[3] = file.getName();
                            long length = (int) file.length();
                            length = length / 1024;
                            param[5] = "other";
                            param[6] = String.valueOf(length);
                            if (file.exists()) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;
                                 BitmapFactory.decodeStream((new FileInputStream(file.getPath())),null,options);
                                int imageHeight = options.outHeight/2;
                                int imageWidth = options.outWidth/2;
                                String imageType = options.outMimeType;
                                Log.i("MemoryCheck","imageHeight : "+imageHeight+" imageWidth : "+imageWidth);
                                options.inSampleSize = calculateInSampleSize(options, imageWidth/2, imageHeight/2);

                                options.inJustDecodeBounds = false;
                                Bitmap tb = BitmapFactory.decodeStream(new FileInputStream(file), null, options);

                                param[4] = encodeTobase64(tb);
                                showDialog();
                                WebServiceReferences.webServiceClient.FileUpload(param, MyAccountActivity.this,"");
                                FileDetailsBean fBean = new FileDetailsBean();
                                fBean.setFilename(param[3]);
                                fBean.setFiletype("image");
                                fBean.setFilecontent(param[4]);
                                fBean.setServicetype("Upload");
                                SingleInstance.fileDetailsBean = fBean;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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

            public void getResponseFromServer(Servicebean servicebean) {
                cancelDialog();
                final WebServiceBean sbean = (WebServiceBean) servicebean.getObj();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        String result = sbean.getText();
                        Log.i("AAAA", "MYACCOUNT " + result);
                        if (result.equalsIgnoreCase("User details updated")) {
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
                            finish();
                            WebServiceReferences.webServiceClient.GetAllProfile(
                                    CallDispatcher.LoginUser, Ac_name_Changed,SingleInstance.mainContext);

                        } else
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

            private void loadFields() {
                try {
                    ProfileBean bean = SingleInstance.myAccountBean;
                    Log.i("AAAA", "MYACCOUNT nickname " + bean.getNickname());
                    if (bean.getNickname() != null)
                        edNickname.setText(bean.getNickname());
                    if (bean.getFirstname() != null)
                        edFname.setText(bean.getFirstname());
                    if (bean.getLastname() != null)
                        edLname.setText(bean.getLastname());
                    if (bean.getOfficeaddress() != null) {
                        add_officeaddress = bean.getOfficeaddress();
                        String[] split = add_officeaddress.split("###");
                        addr_lay.removeAllViews();
                        for (int i = 0; i < split.length; i++) {
                            LinearLayout address_layout = new LinearLayout(context);
                            LinearLayout.LayoutParams dut = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            dut.leftMargin = 20;
                            dut.rightMargin = 20;
                            address_layout.setLayoutParams(dut);
                            address_layout.setWeightSum(1);

                            TextView dynamicText = new TextView(context);
                            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            dim.leftMargin = 30;
                            dim.topMargin = 5;
                            dim.bottomMargin = 7;
                            dim.weight = 1;
                            dynamicText.setLayoutParams(dim);

                            Button button1 = new Button(context);
                            LinearLayout.LayoutParams buton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            button1.setGravity(Gravity.END);
                            buton.rightMargin = 15;
                            button1.setLayoutParams(buton);
                            button1.setTag(split[i]);
                            button1.setOnClickListener(onclicklistener3);
                            button1.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_edit));

                            Button button = new Button(context);
                            LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            button.setGravity(Gravity.END);
                            but.rightMargin = 15;
                            button.setLayoutParams(but);
                            button.setTag(split[i]);
                            button.setOnClickListener(onclicklistener4);
                            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_delete));

                            if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                                dynamicText.setText(split[i]);
                                address_layout.addView(dynamicText);
                                address_layout.addView(button1);
                                address_layout.addView(button);
                                addr_lay.addView(address_layout);
                            }

                        }


//                        tv_addr.setText(bean.getOfficeaddress());
                    }
                    if (bean.getPhoto() != null) {
                        String profilePic = bean.getPhoto();
                        Log.i("AAAA", "MYACCOUNT " + profilePic);
                        if (profilePic != null && profilePic.length() > 0) {
                            if (!profilePic.contains("COMMedia")) {
                                profilePic = Environment
                                        .getExternalStorageDirectory()
                                        + "/COMMedia/" + profilePic;
                                strIPath = profilePic;
                            }
                            Log.i("AAAA", "MYACCOUNT " + profilePic);
                            imageLoader.DisplayImage(profilePic, profile_pic,
                                    R.drawable.icon_buddy_aoffline);
                        }
                    }
                    ArrayAdapter<String> dataAdapter;
                    List<String> list;
                    if (bean.getTitle() != null) {
                        title.setText(bean.getTitle());
                    }
                    if (bean.getSex() != null) {
                        if (bean.getSex().equalsIgnoreCase("male")) {
                            RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);
                            radioFemale.setChecked(false);
                            RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
                            radioMale.setChecked(true);
                        } else if (bean.getSex().equalsIgnoreCase("female")) {
                            RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);
                            radioFemale.setChecked(true);
                            RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
                            radioMale.setChecked(false);
                        }
                    }
                    if (bean.getUsertype() != null) {
                        usertype.setText(bean.getUsertype());
                    }
                    if (bean.getState() != null) {
                        state.setText(bean.getState());
                    }
                    if (bean.getProfession() != null) {
                        rlay_professional_org.setText(bean.getProfession());
                    }
                    if (bean.getSpeciality() != null) {
                        Speciality.setText(bean.getSpeciality());
                    }
                    if (bean.getMedicalschool() != null) {
                        medical_schools.setText(bean.getMedicalschool());
                    }
                    if (bean.getResidencyprogram() != null) {
                        residency_pgm.setText(bean.getResidencyprogram());
                    }
                    if (bean.getFellowshipprogram() != null) {
                        fellowship_pgm.setText(bean.getFellowshipprogram());
                    }
                    if (bean.getHospitalaffiliation() != null) {
                        add_hospital = bean.getHospitalaffiliation();
                        Log.d("values","hospital"+add_hospital);
                        String[] split = add_hospital.split("###");
                        hospital_lay1.removeAllViews();
                        for (int i = 0; i < split.length; i++) {
                            LinearLayout hospital_layout = new LinearLayout(context);
                            LinearLayout.LayoutParams dut = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            dut.leftMargin = 20;
                            dut.rightMargin = 20;
                            hospital_layout.setLayoutParams(dut);
                            hospital_layout.setWeightSum(1);

                            TextView dynamicText = new TextView(context);
                            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            dim.leftMargin = 30;
                            dim.topMargin = 5;
                            dim.weight = 1;
                            dynamicText.setLayoutParams(dim);

                            button = new Button(context);
                            LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            button.setGravity(Gravity.END);
                            but.rightMargin = 15;
                            button.setLayoutParams(but);
                            button.setTag(split[i]);
                            button.setOnClickListener(onclicklistener2);
                            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_delete));

                            if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                                dynamicText.setText(split[i]);
                                hospital_layout.addView(dynamicText);
                                hospital_layout.addView(button);
                                hospital_lay1.addView(hospital_layout);
                            }

                        }

//                        hospital.setSelection(hospitalDetailsAdapter.getPosition(bean.getHospitalaffiliation()));
//                        hospital.setText(bean.getHospitalaffiliation());
                    }
                    if (bean.getCitationpublications() != null) {
//                tv_cite.setText(bean.getCitationpublications());
                        add_citation = bean.getCitationpublications();
                        String[] split = add_citation.split("###");
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
                            dim.bottomMargin = 7;
                            dynamicText.setLayoutParams(dim);
                            button = new Button(context);
                            LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            button.setGravity(Gravity.END);
                            but.rightMargin = 15;
                            button.setLayoutParams(but);
                            button.setTag(split[i]);
                            button.setOnClickListener(onclicklistener);
                            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_delete));


                            if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                                dynamicText.setText(split[i]);
                                llay.addView(dynamicText);
                                llay.addView(button);
                                cite_lay1.addView(llay);
                            }
                        }
                    }
                    if (bean.getOrganizationmembership() != null) {
//                        add_association_mem = bean.getOrganizationmembership();
////                association_membership.setText(bean.getOrganizationmembership());
//
//                        if (association_membership != null) {
//                            Log.d("assocition", "string" + association_membership);
                            add_association_mem = bean.getOrganizationmembership();

                            Log.d("association", "string1" + add_association_mem);
                        String[] split = add_association_mem.split("###");
                        member_lay1.removeAllViews();
//                    member_lay.removeAllViews();
                        for (int i = 0; i < split.length; i++) {

                            LinearLayout hospital_layout = new LinearLayout(context);
                            LinearLayout.LayoutParams dut = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            dut.leftMargin = 20;
                            dut.rightMargin = 20;
                            hospital_layout.setLayoutParams(dut);
                            hospital_layout.setWeightSum(1);

                            TextView dynamicText = new TextView(context);
                            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            dim.leftMargin = 30;
                            dim.topMargin = 5;
                            dim.weight = 1;
                            dynamicText.setLayoutParams(dim);

                            button = new Button(context);
                            LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            button.setGravity(Gravity.END);
                            but.rightMargin = 15;
                            button.setLayoutParams(but);
                            button.setTag(split[i]);
                            button.setOnClickListener(onclicklistener1);
                            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_delete));

                            if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                                dynamicText.setText(split[i]);
                                hospital_layout.addView(dynamicText);
                                hospital_layout.addView(button);
                                member_lay1.addView(hospital_layout);
                            }

                        }
                        }
//                association_membership.setText(bean.getOrganizationmembership());

                } catch (Exception e) {
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

            View.OnClickListener onclicklistener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (add_citation.contains(v.getTag().toString()))
                        add_citation = add_citation.replace(v.getTag().toString(), "");
                    LinearLayout ll = (LinearLayout) v.getParent();
                    ll.removeAllViews();
                }
            };
            View.OnClickListener onclicklistener1 = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (add_association_mem.contains(v.getTag().toString()))
                        add_association_mem = add_association_mem.replace(v.getTag().toString(), "");
                    LinearLayout ll = (LinearLayout) v.getParent();
                    ll.removeAllViews();
                }
            };

    View.OnClickListener onclicklistener2 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (add_hospital.contains(v.getTag().toString()))
                add_hospital = add_hospital.replace(v.getTag().toString(), "");
            LinearLayout ll = (LinearLayout) v.getParent();
            ll.removeAllViews();
        }
    };

    View.OnClickListener onclicklistener3 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (add_officeaddress.contains(v.getTag().toString())) {
                spliting_address = v.getTag().toString();
                isEdit = true;
                addr_plus.performClick();
//                add_officeaddress=add_officeaddress.replace(spliting_address,v.getTag().toString() );
            }
        }
    };

    View.OnClickListener onclicklistener4 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (add_officeaddress.contains(v.getTag().toString()))
                add_officeaddress = add_officeaddress.replace(v.getTag().toString(), "");
            LinearLayout ll = (LinearLayout) v.getParent();
            ll.removeAllViews();
        }
    };

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AppMainActivity.inActivity = this;
        context = this;
        if(AppReference.mainContext.isPinEnable) {
            if (AppReference.mainContext.openPinActivity) {
                AppReference.mainContext.openPinActivity=false;
                if(Build.VERSION.SDK_INT>20 && AppReference.mainContext.isTouchIdEnabled) {
                    Intent i = new Intent(MyAccountActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(MyAccountActivity.this, PinSecurity.class);
                    startActivity(i);
                }
            } else {
                AppReference.mainContext.count=0;
                AppReference.mainContext.registerBroadcastReceiver();
            }
        }
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppReference.mainContext.isApplicationBroughtToBackground();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebServiceReferences.contextTable.remove("myaccountactivity");
    }
}
