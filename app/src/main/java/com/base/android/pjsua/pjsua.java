package com.base.android.pjsua;

import android.util.Log;

public class pjsua {

    void loadLibs() {
        try {
            System.loadLibrary("crypto");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            System.loadLibrary("ssl");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            System.loadLibrary("pjsip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public pjsua() {
        loadLibs();
    }

	static PjsuaInterface pjsuaInterface1 = null;


    public native int init(String proxy);

    public native int add_account(String sip_user, String sip_passwd,
                                  String sip_realm, String sip_registar, String sip_proxy);

    public native int acc_get_default();

    public native int make_call(int acc_id, String uri);

    public native int transfer_call(int transfer_call_id, int[] dest_call_id);

    public native int answer_call(int call_id);

    public native int reject_call(int call_id);

    public native int hold_call(int call_id);

    public native int pstn_play(int call_id);

    public native int createPstnPlayer(String path);

    public native int destroyPstnPlayer();

    public native int unhold_call(int call_id, int swap_call_id);

    public native int pstn_stop(int call_id);

    public native int delete_accId(int acc_id);

    public native int hangup_call(int call_id);

    public native void Reregister(int acc_id);

    public native void Unregister(int acc_id);

    public native void playHoldTone();

    public native void stopHoldTone();

    public native int MuteMic(int call_id);

    public native int UnMuteMic(int call_id);

    public native boolean MuteMicReceiver(int call_id);

    public native boolean UnMuteMicReceiver(int call_id);

    public native void toggleDeviceMicMute(boolean isMute);

    public native boolean holdSession(int swap_callid);

    public native boolean unHoldSession(int swap_callid);

    public native void callTobroadcastMode(boolean isBroadcastmode);

    public native void checkBroadcastMode(int call_id);

    public native int ToggleSpeaker(int isSpeakeron);

    public native void hangupall();

    public native void destroy();

    public native void setpriority(int[] array);

    public native void enablesrtp(int acc_id, int flag, int secure_signalling);

    public native void setfiletorecord(String flename, int len);

    public native void startrecording(int cid);

    public native void stoprecording();

    public native void setRecordCallMusic(int[] call_ids);

    public native void setwhispermodeforcallid(int call_id, int flag);

    public native void setwhispermodeforparticipant(int[] call_id, int[] tot_whis_callids, int flag, int containsHost, int removed_callid);

    public native void connectline(int[] callids,int[] connect_callids,int flag);

    public native void setwhispermusic(int[] call_ids, int containsHost, int removed_id);

    public native void sendmessage(String xml);

    public native int bridgemycall(int callid);

    public native int SwapLines(int callid, int toggle, int devicemicmuteon,
                                int swap_id, int needtodisconnect);

    public native void sendresponsemessage(String response, int call_id);

    public native void SwapIndividualLine(int call_id, int swap_id);

    public native boolean checkMediaAlive(int call_id);

    public native void sendmessagetoreferBuddy(String response,
                                               String remote_info);

    public native int closeTransport(int acc_id);

    public native int createMusicPlayer(String path);

    public native int destroyMusicPlayer();

    public native int createCallRecordingPlayer(String path);

    public native int destroyCallRecordingPlayer();

    public native int createInboundCallPlayer(String path);

    public native int destroyInboundCallMusicPlayer();

    public native int createWhisperPlayer(String path);

    public native int destroyWhisperPlayer();

    public native int answerInboundcall(int call_id);

    public native int unholdinboundcall(int call_id, int swap_callid);

    public native int sendDTMFDigit(int call_id, String dtmfchar);

    final int PJSIP_INV_STATE_NULL = 0;
    /**
     * < Before INVITE is sent or received
     */
    final int PJSIP_INV_STATE_CALLING = 1;
    /**
     * < After INVITE is sent
     */
    final int PJSIP_INV_STATE_INCOMING = 2;
    /**
     * < After INVITE is received.
     */
    final int PJSIP_INV_STATE_EARLY = 3;
    /**
     * < After response with To tag.
     */
    final int PJSIP_INV_STATE_CONNECTING = 4;
    /**
     * < After 2xx is sent/received.
     */
    final int PJSIP_INV_STATE_CONFIRMED = 5;
    /**
     * < After ACK is sent/received.
     */
    final int PJSIP_INV_STATE_DISCONNECTED = 6;

    private String DTMFChar = "";

    private String Conferenceid;

    /**
     * < Session is terminated.
     */

    public void setWrapperListner(PjsuaInterface ins) {
        Log.i("droid123", "came to set listener 123 456" + ins);
        pjsuaInterface1 = ins;
        Log.i("droid123", "came to set listener 123 456" + pjsuaInterface1);
    }

    public PjsuaInterface getWrapperListener() {
        return pjsua.pjsuaInterface1;
    }

    public void on_incoming_call(int callid, final String remoteinfo) {

        Log.i("droid123", "on_incoming_call : Callid = " + callid
                + ", RemoteInfo = " + remoteinfo);
        pjsuaInterface1.didReceiveIncomingCall(callid, remoteinfo);
        

    }

    public void on_call_transfer_request(int call_id, String remoteInfo) {

       

    }


    public void on_incomingjoin_call(int callid, final String remoteinfo,
                                     final String sipinfo) {

        Log.i("droid123", "on_incomingjoin_call : Callid = " + callid
                + ", RemoteInfo = " + remoteinfo + "Sip info " + sipinfo);
        pjsuaInterface1.didReceiveIncomingJoinCall(callid, remoteinfo, sipinfo);

    }


    public void on_call_replace_request(int callid, String sipinfo,
                                        String fromuser, String rhvalue) {

        Log.i("droid123", "on_call_replace_request : Callid = " + callid
                + ", fromuser = " + fromuser + "Sip info " + sipinfo + "ReferheaderValues " + rhvalue);
        pjsuaInterface1.didReceiveCallReplace(callid, sipinfo, fromuser, rhvalue);

    }


//    public void notifyTransportState(int status) {
//        Log.i("call12", "notifyTransportState status is----->" + status);
//    }

    public void on_refermessagereceived(int call_id, String remoteinfo) {
        Log.i("call12", "on refer mesage received");

        Log.i("call12", "on refer mesage received" + pjsuaInterface1);
        pjsuaInterface1.on_referMessageReceived(call_id, remoteinfo);
    }

    public void on_call_media(int call_id) {
      
    }

    public void on_call_sdp_created(int call_id) {
       
    }

    private String GetCurrentConferenceID() {

        return "a";

    }

    private boolean GetIsJoinCall() {
		return false;

      

    }

    // public int makeCallWithJoinParameter(int acc_id, String uri){
    // AppReferences.IsJoinCall = true;
    // Log.d("myid", "=====>Inside makeCallwithJoinParameter");
    // return make_call(acc_id, uri);
    // }

    public void on_pager(int call_id, String from, String to, String contact,
                         String mime_type, String text) {
        Log.i("call12", "on pager");
        Log.i("call12", "on pager received xml----->" + text);
        Log.i("call12", "on pager received call id----->" + call_id);
        Log.i("call12", "on pager received from----->" + from);

//		
    }

    public void on_pager_status(String msg, String to, int status, String reason) {
    	
    	
    }

    public void notifyRegistration(int acc_id, boolean isStatus,
                                   boolean isUpdate, int errCode, String errReason) {
    	Log.i("droid1234","notifyRegistration "+acc_id+" "+isStatus+" "+isUpdate+" "+errCode+" "+errReason+" ");
        pjsuaInterface1. notifyRegistration( acc_id,  isStatus,
         isUpdate,  errCode,  errReason,  "");
    	
    }

    public void notifyDTMFReceived(int call_id, int dtmf) {
    	
    	
    }

    public void didReceiveOnCallState(String remoteinfo, String sessionid,
                                      int call_id, final int call_status, int call_state) {

        Log.d("droid123", "didReceiveOnCallState -->" + "Remote info-->"
                + remoteinfo + "--call_id-->" + call_id + "call_status"
                + call_status);

        pjsuaInterface1.didReceiveOnCallState(remoteinfo, sessionid, call_id,
                call_status);

        switch (call_state) {
            case PJSIP_INV_STATE_NULL:
                break;
            case PJSIP_INV_STATE_CALLING:
                break;
            case PJSIP_INV_STATE_INCOMING:
                break;
            case PJSIP_INV_STATE_EARLY:
                break;
            case PJSIP_INV_STATE_CONNECTING:
                break;
            case PJSIP_INV_STATE_CONFIRMED: {
                Log.d("droid123", "pjsua didReceiveCallConnected --> " + "\n remoteinfo"
                        + remoteinfo + " call_sessionid-->" + sessionid
                        + " call_id-->" + call_id);
                pjsuaInterface1.didReceiveCallConnected(remoteinfo, sessionid,
                        call_id);
            }
            break;
            case PJSIP_INV_STATE_DISCONNECTED: {
                Log.d("droid123", "pjsua didReceiveCallDisconnected -->"
                        + "\n Remote info-->" + remoteinfo + " call_id-->" + call_id
                        + " call_status" + call_status);
                pjsuaInterface1.didReceiveCallDisconnected(remoteinfo, sessionid,
                        call_id, call_status);
            }
            break;

            default:
                break;
        }

    }

    public void didReceiveCallConnected(String remoteinfo,
                                        String call_sessionid, int call_id) {
        pjsuaInterface1.didReceiveCallConnected(remoteinfo, call_sessionid,
                call_id);

        Log.d("droid123", "didReceiveCallConnected --> remoteinfo" + remoteinfo
                + "call_sessionid-->" + call_sessionid + "call_id-->" + call_id);

    }

    public void didReceiveCallDisconnected(String remoteinfo, String sessionid,
                                           int call_id, final int call_status) {
        Log.w("droid123", "didReceiveCallDisconnected *****-->"
                + "Remote info-->" + remoteinfo + "--call_id-->" + call_id
                + "call_status" + call_status);
        pjsuaInterface1.didReceiveCallDisconnected(remoteinfo, sessionid,
                call_id, call_status);
    }

}
