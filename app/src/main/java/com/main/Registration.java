package com.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
    boolean isOffcState=false;
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
    private AutoCompleteTextView state1;
    private String add_citation="",add_officeaddress = "",add_association_mem = "", Addressline1,Addressline2, office_phone_no, office_fax, zip_code, city_value,office_address, state_value = "";


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
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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
        final TextView edcitations = (TextView) findViewById(R.id.citation);
        final Button plus_id1 = (Button)findViewById(R.id.plus_id1);
        edEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        final EditText edRePassword = (EditText) findViewById(R.id.etRegisRepassword);
        final TextView edFname = (TextView) findViewById(R.id.etfname);
        final TextView edLname = (TextView) findViewById(R.id.etlname);
        final TextView edOffc = (TextView) findViewById(R.id.offcAddress);
        final Button plus_id = (Button)findViewById(R.id.plus_id);
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
        plus_id1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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

                            edcitations.setText(citation_info.getText().toString());
                            plus_id1.setVisibility(View.GONE);
                            dialog.dismiss();

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


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
        plus_id.setOnClickListener(new OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

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
                                               final EditText city = (EditText) dialog1.findViewById(R.id.address_edit4);
                                               final EditText office_phone_number = (EditText) dialog1.findViewById(R.id.address_edit5);
                                               final EditText office_fax_number = (EditText) dialog1.findViewById(R.id.address_edit6);
                                               state1 = (AutoCompleteTextView) dialog1.findViewById(R.id.state_of_practice);
                                               final ImageView offcstate_img=(ImageView)dialog1.findViewById(R.id.offcstate_img);

                                               Button save1 = (Button) dialog1.findViewById(R.id.save_button2);
                                               Button cancel1 = (Button) dialog1.findViewById(R.id.cancel_button2);


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
                                                               office_address = office_address + "," + Addressline2;
                                                           }
                                                           if (zip_code != null && !zip_code.trim().equals("")) {
                                                               office_address = office_address + "," + zip_code;
                                                           }
                                                           if (city_value != null && !city_value.trim().equals("")) {
                                                               office_address = office_address + "," + city_value;
                                                           }
                                                           if (state_value != null && !state_value.trim().equals("")) {
                                                               office_address = office_address + "," + state_value;
                                                           }
                                                           if (office_phone_no != null && !office_phone_no.trim().equals("")) {
                                                               office_address = office_address + "," + office_phone_no;
                                                           }
                                                           if (office_fax != null && !office_fax.trim().equals("")) {
                                                               office_address = office_address + "," + office_fax;
                                                           }

                                                           if (Address1.length() > 0) {
                                                               Address1.append(",");
                                                           }
                                                           String full_officeaddress = office_address;



                                edOffc.setText(full_officeaddress);
                                                           plus_id.setVisibility(View.GONE);
                                                           dialog1.dismiss();
                                                           InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                                           imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                                       }else
                                                           showToast("Please enter mandatory fields");

                                                   }
                                               });
                                               states = DBAccess.getdbHeler().getStateDetails();

                                               stateAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_list, states);
                                               state1.setAdapter(stateAdapter);
                                               state1.setThreshold(30);
                                               offcstate_img.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                                       imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                                       if (isOffcState) {
                                                           isOffcState = false;
                                                           state1.dismissDropDown();
                                                           offcstate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_arrow));
                                                       } else {
                                                           isOffcState = true;
                                                           offcstate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_arrow_up));
                                                           state1.showDropDown();
                                                       }
                                                   }
                                               });


