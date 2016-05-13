package com.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.cg.DB.DBAccess;
import com.cg.account.TermsAndAgreement;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;

import org.lib.model.FileDetailsBean;
import org.lib.model.SubscribeBean;
import org.lib.model.WebServiceBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Registration extends Activity {

    private Context context;
    private SharedPreferences preferences;
    private CallDispatcher calldisp;
    ImageView riv;
    String strIPath;
    private Handler service_handler;
    private boolean isDialogVisible = false;
    Spinner  sp5, sp6, sp7;
    AutoCompleteTextView sp1, sp3, sp4,sp2;
    private String tos, baa;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    private String dob, ssn, houseno, zipcode;
    Boolean isClicked = true;
    RadioGroup gender;
    RadioButton genderSelected;
    ArrayList<String> states = new ArrayList<String>();
    ArrayList<String> specialityList = new ArrayList<String>();
    ArrayList<String> medicalschoolsList = new ArrayList<String>();
    ArrayList<String> hospitalList = new ArrayList<String>();
    ArrayList<String> medicalSocietyList = new ArrayList<String>();
    ArrayAdapter<String> stateAdapter;
    ArrayAdapter<String> hospitalDetailsAdapter;
    ArrayAdapter<String> medicalDetailsAdapter;
    AutoCompleteTextView association_membership;
    EditText etsecQues1, etsecQues2, etsecQues3;
    private List<String> list_secQue1;
    private List<String> list_secQue2;
    private List<String> list_secQue3;
    private ArrayAdapter<String> dataAdapter_secQue1;
    private ArrayAdapter<String> dataAdapter_secQue2;
    private ArrayAdapter<String> dataAdapter_secQue3;

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

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registration);

        context = this;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        calldisp = new CallDispatcher(context);

        service_handler = new Handler();

        WebServiceReferences.contextTable.put("registration", context);

        final TextView nickname = (TextView) findViewById(R.id.tv_nickname);
        final TextView email = (TextView) findViewById(R.id.tv_email);
        final TextView fname = (TextView) findViewById(R.id.tv_fname);
        final TextView lname = (TextView) findViewById(R.id.tv_lname);
        final TextView offc = (TextView) findViewById(R.id.tv_offc);
        final TextView pass = (TextView) findViewById(R.id.tv_pass);
        final TextView cpass = (TextView) findViewById(R.id.tv_cpass);
        final TextView pin = (TextView) findViewById(R.id.tv_pin);
        final TextView cpin = (TextView) findViewById(R.id.tv_cpin);
        final TextView a1 = (TextView) findViewById(R.id.tv_a1);
        final TextView a2 = (TextView) findViewById(R.id.tv_a2);
        final TextView a3 = (TextView) findViewById(R.id.tv_a3);
        final TextView q1 = (TextView) findViewById(R.id.tv_q1);
        final TextView q2 = (TextView) findViewById(R.id.tv_q2);
        final TextView q3 = (TextView) findViewById(R.id.tv_q3);
        final LinearLayout fname_lay = (LinearLayout) findViewById(R.id.fname_lay);
        final View view = (View) findViewById(R.id.view);
        final View view1 = (View) findViewById(R.id.view1);
        final View view_q1 = (View) findViewById(R.id.view_q1);
        final View view_q2 = (View) findViewById(R.id.view_q2);
        final View view_q3 = (View) findViewById(R.id.view_q3);
        gender = (RadioGroup) findViewById(R.id.gender);

        final EditText edUser = (EditText) findViewById(R.id.etRegisuser);
        final EditText edPassword = (EditText) findViewById(R.id.etRegispassword);
        final EditText edEmail = (EditText) findViewById(R.id.etemail);
        final EditText edcitations = (EditText) findViewById(R.id.citation);
        edEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        final EditText edRePassword = (EditText) findViewById(R.id.etRegisRepassword);
        final TextView edFname = (TextView) findViewById(R.id.etfname);
        final TextView edLname = (TextView) findViewById(R.id.etlname);
        final EditText edOffc = (EditText) findViewById(R.id.offcAddress);
        final EditText edPin = (EditText) findViewById(R.id.etpin);
        final EditText edCnfrmpin = (EditText) findViewById(R.id.etconfirmpin);
        final EditText etsecAns1 = (EditText) findViewById(R.id.etsecAns1);
        final EditText etsecAns2 = (EditText) findViewById(R.id.etsecAns2);
        final EditText etsecAns3 = (EditText) findViewById(R.id.etsecAns3);
        etsecQues1 = (EditText) findViewById(R.id.etsecques1);
        etsecQues2 = (EditText) findViewById(R.id.etsecques2);
        etsecQues3 = (EditText) findViewById(R.id.etsecques3);
        final CheckBox chbox1 = (CheckBox) findViewById(R.id.chbox1);
        final CheckBox chbox2 = (CheckBox) findViewById(R.id.chbox2);
        final TextView tv_citations=(TextView)findViewById(R.id.tv_citations);

