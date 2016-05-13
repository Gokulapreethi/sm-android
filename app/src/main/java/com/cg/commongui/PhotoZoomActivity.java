package com.cg.commongui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;

/**
 * PhotoZoomActivity screen is used to zoom the particular portion of the image
 * file and it has the image rotation up to 360 degree.After rotation user can
 * save that rotated image too.
 * 
 * 
 * 
 */
public class PhotoZoomActivity extends Activity {
	private Bitmap bitMap;

	private int degree = 0;

	private String strImagePath = "";

	private boolean ishandsketch = false;

	private CallDispatcher callDispatcher;

	/**
	 * When activity stated show the ImageView with zoom option
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.image_zoom_fullscreen);
			WebServiceReferences.contextTable.put("zoomactivity", this);
			strImagePath = getIntent().getStringExtra("Photo_path");
			ishandsketch = getIntent().getBooleanExtra("type", false);

			Log.d("image", "Is handsketch ------>" + ishandsketch);

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDispatcher = new CallDispatcher(this);

			callDispatcher.setNoScrHeight(noScrHeight);
			callDispatcher.setNoScrWidth(noScrWidth);
			/*
			 * TouchImageView touch = new TouchImageView(this);
			 * touch.setImageBitmap(BitmapFactory.decodeFile(strImagePath));
			 * touch.setMaxZoom(4f); setContentView(touch);
			 */
			bitMap = callDispatcher.ResizeImage(strImagePath);

			LinearLayout scrl = (LinearLayout) findViewById(R.id.scrl);
			LinearLayout llay_btn = (LinearLayout) findViewById(R.id.llaytbutton);

			final TouchImageView touch = new TouchImageView(this);
			touch.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));
			touch.setImageBitmap(bitMap);
			touch.setMaxZoom(3f); // change the max level of zoom, default is 3f

			final TextView tvDegree = (TextView) findViewById(R.id.txt_degree);
			Button btnR = (Button) findViewById(R.id.btn_rotate_right);

			btnR.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						degree = degree + 90;

						if (degree == 360) {
							degree = 0;
						}
						tvDegree.setText("" + degree);

						// Getting width & height of the given image.
						int w = bitMap.getWidth();
						int h = bitMap.getHeight();
						// Setting post rotate to 90
						Matrix mtx = new Matrix();
						mtx.postRotate(90);
						// Rotating Bitmap
						bitMap = Bitmap.createBitmap(bitMap, 0, 0, w, h, mtx,
								true);

						touch.setImageBitmap(bitMap);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

			Button btnL = (Button) findViewById(R.id.btn_rotate_left);

			btnL.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {

						degree = degree - 90;

						if (degree == -360) {
							degree = 0;
						}
						tvDegree.setText("" + degree);
						// Getting width & height of the given image.
						int w = bitMap.getWidth();
						int h = bitMap.getHeight();
						// Setting post rotate to 90
						Matrix mtx = new Matrix();
						mtx.postRotate(-90);
						// Rotating Bitmap
						bitMap = Bitmap.createBitmap(bitMap, 0, 0, w, h, mtx,
								true);
						touch.setImageBitmap(bitMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			scrl.addView(touch);

			if (ishandsketch) {
				scrl.setBackgroundColor(Color.GRAY);
				llay_btn.setBackgroundColor(Color.GRAY);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    /**
	 * When Image is rotated from its actual position update that changes into
	 * the Image file
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK && degree != 0)) {
			if (!getIntent().getBooleanExtra("save", false)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setMessage(
						"Do you want to save the image orientation changes ? ")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										File file = new File(strImagePath);
										FileOutputStream fOut;
										try {
											// WebServiceReferences.isImageRotated
											// = true;
											// WebServiceReferences.imgPath =
											// strImagePath;
											fOut = new FileOutputStream(file);
											bitMap.compress(
													Bitmap.CompressFormat.JPEG,
													100, fOut);

											fOut.flush();
											fOut.close();

											Intent i = new Intent();
											Bundle bun = new Bundle();
											bun.putString("changes", "yes");
											i.putExtra("share", bun);
											setResult(-1, i);
											finish();
										} catch (FileNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
										Intent i = new Intent();
										Bundle bun = new Bundle();
										bun.putString("changes", "no");
										i.putExtra("share", bun);
										setResult(-1, i);
										finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub

		if (outState == null) {
			outState = new Bundle();
		}
		outState.putString("Photo_path", strImagePath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("zoomactivity");
		if (bitMap != null) {
			if (!bitMap.isRecycled()) {
				bitMap.recycle();
			}
		}

		super.onDestroy();
	}

}
