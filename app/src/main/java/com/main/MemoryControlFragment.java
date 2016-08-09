package com.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.commongui.LineProgressBar;
import com.cg.files.FileInfoFragment;
import com.cg.snazmed.R;
import com.util.SingleInstance;

import org.lib.model.FileDetailsBean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Vector;


public class MemoryControlFragment extends Fragment {
    private static MemoryControlFragment memoryControlFragment;
    private static Context mainContext;
    public View view;
    LinearLayout content;
    AppMainActivity appMainActivity;
    private LineProgressBar lineProgressbar;
    private LineProgressBar lineProgressbar1;
    private LineProgressBar lineProgressbar2;
    private LineProgressBar lineProgressbar3;
    private LineProgressBar lineProgressbar4;
    private LineProgressBar lineProgressbar5;
    protected int progress;
    int Totalprogress=0;
    int audioprogress=0;
    int videoprogress=0;
    int photoprogress=0;
    int otherprogress=0;
    int messageprogress=0;
    int audio=0, videos=0,other=0,image=0,chat=0;
    Vector<FileDetailsBean> fBeanList;

    private Handler mHandler = new Handler();;
    private int[] colors = {Color.GRAY, Color.CYAN, Color.BLUE};
    public static MemoryControlFragment newInstance(Context context) {
        try {
            if (memoryControlFragment == null) {
                mainContext = context;
                memoryControlFragment = new MemoryControlFragment();
            }

            return memoryControlFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return memoryControlFragment;
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
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
            title.setText("MEMORY CONTROL");
            appMainActivity = (AppMainActivity) SingleInstance.contextTable
                    .get("MAIN");
            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appMainActivity.removeFragments(MemoryControlFragment.newInstance(SingleInstance.mainContext));
                    DashBoardFragment dashBoardFragment = DashBoardFragment.newInstance(mainContext);
                    FragmentManager fragmentManager = SingleInstance.mainContext
                            .getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(
                            R.id.activity_main_content_fragment, dashBoardFragment)
                            .commitAllowingStateLoss();
                }
            });
            view = null;
            if (view == null) {
                view = inflater.inflate(R.layout.memory_control, null);
                try {
                    Log.i("AAAA","Oncreate view of memorycontrol");
                    fBeanList=SingleInstance.fileDetails;
                    for(FileDetailsBean fBean:fBeanList) {
                        Log.i("AAAA", "Omorycontrol &************ size " +fBean.getBranchtype());
                        if (fBean.getBranchtype().equalsIgnoreCase("file")) {
                            if (fBean.getAudiofiles() != null)
                                audio = Integer.parseInt(fBean.getAudiofiles());
                            if (fBean.getVideofiles() != null)
                                videos = Integer.parseInt(fBean.getVideofiles());
                            if (fBean.getImagefiles() != null)
                                image = Integer.parseInt(fBean.getImagefiles());
                        }else if(fBean.getBranchtype().equalsIgnoreCase("im")) {
                            if (fBean.getTotalsize() != null)
                                chat = Integer.parseInt(fBean.getTotalsize());
                        }else if(fBean.getBranchtype().equalsIgnoreCase("other")) {
                                Log.i("AAAA", "Omorycontrol &************ size ");
                                if (fBean.getTotalsize() != null)
                                    other = Integer.parseInt(fBean.getTotalsize());
                            }
                        }
                    TextView tv_audio=(TextView)view.findViewById(R.id.tv_audio);
                    TextView tv_video=(TextView)view.findViewById(R.id.tv_video);
                    TextView tv_photo=(TextView)view.findViewById(R.id.tv_photo);
                    TextView tv_others=(TextView)view.findViewById(R.id.tv_others);
                    TextView tv_chat=(TextView)view.findViewById(R.id.tv_chat);
                    TextView tv_total=(TextView)view.findViewById(R.id.tv_total);
                    TextView tv_free=(TextView)view.findViewById(R.id.tv_free);
                    ImageView audio_image = (ImageView)view.findViewById(R.id.audio_image);
                    ImageView video_image = (ImageView)view.findViewById(R.id.video_image);
                    ImageView photo_image = (ImageView)view.findViewById(R.id.photo_image);
                    ImageView attachment_image = (ImageView)view.findViewById(R.id.attachment_image);
                    ImageView chat_imageview = (ImageView)view.findViewById(R.id.chat_imageview);

                    audio_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FilesFragment filesFragment = FilesFragment.newInstance(mainContext);
                            filesFragment.componentType("AUDIO",true);
                            FragmentManager fragmentManager = SingleInstance.mainContext
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_fragment, filesFragment)
                                    .commitAllowingStateLoss();
                        }
                    });
                    video_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FilesFragment filesFragment = FilesFragment.newInstance(mainContext);
                            filesFragment.componentType("VIDEO",true);
                            FragmentManager fragmentManager = SingleInstance.mainContext
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_fragment, filesFragment)
                                    .commitAllowingStateLoss();
                        }
                    });
                    photo_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FilesFragment filesFragment = FilesFragment.newInstance(mainContext);
                            filesFragment.componentType("PHOTO",true);
                            FragmentManager fragmentManager = SingleInstance.mainContext
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_fragment, filesFragment)
                                    .commitAllowingStateLoss();
                        }
                    });
                    attachment_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FilesFragment filesFragment = FilesFragment.newInstance(mainContext);
                            filesFragment.componentType("ATTACHMENT",true);
                            FragmentManager fragmentManager = SingleInstance.mainContext
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_fragment, filesFragment)
                                    .commitAllowingStateLoss();
                        }
                    });
                    chat_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ContactsFragment contactsFragment = ContactsFragment.getInstance(mainContext);
                            FragmentManager fragmentManager = SingleInstance.mainContext
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_fragment, contactsFragment)
                                    .commitAllowingStateLoss();
                        }
                    });

                    tv_audio.setText(bytesToSize(audio));
                    tv_video.setText(bytesToSize(videos));
                    tv_photo.setText(bytesToSize(image));
                    int total=audio+videos+image+other+chat;
                    tv_total.setText(bytesToSize(total));
                    float num = 5368709120L;
                    float temp1 = num - total;
                    tv_free.setText(bytesToSize(temp1));
                    tv_others.setText(bytesToSize(other));
                    tv_chat.setText(bytesToSize(chat));
                    lineProgressbar = (LineProgressBar) view.findViewById(R.id.line_progressbar);
                    lineProgressbar.setBackgroundColor(getResources().getColor(R.color.yellow));
                    lineProgressbar1 = (LineProgressBar) view.findViewById(R.id.line_progressbar1);
                    lineProgressbar2 = (LineProgressBar) view.findViewById(R.id.line_progressbar2);
                    lineProgressbar3 = (LineProgressBar) view.findViewById(R.id.line_progressbar3);
                    lineProgressbar4 = (LineProgressBar) view.findViewById(R.id.line_progressbar4);
                    lineProgressbar5 = (LineProgressBar) view.findViewById(R.id.line_progressbar5);
                    lineProgressbar5.setForegroundColor(getResources().getColor(R.color.snazlgray));
                    setTimer();
                }catch (Exception e){
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
    private void setTimer() {
        audioprogress=audio;
        videoprogress=videos;
        photoprogress=image;
        otherprogress=other;
        messageprogress=chat;
        Log.i("AAAA","Memory control bean video "+videos+"video progress "+videoprogress);
        Totalprogress=audio+videos+image+other+chat;
        lineProgressbar.setProgress(Totalprogress);
        lineProgressbar1.setProgress(audioprogress);
        lineProgressbar2.setProgress(videoprogress);
        lineProgressbar3.setProgress(photoprogress);
        lineProgressbar4.setProgress(otherprogress);
        lineProgressbar5.setProgress(messageprogress);
    }
    public String size(int size){
        String hrSize = "";
        double m = size/1024.0;
        DecimalFormat dec = new DecimalFormat("0.00");

        if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }
        return hrSize;
    }
    String bytesToSize(float bytes) {
        float kilobyte = 1024;
        float megabyte = kilobyte * 1024;
        float gigabyte = megabyte * 1024;
        float terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return  new BigDecimal(String.valueOf(bytes)).setScale(2, BigDecimal.ROUND_DOWN) + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return  new BigDecimal(String.valueOf((bytes / kilobyte))).setScale(2, BigDecimal.ROUND_DOWN) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return  new BigDecimal(String.valueOf((bytes / megabyte))).setScale(2, BigDecimal.ROUND_DOWN) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return  new BigDecimal(String.valueOf((bytes / gigabyte))).setScale(2, BigDecimal.ROUND_DOWN) + " GB";

        } else if (bytes >= terabyte) {
            return  new BigDecimal(String.valueOf((bytes / terabyte))).setScale(2, BigDecimal.ROUND_DOWN) + " TB";

        } else {
            return  new BigDecimal(String.valueOf(bytes)).setScale(2, BigDecimal.ROUND_DOWN) + " B";
        }
    }

}
