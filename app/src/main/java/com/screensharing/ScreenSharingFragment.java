package com.screensharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.cg.callservices.VideoThread;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.main.AppMainActivity;
import com.util.FullScreenImage;
import com.util.SingleInstance;

import org.core.VideoCallback;
import org.lib.model.SignalingBean;
import org.util.Queue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Set;

public class ScreenSharingFragment extends Fragment implements VideoCallback,
		com.cg.callservices.AnimationListener {
	private static ScreenSharingFragment screenSharingFragment;
	private static Context context;
	private static CallDispatcher callDisp;
	public View view;
	Button selectall;
	Button imView;
	Button settings;

//	private int mWidth = 176;
//
//	private int mHeight = 144;

//	private int mWidth = 640;
//	private int mWidth = 480;

//	private int mHeight = 320;
//	private int mHeight = 480;

	private int mWidth = 320;

	private int mHeight = 240;

	Button plus = null;

	private Handler handler = new Handler();

	private int REQ_CODE_SELECT_BUDDY = 25;

	private int RES_CODE_SELECT_BUDDY = 25;

	private EditText buddyName = null;

	private static final int REQUEST_CODE = 100;
	private static MediaProjection MEDIA_PROJECTION;
	private static String STORE_DIRECTORY;
	private static int IMAGES_PRODUCED;

	private MediaProjectionManager mProjectionManager;
	private ImageReader mImageReader;
	private Handler mHandler;
	private Handler videoBroadCastHandler = null;
	private AlertDialog alert = null;

	private Queue videoQueue;

	private VideoThread videoThread;

	public Handler videoHandler = new Handler();

	ImageProcessing imageProcessor;

	public static int mRenderWidth = 480;
	public static int mRenderHeight = 360;

//	public static int mRenderWidth = 320;
//	public static int mRenderHeight = 240;
	private byte[] lastData;
	public boolean background;

	public static ScreenSharingFragment newInstance(Context maincontext) {
		try {
			if (screenSharingFragment == null) {
				context = maincontext;
				screenSharingFragment = new ScreenSharingFragment();
				callDisp = CallDispatcher.getCallDispatcher(context);
			}

			return screenSharingFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return screenSharingFragment;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Button select = (Button) getActivity().findViewById(R.id.btn_brg);
		select.setVisibility(View.GONE);

		Button imVw = (Button) getActivity().findViewById(R.id.im_view);
		imVw.setVisibility(View.GONE);

		Button setting = (Button) getActivity().findViewById(R.id.btn_settings);
		setting.setVisibility(View.GONE);

		Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
		plusBtn.setVisibility(View.GONE);
		SingleInstance.instanceTable.put("screenshare", screenSharingFragment);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			try {
				// call for the projection manager
				mProjectionManager = (MediaProjectionManager) SingleInstance.mainContext
						.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (view == null) {
			view = inflater.inflate(R.layout.screen_sharing, null);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			SingleInstance.mainContext.getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;

			buddyName = (EditText) view.findViewById(R.id.buddyName);

			Button selectBuddy = (Button) view.findViewById(R.id.select_buddy);
			selectBuddy.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,
							ScreenSharingActivity.class);
					startActivityForResult(intent, REQ_CODE_SELECT_BUDDY);

				}
			});
			final Button start = (Button) view.findViewById(R.id.start_share);
			final Button stop = (Button) view.findViewById(R.id.stop_share);
			start.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (buddyName.getText().toString() != null
							&& buddyName.getText().toString().length() > 0) {
						processCallRequest(7, buddyName.getText().toString());
						v.setVisibility(View.GONE);
						stop.setVisibility(View.VISIBLE);
					}
				}
			});

			stop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					stopProjection();
					if (imageProcessor != null && imageProcessor.isAlive()) {
						imageProcessor.interrupt();
						imageProcessor = null;
					}
					v.setVisibility(View.GONE);
					start.setVisibility(View.VISIBLE);
				}
			});
			Button image1 = (Button) view.findViewById(R.id.image_1);
			image1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					File f = new File(Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + "image_1.png");
					if (!f.exists())
						try {

							InputStream is = SingleInstance.mainContext
									.getAssets().open("image_1.png");
							int size = is.available();
							byte[] buffer = new byte[size];
							is.read(buffer);
							is.close();

							FileOutputStream fos = new FileOutputStream(f);
							fos.write(buffer);
							fos.close();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}

					Intent in = new Intent(context, FullScreenImage.class);
					in.putExtra("image", f.getPath());
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
				}
			});
			Button image2 = (Button) view.findViewById(R.id.image_2);
			image2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					File f = new File(Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + "image_2.png");
					if (!f.exists())
						try {

							InputStream is = SingleInstance.mainContext
									.getAssets().open("image_2.png");
							int size = is.available();
							byte[] buffer = new byte[size];
							is.read(buffer);
							is.close();

							FileOutputStream fos = new FileOutputStream(f);
							fos.write(buffer);
							fos.close();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}

					Intent in = new Intent(context, FullScreenImage.class);
					in.putExtra("image", f.getPath());
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
				}
			});
			Button video1 = (Button) view.findViewById(R.id.video_1);
			video1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
			videoQueue = new Queue();
			videoThread = new VideoThread(videoQueue);
			videoThread.setHandler(videoHandler);
			videoThread.start();
			AppMainActivity.commEngine.setVideoCallback(this);
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		videoBroadCastHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				Bundle bun = (Bundle) msg.obj;

				String action = bun.getString("action");
				try {

					if (action.equals("leave")) {

						//
						try {

							// Log.d("test", "From leave/bye");
							for (int i = 0; i < CallDispatcher.conferenceMembers
									.size(); i++) {
								// Log.d("login", "login");

								String buddy = CallDispatcher.conferenceMembers
										.get(i);
								// Log.d("test", "members " + buddy);
								SignalingBean sb = CallDispatcher.buddySignall
										.get(buddy);

								/* Static Clean up */
								// Log.d("login", "loginuser"
								// + objCallDispatcher.LoginUser);
								sb.setFrom(callDisp.LoginUser);
								sb.setTo(buddy);
								sb.setType("3");
								sb.setCallType("VBC");

								AppMainActivity.commEngine.hangupCall(sb);
							}
							/*
							 * members.clear();
							 */

							// Log.e("callInfo",
							// "Video call Screen Leave received");
							// WebServiceReferences.contextTable.remove("callscreen");
							// finish();
							callDisp.currentSessionid = null;
							CallDispatcher.conferenceMembers.clear();

						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		};
		videoHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Log.i("thread", "Came to handler.....");

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}, 5000);
		return view;
	}

	public View getParentView() {
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (SingleInstance.instanceTable.containsKey("screenshare")) {
			SingleInstance.instanceTable.remove("screenshare");
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if (SingleInstance.instanceTable.containsKey("screenshare")) {
			SingleInstance.instanceTable.remove("screenshare");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			MEDIA_PROJECTION = mProjectionManager.getMediaProjection(
					resultCode, data);

			if (MEDIA_PROJECTION != null) {

				STORE_DIRECTORY = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/screenshots/";
				File storeDirectory = new File(STORE_DIRECTORY);
				if (!storeDirectory.exists()) {
					boolean success = storeDirectory.mkdirs();
					if (!success) {
						Log.e("Screen",
								"failed to create file storage directory.");
						return;
					}
				}

				DisplayMetrics metrics = getResources().getDisplayMetrics();
				int density = metrics.densityDpi;
				int flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY
						| DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
				Display display = SingleInstance.mainContext.getWindowManager()
						.getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				// mWidth = size.x;
				// mHeight = size.y;

				// mWidth = 176;
				// mHeight = 144;
				int wid = size.x;
				int hei = size.y;

				mImageReader = ImageReader.newInstance(wid, hei,
						PixelFormat.RGBA_8888, 1);
				MEDIA_PROJECTION.createVirtualDisplay("screencap", wid, hei,
						density, flags, mImageReader.getSurface(), null,
						mHandler);

				imageProcessor = new ImageProcessing();
				imageProcessor.start();

				// mImageReader.setOnImageAvailableListener(
				// new ImageAvailableListener(), mHandler);
			}
		} else if (requestCode == REQ_CODE_SELECT_BUDDY
				&& resultCode == RES_CODE_SELECT_BUDDY) {
			Bundle bun = data.getBundleExtra("screenshare");
			if (bun != null) {
				String buddy = bun.getString("buddyname");
				buddyName.setText(buddy);
			}
		}
	}

	boolean running = true;

	private class ImageProcessing extends Thread {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				while (running) {

					Image image = null;
					FileOutputStream fos = null;
					Bitmap bitmap = null;

					try {
						image = mImageReader.acquireLatestImage();

						Log.i("ScreenImage123", "captured image " + image);

						if (image != null) {
							if (!background) {
								Log.i("imagesize", "W = " + image.getWidth()
										+ ", H=" + image.getHeight());
								Image.Plane[] planes = image.getPlanes();

								ByteBuffer buffer = planes[0].getBuffer();
								int pixelStride = planes[0].getPixelStride();
								int rowStride = planes[0].getRowStride();
								int rowPadding = rowStride - pixelStride
										* image.getWidth();

								// create bitmap
								// bitmap = Bitmap.createBitmap(mWidth +
								// rowPadding
								// / pixelStride, mHeight,
								// Bitmap.Config.ARGB_8888);
								bitmap = Bitmap.createBitmap(image.getWidth()
										+ (rowPadding / pixelStride),
										image.getHeight(),
										Bitmap.Config.ARGB_8888);
								bitmap.copyPixelsFromBuffer(buffer);
								// write bitmap to a file
								fos = new FileOutputStream(STORE_DIRECTORY
										+ "/myscreen_" + IMAGES_PRODUCED
										+ ".png");
								bitmap.compress(CompressFormat.JPEG, 100, fos);

								bitmap = Bitmap.createScaledBitmap(bitmap,
										mWidth, mHeight, true);

								byte[] yuv420sp = getNV21(mWidth, mHeight,
										bitmap);

								lastData = yuv420sp;

								AppMainActivity.commEngine
										.notifyVideoFrame_Sharing(yuv420sp);

								// write bitmap to a file
								// fos = new FileOutputStream(STORE_DIRECTORY
								// + "/myscreen_" + IMAGES_PRODUCED + ".png");
								// bitmap.compress(CompressFormat.JPEG, 100,
								// fos);

								IMAGES_PRODUCED++;
								Thread.sleep(66);
							} else {
								AppMainActivity.commEngine
										.notifyVideoFrame_Sharing(lastData);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (fos != null) {
							try {
								fos.close();
							} catch (IOException ioe) {
								ioe.printStackTrace();
							}
						}

						if (bitmap != null) {
							bitmap.recycle();
						}

						if (image != null) {
							image.close();
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// untested function
	byte[] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {

		int[] argb = new int[inputWidth * inputHeight];

		scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);

		byte[] yuv = new byte[inputWidth * inputHeight * 3 / 2];
		encodeYUV420SP(yuv, argb, inputWidth, inputHeight);

		scaled.recycle();

		return yuv;
	}

	void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
		final int frameSize = width * height;

		int yIndex = 0;
		int uvIndex = frameSize;

		int a, R, G, B, Y, U, V;
		int index = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {

				a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
				R = (argb[index] & 0xff0000) >> 16;
				G = (argb[index] & 0xff00) >> 8;
				B = (argb[index] & 0xff) >> 0;

				// well known RGB to YUV algorithm
				Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
				U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
				V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;

				// NV21 has a plane of Y and interleaved planes of VU each
				// sampled by a factor of 2
				// meaning for every 4 Y pixels there are 1 V and 1 U. Note the
				// sampling is every other
				// pixel AND every other scanline.
				yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0
						: ((Y > 255) ? 255 : Y));
				if (j % 2 == 0 && index % 2 == 0) {
					yuv420sp[uvIndex++] = (byte) ((V < 0) ? 0
							: ((V > 255) ? 255 : V));
					yuv420sp[uvIndex++] = (byte) ((U < 0) ? 0
							: ((U > 255) ? 255 : U));

				}

				index++;
			}
		}
	}

	public void processCallRequest(int caseid, String selectedBuddy) {
		try {

			callDisp.MakeCall(caseid, selectedBuddy, context);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startProjection() {

		// start capture handling thread
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				mHandler = new Handler();
				Looper.loop();
			}
		}.start();

		startActivityForResult(mProjectionManager.createScreenCaptureIntent(),
				REQUEST_CODE);
	}

	private void stopProjection() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (MEDIA_PROJECTION != null)
					MEDIA_PROJECTION.stop();
				forceHAngUp();
			}
		});
	}

	public void showWifiStateChangedAlert(String message) {
		Log.d("wifi", "callDialScreen");
		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
		alertCall
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton("Hangup",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									forceHAngUp();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
		alertCall.show();
	}

	public void forceHAngUp() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();

				Bundle bun = new Bundle();
				bun.putString("action", "leave");
				msg.obj = bun;
				if (videoBroadCastHandler != null)
					videoBroadCastHandler.sendMessage(msg);
			}
		});

	}

	void showHangUpAlert() {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String ask = "Are you sure do you want to hangup?";

		builder.setMessage(ask)
				.setCancelable(false)
				.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes)
