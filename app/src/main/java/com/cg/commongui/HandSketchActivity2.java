package com.cg.commongui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.ComponentCreator;
import com.main.AppMainActivity;
import com.util.CustomVideoCamera;
import com.util.MyExceptionHandler;
import com.util.SingleInstance;


public class HandSketchActivity2 extends Activity implements OnClickListener {

	private static int GALLERY_REQUEST = 1;
	private static final int CAMERA_REQUEST = 2;

	// custom drawing view
	private DrawingView drawView;
	// buttons
	private ImageView currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
	// sizes
	private float smallBrush, mediumBrush, largeBrush, miniBrush, microBrush;

	private ImageView galleryButt, clearButt, minBtn,maxBtn,redoButt,undoButt;

	private int current = 0;
	String[] app = { "Camera", "Gallery" };
	private int currentFormat = 0;
	Uri fileUri = null;
	String file = null;
	private Context context;
	private boolean send = false;
	private Button btn_cancel1;
	private Button btn_cmp;
	private TextView title;
	private String folderPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/COMMedia/";
	private CallDispatcher callDisp;
	private int size = 1, size5 = 5;
	private boolean isCleared = false;
	private int imageSelectedOption = 2;
	private String StoredFilepath;
	private Uri SelectedGallaryImage;
	LinearLayout pencil_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebServiceReferences.contextTable.put("handsketch", this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		if(SingleInstance.mainContext.getResources()
				.getString(R.string.screenshot).equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.yes))){
getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
}
		setContentView(R.layout.handsketch_layout);
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this,
				HandSketchActivity.class));
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.custom_title1);
//		getWindow().setSoftInputMode(
//				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		context = this;
		send = getIntent().getBooleanExtra("send", false);
		CallDispatcher.handsketch_edit = false;
		btn_cancel1 = (Button) findViewById(R.id.btn_back);
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);
		btn_cancel1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CallDispatcher.handsketch_edit && !isCleared)
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

		btn_cmp = (Button) findViewById(R.id.save);
		pencil_layout=(LinearLayout)findViewById(R.id.pencil_layout);
		ImageView pencilBtn=(ImageView)findViewById(R.id.pencilBtn);
		SeekBar brushSeek = (SeekBar)findViewById(R.id.brushSeek);
		pencilBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pencil_layout.setVisibility(View.INVISIBLE);
			}
		});

		if (WebServiceReferences.contextTable.containsKey("utilitybuyer")
				|| WebServiceReferences.contextTable
						.containsKey("utilityprovider")
				|| WebServiceReferences.contextTable
						.containsKey("utilityneeder")
				|| WebServiceReferences.contextTable
						.containsKey("utilityseller")) {

		}

		btn_cmp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CallDispatcher.handsketch_edit) {
					saveNote();
				} else
					Toast.makeText(context, " Cannot save empty file",
							Toast.LENGTH_LONG).show();
			}
		});

