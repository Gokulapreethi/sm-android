package com.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.util.SingleInstance;

import java.util.UUID;

/**
 * Created by jansi on 5/10/2016.
 */
public class PhysicianInviteFragment extends Fragment {
    private static PhysicianInviteFragment physicianInviteFragment;
    private static Context mainContext;
    public View view;
    Boolean isFrom=false;
    private LinearLayout sender_layout;
    private TextView textinfo_1, textinfo_2, textinfo_3, textinfo_5,textinfo_4;
    private Button share;

    public static PhysicianInviteFragment newInstance(Context context) {
        try {
            if ( physicianInviteFragment== null) {
                mainContext = context;
                physicianInviteFragment = new PhysicianInviteFragment();
                physicianInviteFragment.setContext(context);

            }

            return physicianInviteFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return physicianInviteFragment;
        }
    }
    public void setContext(Context cxt) {
        this.mainContext = cxt;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            AppReference.bacgroundFragment=physicianInviteFragment;
            Button select = (Button) getActivity().findViewById(R.id.btn_brg);
            select.setVisibility(View.GONE);
            TextView title = (TextView) getActivity().findViewById(
                    R.id.activity_main_content_title);
            title.setText("INVITE USERS");
            title.setVisibility(View.VISIBLE);


            Button imVw = (Button) getActivity().findViewById(R.id.im_view);
            imVw.setVisibility(View.GONE);
            Button edit = (Button) getActivity().findViewById(R.id.btn_settings);
            edit.setVisibility(View.GONE);
            RelativeLayout mainHeader = (RelativeLayout) getActivity().findViewById(R.id.mainheader);
            mainHeader.setVisibility(View.VISIBLE);
            LinearLayout contact_layout = (LinearLayout) getActivity().findViewById(R.id.contact_layout);
            contact_layout.setVisibility(View.GONE);
            Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
            plusBtn.setVisibility(View.GONE);
            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.VISIBLE);


            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InviteUserFragment inviteUserFragment = InviteUserFragment.newInstance(mainContext);
                    FragmentManager fragmentManager = SingleInstance.mainContext.getSupportFragmentManager();
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.beginTransaction().replace(R.id.activity_main_content_fragment, inviteUserFragment).commitAllowingStateLoss();
                }
            });
            view = null;
            if (view == null) {
                view = inflater.inflate(R.layout.otheruser_invite, null);
                textinfo_5 = (TextView)view.findViewById(R.id.textinfo_5);
                textinfo_4 = (TextView)view.findViewById(R.id.textinfo_4);
                textinfo_2 = (TextView)view.findViewById(R.id.textinfo_2);
                textinfo_3 = (TextView)view.findViewById(R.id.textinfo_3);
                share = (Button)view.findViewById(R.id.share_button);
                sender_layout = (LinearLayout)view.findViewById(R.id.sender_layout);

                if (isFrom){
//                    textinfo_1.setText("Hi. I am using SnazMed.  An amazing \n" +
//                            "healthcare communication app. Please \n" +
//                            "download it using the links below so we \n" +
//                            "can start exchanging HIPAA-compliant, \n" +
//                            "secure communications and much more." );
//                    textinfo_2.setText("play.google.com");
//
//                    textinfo_2.setTextColor(mainContext.getResources().getColor(R.color.blue2));
                    textinfo_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("https://play.google.com"));
                            startActivity(i);
                        }
                    });
                    textinfo_2.setVisibility(View.GONE);
                    textinfo_3.setVisibility(View.GONE);
                    textinfo_4.setVisibility(View.GONE);
//                    textinfo_3.setVisibility(View.GONE);
                }else{
//                    sender_layout.setWeightSum(3);
//                    textinfo_1.setText("play.google.com");
                    String Code = UUID.randomUUID().toString().trim().substring(0, 8).toUpperCase();
                    Log.d("value","code"+Code);
                    textinfo_4.setText(Code);

                    textinfo_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com"));
                            startActivity(i);
                        }
                    });
//                    textinfo_1.setTextColor(mainContext.getResources().getColor(R.color.blue2));
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//                    startActivity(browserIntent);
//                    textinfo_2.setText("Download the app\n and enter this code:");
//                    textinfo_3.setVisibility(View.VISIBLE);


                }


                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String shareBody = "";
                        if(isFrom)
                            shareBody = "Download the app\nhttps://play.google.com";
                                    else
                        shareBody = "Download the app\n" +
                                "https://play.google.com "+textinfo_3.getText().toString();
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent,""));


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
    public void texturl(){
        textinfo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = textinfo_2.getText().toString().trim();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
            }
        });

    }
    public void isFromOther(Boolean isOther){
        isFrom=isOther;
    }
}
