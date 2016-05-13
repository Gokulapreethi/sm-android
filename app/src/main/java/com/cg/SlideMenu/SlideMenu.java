/*
 * A sliding menu for Android, very much like the Google+ and Facebook apps have.
 * 
 * Copyright (C) 2012 CoboltForge
 * 
 * Based upon the great work done by stackoverflow user Scirocco (http://stackoverflow.com/a/11367825/361413), thanks a lot!
 * The XML parsing code comes from https://github.com/darvds/RibbonMenu, thanks!
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cg.SlideMenu;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.cryse.android.controls.RoundedImageView;
import org.lib.model.KeepAliveBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cg.snazmed.R;
import com.cg.account.buddyView1;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.settings.MenuPage;
import com.util.SingleInstance;

public class SlideMenu extends LinearLayout {

	private final static String KEY_MENUSHOWN = "menuWasShown";

	private final static String KEY_STATUSBARHEIGHT = "statusBarHeight";

	private final static String KEY_SUPERSTATE = "superState";

	private CallDispatcher callDispatcher;

	private AlertDialog alert = null;

	private ToggleButton feedBackToggle;

	public class SlideBarAdapter extends ArrayAdapter {

		private TextView menu_item;

		private ImageView menu_icon;

		private Button btn_patchbuddy;

		private Button btn_patchim;

		private List<String> items = new ArrayList<String>();

		@SuppressWarnings("unchecked")
		public SlideBarAdapter(Context context, int textViewResourceId,
				List objects) {

			super(context, textViewResourceId, objects);

			this.items = objects;
		}

		/**
		 * To get the count of the listview items
		 */
		public int getCount() {
			return this.items.size();
		}

		@SuppressWarnings("finally")
		public View getView(int position, final View convertView,
				final ViewGroup parent) {
			View rowView = convertView;
			try {

				if (rowView == null) {
					LayoutInflater inflater = act.getLayoutInflater();
					rowView = inflater.inflate(R.layout.slidemenu_listitem,
							null);
				}
				Slidebean bean = (Slidebean) datas.get(position);
				menu_item = (TextView) rowView.findViewById(R.id.menu_label);
				menu_item.setFocusableInTouchMode(false);
				menu_item.setFocusable(false);
				menu_item.setClickable(false);
				menu_icon = (ImageView) rowView.findViewById(R.id.menu_icon);
				btn_patchbuddy = (Button) rowView.findViewById(R.id.patch_view);
				btn_patchbuddy.setVisibility(View.GONE);
				btn_patchim = (Button) rowView.findViewById(R.id.im_view);
				btn_patchim.setVisibility(View.GONE);
				feedBackToggle = (ToggleButton) rowView
						.findViewById(R.id.feedback_toggle);
				feedBackToggle.setVisibility(View.GONE);

				menu_item.setText(bean.getTitle());
				switch (bean.getId()) {
				case 1:
					menu_icon.setBackgroundResource(R.drawable.icon_contacts);
					// if (CallDispatcher.showBuddies.size() > 0) {
					// btn_patchbuddy.setVisibility(View.VISIBLE);
					// btn_patchbuddy.setText(Integer
					// .toString(CallDispatcher.showBuddies.size()));
					// } else
					// btn_patchbuddy.setVisibility(View.GONE);

					if (WebServiceReferences.Imcollection.size() > 0) {
						btn_patchim.setVisibility(View.VISIBLE);
						btn_patchim.setText(Integer
								.toString(WebServiceReferences.Imcollection
										.size()));
					} else
						btn_patchim.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				case 2:
					menu_icon.setBackgroundResource(R.drawable.userprofiles);
					btn_patchbuddy.setVisibility(View.GONE);
					btn_patchim.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				case 3:
					menu_icon.setBackgroundResource(R.drawable.invitesend);
					btn_patchbuddy.setVisibility(View.GONE);
					btn_patchim.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				case 4:
					menu_icon.setBackgroundResource(R.drawable.icon_notes);
					int size = callDispatcher.getdbHeler(
							act.getApplicationContext()).getUnreadnotesSize(
							CallDispatcher.LoginUser);
					if (size > 0) {
						btn_patchbuddy.setVisibility(View.VISIBLE);
						btn_patchbuddy.setText(Integer.toString(size));
					} else
						btn_patchbuddy.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				case 5:
					menu_icon.setBackgroundResource(R.drawable.starapp);
					btn_patchbuddy.setVisibility(View.GONE);
					btn_patchim.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				case 6:
					menu_icon.setBackgroundResource(R.drawable.avatar_main);
					btn_patchbuddy.setVisibility(View.GONE);
					btn_patchim.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				case 7:
					menu_icon
							.setBackgroundResource(R.drawable.menu_settings_icon);
					btn_patchbuddy.setVisibility(View.GONE);
					btn_patchim.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				case 8:
					menu_icon.setBackgroundResource(R.drawable.menu_qa_icon);
					btn_patchbuddy.setVisibility(View.GONE);
					btn_patchim.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				case 9:
					menu_icon.setBackgroundResource(R.drawable.forms_main);
					btn_patchbuddy.setVisibility(View.GONE);
					btn_patchim.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				case 10:
					menu_icon.setBackgroundResource(R.drawable.feed_back_icon);
					btn_patchbuddy.setVisibility(View.GONE);
					btn_patchim.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				case 11:
					menu_icon
							.setBackgroundResource(R.drawable.conference_buddy);
					btn_patchbuddy.setVisibility(View.GONE);
					int messageCount = SingleInstance.unreadCount.size()
							+ WebServiceReferences.Imcollection.size();
					if (messageCount > 0) {
						btn_patchim.setVisibility(View.VISIBLE);
						btn_patchim.setText(Integer.toString(messageCount));
					} else
						btn_patchim.setVisibility(View.GONE);
					feedBackToggle.setVisibility(View.GONE);
					break;
				default:
					break;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				return rowView;
			}

		}

	}

	public boolean menuShown = false;

	private int statusHeight;

	private static View menu;

	private static ViewGroup content;

	private static FrameLayout parent;

	private static int menuSize;

	private Activity act;

	private ArrayList<Slidebean> datas;

	private Drawable headerImage;

	private TranslateAnimation slideRightAnim;

	private TranslateAnimation slideMenuLeftAnim;

	private TranslateAnimation slideContentLeftAnim;

	private Context appcontext = null;

	private boolean dontAnimateContent = false;

	private SlideBarAdapter adapterToShow = null;

	private TextView tv_namestatus = null;

	private RoundedImageView profile_picture;

	private ImageView ib_loginlogout;

	private SlideMenuInterface.OnSlideMenuItemClickListener callback;

	/**
	 * Constructor used by the inflation apparatus. To be able to use the
	 * SlideMenu, call the {@link #init init()} method.
	 * 
	 * @param context
	 */
	public SlideMenu(Context context) {
		super(context);
		appcontext = context;
	}

	public boolean isMenuShowing() {
		return this.menuShown;
	}

	/**
	 * Constructor used by the inflation apparatus. To be able to use the
	 * SlideMenu, call the {@link #init init()} method.
	 * 
	 * @param attrs
	 */
	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructs a SlideMenu with the given menu XML.
	 * 
	 * @param act
	 *            The calling activity.
	 * @param menuResource
	 *            Menu resource identifier.
	 * @param cb
	 *            Callback to be invoked on menu item click.
	 * @param slideDuration
	 *            Slide in/out duration in milliseconds.
	 */
	public SlideMenu(Activity act, int menuResource,
			SlideMenuInterface.OnSlideMenuItemClickListener cb,
			int slideDuration) {
		super(act);
		// init(act, menuResource, cb, slideDuration);
	}

	/**
	 * Constructs an empty SlideMenu.
	 * 
	 * @param act
	 *            The calling activity.
	 * @param cb
	 *            Callback to be invoked on menu item click.
	 * @param slideDuration
	 *            Slide in/out duration in milliseconds.
	 */
	public SlideMenu(Activity act,
			SlideMenuInterface.OnSlideMenuItemClickListener cb,
			int slideDuration) {
		this(act, 0, cb, slideDuration);
	}

	/**
	 * Initializes the SlideMenu.
	 * 
	 * @param act
	 *            The calling activity.
	 * @param menuResource
	 *            Menu resource identifier, can be 0 for an empty SlideMenu.
	 * @param cb
	 *            Callback to be invoked on menu item click.
	 * @param slideDuration
	 *            Slide in/out duration in milliseconds.
	 */

	public void init(final Activity act, ArrayList<Slidebean> datas,
			SlideMenuInterface.OnSlideMenuItemClickListener cb,
			int slideDuration) {

		this.act = act;
		this.callback = cb;
		this.datas = datas;
		callDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldisp");
		menuSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				250, act.getResources().getDisplayMetrics());

		slideRightAnim = new TranslateAnimation(-menuSize, 0, 0, 0);
		slideRightAnim.setDuration(slideDuration);
		slideRightAnim.setFillAfter(true);
		slideMenuLeftAnim = new TranslateAnimation(0, -menuSize, 0, 0);
		slideMenuLeftAnim.setDuration(slideDuration);
		slideMenuLeftAnim.setFillAfter(true);
		slideContentLeftAnim = new TranslateAnimation(menuSize, 0, 0, 0);
		slideContentLeftAnim.setDuration(slideDuration);
		slideContentLeftAnim.setFillAfter(true);

	}

	/**
	 * Sets an optional image to be displayed on top of the menu.
	 * 
	 * @param d
	 */
	public void setHeaderImage(Drawable d) {
		headerImage = d;
	}

	/**
	 * Slide the menu in.
	 */
	public void show() {

		this.show(true);

	}

	/**
	 * Set the menu to shown status without displaying any slide animation.
	 */
	public void setAsShown() {
		this.show(false);
	}

	private void show(boolean animate) {

		try {

			Method getSupportActionBar = act.getClass().getMethod(
					"getSupportActionBar", (Class[]) null);
			Object sab = getSupportActionBar.invoke(act, (Object[]) null);
			sab.toString(); // check for null

			if (android.os.Build.VERSION.SDK_INT >= 11) {
				applyStatusbarOffset();
			}
		} catch (Exception es) {
			// es.printStackTrace();
			applyStatusbarOffset();
		}

		try {
			content = ((LinearLayout) act.findViewById(android.R.id.content)
					.getParent());
		} catch (ClassCastException e) {

			e.printStackTrace();
			content = (FrameLayout) act.findViewById(android.R.id.content);
		}

		if (!dontAnimateContent) {
			FrameLayout.LayoutParams parm = new FrameLayout.LayoutParams(-1,
					-1, 3);
			parm.setMargins(menuSize, 0, -menuSize, 0);
			content.setLayoutParams(parm);
			if (animate)
				content.startAnimation(slideRightAnim);
		}
		parent = (FrameLayout) content.getParent();
		LayoutInflater inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menu = inflater.inflate(R.layout.slidemenu, null);
		FrameLayout.LayoutParams lays = new FrameLayout.LayoutParams(-1, -1, 3);
		lays.setMargins(0, statusHeight, 0, 0);
		menu.setLayoutParams(lays);
		parent.addView(menu);

		ListView list = (ListView) act.findViewById(R.id.menu_listview);
		adapterToShow = new SlideBarAdapter(act, R.layout.slidemenu_listitem,
				datas);
		list.setAdapter(adapterToShow);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.e("slidemenu", "menu item clicked...." + callback);
				if (callback != null) {
					final Slidebean db = (Slidebean) adapterToShow
							.getItem(position);
					callback.onSlideMenuItemClick(db.getId(), view, act);
					hide();
				}

			}
		});

		tv_namestatus = (TextView) act.findViewById(R.id.menu_label);
		tv_namestatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (CallDispatcher.LoginUser != null
						&& CallDispatcher.isConnected)
					ShowView((TextView) arg0);
				else {
					if (!CallDispatcher.isConnected)
						Toast.makeText(getContext(),
								"Kindly check your internet connection",
								Toast.LENGTH_LONG).show();
				}
			}
		});
		profile_picture = (RoundedImageView) act
				.findViewById(R.id.userimageview);
		profile_picture
				.setImageBitmap(callDispatcher.getmyProfilePicture(null));
		ib_loginlogout = (ImageView) act.findViewById(R.id.ibtn_signout);

		if (CallDispatcher.LoginUser != null) {
			tv_namestatus.setText(CallDispatcher.LoginUser + "\n"
					+ loadCurrentStatus());
			ib_loginlogout.setImageBitmap(BitmapFactory.decodeResource(
					act.getResources(), R.drawable.logout_slide));
		} else {
			tv_namestatus.setText("");
			ib_loginlogout.setImageBitmap(BitmapFactory.decodeResource(
					act.getResources(), R.drawable.login_slide));
		}
		ib_loginlogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("Signin", "My Login User" + CallDispatcher.LoginUser);

				if (!WebServiceReferences.running)
					callDispatcher.startWebService(
							getResources().getString(R.string.service_url),
							"80");

				if (CallDispatcher.LoginUser != null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getContext());
					builder.setMessage(
							SingleInstance.mainContext.getResources()
									.getString(R.string.do_you_want_to_logout))
							.setCancelable(false)
							.setPositiveButton(
									SingleInstance.mainContext.getResources()
											.getString(R.string.yes),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											try {
												ProgressDialog progressDialog = new ProgressDialog(
														act);
												callDispatcher.showprogress(
														progressDialog, act);
												Log.d("Signin", "Inside if");
												// callDispatcher.logout(true);
												dialog.dismiss();
												if (CallDispatcher.LoginUser == null) {
													Intent intent = new Intent(
															act,
															buddyView1.class);
													act.startActivity(intent);
												}
												hide();

											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										}
									})
							.setNegativeButton(
									SingleInstance.mainContext.getResources()
											.getString(R.string.no),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getContext());
					builder.setMessage(
							SingleInstance.mainContext.getResources()
									.getString(R.string.do_you_want_to_login))
							.setCancelable(false)
							.setPositiveButton(
									SingleInstance.mainContext.getResources()
											.getString(R.string.yes),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											try {
												// Log.d("Signin",
												// "came to else");
												// SharedPreferences preferences
												// = PreferenceManager
												// .getDefaultSharedPreferences(act
												// .getApplicationContext());
												// String username = preferences
												// .getString("uname",
												// null);
												// String password = preferences
												// .getString("pword",
												// null);
												// if
												// (WebServiceReferences.contextTable
												// .containsKey("buddyView1")) {
												// ((buddyView1)
												// WebServiceReferences.contextTable
												// .get("buddyView1"))
												// .doSignin();
												//
												// } else {
												// if (username != null
												// && password != null) {
												// callDispatcher.SignIn(
												// username,
												// password);
												// hide();
												// } else {
												// hide();
												// Toast.makeText(
												// act.getApplicationContext(),
												// "Invalid Credentials",
												// Toast.LENGTH_LONG)
												// .show();
												// callDispatcher
												// .cancelDialog();
												// }
												// }
												buddyView1 buddy = (buddyView1) WebServiceReferences.contextTable
														.get("buddyView1");
												if (buddy == null) {
													Intent intent = new Intent(
															SipNotificationListener
																	.getCurrentContext(),
															buddyView1.class);
													SipNotificationListener
															.getCurrentContext()
															.startActivity(
																	intent);
												}
												dialog.dismiss();
												hide();
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										}
									})
							.setNegativeButton(
									SingleInstance.mainContext.getResources()
											.getString(R.string.no),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.dismiss();
											hide();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();

				}

			}
		});

		if (animate)
			menu.startAnimation(slideRightAnim);

		menu.findViewById(R.id.overlay).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						SlideMenu.this.hide();
					}
				});
		enableDisableViewGroup(content, false);

		menuShown = true;
	}

	public void notifynetworkChanged() {
		adapterToShow.notifyDataSetChanged();
	}

	/**
	 * Slide the menu out.
	 */
	public void hide() {
		if (feedBackToggle.isChecked()) {
			feedBackToggle.setChecked(true);
			AppReference.isWriteInFile = true;
		} else {
			feedBackToggle.setChecked(false);
			AppReference.isWriteInFile = false;
		}
		parent.removeView(menu);

		if (!dontAnimateContent) {

			FrameLayout.LayoutParams parm = null;
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				// over api level 11? add the margin
				parm = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT, 3);
				parm.setMargins(0, 0, 0, 0);
			} else {
				parm = (FrameLayout.LayoutParams) content.getLayoutParams();
				parm.setMargins(0, 0, 0, 0);
			}

			content.setLayoutParams(parm);
		}

		enableDisableViewGroup(content, true);

		menuShown = false;

	}

	private void applyStatusbarOffset() {

		Rect r = new Rect();
		Window window = act.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(r);
		statusHeight = r.top;
	}

	// originally:
	// http://stackoverflow.com/questions/5418510/disable-the-touch-events-for-all-the-views
	// modified for the needs here
	private void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
		int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = viewGroup.getChildAt(i);
			if (view.isFocusable())
				view.setEnabled(enabled);
			if (view instanceof ViewGroup) {
				enableDisableViewGroup((ViewGroup) view, enabled);
			} else if (view instanceof ListView) {
				if (view.isFocusable())
					view.setEnabled(enabled);
				ListView listView = (ListView) view;
				int listChildCount = listView.getChildCount();
				for (int j = 0; j < listChildCount; j++) {
					if (view.isFocusable())
						listView.getChildAt(j).setEnabled(false);
				}
			}
		}
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		try {

			Log.i("lff", "############## came to restoreinstance");

			if (state instanceof Bundle) {
				Bundle bundle = (Bundle) state;

				statusHeight = bundle.getInt(KEY_STATUSBARHEIGHT);

				if (bundle.getBoolean(KEY_MENUSHOWN))
					show(false); // show without animation

				super.onRestoreInstanceState(bundle
						.getParcelable(KEY_SUPERSTATE));
				return;
			}

			super.onRestoreInstanceState(state);

		} catch (NullPointerException e) {
			// in case the menu was not declared via XML but added from code
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_SUPERSTATE, super.onSaveInstanceState());
		bundle.putBoolean(KEY_MENUSHOWN, menuShown);
		bundle.putInt(KEY_STATUSBARHEIGHT, statusHeight);

		return bundle;
	}

	public void setBean(int pos, Slidebean bean) {
		if (datas != null) {
			if (datas.size() > pos) {
				datas.remove(pos);
				datas.add(pos, bean);
			}
		}
		adapterToShow.notifyDataSetChanged();
	}

	public void addNewItems(ArrayList<Slidebean> items) {
		Log.d("my log", "Came to add new items-->" + adapterToShow);

		datas.clear();
		datas.addAll(items);

	}

	public void removeItem(int position) {
		if (datas != null) {
			if (datas.size() > position)
				datas.remove(position);
		}
		adapterToShow.notifyDataSetChanged();
	}

	public void refreshItem() {
		if (adapterToShow != null) {
			adapterToShow.notifyDataSetChanged();
		}
	}

	private void changeFieldType(String type, TextView status) {

		status.setText(type);

		if (CallDispatcher.isConnected) {
			if (type.equals("Online")) {

				status.setText(CallDispatcher.LoginUser + "\n" + type);
				CallDispatcher.myStatus = "1";
				Toast.makeText(
						getContext(),
						SingleInstance.mainContext.getResources().getString(
								R.string.receive_all_services), 1).show();

			} else if (type.equals("Away")) {

				status.setText(CallDispatcher.LoginUser + "\n" + type);
				CallDispatcher.myStatus = "3";
				profile_picture
						.setBackgroundResource(R.drawable.icon_buddy_aoffline);

				Toast.makeText(
						getContext(),
						SingleInstance.mainContext.getResources().getString(
								R.string.not_receive_call_services), 1).show();

			} else if (type.equals("Stealth")) {
				status.setText(CallDispatcher.LoginUser + "\n" + type);
				CallDispatcher.myStatus = "4";
				profile_picture
						.setBackgroundResource(R.drawable.icon_buddy_aoffline);

				Toast.makeText(
						getContext(),
						SingleInstance.mainContext.getResources().getString(
								R.string.not_receive_call_msg_services), 1)
						.show();

			} else if (type.equals("Offline")) {

				status.setText(CallDispatcher.LoginUser + "\n" + type);
				CallDispatcher.myStatus = "0";
				profile_picture
						.setBackgroundResource(R.drawable.icon_buddy_aoffline);

				Toast.makeText(
						getContext(),
						SingleInstance.mainContext.getResources().getString(
								R.string.not_receive_call_broadcast_chat), 1)
						.show();
			} else if (type.equals("Busy")) {

				status.setText(CallDispatcher.LoginUser + "\n" + type);
				CallDispatcher.myStatus = "2";
				Toast.makeText(
						getContext(),
						SingleInstance.mainContext.getResources().getString(
								R.string.receive_all_services), 1).show();
			}

			KeepAliveBean aliveBean = callDispatcher.getKeepAliveBean();
			aliveBean.setKey("0");
			if (!WebServiceReferences.running) {
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(act
								.getApplicationContext());
				String url = preferences.getString("url", null);
				String port = preferences.getString("port", null);
				if (url != null && port != null)
					callDispatcher.startWebService(url, port);
				else
					callDispatcher.startWebService(
							getResources().getString(R.string.service_url),
							"80");
				url = null;
				port = null;
			}

			WebServiceReferences.webServiceClient.heartBeat(aliveBean);
			if (WebServiceReferences.contextTable.containsKey("menupage"))
				((MenuPage) WebServiceReferences.contextTable.get("menupage"))
						.loadCurrentStatus();
		} else {

			status.setText(CallDispatcher.LoginUser + "\n" + "Offline");
			CallDispatcher.myStatus = "0";

			Toast.makeText(
					getContext(),
					SingleInstance.mainContext.getResources().getString(
							R.string.network_error), 1).show();
		}
		callDispatcher.changeMyOnlineStatus();

	}

	AlertDialog.Builder builder = null;

	protected void ShowView(final TextView status) {
		// TODO Auto-generated method stub
		if (alert != null) {
			alert.cancel();
		}
		builder = new AlertDialog.Builder(getContext());
		builder.create();

		builder.setTitle(SingleInstance.mainContext.getResources().getString(
				R.string.change_status));
		final CharSequence[] choiceList = {
				SingleInstance.mainContext.getResources().getString(
						R.string.online),
				SingleInstance.mainContext.getResources().getString(
						R.string.busy),
				SingleInstance.mainContext.getResources().getString(
						R.string.away),
				SingleInstance.mainContext.getResources().getString(
						R.string.stealth) };

		builder.setItems(choiceList, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				changeFieldType(choiceList[which].toString(), status);
				alert.cancel();
			}
		});
		alert = builder.create();
		alert.show();
	}

	public String loadCurrentStatus() {
		String status = null;

		if (CallDispatcher.myStatus.equals("1")) {

			status = SingleInstance.mainContext.getResources().getString(R.string.online);
		} else if (CallDispatcher.myStatus.equals("3")) {
			status = SingleInstance.mainContext.getResources().getString(R.string.away);

		} else if (CallDispatcher.myStatus.equals("4")) {
			status = SingleInstance.mainContext.getResources().getString(R.string.stealth);

		} else if (CallDispatcher.myStatus.equals("0")) {
			status = SingleInstance.mainContext.getResources().getString(R.string.offline);
		} else if (CallDispatcher.myStatus.equals("2")) {
			status = SingleInstance.mainContext.getResources().getString(R.string.busy);

		}

		if (status == null)
			status = "";
		return status;
	}

	public void chageMyStatus() {
		if (tv_namestatus != null) {
			if (CallDispatcher.LoginUser != null) {
				tv_namestatus.setText(CallDispatcher.LoginUser + "\n"
						+ loadCurrentStatus());
				ib_loginlogout.setImageBitmap(BitmapFactory.decodeResource(
						act.getResources(), R.drawable.logout_slide));
			} else {
				tv_namestatus.setText("");
				ib_loginlogout.setImageBitmap(BitmapFactory.decodeResource(
						act.getResources(), R.drawable.login_slide));
			}
		}

	}

}