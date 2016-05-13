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
    AutoCompleteTextView state,hospital;
    RadioButton genderSelected;
    RadioGroup gender;
    ImageView profile_pic, edit_pic,status_icon;
    String strIPath;
     TextView nickname, fname,lname, offc ,edOffc,citations,statusTxt ,edcitations;
     EditText edNickname , edFname , edLname ,tv_cite ,tv_addr;
    private ImageLoader imageLoader;
    ArrayAdapter<String> stateAdapter;

    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    AutoCompleteTextView rlay_professional_org ,Speciality ,residency_pgm, fellowship_pgm;
    AutoCompleteTextView medical_schools ;
    AutoCompleteTextView association_membership;
    ArrayAdapter<String> hospitalDetailsAdapter;
    private String add_citation = "";
    ArrayAdapter<String> medicalDetailsAdapter;
    ArrayList<String> stateList = new ArrayList<String>();
    ArrayList<String> specialityList = new ArrayList<String>();
    ArrayList<String> medicalschoolsList = new ArrayList<String>();
    ArrayList<String> hospitalList = new ArrayList<String>();
    ArrayList<String> medicalSocietyList = new ArrayList<String>();
    protected void onCreate(Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.my_account_1);
        context = this;
        String status=getIntent().getStringExtra("status");
        imageLoader = new ImageLoader(context);
          nickname=(TextView)findViewById(R.id.tv_nickname);
        statusTxt=(TextView)findViewById(R.id.statusTxt1);
          fname=(TextView)findViewById(R.id.tv_fname);
          lname=(TextView)findViewById(R.id.tv_lname);
          offc=(TextView)findViewById(R.id.tv_offc);
          citations=(TextView)findViewById(R.id.tv_citations);
          tv_cite=(EditText)findViewById(R.id.tv_cite);
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
        final LinearLayout cite_lay = (LinearLayout) findViewById(R.id.cite_lay);
        final LinearLayout addr_lay = (LinearLayout) findViewById(R.id.addr_lay);
        final LinearLayout advance_lay=(LinearLayout)findViewById(R.id.optional_lay);
        final Button arrow=(Button)findViewById(R.id.arrow);
        final Button cite_plus=(Button)findViewById(R.id.cite_plus);
        final Button addr_plus=(Button)findViewById(R.id.addr_plus);
        Button btnRegisterOk = (Button) findViewById(R.id.btnRegisterOk);
        final TextView tv_hospital=(TextView)findViewById(R.id.tv_hospital);
        final TextView tv_state=(TextView)findViewById(R.id.tv_state);
        final TextView tv_medical=(TextView)findViewById(R.id.tv_medical);
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
                    dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
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
                            add_citation = citation_info.getText().toString().trim();
                            tv_cite.setText(add_citation);
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
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.office_address);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    Window window = dialog.getWindow();
                    dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
                    window.setAttributes(lp);
                    dialog.show();

                    Button save1 = (Button)dialog.findViewById(R.id.save_button2);
                    Button cancel1 = (Button)dialog.findViewById(R.id.cancel_button2);

                    cancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    save1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

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
        optional.setOnClickListener(new View.OnClickListener() {
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

        final String[] param=new String[28];
        param[0]="";param[2]="";param[3]="";param[4]="";param[5]="";param[6]="";param[7]="";param[8]="";
        param[9]="";param[10]="";param[11]="";param[12]="";param[13]="";param[14]="";param[15]="";param[16]="";
        param[17]="";param[18]="";param[19]="";param[20]="";param[21]="";param[22]="";param[23]="";param[24]="";
        param[25]="";param[26]="";param[27]="";
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
                pBean.setCitationpublications(tv_cite.getText().toString());
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
                tv_cite.setText(bean.getCitationpublications());
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
