package com.thread;

import android.util.Log;

import com.base.android.pjsua.PjsuaInterface;
import com.base.sipclientwrapper.SipClientWrapper;
import com.cg.hostedconf.AppReference;
import com.main.AppMainActivity;

import java.nio.charset.Charset;

public class SipCommunicator extends Thread {

    private SipQueue contact_queue = null;
    private boolean isRunning = false;

    public enum sip_operation_types {

        REGISTER_ACCOUNT, SIGNOUT_ACCOUNT, MAKE_CALL, TRANSFER_CALL, HANGUP_SINGLE_CALL, PLAY_HOLDTONE, STOP_HOLDTONE, CREATE_CALLRECORD_PLAYER, DESTROY_CALLRECORD_PLAYER, PLAY_RECORDCALL_MUSIC, CREATE_PLAYER, DESTROY_PLAYER, CREATE_WHISPER_PLAYER, DESTROY_WHISPER_PLAYER, PLAY_WHISPER_MUSIC, STOP_WHISPER_MUSIC, HANGUP_ALL, MIC_MUTE, UN_MIC_MUTE, ANSWER_CALL, REJECT_CALL, HOLD_CALL, PSTN_PLAY, PSTN_STOP, CREATE_PSTN_PLAYER, DESTORY_PSTN_PLAYER, UN_HOLD_CALL, HOLD_SESSION, UNHOLD_SESSION, CONNECT_LINE, MUTE_MIC_RECEIVER, UNMUTE_MIC_RECEIVER, REREGISTER, LOAD_LIBS, TOGGLEDEVICE_MICMUTE, BROADCAST_MODE, RELOAD_LIBRARY, CHANGE_CODEC, ENABLE_SRTP, SET_RECORDFILE, START_RECORDING, STOP_RECORDING, ENABLE_WISHPERMODE, ENABLE_WISHPERMODE_PARTICIPANT, SEND_MESSAGE, SEND_KEEPALIVERESPONSE, BRIDGE_CALL, NOTIFY_MESSAGERECEIVED, SWAP_CALL, SWAPSINGLE_LINE, CHECKMEDIA_STATE, SENDMSSAGETO_REFERBUDDY, CLOSE_TRANSPORT, CREATE_INBOUNDPLAYER, DESTROY_INBOUNDPLAYER, ANSWER_INBOUNDCALL, UNHOLD_INBOUNDCALL, SEND_DTMF, ENDCALL_USER

    }

    public SipCommunicator(SipQueue thread_queue) {
        this.contact_queue = thread_queue;
    }

    public void setRnning(boolean hastorun) {
        this.isRunning = hastorun;
    }

    public void setCallBack(PjsuaInterface listener) {
        if (AppMainActivity.sipClientWrapper != null){
            AppMainActivity.sipClientWrapper.setListener(listener);
            Log.i("droid123","====> setCallBack method"+listener);
        }
        
        else
            Log.i("droid123", "............" + AppMainActivity.sipClientWrapper);

    }

