package com.cg.forms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.adapters.GridAdapter;
import com.cg.commonclass.CallDispatcher;
import com.util.SingleInstance;

@SuppressLint("ValidFragment")
public class GridFragment extends Fragment {

	private GridView mGridView;
	private GridAdapter mGridAdapter;
	private GridItems[] gridItems = {};
	private Activity activity;
	private boolean isValid = false;

	public GridFragment(GridItems[] gridItems, Activity activity,
			boolean isValid) {
		this.gridItems = gridItems;
		this.activity = activity;
		this.isValid = isValid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.grid_layout, container, false);
		mGridView = (GridView) view.findViewById(R.id.gridView);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (activity != null) {

			mGridAdapter = new GridAdapter(activity, gridItems);
			if (mGridView != null) {
				mGridView.setAdapter(mGridAdapter);
			}

			mGridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView parent, View view,
						int position, long id) {
					onGridItemClick((GridView) parent, view, position, id);
				}
			});
		}
	}

	public void onGridItemClick(GridView g, View v, int position, long id) {
		Log.e("TAG", "POSITION CLICKED " + position + "Name :: "
				+ gridItems[position].title);
		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.free_text))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry",SingleInstance.mainContext.getResources().getString(R.string.free_text ));
			startActivity(intent);
		}

		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.multimedia))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry", SingleInstance.mainContext.getResources().getString(R.string.multimedia));
			startActivity(intent);

		}
		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.date))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry", SingleInstance.mainContext.getResources().getString(R.string.date));
			startActivity(intent);

		}
		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.time))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry", SingleInstance.mainContext.getResources().getString(R.string.time));
			startActivity(intent);

		}
		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.date_and_time))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry", SingleInstance.mainContext.getResources().getString(R.string.date_and_time));
			startActivity(intent);

		}
		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.current_time))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry", SingleInstance.mainContext.getResources().getString(R.string.current_time));
			startActivity(intent);

		}
		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.current_date))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry", SingleInstance.mainContext.getResources().getString(R.string.current_date));
			startActivity(intent);

		}

		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.drop_down))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry", SingleInstance.mainContext.getResources().getString(R.string.drop_down));
			startActivity(intent);
		}
		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.current_loc))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry", SingleInstance.mainContext.getResources().getString(R.string.current_loc));
			startActivity(intent);
		}

		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.radio_button))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry", "radio button");
			startActivity(intent);

		}
		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.numeric))) {
			Intent intent = new Intent(getActivity(), TextInputActivity.class);
			intent.putExtra("entry", "Numeric");
			startActivity(intent);
		}

		if (gridItems[position].title
				.equalsIgnoreCase(SingleInstance.mainContext.getResources()
						.getString(R.string.compute))) {
			if (CallDispatcher.inputFieldList.size() > 1) {
				Intent intent = new Intent(getActivity(),
						TextInputActivity.class);
				intent.putExtra("entry", "Compute");
				startActivity(intent);
			} else
				Toast.makeText(
						getActivity(),
						SingleInstance.mainContext.getResources().getString(
								R.string.add_two_fields), 1).show();

		}
	}

}