,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								try {
									callDisp.accepted_users.clear();
									// Log.d("test", "From leave/bye");
									for (int i = 0; i < CallDispatcher.conferenceMembers
											.size(); i++) {
										// Log.d("login", "login");

										// System.out.println("conferenceMembers :"
										// +
										// CallDispatcher.conferenceMembers);
										// System.out.println("buddySignal :"
										// + CallDispatcher.buddySignall);

										String buddy = CallDispatcher.conferenceMembers
												.get(i);
										// Log.d("test", "members " +
										// buddy);
										SignalingBean sb = CallDispatcher.buddySignall
												.get(buddy);
										// System.out.println("1 "+sb.getSignalport());
										// System.out.println("2 "+sb.getToSignalPort());
										// System.out.println("Buddy :" +
										// buddy);
										/* Static Clean up */
										// Log.d("login", "loginuser"
										// + objCallDispatcher.LoginUser);
										sb.setFrom(CallDispatcher.LoginUser);
										sb.setTo(buddy);
										sb.setType("3");
										sb.setCallType("SS");
										AppMainActivity.commEngine
												.hangupCall(sb);
									}
									if (CallDispatcher.conferenceRequest.size() > 0) {
										try {
											Set<String> set = CallDispatcher.conferenceRequest
													.keySet();

											int i = 0;
											Iterator<String> iterator = set
													.iterator();
											while (iterator.hasNext()) {

												String buddy = (String) iterator
														.next();
												SignalingBean sb = CallDispatcher.conferenceRequest
														.get(buddy);
												Log.d("hang",
														"loginuser"
																+ CallDispatcher.LoginUser);
												sb.setFrom(CallDispatcher.LoginUser);
												sb.setTo(buddy);
												sb.setType("3");
												sb.setCallType("SS");

												AppMainActivity.commEngine
														.hangupCall(sb);

											}
										} catch (Exception e) {
											// TODO: handle exception
										}
									}

									callDisp.currentSessionid = null;
									CallDispatcher.conferenceMembers.clear();
									CallDispatcher.conferenceRequest.clear();

								} catch (Exception e) {
									// TODO: handle exception
									// Log.d("test", "sended bye " +
									// e.toString());
									e.printStackTrace();
								}

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		alert = builder.create();
		alert.show();

		//

	}

	@Override
	public void onTimerElapsed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyDecodedVideoCallback(byte[] data, long ssrc) {
		// TODO Auto-generated method stub
		if (videoQueue.getSize() < 1) {
			// Log.d("list", "two");
			videoQueue.addMsg(data);
		}
	}

	@Override
	public void notifyResolution(int w, int h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// background = true;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// background = false;
	}

}