//		title = (TextView) findViewById(R.id.note_date);
//		title.setText("Hand Sketch");
//		title.setTextSize(14.0f);
//		title.setTypeface(Typeface.DEFAULT_BOLD);
//		setContentView(R.layout.handsketch_layout);

		// get drawing view
		drawView = (DrawingView) findViewById(R.id.drawing);

		// get the palette and first color button
		LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
		currPaint = (ImageView) paintLayout.getChildAt(0);
		currPaint.setImageDrawable(getResources().getDrawable(
				R.drawable.paint_pressed));

		// sizes from dimensions
		miniBrush = getResources().getInteger(R.integer.mini_size);
		microBrush = getResources().getInteger(R.integer.micro_size);
		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);

		// draw button
		drawBtn = (ImageView) findViewById(R.id.draw_btn);
		drawBtn.setOnClickListener(this);

		// set initial size
		drawView.setBrushSize(size5);

		// erase button
		eraseBtn = (ImageView) findViewById(R.id.erase_btn);
		eraseBtn.setOnClickListener(this);

		// new button
		// newBtn = (ImageButton) findViewById(R.id.new_btn);
		// newBtn.setOnClickListener(this);

		// save button
		saveBtn = (ImageView) findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);

		galleryButt = (ImageView) findViewById(R.id.galleryButt);
		galleryButt.setOnClickListener(this);
		maxBtn = (ImageView) findViewById(R.id.maxBtn);
		maxBtn.setOnClickListener(this);
		minBtn = (ImageView) findViewById(R.id.minBtn);
		minBtn.setOnClickListener(this);
		undoButt = (ImageView) findViewById(R.id.undoButt);
		undoButt.setOnClickListener(this);
		redoButt = (ImageView) findViewById(R.id.redoButt);
		redoButt.setOnClickListener(this);

		clearButt = (ImageView) findViewById(R.id.clearButt);
		clearButt.setOnClickListener(this);

		brushSeek.setProgress(size5);
		brushSeek.setMax(30);
		OnSeekBarChangeListener seekListenerbrush = new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				drawView.setBrushSize(size5);
				drawView.setLastBrushSize(size5);
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				drawView.setBrushSize(size5);
				drawView.setLastBrushSize(size5);
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1,
										  boolean arg2) {
				// TODO Auto-generated method stub
				size5 = arg1;
				if (arg1 == 0) {
					size5 = 1;
					arg1 = 1;
				}


			}
		};
		brushSeek.setOnSeekBarChangeListener(seekListenerbrush);

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.contextTable.containsKey("handsketch")) {
			WebServiceReferences.contextTable.remove("handsketch");
		}
		super.onDestroy();
	}

	// user clicked paint
	public void paintClicked(View view) {
		// use chosen color

		// set erase false
		drawView.setErase(false);
		drawView.setBrushSize(drawView.getLastBrushSize());

		if (view != currPaint) {
			ImageView imgView = (ImageView) view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			// update ui
			imgView.setImageDrawable(getResources().getDrawable(
					R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(
					R.drawable.paint));
			currPaint = (ImageView) view;
		}
	}

	public boolean isCleared() {
		return isCleared;
	}

	public void setCleared(boolean isCleared) {
		this.isCleared = isCleared;
	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.draw_btn) {

//				if (imageSelectedOption == 0) {
//					if(StoredFilepath!=null) {
//					File file = new File(StoredFilepath);
//					if (file.exists()) {
//						Log.i("handsketch123", "file name : " + file.getPath());
//						Bitmap bitMap = callDisp.ResizeImage(StoredFilepath);
//						if (bitMap != null) {
//							Log.i("handsketch123", "bitmap not null");
//							drawView.setImage(bitMap);
//						}
//					}
//				}
//			}else if(imageSelectedOption==1) {
//					if (SelectedGallaryImage != null) {
//						Bitmap board;
//
//						String[] filePathColumn = {MediaStore.Images.Media.DATA};
//						Cursor cursor = getContentResolver().query(SelectedGallaryImage,
//								filePathColumn, null, null, null);
//						cursor.moveToFirst();
//
//						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//						String picturePath = cursor.getString(columnIndex);
//						cursor.close();
//
//						// board=BitmapFactory.decodeFile(picturePath);
//						int h = drawView.getHeight(); // 320; // Height in pixels
//						int w = drawView.getWidth();// 480; // Width in pixels
//						board = Bitmap.createScaledBitmap(
//								BitmapFactory.decodeFile(picturePath), w, h, true);
//						drawView.setImage(board);
//					}
//				}

			drawView.setErase(false);
			pencil_layout.setVisibility(View.VISIBLE);
			setDefault();
			drawBtn.setBackgroundColor(getResources().getColor(R.color.black));

			if (SingleInstance.gridViewShow = true) {
				saveBtn.setVisibility(View.GONE);
			}

		}
		else if (view.getId() == R.id.erase_btn) {

			setDefault();
			eraseBtn.setBackgroundColor(getResources().getColor(R.color.black));
			drawView.setErase(true);
		}

		else if (view.getId() == R.id.galleryButt) {

			setDefault();
			galleryButt.setBackgroundColor(getResources().getColor(R.color.black));
			selectImage();
		}
		else if (view.getId() == R.id.maxBtn) {
//			setDefault();
			maxBtn.setBackgroundColor(getResources().getColor(R.color.black));

			float x=drawView.getScaleX();
			float y=drawView.getScaleY();
			if(x<4.0) {
				drawView.setScaleX((float) (x + 1));
				drawView.setScaleY((float) (y));
			}
		}
		else if (view.getId() == R.id.minBtn) {
//			setDefault();
			minBtn.setBackgroundColor(getResources().getColor(R.color.black));

			float x=drawView.getScaleX();
			float y=drawView.getScaleY();
			Log.i("AAAA","handsketch "+x);
			if(x>1.0) {
				drawView.setScaleX((float) (x - 1));
				drawView.setScaleY((float) (y));
			}
		}

		else if (view.getId() == R.id.clearButt) {
			isCleared = true;
//			drawView.startNew();
			 if (imageSelectedOption == 0) {
			 File file = new File(StoredFilepath);
			 if (file.exists()) {
			 Log.i("handsketch123", "file name : " + file.getPath());
			 Bitmap bitMap = callDisp.ResizeImage(StoredFilepath);
			 if (bitMap != null) {
			 Log.i("handsketch123", "bitmap not null");
			 drawView.setImage(bitMap);
			 }
			 }
			 }else if(imageSelectedOption==1){
			 Bitmap board;
			
			 String[] filePathColumn = { MediaStore.Images.Media.DATA };
			 Cursor cursor = getContentResolver().query(SelectedGallaryImage,
			 filePathColumn, null, null, null);
			 cursor.moveToFirst();

			 int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			 String picturePath = cursor.getString(columnIndex);
			 cursor.close();

			 // board=BitmapFactory.decodeFile(picturePath);
			 int h = drawView.getHeight(); // 320; // Height in pixels
			 int w = drawView.getWidth();// 480; // Width in pixels
			 board = Bitmap.createScaledBitmap(
			 BitmapFactory.decodeFile(picturePath), w, h, true);
			 drawView.setImage(board);
			 }
			 else if(imageSelectedOption==2)
			 {
			 drawView.startNew();
			
			 }
			Toast.makeText(this, "Cleared Successfully", Toast.LENGTH_SHORT)
					.show();

		}

		else if (view.getId() == R.id.save_btn) {

			saveNote();
		}
		else if (view.getId() == R.id.undoButt) {

			setDefault();
			undoButt.setBackgroundColor(getResources().getColor(R.color.black));
			drawView.onClickUndo();

		}

		else if (view.getId() == R.id.redoButt) {

			setDefault();
			redoButt.setBackgroundColor(getResources().getColor(R.color.black));
			drawView.onClickRedo();

		}

	}

	private void selectImage() {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_myacc_menu);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		lp.horizontalMargin = 15;
		Window window = dialog.getWindow();
		window.setBackgroundDrawableResource((R.color.lblack));
		window.setAttributes(lp);
		window.setGravity(Gravity.BOTTOM);
		dialog.show();
		TextView photo = (TextView) dialog.findViewById(R.id.delete_acc);
		photo.setText("Take Photo");
		TextView gallery = (TextView) dialog.findViewById(R.id.log_out);
		gallery.setText("Choose from Library");
		TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!CallDispatcher.isCallInitiate) {
				Long free_size = callDisp.getExternalMemorySize();
				if (free_size > 0 && free_size >= 5120) {
					String file = folderPath + "Instruction_image"
							+ getFileName() + ".jpg";
					Intent intent = new Intent(context,
							CustomVideoCamera.class);
					intent.putExtra("filePath", file);
					intent.putExtra("isPhoto", true);
					startActivityForResult(intent, CAMERA_REQUEST);
				}
				} else {
					Toast.makeText(HandSketchActivity2.this, "Please Try again...call  in progress", Toast.LENGTH_SHORT)
							.show();
				}
				dialog.dismiss();

			}
		});
		gallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//					Intent intent = new Intent(