//                    tv_addr.setText(edOffc.getText().toString());
                                               offc.setVisibility(View.VISIBLE);
                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }


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
        list.addAll(loadResidencyFiles());
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
                            if(dob!=null)
                            sb.setDob(dob);
                            if(ssn!=null)
                            sb.setSsn(ssn);
                            if(houseno!=null)
                            sb.setHouseno(houseno);
                            if(zipcode!=null)
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
                                return;
                            } else if (sp5.getSelectedItem().toString().equalsIgnoreCase("Other Question") ||
                                    sp6.getSelectedItem().toString().equalsIgnoreCase("Other Question") ||
                                    sp7.getSelectedItem().toString().equalsIgnoreCase("Other Question")) {
                                if (sp5.getSelectedItem().toString().equalsIgnoreCase("Other Question"))
                                    sb.setSecques1(etsecQues1.toString().trim());
                                if (sp6.getSelectedItem().toString().equalsIgnoreCase("Other Question"))
                                    sb.setSecques2(etsecQues2.toString().trim());
                                if (sp7.getSelectedItem().toString().equalsIgnoreCase("Other Question"))
                                    sb.setSecques3(etsecQues3.toString().trim());

                            } else {
                                sb.setSecques1(sp5.getSelectedItem().toString()
                                        .trim());
                                sb.setSecques2(sp6.getSelectedItem().toString()
                                        .trim());
                                sb.setSecques3(sp7.getSelectedItem().toString()
                                        .trim());
                            }
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
    public ArrayList<String> loadResidencyFiles(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("Abington Memorial Hospital Program");list.add("Advocate Christ Medical Center Program");list.add("Advocate Illinois Masonic Medical Center Program");list.add("Advocate Lutheran General Hospital Program");list.add("Albany Medical Center Program");list.add("Albert Einstein College of Medicine at Bronx-Lebanon Hospital Center Program");list.add("Albert Einstein Healthcare Network Program");list.add("Albert Einstein Medical Center Program");list.add("All Children's Hospital Program");list.add("Allegheny Health Network Medical Education Consortium (AGH) Program");list.add("Allegheny Health Network Medical Education Consortium (SVH) Program");list.add("Allegheny Health Network Medical Education Consortium (WPH) Program");list.add("Allegheny Health Network Medical Education Consortium (WPH/AGH) Program");list.add("AnMed Health (Anderson) Program");list.add("A-OPTIC/The Medical Center Program");list.add("Atlantic Health(Morristown) Program");list.add("Atlantic Health (Overlook) Program");list.add("Atlantic Health Program");list.add("Aurora Health Care Program");list.add("Baton Rouge General Medical Center Program");list.add("Bayfront Health St Petersburg Program");list.add("Baylor College of Medicine Program");list.add("Baylor College of Medicine/St Luke's Episcopal Hospital Program");list.add("Baylor University Medical Center Program");list.add("Baystate Medical Center/Tufts University School of Medicine Program");list.add("Beth Israel Deaconess Medical Center Program");list.add("Beth Israel Deaconess Medical Center/Harvard Medical School Program");list.add("Boston University Medical Center Program");list.add("Bridgeport Hospital Program");list.add("Bridgeport Hospital/Yale University Program");
        list.add("Brigham & Women's Hospital Program");list.add("Brigham and Women's Hospital Program");list.add("Brigham and Women's Hospital/Children's Hospital Program");list.add("Brigham and Women's Hospital/Harvard Medical School Program");list.add("Brigham and Women's Hospital/Massachusetts General Hospital Program");list.add("Brigham and Women's Hospital/Massachusetts General Hospital/Dana-Farber Cancer Institute Program");list.add("Bronx-Lebanon Hospital Center Program");list.add("Brookdale University Hospital and Medical Center Program");list.add("Brooklyn Hospital Center Program");list.add("Brown University (Women and Infants Hospital of Rhode Island) Program");list.add("Brown University Program");list.add("Brown University/Rhode Island Hospital-Lifespan Program");
        list.add("California Pacific Medical Center Program");list.add("Cambridge Health Alliance Program");list.add("Canton Medical Education Foundation/Aultman Hospital/NEOMED Program");list.add("Carilion Clinic-Virginia Tech Carilion School of Medicine Program");list.add("Carle Foundation Hospital Program");list.add("Carolinas Medical Center (Northeast-Cabarrus) Program");list.add("Carolinas Medical Center Program");list.add("Case Western Reserve Univ/Univ Hospitals Case Medical Center Program");list.add("Case Western Reserve University (MetroHealth) Program");list.add("Case Western Reserve University/Univ Hosp Case Med Ctr/Rainbow Babies and Childrens Hospital Program");list.add("Case Western Reserve University/University Hospitals Case Medical Center Program");list.add("Case Western Reserve University-Cleveland Clinic Foundation-MetroHealth Medical Center Program");list.add("Cedars-Sinai Medical Center Program");list.add("CEME/Largo Medical Center Program");list.add("CEME/Larkin Community Hospital Program");list.add("CEME/Magnolia Regional Health Center Program");list.add("CEME/N Broward Hospital District Program");list.add("CEME/Nova Southeastern University COM Program");list.add("CEME/Palmetto General Hosp Program");list.add("Charleston Area Medical Center/West Virginia University (Charleston Division) Program");list.add("Chicago Medical School at Rosalind Franklin University of Medicine and Science Program");list.add("Chicago Medical School/Rosalind Franklin Univ of Med & Sci Program");list.add("Children's Hospital Los Angeles Program");list.add("Children's Hospital Medical Center of Akron Program");list.add("Children's Hospital Medical Center of Akron/NEOMED Program");
        list.add("Children's Hospital of Los Angeles Program");list.add("Children's Hospital of Michigan Program");list.add("Children's Hospital of Philadelphia Program");list.add("Children's Hospital Program");list.add("Children's Hospital/Boston Medical Center Program");list.add("Children's Hospital/Harvard Medical School Program");list.add("Children's Hospital-Oakland Program");list.add("Children's Mercy Hospital Program");list.add("Children's National Medical Center Program");list.add("Children's National Medical Center/George Washington University Program");list.add("Christ Hospital Program");list.add("Christ Hospital/University of Cincinnati College of Medicine Program");list.add("Christiana Care Health Services Program");list.add("Christus Santa Rosa Health Care Program");list.add("Christus Spohn Memorial Hospital Program");list.add("Cincinnati Children's Hospital Medical Center Program");list.add("Cincinnati Children's Hospital Medical Center/University of Cincinnati College of Medicine Program");
        list.add("City of Hope National Medical Center Program");list.add("Cleveland Clinic (Florida) Program");list.add("Cleveland Clinic Florida Program");list.add("Cleveland Clinic Foundation Program");list.add("Clinical Center at the National Institutes of Health Program");list.add("Clinical Center at the National Institutes of Health/National Capital Consortium Program");list.add("Cone Health Program");list.add("Conemaugh Memorial Medical Center Program");list.add("Cooper Hospital-University Medical Center Program");list.add("Cooper Medical School of Rowan University/Cooper University Hospital Program");list.add("CORE/Doctors Hospital Program");list.add("CORE/Firelands Regional Medical Center Main Campus Program");list.add("CORE/Grandview Hosp & Med Ctr Program");list.add("CORE/Grandview Hosp & Medical Ctr Program");list.add("Creighton University Program");list.add("Creighton University School of Medicine Program");list.add("Creighton University/University of Nebraska Program");list.add("Crozer-Chester Medical Center (Keystone Health System) Program");list.add("Dalhousie University");list.add("Danbury Hospital Program");list.add("Dartmouth-Hitchcock Medical Center Program");list.add("Detroit Medical Center Corporation Program");list.add("Detroit Medical Center/Wayne State University Program");list.add("Drexel University College of Medicine/Hahnemann University Hospital Program");list.add("Drexel University College of Medicine/Hahnemann University Program");list.add("Duke University Hospital Program");list.add("East Tennessee State University Program");list.add("Eastern Virginia Medical School Program");list.add("Emory University Program");list.add("Emory University School of Medicine Program");list.add("Fairview Hospital Program");list.add("Family Medicine Residency of Idaho Program");list.add("Florida Hospital Medical Center Program");list.add("Flushing Hospital Medical Center Program");list.add("Geisinger Health System Program");list.add("George Washington University Program");list.add("George Washington University School of Medicine Program");
        list.add("Georgetown University Hospital Program");list.add("Georgetown University Hospital/Adventist Behavioral Health Program");list.add("Georgetown University Hospital/Washington Hospital Center Program");list.add("Grand Rapids Medical Education Partners Program");list.add("Grand Rapids Medical Education Partners/Michigan State University Program");list.add("Grand Rapids Medical Education Partners/Michigan State University Program  New Program");list.add("Grand Rapids Medical Education Partners/MSU/Helen DeVos Children's Hospital Program");list.add("Grant Medical Center (OhioHealth) Program");list.add("Greater Baltimore Medical Center Program");list.add("Greenville Health System/University of South Carolina Program");list.add("Greenwood Genetic Center Program");list.add("Gundersen Lutheran Medical Foundation Program");list.add("Halifax Medical Center Program");list.add("Harlem Hospital Center Program");list.add("Hartford Hospital Program");list.add("HealthPartners Institute for Education and Research/Regions Hospital/Children's Hospitals Program");list.add("Hennepin County Medical Center Program");list.add("Hennepin County Medical Center/Abbott-Northwestern Hospital Program");list.add("Henry Ford Hospital/Wayne State University Program");list.add("Hoboken University Medical Center Program");
        list.add("Hofstra North Shore-LIJ School of Medicine at Cohen Children's Medical Center Program");list.add("Hofstra North Shore-LIJ School of Medicine at Lenox Hill Hospital Program");list.add("Hofstra North Shore-LIJ School of Medicine at North Shore University Hospital Program");list.add("Hofstra North Shore-LIJ School of Medicine at Staten Island University Hospital Program");list.add("Hofstra North Shore-LIJ School of Medicine Program");
        list.add("Hospital Sainte-Justine Howard University Program");list.add("Hurley Medical Center/Michigan State University Program");list.add("Icahn School of Medicine at Mount Sinai (Beth Israel) Program");list.add("Icahn School of Medicine at Mount Sinai (Beth Israel/Brooklyn Hospital Center) Program");list.add("Icahn School of Medicine at Mount Sinai (Clinical Track) Program");list.add("Icahn School of Medicine at Mount Sinai (Elmhurst) Program");list.add("Icahn School of Medicine at Mount Sinai (Elmhurst) Program  New Program");list.add("Icahn School of Medicine at Mount Sinai Program");list.add("Icahn School of Medicine at Mount Sinai/St Lukeís-Roosevelt Hospital Center Program");list.add("Indiana University School of Medicine Program");list.add("Indiana University School of Medicine Program  New Program");list.add("Inova Fairfax Medical Campus/Inova Children's Hospital Program");list.add("Institute of Living/Hartford Hospital Program");list.add("Interfaith Medical Center Program");list.add("Jackson Memorial Hospital/Jackson Health System Program");list.add("Jacobi Medical Center/Albert Einstein College of Medicine Program");list.add("Jamaica Hospital Medical Center Program");list.add("Jamaica Hospital Medical Center/Albert Einstein College of Medicine Program");list.add("Jefferson Medical College Program");list.add("Jefferson Medical College/Christiana Care Health Services Program");list.add("Jefferson Medical College/duPont Hospital for Children Program");list.add("Jersey Shore University Medical Center Program");list.add("JFK Medical Center Program");list.add("John H Stroger Hospital of Cook County Program");list.add("John Peter Smith Hospital (Tarrant County Hospital District) Program");list.add("John Wayne Cancer Institute at St. John's Health Center Program");list.add("Johns Hopkins University Program");list.add("Johns Hopkins University School of Medicine Program");list.add("Johns Hopkins University/Bayview Medical Center Program");list.add("Kaiser Permanente Medical Group (Northern California)/San Francisco Program");list.add("Kaiser Permanente Southern California (Fontana) Program");list.add("Kaiser Permanente Southern California (Los Angeles) Program");list.add("Kaiser Permanente Southern California Program");list.add("Kettering Health Network Program");list.add("Lahey Clinic Program");list.add("Lancaster General Hospital Program");list.add("LECOMT/Arnot Ogden Medical Center Program");list.add("LECOMT/Millcreek Community Hospital Program");list.add("LECOMT/Northside Hosp & Heart Inst Program");list.add("LECOMT/UH Parma Medical Center Program");list.add("LECOMT/Wright Center for GME Program");list.add("Lehigh Valley Health Network Program");list.add("Lehigh Valley Health Network/University of South Florida College of Medicine Program");
        list.add("Lincoln Medical and Mental Health Center Program");list.add("Loma Linda University (Children's Hospital) Program");list.add("Loma Linda University Program");list.add("Loma Linda-Inland Empire Consortium for Healthcare Education Program");list.add("Long Beach Memorial Medical Center Program");list.add("Los Angeles County-Harbor-UCLA Medical Center Program");list.add("Louisiana State University (Shreveport) Program");list.add("Louisiana State University Health Sciences Center Program");list.add("Louisiana State University Program");list.add("Louisiana State University School of Medicine Program");list.add("Loyola University Medical Center Program");list.add("Loyola University Program");list.add("MacNeal Hospital Program");list.add("Magee Womens Hospital");list.add("Maimonides Medical Center Program");
        list.add("Maimonides Medical Center/Infants and Children's Hospital of Brooklyn Program");list.add("Main Line Health System/Lankenau Medical Center Program");list.add("Maine Medical Center Program");list.add("Maine-Dartmouth Family Medicine Program");list.add("Maricopa Medical Center Program");list.add("Marshall University School of Medicine Program");list.add("Marshfield Clinic-St Joseph's Hospital Program");list.add("Mary Hitchcock Memorial Hospital Program");list.add("Massachusetts General Hospital Program");list.add("Massachusetts General Hospital/BIDMC/Harvard Medical School Program");list.add("Massachusetts General Hospital/Brigham and Women's Hospital Program");list.add("Massachusetts General Hospital/Brigham and Women's Hospital/Harvard Medical School Program");list.add("Massachusetts General Hospital/Dana-Farber Cancer Institute/Children's Hospital Boston Program");list.add("Massachusetts General Hospital/Harvard Medical School Program");list.add("Massachusetts General Hospital/McLean Hospital Program");list.add("Mayo Clinic College of Medicine (Arizona) Program");list.add("Mayo Clinic College of Medicine (Jacksonville) Program");list.add("Mayo Clinic College of Medicine (Rochester) ProgramMacNeal Hospital Program");list.add("Magee Womens Hospital");list.add("Maimonides Medical Center Program");list.add("Maimonides Medical Center/Infants and Children's Hospital of Brooklyn Program");list.add("Main Line Health System/Lankenau Medical Center Program");list.add("Maine Medical Center Program");list.add("Maine-Dartmouth Family Medicine Program");list.add("Maricopa Medical Center Program");list.add("Marshall University School of Medicine Program");list.add("Marshfield Clinic-St Joseph's Hospital Program");list.add("Mary Hitchcock Memorial Hospital Program");list.add("Massachusetts General Hospital Program");list.add("Massachusetts General Hospital/BIDMC/Harvard Medical School Program");list.add("Massachusetts General Hospital/Brigham and Women's Hospital Program");list.add("Massachusetts General Hospital/Brigham and Women's Hospital/Harvard Medical School Program");list.add("Massachusetts General Hospital/Dana-Farber Cancer Institute/Children's Hospital Boston Program");list.add("Massachusetts General Hospital/Harvard Medical School Program");list.add("Massachusetts General Hospital/McLean Hospital Program");list.add("Mayo Clinic College of Medicine (Arizona) Program");list.add("Mayo Clinic College of Medicine (Jacksonville) Program");list.add("Mayo Clinic College of Medicine (Rochester) Program");list.add("McGaw Medical Center of Northwestern University Program");list.add("McGill University");list.add("McLennan County Medical Education and Research Foundation Program");list.add("McMaster University");
        list.add("Medical Center of Central Georgia Program");list.add("Medical Center of Central Georgia/Mercer University School of Medicine Program");list.add("Medical College of Georgia Program");list.add("Medical College of Wisconsin Affiliated Hospitals Program");list.add("Medical College of Wisconsin Affiliated Hospitals, Inc Program");
        list.add("McGaw Medical Center of Northwestern University Program");list.add("McGill University");list.add("McLennan County Medical Education and Research Foundation Program");list.add("McMaster University");list.add("Medical Center of Central Georgia Program");list.add("Medical Center of Central Georgia/Mercer University School of Medicine Program");
        list.add("Medical College of Georgia Program");list.add("Medical College of Wisconsin Affiliated Hospitals Program");list.add("Medical College of Wisconsin Affiliated Hospitals, Inc Program");list.add("Medical College of Wisconsin Affiliated Hospitals/Children's Hospital of Wisconsin Program");list.add("Medical University of South Carolina Program");list.add("Meharry Medical College Program");list.add("Memorial Hospital of Rhode Island/Brown University Program");list.add("Memorial Hospital of South Bend Program");list.add("Memorial Sloan Kettering Cancer Center Program");list.add("Memorial Sloan Kettering Cancer Center/New York Presbyterian Hospital (Cornell Campus) Program");list.add("Memorial Sloan-Kettering Cancer Center");list.add("Mercy Health System Program");list.add("Mercy Medical Center-North Iowa Program");list.add("Mercy St Vincent Medical Center Program");list.add("Methodist Health System Dallas Program");list.add("Methodist Hospital (Houston) Program");list.add("Methodist Hospital (Houston) Sugar Land Program");list.add("Methodist Hospital (Houston) Willowbrook Program");list.add("MetroHealth Medical Center Program");list.add("MetroHealth Medical Center/Case Western Reserve University Program");list.add("Miami Children's Health System/Nicklaus Children's Hospital Program");list.add("Michigan State University Program");list.add("Montana Family Medicine Sports Medicine Program");list.add("Montefiore Medical Center/Albert Einstein College of Med at MJHS Hospice and Palliative Care Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Montefiore) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Montefiore-Bronx Lebanon) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Moses and Weiler Campuses) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Wakefield Campus) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine of Yeshiva University Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine Program");list.add("Montreal Children's Hospital");list.add("Mount Auburn Hospital Program");list.add("Mount Carmel Health System Program");list.add("Mount Sinai Medical Center of Florida Program");list.add("Mountain Area Health Education Center Program");
        list.add("MWU/OPTI/Mountain Vista Medical Center Program");list.add("MWU/OPTI/Presence Resurrection Medical Center Program");list.add("MWU/OPTI/Riverside Medical Center Program");list.add("MWU/OPTI/St James Hosp & Health Centers Program");list.add("MWU/OPTI/Swedish Covenant Hospital ProgramMcGaw Medical Center of Northwestern University Program");list.add("McGill University");list.add("McLennan County Medical Education and Research Foundation Program");list.add("McMaster University");list.add("Medical Center of Central Georgia Program");list.add("Medical Center of Central Georgia/Mercer University School of Medicine Program");list.add("Medical College of Georgia Program");list.add("Medical College of Wisconsin Affiliated Hospitals Program");list.add("Medical College of Wisconsin Affiliated Hospitals, Inc Program");list.add("Medical College of Wisconsin Affiliated Hospitals/Children's Hospital of Wisconsin Program");list.add("Medical University of South Carolina Program");list.add("Meharry Medical College Program");list.add("Memorial Hospital of Rhode Island/Brown University Program");list.add("Memorial Hospital of South Bend Program");list.add("Memorial Sloan Kettering Cancer Center Program");list.add("Memorial Sloan Kettering Cancer Center/New York Presbyterian Hospital (Cornell Campus) Program");list.add("Memorial Sloan-Kettering Cancer Center");list.add("Mercy Health System Program");list.add("Mercy Medical Center-North Iowa Program");list.add("Mercy St Vincent Medical Center Program");list.add("Methodist Health System Dallas Program");list.add("Methodist Hospital (Houston) Program");list.add("Methodist Hospital (Houston) Sugar Land Program");list.add("Methodist Hospital (Houston) Willowbrook Program");list.add("MetroHealth Medical Center Program");list.add("MetroHealth Medical Center/Case Western Reserve University Program");list.add("Miami Children's Health System/Nicklaus Children's Hospital Program");list.add("Michigan State University Program");list.add("Montana Family Medicine Sports Medicine Program");list.add("Montefiore Medical Center/Albert Einstein College of Med at MJHS Hospice and Palliative Care Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Montefiore) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Montefiore-Bronx Lebanon) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Moses and Weiler Campuses) Program");
        list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Wakefield Campus) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine of Yeshiva University Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine Program");list.add("Montreal Children's Hospital");list.add("Mount Auburn Hospital Program");list.add("Mount Carmel Health System Program");list.add("Mount Sinai Medical Center of Florida Program");list.add("Mountain Area Health Education Center Program");list.add("MWU/OPTI/Mountain Vista Medical Center Program");
        list.add("MWU/OPTI/Presence Resurrection Medical Center Program");list.add("MWU/OPTI/Riverside Medical Center Program");list.add("MWU/OPTI/St James Hosp & Health Centers Program");list.add("MWU/OPTI/Swedish Covenant Hospital Program");list.add("Children's Hospital of Wisconsin Program");list.add("Medical University of South Carolina Program");list.add("Meharry Medical College Program");list.add("Memorial Hospital of Rhode Island/Brown University Program");list.add("Memorial Hospital of South Bend Program");list.add("Memorial Sloan Kettering Cancer Center Program");list.add("Memorial Sloan Kettering Cancer Center/New York Presbyterian Hospital (Cornell Campus) Program");
        list.add("Memorial Sloan-Kettering Cancer Center");list.add("Mercy Health System Program");list.add("Mercy Medical Center-North Iowa Program");list.add("Mercy St Vincent Medical Center Program");list.add("Methodist Health System Dallas Program");list.add("Methodist Hospital (Houston) Program");list.add("Methodist Hospital (Houston) Sugar Land Program");list.add("Methodist Hospital (Houston) Willowbrook Program");list.add("MetroHealth Medical Center Program");list.add("MetroHealth Medical Center/Case Western Reserve University Program");list.add("Miami Children's Health System/Nicklaus Children's Hospital Program");list.add("Michigan State University Program");list.add("Montana Family Medicine Sports Medicine Program");list.add("Montefiore Medical Center/Albert Einstein College of Med at MJHS Hospice and Palliative Care Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Montefiore) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Montefiore-Bronx Lebanon) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Moses and Weiler Campuses) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine (Wakefield Campus) Program");list.add("Montefiore Medical Center/Albert Einstein College of Medicine of Yeshiva University Program");
        list.add("Montefiore Medical Center/Albert Einstein College of Medicine Program");list.add("Montreal Children's Hospital");list.add("Mount Auburn Hospital Program");list.add("Mount Carmel Health System Program");list.add("Mount Sinai Medical Center of Florida Program");list.add("Mountain Area Health Education Center Program");list.add("MWU/OPTI/Mountain Vista Medical Center Program");list.add("MWU/OPTI/Presence Resurrection Medical Center Program");list.add("MWU/OPTI/Riverside Medical Center Program");list.add("MWU/OPTI/St James Hosp & Health Centers Program");list.add("MWU/OPTI/Swedish Covenant Hospital Program");list.add("Nassau University Medical Center Program");
        list.add("National Institutes of Health Clinical Center Program");list.add("National Institutes of Health Clinical Center/Eunice Kennedy Shriver NICHD Program");list.add("National Rehabilitation Hospital/Washington Hospital Center/Georgetown University Hospital Program");list.add("Nationwide Children's Hospital/Ohio State University Program");list.add("NEOMEN/Kent Hospital Program");list.add("New York Medical College (Metropolitan) Program");list.add("New York Medical College at St Joseph's Regional Medical Center Program");list.add("New York Medical College at Westchester Medical Center Program");list.add("New York Methodist Hospital Program");list.add("New York Presbyterian Hospital (Columbia and Cornell Campus) Program");list.add("New York Presbyterian Hospital (Columbia Campus) Program");list.add("New York Presbyterian Hospital (Columbia) Program");list.add("New York Presbyterian Hospital (Cornell Campus) Program");list.add("New York Presbyterian Hospital (Cornell Campus)/Brooklyn Hospital Center Program");list.add("New York Presbyterian Hospital (Weill-Cornell) Program");list.add("New York Presbyterian Hospital Program");list.add("New York University School of Medicine Program");list.add("New York-Presbyterian/Queens Program");list.add("Newark Beth Israel Medical Center Program");list.add("Northridge Hospital Medical Center Program");list.add("Northside Hospital Program");list.add("Norwalk Hospital Program");list.add("Norwalk Hospital/Yale University Program");list.add("NSUCOM/Larkin Community Hospital Program");list.add("NYCOMEC/Coney Island Hospital Program");list.add("NYCOMEC/MediSys Health Network Program");list.add("NYCOMEC/Nassau University Medical Center Program");list.add("NYCOMEC/Palisades Medical Center Program");list.add("NYCOMEC/Southampton Hospital Program");list.add("Oakwood Hospital Program");list.add("Ochsner Clinic Foundation Program");list.add("O’Connor Hospital (San Jose) Program");list.add("Ohio State University Hospital Program");list.add("Olive View/UCLA Medical Center Program");list.add("OMECO/Integris Baptist Medical Center Program");list.add("OMECO/Oklahoma State University Medical Center Program");list.add("OMECO/Southwestern Regional Med Center Program");list.add("OMNEE/Blue Ridge Healthcare Program");list.add("OPTI West/Nathan Adelson Hospice Program");list.add("OPTI-West/Arrowhead Regional Medical Center Program");list.add("OPTI-West/Community Memorial Health System Program");list.add("OPTI-West/Good Samaritan Reg Med Ctr Program");list.add("OPTI-West/Touro University Nevada COM Program");list.add("OPTI-West/Valley Hospital Medical Center Program");list.add("Oregon Health & Science University Hospital Program");list.add("Oregon Health & Science University Program");list.add("Orlando Health Program");list.add("OU Medical Center Program");list.add("Palmetto Health/University of South Carolina School of Medicine Program");list.add("PCOM/Aria Health Program");list.add("PCOM/Deborah Heart & Lung Center Program");list.add("PCOM/UPMC Altoona Program");list.add("PCOM/Wilson Memorial Reg Med Ctr Program");list.add("Penn State Milton S Hershey Medical Center Program");list.add("Penn State Milton S. Hershey Medical Center Program");list.add("Penn State University Program");list.add("Pennsylvania Hospital of the University of Pennsylvania Health System Program");list.add("Philadelphia College Osteopathic Med Program");list.add("Phoenix Children's Hospital Program");list.add("Ponce School of Medicine Program");list.add("Presence Resurrection Medical Center Program");list.add("Program Name");
        list.add("Providence Hospital and Medical Centers Program");list.add("Providence Hospital Program");list.add("Riverside Methodist Hospitals (OhioHealth) Program");list.add("RMOPTI/Parkview Medical Center Program");list.add("Robert Packer Hospital/Guthrie Program");list.add("Roger Williams Medical Center Program");list.add("RowanSOM/CarePoint Health-Bayonne Medical Center Program");list.add("RowanSOM/OPTI/Inspira Health Network Program");list.add("RowanSOM/OPTI/Kennedy Univ Hosp/Our Lady of Lourdes Program");
        list.add("RowanSOM/OPTI/Kennedy University Hosp/Our Lady of Lourdes Program");list.add("Rush University Medical Center Program");list.add("Rush University Medical Center/Copley Memorial Hospital Program");list.add("Rutgers New Jersey Medical School Program");
        list.add("Rutgers Robert Wood Johnson Medical School at CentraState Program");list.add("Rutgers Robert Wood Johnson Medical School Program");list.add("Sacred Heart Hospital/Temple University (Allentown) Program");list.add("Scott and White Memorial Hospital Program");list.add("Scripps Clinic/Scripps Green Hospital Program");list.add("Scripps Mercy Hospital Program");list.add("SCS/MSUCOM/Botsford Hospital Program");list.add("SCS/MSUCOM/Detroit Wayne County Health Authority GME Program");list.add("SCS/MSUCOM/EW Sparrow Hospital Program");list.add("SCS/MSUCOM/Garden City Hospital Program");list.add("SCS/MSUCOM/Genesys Regional Med Ctr-Health Park Program");list.add("SCS/MSUCOM/Lakeland Regional Med Ctr Program");list.add("SCS/MSUCOM/McLaren Greater Lansing Program");list.add("SCS/MSUCOM/McLaren Macomb Program");list.add("SCS/MSUCOM/McLaren Oakland Program");list.add("SCS/MSUCOM/Metro Health Hospital Program");list.add("SCS/MSUCOM/St John Providence Health System-Osteo Div Program");
        list.add("Self Regional Healthcare/Greenwood Program");list.add("Seton Hall University School of Health and Medical Sciences Program");list.add("Southern Illinois University (Carbondale) Program");list.add("Southern Illinois University (Quincy) Program");list.add("Southern Illinois University Program");list.add("Sparrow Hospital/Michigan State University Program");list.add("Spartanburg Regional Healthcare System Program");list.add("Spaulding Rehabilitation Hospital/Harvard Medical School Program");list.add("Spokane Teaching Health Center Program");list.add("St Christopher's Hospital for Children Program");list.add("St Elizabeth Medical Center Program");list.add("St Elizabeth's Medical Center Program");list.add("St Francis Hospital and Medical Center Program");list.add("St John Hospital and Medical Center Program");list.add("St Joseph Mercy Hospital Program");list.add("St Joseph Mercy-Oakland Program");list.add("St Joseph's Hospital and Medical Center Program");list.add("St Joseph's Regional Medical Center (South Bend) Program");list.add("St Louis University Group of Hospitals Program");list.add("St Louis University School of Medicine Program");list.add("St Luke's Hospital Program");list.add("St Mark's Health Care Foundation Program");list.add("St Mary's Hospital and Medical Center Program");list.add("St Vincent Health Center Program");list.add("St Vincent Hospital and Health Care Center Program");list.add("St Vincent Hospital Program");list.add("St Vincent Hospitals and Health Care Center Program");list.add("St Vincent's East Program");list.add("St. Joseph Mercy Hospital Program");list.add("St. Luke's Hospital Program");list.add("Stanford Hospital and Clinics Program");list.add("Stanford University Program");list.add("St Vincent's East Program");list.add("St. Joseph Mercy Hospital Program");list.add("St. Luke's Hospital Program");list.add("St Vincent's East Program");list.add("St. Joseph Mercy Hospital Program");list.add("St. Luke's Hospital Program");list.add("Stanford Hospital and Clinics Program");list.add("Stanford University Program");list.add("STILL OPTI/Mercy Heart Ctr/Mercy Center-North IA Program");list.add("Stony Brook Medicine/University Hospital Program");list.add("Stony Brook Medicine/Winthrop University Hospital Program");list.add("Strong Memorial Hospital of the University of Rochester Program");list.add("Summa Health System/NEOMED Program");list.add("SUNY Health Science Center at Brooklyn Program");list.add("SUNY Upstate Medical University Program");list.add("Swedish Medical Center Program");list.add("Swedish Medical Center/First Hill Program");list.add("Sylvester Cancer Center, Jackson Memorial Hospital");list.add("Temple University Hospital Program");list.add("Temple University Hospital/Fox Chase Cancer Center Program");list.add("Texas A&M College of Medicine/Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Program");list.add("Texas A&M Health Science Center College of Medicine Program");list.add("Texas Health Presbyterian Dallas Program");list.add("Texas Heart Institute/Baylor College of Medicine Program");list.add("Texas OPTI/Bay Area Corpus Christi Medical Center Program");list.add("Texas OPTI/Plaza Medical Center Program");list.add("Texas OPTI/University of North Texas HSC Program");list.add("Texas Tech University (Amarillo) Program");list.add("Texas Tech University (Lubbock) Program");list.add("Texas Tech University (Permian Basin) ProgramSt Vincent's East Program");list.add("St. Joseph Mercy Hospital Program");list.add("St. Luke's Hospital Program");list.add("Stanford Hospital and Clinics Program");list.add("Stanford University Program");list.add("STILL OPTI/Mercy Heart Ctr/Mercy Center-North IA Program");list.add("Stony Brook Medicine/University Hospital Program");list.add("Stony Brook Medicine/Winthrop University Hospital Program");
        list.add("Strong Memorial Hospital of the University of Rochester Program");list.add("Summa Health System/NEOMED Program");list.add("SUNY Health Science Center at Brooklyn Program");list.add("SUNY Upstate Medical University Program");list.add("Swedish Medical Center Program");list.add("Swedish Medical Center/First Hill Program");
        list.add("Sylvester Cancer Center, Jackson Memorial Hospita");list.add("Temple University Hospital Program");list.add("Temple University Hospital/Fox Chase Cancer Center Program");list.add("Texas A&M College of Medicine/Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Program");list.add("Texas A&M Health Science Center College of Medicine Program");list.add("Texas Health Presbyterian Dallas Program");list.add("Texas Heart Institute/Baylor College of Medicine Program");list.add("Texas OPTI/Bay Area Corpus Christi Medical Center Program");list.add("Texas OPTI/Plaza Medical Center Program");list.add("Texas OPTI/University of North Texas HSC Program");list.add("Texas Tech University (Amarillo) Program");list.add("Texas Tech University (Lubbock) Program");list.add("Texas Tech University (Permian Basin) ProgramStanford Hospital and Clinics Program");list.add("Stanford University Program");list.add("STILL OPTI/Mercy Heart Ctr/Mercy Center-North IA Program");list.add("Stony Brook Medicine/University Hospital Program");list.add("Stony Brook Medicine/Winthrop University Hospital Program");list.add("Strong Memorial Hospital of the University of Rochester Program");list.add("Summa Health System/NEOMED Program");list.add("SUNY Health Science Center at Brooklyn Program");list.add("SUNY Upstate Medical University Program");list.add("Swedish Medical Center Program");
        list.add("Swedish Medical Center/First Hill Program");list.add("Sylvester Cancer Center, Jackson Memorial Hospital");list.add("Temple University Hospital Program");list.add("Temple University Hospital/Fox Chase Cancer Center Program");list.add("Texas A&M College of Medicine/Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Program");list.add("Texas A&M Health Science Center College of Medicine Program");list.add("Texas Health Presbyterian Dallas Program");list.add("Texas Heart Institute/Baylor College of Medicine Program");list.add("Texas OPTI/Bay Area Corpus Christi Medical Center Program");list.add("Texas OPTI/Plaza Medical Center Program");list.add("Texas OPTI/University of North Texas HSC Program");list.add("Texas Tech University (Amarillo) Program");list.add("Texas Tech University (Lubbock) Program");list.add("Texas Tech University (Permian Basin) ProgramSt Vincent's East Program");list.add("St. Joseph Mercy Hospital Program");list.add("St. Luke's Hospital Program");list.add("Stanford Hospital and Clinics Program");list.add("Stanford University Program");list.add("STILL OPTI/Mercy Heart Ctr/Mercy Center-North IA Program");list.add("Stony Brook Medicine/University Hospital Program");list.add("Stony Brook Medicine/Winthrop University Hospital Program");list.add("Strong Memorial Hospital of the University of Rochester Program");list.add("Summa Health System/NEOMED Program");list.add("SUNY Health Science Center at Brooklyn Program");list.add("SUNY Upstate Medical University Program");list.add("Swedish Medical Center Program");list.add("Swedish Medical Center/First Hill Program");list.add("Sylvester Cancer Center, Jackson Memorial Hospital");list.add("Temple University Hospital Program");list.add("Temple University Hospital/Fox Chase Cancer Center Program");list.add("Texas A&M College of Medicine/Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Program");list.add("Texas A&M Health Science Center College of Medicine Program");list.add("Texas Health Presbyterian Dallas Program");list.add("Texas Heart Institute/Baylor College of Medicine Program");list.add("Texas OPTI/Bay Area Corpus Christi Medical Center Program");list.add("Texas OPTI/Plaza Medical Center Program");list.add("Texas OPTI/University of North Texas HSC Program");list.add("Texas Tech University (Amarillo) Program");list.add("Texas Tech University (Lubbock) Program");list.add("Texas Tech University (Permian Basin) ProgramSt Vincent's East Program");list.add("St. Joseph Mercy Hospital Program");list.add("St. Luke's Hospital Program");list.add("Stanford Hospital and Clinics Program");list.add("Stanford University Program");list.add("STILL OPTI/Mercy Heart Ctr/Mercy Center-North IA Program");list.add("Stony Brook Medicine/University Hospital Program");list.add("Stony Brook Medicine/Winthrop University Hospital Program");list.add("Strong Memorial Hospital of the University of Rochester Program");list.add("Summa Health System/NEOMED Program");list.add("SUNY Health Science Center at Brooklyn Program");list.add("SUNY Upstate Medical University Program");list.add("Swedish Medical Center Program");list.add("Swedish Medical Center/First Hill Program");list.add("Sylvester Cancer Center, Jackson Memorial Hospital");list.add("Temple University Hospital Program");list.add("Temple University Hospital/Fox Chase Cancer Center Program");list.add("Texas A&M College of Medicine/Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Program");list.add("Texas A&M Health Science Center College of Medicine Program");list.add("Texas Health Presbyterian Dallas Program");list.add("Texas Heart Institute/Baylor College of Medicine Program");list.add("Texas OPTI/Bay Area Corpus Christi Medical Center Program");list.add("Texas OPTI/Plaza Medical Center Program");list.add("Texas OPTI/University of North Texas HSC Program");list.add("Texas Tech University (Amarillo) Program");list.add("Texas Tech University (Lubbock) Program");list.add("Texas Tech University (Permian Basin) ProgramSt Vincent's East Program");list.add("St. Joseph Mercy Hospital Program");list.add("St. Luke's Hospital Program");list.add("Stanford Hospital and Clinics Program");list.add("Stanford University Program");list.add("STILL OPTI/Mercy Heart Ctr/Mercy Center-North IA Program");list.add("Stony Brook Medicine/University Hospital Program");list.add("Stony Brook Medicine/Winthrop University Hospital Program");list.add("Strong Memorial Hospital of the University of Rochester Program");list.add("Summa Health System/NEOMED Program");list.add("SUNY Health Science Center at Brooklyn Program");list.add("SUNY Upstate Medical University Program");
        list.add("Swedish Medical Center Program");list.add("Swedish Medical Center/First Hill Program");
        list.add("Sylvester Cancer Center, Jackson Memorial Hospital");list.add("Temple University Hospital Program");list.add("Temple University Hospital/Fox Chase Cancer Center Program");
        list.add("Texas A&M College of Medicine/Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Program");list.add("Texas A&M Health Science Center College of Medicine Program");list.add("Texas Health Presbyterian Dallas Program");list.add("Texas Heart Institute/Baylor College of Medicine Program");list.add("Texas OPTI/Bay Area Corpus Christi Medical Center Program");list.add("Texas OPTI/Plaza Medical Center Program");list.add("Texas OPTI/University of North Texas HSC Program");list.add("Texas Tech University (Amarillo) Program");list.add("Texas Tech University (Lubbock) Program");list.add("Texas Tech University (Permian Basin) ProgramSt Vincent's East Program");list.add("St. Joseph Mercy Hospital Program");list.add("St. Luke's Hospital Program");list.add("Stanford Hospital and Clinics Program");list.add("Stanford University Program");list.add("STILL OPTI/Mercy Heart Ctr/Mercy Center-North IA Program");list.add("Stony Brook Medicine/University Hospital Program");list.add("Stony Brook Medicine/Winthrop University Hospital Program");list.add("Strong Memorial Hospital of the University of Rochester Program");list.add("Summa Health System/NEOMED Program");list.add("SUNY Health Science Center at Brooklyn Program");list.add("SUNY Upstate Medical University Program");list.add("Swedish Medical Center Program");list.add("Swedish Medical Center/First Hill Program");list.add("Sylvester Cancer Center, Jackson Memorial Hospital");list.add("Temple University Hospital Program");list.add("Temple University Hospital/Fox Chase Cancer Center Program");list.add("Texas A&M College of Medicine/Scott and White Memorial Hospital Program");
        list.add("Texas A&M College of Medicine-Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Program");list.add("Texas A&M Health Science Center College of Medicine Program");list.add("Texas Health Presbyterian Dallas Program");list.add("Texas Heart Institute/Baylor College of Medicine Program");list.add("Texas OPTI/Bay Area Corpus Christi Medical Center Program");list.add("Texas OPTI/Plaza Medical Center Program");list.add("Texas OPTI/University of North Texas HSC Program");list.add("Texas Tech University (Amarillo) Program");list.add("Texas Tech University (Lubbock) Program");list.add("Texas Tech University (Permian Basin) ProgramSt Vincent's East Program");list.add("St. Joseph Mercy Hospital Program");list.add("St. Luke's Hospital Program");list.add("Stanford Hospital and Clinics Program");list.add("Stanford University Program");list.add("STILL OPTI/Mercy Heart Ctr/Mercy Center-North IA Program");list.add("Stony Brook Medicine/University Hospital Program");list.add("Stony Brook Medicine/Winthrop University Hospital Program");list.add("Strong Memorial Hospital of the University of Rochester Program");list.add("Summa Health System/NEOMED Program");list.add("SUNY Health Science Center at Brooklyn Program");
        list.add("SUNY Upstate Medical University Program");list.add("Swedish Medical Center Program");list.add("Swedish Medical Center/First Hill Program");list.add("Sylvester Cancer Center, Jackson Memorial Hospital");list.add("Temple University Hospital Program");list.add("Temple University Hospital/Fox Chase Cancer Center Program");list.add("Texas A&M College of Medicine/Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Program");list.add("Texas A&M Health Science Center College of Medicine Program");list.add("Texas Health Presbyterian Dallas Program");list.add("Texas Heart Institute/Baylor College of Medicine Program");list.add("Texas OPTI/Bay Area Corpus Christi Medical Center Program");list.add("Texas OPTI/Plaza Medical Center Program");list.add("Texas OPTI/University of North Texas HSC Program");list.add("Texas Tech University (Amarillo) Program");list.add("Texas Tech University (Lubbock) Program");list.add("Texas Tech University (Permian Basin) ProgramSTILL OPTI/Mercy Heart Ctr/Mercy Center-North IA Program");list.add("Stony Brook Medicine/University Hospital Program");list.add("Stony Brook Medicine/Winthrop University Hospital Program");list.add("Strong Memorial Hospital of the University of Rochester Program");list.add("Summa Health System/NEOMED Program");list.add("SUNY Health Science Center at Brooklyn Program");list.add("SUNY Upstate Medical University Program");list.add("Swedish Medical Center Program");list.add("Swedish Medical Center/First Hill Program");list.add("Sylvester Cancer Center, Jackson Memorial Hospital");list.add("Temple University Hospital Program");list.add("Temple University Hospital/Fox Chase Cancer Center Program");list.add("Texas A&M College of Medicine/Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Memorial Hospital Program");list.add("Texas A&M College of Medicine-Scott and White Program");list.add("Texas A&M Health Science Center College of Medicine Program");list.add("Texas Health Presbyterian Dallas Program");list.add("Texas Heart Institute/Baylor College of Medicine Program");list.add("Texas OPTI/Bay Area Corpus Christi Medical Center Program");list.add("Texas OPTI/Plaza Medical Center Program");list.add("Texas OPTI/University of North Texas HSC Program");list.add("Texas Tech University (Amarillo) Program");list.add("Texas Tech University (Lubbock) Program");list.add("Texas Tech University (Permian Basin) Program");list.add("Texas Tech University Health Science Center Paul L Foster School of Medicine Program");list.add("Texas Tech University Health Sciences Center (Permian Basin) Program");list.add("Texas Tech University Health Sciences Center at Lubbock Program");list.add("Texas Tech University Hlth Sci Ctr Paul L. Foster Sch of Med Program");list.add("The Hospital for Sick Children");list.add("Thomas Jefferson University Hospital Program");list.add("Thomas Jefferson University Program");list.add("Toledo Hospital (Jobst Vascular Institute) Program");list.add("Toledo Hospital Program");list.add("TriHealth (Bethesda North Hospital) Program");list.add("TriHealth (Good Samaritan Hospital) Program");list.add("TriHealth Program");list.add("Tufts Medical Center (New England) Program");list.add("Tufts Medical Center Program");list.add("Tulane University Program");list.add("Tulane University School of Medicine Program");list.add("Tuscaloosa College of Community Health Science Program");list.add("Tuscaloosa College of Community Health Sciences Program ");list.add("UAMS-Area Health Education Centers Program");list.add("UC Irvine Medical Center");list.add("UCLA David Geffen School of Medicine/UCLA Medical Center Program");list.add("UCLA Medical Center Program");list.add("UCLA-Cedars Sinai Medical Center");list.add("UCLA-Kern Medical Center Program");list.add("UCSD-Kaiser San Diego");list.add("UCSF Fresno Medical Education Program");list.add("UH Regional Hospitals Program");list.add("Union Memorial Hospital Program");list.add("United Health Services Hospitals Program");list.add("Univ of British Columbia/BC Children's Hospital");list.add("University at Buffalo Program");list.add("University at Buffalo School of Medicine Program");list.add("University Hospitals and Clinics/Louisiana State University (Lafayette) Program");list.add("University of Alabama at Birmingham");list.add("University of Alabama Hospital Program");list.add("University of Alabama Medical Center Program");list.add("University of Arizona College of Medicine Program");
        list.add("University of Arizona College of Medicine-Phoenix Program");list.add("University of Arizona HSC");list.add("University of Arizona Program");list.add("University of Arkansas College of Medicine Program");list.add("University of Arkansas for Medical Sciences Program");list.add("University of Calgary");list.add("University of Calgary/Alberta Children's Hospital");list.add("University of California (Davis) Health System Program");list.add("University of California (Irvine) Medical Center Program");list.add("University of California (Irvine) Program");list.add("University of California (Irvine)/Children's Hospital of Orange County Program");list.add("University of California (Irvine)/Miller Children's Hospital Program");list.add("University of California (San Diego) Medical Center Program");list.add("University of California (San Diego) Program");list.add("University of California (San Francisco) Program");list.add("University of California (San Francisco) School of Medicine Program");list.add("University of California (San Francisco)/Fresno Program");list.add("University of California(Irvine) Medical Center Program");list.add("University of Chicago (NorthShore) Program");
        list.add("University of Chicago Medical Center");list.add("University of Chicago Medical Center Program");list.add("University of Chicago Program");list.add("University of Cincinnati Medical Center/College of Medicine Program");list.add("University of Cincinnati Medical Center/College of Medicine/Cincinnati Children's Program");
        list.add("University of Colorado (University Hospital) Program");list.add("University of Colorado Program");list.add("University of Colorado Program A");list.add("University of Colorado School of Medicine Program");list.add("University of Connecticut Program");list.add("University of Connecticut/Hartford Hospital Program");list.add("University of Florida (Orlando) Program");list.add("University of Florida College of Medicine Jacksonville Program");list.add("University of Florida College of Medicine Program");list.add("University of Florida Program");list.add("University of Hawaii Program");list.add("University of Illinois College of Medicine at Chicago Program");list.add("University of Illinois College of Medicine at Peoria Program");list.add("University of Iowa Hospitals and Clinics Program");list.add("University of Kansas (Wichita)/Via Christi Hospitals Wichita Program");list.add("University of Kansas Medical Center/Children's Mercy Hospital and Clinics Program");list.add("University of Kansas School of Medicine Program");list.add("University of Kansas School of Medicine Program  New Program");
        list.add("University of Kentucky College of Medicine Program");list.add("University of Louisville Program");list.add("University of Louisville School of Medicine Program");list.add("University of Maryland Program");list.add("University of Massachusetts (Worcester) Program");list.add("University of Massachusetts Medical School Program");
        list.add("University of Massachusetts Program");list.add("University of Miami Miller School of Medicine/Holy Cross Hospital Program");list.add("University of Miami/JFK Medical Center Palm Beach Regional GME Consortium Program");list.add("University of Michigan Hospitals and Health Centers Program");list.add("University of Michigan Program");list.add("University of Minnesota Medical School Program");list.add("University of Minnesota Program");list.add("University of Minnesota Program/North Memorial Hospital Program");list.add("University of Minnesota/Gillette Children's Specialty Healthcare Program");list.add("University of Mississippi Medical Center Program");list.add("University of Mississippi School of Medicine Program");list.add("University of Missouri at Kansas City Program");list.add("University of Missouri-Columbia Program");list.add("University of Nebraska Medical Center College of Medicine Program");list.add("University of Nebraska Medical Center College of Medicine/Creighton University Program");list.add("University of Nevada School of Medicine (Las Vegas) Program");list.add("University of Nevada School of Medicine (Reno) Program");list.add("University of Nevada School of Medicine Program");list.add("University of New Mexico Program");list.add("University of New Mexico School of Medicine Program");list.add("University of North Carolina Hospitals Program");list.add("University of Oklahoma College of Medicine Program");list.add("University of Oklahoma Health Sciences Center Program");list.add("University of Oklahoma School of Community Medicine (Tulsa) Program");list.add("University of Oklahoma School of Community Medicine, at Tulsa Program");list.add("University of Ottawa");list.add("University of Pennsylvania Health System Program");list.add("University of Pennsylvania Program");list.add("University of Pennsylvania/Children's Hospital of Philadelphia Program");list.add("University of Puerto Rico Program");list.add("University of Rochester Program");list.add("University of Rochester/Highland Hospital of Rochester Program");list.add("University of South Alabama Hospitals Program");list.add("University of South Alabama Program");list.add("University of South Alabama/Andrews Program");list.add("University of South Dakota Program");list.add("University of South Dakota School of Medicine Program");list.add("University of South Florida Morsani (All Children's) Program");list.add("University of South Florida Morsani (Morton Plant Mease Health Care) Program");list.add("University of South Florida Morsani College of Medicine Program");list.add("University of South Florida Morsani Program");list.add("University of South Florida Morsani Program");list.add("University of Southern California/LAC+USC Medical Center Program");list.add("University of Tennessee College of Medicine at Chattanooga Program");list.add("University of Tennessee College of Medicine Chattanooga Program");list.add("University of Tennessee College of Medicine Program");list.add("University of Tennessee College of Medicine-Chattanooga Program");list.add("University of Tennessee Medical Center at Knoxville Program");list.add("University of Tennessee Medical Center at Knoxville Program,  New Program");list.add("University of Tennessee Program");list.add("University of Tennessee/Saint Francis Program");list.add("University of Texas at Austin Dell Medical School Program");list.add("University of Texas Health Science Center at Houston Program");list.add("University of Texas Health Science Center at Houston/M D Anderson Cancer Center Program");list.add("University of Texas Health Science Center at Tyler Program");list.add("University of Texas Health Science Center School of Medicine at San Antonio Program");
        list.add("University of Texas HSC at San Antonio");list.add("University of Texas M D Anderson Cancer Center Program");list.add("University of Texas MD Anderson Cancer Center/University of Texas Medical School at Houston Program");list.add("University of Texas Medical Branch Hospitals Program");list.add("University of Texas Southwestern Medical School Program");list.add("University of Toledo Program");list.add("University of Toronto");list.add("University of Utah Medical Center Program");list.add("University of Utah Program");list.add("University of Vermont Medical Center Program");list.add("University of Vermont/Fletcher Allen Health Care Program");list.add("University of Virginia Medical Center Program");list.add("University of Virginia Program");list.add("University of Washington Program");list.add("University of Washington School of Medicine Program");list.add("University of Wisconsin Hospital and Clinics Program");list.add("University of Wisconsin Program");list.add("University of Wyoming (Casper) Program");list.add("UPMC Medical Education (Altoona Hospital) Program");list.add("UPMC Medical Education (St Margaret Hospital) Program");list.add("UPMC Medical Education Program");list.add("USC/LAC-Keck School of Medicine");list.add("Utah Valley Regional Medical Center Program");list.add("VA Caribbean Healthcare System Program");list.add("VA Greater Los Angeles Healthcare System Program");list.add("Vanderbilt University Medical Center Program");list.add("Vanderbilt University Medical Center/Monroe Carell, Jr Childrens Hospital Program");list.add("Vidant Medical Center Program");list.add("Vidant Medical Center/East Carolina University Program");list.add("Virginia Commonwealth University Health System (Falls Church) Program");list.add("Virginia Commonwealth University Health System Program");list.add("Virginia Commonwealth University-Bon Secours (St Francis) Program");list.add("Virginia Mason Medical Center Program");list.add("Wake Forest University Baptist Medical Center Program");list.add("Wake Forest University School of Medicine Program");list.add("Washington Hospital Center Program");list.add("Washington University/B-JH/SLCH Consortium Program");list.add("Washington University/B-JH/SLCH Consortium Program  New Program");list.add("Washington University/B-JH/SLCH/ Consortium Program");list.add("West Virginia University Program");list.add("West Virginia University Rural Program");list.add("Western Michigan University Homer Stryker MD School of Medicine Program");list.add("William Beaumont Hospital Program");list.add("Winthrop-University Hospital Program");list.add("Women and Infants Hospital of Rhode Island Program");list.add("Wright Center for Graduate Medical Education Program");list.add("Wright State University Boonshoft School of Medicine Program");list.add("Wright State University Program");list.add("Wright State University/Dayton Community Hospitals Program");list.add("Yale-New Haven Hospital Program");
        list.add("Yale-New Haven Medical Center Program");list.add("York Hospital Program");
        return list;
    }
}
