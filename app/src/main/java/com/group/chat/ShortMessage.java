package com.group.chat;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cg.snazmed.R;


public class ShortMessage extends Activity {	
	private Button btnSend;
	private EditText PhoneNo;
	private EditText Message; 
	boolean fromGrid;
	@Override	
protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sendsms);	
	
		final Bundle bndl = getIntent().getExtras();
		fromGrid = bndl.getBoolean("Grid");
	  btnSend = (Button) findViewById(R.id.sendSMSBtn);
	  PhoneNo = (EditText) findViewById(R.id.toPhoneNumberET);
	  Message = (EditText) findViewById(R.id.smsMessageET);
	  
	  btnSend.setOnClickListener(new OnClickListener() {    
		public void onClick(View arg0) {
			  sendSMS();
		}
	  });
	}
	protected void sendSMS(){
		if(fromGrid){
		String phoneNo = PhoneNo.getText().toString();
	     String msg = Message.getText().toString(); 
	     try {      
	    SmsManager smsManager = SmsManager.getDefault();
	    smsManager.sendTextMessage(phoneNo, null, msg, null, null);    
	    Toast.makeText(getApplicationContext(), "Message Sent",
	       Toast.LENGTH_LONG).show();
	    finish();
	     } catch (Exception ex) {
	    Toast.makeText(getApplicationContext(),
	     ex.getMessage().toString(),
	     Toast.LENGTH_LONG).show();
	    ex.printStackTrace();
	     } 
		}else
		{
			Log.i("sms123","Sending Failed");
		}
	    
	 }
}
