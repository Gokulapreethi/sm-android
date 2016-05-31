package com.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.callservices.AudioCallScreen;
import com.cg.callservices.VideoCallScreen;
import com.cg.rounding.RoundingFragment;
import com.cg.snazmed.R;
import com.util.SingleInstance;

/**
 * Created by jansi on 5/10/2016.
 */
public class InviteUserFragment extends android.support.v4.app.Fragment {
    private static InviteUserFragment inviteUserFragment;
    private static Context mainContext;
    public View view;
    private LinearLayout physician, Other_user;

    public static InviteUserFragment newInstance(Context context) {
        try {
            if (inviteUserFragment == null) {
                mainContext = context;
                inviteUserFragment = new InviteUserFragment();
                inviteUserFragment.setContext(context);

            }

            return inviteUserFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return inviteUserFragment;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            Button select = (Button) getActivity().findViewById(R.id.btn_brg);
            select.setVisibility(View.GONE);
            TextView title = (TextView) getActivity().findViewById(
                    R.id.activity_main_content_title);
            title.setText("INVITE USERS");
            title.setVisibility(View.VISIBLE);

            Button imVw = (Button) getActivity().findViewById(R.id.im_view);
            imVw.setVisibility(View.GONE);
            Button edit = (Button) getActivity().findViewById(
                    R.id.btn_settings);
            edit.setVisibility(View.GONE);
            final RelativeLayout mainHeader = (RelativeLayout) getActivity().findViewById(R.id.mainheader);
            mainHeader.setVisibility(View.VISIBLE);
            LinearLayout contact_layout = (LinearLayout) getActivity()
                    .findViewById(R.id.contact_layout);
            contact_layout.setVisibility(View.GONE);
            Button plusBtn = (Button) getActivity()
                    .findViewById(R.id.add_group);
            plusBtn.setVisibility(View.GONE);
            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.GONE);
            RelativeLayout audio_minimize = (RelativeLayout)getActivity().findViewById(R.id.audio_minimize);
            RelativeLayout video_minimize = (RelativeLayout)getActivity().findViewById(R.id.video_minimize);
            audio_minimize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainHeader.setVisibility(View.GONE);
                    addShowHideListener(true);
                }
            });
            video_minimize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainHeader.setVisibility(View.GONE);
                    addShowHideListener(false);
                }
            });
            view = null;
            if (view == null) {
                view = inflater.inflate(R.layout.invite_user, null);
                physician = (LinearLayout) view.findViewById(R.id.physician);
                Other_user = (LinearLayout) view.findViewById(R.id.other_user);

                physician.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhysicianInviteFragment physicianInvite = PhysicianInviteFragment.newInstance(mainContext);
                        physicianInvite.isFromOther(true);
                        FragmentManager fragmentManager = SingleInstance.mainContext
                                .getSupportFragmentManager();
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.beginTransaction().replace(
                                R.id.activity_main_content_fragment, physicianInvite)
                                .commitAllowingStateLoss();
                    }
                });
                Other_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhysicianInviteFragment othersInvite = PhysicianInviteFragment.newInstance(mainContext);
                        othersInvite.isFromOther(false);
                        FragmentManager fragmentManager = SingleInstance.mainContext
                                .getSupportFragmentManager();
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.beginTransaction().replace(
                                R.id.activity_main_content_fragment, othersInvite)
                                .commitAllowingStateLoss();
                    }
                });

            } else {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    public View getParentView() {
        return view;
    }
    public void setContext(Context cxt) {
        this.mainContext = cxt;
    }
    void addShowHideListener( final Boolean isAudio) {
        if(isAudio) {
            AudioCallScreen audioCallScreen = AudioCallScreen.getInstance(SingleInstance.mainContext);
            FragmentManager fragmentManager = SingleInstance.mainContext
                    .getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(
                    R.id.activity_main_content_fragment, audioCallScreen)
                    .commitAllowingStateLoss();
        }else {
            VideoCallScreen videoCallScreen = VideoCallScreen.getInstance(SingleInstance.mainContext);
            FragmentManager fragmentManager = SingleInstance.mainContext
                    .getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(
                    R.id.activity_main_content_fragment, videoCallScreen)
                    .commitAllowingStateLoss();
        }
    }
}
