package com.cg.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.app.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by jansi on 5/3/2016.
 */
public class MyAlarmService extends BroadcastReceiver {
    public MyAlarmService() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();
//        Log.d("schedulemanager", "-----schedule Time class  1");
//        AppReferences.printLog("schedulemanager",
//                "Schedule Alarm Manager Received", "INFO", null);
//        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
//        String tm = time.format(new Date());
//
//
////        SimpleDateFormat temp = new SimpleDateFormat("HH:mm:ss");
////        String tep_time = temp.format(new Date());
////        Log.i("schedulemanager", "tep_time Is====>" + tep_time);
//
//        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
//        String dt = date.format(new Date());
//
//        if (AppReferences.mainContext != null) {
//            Log.i("schedulemanager", "Now Tome Is====>" + tm);
//            Log.i("schedulemanager", "Now date Is====>" + dt);
//            AppReferences.printLog("schedulecall", "Alarm manager device time-->"+tm,"DEBUG", null);
//            AppReferences.printLog("schedulecall", "Alarm manager device date--->"+dt,"DEBUG", null);
//
////   ArrayList<SheduleCallbean> group = DatabaseHelper.getDB(
////     ((HomeFragment) AppReferences.contextTable.get("MAIN"))
////       .getCurrentContext())
////     .selectSheduledCallDetailsOntime(dt, tm);
//            DatabaseHelper.getDB(AppReferences.mainContext).openDatabase();
//            String hour = tm.split(":")[0];
//            Log.i("schedulemanager", "hour time--->" + hour + "  hour length--->" + hour.length());
//            if (hour.startsWith("0")) {
//                StringBuffer buffer = new StringBuffer(tm);
//                tm = buffer.deleteCharAt(0).toString();
//            }
//            Log.i("schedulemanager", "Remove 0 time--->" + tm);
//            AppReferences.printLog("schedulemanager", "Remove 0 time--->"+tm,"DEBUG", null);
//            ArrayList<SheduleCallbean> group = DatabaseHelper.getDB(
//                    AppReferences.mainContext).selectSheduledCallDetailsOntime(dt, tm);
//            Log.d("schedulemanager", "-----group size---->" + group.size());
//            if (group.size() > 0) {
//                AppReferences.printLog("schedulemanager", "-----schedule call group.size>0 and group size-->"+group.size(),"DEBUG", null);
//                Log.d("schedulemanager", "-----schedule call group.size>0");
////                for (int i = 0; i < group.size(); i++) {
//                final SheduleCallbean callbean = group.get(0);
//
////                    String startTime = callbean.getStartTime();
////                    AppReferences.printLog("schedulemanager", "ScheduleTimer Database time-->"+startTime,"DEBUG", null);
////                    if (startTime.equalsIgnoreCase(tm)) {
////                        AppReferences.printLog("schedulemanager", "ScheduleTimer DB time and Device time same","DEBUG", null);
//                if (AppReferences.contextTable.containsKey("MAIN")) {
////       ((HomeFragment) AppReferences.contextTable
////         .get("MAIN")).ShowSheduleScreen(
////         ((HomeFragment) AppReferences.contextTable
////           .get("MAIN")).getCurrentContext(),
////         callbean);
//                    AppReferences.printLog("schedulemanager", "AppReferences.contextTable.containsKey(MAIN)","DEBUG", null);
//                    Log.d("schedulemanager", "AppReferences.contextTable.containsKey(MAIN)");
//                    AppReferences.mainContext.ShowSheduleScreen(((HomeFragment) AppReferences.contextTable
//                                    .get("MAIN")).getCurrentContext(),
//                            callbean);
//
//                }
//
////                    }
//
////                }
//            }
//        }
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }
}