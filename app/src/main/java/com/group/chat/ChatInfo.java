package com.group.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bean.GroupChatBean;
import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.main.ContactsFragment;

import org.lib.model.BuddyInformationBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by  on 04-03-2016.
 */
public class ChatInfo extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chatinfo);
        try {
            Button cancel = (Button) findViewById(R.id.cancel);
            TextView msgView = (TextView) findViewById(R.id.msgView);
            TextView sentView = (TextView) findViewById(R.id.sentView);
            TextView tvdate = (TextView) findViewById(R.id.tvdate);
            TextView tvread = (TextView) findViewById(R.id.tvread);
            TextView tvdelivered = (TextView) findViewById(R.id.tvdelivered);
            GroupChatBean chatList = (GroupChatBean) getIntent().getSerializableExtra("chatlistindividual");
            msgView.setText(chatList.getMessage());
            if(chatList!=null && chatList.getMediaName()!=null){
                if(chatList.getMediaName().contains("COMMedia")){
                    String allFiles_name=null;
                    if(chatList.getMediaName().contains(",")){
                        String multi_name[]=chatList.getMediaName().split(",");
                        for(int i=0;i<multi_name.length;i++){
                            String filename = multi_name[i].split("COMMedia/")[1];
                            if (filename != null && !filename.equalsIgnoreCase("null")) {
                                if (allFiles_name == null) {
                                    allFiles_name = filename;
                                } else {
                                    allFiles_name = allFiles_name + "\n" + filename;
                                }
                            }

                        }

                    }else {
                        String filename = chatList.getMediaName().split("COMMedia/")[1];
                        if (filename != null && !filename.equalsIgnoreCase("null")) {
                            allFiles_name=filename;
                        }
                    }
                    if(chatList.getMessage()!=null && !chatList.getMessage().equalsIgnoreCase("null")&&allFiles_name!=null){
                        msgView.setText(chatList.getMessage()+"\n"+allFiles_name);
                    }else{
                        if(allFiles_name!=null) {
                            msgView.setText(allFiles_name);
                        }
                    }


                }
            }
            sentView.setText("Sent:");
//        tvdate.setText(chatList.getSenttime());
            tvread.setText(Html.fromHtml("<font color=\"#FFFFFF\">"
                    + "READ "
                    + "</font>"
                    + "  "
                    + "<font color=\"#2789e4\">  1"));
            tvdelivered.setText(Html.fromHtml("<font color=\"#FFFFFF\">"
                    + "DELIVERED "
                    + "</font>"
                    + "  "
                    + "<font color=\"#2789e4\">  1"));
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            String dateInString = chatList.getSenttime();
            SimpleDateFormat fmt2 = new SimpleDateFormat("MM:dd:yyyy hh:mm a");
            try {
                Date date = fmt.parse(dateInString);
                tvdate.setText(fmt2.format(date));
            } catch (Exception pe) {

            }
            if (chatList.getCategory().equalsIgnoreCase("g")) {
                ArrayList<ChatInfoBean> ChatInfoMap = new ArrayList<ChatInfoBean>();
                ChatInfoMap = DBAccess.getdbHeler(getApplicationContext()).ChatInfoMembers(chatList.getSignalid());

                Vector<ChatInfoBean> chat = new Vector<ChatInfoBean>();
                Vector<ChatInfoBean> deliveredchat = new Vector<ChatInfoBean>();

                for(ChatInfoBean b:ChatInfoMap)
                {
                    ProfileBean profileBean = DBAccess.getdbHeler().getProfileDetails(b.getName());
                    b.setName(profileBean.getFirstname()+" "+profileBean.getLastname());
                    Log.d("username","string"+b.getName());
                    if(b.getStatus().equalsIgnoreCase("3"))
                    {

                        chat.add(b);
                    }
                    deliveredchat.add(b);
                }
                ListView readList = (ListView) findViewById(R.id.read);
                ChatInfoAdapter adapter = new ChatInfoAdapter(ChatInfo.this, chat);

                ListView deliveredList = (ListView) findViewById(R.id.deliverd);
                ChatInfoAdapter deliveredListadapter = new ChatInfoAdapter(ChatInfo.this, deliveredchat);
                readList.setAdapter(adapter);
                deliveredList.setAdapter(deliveredListadapter);
                tvread.setText(Html.fromHtml("<font color=\"#FFFFFF\">"
                        + "READ "
                        + "</font>"
                        + "  "
                        + "<font color=\"#2789e4\">  "+chat.size()));
                tvdelivered.setText(Html.fromHtml("<font color=\"#FFFFFF\">"
                        + "DELIVERED "
                        + "</font>"
                        + "  "
                        + "<font color=\"#2789e4\">  "+deliveredchat.size()));

            } else {
                ArrayList<ChatInfoBean> ChatInfoMap = new ArrayList<ChatInfoBean>();
                ChatInfoMap = DBAccess.getdbHeler(getApplicationContext()).ChatInfoMembers(chatList.getSignalid());
                Vector<ChatInfoBean> chat = new Vector<ChatInfoBean>();
                Vector<ChatInfoBean> deliveredchat = new Vector<ChatInfoBean>();
                for(ChatInfoBean b:ChatInfoMap)
                {
                    ProfileBean profileBean = DBAccess.getdbHeler().getProfileDetails(b.getName());
                    b.setName(profileBean.getFirstname()+" "+profileBean.getLastname());
                    Log.d("username","stringe"+b.getName());
                    if(b.getStatus().equalsIgnoreCase("3"))
                    {
                        chat.add(b);
                    }
                    deliveredchat.add(b);
                }
                ListView readList = (ListView) findViewById(R.id.read);
                ChatInfoAdapter adapter = new ChatInfoAdapter(ChatInfo.this, chat);
                readList.setAdapter(adapter);
                ListView deliveredList = (ListView) findViewById(R.id.deliverd);
                ChatInfoAdapter deliveredListadapter = new ChatInfoAdapter(ChatInfo.this, deliveredchat);
                deliveredList.setAdapter(deliveredListadapter);
                tvread.setText(Html.fromHtml("<font color=\"#FFFFFF\">"
                        + "READ "
                        + "</font>"
                        + "  "
                        + "<font color=\"#2789e4\">  "+chat.size()));
                tvdelivered.setText(Html.fromHtml("<font color=\"#FFFFFF\">"
                        + "DELIVERED "
                        + "</font>"
                        + "  "
                        + "<font color=\"#2789e4\">  " + deliveredchat.size()));
            }
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
