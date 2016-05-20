package com.cg.files;

import android.content.Context;
import android.os.Bundle;
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

import com.cg.snazmed.R;
import com.image.utils.FileImageLoader;
import com.main.AppMainActivity;
import com.main.FilesFragment;
import com.util.SingleInstance;

import java.io.File;

/**
 * Created by Rajalakshmi gurunath on 06-05-2016.
 */
public class FileInfoFragment extends Fragment {

    private static FileInfoFragment fileInfoFragment;
    private static Context mainContext;
    public View view;
    LinearLayout content;
    AppMainActivity appMainActivity;
    CompleteListBean cbean;
    private FileImageLoader fileImageLoader = null;
    public static FileInfoFragment newInstance(Context context) {
        try {
            if (fileInfoFragment == null) {
                mainContext = context;
                fileInfoFragment = new FileInfoFragment();
            }

            return fileInfoFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return fileInfoFragment;
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

            final Button edit = (Button) getActivity().findViewById(R.id.btn_settings);
            edit.setVisibility(View.VISIBLE);
            edit.setBackgroundResource(R.drawable.navigation_edit);
            edit.setText("");

            final Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
            plusBtn.setVisibility(View.VISIBLE);
            plusBtn.setText("");
            plusBtn.setBackgroundResource(R.drawable.dot);

            TextView title = (TextView) getActivity().findViewById(
                    R.id.activity_main_content_title);
            title.setVisibility(View.VISIBLE);
            title.setText(cbean.getContentName());
            appMainActivity = (AppMainActivity) SingleInstance.contextTable
                    .get("MAIN");
            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appMainActivity.removeFragments(FileInfoFragment.newInstance(SingleInstance.mainContext));
                    FilesFragment filesFragment = FilesFragment.newInstance(mainContext);
                    FragmentManager fragmentManager = SingleInstance.mainContext
                            .getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(
                            R.id.activity_main_content_fragment, filesFragment)
                            .commitAllowingStateLoss();
                }
            });
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            view = null;
            if (view == null) {
                view = inflater.inflate(R.layout.file_info_fragment, null);
                try {
                    content = (LinearLayout)view. findViewById(R.id.content);
                    content.removeAllViews();
                    LayoutInflater layoutInflater = (LayoutInflater)mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v1 = layoutInflater.inflate(R.layout.file_info, content);
                    TextView modofieddate=(TextView)v1.findViewById(R.id.modifieddate);
                    TextView size=(TextView)v1.findViewById(R.id.size);
                    TextView available=(TextView)v1.findViewById(R.id.available);
                    TextView type=(TextView)v1.findViewById(R.id.type);
                    TextView filename=(TextView)v1.findViewById(R.id.filename);
                    TextView filedesc=(TextView)v1.findViewById(R.id.filedesc);
                    ImageView fileIcon=(ImageView)v1.findViewById(R.id.newfile);

                    fileImageLoader = new FileImageLoader(mainContext);
                    if(cbean!=null){
                        if(cbean.getContentName()!=null) {


                            String fileName = null;
                            if (cbean.getcomponentType().equalsIgnoreCase("hand sketch")) {

                                fileName = cbean.getContentName() + ".jpg";
                            } else if (cbean.getcomponentType().equalsIgnoreCase("video")) {
                                fileName = cbean.getContentName() + ".mp4";
                            } else if (cbean.getcomponentType().equalsIgnoreCase("Audio")) {
                                fileName = cbean.getContentName() + ".mp3";
                            } else if (cbean.getcomponentType().equalsIgnoreCase("photo")) {
                                fileName = cbean.getContentName() + ".jpg";
                            } else {
                                fileName = cbean.getContentName();
                            }
                            filename.setText(fileName);
                        }

                        if(cbean.getContent()!=null)
                            filedesc.setText(cbean.getContent());
                        if(cbean.getcomponentType()!=null)
                            type.setText(cbean.getcomponentType());
                        if(cbean.getDateAndTime()!=null)
                            available.setText(cbean.getDateAndTime());
                        if(cbean.getDateAndTime()!=null)
                            modofieddate.setText(cbean.getDateAndTime());
                        if(cbean.getContentpath()!=null) {
                            size.setText(cbean.getcomponentType());
                            File file = new File(cbean.getContentpath());
                            long length = (int) file.length();
                            length = length/1024;
                            size.setText(bytesToSize((int) length));
                        }
                        if (cbean.getcomponentType().trim().equals("audio")) {
                            fileIcon.setTag(cbean.getComponentId());
                            fileIcon.setVisibility(View.VISIBLE);
                            fileImageLoader.DisplayImage(cbean.getContentpath().replace(".mp4", ".jpg"), fileIcon, R.drawable.audionotesnew);
                        }

                        else if (cbean.getcomponentType().trim().equals("video")) {
                           fileIcon.setTag(cbean.getComponentId());
                            fileIcon.setVisibility(View.VISIBLE);
                            fileImageLoader.DisplayImage(cbean.getContentpath() + ".mp4",fileIcon, R.drawable.videonotesnew);
                        }
                        else if (cbean.getcomponentType().trim().equalsIgnoreCase("sketch")) {
                            fileIcon.setVisibility(View.VISIBLE);
                            fileImageLoader.DisplayImage(cbean.getContentpath(), fileIcon, R.drawable.handpencil);
                        }
                        else if (cbean.getcomponentType().trim().equals("photo")) {
                            fileIcon.setVisibility(View.VISIBLE);
                            fileImageLoader.DisplayImage(cbean.getContentpath(), fileIcon, R.drawable.photonotesnew);
                        } else if (cbean.getcomponentType().trim().equalsIgnoreCase("document")) {
//                            fileIcon.setBackgroundResource(R.drawable.textnotesnew);
//                            String[] name = cbean.getContentpath().split("\\.");
//                            fileIcon.setTag(cbean.getComponentId());
//                            String extn = name[1];
//                            if (cbean.getFromUser().equals("")) {
//                                fileName.setText(extn + " "
//                                        + cbean.getContentName());
//                            } else {
//                                holder.fileName.setText(extn + " "
//                                        + cbean.getContentName() + "\n" + "From: "
//                                        + cbean.getFromUser());
//                            }
                        }
                    }
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
    public void setFileBean(CompleteListBean cBean) {
        cbean = cBean;
    }

    String bytesToSize(int bytes) {
        int kilobyte = 1024;
        int megabyte = kilobyte * 1024;
        int gigabyte = megabyte * 1024;
        int terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " Bytes";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " B";
        }
    }
}
