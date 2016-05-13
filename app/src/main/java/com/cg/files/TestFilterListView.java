package com.cg.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.main.AppMainActivity;

/**
 * This screen is used to list the available time zone and it's City names. When
 * user select any time zone send the selected time zone details to the previous
 * screen which is called this activity
 * 
 * 
 * 
 */
public class TestFilterListView extends Activity {
	FrameLayout historyContainer;
	ViewStub viewStub;
	List<TimeZoneListBean> historyList = new ArrayList<TimeZoneListBean>();

	TimeZoneListBean sendObj;

	private static final String TIMEZONE_ID_PREFIXES = "^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*";

	/**
	 * When activity is stared load all the available Time zone in to the
	 * ListView manner.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_container);
		historyContainer = (FrameLayout) findViewById(R.id.searchContainerLayout);
		EditText filterEditText = (EditText) findViewById(R.id.search_filter_text);
		filterEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				historyContainer.removeAllViews();
				final List<TimeZoneListBean> tempHistoryList = new ArrayList<TimeZoneListBean>();
				tempHistoryList.addAll(historyList);
				int idx;
				// for(String data : historyList.get(idx).getText()) {
				// idx++;
				// if(data.indexOf((s.toString())) == -1) {
				// tempHistoryList.remove(data);
				// }
				// }

				for (idx = 0; idx < historyList.size(); idx++) {
					TimeZoneListBean obj = historyList.get(idx);
					String data = obj.getId();
					String data1 = obj.getName();

					String strCheckTemp = data + " " + data1;
					// if (data.indexOf((s.toString())) == -1) {
					// tempHistoryList.remove(obj);
					// }

					if (!strCheckTemp.matches("(?i).*" + s.toString() + ".*")) {
						tempHistoryList.remove(obj);
					}

				}

				viewStub = new ViewStub(TestFilterListView.this,
						R.layout.search_schedule);
				viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
					public void onInflate(ViewStub stub, View inflated) {

						setUIElements(inflated, tempHistoryList);
					}
				});
				historyContainer.addView(viewStub);
				viewStub.inflate();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		setViewStub();
	}
@Override
 protected void onResume() {
  super.onResume();
  AppMainActivity.inActivity = this;
 }

	/**
	 * Prepare the list of TimeZoneListBean and order that in a alphabetical
	 * manner
	 */
	private void prePareList() {
		String[] zoneIds = TimeZone.getAvailableIDs();

		for (int i = 0; i < zoneIds.length; i++) {
			TimeZone tz = TimeZone.getTimeZone(zoneIds[i]);
			String temp = tz.getDisplayName(false, TimeZone.SHORT);

			if (tz.getID().matches(TIMEZONE_ID_PREFIXES)) {

				int rawOffset = tz.getRawOffset();

				TimeZoneListBean tzlb = new TimeZoneListBean();
				tzlb.setId(tz.getID());
				tzlb.setName(temp);
				tzlb.setOffSetTime(rawOffset);

				historyList.add(tzlb);

			}
		}

		Collections.sort(historyList, new Comparator<TimeZoneListBean>() {

			public int compare(TimeZoneListBean tz1, TimeZoneListBean tz2) {

				if (tz1.getOffsetTime() > tz2.getOffsetTime()) {

					return 1;
				} else if (tz1.getOffsetTime() < tz2.getOffsetTime()) {

					return -1;
				} else {

					return 0;
				}

			}

		});

		// alTZLB;
	}

	/**
	 * Prepare the TimeZoneListBean and initialize the ListView Row
	 */
	private void setViewStub() {
		prePareList();
		viewStub = new ViewStub(TestFilterListView.this,
				R.layout.search_schedule);
		viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
			public void onInflate(ViewStub stub, View inflated) {

				setUIElements(inflated, historyList);
			}
		});
		historyContainer.addView(viewStub);
		viewStub.inflate();
	}

	/********************************************************************************************************/

	String displayName = "";
	ListView historyListView;

	/**
	 * Design the every row of the ListView
	 * 
	 * @param v
	 * @param historyLists
	 */
	private void setUIElements(View v, final List<TimeZoneListBean> historyLists) {

		if (v != null) {
			historyScheduleData.clear();
			// historyList.clear();

			historyScheduleData.addAll(historyLists);
			historyListView = (ListView) findViewById(R.id.search_list);
			historyListView.setAdapter(new BeatListAdapter(this));
			historyListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					sendObj = historyLists.get(arg2);
					Intent intent = new Intent();
					intent.putExtra("tz", sendObj);
					setResult(Activity.RESULT_OK, intent);
					finish();

					Toast.makeText(getApplicationContext(), sendObj.getId(),
							Toast.LENGTH_SHORT).show();

				}
			});

			registerForContextMenu(historyListView);

		}
	}

	/**
	 * Creation of ListView adapter
	 * 
	 * @author
	 * 
	 */
	private static class BeatListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public BeatListAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return historyScheduleData.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.search_list_view, null);
				holder = new ViewHolder();
				holder.historyData = (TextView) convertView
						.findViewById(R.id.search_text);

				holder.ivType = (ImageView) convertView
						.findViewById(R.drawable.tz_icon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.historyData.setText(historyScheduleData.get(position)
					.getId()
					+ " (" + historyScheduleData.get(position).getName() + ")");

			TimeZoneListBean obj = historyScheduleData.get(position);

			return convertView;
		}

		static class ViewHolder {

			TextView historyData;

			ImageView ivType;
		}
	}

	private static final List<TimeZoneListBean> historyScheduleData = new ArrayList<TimeZoneListBean>();

}
