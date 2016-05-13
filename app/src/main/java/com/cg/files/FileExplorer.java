package com.cg.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class FileExplorer extends Activity {

	// Stores names of traversed directories
	ArrayList<String> str = new ArrayList<String>();

	// Check if the first level of the directory structure is the one showing
	private Boolean firstLvl = true;

	private static final String TAG = "F_PATH";

	private String copyFilePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/COMMedia/";

	private Item[] fileList;
	private File path = new File(Environment.getExternalStorageDirectory() + "");
	private String chosenFile;
	private static final int DIALOG_LOAD_FILE = 1000;
	private Button btnDone = null;
	private Button btnBack = null;
	private TextView title = null;
	private Button selectFile = null;
	private Context context = null;
	private CallDispatcher callDisp = null;

	ListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.custom_title1);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.custom_title1);
			context = this;
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
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
			btnDone = (Button) findViewById(R.id.btncomp);
			btnDone.setVisibility(View.GONE);
			btnBack = (Button) findViewById(R.id.settings);
			btnBack.setText(SingleInstance.mainContext.getResources().getString(R.string.back));
			btnBack.setBackgroundResource(R.color.title);
			btnBack.setTextSize(14);
			title = (TextView) findViewById(R.id.note_date);
			title.setText(SingleInstance.mainContext.getResources().getString(R.string.file_explorer));
			setContentView(R.layout.fileexplorer);
			selectFile = (Button) findViewById(R.id.selectFile);
			CallDispatcher.pdialog = new ProgressDialog(context);
			selectFile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//callDisp.showprogress(CallDispatcher.pdialog, context);
					loadFileList();
					showDialog(DIALOG_LOAD_FILE);
				}
			});
			btnBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});

			loadFileList();

			showDialog(DIALOG_LOAD_FILE);
			Log.d(TAG, path.getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadFileList() {
		try {
			path.mkdirs();

			// Checks whether path exists
			if (path.exists()) {
				FilenameFilter filter = new FilenameFilter() {
					@Override
					public boolean accept(File dir, String filename) {
						File sel = new File(dir, filename);
						// Filters based on whether the file is hidden or not
						return (sel.isFile() || sel.isDirectory())
								&& !sel.isHidden();

					}
				};

				String[] fList = path.list(filter);
				fileList = new Item[fList.length];
				for (int i = 0; i < fList.length; i++) {
					fileList[i] = new Item(fList[i], R.drawable.file_icon);

					// Convert into file path
					File sel = new File(path, fList[i]);

					// Set drawables
					if (sel.isDirectory()) {
						fileList[i].icon = R.drawable.directory_icon;
						Log.d("DIRECTORY", fileList[i].file);
					} else {
						Log.d("FILE", fileList[i].file);

					}

				}

				if (!firstLvl) {
					Item temp[] = new Item[fileList.length + 1];
					for (int i = 0; i < fileList.length; i++) {
						temp[i + 1] = fileList[i];
					}
					temp[0] = new Item("Up", R.drawable.directory_up);
					fileList = temp;
				}
			} else {
				Log.e(TAG, "path does not exist");
			}

			adapter = new ArrayAdapter<Item>(this,
					android.R.layout.select_dialog_item, android.R.id.text1,
					fileList) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					try {
						// creates view
						View view = super
								.getView(position, convertView, parent);
						TextView textView = (TextView) view
								.findViewById(android.R.id.text1);

						// put the image on the text view
						textView.setCompoundDrawablesWithIntrinsicBounds(
								fileList[position].icon, 0, 0, 0);

						// add margin between image and text (support various
						// screen
						// densities)
						int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
						textView.setCompoundDrawablePadding(dp5);

						return view;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				}

			};
			callDisp.cancelDialog();
		} catch (Exception e) {
			Log.e(TAG, "unable to write on the sd card ");
		}
	}

	private class Item {
		public String file;
		public int icon;

		public Item(String file, Integer icon) {
			this.file = file;
			this.icon = icon;
		}

		@Override
		public String toString() {
			return file;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		try {
			Dialog dialog = null;
			AlertDialog.Builder builder = new Builder(this);

			if (fileList == null) {
				Log.e(TAG, "No files loaded");
				dialog = builder.create();
				return dialog;
			}

			switch (id) {
			case DIALOG_LOAD_FILE:
				builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.choose_your_file));
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((Dialog) dialog)
										.setCanceledOnTouchOutside(true);
								chosenFile = fileList[which].file;
								File sel = new File(path + "/" + chosenFile);
								if (sel.isDirectory()) {
									firstLvl = false;

									// Adds chosen directory to list
									str.add(chosenFile);
									fileList = null;
									path = new File(sel + "");

									loadFileList();

									removeDialog(DIALOG_LOAD_FILE);
									showDialog(DIALOG_LOAD_FILE);
									Log.d(TAG, path.getAbsolutePath());

								}

								// Checks if 'up' was clicked
								else if (chosenFile.equalsIgnoreCase("up")
										&& !sel.exists()) {

									// present directory removed from list
									String s = str.remove(str.size() - 1);

									// path modified to exclude present
									// directory
									path = new File(path.toString().substring(
											0, path.toString().lastIndexOf(s)));
									fileList = null;

									// if there are no more directories in the
									// list, then
									// its the first level
									if (str.isEmpty()) {
										firstLvl = true;
									}
									loadFileList();

									removeDialog(DIALOG_LOAD_FILE);
									showDialog(DIALOG_LOAD_FILE);
									Log.d(TAG, path.getAbsolutePath());

								}
								// File picked
								else {
									// Perform action with file picked
									try {
										if (sel.isFile()) {
											File ComFile = new File(
													copyFilePath);

											if (!ComFile.exists()) {
												ComFile.mkdir();
											}
											String destinationPath = copyFilePath
													+ chosenFile;

											File destination = new File(
													destinationPath);
											try {
												InputStream in = new FileInputStream(
														sel.getAbsolutePath());

												OutputStream out = new FileOutputStream(
														destinationPath);

												// Copy the bits from instream
												// to outstream
												byte[] buf = new byte[1024];
												int len;
												while ((len = in.read(buf)) > 0) {
													out.write(buf, 0, len);
												}
												in.close();
												out.close();
											} catch (IOException e) {
												e.printStackTrace();
											}

											int n = chosenFile.lastIndexOf(".");
											String name = chosenFile;
											String fileName, fileExt;

											if (n == -1)
												return;

											else {
												fileName = name.substring(0, n);
												fileExt = name.substring(n);
												// if
												// (!fileExt.equals(".docx")
												// &&
												// !fileExt.equals(".pdf")
												// && !fileExt.equals("doc")
												// && !fileExt.equals("xls")
												// &&
												// !fileExt.equals("xlsx"))
												//
												// return;

											}
											if (WebServiceReferences.contextTable
													.containsKey("Component")) {
												((ComponentCreator) WebServiceReferences.contextTable
														.get("Component"))
														.saveFileFromGallery(
																destinationPath,
																fileName, "",
																"", "", "", "",
																0);
												Log.i("FILE",
														"==> path "
																+ sel.getPath());

											}

											finish();

										}
									} catch (Exception e) {
										Log.e("sharefile",
												"===> " + e.getMessage());
									}
								}

							}
						});
				break;
			}
			dialog = builder.show();
			return dialog;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
@Override
	protected void onResume() {
		super.onResume();
		AppMainActivity.inActivity = this;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			// TODO Auto-generated method stub
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				if ((keyCode == KeyEvent.KEYCODE_BACK)) {
					finish();
				}
			}
			finish();
			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}