//		final EditText edPhone = (EditText) findViewById(R.id.etphone);
//		edPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        final String fName=getIntent().getStringExtra("fname");
        String lName=getIntent().getStringExtra("lname");
        edFname.setText(fName);
        edLname.setText(lName);
        final LinearLayout optional = (LinearLayout) findViewById(R.id.optional);
        final LinearLayout advance_lay = (LinearLayout) findViewById(R.id.optional_lay);
        final Button arrow = (Button) findViewById(R.id.arrow);
        optional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClicked) {
                    isClicked = false;
                    advance_lay.setVisibility(View.VISIBLE);
                    arrow.setBackgroundResource(R.drawable.up_arrow);
                } else {
                    isClicked = true;
                    advance_lay.setVisibility(View.GONE);
                    arrow.setBackgroundResource(R.drawable.input_arrow);
                }
            }
        });

        final RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
        final RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        radioMale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioMale.isChecked()) {
                    radioMale.setTextColor(Color.parseColor("#458EDB"));
                    radioFemale.setTextColor(Color.parseColor("#ffffff"));
                }


            }
        });
        radioFemale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                radioFemale.setTextColor(Color.parseColor("#458EDB"));
                radioMale.setTextColor(Color.parseColor("#ffffff"));

            }
        });
        dob = getIntent().getStringExtra("dob");
        ssn = getIntent().getStringExtra("ssn");
        houseno = getIntent().getStringExtra("houseno");
        zipcode = getIntent().getStringExtra("zipcode");
        edUser.addTextChangedListener(new TextWatcher() {
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
//		edPhone.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//			}
//
//			@Override
//			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//				if (charSequence.length() > 0) {
//					mobno.setVisibility(View.VISIBLE);
//				} else {
//					mobno.setVisibility(View.GONE);
//				}
//			}
//
//			@Override
//			public void afterTextChanged(Editable editable) {
//			}
//		});
        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    email.setVisibility(View.VISIBLE);
                } else {
                    email.setVisibility(View.GONE);
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
                    if (!isNumeric(charSequence.toString())) {
                        fname_lay.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.VISIBLE);
                        view.setVisibility(View.GONE);
                    } else {
                        fname_lay.setVisibility(View.GONE);
                        view1.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                    }
                } else {
                    fname_lay.setVisibility(View.GONE);
                    fname.setVisibility(View.GONE);
                }
            }

            private boolean isNumeric(String s) {
                String regexStr = "^[a-zA-Z ]+$";
                return s.matches(regexStr);
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
					tv_citations.setVisibility(View.VISIBLE);
				} else {
                    tv_citations.setVisibility(View.GONE);
				}

			}
			@Override
			public void afterTextChanged(Editable editable) {
			}
		});
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    pass.setVisibility(View.VISIBLE);
                } else {
                    pass.setVisibility(View.GONE);
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
                } else {
                    offc.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edPassword.getText().toString().length() == 0) {
                    edPassword.requestFocus();
                    Toast.makeText(context, "Please Enter password",
                            Toast.LENGTH_SHORT).show();
                }
                if (charSequence.length() > 0) {
                    cpass.setVisibility(View.VISIBLE);
                } else {
                    cpass.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edPassword.getText().toString().trim()
                        .equals(edRePassword.getText().toString().trim())) {
                    edRePassword.requestFocus();
                    showAlert1("Password & Confirm password Should be same");
                }
                if (charSequence.length() > 0) {
                    pin.setVisibility(View.VISIBLE);
                } else {
                    pin.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edCnfrmpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    cpin.setVisibility(View.VISIBLE);
                } else {
                    cpin.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etsecAns1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    a1.setVisibility(View.VISIBLE);
                } else {
                    a1.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etsecAns2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    a2.setVisibility(View.VISIBLE);
                } else {
                    a2.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etsecAns3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    a3.setVisibility(View.VISIBLE);
                } else {
                    a3.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        riv = (ImageView) findViewById(R.id.riv1);
        ImageView capture_image = (ImageView) findViewById(R.id.capture_image_view);
        capture_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                strIPath = Environment.getExternalStorageDirectory()
                        + "/COMMedia/MPD_" + getFileName()
                        + ".jpg";
                Intent intent = new Intent(context, CustomVideoCamera.class);
                intent.putExtra("filePath", strIPath);
                intent.putExtra("isPhoto", true);
                startActivityForResult(intent, 32);
            }
        });
        tos = "0";
        baa = "0";
        final TextView tv_tos = (TextView) findViewById(R.id.tv_tos);
        final TextView tv_baa = (TextView) findViewById(R.id.tv_baa);

        chbox1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chbox1.isChecked()) {
                    tos = "1";
                    tv_tos.setTextColor(Color.parseColor("#458EDB"));
                    Intent intent = new Intent(Registration.this, TermsAndAgreement.class);
                    startActivity(intent);
                } else {
                    tos = "0";
                    tv_tos.setTextColor(Color.parseColor("#808080"));
                }
            }
        });
        chbox2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chbox2.isChecked()) {
                    baa = "1";
                    tv_baa.setTextColor(Color.parseColor("#458EDB"));
                    Intent intent = new Intent(Registration.this, TermsAndAgreement.class);
                    startActivity(intent);
                } else {
                    baa = "0";
                    tv_baa.setTextColor(Color.parseColor("#808080"));
                }
            }
        });

        calldisp.getMobileDetails();
        sp1 = (AutoCompleteTextView) findViewById(R.id.rlay_tilte);
        sp2 = (AutoCompleteTextView) findViewById(R.id.rlay_user_type);
        sp3 = (AutoCompleteTextView) findViewById(R.id.rlay_state_of_practice);
        sp4 = (AutoCompleteTextView) findViewById(R.id.rlay_hospital_affiliation);
        sp5 = (Spinner) findViewById(R.id.spsecAns1);
        sp6 = (Spinner) findViewById(R.id.spsecAns2);
        sp7 = (Spinner) findViewById(R.id.spsecAns3);

        final AutoCompleteTextView rlay_professional_org = (AutoCompleteTextView) findViewById(R.id.prof_desgn);
        final AutoCompleteTextView Speciality = (AutoCompleteTextView) findViewById(R.id.speciality);
        final AutoCompleteTextView medical_schools = (AutoCompleteTextView) findViewById(R.id.medical_scl);
        final AutoCompleteTextView residency_pgm = (AutoCompleteTextView) findViewById(R.id.resi_pro);
        final AutoCompleteTextView fellowship_pgm = (AutoCompleteTextView) findViewById(R.id.fellow);
        association_membership = (AutoCompleteTextView) findViewById(R.id.association_membership);
        final TextView tv_title = (TextView) findViewById(R.id.tv_title);
        final TextView tv_usertype = (TextView) findViewById(R.id.tv_usertype);
        final TextView tv_prof = (TextView) findViewById(R.id.tv_Profdesgn);
        final TextView tv_state = (TextView) findViewById(R.id.tv_state);
        final TextView tv_speciality = (TextView) findViewById(R.id.tv_speciality);
        final TextView tv_medical = (TextView) findViewById(R.id.tv_medical);
        final TextView tv_residency = (TextView) findViewById(R.id.tv_residency);
        final TextView tv_fellow = (TextView) findViewById(R.id.tv_fellow);
        final TextView tv_hospital = (TextView) findViewById(R.id.tv_hospital);
        final TextView tv_association = (TextView) findViewById(R.id.tv_association);

        medicalSocietyList=DBAccess.getdbHeler().getMedicalSocietiesDetails();
        medicalDetailsAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, medicalSocietyList);
        association_membership.setAdapter(medicalDetailsAdapter);
        association_membership.setThreshold(1);
        association_membership.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                association_membership.showDropDown();
            }
        });

        hospitalList=DBAccess.getdbHeler().getHospitalDetails();
        hospitalDetailsAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, hospitalList);
        sp4.setAdapter(hospitalDetailsAdapter);
        sp4.setThreshold(1);
        sp4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sp4.showDropDown();
            }
        });


        List<String> list = new ArrayList<String>();
        list.add("Mr.");
        list.add("Ms.");
        list.add("Miss.");
        list.add("Mrs.");
        list.add("Dr.");
        list.add("Prof.");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown_list, list);

        dataAdapter.setDropDownViewResource
                (R.layout.spinner_dropdown_list);

        sp1.setAdapter(dataAdapter);
        sp1.setThreshold(1);
        sp1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sp1.showDropDown();
            }
        });


        list = new ArrayList<String>();
        list.add("Student");
        list.add("Resident");
        list.add("Fellow");
        list.add("Attending");
        list.add("Other");

        dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        sp2.setAdapter(dataAdapter);
        sp2.setSelection(0);
        sp2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sp2.showDropDown();
            }
        });

        states= DBAccess.getdbHeler().getStateDetails();
        stateAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, states);
        sp3.setAdapter(stateAdapter);
        sp3.setThreshold(1);
        sp3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sp3.showDropDown();
            }
        });

        final ArrayList<String> quesList = DBAccess.getdbHeler().getSecurityQuestions();

        list_secQue1 = new ArrayList<String>();
        list_secQue1.add("Choose Question");
        list_secQue1.addAll(quesList);

        dataAdapter_secQue1 = new ArrayAdapter<String>(context, R.layout.spinner_lay, list_secQue1);
        dataAdapter_secQue1.setDropDownViewResource(R.layout.spinner_dropdown_list);

        sp5.setAdapter(dataAdapter_secQue1);

        list_secQue2 = new ArrayList<String>();
        list_secQue2.add("Choose Question");
        list_secQue2.addAll(quesList);

        dataAdapter_secQue2 = new ArrayAdapter<String>(context, R.layout.spinner_lay, list_secQue2);
        dataAdapter_secQue2.setDropDownViewResource(R.layout.spinner_dropdown_list);

        sp6.setAdapter(dataAdapter_secQue2);

        list_secQue3 = new ArrayList<String>();
        list_secQue3.add("Choose Question");
        list_secQue3.addAll(quesList);

        dataAdapter_secQue3 = new ArrayAdapter<String>(context, R.layout.spinner_lay, list_secQue3);
        dataAdapter_secQue3.setDropDownViewResource(R.layout.spinner_dropdown_list);

        sp7.setAdapter(dataAdapter_secQue3);


        list = new ArrayList<String>();
        list.add("Residency Program");
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        residency_pgm.setAdapter(dataAdapter);
        residency_pgm.setSelection(0);
        residency_pgm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                residency_pgm.showDropDown();
            }
        });

        residency_pgm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (residency_pgm.getText().toString().length() > 0)
                    tv_residency.setVisibility(View.VISIBLE);
                else
                    tv_residency.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        list = new ArrayList<String>();
        list.add("Fellowship Program");
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        fellowship_pgm.setAdapter(dataAdapter);
        fellowship_pgm.setSelection(0);
        fellowship_pgm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                fellowship_pgm.showDropDown();
            }
        });

        fellowship_pgm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (fellowship_pgm.getText().toString().length() > 0)
                    tv_fellow.setVisibility(View.VISIBLE);
                else
                    tv_fellow.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        list = new ArrayList<String>();
        list.add("MD");
        list.add("DO");
        list.add("Other");
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        rlay_professional_org.setAdapter(dataAdapter);
        rlay_professional_org.setSelection(0);
        rlay_professional_org.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rlay_professional_org.showDropDown();
            }
        });

        rlay_professional_org.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (rlay_professional_org.getText().toString().length() > 0)
                    tv_prof.setVisibility(View.VISIBLE);
                else
                    tv_prof.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       specialityList=DBAccess.getdbHeler().getSpecialityDetails();
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list, specialityList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        Speciality.setAdapter(dataAdapter);
        Speciality.setThreshold(1);
        Speciality.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Speciality.showDropDown();
            }
        });

        Speciality.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Speciality.getText().toString().length() > 0)
                    tv_speciality.setVisibility(View.VISIBLE);
                else
                    tv_speciality.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        medicalschoolsList=DBAccess.getdbHeler().getMedicalSchoolDetails();
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_list, medicalschoolsList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        medical_schools.setAdapter(dataAdapter);
        medical_schools.setThreshold(1);
        medical_schools.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                medical_schools.showDropDown();
            }
        });

        medical_schools.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (medical_schools.getText().toString().length() > 0)
                    tv_medical.setVisibility(View.VISIBLE);
                else
                    tv_medical.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edUser.setFilters(getpasswordFilter());

        edUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,
                                          int i2, int i3) {
                if (edPassword.getText().toString().length() == 1) {
                    edPassword.setText("");
                }
                if (edRePassword.getText().toString().length() == 1) {
                    edRePassword.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2,
                                      int i3) {
                String username = charSequence.toString();
                if (username.length() > 15) {
                    Toast.makeText(context,
                            "Username should be 4-15 Characters",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        edPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String s = edEmail.getText().toString();
                    if (!s.equals(s.toLowerCase())) {
                        s = s.toLowerCase();
                        edEmail.setText(s);
                    }

                }
            }
        });
        sp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sp2.getText().toString().length() > 0)
                    tv_usertype.setVisibility(View.VISIBLE);
                else
                    tv_usertype.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


