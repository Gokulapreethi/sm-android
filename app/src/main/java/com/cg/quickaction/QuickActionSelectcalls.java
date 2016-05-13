package com.cg.quickaction;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.main.QuickActionFragment;
import com.util.SingleInstance;

public class QuickActionSelectcalls extends Activity implements android.view.View.OnClickListener {

	Context context;
	TextView tv_title;
	Button btn_cancel, btn_next;
	public Button IMRequest;
	Button btn_notification;
	ListView lv_buddylist;
	MyArrayAdapter myArrayAdapter;
	String title, action, buddyname;
	RadioButton checkbox;
	CallDispatcher callDisp;
	private ContactLogicbean beanObj;
	int id = 0;
	String buddies;
	boolean editUser = false;
	boolean checkEdit = false;
	private Handler viewHandler = new Handler();
	public String owner;

	// ContactArrayAdapter contactArrayAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			context = this;
			beanObj = new ContactLogicbean();
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.calloptions_one);
			WebServiceReferences.contextTable.put("QuickActionSelectcalls",
					context);

			QuickActionFragment quickActionFragment = QuickActionFragment
					.newInstance(context);
			editUser = getIntent().getExtras().getBoolean("editUser");

			tv_title = (TextView) findViewById(R.id.textcalls);
			btn_next = (Button) findViewById(R.id.btn_next);
			btn_next.setOnClickListener(this);
			btn_cancel = (Button) findViewById(R.id.btn_cancel);
			btn_cancel.setOnClickListener(this);
			lv_buddylist = (ListView) findViewById(R.id.buddylist);
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
			if (!editUser) {
				title = beanObj.getLabel();
				action = beanObj.getAction();
				tv_title.setText(title);
				myArrayAdapter = new MyArrayAdapter(this, quickActionFragment.getBuddyBean());
				lv_buddylist.setAdapter(myArrayAdapter);

			} else {
				buddies = getIntent().getExtras().getString("buddies");

				action = getIntent().getExtras().getString("action");
				myArrayAdapter = new MyArrayAdapter(this,
						quickActionFragment.getBuddyBeanEdit(buddies));
				lv_buddylist.setAdapter(myArrayAdapter);

			}
			Log.i("QAA", "Title,action=====>" + title + "===" + action);
			btn_notification = (Button) findViewById(R.id.notification);
			btn_notification.setVisibility(View.GONE);
			btn_notification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});

			IMRequest = (Button) findViewById(R.id.im);

			IMRequest.setVisibility(View.INVISIBLE);
			IMRequest.setBackgroundResource(R.drawable.one);
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

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		WebServiceReferences.contextTable.remove("QuickActionSelectcalls");
	}

	public class MyArrayAdapter extends ArrayAdapter<BuddyBean> {
		Context context;
		ArrayList<BuddyBean> values;
		String editBuddy = "null";

		public MyArrayAdapter(Context context, ArrayList<BuddyBean> list_values) {
			super(context, R.layout.buddyname, list_values);
			this.context = context;
			this.values = list_values;
		}

		public void addBean(ArrayList<BuddyBean> values) {
			this.values.addAll(values);
		}

		public Object getBean(int pos) {
			return values.get(pos);
		}

		public void clear() {
			values.clear();
			notifyDataSetChanged();
		}

		public ArrayList<BuddyBean> getAllItems() {
			return values;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = convertView;
			// View rowView;
			// if(rowView==null)
			rowView = inflater.inflate(R.layout.buddyname, parent, false);

			RelativeLayout rlayout = (RelativeLayout) rowView
					.findViewById(R.id.buddy_row_item);
			final BuddyBean buddyBean = (BuddyBean) values.get(position);

			TextView userInfo_tv = (TextView) rlayout
					.findViewById(R.id.username);

			ImageView userPic = (ImageView) rlayout.findViewById(R.id.icon2);
			CheckBox checkbox = (CheckBox) rlayout.findViewById(R.id.checkbox);
			Bitmap bitmap = null;

			userInfo_tv.setText(buddyBean.getBuddyName());
			checkbox.setChecked(buddyBean.isSelect());
			if (buddyBean.getBuddyPic() != null) {
				bitmap = callDisp.ResizeImage(buddyBean.getBuddyPic());
			}
			if (bitmap != null) {
				bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);
				bitmap = CallDispatcher.getRoundedBitmap(bitmap);
				userPic.setImageBitmap(bitmap);
			} else {
				userPic.setBackgroundResource(R.drawable.icon_buddy_aoffline);
			}
			Log.d("QA", "Get view-----" + buddyBean.isSelect());

			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					Log.d("QA", "Get view-----");
					if (isChecked) {

						if (action.equalsIgnoreCase("ABC")
								|| action.equalsIgnoreCase("VBC")
								|| action.equalsIgnoreCase("HC")
								|| action.equalsIgnoreCase("ACF")
								|| action.equalsIgnoreCase("VCF")) {

							buddyBean.setSelect(isChecked);

						} else {
							for (int i = 0; i < values.size(); i++) {
								BuddyBean bean = values.get(i);

								if (!buddyBean.getBuddyName().equals(
										bean.getBuddyName())) {

									bean.setSelect(false);

								} else {

									bean.setSelect(isChecked);
								}

							}
						}
					}

					else
						buddyBean.setSelect(isChecked);

					notifyDataSetChanged();

				}
			});

			return rowView;

		}
	}

	public void onClick(View v) {

		if (v.getId() == btn_cancel.getId()) {
			finish();
		} else if (v.getId() == btn_next.getId()) {
			ArrayList<BuddyBean> buddy_list = myArrayAdapter.getAllItems();
			int count = 0;
			StringBuffer sb = new StringBuffer();
			if (!action.startsWith("Show")) {
				for (BuddyBean buddyBean : buddy_list) {

					if (buddyBean.isSelect()) {
						// buddyBean.getBuddyName();
						// buddyname = buddyBean.getBuddyName();
						// beanObj.setTouser(buddyname);
						if (sb.length() > 0)
							sb.append(",");
						sb.append(buddyBean.getBuddyName());

					}

					else if (!buddyBean.isSelect()) {
						count++;
					}
				}

				beanObj = new ContactLogicbean();
				if (editUser) {
					if (count == buddy_list.size()) {
						Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_select_the_buddy),
								Toast.LENGTH_SHORT).show();

					} else {
						Intent i = new Intent();
						Bundle bun = new Bundle();
						bun.putString("selectedUser", sb.toString());
						i.putExtra("quick", bun);
						setResult(-15, i);
						finish();
					}
				} else {
					if (count == buddy_list.size()) {
						Toast.makeText(context,SingleInstance.mainContext.getResources().getString(R.string.kindly_select_the_buddy),
								Toast.LENGTH_SHORT).show();

					} else {
						beanObj.setEditToUser(sb.toString());
						String type = "";
						String filePath = getIntent()
								.getStringExtra("filepath");
						if (filePath != null) {
							type = getIntent().getStringExtra("type");
						}
						beanObj.setFtpPath(filePath);
						beanObj.setAction(action);
						Intent intent = new Intent(context,
								QuickActionSettingcalls.class);
						intent.putExtra("type", type);
						intent.putExtra("qabean", beanObj);
						startActivity(intent);
						// finish();
					}
				}
			} else {
				for (BuddyBean buddyBean : buddy_list) {

					if (buddyBean.isSelect()) {
						// buddyBean.getBuddyName();
						// buddyname = buddyBean.getBuddyName();
						// beanObj.setTouser(buddyname);
						if (sb.length() > 0)
							
							sb.append("");
							
						sb.append(buddyBean.getBuddyName());

					}

					else if (!buddyBean.isSelect()) {
						count++;
					}
				}
				beanObj = new ContactLogicbean();
				if (editUser) {
					Intent i = new Intent();
					Bundle bun = new Bundle();
					bun.putString("selectedUser", sb.toString());
					i.putExtra("quick", bun);
					setResult(-15, i);
					finish();
				} else {
					if (sb.toString().length() > 0) {
						beanObj.setEditToUser(sb.toString());
					} else {
						beanObj.setEditToUser("");
					}
					String filePath = null;
					filePath = getIntent().getStringExtra("filepath");
					String type = "";
					Log.i("QAA", "FilePath Length==>" + filePath);
					if (filePath != null) {
						type = getIntent().getStringExtra("type");
					}
					beanObj.setFtpPath(filePath);
					beanObj.setAction(action);
					// if (action.equalsIgnoreCase("HC")) {
					// if (sb.length() > 0) {
					// String buddies[] = sb.toString().split(",");
					// for (int i = 0; i < buddies.length; i++) {
					// CallDispatcher.buddiesConferenceList.add(buddies[i]);
					// }
					// contactArrayAdapter=new ContactArrayAdapter(context,
					// callDisp.getOnLineBuddiesP());
					// CallDispatcher.call_list.add(object)
					//
					// }
					// }
					Intent intent = new Intent(context,
							QuickActionSettingcalls.class);
					intent.putExtra("type", type);
					intent.putExtra("qabean", beanObj);
					startActivity(intent);
					// finish();
				}
			}

		}

	}
}
