package com.cg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.forms.GridItems;
import com.util.SingleInstance;


public class GridAdapter extends BaseAdapter {

	Context context;


	public class ViewHolder {
		public ImageView imageView;
		public TextView textTitle;
	}

	private GridItems[] items;
	private LayoutInflater mInflater;

	public GridAdapter(Context context, GridItems[] locations) {

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		items = locations;

	}

	public GridItems[] getItems() {
		return items;
	}

	public void setItems(GridItems[] items) {
		this.items = items;
	}

	@Override
	public int getCount() {
		if (items != null) {
			return items.length;
		}
		return 0;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		if (items != null && position >= 0 && position < getCount()) {
			return items[position];
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (items != null && position >= 0 && position < getCount()) {
			return items[position].id;
		}
		return 0;
	}

	public void setItemsList(GridItems[] locations) {
		this.items = locations;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder viewHolder;

		if (view == null) {

			view = mInflater.inflate(R.layout.custom, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) view
					.findViewById(R.id.grid_item_image);
			viewHolder.textTitle = (TextView) view
					.findViewById(R.id.grid_item_label);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		GridItems gridItems = items[position];
		setCatImage(position, viewHolder, gridItems.title);
		return view;
	}

	private void setCatImage(int pos, ViewHolder viewHolder, String catTitle) {
		if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.free_text))) {
			viewHolder.imageView.setImageResource(R.drawable.textmode);
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.multimedia))) {
			viewHolder.imageView.setImageResource(R.drawable.multimediamode);
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.date))) {
			viewHolder.imageView.setImageResource(R.drawable.datemode);
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.time))) {
			viewHolder.imageView.setImageResource(R.drawable.timemode);
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.date_and_time))) {
			viewHolder.imageView.setImageResource(R.drawable.datetimemode);
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.current_date))) {
			viewHolder.imageView.setImageResource(R.drawable.currentdatemode);
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.current_time))) {
			viewHolder.imageView.setImageResource(R.drawable.currenttimemode);
		
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.drop_down))) {
			viewHolder.imageView.setImageResource(R.drawable.dropdownmode);
			
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.radio_button))) {
			viewHolder.imageView.setImageResource(R.drawable.radiomode);
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.numeric))) {
			viewHolder.imageView.setImageResource(R.drawable.numbermode);
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.current_loc))) {
			viewHolder.imageView.setImageResource(R.drawable.locationmode);
		} else if (catTitle.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.compute))) {
			viewHolder.imageView.setImageResource(R.drawable.computedmode);
		}

		viewHolder.textTitle.setText(catTitle);

	}
}