//		edPassword.setFilters(getpasswordFilter());
//        edPassword.setInputType(InputType.TYPE_CLASS_TEXT
//                | InputType.TYPE_TEXT_VARIATION_PASSWORD);


//		edRePassword.setEnabled(false);
//		edRePassword.setFilters(getpasswordFilter());
//        edRePassword.setInputType(InputType.TYPE_CLASS_TEXT
//                | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        sp3.addTextChangedListener(new TextWatcher() {
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
            }
        });
        final String[] param = new String[28];
        param[0] = "";
        param[2] = "";
        param[3] = "";
        param[4] = "";
        param[5] = "";
        param[6] = "";
        param[7] = "";
        param[8] = "";
        param[9] = "";
        param[10] = "";
        param[11] = "";
        param[12] = "";
        param[13] = "";
        param[14] = "";
        param[15] = "";
        param[16] = "";
        param[17] = "";
        param[18] = "";
        param[19] = "";
        param[20] = "";
        param[21] = "";
        param[22] = "";
        param[23] = "";
        param[24] = "";
        param[25] = "";
        param[26] = "";
        param[27] = "";

        sp4.addTextChangedListener(new TextWatcher() {
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
        sp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp5.getSelectedItem().toString().equalsIgnoreCase("Other Question")) {
                    etsecQues1.setVisibility(View.VISIBLE);
                    view_q1.setVisibility(View.VISIBLE);
                } else {
                    etsecQues1.setVisibility(View.GONE);
                    view_q1.setVisibility(View.GONE);
                }
                setQuestions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setQuestions();
            }
        });
        etsecQues1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    q1.setVisibility(View.VISIBLE);
                } else {
                    q1.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp6.getSelectedItem().toString().equalsIgnoreCase("Other Question")) {
                    etsecQues2.setVisibility(View.VISIBLE);
                    view_q2.setVisibility(View.VISIBLE);
                } else {
                    etsecQues2.setVisibility(View.GONE);
                    view_q2.setVisibility(View.GONE);
                }
                setQuestions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setQuestions();
            }
        });
        etsecQues2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    q2.setVisibility(View.VISIBLE);
                } else {
                    q2.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        sp7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp7.getSelectedItem().toString().equalsIgnoreCase("Other Question")) {
                    etsecQues3.setVisibility(View.VISIBLE);
                    view_q3.setVisibility(View.VISIBLE);
                } else {
                    etsecQues3.setVisibility(View.GONE);
                    view_q3.setVisibility(View.GONE);
                }
                setQuestions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setQuestions();
            }
        });
        etsecQues3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    q3.setVisibility(View.VISIBLE);
                } else {
                    q3.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final EditText edAnswer = null;

        Button save = (Button) findViewById(R.id.btnRegisterOk);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    if (checkSettings()) {
                        Log.i("AAAA", "REGISTRATION ____");
                        if (edPassword.getText().toString().trim().length() > 0
                                && edRePassword.getText().toString().trim().length() > 0
                                && edEmail.getText().toString().trim().length() > 0
                                && edPassword.getText().toString().trim().length() > 2
                                && edPassword.getText().toString().trim().length() < 32
                                && edRePassword.getText().toString().trim().length() > 0
                                && edRePassword.getText().toString().trim()
                                .equals(edPassword.getText().toString().trim())
                                && validateEmail(edEmail.getText().toString().trim())
                                && edCnfrmpin.getText().toString().trim()
                                .equals(edPin.getText().toString().trim())
                                && edFname.getText().toString().trim()
                                .length() > 0 && edLname.getText().toString().trim()
                                .length() > 0 && edPin.getText().toString().trim()
                                .length() > 0 && edCnfrmpin.getText().toString().trim()
                                .length() > 0 && etsecAns1.getText().toString().trim()
                                .length() > 0 && etsecAns2.getText().toString().trim()
                                .length() > 0 && etsecAns3.getText().toString().trim()
                                .length() > 0 && tos.equalsIgnoreCase("1") && baa.equalsIgnoreCase("1") )

                        {
                            Log.i("AAAA", "REGISTRATION ____");
                            SubscribeBean sb = new SubscribeBean();
                            sb.setName(edUser.getText().toString());
                            sb.setMobileNo("");
                            sb.setPassword(edPassword.getText().toString()
                                    .trim());
                            sb.setEmailId(edEmail.getText().toString().trim());
                            sb.setPhoto(strIPath);
                            sb.setSecques1(sp5.getSelectedItem().toString()
                                    .trim());
                            sb.setSecques2(sp6.getSelectedItem().toString()
                                    .trim());
                            sb.setSecques3(sp7.getSelectedItem().toString()
                                    .trim());
                            sb.setOccupation(sp2.getText().toString());
                            sb.setFname(edFname.getText().toString());
                            sb.setMname("");
                            sb.setLname(edLname.getText().toString());
                            sb.setPin(edPin.getText().toString());
                            sb.setOffcAddr(edOffc.getText().toString());
                            sb.setTitle(sp1.getText().toString().trim());
                            sb.setState(sp3.getText().toString());
                            sb.setHospitalaff(sp4.getText().toString());
                            sb.setExternalip(calldisp.getPublicipaddress());
                            sb.setDevicetype("ANDROID");
                            sb.setTos(tos);
                            sb.setDos(calldisp.returnDetails[2]);
                            sb.setReserved("0");
                            sb.setBaa(baa);
                            sb.setSecans1(etsecAns1.getText().toString());
                            sb.setSecans2(etsecAns2.getText().toString());
                            sb.setSecans3(etsecAns3.getText().toString());
                            sb.setAver(context.getResources().getString(
                                    R.string.app_version));
                            sb.setDob(dob);
                            sb.setSsn(ssn);
                            sb.setHouseno(houseno);
                            sb.setZipcode(zipcode);
                            sb.setProfession(rlay_professional_org.getText().toString());
                            sb.setSpeciality(Speciality.getText().toString());
                            sb.setMedicalschool(medical_schools.getText().toString());
                            sb.setRes_pgm(residency_pgm.getText().toString());
                            sb.setFellow_pgm(fellowship_pgm.getText().toString());
                            sb.setCitations(edcitations.getText().toString());
                            int selectedId = gender.getCheckedRadioButtonId();
                            if(selectedId>0) {
                                genderSelected = (RadioButton) findViewById(selectedId);
                                sb.setSex(genderSelected.getText().toString());
                            }else
                                sb.setSex("");
                            sb.setAssociation(association_membership.getText().toString());
                            if (sp5.getSelectedItem().toString().equalsIgnoreCase("Choose Question") ||
                                    sp6.getSelectedItem().toString().equalsIgnoreCase("Choose Question") ||
                                    sp7.getSelectedItem().toString().equalsIgnoreCase("Choose Question")) {
                                showAlert1("Please Enter valid secret questions");
                            } else if (sp5.getSelectedItem().toString().equalsIgnoreCase("Other Question") ||
                                    sp6.getSelectedItem().toString().equalsIgnoreCase("Other Question") ||
                                    sp7.getSelectedItem().toString().equalsIgnoreCase("Other Question")) {
                                if (sp5.getSelectedItem().toString().equalsIgnoreCase("Other Question"))
                                    sb.setSecques1(etsecQues1.toString().trim());
                                if (sp5.getSelectedItem().toString().equalsIgnoreCase("Other Question"))
                                    sb.setSecques2(etsecQues2.toString().trim());
                                if (sp5.getSelectedItem().toString().equalsIgnoreCase("Other Question"))
                                    sb.setSecques3(etsecQues3.toString().trim());

                            } else {
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
                                WebServiceReferences.webServiceClient.Subscribe(sb, context);
                                showDialog();
                                CallDispatcher.LoginUser = sb.getEmailId();
                                CallDispatcher.Password = sb.getPassword();
                            }

                        } else if (edEmail.getText().toString().trim().length() == 0) {
                            edEmail.requestFocus();
                            showAlert1("Enter Email address");
                        } else if (!validateEmail(edEmail.getText().toString().trim())) {
                            edEmail.requestFocus();
                            showAlert1("Enter valid Email address");
                        } else if (edPassword.getText().toString().trim().length() < 2) {
                            edPassword.requestFocus();
                            showAlert1("Enter Password");

                        } else if (edPassword.getText().toString().trim().length() > 32) {
                            edUser.requestFocus();
                            showAlert1("Password should be maximum 32 characters");
                        } else if (edPassword.getText().toString().trim()
                                .length() != 0
                                && edRePassword.getText().toString().trim()
                                .length() == 0) {
                            edRePassword.requestFocus();
                            showAlert1("Kindly retype your password");
                        } else if (!edPassword.getText().toString().trim()
                                .equals(edRePassword.getText().toString().trim())) {
                            edRePassword.requestFocus();
                            showAlert1("Password & Confirm password Should be same");
                        } else if (!edCnfrmpin.getText().toString().trim()
                                .equals(edPin.getText().toString().trim())) {
                            edCnfrmpin.requestFocus();
                            showAlert1("Pin & Confirm pin Should be same");
                        } else if (etsecAns1.getText().toString().trim().length() == 0 ||
                                etsecAns2.getText().toString().trim().length() == 0 ||
                                etsecAns3.getText().toString().trim().length() == 0) {
                            showAlert1("Please enter secret answer");
                        }else if (tos.equalsIgnoreCase("0") || baa.equalsIgnoreCase("0")) {
                            showAlert1("Please select terms and conditions");
                        } else {
                            if (!(edEmail.getText().toString().trim().length() > 0)
                                    || !(edPassword.getText().toString().trim().length() > 0)
                                    || !(edRePassword.getText().toString().trim().length() > 0)
                                    || !(edAnswer.getText().toString().trim().length() > 0)) {

                                showAlert1("Enter The Mandatory Fields");
                            }
                        }
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    if (AppReference.isWriteInFile)
                        AppReference.logger.error(e.getMessage(), e);

                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 32) {

            File new_file = new File(strIPath);
            if (new_file.exists()) {
                ImageLoader imageLoader;
                imageLoader = new ImageLoader(context.getApplicationContext());
                imageLoader.DisplayImage(strIPath, riv, R.drawable.userphoto);
            }
        }
    }

    public InputFilter[] getpasswordFilter() {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (end > start) {

                    char[] acceptedChars = new char[]{'a', 'b', 'c', 'd',
                            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                            'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
                            '2', '3', '4', '5', '6', '7', '8', '9'};
                    // ,'@', '.',
                    // '_', '#', '$', '%', '*', '-', '+', '(', ')', '!',
                    // '\'', ':', ';', '/', '?', ',', '~', '`', '|', '\\',
                    // '^', '{', '}', '[', ']', '=', '.', '&' };

                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedChars).contains(String
                                .valueOf(source.charAt(index)))) {
                            final EditText edUser = (EditText) findViewById(R.id.etRegisuser);
                            Log.d("Test", "User Length"
                                    + edUser.getText().length());
                            int edUserLength = edUser.getText().length();
                            Log.d("Test",
                                    "not acceptedChar"
                                            + (String.valueOf(source
                                            .charAt(index)) + "Index" + index));
                            if (edUserLength != 0) {
                                Toast.makeText(context,
                                        "Special Characters Not Allowed",
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (edUserLength == 0) {
                                Toast.makeText(
                                        context,
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
        return filters;

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
            Log.d("error", "" + e);
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);

        } finally {
            url = null;
            loginIP = null;
            urlPort = null;
            return returnValue;
        }

    }

    public boolean isAlphaNumeric(String x) {
        String test = x.substring(0, 1);

        if (isNumeric(test)) {
            return false;

        }

        if (x.matches("[a-zA-z0-9]*")) {
            System.out.println("alphanumeric");
            Log.d("AN", "alphanume");
            return true;
        }
        System.out.println("Non alpha numeric");
        Log.d("AN", "Non alphanume");
        return false;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean validateEmail(String id) {
        if (id.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && id.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void notifyRegistrationResponse(final Object obj) {
        Log.i("register", "------888------");
        cancelDialog();
        service_handler.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (obj instanceof String) {
                    String result = (String) obj;
                    if (result.endsWith("successfully")) {
                        Log.i("AAAA", "------runnnn------ if");
                        showToast(result);
                        if (strIPath != null) {
                            String[] param = new String[7];
                            param[0] = CallDispatcher.LoginUser;
                            param[1] = CallDispatcher.Password;
                            param[2] = "image";
                            File file = new File(strIPath);
                            param[3] = file.getName();
                            long length = (int) file.length();
                            length = length/1024;
                            param[5]="other";
                            param[6]= String.valueOf(length);
                            if (file.exists()) {
                                param[4] = encodeTobase64(BitmapFactory.decodeFile(file.getPath()));
                                WebServiceReferences.webServiceClient.FileUpload(param,Registration.this);
                                FileDetailsBean fBean=new FileDetailsBean();
                                fBean.setFilename(param[3]);
                                fBean.setFiletype("image");
                                fBean.setFilecontent(param[4]);
                                fBean.setServicetype("Upload");
                                SingleInstance.fileDetailsBean=fBean;
                            }
                        }
                        CallDispatcher.LoginUser = "";
                        CallDispatcher.Password = "";
                        finish();

                    } else {
                        showAlert1(result);
                        Log.i("AAAA", "------runnnn------ else");
                    }

                    result = null;
                } else if (obj instanceof String)
                    showAlert1((String) obj);
            }

        });
    }

    private String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public void showAlert1(String message) {

        Log.d("call", "callDialScreen");
        AlertDialog.Builder alertCall = new AlertDialog.Builder(this);
        alertCall.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            dialog.dismiss();
                            isDialogVisible = false;
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            if (AppReference.isWriteInFile)
                                AppReference.logger.error(e.getMessage(), e);
                            else
                                e.printStackTrace();
                        }
                    }
                });
        AlertDialog alertDialog = alertCall.create();
        isDialogVisible = true;
        alertDialog.show();
    }


    // private Servicebean getServiceBean(SubscribeBean sb) {
    // XmlComposer xmlComposer = new XmlComposer();
    // String subscribexml = xmlComposer.composeSubscribeXml(sb);
    // Servicebean servicebean = new Servicebean();
    // HashMap<String, String> property_map = new HashMap<String, String>();
    // property_map.put("regInputXml", subscribexml);
    // servicebean.setProperty_map(property_map);
    // servicebean.setWsmethodname("Subscribe");
    // servicebean.setServiceMethods(EnumWebServiceMethods.SUBSCRIBE);
    // servicebean.setCallBack(Registration.this);
    // return servicebean;
    // }

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
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

    public InputFilter[] getNameFilter() {
        InputFilter[] filters = null;
        try {
            filters = new InputFilter[1];
            filters[0] = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start,
                                           int end, Spanned dest, int dstart, int dend) {
                    if (end > start) {

                        char[] acceptedChars = new char[]{'a', 'b', 'c', 'd',
                                'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                                'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
                                'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                                'X', 'Y', 'Z'};

                        for (int index = start; index < end; index++) {
                            if (!new String(acceptedChars).contains(String
                                    .valueOf(source.charAt(index)))) {


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
    public void setQuestions() {

        final ArrayList<String> quesList = DBAccess.getdbHeler().getSecurityQuestions();

        list_secQue1.clear();
        list_secQue1.add("Choose Question");
        list_secQue1.addAll(quesList);
        list_secQue1.add("Other Question");

        list_secQue2.clear();
        list_secQue2.add("Choose Question");
        list_secQue2.addAll(quesList);
        list_secQue2.add("Other Question");

        list_secQue3.clear();
        list_secQue3.add("Choose Question");
        list_secQue3.addAll(quesList);
        list_secQue3.add("Other Question");

        if (!sp5.getSelectedItem().toString().equalsIgnoreCase("Choose Question") && !sp5.getSelectedItem().toString().equalsIgnoreCase("Other Question")) {
            list_secQue2.remove(sp5.getSelectedItem());
            list_secQue3.remove(sp5.getSelectedItem());
            dataAdapter_secQue2.notifyDataSetChanged();
            dataAdapter_secQue3.notifyDataSetChanged();
        }
        if (!sp6.getSelectedItem().toString().equalsIgnoreCase("Choose Question") && !sp6.getSelectedItem().toString().equalsIgnoreCase("Other Question")) {
            list_secQue1.remove(sp6.getSelectedItem());
            list_secQue3.remove(sp6.getSelectedItem());
            dataAdapter_secQue1.notifyDataSetChanged();
            dataAdapter_secQue3.notifyDataSetChanged();
        }
        if (!sp7.getSelectedItem().toString().equalsIgnoreCase("Choose Question") && !sp7.getSelectedItem().toString().equalsIgnoreCase("Other Question")) {
            list_secQue1.remove(sp7.getSelectedItem());
            list_secQue2.remove(sp7.getSelectedItem());
            dataAdapter_secQue1.notifyDataSetChanged();
            dataAdapter_secQue2.notifyDataSetChanged();
        }
    }
}
