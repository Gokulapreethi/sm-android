package com.base.android.pjsua;

public interface PjsuaInterface {

    void didReceiveOnCallState(String remoteinfo, String sessionid,
                               int call_id, int call_status);

    void didReceiveCallConnected(String remoteinfo,
                                 String call_sessionid, int call_id);

    void didReceiveCallDisconnected(String remoteinfo, String sessionid,
                                    int call_id, int call_status);

    void didReceiveCallReplace(int callid, String sipinfo, String fromuser, String rhvalue);

    void notifyRegistration(int acc_id, boolean isStatus,
                            boolean isUpdate, int errCode, String errReason, String sipReason);

    void didReceiveIncomingCall(int callid, String remoteinfo);

    void didReceiveIncomingJoinCall(int callid, String remoteinfo, String sipinfo);

    void onMessageReceived(int call_id, String from, String to,
                           String contact, String mime_type, String text);

    void on_referMessageReceived(int call_id, String remoteinfo);

    void on_call_media(int callid);

    void notifyMediaState(int call_id, String touser_no, boolean status);
}
