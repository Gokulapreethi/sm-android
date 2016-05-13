package com.cg.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.lib.model.FormsListBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;

public class FormSettings extends Activity implements OnClickListener {
	private ListView lookup_view = null;
	private Context context = null;
	private MyCustomAdapter lookup_adapter = null;
	private ArrayList<FormsListBean> ownlist = null;
	private ArrayList<FormsListBean> buddylist = null;
	private CallDispatcher callDisp = null;
	private ArrayList<FormsListBean> allitems = null;
	private HashMap<String, String> dtype = new HashMap<String, String>();
	private int selected_index;
	private String formid = null;
	boolean isreject = false;
	private Button IMRequest;
	private Button btn_notification = null;
	private ArrayList<String[]> rec_list = null;
	private Bitmap formIcon = null;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		context = this;
		lookup_view = (ListView) findViewById(R.id.form_listview);
		lookup_adapter = new MyCustomAdapter();
		new MyCustomAdapter();
		lookup_view.setAdapter(lookup_adapter);

		WebServiceReferences.contextTable.put("formsettings", context);
		new Handler();
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
		

		Button btn_cancel = (Button) findViewById(R.id.btn_cancel);

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_notification = (Button) findViewById(R.id.settingnotification);

		btn_notification.setVisibility(View.GONE);

		IMRequest = (Button) findViewById(R.id.settingim);

		IMRequest.setVisibility(View.GONE);
		IMRequest.setBackgroundResource(R.drawable.one);
		lookup_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub

				CallDispatcher.formMenu = position;

				// popupWindowMenus = popupWindowMenus(bean.getFormId());
				// popupWindowMenus.showAsDropDown(v.findViewById(R.id.usr_icn),
				// -5, 0);

				dtype.clear();
				FormsListBean bean = (FormsListBean) lookup_adapter
						.getItem(position);

				dtype = callDisp.getdbHeler(context).getColumnTypes(
						bean.getForm_name() + "_" + bean.getFormId());
				rec_list = callDisp.getdbHeler(context).getRecordsofSettingtbl(
						bean.getFormId());

