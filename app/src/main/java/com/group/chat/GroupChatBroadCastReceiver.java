package com.group.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bean.GroupChatBean;
import com.cg.commonclass.CallDispatcher;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class GroupChatBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // TODO Auto-generated method stub
            Log.i("BroadCast", "inside GroupChatBroadCastReceiver");
            if (CallDispatcher.LoginUser != null) {
                final GroupChatBean gcBean = (GroupChatBean) intent
                        .getSerializableExtra("groupchat");
                String parent_id = intent.getStringExtra("parent_id");

                Log.d("insidebroadcast", "Parent id 1 :" + parent_id);
                if(parent_id != null) {
                    gcBean.setParentId(parent_id);
                    Log.d("insidebroadcast", "Parent id 2 :" + gcBean.getParentId());
                    // String parentId = intent.getStringExtra("parentid");
                    //	Bundle bun = intent.getExtras().getBundle("bun");
                    //	String parentId = bun.getString("pId");

                    AppMainActivity appMain = (AppMainActivity) SingleInstance.contextTable
                            .get("MAIN");
                    if (appMain != null && gcBean != null) {
                        appMain.notifyScheduleAndDeadLineMsg(gcBean,
                                gcBean.getParentId());
                    }
                    if (gcBean.getSubCategory().equalsIgnoreCase("gs"))
                        SingleInstance.scheduledMsg.remove(gcBean.getSignalid());
                    else if (gcBean.getSubCategory().equalsIgnoreCase("gd"))
                        SingleInstance.deadLineMsgCount.remove(gcBean.getGroupId());

                }

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
