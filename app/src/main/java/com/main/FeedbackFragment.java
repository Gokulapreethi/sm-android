package com.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;

import org.lib.model.PatientCommentsBean;
import org.lib.webservice.EnumWebServiceMethods;
import org.lib.webservice.Servicebean;

import java.util.HashMap;

/**
 * Created by jansi on 5/10/2016.
 */
public class FeedbackFragment extends Fragment {
    public static FeedbackFragment feedbackFragment;
    public static Context mainContext;
    public View view;
    private  ImageView toggle_off, right_arrow;
    private RelativeLayout relay_feedback;
    TextView text_feedback;
    Boolean istoggleOff=false;
    String add_citation = "";
    private Handler handler = new Handler();
    private Dialog dialog;
    ProgressDialog progressDialog;
    public static CallDispatcher calldisp;

    public static FeedbackFragment newInstance(Context context){
        try{
            if(feedbackFragment ==null){
                mainContext = context;
                feedbackFragment = new FeedbackFragment();
                calldisp = CallDispatcher.getCallDispatcher(context);
            }
            return feedbackFragment;

        }catch (Exception e){
            e.printStackTrace();
            return feedbackFragment;
        }
    }
    @Override
    public View onCreateView(LayoutInflater layoutInflater,ViewGroup container, Bundle savedInstance){
        try{
            AppReference.bacgroundFragment=feedbackFragment;
            Button select = (Button) getActivity().findViewById(R.id.btn_brg);
            select.setVisibility(View.GONE);
            RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
            mainHeader.setVisibility(View.VISIBLE);
            LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
            contact_layout.setVisibility(View.GONE);
            progressDialog= new ProgressDialog(
                    mainContext);

            Button imVw = (Button) getActivity().findViewById(R.id.im_view);
            imVw.setVisibility(View.GONE);

            final Button search1 = (Button) getActivity().findViewById(R.id.btn_settings);
            search1.setVisibility(View.GONE);

            final Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
            plusBtn.setVisibility(View.GONE);

            TextView title = (TextView) getActivity().findViewById(
                    R.id.activity_main_content_title);
            title.setVisibility(View.VISIBLE);
            title.setText("SETTINGS");

            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.GONE);

            view = null;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.feedback_lay, null);
                toggle_off = (ImageView)view.findViewById(R.id.toggle_off);
                relay_feedback = (RelativeLayout)view.findViewById(R.id.relay_feedback);
                right_arrow = (ImageView)view.findViewById(R.id.right_arrow);
                text_feedback = (TextView)view.findViewById(R.id.text_feedback);
                right_arrow.setClickable(false);


                toggle_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!istoggleOff) {
                            toggle_off.setBackgroundDrawable(getResources().getDrawable(R.drawable.toogle_button_on_pink));
                            right_arrow.setClickable(true);
                            text_feedback.setTextColor(getResources().getColor(R.color.white));
                            istoggleOff = true;
                        } else {
                            Log.d("hhhhhi", "jjjj");
                            toggle_off.setBackgroundDrawable(getResources().getDrawable(R.drawable.touch_toogle));
                            right_arrow.setClickable(false);
                            text_feedback.setTextColor(getResources().getColor(R.color.grey));
                            istoggleOff = false;
                        }

                    }
                });



                right_arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showview();

                    }
                });



            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }
    public void showview(){
        try {
             dialog = new Dialog(mainContext);
            dialog.setContentView(R.layout.add_citation);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            Window window = dialog.getWindow();
            dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
            window.setAttributes(lp);
            dialog.show();
            TextView Text_view1 = (TextView)dialog.findViewById(R.id.Text_view1);
            Text_view1.setVisibility(View.GONE);
            final EditText citation_info = (EditText)dialog.findViewById(R.id.Edit_text1);

            Button save = (Button)dialog.findViewById(R.id.save_button1);
            save.setText("SEND");
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

                    if (citation_info.getText().toString().trim().length()>0) {
                       add_citation = citation_info.getText().toString().trim();
                        Log.i("feedback", "save button click");
                        calldisp.showprogress(progressDialog,mainContext);
                        WebServiceReferences.webServiceClient.FeedBack(add_citation);
//                        dialog.dismiss();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showwindow(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                calldisp.cancelDialog(progressDialog);
                final Dialog dialog1 = new Dialog(mainContext);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.feedback_info);
                dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog1.getWindow().setBackgroundDrawableResource(R.color.black2);
                dialog1.show();
                Button btn_ok = (Button) dialog1.findViewById(R.id.btn_ok);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog1.dismiss();

                    }
                });


            }
        });


        }
    }
