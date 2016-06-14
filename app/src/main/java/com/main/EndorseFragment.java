package com.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.account.SecurityQuestions;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.util.SingleInstance;


public class EndorseFragment extends Fragment {

    private static EndorseFragment endorseFragment;
    private static Context mainContext;
    public View view;
    LinearLayout content;
    AppMainActivity appMainActivity;

    public static EndorseFragment newInstance(Context context) {
        try {
            if (endorseFragment == null) {
                mainContext = context;
                endorseFragment = new EndorseFragment();
            }

            return endorseFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return endorseFragment;
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        try {
            AppReference.bacgroundFragment=endorseFragment;
            Button select = (Button) getActivity().findViewById(R.id.btn_brg);
            select.setVisibility(View.GONE);
            final RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
            mainHeader.setVisibility(View.VISIBLE);
            LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
            contact_layout.setVisibility(View.GONE);

            Button imVw = (Button) getActivity().findViewById(R.id.im_view);
            imVw.setVisibility(View.GONE);

            final Button search1 = (Button) getActivity().findViewById(R.id.btn_settings);
            search1.setVisibility(View.GONE);

            final Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
            plusBtn.setVisibility(View.GONE);
            plusBtn.setText("");
            plusBtn.setBackgroundResource(R.drawable.navigation_check);

            TextView title = (TextView) getActivity().findViewById(
                    R.id.activity_main_content_title);
            title.setVisibility(View.VISIBLE);
            title.setText("ENDORSE");
            appMainActivity = (AppMainActivity) SingleInstance.contextTable
                    .get("MAIN");
            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appMainActivity.removeFragments(EndorseFragment.newInstance(SingleInstance.mainContext));
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
                view = inflater.inflate(R.layout.main_endorse, null);
                try {
                    content = (LinearLayout)view. findViewById(R.id.content);
                    content.removeAllViews();
                    LayoutInflater layoutInflater = (LayoutInflater)mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v1 = layoutInflater.inflate(R.layout.endorse1, content);
                    Button confirm=(Button)v1.findViewById(R.id.confirm);
                    final EditText et_code=(EditText)v1.findViewById(R.id.et_code);
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (et_code.getText().toString().length()!=0) {
                                content.removeAllViews();
                                LayoutInflater layoutInflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View v1 = layoutInflater.inflate(R.layout.endorse2, content);
                                Button endorseBtn = (Button) v1.findViewById(R.id.endorseBtn);
                                endorseBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SettingsFragment settingsFragment = SettingsFragment.newInstance(mainContext);
                                        FragmentManager fragmentManager = SingleInstance.mainContext
                                                .getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(
                                                R.id.activity_main_content_fragment, settingsFragment)
                                                .commitAllowingStateLoss();
                                    }
                                });
                            } else
                                Toast.makeText(mainContext, "Please enter endorse code", Toast.LENGTH_SHORT).show();
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
}