    public SipClientWrapper getwrapperInstance() {
        return AppMainActivity.sipClientWrapper;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (isRunning) {
            try {

                CommunicationBean bean = (CommunicationBean) contact_queue
                        .getMsg();
                Log.i("droid123",
                        "came to run method----->" + bean.getoperationType());

                switch (bean.getoperationType()) {

                    case LOAD_LIBS:
                    	Log.i("droid123","===> Inside LOAD_LIBS");
                        checkisLibraryLoaded();
                        break;

                    case RELOAD_LIBRARY:
                        reloadLibrary();
                        break;
                    case REGISTER_ACCOUNT:
                        Log.i("droid123", "Registration -->");

                        reloadLibrary();
                        int stat = AppMainActivity.sipClientWrapper.add_account(
                                bean.getUsername(), bean.getPassword(),
                                bean.getRealm(), bean.getRegistrar(),
                                bean.getSipProxy());
                        Log.i("droid123", "Registration status-->" + stat);
                        AppReference.acc_id = stat;
                        break;
//                        Log.i("droid123", "Registration -->");
//                        reloadLibrary();
//                        String proxy = bean.getSipProxy();
//                        Charset.forName("UTF-8").encode(proxy);
//                        String realm = bean.getRealm();
//                        Charset.forName("UTF-8").encode(realm);
//                        String registrar = bean.getRegistrar();
//                        Charset.forName("UTF-8").encode(registrar);
//                        String password = bean.getPassword();
//                        Charset.forName("UTF-8").encode(password);
//                        String username = bean.getUsername();
//                        Charset.forName("UTF-8").encode(username);
//                        int stat = AppMainActivity.sipClientWrapper.add_account(
//                                username, password, realm, registrar, proxy);
//
//                        Log.i("droid123", "Registration status-->" + stat);
//                         AppReference.acc_id = stat;
//                        break;

                    case SIGNOUT_ACCOUNT:
                        Log.d("droid123","SC Signing out SIP ");
                        AppMainActivity.sipClientWrapper.delete_accId(bean.getAcc_id());
                        AppMainActivity.sipClientWrapper.destroy();
                        break;

                    case REREGISTER:
                        AppMainActivity.sipClientWrapper.Reregister(bean.getAcc_id());
                        break;

                    case ANSWER_CALL:
                        Log.i("entry", "----answer call------");
                        int status = AppMainActivity.sipClientWrapper.answer_call(bean
                                .getCall_id());
                        AppReference.mainContext.notifyAcceptReject(bean.getCall_id());

                        break;

                    case REJECT_CALL:
                        AppMainActivity.sipClientWrapper
                                .reject_call(bean.getCall_id());
//                        AppMainActivity.sipClientWrapper.UnMuteMic(bean.getCall_id());
//                        AppMainActivity.sipClientWrapper.UnMuteMicReceiver(bean.getCall_id());
                        break;

                    case HANGUP_SINGLE_CALL:
                        Log.i("droid123", "########## hangup call");
                        AppMainActivity.sipClientWrapper
                                .hangup_call(bean.getCall_id());
                        break;

                    case MUTE_MIC_RECEIVER:
                        AppMainActivity.sipClientWrapper.MuteMicReceiver(bean
                                .getCall_id());
                        break;

                    case UNMUTE_MIC_RECEIVER:
                        AppMainActivity.sipClientWrapper.UnMuteMicReceiver(bean
                                .getCall_id());
                        break;

                    case HOLD_CALL:
                        AppMainActivity.sipClientWrapper.hold_call(bean.getCall_id());
                        break;

                 
                    case TOGGLEDEVICE_MICMUTE:
                        AppMainActivity.sipClientWrapper.toggleDeviceMicMute(bean
                                .getFlag());
                        break;

                    case BROADCAST_MODE:
                        AppMainActivity.sipClientWrapper.callTobroadcastMode(bean
                                .getFlag());
                        break;

                    case MIC_MUTE:
                        AppMainActivity.sipClientWrapper.MuteMic(bean.getCall_id());
                        break;

                    case UN_MIC_MUTE:
                        AppMainActivity.sipClientWrapper.UnMuteMic(bean.getCall_id());
                        break;

                    case MAKE_CALL:
                        try {
                            Log.i("droid123", "acc_id--->" + bean.getAcc_id());
                            Log.i("droid123", "to_number--->" + bean.getSipEndpoint());
                            Log.i("droid123", "====>" + AppMainActivity.sipClientWrapper);
                            String endPoint = bean.getSipEndpoint();
                            int c_stat = AppMainActivity.sipClientWrapper.make_call(
                                    bean.getAcc_id(), endPoint);

                            Log.i("calltest", "call_status----->" + c_stat);
                        }catch(Exception e){
                            Log.i("droid123", "Error SC => " + e.toString());
                        }
                        break;


                    case HANGUP_ALL:
                        Log.i("droid123", "########## hangup all");
                        AppMainActivity.sipClientWrapper.hangupall();
                        break;
                  

                    case START_RECORDING:
                        AppMainActivity.sipClientWrapper.startRecording(bean
                                .getCall_id());
                        break;

                    case STOP_RECORDING:
                        AppMainActivity.sipClientWrapper.stopRecording();
                        break;

                    
                  
                    case HOLD_SESSION:
                        AppMainActivity.sipClientWrapper
                                .holdSession(bean.getCall_id());
                        break;
                    case UNHOLD_SESSION:
                        AppMainActivity.sipClientWrapper.unHoldSession(bean
                                .getCall_id());
                        break;
                   
                   

                    default:
                        break;
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.i("droid123", "Method executed.......");
        }
        Log.i("droid123", "came out of loop");
        super.run();
    }

    public void checkisLibraryLoaded() {
    	
    	
        AppMainActivity.sipClientWrapper.Init("");
        setCallBack(AppReference.mainContext);
        Log.i("droid123","===> Inside LOAD_LIBS===>checkisLibraryLoaded()");
        
    }

    public void reloadLibrary() {
        AppMainActivity.sipClientWrapper.destroy();
        AppMainActivity.sipClientWrapper.Init("");

    }

}