				if (rec_list.size() == 0) {

					Intent viewer_intent = new Intent(FormSettings.this,
							AccessAndSync.class);
					viewer_intent.putExtra("id", bean.getFormId());
					viewer_intent.putExtra("owner", bean.getForm_owner());
					startActivity(viewer_intent);
				} else {
					Intent viewer_intent = new Intent(FormSettings.this,
							FormPermissionViewer.class);
					viewer_intent.putExtra("name", bean.getForm_name());
					viewer_intent.putExtra("id", bean.getFormId());
					viewer_intent.putExtra("types", dtype);
					viewer_intent.putExtra("owner", bean.getForm_owner());
					startActivity(viewer_intent);

				}

			}
		});

		allitems = new ArrayList<FormsListBean>();
		populateLists();

	}

	public void populateLists() {
		lookup_adapter.clearItem();

		if (ownlist != null)
			ownlist.clear();
		if (buddylist != null)
			buddylist.clear();
		if (allitems != null)
			allitems.clear();

		ownlist = callDisp.getdbHeler(context).ownLookUpRecordss(
				CallDispatcher.LoginUser);
		if (ownlist.size() != 0) {

			for (int i = 0; i < ownlist.size(); i++) {

				lookup_adapter.addItem(ownlist.get(i));
				allitems.add(ownlist.get(i));
			}
		}

	}

	@Override
	public void onClick(View v) {

	}

	private class MyCustomAdapter extends BaseAdapter {

		private static final int TYPE_ITEM = 0;
		private static final int TYPE_SEPARATOR = 1;
		private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

		private ArrayList<FormsListBean> mData = new ArrayList<FormsListBean>();
		private LayoutInflater mInflater;

		private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

		public MyCustomAdapter() {
			mInflater = (LayoutInflater) getSystemService(context.LAYOUT_INFLATER_SERVICE);
		}

		public ArrayList<FormsListBean> getallItems() {
			return this.mData;
		}

		public void addItematPosition(int position, FormsListBean bean) {
			mData.remove(position);
			mData.add(position, bean);
			notifyDataSetChanged();
		}

		public void addItem(final FormsListBean bean) {
			mData.add(bean);
			notifyDataSetChanged();
		}

		public void clearItem() {
			mData.clear();
			mSeparatorsSet.clear();
			notifyDataSetChanged();
		}

		public void addAll(ArrayList<FormsListBean> beanlist) {
			mData.clear();
			mData = beanlist;
		}

		public void addSeparatorItem(final FormsListBean item) {
			mData.add(item);
			// save separator position
			mSeparatorsSet.add(mData.size() - 1);
			notifyDataSetChanged();
		}

		@Override
		public int getItemViewType(int position) {
			return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR
					: TYPE_ITEM;
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_MAX_COUNT;
		}

		public int getCount() {
			return mData.size();
		}

		public FormsListBean getItem(int position) {
			return mData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			int type = getItemViewType(position);
			System.out.println("getView " + position + " " + convertView
					+ " type = " + type);
			if (convertView == null) {
				holder = new ViewHolder();
				switch (type) {
				case TYPE_ITEM:
					convertView = mInflater.inflate(R.layout.list_row, null);
					holder.textView = (TextView) convertView
							.findViewById(R.id.tview_lvrow);
					holder.iview = (ImageView) convertView
							.findViewById(R.id.usr_icn);
					holder.formView = (ImageView) convertView
							.findViewById(R.id.form_icn);
					holder.iview
							.setBackgroundResource(R.drawable.ic_action_overflow);
					holder.iview.setVisibility(View.INVISIBLE);

					holder.tv_describer = (TextView) convertView
							.findViewById(R.id.tview_lvrow1);
					break;
				case TYPE_SEPARATOR:
					convertView = mInflater.inflate(R.layout.section_header,
							null);
					holder.textView = (TextView) convertView
							.findViewById(R.id.tv_header);
					break;
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setTextColor(Color.parseColor("#696969"));
			holder.textView.setText(((FormsListBean) mData.get(position))
					.getForm_name());
			holder.textView.setTypeface(null, Typeface.BOLD);
			if (mData.get(position).getFormicon() != null) {
				holder.formView.setImageBitmap(callDisp.setIconImage(formIcon,
						mData.get(position).getFormicon()));
			}
			if (type == TYPE_ITEM) {
				formid = ((FormsListBean) mData.get(position)).getFormId();
				holder.tv_describer.setTextColor(Color.parseColor("#A9A9A9"));
				holder.tv_describer.setText("Owner : "
						+ ((FormsListBean) mData.get(position)).getForm_owner()
						+ "  Rules : "
						+ callDisp.getdbHeler(context).getreocrdcount(formid));

			}
			return convertView;
		}

	}

	public static class ViewHolder {
		public TextView textView;
		public TextView tv_describer;
		public ImageView iview;
		public ImageView formView;
	}

	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		WebServiceReferences.contextTable.remove("formsettings");

	}

	public void AddReordCount(String count) {

		FormsListBean bn = lookup_adapter.getItem(selected_index);
		bn.setnumberof_rows(count);
		lookup_adapter.addItematPosition(selected_index, bn);

	}

	public void notifyForceLogout(String title, String msg) {

		if (WebServiceReferences.contextTable.containsKey("frmreccreator")) {
			FormRecordsCreators rec_creator = (FormRecordsCreators) WebServiceReferences.contextTable
					.get("frmreccreator");
			rec_creator.finish();
		}
		if (WebServiceReferences.contextTable.containsKey("frmviewer")) {
			FormViewer viewer = (FormViewer) WebServiceReferences.contextTable
					.get("frmviewer");
			viewer.finish();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog alert = null;

		builder.setMessage(msg).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						finish();

					}
				});

		builder.setTitle(title);
		alert = builder.create();

		alert.show();
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
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
	}

	// public PopupWindow popupWindowMenus(String formId) {
	// PopupWindow popupWindow = new PopupWindow(this);
	//
	// // ListView listViewMenus = new ListView(this);
	//
	// // listViewMenus.setAdapter(callDisp.menusAdapter(popUpContents,
	// context));
	//
	// listViewMenus.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1,
	// int position, long arg3) {
	//
	// popupWindowMenus.dismiss();
	// if (position == 0) {
	// dtype.clear();
	// FormsListBean bean = (FormsListBean) lookup_adapter
	// .getItem(CallDispatcher.formMenu);
	//
	// dtype = callDisp.getdbHeler(context).getColumnTypes(
	// bean.getForm_name() + "_" + bean.getFormId());
	// rec_list = callDisp.getdbHeler(context)
	// .getRecordsofSettingtbl(bean.getFormId());
	//
	// if (rec_list.size() == 0) {
	//
	// Intent viewer_intent = new Intent(FormSettings.this,
	// AccessAndSync.class);
	// viewer_intent.putExtra("id", bean.getFormId());
	// viewer_intent.putExtra("owner", bean.getForm_owner());
	// startActivity(viewer_intent);
	// finish();
	// } else {
	//
	// Intent viewer_intent = new Intent(FormSettings.this,
	// FormPermissionViewer.class);
	// viewer_intent.putExtra("name", bean.getForm_name());
	// viewer_intent.putExtra("id", bean.getFormId());
	// viewer_intent.putExtra("types", dtype);
	// viewer_intent.putExtra("owner", bean.getForm_owner());
	// startActivity(viewer_intent);
	// finish();
	//
	// }
	// }
	//
	// else if (position == 1) {
	// FormsListBean bean = (FormsListBean) lookup_adapter
	// .getItem(CallDispatcher.formMenu);
	//
	// Intent viewer_intent = new Intent(FormSettings.this,
	// AccessAndSync.class);
	// viewer_intent.putExtra("id", bean.getFormId());
	// viewer_intent.putExtra("owner", bean.getForm_owner());
	// startActivity(viewer_intent);
	// finish();
	//
	// }
	//
	// }
	//
	// });
	//
	// popupWindow.setFocusable(true);
	// popupWindow.setWidth(220);
	// popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
	// popupWindow.setContentView(listViewMenus);
	//
	// return popupWindow;
	// }

}
