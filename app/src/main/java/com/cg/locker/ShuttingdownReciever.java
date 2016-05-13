package com.cg.locker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created on 30-09-15.
 */
public class ShuttingdownReciever extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if(intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")) {
                Log.d("ABCD", "the phone is switched off");
                File f = new File(Environment.getExternalStorageDirectory()+"/COMMedia");
                if(f.exists()) {
                    Log.d("ABCD", "Folder Exist and attempting to delete commedia");
                    f.delete();
                    if (f.isDirectory())
                    {
                        String[] children = f.list();
                        for (int i = 0; i < children.length; i++)
                        {
                            Log.d("ABCD", "All Files "+new File(f, children[i]).getName());
                            new File(f, children[i]).delete();
                        }
                    }
                }
            }
        }catch(Exception e){
            Log.d("ABCD","Error ==> "+e.toString());
        }
    }
}
