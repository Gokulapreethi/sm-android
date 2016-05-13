package com.cg.commongui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.ComponentCreator;
import com.util.MyExceptionHandler;

public class HandSketchActivity extends GraphicsActivity implements
		ColorPickerDialog.OnColorChangedListener {

	public static Bitmap bmp = null;
	public static Canvas canvs = null;
	private FileOutputStream fout;
	private String folderPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/COMMedia/";
	private String filename = null;
	private Button IMRequest = null;
	private Button btn_cancel1 = null;
	private Context context = null;
	public static Button btn_cmp = null;
	private MyView myview = null;
	private TextView title = null;
	CallDispatcher callDisp = null;
	private SharedPreferences preferences;
	private boolean send;
	private boolean isError;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {

			WebServiceReferences.contextTable.put("handsketch", this);
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.custom_title1);
			Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(
					this, HandSketchActivity.class));
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.custom_title1);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

			context = this;
			IMRequest = (Button) findViewById(R.id.im);
			send = getIntent().getBooleanExtra("send", false);

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			preferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			CallDispatcher.handsketch_edit = false;
			btn_cancel1 = (Button) findViewById(R.id.settings);
			btn_cancel1.setBackgroundResource(R.drawable.ic_action_back);
			btn_cancel1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (CallDispatcher.handsketch_edit)
						showAlert();
					else {
						if (send)
							if (WebServiceReferences.contextTable
									.containsKey("Component"))
								((ComponentCreator) WebServiceReferences.contextTable
										.get("Component")).finish();
						finish();
					}

				}
			});

			btn_cmp = (Button) findViewById(R.id.btncomp);
			if (send)
				btn_cmp.setText("Send");
			else
				btn_cmp.setText("Save");
			if (WebServiceReferences.contextTable.containsKey("utilitybuyer")
					|| WebServiceReferences.contextTable
							.containsKey("utilityprovider")
					|| WebServiceReferences.contextTable
							.containsKey("utilityneeder")
					|| WebServiceReferences.contextTable
							.containsKey("utilityseller")) {

			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_VERTICAL;
			btn_cmp.setLayoutParams(params);
			btn_cmp.setBackgroundResource(R.color.title);
			btn_cmp.setTextColor(Color.parseColor("#FFFFFF"));
			btn_cmp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (CallDispatcher.handsketch_edit) {
						saveNote();
						finish();
					} else
						Toast.makeText(context, " Cannot save empty file",
								Toast.LENGTH_LONG).show();
				}
			});

			title = (TextView) findViewById(R.id.note_date);
			title.setText("Hand Sketch");
			title.setTextSize(14.0f);
			title.setTypeface(Typeface.DEFAULT_BOLD);

			IMRequest.setVisibility(View.INVISIBLE);
			IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);
			// IMRequest.setWidth(70);

			myview = new MyView(this);

			setContentView(myview);
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setDither(true);
			mPaint.setColor(0xFFFF0000);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(12);

			mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6,
					3.5f);
			mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
		} catch (Exception e) {
			e.printStackTrace();
			isError = true;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			isError = true;
		} finally {

		}

		if (isError) {
			Intent i = new Intent();
			setResult(RESULT_CANCELED, i);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		WebServiceReferences.contextTable.remove("handsketch");
		super.onDestroy();
	}

	private Paint mPaint;
	private MaskFilter mEmboss;
	private MaskFilter mBlur;

	public void colorChanged(int color) {
		mPaint.setColor(color);
	}

	public class MyView extends View {

		private static final float MINP = 0.25f;
		private static final float MAXP = 0.75f;

		private Bitmap mBitmap;
		private Canvas mCanvas;
		private Path mPath;
		private Paint mBitmapPaint;
		private int noScrWidth;
		private int noscrHeight;

		public MyView(Context c) {
			super(c);

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			noscrHeight = displaymetrics.heightPixels;
			noScrWidth = displaymetrics.widthPixels;
			mBitmap = Bitmap.createBitmap(noScrWidth, noscrHeight,
					Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			mCanvas.drawColor(Color.WHITE);
			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);

			HandSketchActivity.bmp = mBitmap;
			HandSketchActivity.canvs = mCanvas;
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(0xFFAAAAAA);

			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

			canvas.drawPath(mPath, mPaint);

		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;

		private void touch_start(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;

			HandSketchActivity.btn_cmp.setVisibility(View.VISIBLE);

		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;

				HandSketchActivity.btn_cmp.setVisibility(View.VISIBLE);
				CallDispatcher.handsketch_edit = true;
			}
		}

		private void touch_up() {
			mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			mCanvas.drawPath(mPath, mPaint);
			// kill this so we don't double draw
			mPath.reset();

			HandSketchActivity.btn_cmp.setVisibility(View.VISIBLE);

		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				// CallDispatcher.handsketch_edit = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				// CallDispatcher.handsketch_edit = true;
				break;
			}
			return true;
		}
	}

	private static final int COLOR_MENU_ID = Menu.FIRST;
	private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
	private static final int BLUR_MENU_ID = Menu.FIRST + 2;
	private static final int ERASE_MENU_ID = Menu.FIRST + 3;
	private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;
	private static final int SAVE_MENU_ID = Menu.FIRST + 5;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
		menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
		menu.add(0, BLUR_MENU_ID, 0, "Clear").setShortcut('5', 'z');
		menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
		menu.add(0, SRCATOP_MENU_ID, 0, "Stroke Width").setShortcut('5', 'z');
		menu.add(0, SAVE_MENU_ID, 0, "Save");
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mPaint.setXfermode(null);
		mPaint.setAlpha(0xFF);

		switch (item.getItemId()) {
		case COLOR_MENU_ID:
			new ColorPickerDialog(this, this, mPaint.getColor()).show();
			return true;
		case EMBOSS_MENU_ID:
			if (mPaint.getMaskFilter() != mEmboss) {
				mPaint.setMaskFilter(mEmboss);
			} else {
				mPaint.setMaskFilter(null);
			}
			return true;
		case BLUR_MENU_ID:
			clearAll();
			return true;
		case ERASE_MENU_ID:
			mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			mPaint.setAlpha(0xFF);
			return true;
		case SRCATOP_MENU_ID:

			final CharSequence[] items = { "12", "20", "30", "40" };
			AlertDialog.Builder builder = new AlertDialog.Builder(
					HandSketchActivity.this);
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {

					if (item == 0) {
						mPaint.setStrokeWidth(12);
					} else if (item == 1) {
						mPaint.setStrokeWidth(20);
					} else if (item == 2) {
						mPaint.setStrokeWidth(30);
					} else if (item == 3) {
						mPaint.setStrokeWidth(40);

					}

				}
			});
			AlertDialog alert = builder.create();
			alert.show();

			return true;

		case SAVE_MENU_ID:
			saveNote();

			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	private String getFileName() {
		String strFilename;
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHH_mm_ss");
		Date date = new Date();
		strFilename = dateFormat.format(date);
		return strFilename;
	}

	private void saveNote() {
		try {
			File dir = new File(folderPath);
			Log.i("thgg", "#####################" + dir.exists());
			if (!dir.exists()) {
				dir.mkdir();
			}

			File fle = new File(folderPath, "Instruction_image" + getFileName()
					+ ".jpg");
			filename = folderPath + "Instruction_image" + getFileName()
					+ ".jpg";
			try {

				fout = new FileOutputStream(fle);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			bmp.compress(CompressFormat.PNG, 75, fout);

			try {
				fout.flush();
				fout.close();
				Intent i = new Intent();
				Bundle bun = new Bundle();
				bun.putString("path", filename);
				i.putExtra("sketch", bun);
				setResult(RESULT_OK, i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CallDispatcher.handsketch_edit = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void clearAll() {

		CallDispatcher.handsketch_edit = false;
		canvs.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
		bmp.eraseColor(Color.WHITE);
		myview.invalidate();
		HandSketchActivity.btn_cmp.setVisibility(View.INVISIBLE);
	}

	private void showAlert() {

		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			String message = "";
			if (!send)
				message = "Are you sure want to Save and Go Back?";
			else
				message = "Are you sure want to Send and Go Back?";
			builder.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									saveNote();
									dialog.dismiss();
									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									CallDispatcher.handsketch_edit = false;
									if (send)
										if (WebServiceReferences.contextTable
												.containsKey("Component"))
											((ComponentCreator) WebServiceReferences.contextTable
													.get("Component")).finish();

									finish();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				if (CallDispatcher.handsketch_edit)
					showAlert();
				else {
					if (send)
						if (WebServiceReferences.contextTable
								.containsKey("Component"))
							((ComponentCreator) WebServiceReferences.contextTable
									.get("Component")).finish();
					finish();
				}
			} catch (Exception e) {

			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
