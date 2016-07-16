package com.cg.rounding;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.group.chat.GroupChatActivity;
import com.main.AppMainActivity;
import com.util.SingleInstance;

/**
 * Created by jansi on 6/21/2016.
 */
public class ScheduleManager extends BroadcastReceiver {
    public static Context mainContext;
    private String Pateintname = null;
    public void setContext(Context cxt) {

        this.mainContext = cxt;
    }

    public ScheduleManager() {

        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("timevalue","enterintomanagerclass");
            GroupChatActivity groupChatActivity =(GroupChatActivity)SingleInstance.contextTable.get("groupchat");
            if(groupChatActivity != null) {
                context=groupChatActivity;
            }else
            context=SingleInstance.mainContext;
        String patientname=intent.getStringExtra("patientname");
        String Duedate = intent.getStringExtra("Duedate");
        Log.d("patientname","value"+patientname);
        Log.d("patientname","duedate"+Duedate);
        showAlert1(
                "Task Reminder \n" + patientname, "Due date :" + Duedate,
                true,context);
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }


    public void showAlert1(String title, String message, final boolean isLogout,Context context) {
        AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
        alertCall.setTitle(title).setMessage(message).setCancelable(false)
                .setNegativeButton("DONE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
//                            if (isLogout) {
//                                appMainActivity.showprogress();
//                                ((AppMainActivity) SingleInstance.contextTable
//                                        .get("MAIN")).logout(true);
//                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
        alertCall.show();
    }
}

