package com.cg.files;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.group.chat.ForwardUserSelect;
import com.image.utils.FileImageLoader;
import com.main.AppMainActivity;
import com.main.FilesFragment;
import com.util.SingleInstance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

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
    private TextView tv_send;
    private String componenttype;
    private ArrayList<String> uploadFilesList = null;
    private String filename1;
    CallDispatcher calldisp = new CallDispatcher(SingleInstance.mainContext);
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

            final Button imVw = (Button) getActivity().findViewById(R.id.im_view);
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
                    TextView tv_share = (TextView)view.findViewById(R.id.tv_share);
                    tv_send = (TextView)view.findViewById(R.id.tv_send);


                    fileImageLoader = new FileImageLoader(mainContext);
                    if(cbean!=null){
                        if(cbean.getContentName()!=null) {

                            componenttype = cbean.getcomponentType();

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
                        filename1 = cbean.getContentpath();
                        tv_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(SingleInstance.mainContext, ForwardUserSelect.class);
                                intent.putExtra("fromfiles",true);
                                SingleInstance.mainContext.startActivity(intent);

                            }
                        });
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
    private ArrayList<String> SendbuddyList = new ArrayList<String>();
    public ArrayList<String> uploadDatas = new ArrayList<String>();

    private void shareMultipleFiles() {

        try {{


            if (SendbuddyList.size() > 0) {

                        if (WebServiceReferences.running) {
                            uploadDatas.add(cbean.getcomponentType());
                            uploadDatas.add("false");
                            uploadDatas.add("");
                            uploadDatas.add("");
                            uploadDatas.add("auto");
                            Log.d("sendershare", "" + uploadDatas);
                            sendshare(true);
                        }





            } else {
                Toast.makeText(mainContext,
                        SingleInstance.mainContext.getResources().getString(R.string.kindly_select_any_buddies),
                        Toast.LENGTH_LONG).show();

            }

            // sendShare(spinner1.getSelectedItem().toString());

        }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
        }

    }

    private void sendshare(boolean flag) {
        try {
            String username = CallDispatcher.LoginUser;
            String password = CallDispatcher.Password;
            ArrayList<String> b_list = new ArrayList<String>();
            b_list.addAll(SendbuddyList);
            Collections.sort(b_list);
            ArrayList<String> u_list = new ArrayList<String>();
            u_list.addAll(uploadDatas);
            calldisp.uploadData.add(filename1);
            uploadFilesList.add(filename1);

			/*
			 * To upload files using webservice
			 */
            String path = filename1;


            if(componenttype.equalsIgnoreCase("photo")||componenttype.equalsIgnoreCase("handsketch")) {
                Log.i("FileUpload", "IF PHOTO||Handsketch--->");

                Bitmap bitmap = BitmapFactory.decodeFile(path);
                String base64 = encodeTobase64(bitmap);
                String fname = filename1.split("/")[5];
                Log.i("FileUpload", "fname--->" + fname);
                Log.i("FileUpload", "uname--->" + username);
                Log.i("FileUpload", "password--->" + password);
                Log.i("FileUpload", "type--->" + componenttype);
                Log.i("FileUpload", "base64--->" + base64);
                calldisp.uploadFile(username, password, componenttype, fname, base64, filename1, mainContext);


            }else if(componenttype.equalsIgnoreCase("audio")||componenttype.equalsIgnoreCase("video"))
            {

                Log.i("FileUpload", "ELSE IF AUDIO||Video--->" );
                Log.i("FileUploadIM", "path" +filename1);
                String base64 = encodeAudioVideoToBase64(path);
                if(componenttype.equalsIgnoreCase("video"))
                    base64 = encodeAudioVideoToBase64(path+".mp4");

                String fname = filename1.split("/")[3];
                Log.i("FileUpload", "fname--->" + fname);
                Log.i("FileUpload", "uname--->" + username);
                Log.i("FileUpload", "password--->" + password);
                Log.i("FileUpload", "type--->" + componenttype);
                Log.i("FileUpload", "base64--->" + base64);
                if(componenttype.equalsIgnoreCase("video"))
                    calldisp.uploadFile(username, password, componenttype, fname+".mp4", base64,filename1,mainContext);
                else
                    calldisp.uploadFile(username, password, componenttype, fname, base64,filename1,mainContext);
            }
            ProgressDialog dialog = new ProgressDialog(mainContext);
            calldisp.showprogress(dialog, mainContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendFile(){
        calldisp.cancelDialog();
        ArrayList<String> b_list = new ArrayList<String>();
        b_list.addAll(SendbuddyList);
        Collections.sort(b_list);
        ArrayList<String> u_list = new ArrayList<String>();
        u_list.addAll(uploadDatas);
        calldisp.uploadData.add(filename1);
        uploadFilesList.add(filename1);
//        calldisp.sendshare(true, username, CallDispatcher.Password, buddieslist.getText()
//                        .toString(), b_list, u_list, componenttype, comments,
//                filename, "sendFiles", by_time, time_spinner, ttl_result,
//                ttl_value, time_input, stream_toggle.isChecked(), cbean);
    }

    private String encodeAudioVideoToBase64(String path){
        String strFile=null;
        File file=new File(path);
        try {
            FileInputStream file1=new FileInputStream(file);
            byte[] Bytearray=new byte[(int)file.length()];
            file1.read(Bytearray);
            strFile = Base64.encodeToString(Bytearray, Base64.NO_WRAP);//Convert byte array into string

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("FileUpload", "audioVideoEncode========" + strFile);
        return strFile;
    }
    private String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }


}
