package com.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.cg.hostedconf.AppReference;
import com.views.FloatingImage;
import com.views.FloatingIncomingImage;
import com.views.FloatingOutgoingImage;


public class FloatingCallService extends Service implements FloatingImage.CallButtonCallBack, FloatingIncomingImage.CallButtonCallBack, FloatingOutgoingImage.CallButtonCallBack {

    int selected_view = 0;
    FloatingImage floatingImage;
    String current_callscrren;
    FloatingIncomingImage floatingIncomingImage;
    FloatingOutgoingImage floatingOutgoingImage;
    private boolean newly_started = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (newly_started) {
            newly_started = false;
            selected_view = intent.getIntExtra("sview", 0);
            if (selected_view == 0) {
                floatingIncomingImage = new FloatingIncomingImage(this);
                floatingIncomingImage.setCallbacks(this);
            } else if (selected_view == 1) {
                floatingOutgoingImage = new FloatingOutgoingImage(this);
                floatingOutgoingImage.setCallbacks(this);
            } else if (selected_view == 2) {
                current_callscrren = intent.getStringExtra("callscreen");
                floatingImage = new FloatingImage(this);
                floatingImage.setCallbacks(this);
            }
        }
        return START_NOT_STICKY;
    }

    /**
     * @param intent
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.i("Float", "onCreater");
        newly_started = true;
//		floatingImage = new FloatingImage(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingImage != null) {
            floatingImage.destroy(); //  now its an efficient way to destroy the imageView
        }

        if (floatingIncomingImage != null) {
            floatingIncomingImage.destroy();
        }

        if (floatingOutgoingImage != null) {
            floatingOutgoingImage.destroy();
        }
    }

    @Override
    public void buttonClickState() {
        AppReference.mainContext.buttonClickState("callscreen", current_callscrren);
    }

    @Override
    public void incomingImageButtonClickState() {
        AppReference.mainContext.buttonClickState("incomingalert", "");
    }

    @Override
    public void outgoingImageButtonClickState() {
        AppReference.mainContext.buttonClickState("connecting", "");
    }

    public interface CallButtonCallBack {

        public void buttonClickState(String current_screen, String callscreen);
    }
}
