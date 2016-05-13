package com.cg.callservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.android.pjsua.PjsuaInterface;
import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.hostedconf.AppReference;
import com.main.AppMainActivity;
import com.thread.CommunicationBean;
import com.thread.SipCommunicator.sip_operation_types;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;

import java.util.ArrayList;

public class SipCallConnectingScreen extends Activity implements PjsuaInterface {
	public ImageView end_call, mute, speaker;
	private boolean isHost;
	private int current_callid;
	private ListView callerDetailListView;
	private String fromName;
	ArrayList<BuddyInformationBean> callerdetails;
	Context cContext;
	Context temp;
	public AudioManager audio_manager = null;
    private AlertDialog alert = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.callscreenactivity_new);
            SingleInstance.inCall = true;
            cContext = this;
            audio_manager = (AudioManager) cContext
                    .getSystemService(Context.AUDIO_SERVICE);
            callerdetails = new ArrayList<BuddyInformationBean>();
            CallScreenAdapter callScreenAdapter = new CallScreenAdapter(cContext,
                    R.layout.callscreenrow, callerdetails);
            Intent i = getIntent();
            isHost = i.getBooleanExtra("host", false);

            if (!isHost) {
                Log.d("droid123","Begining false");
                current_callid = i.getIntExtra("callid", -1);
                fromName = i.getStringExtra("fromname");

                BuddyInformationBean bean = new BuddyInformationBean();
                bean.setName(fromName);
                callerdetails.add(bean);

                BuddyInformationBean ownerbean = new BuddyInformationBean();
                ownerbean.setName("Me");
                callerdetails.add(ownerbean);
                Log.d("droid123", "End false");
            } else {
                Log.d("droid123","Begining true");
                fromName = i.getStringExtra("fromname");

                BuddyInformationBean ownerbean = new BuddyInformationBean();
                ownerbean.setName("Me");
                callerdetails.add(ownerbean);

                BuddyInformationBean bean = new BuddyInformationBean();
                bean.setName(fromName);
                callerdetails.add(bean);
                Log.d("droid123", "End true");
            }

            Log.i("incoming", "isHost :" + isHost);
            SingleInstance.contextTable.put("sipcallscreen", this);
            callerDetailListView = (ListView) findViewById(R.id.llayoutcallitem);
            callerDetailListView.setAdapter(callScreenAdapter);
            ImageView end_call = (ImageView) findViewById(R.id.endcall_iv);
            final ImageView mute = (ImageView) findViewById(R.id.mute);
            final ImageView speaker = (ImageView) findViewById(R.id.speaker);

            audio_manager.setMode(AudioManager.MODE_NORMAL);
            audio_manager.setSpeakerphoneOn(false);

            end_call.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    // if (isHost) {
                    CommunicationBean cb = new CommunicationBean();
                    cb.setCall_id(current_callid);
                    Log.i("incoming", "end_call : callid" + current_callid);
                    cb.setOperationType(sip_operation_types.HANGUP_SINGLE_CALL);
                    AppReference.sipQueue.addMsg(cb);
                    // }
                    finish();
                }
            });

            mute.setTag(0);
            mute.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (mute.getTag().equals(0)) {

                        mute.setImageResource(R.drawable.mute_icon_yellow);
                        CommunicationBean m_bean = new CommunicationBean();
                        m_bean.setOperationType(sip_operation_types.TOGGLEDEVICE_MICMUTE);
                        m_bean.setFlag(true);
                        AppReference.sipQueue.addMsg(m_bean);
                        mute.setTag(1);

                    } else {

                        mute.setImageResource(R.drawable.mute_icon_blue);
                        CommunicationBean m_bean = new CommunicationBean();
                        m_bean.setOperationType(sip_operation_types.TOGGLEDEVICE_MICMUTE);
                        m_bean.setFlag(false);
                        AppReference.sipQueue.addMsg(m_bean);
                        mute.setTag(0);

                    }

                }
            });

            speaker.setTag(0);
            speaker.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (speaker.getTag().equals(0)) {

                        speaker.setImageResource(R.drawable.speakeronyellow);
                        if (audio_manager != null) {
                            audio_manager.setMode(AudioManager.MODE_NORMAL);
                            audio_manager.setSpeakerphoneOn(true);
                        }
                        speaker.setTag(1);
                    } else {
                        speaker.setImageResource(R.drawable.speakericonblue);
                        if (audio_manager != null) {
                            audio_manager.setMode(AudioManager.MODE_NORMAL);
                            audio_manager.setSpeakerphoneOn(false);
                        }
                        speaker.setTag(0);
                    }
                }
            });
        }catch(Exception e){
            Log.d("SIP","Error => "+e.toString());
        }
	}

    @Override
    protected void onResume() {
        super.onResume();
        temp = AppMainActivity.inActivity;
        AppMainActivity.inActivity = this;
    }

    public class CallScreenAdapter extends ArrayAdapter<BuddyInformationBean> {

		ArrayList<BuddyInformationBean> callerdetailsinadapter;
		Context context;

		public CallScreenAdapter(Context context, int resource,
				ArrayList<BuddyInformationBean> objects) {
			super(context, resource, objects);
			this.context = context;
			this.callerdetailsinadapter = objects;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View rowView = convertView;
                // if (rowView == null) {
                rowView = inflater.inflate(R.layout.callscreenrow, parent, false);
                TextView username = (TextView) rowView
                        .findViewById(R.id.my_userinfo_tv);

                BuddyInformationBean adapterbean = callerdetailsinadapter
                        .get(position);
                if(!adapterbean.getName().equalsIgnoreCase("Me")) {
                    ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(adapterbean.getName());
                    String sipUsername=pBean.getFirstname()+" "+pBean.getLastname();
                    Log.i("abcd", "sip username" + adapterbean.getName());
                    username.setText(sipUsername);
                }else {
                    username.setText(adapterbean.getName());

                }
                // }
                return rowView;
            }catch(Exception e){
                Log.d("SIP","Error => "+e.toString());
            }
            return null;
		}

	}

	@Override
	public void didReceiveOnCallState(String remoteinfo, String sessionid,
			int call_id, int call_status) {
		// TODO Auto-generated method stub
		current_callid = call_id;
		Log.i("incoming", "didReceiveOnCallState : callid :" + current_callid
				+ " call_id :" + call_id);
	}

	@Override
	public void didReceiveCallConnected(String remoteinfo,
			String call_sessionid, int call_id) {
		// TODO Auto-generated method stub
//        current_callid = call_id;
	}

	@Override
	public void didReceiveCallDisconnected(String remoteinfo, String sessionid,
			int call_id, int call_status) {
		// TODO Auto-generated method stub
		Log.i("incoming", "didReceiveCallDisconnected call_id :"+call_id+" current_callid :"+current_callid);
       // if(SingleInstance.inCall) {

//        if(current_callid==call_id)
//        {
            finish();
//        }else
//        {
//
//        }

       // }
	}

	@Override
	public void didReceiveCallReplace(int callid, String sipinfo,
			String fromuser, String rhvalue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyRegistration(int acc_id, boolean isStatus,
			boolean isUpdate, int errCode, String errReason, String sipReason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didReceiveIncomingCall(int call_Id, String remoteinfo) {
		// TODO Auto-generated method stub
        try{
            Log.d("incoming","didReceiveIncomingCall => call_Id"+call_Id);

            CommunicationBean bean = new CommunicationBean();
            bean.setCall_id(call_Id);
            bean.setOperationType(sip_operation_types.REJECT_CALL);
            AppReference.sipQueue.addMsg(bean);
        }catch(Exception e){
            Log.d("SIP","Error => "+e.toString());
        }
	}

	@Override
	public void didReceiveIncomingJoinCall(int callid, String remoteinfo,
			String sipinfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessageReceived(int call_id, String from, String to,
			String contact, String mime_type, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void on_referMessageReceived(int call_id, String remoteinfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void on_call_media(int callid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyMediaState(int call_id, String touser_no, boolean status) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
        try{
            SingleInstance.contextTable.remove("sipcallscreen");
            SingleInstance.inCall=false;
            super.onDestroy();
            AppMainActivity.inActivity = temp;
        }catch(Exception e){
            Log.d("SIP","Error => "+e.toString());
        }
	};

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                if (alert == null) {
                    showHangUpAlert();
                } else if (!alert.isShowing()) {
                    showHangUpAlert();
                }

            }
        }catch(Exception e){
            Log.d("SIP","Error => "+e.toString());
        }
        return super.onKeyDown(keyCode, event);
    }

    void showHangUpAlert() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String ask = SingleInstance.mainContext.getResources().getString(
                    R.string.need_call_hangup);

            builder.setMessage(ask)
                    .setCancelable(false)
                    .setPositiveButton(
                            SingleInstance.mainContext.getResources().getString(
                                    R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    CommunicationBean cb = new CommunicationBean();
                                    cb.setCall_id(current_callid);
                                    Log.i("incoming", "end_call : callid" + current_callid);
                                    cb.setOperationType(sip_operation_types.HANGUP_SINGLE_CALL);
                                    AppReference.sipQueue.addMsg(cb);
                                    // }
                                    finish();

                                }
                            })
                    .setNegativeButton(
                            SingleInstance.mainContext.getResources().getString(
                                    R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            alert = builder.create();
            alert.show();
        }catch(Exception e){
            Log.d("SIP","Error => "+e.toString());
        }
    }
}