//							Intent.ACTION_PICK,
//							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//					startActivityForResult(intent, GALLERY_REQUEST);
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(intent, GALLERY_REQUEST);
				dialog.dismiss();
			}
		});
	}

	private void saveNote() {

		// save drawing

		AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
		if (SingleInstance.contextTable.containsKey("mfcmembers")) {
			saveDialog.setTitle("Save Sign");
			saveDialog.setMessage("Save Sign?");
		} else {
			if (send) {
				saveDialog.setTitle("Send Handsketch");
				saveDialog.setMessage("Send handsketch?");
			} else {
				saveDialog.setTitle(SingleInstance.mainContext.getResources().getString(R.string.save_handsketch));
				saveDialog.setMessage(SingleInstance.mainContext.getResources().getString(R.string.save_handsketches1));
			}
		}
		saveDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// save drawing
						drawView.setDrawingCacheEnabled(true);
						// attempt to save
						File dir = new File(folderPath);
						Log.i("thgg", "#####################" + dir.exists());
						if (!dir.exists()) {
							dir.mkdir();
						}

						File fle = new File(folderPath, "Sketch_file"
								+ getFileName() + ".jpg");
						String filename = folderPath + "Sketch_file"
								+ getFileName() + ".jpg";
						// String imgSaved =
						// MediaStore.Images.Media.insertImage(
						// getContentResolver(),
						// drawView.getDrawingCache(), filename, "drawing");
						Bitmap img = drawView.getDrawingCache();
						String imgSaved = null;
						if (img != null) {
							BufferedOutputStream stream;
							try {
								stream = new BufferedOutputStream(
										new FileOutputStream(new File(filename)));
								img.compress(CompressFormat.JPEG, 75, stream);

								imgSaved = "saved";

							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						// feedback
						if (imgSaved != null) {
							// Toast savedToast = Toast.makeText(
							// getApplicationContext(),
							// "Drawing saved to Gallery!",
							// Toast.LENGTH_SHORT);
							// savedToast.show();
						} else {
							Toast unsavedToast = Toast.makeText(
									getApplicationContext(),
									"Oops! Image could not be saved.",
									Toast.LENGTH_SHORT);
							unsavedToast.show();
						}
						drawView.destroyDrawingCache();
						try {

							Intent i = new Intent();
							Bundle bun = new Bundle();
							bun.putString("path", filename);
							i.putExtra("sketch", bun);
							setResult(RESULT_OK, i);
							finish();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						CallDispatcher.handsketch_edit = false;
					}
				});
		saveDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		saveDialog.show();

	}

	public Bitmap ResizeImage(String filePath) {
		try {

			DisplayMetrics displaymetrics = new DisplayMetrics();
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			int targetWidth = noScrWidth;// (int) (noScrWidth / 1.2);
			int targetHeight = noScrHeight;// (int) (noScrHeight / 1.2);

			Bitmap bitMapImage = null;
			Options options = new Options();
			options.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
			if (bmp != null)
				bmp.recycle();
			double sampleSize = 0;
			Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math
					.abs(options.outWidth - targetWidth);

			if (options.outHeight * options.outWidth * 2 >= 1638) {

				sampleSize = scaleByHeight ? options.outHeight / targetHeight
						: options.outWidth / targetWidth;
				sampleSize = (int) Math.pow(2d,
						Math.floor(Math.log(sampleSize) / Math.log(2d)));
			}

			options.inJustDecodeBounds = false;
			options.inTempStorage = new byte[128];
			while (true) {
				try {
					if (bitMapImage != null)
						bitMapImage.recycle();
					System.gc();
					options.inSampleSize = (int) sampleSize;
					bitMapImage = BitmapFactory.decodeFile(filePath, options);
					break;
				} catch (Exception ex) {
					try {
						sampleSize = sampleSize * 2;
					} catch (Exception ex1) {

					}
				}
			}
			return bitMapImage;
		} catch (OutOfMemoryError e) {
			Log.e("bitmap", "===> " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String getFileName() {
		String strFilename = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			Date date = new Date();
			strFilename = dateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return strFilename;
	}

	/*
	 * private File getOutputPhotoFile() { String directory = "/sdcard/Notes/";
	 * 
	 * String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new
	 * Date()); return new File( directory + File.separator + "IMG_" + timeStamp
	 * + ".jpg"); }
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);

			Bitmap board;

			if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
				if(data!=null) {
					String filePath = data.getStringExtra("filePath");
					String orientation=data.getStringExtra("orientation");
					Log.i("orientation","chatActivity orientation-->"+orientation);
					StoredFilepath = filePath;
					imageSelectedOption = 0;
					File file = new File(filePath);
					if (file.exists()) {
						Log.i("handsketch123", "file name : " + file.getPath());
						if(orientation!=null && orientation.equalsIgnoreCase("0")) {
							Bitmap bitMap = callDisp.ResizeImage(filePath);
							if (bitMap != null) {
								Log.i("handsketch123", "bitmap not null");
//						Bitmap output = bitMap.createBitmap(200,200,
//								Bitmap.Config.ARGB_8888);
								Log.i("handsketch123", "bitmap name : " + bitMap);
								//drawView.setImage(bitMap);
								int h = drawView.getHeight(); // 320; // Height in pixels
								int w = drawView.getWidth();// 480; // Width in pixels
								drawView.setImage(Bitmap.createScaledBitmap(bitMap, w, h, true));
							}
						}else{
							AppReference.imageOrientation imageOrientation=new AppReference.imageOrientation();
							imageOrientation.execute(orientation,filePath);
							Bitmap bitMap = callDisp.ResizeImage(filePath);
							if (bitMap != null) {
								Log.i("handsketch123", "bitmap not null");
//						Bitmap output = bitMap.createBitmap(200,200,
//								Bitmap.Config.ARGB_8888);
								Log.i("handsketch123", "bitmap name : " + bitMap);
								//drawView.setImage(bitMap);
								int h = drawView.getHeight(); // 320; // Height in pixels
								int w = drawView.getWidth();// 480; // Width in pixels
								drawView.setImage(Bitmap.createScaledBitmap(bitMap, w, h, true));
							}
						}
					}
				}
			} else if (requestCode == GALLERY_REQUEST
					&& resultCode == RESULT_OK && data != null) {
				imageSelectedOption=1;
				Uri selectedImage = data.getData();
				SelectedGallaryImage = selectedImage;
//				String[] filePathColumn = { MediaStore.Images.Media.DATA };
//				Cursor cursor = getContentResolver().query(selectedImage,
//						filePathColumn, null, null, null);
//				cursor.moveToFirst();
//
//				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//				String picturePath = cursor.getString(columnIndex);
//				cursor.close();

				// board=BitmapFactory.decodeFile(picturePath);
//				int h = drawView.getHeight(); // 320; // Height in pixels
//				int w = drawView.getWidth();// 480; // Width in pixels
//				board = Bitmap.createScaledBitmap(
//						BitmapFactory.decodeFile(picturePath), w, h, true);
//				drawView.setImage(board);
//				Log.i("handsketch123", "picturePath--->"+picturePath);

				int h = drawView.getHeight(); // 320; // Height in pixels
				int w = drawView.getWidth();// 480; // Width in pixels
				board = Bitmap.createScaledBitmap(
						BitmapFactory.decodeFile(getPath(selectedImage)), w, h, true);

				drawView.setImage(board);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									CallDispatcher.handsketch_edit = false;
									setCleared(true);

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
				if (CallDispatcher.handsketch_edit && isCleared)
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

	private void setDefault()
	{
		drawBtn.setBackgroundColor(getResources().getColor(R.color.black2));
		eraseBtn.setBackgroundColor(getResources().getColor(R.color.black2));
		galleryButt.setBackgroundColor(getResources().getColor(R.color.black2));
		undoButt.setBackgroundColor(getResources().getColor(R.color.black2));
		redoButt.setBackgroundColor(getResources().getColor(R.color.black2));
		maxBtn.setBackgroundColor(getResources().getColor(R.color.black2));
		minBtn.setBackgroundColor(getResources().getColor(R.color.black2));
	}


	private String getPath(Uri uri) {
		if( uri == null ) {
			return null;
		}

		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor;
		if(Build.VERSION.SDK_INT >19)
		{
			// Will return "image:x*"
			String wholeID = DocumentsContract.getDocumentId(uri);
			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];
			// where id is equal to
			String sel = MediaStore.Images.Media._ID + "=?";

			cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					projection, sel, new String[]{ id }, null);
		}
		else
		{
			cursor = getContentResolver().query(uri, projection, null, null, null);
		}
		String path = null;
		try
		{
			int column_index = cursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index).toString();
			cursor.close();
		}
		catch(NullPointerException e) {
              e.printStackTrace();
		}
		return path;
	}

}
