package com.cg.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.SettingsFragment;
import com.util.SingleInstance;

import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecurityQuestions extends Fragment {
    private EditText sec_ans1;
    private EditText sec_ans2;
    private EditText sec_ans3;
    private TextView tv_ans1,tv_ans2,tv_ans3;
    private ImageView donebtn;
    private ProgressDialog progressDialog = null;
    AutoCompleteTextView secQues1,secQues2,secQues3;
    private static SecurityQuestions securityQuestions;
    private static Context mainContext;
    public View view;
    private Handler handler=new Handler();
    List<String> list1;
    private ArrayList<String> list_secQue1;
    private ArrayList<String> list_secQue2;
    private ArrayList<String> list_secQue3;
    private ArrayAdapter<String> dataAdapter_secQue1;
    private ArrayAdapter<String> dataAdapter_secQue2;
    private ArrayAdapter<String> dataAdapter_secQue3;
    AppMainActivity appMainActivity;
    EditText etsecQues1, etsecQues2, etsecQues3;
    public static SecurityQuestions newInstance(Context context) {
        try {
            if (securityQuestions == null) {
                mainContext = context;
                securityQuestions = new SecurityQuestions();

            }

            return securityQuestions;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return securityQuestions;
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            AppReference.bacgroundFragment=securityQuestions;
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
            plusBtn.setVisibility(View.VISIBLE);
            plusBtn.setText("");
            plusBtn.setBackgroundResource(R.drawable.navigation_check);

            TextView title = (TextView) getActivity().findViewById(
                    R.id.activity_main_content_title);
            title.setVisibility(View.VISIBLE);
            title.setText("SECURITY QUESTIONS");
            appMainActivity = (AppMainActivity) SingleInstance.contextTable
                    .get("MAIN");

            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appMainActivity.removeFragments(SecurityQuestions.newInstance(SingleInstance.mainContext));
                    SettingsFragment settingsFragment = SettingsFragment.newInstance(mainContext);
                    FragmentManager fragmentManager = SingleInstance.mainContext
                            .getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(
                            R.id.activity_main_content_fragment, settingsFragment)
                            .commitAllowingStateLoss();
                }
            });
            view = null;
            if (view == null) {
                view = inflater.inflate(R.layout.securityquestions, null);
                try {
                    sec_ans1 = (EditText) view.findViewById(R.id.sec_ans1);
                    sec_ans2 = (EditText) view.findViewById(R.id.sec_ans2);
                    sec_ans3 = (EditText)view. findViewById(R.id.sec_ans3);
                    tv_ans1 = (TextView)view. findViewById(R.id.tv_ans1);
                    tv_ans2 = (TextView)view. findViewById(R.id.tv_ans2);
                    tv_ans3 = (TextView)view. findViewById(R.id.tv_ans3);
                    secQues1 = (AutoCompleteTextView)view. findViewById(R.id.question1);
                    secQues2 = (AutoCompleteTextView) view.findViewById(R.id.question2);
                    secQues3 = (AutoCompleteTextView) view.findViewById(R.id.question3);
                    ImageView back = (ImageView) view.findViewById(R.id.back);

                    String[] questions = Arrays.copyOf(SettingsFragment.questions, SettingsFragment.questions.length);
                    final ArrayList<String> quesList=DBAccess.getdbHeler().getSecurityQuestions();
                    quesList.add("Other Question");

                    list_secQue1 = new ArrayList<String>();
                    list_secQue1.addAll(quesList);
                    sec_ans1.setText(questions[3]);
                    sec_ans2.setText(questions[4]);
                    sec_ans3.setText(questions[5]);
                    tv_ans1.setVisibility(View.VISIBLE);
                    tv_ans2.setVisibility(View.VISIBLE);
                    tv_ans3.setVisibility(View.VISIBLE);

                    dataAdapter_secQue1 = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list, list_secQue1);
                    dataAdapter_secQue1.setDropDownViewResource(R.layout.spinner_dropdown_list);
                    Log.i("AAAA", "Sec " + questions[0] + " " + questions[1] + " " + questions[2]);
                    Log.i("AAAA", "Sec question " + dataAdapter_secQue1.getPosition(questions[0]));
                    secQues1.setAdapter(dataAdapter_secQue1);
                    secQues1.setThreshold(50);
                    secQues1.setText(questions[0]);

                    list_secQue2 = new ArrayList<String>();
                    list_secQue2.addAll(quesList);

                    dataAdapter_secQue2 = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list, list_secQue2);
                    dataAdapter_secQue2.setDropDownViewResource(R.layout.spinner_dropdown_list);

                    secQues2.setAdapter(dataAdapter_secQue2);
                    secQues2.setThreshold(50);
                    secQues2.setText(questions[1]);


                    list_secQue3 = new ArrayList<String>();
                    list_secQue3.addAll(quesList);
                    etsecQues1 = (EditText)view. findViewById(R.id.etsecques1);
                    etsecQues2 = (EditText)view. findViewById(R.id.etsecques2);
                    etsecQues3 = (EditText) view.findViewById(R.id.etsecques3);
                    final View view_q1 = (View) view.findViewById(R.id.view1);
                    final View view_q2 = (View)view. findViewById(R.id.view2);
                    final View view_q3 = (View)view. findViewById(R.id.view3);
                    final TextView q1 = (TextView) view.findViewById(R.id.tv_q1);
                    final TextView q2 = (TextView) view.findViewById(R.id.tv_q2);
                    final TextView q3 = (TextView) view.findViewById(R.id.tv_q3);

                    dataAdapter_secQue3 = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list, list_secQue3);
                    dataAdapter_secQue3.setDropDownViewResource(R.layout.spinner_dropdown_list);
                    secQues3.setAdapter(dataAdapter_secQue3);
                    secQues3.setThreshold(50);
                    secQues3.setText(questions[2]);
                    if(dataAdapter_secQue1.getPosition(questions[0])<0){
                        secQues1.setSelection(dataAdapter_secQue1.getPosition("Other Question"));
                        q1.setVisibility(View.VISIBLE);
                        etsecQues1.setVisibility(View.VISIBLE);
                        view_q1.setVisibility(View.VISIBLE);
                        etsecQues1.setText(questions[0]);
                    }
                    if(dataAdapter_secQue2.getPosition(questions[1])<0){
                        secQues2.setSelection(dataAdapter_secQue2.getPosition("Other Question"));
                        q2.setVisibility(View.VISIBLE);
                        etsecQues2.setVisibility(View.VISIBLE);
                        view_q2.setVisibility(View.VISIBLE);
                        etsecQues2.setText(questions[1]);
                    }
                    if(dataAdapter_secQue3.getPosition(questions[2])<0){
                        secQues3.setSelection(dataAdapter_secQue3.getPosition("Other Question"));
                        q3.setVisibility(View.VISIBLE);
                        etsecQues3.setVisibility(View.VISIBLE);
                        view_q3.setVisibility(View.VISIBLE);
                        etsecQues3.setText(questions[2]);
                    }
                    setQuestions();
                    secQues1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            secQues1.showDropDown();
                        }
                    });
                    secQues2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            secQues2.showDropDown();
                        }
                    });
                    secQues3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("sss", "dropdown ");
                            secQues3.showDropDown();
                        }
                    });
                    secQues1.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (secQues1.getText().toString().equalsIgnoreCase("Other Question")) {
                                etsecQues1.setVisibility(View.VISIBLE);
                                view_q1.setVisibility(View.VISIBLE);
                            } else {
                                etsecQues1.setVisibility(View.GONE);
                                view_q1.setVisibility(View.GONE);
                                q1.setVisibility(View.GONE);
                                setQuestions();
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    secQues2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (secQues2.getText().toString().equalsIgnoreCase("Other Question")) {
                                etsecQues2.setVisibility(View.VISIBLE);
                                view_q2.setVisibility(View.VISIBLE);
                            } else {
                                etsecQues2.setVisibility(View.GONE);
                                view_q2.setVisibility(View.GONE);
                                q2.setVisibility(View.GONE);
                                setQuestions();
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    secQues3.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (secQues3.getText().toString().equalsIgnoreCase("Other Question")) {
                                etsecQues3.setVisibility(View.VISIBLE);
                                view_q3.setVisibility(View.VISIBLE);
                            } else {
                                etsecQues3.setVisibility(View.GONE);
                                view_q3.setVisibility(View.GONE);
                                q3.setVisibility(View.GONE);
                                setQuestions();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
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

                    sec_ans1.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() > 0) {
                                tv_ans1.setVisibility(View.VISIBLE);
                            } else {
                                tv_ans1.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                    sec_ans2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() > 0) {
                                tv_ans2.setVisibility(View.VISIBLE);
                            } else {
                                tv_ans2.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                    sec_ans3.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() > 0) {
                                tv_ans3.setVisibility(View.VISIBLE);
                            } else {
                                tv_ans3.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });

                    plusBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!(sec_ans1.getText().toString().trim().equals("")
                                    || sec_ans2.getText().toString().trim().equals("")
                                    || sec_ans3.getText().toString().trim().equals(""))) {


                                String[] parm = new String[7];
                                        parm[0]=CallDispatcher.LoginUser;

                                        parm[2]=sec_ans1.getText().toString().trim();
                                        parm[4]=sec_ans2.getText().toString().trim();
                                        parm[6]=sec_ans3.getText().toString().trim();
                                if (secQues1.getText().toString().equalsIgnoreCase("Other Question") ||
                                        secQues2.getText().toString().equalsIgnoreCase("Other Question") ||
                                        secQues3.getText().toString().equalsIgnoreCase("Other Question")) {
                                    if (secQues1.getText().toString().equalsIgnoreCase("Other Question"))
                                        parm[1]=etsecQues1.getText().toString().trim();
                                    else
                                        parm[1]=secQues1.getText().toString().trim();
                                    if (secQues2.getText().toString().equalsIgnoreCase("Other Question"))
                                        parm[3]=etsecQues2.getText().toString().trim();
                                    else
                                        parm[3]=secQues2.getText().toString().trim();
                                    if (secQues3.getText().toString().equalsIgnoreCase("Other Question"))
                                        parm[5]=etsecQues3.getText().toString().trim();
                                    else
                                        parm[5]=secQues3.getText().toString().trim();

                                } else {
                                    parm[1]=secQues1.getText().toString().trim();
                                    parm[3]=secQues2.getText().toString().trim();
                                    parm[5]=secQues3.getText().toString().trim();
                                }
                                showDialog();
                                WebServiceReferences.webServiceClient
                                        .UpdateSecretQuestion(parm, securityQuestions);

                            } else {
                                Toast.makeText(mainContext, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return view;
    }
    public View getParentView() {
        return view;
    }
    public void notifyWebServiceResponse(Servicebean servicebean) {
        // TODO Auto-generated method stub
        cancelDialog();
        final WebServiceBean sbean = (WebServiceBean) servicebean.getObj();
        handler.post(new Runnable() {

            @Override
            public void run() {
                String Response = sbean.getText();
                try {

                    if (Response.contains("successfully")) {
                        Toast.makeText(mainContext,
                                Response, Toast.LENGTH_SHORT).show();
                        SettingsFragment settingsFragment = SettingsFragment.newInstance(mainContext);
                        FragmentManager fragmentManager = SingleInstance.mainContext
                                .getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(
                                R.id.activity_main_content_fragment, settingsFragment)
                                .commitAllowingStateLoss();

                    } else {
                        Toast.makeText(mainContext,
                                Response, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        });
    }
    private void showDialog() {
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
    public void setQuestions() {

        final ArrayList<String> quesList = DBAccess.getdbHeler().getSecurityQuestions();

        list_secQue1.clear();
        list_secQue1.addAll(quesList);
        list_secQue1.add("Other Question");

        list_secQue2.clear();
        list_secQue2.addAll(quesList);
        list_secQue2.add("Other Question");

        list_secQue3.clear();
        list_secQue3.addAll(quesList);
        list_secQue3.add("Other Question");
        for(String text:list_secQue1){
            if(secQues1.getText().toString().equalsIgnoreCase(text)){
                list_secQue2.remove(text);
                list_secQue3.remove(text);
            }
        }
        for(String text:list_secQue2){
            if(secQues2.getText().toString().equalsIgnoreCase(text)){
                list_secQue1.remove(text);
                list_secQue3.remove(text);
            }
        }
        for(String text:list_secQue3){
            if(secQues3.getText().toString().equalsIgnoreCase(text)){
                list_secQue2.remove(text);
                list_secQue1.remove(text);
            }
        }
        dataAdapter_secQue1 = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list, list_secQue1);
        dataAdapter_secQue2 = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list, list_secQue2);
        dataAdapter_secQue3 = new ArrayAdapter<String>(mainContext, R.layout.spinner_dropdown_list, list_secQue3);
        secQues1.setAdapter(dataAdapter_secQue1);
        secQues2.setAdapter(dataAdapter_secQue2);
        secQues3.setAdapter(dataAdapter_secQue3);
            dataAdapter_secQue1.notifyDataSetChanged();
            dataAdapter_secQue2.notifyDataSetChanged();
            dataAdapter_secQue3.notifyDataSetChanged();
    }

    }
