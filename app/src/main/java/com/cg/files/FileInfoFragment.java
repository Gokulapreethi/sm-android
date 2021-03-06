package com.cg.files;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.group.chat.ForwardUserSelect;
import com.group.chat.GroupChatActivity;
import com.image.utils.FileImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.FilesFragment;
import com.util.FullScreenImage;
import com.util.SingleInstance;
import com.util.VideoPlayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    ProgressDialog progress;
    private String componenttype;
    private ArrayList<String> uploadFilesList = new ArrayList<String>();
    private String filename1;
    CallDispatcher calldisp = new CallDispatcher(SingleInstance.mainContext);
    private boolean isFromChat=false,isGroup=false;
    private String groupid;
    View view_info, view_comments, view_access;
    ImageView info_img,comments_img,access_img;
    TextView tv_info,tv_comments,tv_access;
    public FilesAdapter filesAdapter = null;
    private FilesFragment filesFragment = null;


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
                    if (isFromChat) {
                        Intent intent = new Intent(mainContext, GroupChatActivity.class);
                        if (isGroup) {
                            intent.putExtra("isGroup", true);
                            intent.putExtra("groupid", groupid);
                        } else {
                            intent.putExtra("isGroup", false);
                            intent.putExtra("buddy", groupid);
                        }
                        intent.putExtra("isReq", "F");
                        startActivity(intent);
                        ContactsFragment contactsFragment = ContactsFragment.getInstance(mainContext);
                        FragmentManager fragmentManager = SingleInstance.mainContext
                                .getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(
                                R.id.activity_main_content_fragment, contactsFragment)
                                .commitAllowingStateLoss();
                    } else {
                        FilesFragment filesFragment = FilesFragment.newInstance(mainContext);
                        FragmentManager fragmentManager = SingleInstance.mainContext
                                .getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(
                                R.id.activity_main_content_fragment, filesFragment)
                                .commitAllowingStateLoss();
                    }
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentComponent = new Intent(mainContext,
                            ComponentCreator.class);
                    Bundle bndl = new Bundle();
                    bndl.putString("type", "note");
                    bndl.putBoolean("action", false);
                    bndl.putBoolean("fromNew",false);
                    intentComponent.putExtra("viewBean", cbean);
                    intentComponent.putExtras(bndl);
                    mainContext.startActivity(intentComponent);
                }
            });
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(mainContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.group_dialog);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.horizontalMargin = 15;
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    window.setAttributes(lp);
                    window.setGravity(Gravity.BOTTOM);
                    dialog.show();

                    TextView save = (TextView) dialog.findViewById(R.id.edit_grp);
                    save.setVisibility(View.VISIBLE);
                    save.setText("Save as");
                    TextView download = (TextView) dialog.findViewById(R.id.invite_grp);
                    download.setText("Download");
                    TextView move = (TextView) dialog.findViewById(R.id.leave_grp);
                    move.setText("Move to");
                    move.setBackgroundColor(getResources().getColor(R.color.blue2));
                    TextView delete = (TextView) dialog.findViewById(R.id.delete_grp);
                    delete.setText("Delete");
                    TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            try {
                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    save.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            try {
                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    move.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            final Dialog dialog = new Dialog(mainContext);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.callrecord_delete_dialog);
                            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                            dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
                            dialog.show();
                            TextView tv_SecondLine = (TextView)dialog.findViewById(R.id.tv_SecondLine);
                            tv_SecondLine.setText("You are going to delete \n 1 file from SnaxBox");
                            TextView tv_thirdline = (TextView)dialog.findViewById(R.id.tv_thirdline);
                            tv_thirdline.setVisibility(View.VISIBLE);
                            TextView tv_fourthline = (TextView)dialog.findViewById(R.id.tv_fourthline);
                            tv_fourthline.setVisibility(View.VISIBLE);
                            Button cancel = (Button) dialog.findViewById(R.id.save);
                            Button delete = (Button) dialog.findViewById(R.id.delete);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   try {
                                    String strDeleteQry = "delete from component where componentid="
                                            + cbean.getComponentId();

                                    if (calldisp.getdbHeler(mainContext).ExecuteQuery(
                                            strDeleteQry)) {
//                                        filesList.remove(cBean);
                                        if (cbean.getContentpath() != null
                                                && cbean.getContentpath() != "") {
                                            if (!cbean.getcomponentType()
                                                    .equalsIgnoreCase("video")) {

                                                File f = new File(cbean
                                                        .getContentpath());
                                                if (f.exists()) {
                                                    f.delete();
                                                }

                                            } else if (cbean.getcomponentType()
                                                    .equalsIgnoreCase("video")) {

                                                File f = new File(cbean
                                                        .getContentpath() + ".mp4");
                                                if (f.exists()) {
                                                    f.delete();
                                                }
                                                File f1 = new File(cbean
                                                        .getContentpath() + ".jpg");
                                                if (f1.exists()) {
                                                    f1.delete();
                                                }

                                            }
                                        }
                                    }
                                       FilesFragment filesFragment = FilesFragment.newInstance(mainContext);
                                       FragmentManager fragmentManager = SingleInstance.mainContext
                                               .getSupportFragmentManager();
                                       fragmentManager.beginTransaction().replace(
                                               R.id.activity_main_content_fragment, filesFragment)
                                               .commitAllowingStateLoss();
                                } catch (Exception e) {
                                    Log.i("callhistory", "" + e.getMessage());
                                    if (AppReference.isWriteInFile)
                                        AppReference.logger.error(e.getMessage(), e);

                                }

                                if (WebServiceReferences.contextTable
                                        .containsKey("Component")) {
                                    ((ComponentCreator) WebServiceReferences.contextTable
                                            .get("Component")).finish();
                                }
                                    notifyUI();
                                    dialog.dismiss();
                                }
                            });
                        }

                    });

                }
            });
            view = null;
            if (view == null) {
                view = inflater.inflate(R.layout.file_info_fragment, null);
                try {
                    final LinearLayout content = (LinearLayout) view.findViewById(R.id.content);
                    content.removeAllViews();
                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v1 = layoutInflater.inflate(R.layout.file_info, content);
                    final LinearLayout info = (LinearLayout) view.findViewById(R.id.chat);
                    final LinearLayout comments = (LinearLayout) view.findViewById(R.id.profilechat);
                    final LinearLayout access = (LinearLayout) view.findViewById(R.id.snazbox_chat);
                    TextView modofieddate=(TextView)view.findViewById(R.id.modifieddate);
                    TextView size=(TextView)view.findViewById(R.id.size);
                    TextView available=(TextView)view.findViewById(R.id.available);
                    TextView type=(TextView)view.findViewById(R.id.type);
                    TextView filename=(TextView)view.findViewById(R.id.filename);
                    TextView filedesc=(TextView)view.findViewById(R.id.filedesc);
                    ImageView fileIcon=(ImageView)view.findViewById(R.id.newfile);
                    ImageView overlay=(ImageView)view.findViewById(R.id.overlay);
                    TextView tv_share = (TextView)view.findViewById(R.id.tv_share);
                    tv_send = (TextView)view.findViewById(R.id.tv_send);
                    view_comments=(View)view.findViewById(R.id.view_comment);
                    view_info=(View)view.findViewById(R.id.view_info);
                    view_access=(View)view.findViewById(R.id.view_access);
                    info_img=(ImageView)view.findViewById(R.id.info_img);
                    comments_img=(ImageView)view.findViewById(R.id.comment_img);
                    access_img=(ImageView)view.findViewById(R.id.access_img);
                    tv_info=(TextView)view.findViewById(R.id.tv_info);
                    tv_comments=(TextView)view.findViewById(R.id.tv_comment);
                    tv_access=(TextView)view.findViewById(R.id.tv_access);

                    info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setDefault();
                            info_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.dashboard_message_white));
                            tv_info.setTextColor(getResources().getColor(R.color.white));
                            view_info.setVisibility(View.VISIBLE);
                        }
                    });
                    comments.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setDefault();
                            comments_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_comments_white));
                            tv_comments.setTextColor(getResources().getColor(R.color.white));
                            view_comments.setVisibility(View.VISIBLE);
                        }
                    });
                    access.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setDefault();
                            access_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members_white));
                            tv_access.setTextColor(getResources().getColor(R.color.white));
                            view_access.setVisibility(View.VISIBLE);
                        }
                    });

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
                        if(cbean.getDateAndTime()!=null) {
                            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                            Date d1 = new Date();
                            String[] month = cbean.getDateAndTime().split(" ");
                            String date = sdf.format(d1);
                            String[] time = month[1].split(":");
                            available.setText(date + " "+ time[0]+":"+time[1] +" "+month[2]);

                        }
                        if(cbean.getDateAndTime()!=null) {
                            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM");
                            Date d1 = new Date();
                            String[] month = cbean.getDateAndTime().split(" ");
                            d1 = dateformat.parse(month[0]);
                            String date = sdf.format(d1);
                            String[] time = month[1].split(":");
                            modofieddate.setText(date + " "+ time[0]+":"+time[1] +" "+month[2]);
                        }
                        if(cbean.getContentpath()!=null) {
                            size.setText(cbean.getcomponentType());
                            File file;
                            if(cbean.getcomponentType().equalsIgnoreCase("video"))
                                file = new File(cbean.getContentpath()+".mp4");
                            else
                                file = new File(cbean.getContentpath());
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
                            overlay.setVisibility(View.VISIBLE);
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
                            fileImageLoader.DisplayImage("", fileIcon, R.drawable.attachfile);
                        }
                        filename1 = cbean.getContentpath();
                        tv_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(mainContext, ForwardUserSelect.class);
                                intent.putExtra("fromfile",true);
                                startActivityForResult(intent, 112);

                            }
                        });
                        fileIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (cbean.getcomponentType().equalsIgnoreCase("video")) {
                                    Intent intent = new Intent(mainContext, VideoPlayer.class);
                                    intent.putExtra("video", cbean.getContentpath()+".mp4");
                                    mainContext.startActivity(intent);
                                } else if (cbean.getcomponentType().equalsIgnoreCase("photo")) {
                                    Intent intent = new Intent(mainContext, FullScreenImage.class);
                                    intent.putExtra("image", cbean.getContentpath());
                                    mainContext.startActivity(intent);
                                }else if(cbean.getcomponentType().equalsIgnoreCase("audio")){
                                    Intent intent = new Intent(mainContext, MultimediaUtils.class);
                                    intent.putExtra("filePath", cbean.getContentpath());
                                    intent.putExtra("requestCode", 4);
                                    intent.putExtra("action", "audio");
                                    intent.putExtra("createOrOpen", "open");
                                    startActivity(intent);
                                } else {
                                    Log.i("AAAA","openFilesinExternalApp");
                                    FilesFragment.openFilesinExternalApp(cbean.getContentpath());
                                }
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
    public void setFrom(boolean isfrom,String groupId,boolean isgroup) {
        isFromChat = isfrom;
        groupid=groupId;
        isGroup=isgroup;
    }
    public void notifyUI() {
        try {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
//                     filesAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Log.i("AAAA", "forawrd user " );
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 112) {
                Log.i("AAAA", "forawrd user code" );
                    String members = data.getStringExtra("SELECTED_MEMBERS");
                    String groupmembers = data.getStringExtra("SELECTED_GROUPS");
                    Log.i("AAAA", "forawrd user " + groupmembers + members);
                    if (members != null) {
                        String[] buddies = members.split(",");
                        for (String temp : buddies) {
                            SendbuddyList.add(temp);
                        }
                    }
                    if (groupmembers != null) {
                        String[] groupMembers = groupmembers.split(",");
                        for (String temp : groupMembers) {
                            SendbuddyList.add(temp);
                        }
                    }
                shareMultipleFiles();
                }
        } catch (Exception e) {

        }
    }

    private void setDefault() {
        info_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat));
        comments_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_comments));
        access_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members));
        tv_info.setTextColor(getResources().getColor(R.color.black));
        tv_comments.setTextColor(getResources().getColor(R.color.black));
        tv_access.setTextColor(getResources().getColor(R.color.black));
        view_info.setVisibility(View.GONE);
        view_comments.setVisibility(View.GONE);
        view_access.setVisibility(View.GONE);
    }

    private void shareMultipleFiles() {

        try {
            Log.i("AAAA", "forawrd user share" +
                    "");
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
                calldisp.uploadFile(username, password, componenttype, fname, base64, filename1, fileInfoFragment);


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
                    calldisp.uploadFile(username, password, componenttype, fname+".mp4", base64,filename1,fileInfoFragment);
                else
                    calldisp.uploadFile(username, password, componenttype, fname, base64,filename1,fileInfoFragment);
            }
            showprogress();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendFile(){
        Log.i("AAAA","send file info fragment");
        cancelDialog();
        ArrayList<String> b_list = new ArrayList<String>();
        b_list.addAll(SendbuddyList);
        Collections.sort(b_list);
        ArrayList<String> u_list = new ArrayList<String>();
        u_list.addAll(uploadDatas);
        calldisp.uploadData.add(filename1);
        uploadFilesList.add(filename1);
        calldisp.sendshare(true, CallDispatcher.LoginUser, CallDispatcher.Password, "",
                b_list, u_list, componenttype, cbean.getComment(),
                cbean.getContentpath(), "", null, null,null, null, null, true, cbean);
    }

    Handler handler = new Handler();
    public void showprogress() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if (progress == null) {
                        progress = new ProgressDialog(mainContext);
                        if (progress != null) {
                            progress.setCancelable(false);
                            progress.setMessage("File Uploading in Progress ...");
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progress.setProgress(0);
                            progress.setMax(100);
                            progress.show();
                        }
                    }

                } catch (Exception e) {
                    SingleInstance.printLog(null, e.getMessage(), null, e);
                }
            }

        });

    }

    public void cancelDialog() {
        try {
            if (progress != null) {
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
        }

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
