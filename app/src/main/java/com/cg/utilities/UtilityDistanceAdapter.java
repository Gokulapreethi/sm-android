package com.cg.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cg.snazmed.R;


public class UtilityDistanceAdapter extends BaseAdapter {
	private final ArrayList mData;
	private static LayoutInflater inflater = null;
	private Activity activity;

	public UtilityDistanceAdapter(HashMap<String, String> map, Activity a) {
		mData = new ArrayList();
		mData.addAll(map.entrySet());
		activity = a;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO implement you own logic with ID
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View result;

		if (convertView == null) {
			result = inflater.inflate(R.layout.utilitydistance, null);
		} else {
			result = convertView;
		}

		Map.Entry<String, String> item = getItem(position);

		// TODO replace findViewById by ViewHolder
		((TextView) result.findViewById(R.id.buddy_name))
				.setText(item.getKey());
		if (item.getValue().equalsIgnoreCase("nil")) {
			((TextView) result.findViewById(R.id.distance)).setText("0");
		} else {
			((TextView) result.findViewById(R.id.distance)).setText(item
					.getValue());
		}
		return result;
	}

	@Override
	public Map.Entry<String, String> getItem(int position) {
		// TODO Auto-generated method stub
		return (Map.Entry) mData.get(position);
	}
}