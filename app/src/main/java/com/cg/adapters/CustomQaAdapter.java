package com.cg.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.forms.formItem;

public class CustomQaAdapter extends ArrayAdapter<formItem> {
	Context context;
	int layoutResourceId;
	ArrayList<formItem> data = new ArrayList<formItem>();

	public CustomQaAdapter(Context context, int layoutResourceId,
			ArrayList<formItem> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RecordHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.appnameTXT);
			holder.action = (TextView) row.findViewById(R.id.actionTXT);
			holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}

		formItem item = data.get(position);
		holder.txtTitle.setText(item.getTitle());
		holder.action.setText(setAction(item.getAction()));
		holder.imageItem.setImageBitmap(setBitMap(item.getAction()));
		holder.imageItem.setContentDescription(item.getId());

		return row;

	}

	static class RecordHolder {
		TextView txtTitle;
		TextView action;

		ImageView imageItem;

	}

	Bitmap setBitMap(String action) {

		Bitmap Action = null;

		Log.i("name", "conditioninside");
		Log.i("apps123", "Action : " + action);

		if (action.equalsIgnoreCase("sp")) {

			Log.i("name", "photo");
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridshare);

		}

		else if (action.equalsIgnoreCase("st")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridshare);
		} else if (action.equalsIgnoreCase("Show Results Form")) {

			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridfmicon);
		} else if (action.equalsIgnoreCase("Dynamic audio Call")) {

			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridcall);

		} else if (action.equalsIgnoreCase("Dynamic Video Call")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridcall);

		} else if (action.equalsIgnoreCase("sa")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridshare);
		} else if (action.equalsIgnoreCase("sv")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridshare);

		} else if (action.equalsIgnoreCase("SHS")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridshare);
		} else if (action.equalsIgnoreCase("AC")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridcall);
		} else if (action.equalsIgnoreCase("VC")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridcall);

		} else if (action.equalsIgnoreCase("AbC")) {

			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridbcast);

		} else if (action.equalsIgnoreCase("VBC")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridbcast);

		} else if (action.equalsIgnoreCase("ACF")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridcall);

		} else if (action.equalsIgnoreCase("VCF")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridcall);

		} else if (action.equalsIgnoreCase("HC")) {
			Action = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.gridcall);

		}

		return Action;

	}

	String setAction(String action) {

		String Action = null;

		Log.i("name", "conditioninside");

		if (action.equalsIgnoreCase("sp")) {

			Log.i("name", "photo");

			Action = "Photo";

		}

		else if (action.equalsIgnoreCase("st")) {
			Action = "Text Note";
		} else if (action.equalsIgnoreCase("Show Results Form")) {

			Action = "Show Results Form";
		} else if (action.equalsIgnoreCase("Dynamic audio Call")) {

			Action = "Dynamic audio Call";

		} else if (action.equalsIgnoreCase("Dynamic Video Call")) {
			Action = "Dynamic Video Call";

		} else if (action.equalsIgnoreCase("sa")) {
			Action = "Audio Note";
		} else if (action.equalsIgnoreCase("sv")) {
			Action = "Video Note";

		} else if (action.equalsIgnoreCase("SHS")) {
			Action = "Hand Sketch";
		} else if (action.equalsIgnoreCase("AC")) {
			Action = "Audio Call";
		} else if (action.equalsIgnoreCase("VC")) {
			Action = "Video Call";

		} else if (action.equalsIgnoreCase("AbC")) {

			Action = "Audio Broadcast Call";

		} else if (action.equalsIgnoreCase("VBC")) {
			Action = "Video Broadcast Call";

		} else if (action.equalsIgnoreCase("ACF")) {
			Action = "Video Conference Call";

		} else if (action.equalsIgnoreCase("VCF")) {
			Action = "Video Conference Call";

		}

		return Action;

	}

	public String getId(int index) {
		formItem data = this.data.get(index);
		return data.getId();
	}
}
