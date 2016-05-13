package com.cg.locker;
import com.cg.snazmed.R;
import com.main.AppMainActivity;
import com.util.CustomVideoCamera;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class DevicePolicyDemoActivity extends Activity implements
		OnCheckedChangeListener {
	static final String TAG = "DevicePolicyDemoActivity";
	static final int ACTIVATION_REQUEST = 47; // identifies our request id
	public DevicePolicyManager devicePolicyManager;
	public ComponentName demoDeviceAdmin;
	ToggleButton toggleButton;
	
	public static DevicePolicyDemoActivity context;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.devicepolicydemomain);
		
		context = this;
		

		toggleButton = (ToggleButton) super
				.findViewById(R.id.toggle_device_admin);
		toggleButton.setOnCheckedChangeListener(this);

		// Initialize Device Policy Manager service and our receiver class
		devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		demoDeviceAdmin = new ComponentName(this, DemoDeviceAdminReceiver.class);
		
//		devicePolicyManager.removeActiveAdmin(demoDeviceAdmin);
		
		
//		if(devicePolicyManager.isAdminActive(demoDeviceAdmin))
			toggleButton.setChecked(devicePolicyManager.isAdminActive(demoDeviceAdmin));
		
		try {
			devicePolicyManager.setCameraDisabled(demoDeviceAdmin, false);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
		
//		ToggleButton toggle_camera = (ToggleButton) findViewById(R.id.toggle_camera_lock);
//		toggle_camera.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				
//				if(isChecked)
//				{
//					Toast.makeText(getApplicationContext(), "Camera disabling..", Toast.LENGTH_SHORT).show();
//					devicePolicyManager.setCameraDisabled(demoDeviceAdmin, isChecked);
//				}else
//				{
//					Toast.makeText(getApplicationContext(), "Camera enabling..", Toast.LENGTH_SHORT).show();
//					devicePolicyManager.setCameraDisabled(demoDeviceAdmin, isChecked);
//				}
//				
//			}
//		});
		
	}
	
	public void enablePermission()
	{
		try {
			devicePolicyManager.setCameraDisabled(demoDeviceAdmin, false);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void disablePermission()
	{
		try {
			devicePolicyManager.setCameraDisabled(demoDeviceAdmin, true);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
		try {
			devicePolicyManager.setCameraDisabled(demoDeviceAdmin, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		try {
			devicePolicyManager.setCameraDisabled(demoDeviceAdmin, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onPause();
	}

	/**
	 * Called when a button is clicked on. We have Lock Device and Reset Device
	 * buttons that could invoke this method.
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.button_capture:
			
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+System.currentTimeMillis()+".jpg";
			
			Intent intent = new Intent(DevicePolicyDemoActivity.this,CustomVideoCamera.class);
			intent.putExtra("isPhoto", true);
			intent.putExtra("filePath", path);
			startActivityForResult(intent, 101);
			break;
		
//		case R.id.button_lock_device:
//			// We lock the screen
//			Toast.makeText(this, "Locking device...", Toast.LENGTH_LONG).show();
//			Log.d(TAG, "Locking device now");
//			
//			
//			devicePolicyManager.lockNow();
//			break;
//		case R.id.button_reset_device:
//			// We reset the device - this will erase entire /data partition!
//			Toast.makeText(this, "Locking device...", Toast.LENGTH_LONG).show();
//			Log.d(TAG,
//					"RESETing device now - all user data will be ERASED to factory settings");
//			devicePolicyManager.wipeData(ACTIVATION_REQUEST);
//			
//			break;
		}
	}
	

	

	/**
	 * Called when the state of toggle button changes. In this case, we send an
	 * intent to activate the device policy administration.
	 */
	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		if (isChecked) {
			// Activate device administration
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
					demoDeviceAdmin);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					"Security Reason - Custom message");
			startActivityForResult(intent, ACTIVATION_REQUEST);
		}else
		{
			devicePolicyManager.removeActiveAdmin(demoDeviceAdmin);
		}
		Log.d(TAG, "onCheckedChanged to: " + isChecked);
	}

	/**
	 * Called when startActivityForResult() call is completed. The result of
	 * activation could be success of failure, mostly depending on user okaying
	 * this app's request to administer the device.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVATION_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Log.i(TAG, "Administration enabled!");
				toggleButton.setChecked(true);
			} else {
				Log.i(TAG, "Administration enable FAILED!");
				toggleButton.setChecked(false);
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}