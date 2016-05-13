package com.cg.settings;

import java.util.ArrayList;
import java.util.HashMap;

import org.lib.model.SignalingBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.instancemessage.ActionItem1;
import com.cg.instancemessage.IMNotifier;
import com.cg.instancemessage.QuickAction1;
import com.main.AppMainActivity;
import com.main.SettingsFragment;
import com.util.SingleInstance;

public class ShareSettings extends Activity implements IMNotifier {

	private CallDispatcher callDisp = null;
	private SharedPreferences sPreferences = null;
	private Button IMRequest = null;

	private Context context = null;

	private Button btn_cancel1 = null;

	private Button btn_cmp = null;

	private Button btn_notification = null;

	private Button btn_cancel = null;

	private Button btn_save = null;

	private Button btn_preview = null;

	private Spinner toneSpinner = null;

	private ToggleButton btn_tgl = null;

	private MediaPlayer iplayer = null;

	private LinearLayout llay_tone = null;

	private UserSettingsBean sett_bean = null;

	private TextView titles = null;

	private Handler handler = new Handler();

	private AlertDialog confirmation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		WebServiceReferences.contextTable.put("a_play", this);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title1);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		callDisp = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldisp");
		context = this;
		titles = (TextView) findViewById(R.id.note_date);
		titles.setText(SingleInstance.mainContext
				.getString(R.string.share_tone));
		IMRequest = (Button) findViewById(R.id.im);

		IMRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callDisp.openReceivedIm(v, context);
			}

		});

		btn_cancel1 = (Button) findViewById(R.id.settings);
		btn_cancel1.setBackgroundResource(R.drawable.ic_action_back);
		btn_cancel1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_cmp = (Button) findViewById(R.id.btncomp);
		btn_cmp.setVisibility(View.INVISIBLE);

		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);
		IMRequest.setWidth(70);

		WebServiceReferences.contextTable.put("IM", this);
		WebServiceReferences.contextTable.put("sharesettings", this);
		btn_notification = (Button) findViewById(R.id.notification);
		btn_notification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		setContentView(R.layout.sharetone_settings);

		btn_cancel = (Button) findViewById(R.id.btnCancelToneSettings);
		btn_save = (Button) findViewById(R.id.btnSaveToneSettings);
		btn_preview = (Button) findViewById(R.id.btnplaytone);

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		llay_tone = (LinearLayout) findViewById(R.id.tonelay);
		btn_tgl = (ToggleButton) findViewById(R.id.toggletoneManual);
		btn_tgl.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (btn_tgl.isChecked()) {
					llay_tone.setVisibility(View.VISIBLE);
				} else {
					llay_tone.setVisibility(View.GONE);
				}
			}
		});

		sett_bean = callDisp.getSettings();
		toneSpinner = (Spinner) findViewById(R.id.tone_Spinner);
		String[] tones = { "Sharetone1.mp3", "Sharetone2.mp3", "Sharetone3.mp3" };
		ArrayAdapter<String> adself = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, tones);
		adself.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		toneSpinner.setAdapter(adself);

		String tonename = null;
		Log.i("thread",
				"!!!!!!!!!!!!!!!!!!!!!!! tone  " + callDisp.getToneEnabled());
		if (callDisp.getToneEnabled()) {
			if (sett_bean != null) {
				if (sett_bean.getShareToneSevice() != null) {
					if (sett_bean.getShareToneSevice().equals("1")) {

						tonename = sett_bean.getShareToveValue1();
					}
				}
			}
			Log.i("thread", "!!!!!!!!!!!!!!!!!!!!!!! tone  " + tonename);
			if (tonename != null) {
				if (tonename.equalsIgnoreCase(tones[0])) {
					toneSpinner.setSelection(0);
				} else if (tonename.equalsIgnoreCase(tones[1])) {
					toneSpinner.setSelection(1);
				} else if (tonename.equalsIgnoreCase(tones[2])) {
					toneSpinner.setSelection(2);
				}
				btn_tgl.setChecked(true);
			} else {
				btn_tgl.setChecked(false);
				llay_tone.setVisibility(View.GONE);
			}

		} else {
			toneSpinner.setSelection(0);
			llay_tone.setVisibility(View.GONE);
			btn_tgl.setChecked(false);
		}
		Log.i("thread", "############################ spinner selected item"
				+ toneSpinner.getSelectedItem().toString());

		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String tonename = null;
				if (callDisp.getToneEnabled())
					tonename = sett_bean.getShareToveValue1();
				if (btn_tgl.isChecked()) {
					if (tonename == null) {
						CallDispatcher.istoneEnabled = true;
						SettingsFragment.tonename = toneSpinner
								.getSelectedItem().toString();
						if (callDisp.getdbHeler(context).isRecordExists(
								"select * from Settings where username='"
										+ CallDispatcher.LoginUser
										+ "' and settings='sharetone'")) {
							String updatequerry = "update Settings set service='1',value1='"
									+ toneSpinner.getSelectedItem()
									+ "' where settings='sharetone' and username='"
									+ CallDispatcher.LoginUser + "'";
							boolean res = callDisp.getdbHeler(context)
									.ExecuteQuery(updatequerry);
							Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result"
									+ res);

						} else {
							String insertQuery = "insert into Settings(settings,service,value1,username)values('sharetone','1','"
									+ toneSpinner.getSelectedItem()
									+ "','"
									+ CallDispatcher.LoginUser + "')";
							boolean res = callDisp.getdbHeler(context)
									.ExecuteQuery(insertQuery);
							Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result"
									+ res);
						}
						if (sett_bean != null) {
							sett_bean.setShareToneValue1(toneSpinner
									.getSelectedItem().toString());
							sett_bean.setSharetoneService("1");
						} else {
							sett_bean = callDisp.getdbHeler(context)
									.getUserSettings();
							sett_bean.setShareToneValue1(toneSpinner
									.getSelectedItem().toString());
							sett_bean.setSharetoneService("1");
							callDisp.setSettings(sett_bean);
						}
						showToast("Settings saved");
						callDisp.setToneEnabled(true);
						finish();
					} else {
						if (tonename.equalsIgnoreCase(toneSpinner
								.getSelectedItem().toString())) {
							SettingsFragment.tonename = toneSpinner
									.getSelectedItem().toString();
							callDisp.setToneEnabled(true);
							finish();
						} else {
							SettingsFragment.tonename = toneSpinner
									.getSelectedItem().toString();
							if (callDisp.getdbHeler(context).isRecordExists(
									"select * from Settings where username='"
											+ CallDispatcher.LoginUser
											+ "' and settings='sharetone'")) {
								String updatequerry = "update Settings set service='1',value1='"
										+ toneSpinner.getSelectedItem()
										+ "' where settings='sharetone' and username='"
										+ CallDispatcher.LoginUser + "'";
								boolean res = callDisp.getdbHeler(context)
										.ExecuteQuery(updatequerry);
								Log.i("thread",
										"@@@@@@@@@@@@@@@@@@@ querry result"
												+ res);

							} else {
								SettingsFragment.tonename = toneSpinner
										.getSelectedItem().toString();
								String insertQuery = "insert into Settings(settings,service,value1,username)values('sharetone','1','"
										+ toneSpinner.getSelectedItem()
										+ "','"
										+ CallDispatcher.LoginUser + "')";
								boolean res = callDisp.getdbHeler(context)
										.ExecuteQuery(insertQuery);
								Log.i("thread",
										"@@@@@@@@@@@@@@@@@@@ querry result"
												+ res);
							}
							if (sett_bean != null) {
								sett_bean.setShareToneValue1(toneSpinner
										.getSelectedItem().toString());
								sett_bean.setSharetoneService("1");
							} else {
								sett_bean = callDisp.getdbHeler(context)
										.getUserSettings();
								sett_bean.setShareToneValue1(toneSpinner
										.getSelectedItem().toString());
								sett_bean.setSharetoneService("1");
								callDisp.setSettings(sett_bean);
							}
							showToast(SingleInstance.mainContext.getResources().getString(R.string.settings_saved));
							callDisp.setToneEnabled(true);
							finish();
						}
					}
				} else {
					if (callDisp.getdbHeler(context).isRecordExists(
							"select * from Settings where username='"
									+ CallDispatcher.LoginUser
									+ "' and settings='sharetone'")) {
						String updatequerry = "delete from Settings where username='"
								+ CallDispatcher.LoginUser
								+ "' and settings='sharetone'";
						boolean res = callDisp.getdbHeler(context)
								.ExecuteQuery(updatequerry);
						Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result"
								+ res);

					}
					if (sett_bean != null) {
						sett_bean.setShareToneValue1("");
						sett_bean.setSharetoneService("");
					} else {
						sett_bean = callDisp.getdbHeler(context)
								.getUserSettings();
						sett_bean.setShareToneValue1("");
						sett_bean.setSharetoneService("");
						callDisp.setSettings(sett_bean);
					}
					callDisp.setToneEnabled(false);
					showToast(SingleInstance.mainContext.getResources().getString(R.string.settings_saved));
					finish();

				}
			}
		});

		btn_preview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btn_tgl.isChecked()) {
					playShareTone(toneSpinner.getSelectedItem().toString());
				} else {
					showToast(SingleInstance.mainContext.getResources().getString(R.string.enable_share_tone_));
				}
			}
		});

	}

	public void playShareTone(String flname) {
		try {

			Log.d("RING", "on IM Tone()");
			if ((iplayer == null) && flname != null) {
				Log.d("RING", "Started Player caller Ring Tone");
				iplayer = new MediaPlayer();
				AssetFileDescriptor descriptor;
				descriptor = context.getAssets().openFd(flname);
				iplayer.setDataSource(descriptor.getFileDescriptor(),
						descriptor.getStartOffset(), descriptor.getLength());
				descriptor.close();
				iplayer.setLooping(false);
				iplayer.prepare();
				iplayer.start();
				iplayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						try {
							iplayer.release();
							iplayer = null;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("RING", "on startCallRingTone()" + e.toString());
		}

	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public void ShowImMembers(final ArrayList al, View v, final int[] ix,
			final HashMap<String, String> hs) {

		final QuickAction1 quickAction = new QuickAction1(this);

		for (int i = 0; i < al.size(); i++) {
			ActionItem1 dashboard = new ActionItem1();
			// dashboard.setTitle("My name is Test");
			String size = Integer.toString(ix[i]);
			dashboard.setTitle((String) al.get(i) + "   " + size);
			// dashboard.
			dashboard.setIcon(getResources().getDrawable(R.drawable.kontak));
			quickAction.addActionItem(dashboard);

		}
		quickAction
				.setOnActionItemClickListener(new QuickAction1.OnActionItemClickListener() {
					@Override
					public void onItemClick(int pos) {

						try {
							Log.e("IM", "Postition :" + pos);
							Log.e("IM", "NAME :" + al.get(pos));

							Log.e("IM", "Session :" + hs.get(al.get(pos)));
							String name = (String) al.get(pos);
							String session = (String) hs.get(al.get(pos));

							showActiveSession(session, name);

							al.remove(pos);
							if (al.size() <= 0) {
								IMRequest.setVisibility(View.INVISIBLE);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						// ShowIm();
						// IMRequest.setVisibility(View.INVISIBLE);
					}
				});

		quickAction.show(v);
	}

	public void showActiveSession(String session, String selectedBuddy) {
		Log.d("session",
				"reloadCurrentSession................................."
						+ session);
		Log.d("session",
				"reloadCurrentSession................................."
						+ selectedBuddy);

		// if (WebServiceReferences.instantMessageScreen.containsKey(session)) {
		// InstantMessageScreen imscreen = (InstantMessageScreen)
		// WebServiceReferences.instantMessageScreen
		// .get(session);
		// imscreen.finish();
		// }
		SignalingBean bean = new SignalingBean();
		bean.setSessionid(session);
		bean.setFrom(CallDispatcher.LoginUser);
		bean.setTo(selectedBuddy);
		bean.setConferencemember("");
		bean.setMessage("");
		bean.setCallType("MSG");
		// Intent intent = new Intent(context, IMTabScreen.class);
		// intent.putExtra("sb", bean);
		// intent.putExtra("fromto", true);
		// startActivity(intent);

	}

	@Override
	protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;

		Log.d("lg", "########### Show IM Called"
				+ WebServiceReferences.Imcollection.size());

		if (WebServiceReferences.Imcollection.size() <= 0) {
			if (IMRequest != null) {
				IMRequest.setVisibility(View.INVISIBLE);
			}
		} else {
			if (IMRequest != null) {
				IMRequest.setVisibility(View.VISIBLE);
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		if (WebServiceReferences.contextTable.containsKey("a_play")) {
			WebServiceReferences.contextTable.remove("a_play");
		}
		if (WebServiceReferences.contextTable.containsKey("IM")) {
			WebServiceReferences.contextTable.remove("IM");
		}
		if (WebServiceReferences.contextTable.containsKey("sharesettings")) {
			WebServiceReferences.contextTable.remove("sharesettings");
		}
		super.onDestroy();
	}

	public void ShowError(String Title, String Message) {

		Log.e("logout", "show error() " + Title + "....." + Message);
		try {
			confirmation = new AlertDialog.Builder(this).create();
			confirmation.setTitle(Title);
			confirmation.setMessage(Message);
			confirmation.setCancelable(true);
			confirmation.setButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});

			confirmation.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();

			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void notifyReceivedIM(final SignalingBean sb) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				IMRequest.setVisibility(View.VISIBLE);
				IMRequest.setEnabled(true);

				IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);

				if (!callDisp.getdbHeler(context).userChatting(sb.getFrom())) {
					callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
							CallDispatcher.LoginUser, 1,
							CallDispatcher.LoginUser);
				}

			}
		});

	}
}
