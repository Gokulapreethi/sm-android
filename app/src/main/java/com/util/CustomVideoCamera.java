package com.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.ComponentCreator;
import com.main.AppMainActivity;

public class CustomVideoCamera extends Activity {
	private boolean isPhoto = false;
	private Camera myCamera;
	private MyCameraSurfaceView myCameraSurfaceView;
	private MediaRecorder mediaRecorder;

	Button myButton,start_rec;
	SurfaceHolder surfaceHolder;
	boolean recording;
	Button back, front;
	FrameLayout myCameraPreview;
	private int camera_no;
	// private String filename;
	private String path;
	Chronometer chronometer;
	private String filepath;
	private boolean ispaused = false;
	private Handler handler = new Handler();
	Context context=null;
	PowerManager powerManager;
	PowerManager.WakeLock wakeLock;
	OrientationEventListener myOrientationEventListener;
	String orientation=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Button.OnClickListener myButtonOnClickListener = new Button.OnClickListener() {

			@Override
			   public void onClick(final View v) {
				myButton.setEnabled(false);
			   if (isPhoto) {
			       myCamera.takePicture(null, null, jpegCallback);
			   } else {
			    stopRecording();
			    }
			   }
		};

		try {
			super.onCreate(savedInstanceState);
			context = this;
			myOrientationEventListener
					= new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL){

				@Override
				public void onOrientationChanged(int arg0) {
					// TODO Auto-generated method stub
					Log.i("orientation","Orientation: " + String.valueOf(arg0));
					if(camera_no==0) {
						if (arg0 > 250 && arg0 < 290) {
							orientation = "0";
							Log.i("orientation", "values 1-->" + orientation);
						} else if (arg0 > 80 && arg0 < 100) {
							orientation = "180";
						} else if (arg0 > 170 && arg0 < 190) {
							orientation = "270";
						} else {
							orientation = "90";

						}
					}else if(camera_no==1){
						if (arg0 > 250 && arg0 < 290) {
							orientation = "0";
							Log.i("orientation", "values 1-->" + orientation);
						} else if (arg0 > 80 && arg0 < 100) {
							orientation = "180";
						} else if (arg0 > 170 && arg0 < 190) {
							orientation = "90";
						} else {
							orientation = "270";

						}
					}
					Log.i("orientation","values 2-->"+orientation);
				}};
			if (myOrientationEventListener.canDetectOrientation()){
//    Toast.makeText(this, "Can DetectOrientation", Toast.LENGTH_LONG).show();
				myOrientationEventListener.enable();
			}
			SingleInstance.contextTable.put("customvideocallscreen",context);
			recording = false;
			powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
			wakeLock.acquire();
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.customvideo);
			filepath = getIntent().getStringExtra("filePath");
			isPhoto = getIntent().getBooleanExtra("isPhoto", false);
			myButton = (Button) findViewById(R.id.mybutton);
			start_rec=(Button)findViewById(R.id.mybutton1);
			front = (Button) findViewById(R.id.Button1);
			back = (Button) findViewById(R.id.Button2);
			start_rec.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					myButton.setVisibility(View.VISIBLE);
					start_rec.setVisibility(View.GONE);
