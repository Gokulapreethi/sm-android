package com.cg.account;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.SettingsFragment;
import com.util.SingleInstance;

import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;

public class PinAndTouchId extends Fragment {
    private Context context;
    private Handler handler = new Handler();
    private EditText et_oldpin;
    private EditText et_newpin;
    private EditText et_repeatpin;
    private TextView oldpin;
    private TextView newpin;
    private TextView repeatpin;
    private ImageView toggle_off;
    private Button savepswd;
    private ProgressDialog progressDialog = null;
    private static PinAndTouchId pinAndTouchId;
    private static CallDispatcher calldisp = null;
    public View _rootView;
    AppMainActivity appMainActivity;
    Boolean istoggleOff=false;
    public static PinAndTouchId newInstance(Context context) {
        try {
            if (pinAndTouchId == null) {
                pinAndTouchId = new PinAndTouchId();
                pinAndTouchId.setContext(context);
                calldisp = CallDispatcher.getCallDispatcher(context);

            }
            return pinAndTouchId;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return pinAndTouchId;
        }
    }
    public void setContext(Context cxt) {
        this.context = cxt;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        title.setText("PIN & TOUCH ID");
        appMainActivity = (AppMainActivity) SingleInstance.contextTable
                .get("MAIN");
        Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appMainActivity.removeFragments(PinAndTouchId.newInstance(SingleInstance.mainContext));
                SettingsFragment settingsFragment = SettingsFragment.newInstance(context);
                FragmentManager fragmentManager = SingleInstance.mainContext
                        .getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(
                        R.id.activity_main_content_fragment, settingsFragment)
                        .commitAllowingStateLoss();
            }
        });
        _rootView = null;
        if (_rootView == null) {

            _rootView = inflater.inflate(R.layout.pinandtouchid, null);
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            try {

                et_oldpin = (EditText) _rootView.findViewById(R.id.et_oldpin);
                et_newpin = (EditText)_rootView. findViewById(R.id.et_newpin);
                et_repeatpin = (EditText)_rootView. findViewById(R.id.et_repeatpin);
                oldpin = (TextView)_rootView. findViewById(R.id.oldpin);
                newpin = (TextView)_rootView. findViewById(R.id.newpin);
                repeatpin = (TextView)_rootView. findViewById(R.id.repeatpin);
                savepswd = (Button) _rootView.findViewById(R.id.savepswd);
                toggle_off = (ImageView)_rootView.findViewById(R.id.toggle_off);
                setSave();

                toggle_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!istoggleOff) {
                            toggle_off.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_dummy));
                            istoggleOff = true;
                            final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.error_dialogue);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
                ImageView error_img = (ImageView) dialog.findViewById(R.id.error_img);
                TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                TextView tv_firstLine = (TextView) dialog.findViewById(R.id.tv_firstLine);
                TextView tv_secondLine = (TextView) dialog.findViewById(R.id.tv_secondLine);
                TextView tv_note = (TextView) dialog.findViewById(R.id.tv_note);
                Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                error_img.setVisibility(View.VISIBLE);
                tv_title.setVisibility(View.VISIBLE);
                tv_firstLine.setVisibility(View.VISIBLE);
                tv_firstLine.setText("You need to activate\nthe option in your\nphone settings");
                tv_secondLine.setVisibility(View.GONE);
                tv_note.setVisibility(View.GONE);
                btn_ok.setText("OK");
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SingleInstance.mainContext.isTouchIdEnabled=true;
                        dialog.dismiss();
                    }
                });
                dialog.show();
//
                        } else {
                            SingleInstance.mainContext.isTouchIdEnabled=false;
                            toggle_off.setBackgroundDrawable(getResources().getDrawable(R.drawable.touch_toogle));
                            Log.d("toggle1", "button");
                            istoggleOff = false;
                        }

                    }
                });

                et_newpin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            newpin.setVisibility(View.VISIBLE);
                        } else {
                            newpin.setVisibility(View.GONE);
                        }
                        setSave();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });

                et_repeatpin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            repeatpin.setVisibility(View.VISIBLE);
                        } else {
                            repeatpin.setVisibility(View.GONE);
                        }
                        setSave();
                        if(charSequence.length() >= 4){
                            InputMethodManager imm = (InputMethodManager)SingleInstance.mainContext.getSystemService(SingleInstance.mainContext.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(et_repeatpin.getWindowToken(), 0);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                et_oldpin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > 0) {
                            oldpin.setVisibility(View.VISIBLE);
                        } else {
                            oldpin.setVisibility(View.GONE);
                        }
                        setSave();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });

                savepswd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(et_newpin.getText().toString().equals(et_repeatpin.getText().toString())){
                            if(!et_oldpin.getText().toString().equals(et_newpin.getText().toString().trim())){
                                String[] parm = {CallDispatcher.LoginUser, et_oldpin.getText().toString(),et_newpin.getText().toString()};
                                WebServiceReferences.webServiceClient.ResetPin(parm, pinAndTouchId);
                                showDialog();
                            }else{
                                Toast.makeText(context,"Your old PIN and new PIN are same. Please try different",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(context,"Please enter correct PIN ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//                toggle_off.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.error_dialogue);
//                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
//                ImageView error_img = (ImageView) dialog.findViewById(R.id.error_img);
//                TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
//                TextView tv_firstLine = (TextView) dialog.findViewById(R.id.tv_firstLine);
//                TextView tv_secondLine = (TextView) dialog.findViewById(R.id.tv_secondLine);
//                TextView tv_note = (TextView) dialog.findViewById(R.id.tv_note);
//                Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
//                error_img.setVisibility(View.VISIBLE);
//                tv_title.setVisibility(View.VISIBLE);
//                tv_firstLine.setVisibility(View.VISIBLE);
//                tv_firstLine.setText("You need to activate\nthe option in your\nphone settings");
//                tv_secondLine.setVisibility(View.GONE);
//                tv_note.setVisibility(View.GONE);
//                btn_ok.setText("OK");
//                btn_ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
            ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        return _rootView;
    }
    public void setSave(){
        if ((et_newpin.getText().toString().length() > 0) && (et_oldpin.getText().toString().length() > 0) &&
                (et_repeatpin.getText().toString().length() > 0)){
            savepswd.setEnabled(true);
        }else{
            savepswd.setEnabled(false);
        }
    }
    public View getParentView() {
        return _rootView;
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
                        AppMainActivity.pinNo=et_newpin.getText().toString().trim();
                        Toast.makeText(context,
                                Response, Toast.LENGTH_SHORT).show();
                        SettingsFragment settingsFragment = SettingsFragment.newInstance(context);
                        FragmentManager fragmentManager = SingleInstance.mainContext
                                .getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(
                                R.id.activity_main_content_fragment, settingsFragment)
                                .commitAllowingStateLoss();

                    } else {
                        Toast.makeText(context,
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
