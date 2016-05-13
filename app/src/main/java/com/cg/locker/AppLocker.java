package com.cg.locker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.main.AppMainActivity;

public class AppLocker extends Activity{
	public static Context context;
	public static AlertDialog alertMissedCall;
	private AppMainActivity appMainActivity = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context=this;
		TextView textView=(TextView)findViewById(R.id.text);
		Button cancel=(Button)findViewById(R.id.cancel);
		Log.i("locker123","*********app" );
		// Start broadcast receiver may be StartupReceiver not started on
		// BOOT_COMPLETED
		// Check AndroidManifest.xml file
//		appMainActivity.initialize();
		
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//finish();
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
	}
//	private void initialize() {
//
//		// Start receiver with the name StartupReceiver_Manual_Start
//		// Check AndroidManifest.xml file
//		getBaseContext().getApplicationContext().sendBroadcast(
//				new Intent("StartupReceiver_Manual_Start"));
//	}
//	
//	public void killapp(int pid){
//		appMainActivity.killApp(pid);
//	}
//	public void killApp(int pid){
//		Toast.makeText(context, "Cannot Access",
//				Toast.LENGTH_SHORT).show();
////		android.os.Process.killProcess(pid);
//		
//		Intent startHomescreen=new Intent(Intent.ACTION_MAIN);
//		startHomescreen.addCategory(Intent.CATEGORY_HOME);
//		startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////		startHomescreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//		startActivity(startHomescreen);
//	}
//	


    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }
}

