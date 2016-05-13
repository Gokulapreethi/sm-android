package com.base.sipclientwrapper;

import android.util.Log;

import com.base.android.pjsua.PjsuaInterface;
import com.base.android.pjsua.pjsua;

import java.nio.charset.Charset;


public class SipClientWrapper {

    PjsuaInterface pjIns = null;

    private pjsua pjsuaInstace = null;

    public SipClientWrapper() {
        if (pjsuaInstace == null)
            pjsuaInstace = new pjsua();

    }

    public void setListener(PjsuaInterface ins) {
        Log.i("droid123", "came to set listener" + ins);
        pjsuaInstace.setWrapperListner(ins);
    }

    public PjsuaInterface getListener() {
        return pjsuaInstace.getWrapperListener();
    }

    public void Init(String proxy) {
        pjsuaInstace.init(proxy);
    }

    public int add_account(String sip_user, String sip_passwd,
                           String sip_realm, String sip_registar, String sip_proxy) {
        return pjsuaInstace.add_account(sip_user, sip_passwd, sip_realm,
                sip_registar, sip_proxy);
    }

    public int acc_get_default() {
        return pjsuaInstace.acc_get_default();
    }

    public int make_call(int acc_id, String uri) {
        try {
            Log.i("droid123", "====>in make_call acc_id and uri is" + " " + acc_id + " " + uri);
            String endPoint = uri;
            Charset.forName("UTF-8").encode(endPoint);
            return pjsuaInstace.make_call(acc_id, endPoint);
        }catch (Exception e){
            Log.d("droid123","Error SCW => "+e.toString());
            return 0;
        }
    }

    public int answer_call(int call_id) {
        return pjsuaInstace.answer_call(call_id);
    }

    public int reject_call(int call_id) {
        return pjsuaInstace.reject_call(call_id);
    }

    public int hold_call(int call_id) {
        return pjsuaInstace.hold_call(call_id);
    }



    public int unhold_call(int call_id, int swap_callid) {
        return pjsuaInstace.unhold_call(call_id, swap_callid);
    }

    public int delete_accId(int acc_id) {
        return pjsuaInstace.delete_accId(acc_id);
    }

    public int hangup_call(int call_id) {
        return pjsuaInstace.hangup_call(call_id);
    }

    public void Reregister(int acc_id) {
        pjsuaInstace.Reregister(acc_id);
    }

    public void Unregister(int acc_id) {
        pjsuaInstace.Unregister(acc_id);
    }

    public int MuteMic(int call_id) {
        return pjsuaInstace.MuteMic(call_id);
    }

    public int UnMuteMic(int call_id) {
        return pjsuaInstace.UnMuteMic(call_id);
    }

    public int ToggleSpeaker(int isSpeakeron) {
        return pjsuaInstace.ToggleSpeaker(isSpeakeron);
    }

    public boolean MuteMicReceiver(int call_id) {
        return pjsuaInstace.MuteMicReceiver(call_id);
    }

    public boolean UnMuteMicReceiver(int call_id) {
        return pjsuaInstace.UnMuteMicReceiver(call_id);
    }

    public void toggleDeviceMicMute(boolean isMute) {
        pjsuaInstace.toggleDeviceMicMute(isMute);
    }

    public boolean holdSession(int swap_callid) {
        return pjsuaInstace.holdSession(swap_callid);
    }

    public boolean unHoldSession(int swap_callid) {
        return pjsuaInstace.unHoldSession(swap_callid);
    }

    public void callTobroadcastMode(boolean isBroadcastmode) {
        pjsuaInstace.callTobroadcastMode(isBroadcastmode);
    }

    public void checkBroadcastMode(int call_id) {
        pjsuaInstace.checkBroadcastMode(call_id);
    }

    public void hangupall() {
        pjsuaInstace.hangupall();
    }

    public void destroy() {
        pjsuaInstace.destroy();
    }

    public void setPriority(int[] array) {
        pjsuaInstace.setpriority(array);
    }

    public void enableSRTP(int id, int flag, int secure_signalling) {
        pjsuaInstace.enablesrtp(id, flag, secure_signalling);
    }

    public void setrecordfilePath(String path, int length) {
        pjsuaInstace.setfiletorecord(path, length);
    }

    public void startRecording(int call_id) {
        pjsuaInstace.startrecording(call_id);
    }

    public void stopRecording() {
        pjsuaInstace.stoprecording();
    }

   
}
