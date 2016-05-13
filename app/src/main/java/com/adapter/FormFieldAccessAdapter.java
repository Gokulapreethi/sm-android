package com.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.lib.model.FormFieldSettingsBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.BuddyPermission;
import com.bean.DefaultPermission;
import com.bean.FormFieldBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.forms.FormFieldAccessActivity;
import com.util.SingleInstance;

public class FormFieldAccessAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private Vector<FormFieldBean> formFieldList;
	private Context context;

	public FormFieldAccessAdapter(Context context,
			Vector<FormFieldBean> formFieldList) {
		layoutInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.formFieldList = formFieldList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return formFieldList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return formFieldList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			final ViewHolder holder;

			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.field_access_list_row, null, false);
				holder = new ViewHolder();
				holder.fieldName = (TextView) convertView
						.findViewById(R.id.fieldname_text);
				holder.defaultAccess = (Button) convertView
						.findViewById(R.id.default_access_btn);
				holder.addAccess = (Button) convertView
						.findViewById(R.id.add_access_btn);
				holder.buddyContainer = (LinearLayout) convertView
						.findViewById(R.id.buddy_access_container);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final FormFieldBean ffBean = formFieldList.get(position);
			holder.fieldName.setText(ffBean.getFieldName());
			if (ffBean.getDefaultPermissionList() != null) {
				for (int k = 0; k < ffBean.getDefaultPermissionList().size(); k++) {
					final DefaultPermission dPermission = ffBean
							.getDefaultPermissionList().get(k);
					holder.defaultAccess.setText(dPermission
							.getDefaultPermission());
					holder.defaultAccess.setTag(dPermission);
					holder.addAccess.setTag(dPermission);
					final Vector<BuddyPermission> bList = dPermission
							.getBuddyPermissionList();
					Log.i("formfield123", "bList size : " + bList.size());
					holder.buddyContainer.setTag(k);
					holder.buddyContainer.removeAllViewsInLayout();
					for (int i = 0; i < bList.size(); i++) {
						final int pos = i;
						final BuddyPermission bPermission = bList.get(i);
						View view = layoutInflater.inflate(
								R.layout.buddyaccess_row, null, false);
						final Button selectBuddy = (Button) view
								.findViewById(R.id.select_buddy);
						final Button selectAccess = (Button) view
								.findViewById(R.id.select_access);
						Button deleteAccess = (Button) view
								.findViewById(R.id.delete_access_btn);
						selectBuddy.setText(bPermission.getBuddyName());
						selectBuddy.setTag(bPermission);
						selectAccess.setText(bPermission.getBuddyAccess());
						selectAccess.setTag(bPermission);
						selectAccess.setContentDescription(dPermission
								.getDefaultPermission());
						deleteAccess.setTag(bPermission);

						holder.buddyContainer.addView(view);
						deleteAccess.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								try {
									// TODO Auto-generated method stub'
									FormFieldSettingsBean settingsBean = new FormFieldSettingsBean();
									settingsBean.setFormId(ffBean.getFormId());
									settingsBean
											.setUserId(CallDispatcher.LoginUser);
									settingsBean.setMode("delete");
									Vector<BuddyPermission> bPermissionsList = new Vector<BuddyPermission>();
									bPermissionsList.add((BuddyPermission) view
											.getTag());
									if (DBAccess.getdbHeler().isRecordExists(
											"select * from formfieldbuddyaccess where formid='"
													+ ffBean.getFormId()
													+ "' and attributeid='"
													+ dPermission
															.getAttributeId()
													+ "' and accessiblebuddy='"
													+ bPermission
															.getBuddyName()
													+ "'")) {
										Vector<DefaultPermission> dPermissionList = new Vector<DefaultPermission>();
										DefaultPermission defaultPermission = new DefaultPermission();
										defaultPermission
												.setAttributeId(dPermission
														.getAttributeId());
										defaultPermission
												.setDefaultPermission(dPermission
														.getDefaultPermission());
										defaultPermission
												.setBuddyPermissionList(bPermissionsList);
										dPermissionList.add(defaultPermission);
										WebServiceReferences.webServiceClient
												.setorDeleteFormFieldSettings(
														settingsBean,
														dPermissionList,
														context);
									}
									DBAccess.getdbHeler()
											.deleteFormFieldBuddyAccess(
													ffBean.getFormId(),
													dPermission
															.getAttributeId(),
													((BuddyPermission) view
															.getTag())
															.getBuddyName());
									dPermission.getBuddyPermissionList()
											.remove((BuddyPermission) view
													.getTag());
									notifyDataSetChanged();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						selectBuddy.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								try {
									// TODO Auto-generated method stub
									ArrayList<String> buddyNamesList = DBAccess
											.getdbHeler()
											.getBuddyNamesFormFieldSettings(
													CallDispatcher.LoginUser,
													selectBuddy.getText()
															.toString(),
													ffBean.getFormId());
									if (buddyNamesList != null
											&& buddyNamesList.size() > 0) {
										showBuddyDialog(buddyNamesList,
												selectBuddy, v);

									} else {
										showToast("Sorry no buddies to select");
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});
						selectAccess.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								// TODO Auto-generated method stub
								showAccessDialog(selectAccess.getText()
										.toString(), view
										.getContentDescription().toString(),
										selectAccess, view);
							}
						});
					}

				}
				holder.addAccess.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							// TODO Auto-generated method stub
							DefaultPermission dPermission = (DefaultPermission) v
									.getTag();
							if (dPermission == null) {
								dPermission = new DefaultPermission();
								BuddyPermission bPermission = new BuddyPermission();
								bPermission.setBuddyName("Select Buddy");
								bPermission.setBuddyAccess("Select Access");
								Vector<BuddyPermission> bList = new Vector<BuddyPermission>();
								bList.add(bPermission);
								dPermission.setBuddyPermissionList(bList);
								notifyDataSetChanged();
							}
							if (dPermission.getBuddyPermissionList() != null) {
								BuddyPermission bPermission = new BuddyPermission();
								bPermission.setBuddyName("Select Buddy");
								bPermission.setBuddyAccess("Select Access");
								dPermission.getBuddyPermissionList().add(
										bPermission);
								notifyDataSetChanged();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				holder.defaultAccess.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						showDefaultAccessDialog(holder.defaultAccess.getText()
								.toString(), holder.defaultAccess, view);
					}
				});
			}

			return convertView;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private class ViewHolder {
		public TextView fieldName;
		public Button defaultAccess;
		public Button addAccess;
		public LinearLayout buddyContainer;

	}

	private void showToast(final String message) {
		Toast.makeText(SingleInstance.mainContext, message, Toast.LENGTH_LONG)
				.show();

	}

	private void showBuddyDialog(ArrayList<String> buddyNamesList,
			final Button button, final View view) {
		AlertDialog alert = null;
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();
			builder.setTitle("Select Buddy");
			int selected = -1; // does not select anything
			final String[] choiceList = buddyNamesList
					.toArray(new String[buddyNamesList.size()]);
			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							button.setText(choiceList[which]);
							BuddyPermission bPermission = (BuddyPermission) view
									.getTag();
							bPermission.setBuddyName(choiceList[which]);
							notifyDataSetChanged();
							dialog.dismiss();
						}
					});
			alert = builder.create();
			if (choiceList != null) {
				if (choiceList.length != 0) {
					alert.show();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void showDefaultAccessDialog(String access, final Button button,
			final View view) {
		AlertDialog alert = null;
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();
			builder.setTitle("Select Access");
			int selected = -1; // does not select anything
			String[] finalAccessList = null;
			if (access.equalsIgnoreCase("View")) {
				String[] tempList = { "Modify", "None" };
				finalAccessList = tempList;
			} else if (access.equalsIgnoreCase("Modify")) {
				String[] tempList = { "View", "None" };
				finalAccessList = tempList;
			} else if (access.equalsIgnoreCase("None")) {
				String[] tempList = { "View", "Modify" };
				finalAccessList = tempList;
			} else {
				String[] tempList = { "View", "Modify", "None" };
				finalAccessList = tempList;
			}

			if (finalAccessList != null && finalAccessList.length > 0) {
				final String[] accessList = finalAccessList;
				builder.setSingleChoiceItems(accessList, selected,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								button.setText(accessList[which]);
								DefaultPermission dPermission = (DefaultPermission) view
										.getTag();
								if (dPermission != null) {
									dPermission
											.setDefaultPermission(accessList[which]);
								} else {
									dPermission = new DefaultPermission();
									dPermission
											.setDefaultPermission(accessList[which]);
									view.setTag(dPermission);
								}
								notifyDataSetChanged();
								dialog.dismiss();
							}
						});
				alert = builder.create();
				if (finalAccessList != null) {
					if (finalAccessList.length != 0) {
						alert.show();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void showAccessDialog(String access, String defaultAccess,
			final Button button, final View view) {
		AlertDialog alert = null;
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();
			builder.setTitle("Select Access");
			int selected = -1; // does not select anything
			String[] choiceList = null;
			if (access.equalsIgnoreCase("View")) {
				String[] tempList = { "Modify", "None" };
				choiceList = tempList;
			} else if (access.equalsIgnoreCase("Modify")) {
				String[] tempList = { "View", "None" };
				choiceList = tempList;
			} else if (access.equalsIgnoreCase("None")) {
				String[] tempList = { "View", "Modify" };
				choiceList = tempList;
			} else {
				String[] tempList = { "View", "Modify", "None" };
				choiceList = tempList;
			}
			List<String> tempStringList = Arrays.asList(choiceList);
			List<String> finalAccessList = new ArrayList<String>();
			for (int j = 0; j < tempStringList.size(); j++) {
				if (!tempStringList.get(j).equalsIgnoreCase(defaultAccess)) {
					finalAccessList.add(tempStringList.get(j));
				}
			}
			if (finalAccessList != null && finalAccessList.size() > 0) {
				final String[] accessList = finalAccessList
						.toArray(new String[finalAccessList.size()]);
				builder.setSingleChoiceItems(accessList, selected,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								button.setText(accessList[which]);
								BuddyPermission bPermission = (BuddyPermission) view
										.getTag();
								bPermission.setBuddyAccess(accessList[which]);
								dialog.dismiss();
							}
						});
				alert = builder.create();
				if (choiceList != null) {
					if (choiceList.length != 0) {
						alert.show();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void saveFormFieldSettings() {
		try {
			// TODO Auto-generated method stub'

			// Vector<DefaultPermission> defaultPermissionList = ffBean
			// .getDefaultPermissionList();
			// for (DefaultPermission dPermissionbean : ffBean
			// .getDefaultPermissionList()) {
			// Vector<BuddyPermission> bPermissionList = dPermissionbean
			// .getBuddyPermissionList();
			// if (checksave(bPermissionList)) {
			//

			if (checksave1(formFieldList)) {

				for (FormFieldBean ffBean : formFieldList) {

					((FormFieldAccessActivity) context).showProgress();
					FormFieldSettingsBean settingsBean = new FormFieldSettingsBean();
					settingsBean.setFormId(ffBean.getFormId());
					settingsBean.setUserId(CallDispatcher.LoginUser);
					settingsBean.setMode("update");
					WebServiceReferences.webServiceClient
							.setorDeleteFormFieldSettings(settingsBean,
									ffBean.getDefaultPermissionList(), context);
					for (DefaultPermission dPermission : ffBean
							.getDefaultPermissionList()) {
						DBAccess.getdbHeler().saveOrUpdateOwnerFormField(
								ffBean.getFormId(),
								dPermission.getAttributeId(),
								getPermissionId(dPermission
										.getDefaultPermission()));
						if (dPermission.getBuddyPermissionList() != null) {
							Vector<BuddyPermission> bList = dPermission
									.getBuddyPermissionList();
							for (BuddyPermission bPermission : bList) {
								DBAccess.getdbHeler()
										.saveOrUpdateIndividualFormField(
												ffBean.getFormId(),
												dPermission.getAttributeId(),
												bPermission.getBuddyName(),
												getPermissionId(bPermission
														.getBuddyAccess()));
							}
						}
					}

					notifyDataSetChanged();

				}
			}
			((FormFieldAccessActivity) context).isProgressStarted = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getPermissionId(String permission) {
		if (permission.equalsIgnoreCase("none")) {
			return "F1";
		} else if (permission.equalsIgnoreCase("view")) {
			return "F2";
		} else if (permission.equalsIgnoreCase("modify")) {
			return "F3";
		} else {
			return "";
		}
	}

	private boolean checksave(Vector<BuddyPermission> bPermissionList) {
		if (bPermissionList == null) {
			return true;
		} else if (bPermissionList != null) {
			for (BuddyPermission buddyPermission : bPermissionList) {
				if (buddyPermission.getBuddyName().trim()
						.equalsIgnoreCase("Select Buddy")) {
					showToast("Please Select buddy");
					return false;
				} else if (buddyPermission.getBuddyAccess().trim()
						.equalsIgnoreCase("Select Access")) {
					showToast("Please Select Buddy Access");
					return false;
				}
			}
		}
		return true;

	}

	private boolean checksave1(Vector<FormFieldBean> tempformFieldList) {

		for (FormFieldBean formFieldBean : tempformFieldList) {

			for (DefaultPermission dPermissionbean : formFieldBean
					.getDefaultPermissionList()) {
				Vector<BuddyPermission> bPermissionList = dPermissionbean
						.getBuddyPermissionList();
				if (bPermissionList != null) {
					for (BuddyPermission buddyPermission : bPermissionList) {
						if (!buddyPermission.getBuddyAccess().trim()
								.equalsIgnoreCase("Select Access")) {
							if (buddyPermission.getBuddyName().trim()
									.equalsIgnoreCase("Select Buddy")) {
								showToast("Please Select buddy");
								return false;
							}

						}
					}
				}
			}
		}

		return true;

	}
}
