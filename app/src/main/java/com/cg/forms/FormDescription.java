package com.cg.forms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.lib.model.FormAttributeBean;
import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bean.EditFormBean;
import com.bean.FormInfoBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.group.chat.GroupChatActivity;
import com.main.AppMainActivity;
import com.main.FormsFragment;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;

public class FormDescription extends Activity implements OnClickListener {
	private Button savebtn, cancelbtn, IMRequest, btn_notification;
	private EditText formName, formDescription;
	private ArrayList<InputsFields> formFieldsList = new ArrayList<InputsFields>();
	private ArrayList<String> field_list = new ArrayList<String>();
	private ArrayList<String> field = null;
	private static String[] fields = null;
	private HashMap<String, String> fieldDatayptes = new HashMap<String, String>();
	private Context context;
	private Handler handler = new Handler();
	private CallDispatcher callDisp = null;
	private String fName = null;
	private Handler wservice_handler = null;
	private static String[] types = null;
	private static String[] fieldvalue = null;
	private static String[] fieldname = null;
	private static String formname = "";
	private ArrayList<String[]> details = null;
	private ProgressDialog dialog = null;
	public boolean isUpload = true;
	private static String instruction;
	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();
	boolean issuccessresult = true;
	private String FormDescription = "";
	private String FormIconPath = "";
	private AlertDialog alert = null;
	private String blob_path;
	private ImageView FormIconImage = null;
	private String upload = null;
	private Bitmap bitmap = null;
	private String formICON;
	private boolean isChat = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.form_description);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
		CallDispatcher.pdialog = new ProgressDialog(context);
		IMRequest = (Button) findViewById(R.id.im);

		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);

		WebServiceReferences.contextTable.put("locbusy", this);
		btn_notification = (Button) findViewById(R.id.notification);
		callDisp.startWebService(
				getResources().getString(R.string.service_url), "80");
		savebtn = (Button) findViewById(R.id.btn_save);
		savebtn.setOnClickListener(this);
		cancelbtn = (Button) findViewById(R.id.btn_cancel);
		cancelbtn.setOnClickListener(this);
		formName = (EditText) findViewById(R.id.form_name);
		formDescription = (EditText) findViewById(R.id.form_desc);
		FormIconImage = (ImageView) findViewById(R.id.form_img);
		FormIconImage.setOnClickListener(this);
		formFieldsList = CallDispatcher.inputFieldList;
		context = this;
		isChat = getIntent().getExtras().getBoolean("isChat", false);
		WebServiceReferences.contextTable.put("formdesc", context);
		wservice_handler = new Handler();
		fName = formName.getText().toString().trim();
		details = new ArrayList<String[]>();
		field = new ArrayList<String>();

	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
	}

	private boolean beginalpha(String val) {

		if (val.length() > 0) {
			char c = val.charAt(0);
			if (!Character.isLetter(c)) {
				return false;
			}
		}

		return true;
	}

	void saveForm() {

		// TODO Auto-generated method stub
		if (isUpload) {
			if (!CallDispatcher.isWifiClosed) {

				if (formName.getText().toString().length() > 0 && !formName.getText().toString().equalsIgnoreCase("")) {
					if (beginalpha(formName.getText().toString().trim())) {

                        if (upload != null) {
                            if (upload.equals("1")) {
                                uploadConfiguredNote(formICON);
                            } else if (upload.equals("2")) {
                                uploadConfiguredNote(blob_path);
                            }
                        }

						if (issuccessresult) {

							for (InputsFields inputs : formFieldsList) {

								String[] values = { inputs.getFieldName(),
										inputs.getFieldType(),
										inputs.getValidData(),
										inputs.getDefaultValue(),
										inputs.getInstructions(),
										inputs.getErrorMsg() };
								details.add(values);

							}
							for (int a = 0; a < details.size(); a++) {

								fieldname = new String[6];
								fieldname = details.get(a);

								if (fieldname[1].equalsIgnoreCase("multimedia")) {
									field_list.add("blob_" + fieldname[0]);
									fieldDatayptes.put("blob_" + fieldname[0],
											"nvarchar(20)");
								} else if (fieldname[1]
										.equalsIgnoreCase("numeric")) {
									field_list.add(fieldname[0]);
									fieldDatayptes.put(fieldname[0], "INTEGER");
								} else if (fieldname[1]
										.equalsIgnoreCase("compute")) {
									if (fieldname[2].startsWith("NM")) {
										field_list.add(fieldname[0]);
										fieldDatayptes.put(fieldname[0],
												"INTEGER");
									} else {

										field_list.add(fieldname[0]);
										fieldDatayptes.put(fieldname[0],
												"nvarchar(20)");

									}
								}

								else {
									field_list.add(fieldname[0]);
									fieldDatayptes.put(fieldname[0],
											"nvarchar(20)");

								}

							}

							showprogress();

							field_list.add("uuid");
							fieldDatayptes.put("uuid", "nvarchar(20)");
							field_list.add("euuid");
							fieldDatayptes.put("euuid", "nvarchar(20)");

							field_list.add("uudate");
							fieldDatayptes.put("uudate", "nvarchar(20)");
							fields = field_list.toArray(new String[field_list
									.size()]);

							types = new String[field_list.size()];

							for (int i = 0; i < field_list.size(); i++) {

								if (fieldDatayptes.containsKey(field_list
										.get(i))) {

									if (fieldDatayptes.get(field_list.get(i))
											.equals("nvarchar(20)"))
										types[i] = "VARCHAR(45)";
									else if (fieldDatayptes.get(
											field_list.get(i))
											.equals("INTEGER"))
										types[i] = "int(10)";

								}

							}
							if (formDescription.getText().toString().length() > 0) {
								FormDescription = formDescription.getText()
										.toString();

							}
							String[] params = new String[4];
							params[0] = CallDispatcher.LoginUser.trim();
							params[1] = formName.getText().toString();
							params[2] = FormIconPath;
							params[3] = FormDescription;
							if (WebServiceReferences.running) {
								WebServiceReferences.webServiceClient
										.CreateFormAttribute(params, fields,
												types, details, context);

							} else {
								callDisp.startWebService(getResources()
										.getString(R.string.service_url), "80");

								WebServiceReferences.webServiceClient
										.CreateFormAttribute(params, fields,
												types, details, context);

							}

						} else {
							showprogress();

							if (formDescription.getText().toString().length() > 0) {
								FormDescription = formDescription.getText()
										.toString();

							}
							String[] params = new String[4];
							params[0] = CallDispatcher.LoginUser.trim();
							params[1] = formName.getText().toString();
							params[2] = FormIconPath;
							params[3] = FormDescription;
							if (WebServiceReferences.running) {
								WebServiceReferences.webServiceClient
										.CreateFormAttribute(params, fields,
												types, details, context);

							} else {
								callDisp.startWebService(getResources()
										.getString(R.string.service_url), "80");
								WebServiceReferences.webServiceClient
										.CreateFormAttribute(params, fields,
												types, details, context);

							}

						}
					} else {

						Toast.makeText(
								context,
								SingleInstance.mainContext.getResources()
										.getString(R.string.form_name_alpha),
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(
							context,
							SingleInstance.mainContext.getResources()
									.getString(R.string.enter_form_name),
							Toast.LENGTH_LONG).show();

				}
			} else {

				ShowError(
						SingleInstance.mainContext.getResources().getString(
								R.string.network_err),
						SingleInstance.mainContext.getResources().getString(
								R.string.network_unreachable));
			}
		} else {
			Toast.makeText(
					context,
					SingleInstance.mainContext.getResources().getString(
							R.string.icon_upload_failed), Toast.LENGTH_LONG)
					.show();
		}

	}

	public void notifyWebServiceResponse(final Object obj) {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				if (obj instanceof ArrayList) {
					@SuppressWarnings("unchecked")
					ArrayList<String[]> responses = (ArrayList<String[]>) obj;

					CallDispatcher.inputFieldList.clear();

					String[] response = responses.get(0);
					String id = response[0];
					fName = response[1];
					String formtime = response[3];
					String fsid = response[4];
					String status = "1";

					String errortip = null;
					String column = null;
					String insertQuery = "insert into formslookup(owner,tablename,tableid,rowcount,formtime,status,Formiconname,Formdescription)"
							+ "values('"
							+ CallDispatcher.LoginUser
							+ "','"
							+ fName
							+ "','"
							+ id
							+ "','"
							+ "0"
							+ "','"
							+ formtime
							+ "','"
							+ status
							+ "','"
							+ FormIconPath
							+ "','" + FormDescription + "')";

					if (DBAccess.getdbHeler().ExecuteQuery(insertQuery)) {
						field_list.add("recorddate");
						fieldDatayptes.put("recorddate", "nvarchar(20)");
						field_list.add("status");
						fieldDatayptes.put("status", "nvarchar(20)");

						String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
								+ "values('"
								+ fsid
								+ "','"
								+ CallDispatcher.LoginUser
								+ "','"
								+ id
								+ "','"
								+ CallDispatcher.LoginUser
								+ "','"
								+ "A04"
								+ "','"
								+ "S02"
								+ "','"
								+ ""
								+ "','"
								+ formtime
								+ "','" + formtime + "')";

						if (DBAccess.getdbHeler().ExecuteQuery(insertQuery1)) {

							for (int i = 0; i < field_list.size(); i++) {
								String obj = "[" + field_list.get(i) + "]";

								field.add(obj);
							}
							String formName = "[" + fName + "_" + id + "]";
							if (DBAccess.getdbHeler().createFormTable(field,
									formName, fieldDatayptes)) {
								for (int i = 1; i < responses.size(); i++) {
									String[] useraattribute = responses.get(i);
									
									String attributeid = useraattribute[2];
									DBAccess.getdbHeler()
											.saveOrUpdateOwnerFormField(id,
													attributeid, "F3");

								}
//								DBAccess.getdbHeler().getBoolean();
								// formname = "[" + fName + "_" + id + "]";

								for (int a = 0; a < details.size(); a++) {
									fieldvalue = new String[6];
									fieldvalue = details.get(a);

									if (fieldvalue[4].length() != 0) {

										if (fieldvalue[4].endsWith(",")) {
											instruction = fieldvalue[4]
													.substring(0, fieldvalue[4]
															.length() - 1);

										} else {
											instruction = fieldvalue[4];
										}

									} else {

										instruction = "";
									}

									if (fieldvalue[5].length() != 0) {
										errortip = fieldvalue[5];

									} else {
										errortip = "";
									}

									if (fieldvalue[1]
											.equalsIgnoreCase("multimedia")) {

										column = "blob_" + fieldvalue[0];

									} else {
										column = fieldvalue[0];

									}

									FormInfoBean fIBean = new FormInfoBean();
									fIBean.setFormName(formName);
									fIBean.setColumn("[" + column + "]");
									fIBean.setEntryMode(fieldvalue[1]);
									fIBean.setValidData(fieldvalue[2]);
									fIBean.setDefaultValue(fieldvalue[3]);
									fIBean.setInstruction(instruction);
									fIBean.setErrorTip(errortip);
									fIBean.setAttributeId("");
									long infoId = DBAccess.getdbHeler()
											.insertAttribute(fIBean);
									Log.i("formfield123", "form infoid : "
											+ infoId);
									if (infoId > 0) {
										String[] useraattribute = null;

										for (int i = 1; i < responses.size(); i++) {

											useraattribute = responses.get(i);
											String columName = useraattribute[1];
											String attributeid = useraattribute[2];

											int value = Integer
													.parseInt(attributeid);
											fIBean.setAttributeId(String
													.valueOf(value));
											fIBean.setColumn("[" + columName
													+ "]");
											if (DBAccess.getdbHeler()
													.updatesAttribute(fIBean)) {

												finish();

											} else {

											}
										}
									}
								}

							}
						}

						if (WebServiceReferences.contextTable
								.containsKey("frmcreator"))
							((AddNewForm) WebServiceReferences.contextTable
									.get("frmcreator")).finish();

						if (SingleInstance.contextTable.containsKey("forms")) {
							FormsFragment quickActionFragment = FormsFragment
									.newInstance(context);

							quickActionFragment.notifylistChanged(fName, id,
									CallDispatcher.LoginUser);

						}
						if (isChat) {
							GroupChatActivity gChatActivity = (GroupChatActivity) SingleInstance.contextTable
									.get("groupchat");
							if (gChatActivity != null) {
								gChatActivity.notifyFormCreatedOrEdited(id,
										fName);
							}
						}
						cancelDialog(true);
						InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputManager.hideSoftInputFromWindow(getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
						finish();
					}

				} else if (obj instanceof WebServiceBean) {
					WebServiceBean service_bean = (WebServiceBean) obj;
					issuccessresult = false;
					Log.i("formresponse123",
							"response : " + service_bean.getText());
					showAlert("Response", service_bean.getText());
					cancelDialog(true);
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} else if (obj instanceof String) {
					String message = (String) obj;
					showToast(message);
					cancelDialog(true);
				}
			}
		});
	}


	public void notifyWebServiceGetAttributeResponse(final Object obj,
			final String isEditFormField) {
		wservice_handler.post(new Runnable() {

            @Override
            public void run() {
                cancelDialog(true);
                if (obj instanceof ArrayList) {

                    ArrayList response = (ArrayList) obj;

                    for (int i = 0; i < response.size(); i++) {

                        final Object obj = response.get(i);
                        if (obj instanceof FormAttributeBean) {
                            FormAttributeBean faBean = (FormAttributeBean) response
                                    .get(i);
                            String tableName = "[" + faBean.getTableName()
                                    + "_" + faBean.getFormid() + "]";
                            String columnName = "[" + faBean.getFieldname()
                                    + "]";
                            // String insertQueryinfotbl =
                            // "insert into forminfo(tablename,column,entrymode,validdata,defaultvalue,instruction,errortip)"
                            // + "values('"
                            // + "["
                            // + bib.getTableName()
                            // + "_"
                            // + bib.getFormid()
                            // + "]"
                            // + "','"
                            // + "["
                            // + bib.getFieldname()
                            // + "]"
                            // + "','"
                            // + bib.getEntry()
                            // + "','"
                            // + bib.getDatavalidation()
                            // + "','"
                            // + bib.getDefaultvalue()
                            // + "','"
                            // + bib.getInstruction()
                            // + "','"
                            // + bib.getErrortip() + "')";

                            String instruction = faBean.getInstruction();

                            if (instruction.length() != 0) {
                                String[] instructions = instruction.split(",");

                                for (int j = 0; j < instructions.length; j++) {
                                    File extStore = Environment
                                            .getExternalStorageDirectory();
                                    File myFile = new File(extStore
                                            .getAbsolutePath()
                                            + "/COMMedia/"
                                            + instructions[j]);

                                    if (!myFile.exists()) {
                                        callDisp.downloadOfflineresponse(
                                                instructions[j], "", "forms",
                                                "");

                                    }

                                }

                            }

                            if (DBAccess.getdbHeler().saveOrUpdateFormInfo(
                                    tableName, columnName, faBean) > 0) {

                            }
                            if (isEditFormField.equalsIgnoreCase("yes")) {
                                ArrayList<EditFormBean> eList = DBAccess
                                        .getdbHeler().getFormInfoDetails(
                                                tableName, columnName);
                                // DBAccess.getdbHeler().updateTableColumn(
                                // tableName, eList);
                            }

                        }
                    }

                } else if (obj instanceof WebServiceBean) {

                }
            }
        });
	}

	private void showAlert(String title, String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				FormDescription.this).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();
                // finish();
            }
        });

		// Showing Alert Message
		alertDialog.show();

	}

	private void showprogress() {

		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();

	}

	public void cancelDialog(boolean toast) {
		if (toast) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;

			}
			finish();

		} else {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.unable_to_connect_server),
						Toast.LENGTH_LONG).show();

			}

		}

	}

	@Override
	protected void onDestroy() {
		WebServiceReferences.contextTable.remove("formdesc");
		super.onDestroy();
	}

	public void fileUploading() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				btn_notification.setVisibility(View.VISIBLE);
			}
		});
		if (callDisp != null) {
			if (callDisp.isApplicationInBackground(this)) {
			}
		}
	}

	public void fileDownloading() {
		handler.post(new Runnable() {

            @Override
            public void run() {
                btn_notification.setVisibility(View.VISIBLE);
            }
        });
		if (callDisp != null) {
			if (callDisp.isApplicationInBackground(this)) {
			}
		}
	}

	public void ShowError(String Title, String Message) {
		AlertDialog confirmation = new AlertDialog.Builder(this).create();
		confirmation.setTitle(Title);
		confirmation.setMessage(Message);
		confirmation.setCancelable(true);
		confirmation.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		confirmation.show();
	}

	public void UploadingCompleted() {

		handler.post(new Runnable() {

			@Override
			public void run() {
				btn_notification.setVisibility(View.GONE);
				cancelDialog(false);

			}
		});
		if (callDisp != null) {
			if (callDisp.isApplicationInBackground(this)) {
			}
		}
	}

	public void downloadinCompleted() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				btn_notification.setVisibility(View.GONE);
			}
		});
		if (callDisp != null) {
			if (callDisp.isApplicationInBackground(this)) {
			}
		}
	}

	public void notifyFileUploadError() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				btn_notification.setVisibility(View.GONE);
				cancelDialog(false);

				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.upload_failed), Toast.LENGTH_LONG)
						.show();
			}
		});
		if (callDisp != null) {
			if (callDisp.isApplicationInBackground(this)) {
			}
		}
	}

	public void notifyFileDownloadError() {
		handler.post(new Runnable() {

            @Override
            public void run() {
                btn_notification.setVisibility(View.GONE);
            }
        });
		if (callDisp != null) {
			if (callDisp.isApplicationInBackground(this)) {
			}
		}
	}

	public void openGallery() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(intent, 19);
	}

	private void showBlobSelector() {
        fName = formName.getText().toString().trim();
        if(!(fName.equals(null) || fName.equals(""))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.create();
            builder.setTitle(SingleInstance.mainContext.getResources().getString(
                    R.string.connected_members));

            final CharSequence[] choiceList = {
                    SingleInstance.mainContext.getResources().getString(
                            R.string.from_gallery),
                    SingleInstance.mainContext.getResources().getString(
                            R.string.from_camera)};
            int selected = -1;
            builder.setSingleChoiceItems(choiceList, selected,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int index) {

                            OpenBlobInterface(index);
                            alert.cancel();
                        }
                    });
            alert = builder.create();
            alert.show();
        }else{
            Toast.makeText(
                    context,
                    SingleInstance.mainContext.getResources()
                            .getString(R.string.enter_form_name),
                    Toast.LENGTH_LONG).show();
        }
	}

	private void showToast(final String msg) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			}
		});

	}

	private static String getFileName() {
		String strFilename;
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHH_mm_ss");
		Date date = new Date();
		strFilename = dateFormat.format(date);
		return strFilename;
	}

	static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return (availableBlocks * blockSize) / 1024;
		} else {
			return -1;
		}
	}

	private void OpenBlobInterface(int selected_option) {
		switch (selected_option) {
		case 0:
			if (Build.VERSION.SDK_INT < 19) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, 2);
			} else {
				openGallery();
			}
			break;
		case 1:

			Long free_size = getAvailableExternalMemorySize();
			if (free_size > 0 && free_size >= 5120) {
				blob_path = "/sdcard/COMMedia/FormIcon_" + getFileName()
						+ ".jpg";
				// Intent intent = new Intent(context, MultimediaUtils.class);
				// intent.putExtra("filePath", blob_path);
				// intent.putExtra("requestCode", 1);
				// intent.putExtra("action", MediaStore.ACTION_IMAGE_CAPTURE);
				// intent.putExtra("createOrOpen", "create");
				// startActivity(intent);
				Intent intent = new Intent(context, CustomVideoCamera.class);
				intent.putExtra("filePath", blob_path);
				intent.putExtra("isPhoto", true);
				startActivityForResult(intent, 1);
			} else {
				showToast(SingleInstance.mainContext.getResources().getString(
						R.string.insufficient_memory));
			}

			break;

		default:
			break;
		}
	}

	private void uploadConfiguredNote(String path) {
		showprogress();
		callDisp.uploadofflineResponse(path, false, 1, "forms");
	}

	public String Copy(String path) {
		// TODO Auto-generated method stub

		String destinationImagePath = null;
		try {
			File sd = Environment.getExternalStorageDirectory();
			if (sd.canWrite()) {
				String external = Environment.getExternalStorageDirectory()
						.toString();

				destinationImagePath = "/COMMedia/FormIcon_" + getFileName()
						+ ".jpg";
				String filepath = path.replace(external, "");
				File source = new File(sd, filepath);
				File destination = new File(sd, destinationImagePath);
				if (source.exists()) {
					FileChannel src = new FileInputStream(source).getChannel();
					FileChannel dst = new FileOutputStream(destination)
							.getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();

				}
			}
		} catch (Exception e) {

		}

		return Environment.getExternalStorageDirectory().getPath()
				+ destinationImagePath;

	}

	private void setBlobImage() {
		FormIconPath.split("\\.");

		if (FormIconPath.contains(".jpg") || FormIconPath.contains(".png")) {
			bitmap = callDisp.ResizeImage(blob_path);
			if (bitmap != null) {
				bitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, false);
			}

		} else if (blob_path.contains("MVD_"))

		{
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.v_play);

		} else

		{
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.v_play);

		}
		if (bitmap != null) {
			FormIconImage.setImageBitmap(bitmap);
		} else {
			FormIconImage.setBackgroundResource(R.drawable.broken);
		}
	}

	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			switch (requestCode) {
			case 2:
				if (resultCode == Activity.RESULT_CANCELED) {

					blob_path = null;
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.choose_image));

				}

				else {
					if (data != null) {
						Uri selectedImageUri = data.getData();
						blob_path = getRealPathFromURI(context,
								selectedImageUri);
						File selected_file = new File(blob_path);
						int length = (int) selected_file.length() / 1048576;

						if (length <= 2) {
							formICON = Copy(blob_path);
							String[] image = formICON.split("/");
							FormIconPath = image[image.length - 1];
							upload = "1";
							setBlobImage();
						} else {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.large_image));
						}

					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.choose_image));
					}
				}

				break;

			case 19:
				if (resultCode == Activity.RESULT_CANCELED) {

					blob_path = null;
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.choose_image));

				}

				else {
					if (data != null) {
						// ParcelFileDescriptor parcelFileDescriptor;
						// try {
						// Uri selectedImagUri = data.getData();
						// // blob_path = getPath(selectedImagUri);
						// parcelFileDescriptor =
						// getContentResolver().openFileDescriptor(selectedImagUri,
						// "r");
						// FileDescriptor fileDescriptor =
						// parcelFileDescriptor.getFileDescriptor();
						// Bitmap image =
						// BitmapFactory.decodeFileDescriptor(fileDescriptor);
						// parcelFileDescriptor.close();
						// FormIconImage.setImageBitmap(image);
						// blob_path = getRealPathFromURI(context,
						// selectedImagUri);
						//
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						Uri uri = data.getData();
						if (uri != null) {
							try {
								if (uri == null) {
									blob_path = uri.getPath();
								} else {
									// get the id of the image selected by the
									// user
									String wholeID = DocumentsContract
											.getDocumentId(data.getData());
									String id = wholeID.split(":")[1];

									String[] projection = { MediaStore.Images.Media.DATA };
									String whereClause = MediaStore.Images.Media._ID
											+ "=?";
									Cursor cursor = getContentResolver().query(
											getUri(), projection, whereClause,
											new String[] { id }, null);
									if (cursor != null) {
										int column_index = cursor
												.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
										if (cursor.moveToFirst()) {
											blob_path = cursor
													.getString(column_index);
										}

										cursor.close();
									} else {
										blob_path = uri.getPath();
									}
								}
							} catch (Exception e) {
							}
						}
						Log.i("hs", "selecedImagePath" + blob_path);

						// blob_path = getRealPathFromURI(context,
						// selectedImageUri);
						// blob_path=callDisp.getRealPathFromURI(selectedImagUri);
						File selected_file = new File(blob_path);
						int length = (int) selected_file.length() / 1048576;

						if (length <= 2) {
							formICON = Copy(blob_path);
							String[] image = formICON.split("/");
							FormIconPath = image[image.length - 1];

							upload = "1";

							setBlobImage();
						} else {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.large_image));
						}

					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.choose_image));
					}
				}

				break;
			case 1:
				if (resultCode == Activity.RESULT_CANCELED) {

					blob_path = null;
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.choose_image));

				} else {
					String[] image = blob_path.split("/");
					FormIconPath = image[image.length - 1];
					File file = new File(blob_path);
					Bitmap bmp = callDisp.ResizeImage(blob_path);
					callDisp.changemyPictureOrientation(bmp, blob_path);
					if (bmp != null && !bmp.isRecycled())
						bmp.recycle();
					bmp = null;

					if (file.exists()) {
						upload = "2";

						setBlobImage();

					} else {

						blob_path = null;
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.choose_image));

					}
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Uri getUri() {
		String state = Environment.getExternalStorageState();
		if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

		return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	}

	public void notifyUploadIcon() {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				cancelDialog(true);

				if (!isUpload) {
					showAlert(SingleInstance.mainContext.getResources()
							.getString(R.string.file_upload),
							SingleInstance.mainContext.getResources()
									.getString(R.string.icon_upload_try_again));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == savebtn.getId()) {
			saveForm();
		}

		else if (v.getId() == FormIconImage.getId()) {
			showBlobSelector();

		}

		else if (v.getId() == cancelbtn.getId()) {
			InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			finish();
		}
	}

}