//					front.setVisibility(View.GONE);
//					back.setVisibility(View.GONE);
					front.setEnabled(false);
					back.setEnabled(false);
					startRecording();
				}
			});
			if(!isPhoto){
				myButton.setVisibility(View.GONE);
				start_rec.setVisibility(View.VISIBLE);

			}

			myCameraPreview = (FrameLayout) findViewById(R.id.videoview);
			// Get Camera for preview
			int numOfCam = Camera.getNumberOfCameras();
			Log.i("log", "" + numOfCam);
			//myButton.setEnabled(false);
			if (myCamera == null) {
				if (numOfCam > 1) {
					//For Change Camera front from back
					//start
//					camera_no = 1;
//					myCamera = Camera
//							.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
					camera_no = 0;
					myCamera = Camera
							.open(Camera.CameraInfo.CAMERA_FACING_BACK);
					//end
				} else {
					//For Change Camera front from back
					//start
//					camera_no = 0;
//					myCamera = Camera
//							.open(Camera.CameraInfo.CAMERA_FACING_BACK);
					camera_no = 1;
					myCamera = Camera
							.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
					//end
				}

				myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
				myCameraPreview.addView(myCameraSurfaceView);
			}

			if (myCamera == null) {
				Toast.makeText(CustomVideoCamera.this, "Fail to get Camera",
						Toast.LENGTH_LONG).show();
			}


			if (!(numOfCam == 1)) {
				//For Change Camera front from back
				//start
//				back.setVisibility(View.VISIBLE);
				front.setVisibility(View.VISIBLE);
				//end
			}
			chronometer = (Chronometer) findViewById(R.id.chronometer1);
			if (isPhoto) {
				chronometer.setVisibility(View.GONE);
			}

			myButton.setOnClickListener(myButtonOnClickListener);
			front.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						// TODO Auto-generated method stub
						Log.i("camera","front click");
						myCameraPreview.removeView(myCameraSurfaceView);
						myCamera.stopPreview();
						myCamera.release();
						myCamera = null;
						myCamera = Camera
								.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
						myCameraSurfaceView = new MyCameraSurfaceView(
								CustomVideoCamera.this, myCamera);
						myCameraPreview.addView(myCameraSurfaceView);
						camera_no = 1;
						front.setVisibility(View.GONE);
						back.setVisibility(View.VISIBLE);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						// TODO Auto-generated method stub
						Log.i("camera","back click");
						myCameraPreview.removeView(myCameraSurfaceView);
						myCamera.stopPreview();
						myCamera.release();
						myCamera = null;
						myCamera = Camera
								.open(Camera.CameraInfo.CAMERA_FACING_BACK);
						myCameraSurfaceView = new MyCameraSurfaceView(
								CustomVideoCamera.this, myCamera);
						myCameraPreview.addView(myCameraSurfaceView);
						camera_no = 0;
						back.setVisibility(View.GONE);
						front.setVisibility(View.VISIBLE);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private void startRecording() {
		  try {

		   // Release Camera before MediaRecorder start
		   releaseCamera();

		   if (!prepareMediaRecorder(filepath)) {
		    Toast.makeText(CustomVideoCamera.this,
		      "Fail in prepareMediaRecorder()!\n - Ended -............", Toast.LENGTH_LONG).show();
		    finish();
		   }
		   // v.setEnabled(false);
		   //
		   // handler.postDelayed(new Runnable() {
		   //
		   // @Override
		   // public void run() {
		   // v.setEnabled(true);
		   //
		   // }
		   // }, 1000);
		   mediaRecorder.start();
		   recording = true;

			  //For Change Camera front from back
			  //start
//		   back.setVisibility(View.VISIBLE);
//		   front.setVisibility(View.GONE);
			  back.setVisibility(View.GONE);
			  front.setVisibility(View.VISIBLE);
			  //end
		   // myButton.setText("STOP");
		   // myButton.setBackgroundResource(R.drawable.stop_recording);
		   chronometer.setBase(SystemClock.elapsedRealtime());
		   chronometer.start();
		  } catch (Exception e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		 }
	
	public void stopRecording(){
		
		if (recording) {
			// stop recording and release camera
			if (mediaRecorder != null)
				try {
					mediaRecorder.stop();// stop the recording
				}catch(Exception e){
					e.printStackTrace();
				}
			releaseMediaRecorder(); // release the MediaRecorder
									// object
			chronometer.stop();
			// myButton.setText("START");
			myButton.setBackgroundResource(R.drawable.start_recording);
			// Exit after saved

			int pos = getIntent().getExtras().getInt("others", 0);
			String path = getIntent().getStringExtra("filePath");
			Intent intent = new Intent();
			intent.putExtra("others", pos);
			intent.putExtra("filePath", path);

			setResult(RESULT_OK, intent);
			finish();
		}
	}

	private void getCameraInstance(int front_back) {
		// TODO Auto-generated method stub
		try {

			CameraInfo camInfo = new CameraInfo();

			Camera.getCameraInfo(front_back, camInfo);
			if (front_back == 0) {

				myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
			} else if (front_back == 1) {

				myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);

			}
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			e.printStackTrace();
		}
	}

	private boolean prepareMediaRecorder(String filename) {

		try {
			// String path = Environment.getExternalStorageDirectory()
			// .getAbsolutePath().toString()
			// + "/";
			// Date date = new Date();
			// filename = "rec" + date.toString().replace(" ", "_").replace(":",
			// "_")
			// + ".mp4";
			// create empty file it must use
			// File file = new File(path, filename);
			if (!filename.endsWith(".mp4")) {
				filename = filename + ".mp4";
			}

			getCameraInstance(camera_no);

			Camera.Parameters parameters = myCamera.getParameters();
			List<Size> sizes = parameters.getSupportedPictureSizes();
			for (Size size : sizes) {

				Log.i("log", "Available resolution: " + size.width + " "
						+ size.height);
			}
			mediaRecorder = new MediaRecorder();
			myCamera.lock();
			myCamera.unlock();
			mediaRecorder.setCamera(myCamera);
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

			// CamcorderProfile profile = CamcorderProfile
			// .get(CamcorderProfile.QUALITY_LOW);
			// profile.videoFrameWidth = 320;
			// profile.videoFrameHeight = 240;
			// profile.videoFrameWidth = 640;
			// profile.videoFrameHeight = 480;
			// profile.videoFrameWidth = 1280;
			// profile.videoFrameHeight = 720;
			// mediaRecorder.setProfile(profile);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion <= android.os.Build.VERSION_CODES.GINGERBREAD) {
				// Do something for froyo and above versions
				mediaRecorder
						.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			} else {
				// do something for phones running an SDK before gingerbread
				mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			}

			mediaRecorder.setVideoSize(320, 240);
			mediaRecorder.setOutputFile(filename);
			mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
			mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M
			mediaRecorder.setOnInfoListener(new OnInfoListener() {

				@Override
				public void onInfo(MediaRecorder mr, int what, int extra) {
					// TODO Auto-generated method stub
					if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
						// mr.stop();
						chronometer.stop();
						Toast.makeText(getApplicationContext(),
								"Duration limit reached", Toast.LENGTH_LONG)
								.show();
						mediaRecorder.stop();
						releaseMediaRecorder();
						releaseCamera();
						Intent intent = new Intent();

						// intent.putExtra("MESSAGE", filename);
						setResult(RESULT_OK, intent);
						finish();
					}
				}
			});

			mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder()
					.getSurface());
			try {
				mediaRecorder.prepare();
			} catch (IllegalStateException e) {
				releaseMediaRecorder();
				return false;
			} catch (IOException e) {
				releaseMediaRecorder();
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void onPause() {
		try {
			ispaused = true;
			super.onPause();
			if (recording) {
				// stop recording and release camera
				if (mediaRecorder != null)
					mediaRecorder.stop(); // stop the recording
				chronometer.stop();
				myButton.setBackgroundResource(R.drawable.start_recording);
				Intent intent = new Intent();
				int pos = getIntent().getExtras().getInt("others", 0);
				String path = getIntent().getStringExtra("filePath");
				intent.putExtra("others", pos);
				intent.putExtra("filePath", path);
				setResult(RESULT_OK, intent);
			}
			releaseMediaRecorder();
			releaseCamera();
			finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset(); // clear recorder configuration
			mediaRecorder.release(); // release the recorder object
			mediaRecorder = null;
			myCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera() {
		if (myCamera != null) {
			myCamera.stopPreview();
			myCamera.release(); // release the camera for other applications
			myCamera = null;
		}
	}

	public class MyCameraSurfaceView extends SurfaceView implements
			SurfaceHolder.Callback {

		private SurfaceHolder mHolder;
		private Camera mCamera;

		@SuppressWarnings("deprecation")
		public MyCameraSurfaceView(Context context, Camera camera) {
			super(context);
			mCamera = camera;
			Log.i("log", "mycamSurfaceView");
			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format,
				int weight, int height) {
			if (mHolder.getSurface() == null) {
				// preview surface does not exist

				return;
			}
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!isPhoto) {
//			    startRecording();
			   }
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			if (ispaused) {
				mCamera = null;
				getCameraInstance(camera_no);
				mCamera = myCamera;
			}
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if (mediaRecorder != null) {

				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle(SingleInstance.mainContext.getResources().getString(R.string.confirm_stop_recording));

				alert.setMessage(
						"DO YOU WANT TO CANCEL RECORDING AND EXIT?")
						.setCancelable(false)
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										dialog.cancel();
									}
								})
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										// do your stuff
										releaseMediaRecorder(); // if you are
																// using
																// MediaRecorder,
																// release
																// it first
										releaseCamera(); // release the camera
															// immediately on
															// pause event
										if (WebServiceReferences.contextTable
												.containsKey("Component")) {
											ComponentCreator comp = (ComponentCreator) WebServiceReferences.contextTable
													.get("Component");
											if (SingleInstance.notePicker) {
												comp.finish();
												SingleInstance.notePicker = false;
											}

									}						
								
									finish();
							
								}
							  });
			 
							AlertDialog alertDialog = alert.create();			 
							alertDialog.show();
					}
				
		}
		return super.onKeyDown(keyCode, event);
	}

	private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

		@Override
		protected Void doInBackground(byte[]... data) {
			FileOutputStream outStream = null;

			// Write to SD Card
			try {
				File outFile = new File(filepath);
				outStream = new FileOutputStream(outFile);
				outStream.write(data[0]);
				outStream.flush();
				outStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			return null;
		}

	}

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			new SaveImageTask().execute(data);
			releaseCamera();
			int pos = getIntent().getExtras().getInt("others", 0);
			String path = getIntent().getStringExtra("filePath");
			Intent intent = new Intent();
			intent.putExtra("others", pos);
			intent.putExtra("filePath", path);
			if(orientation!=null){
				intent.putExtra("orientation", orientation);
			}
			setResult(RESULT_OK, intent);
			finish();
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Locale locale = null;
		wakeLock.release();
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SingleInstance.mainContext);
		String locale_string = sharedPreferences.getString("locale",
				SingleInstance.mainContext.getResources()
						.getString(R.string.english_langugage));

		if(SingleInstance.contextTable.containsKey("customvideocallscreen")){
			SingleInstance.contextTable.remove("customvideocallscreen");
		}
		
		int pos = 0;
		if (locale_string.equalsIgnoreCase("English")) {
			locale = new Locale("en");
		}
		// else if (locale_string.equalsIgnoreCase(SingleInstance.mainContext
		// .getResources().getString(R.string.tamil_langugage))) {
		// pos = 1;
		// }
		else if (locale_string.equalsIgnoreCase("Chinese")) {
			locale = new Locale("zh");
		}
		
		
		Locale.setDefault(locale);
		Configuration config = SingleInstance.mainContext
				.getResources().getConfiguration();
		config.locale = locale;
		SingleInstance.mainContext.getResources()
				.updateConfiguration(
						config,
						SingleInstance.mainContext
								.getResources()
								.getDisplayMetrics());
		
		
		onConfigurationChanged(config);
		myOrientationEventListener.disable();

	}

}
