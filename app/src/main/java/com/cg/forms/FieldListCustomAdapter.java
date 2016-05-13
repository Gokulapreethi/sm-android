package com.cg.forms;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.util.SingleInstance;

public class FieldListCustomAdapter extends BaseAdapter {

	private ArrayList data;
	private static LayoutInflater inflater = null;
	public Resources res;
	private Context context = null;
	private InputsFields tempValues = null;
	private int i = 0;

	public FieldListCustomAdapter(Context a, ArrayList<?> d, Resources resLocal) {

		context = a;
		data = d;
		res = resLocal;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {

		public TextView fieldName;
		public TextView fieldType;
		public ImageView image;
		public ImageView deleteImg;
		public RelativeLayout container;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		try {
			// TODO Auto-generated method stub

			ViewHolder holder;

			if (convertView == null) {

				vi = inflater.inflate(R.layout.formfieldlist, null);

				holder = new ViewHolder();
				holder.fieldName = (TextView) vi.findViewById(R.id.fieldName);
				holder.fieldName = (TextView) vi.findViewById(R.id.fieldName);
				holder.fieldType = (TextView) vi.findViewById(R.id.fieldType);
				holder.image = (ImageView) vi.findViewById(R.id.image);
				holder.deleteImg = (ImageView) vi.findViewById(R.id.deletebtn);
				holder.container = (RelativeLayout) vi
						.findViewById(R.id.container);
				super.notifyDataSetChanged();
				vi.setTag(holder);
			} else
				holder = (ViewHolder) vi.getTag();

			if (data.size() <= 0) {
				holder.fieldName.setText("");
				holder.fieldType.setText("");
				holder.image.setImageBitmap(null);
				holder.deleteImg.setImageBitmap(null);
			} else {
				tempValues = null;
				tempValues = (InputsFields) data.get(position);
				if (tempValues.getAttributeId() != null
						&& tempValues.getAttributeId().length() > 0) {
					holder.container.setBackgroundColor(R.color.light_grey);
				} else {
					holder.container.setBackgroundColor(R.color.lgyellow);
				}
				holder.fieldName.setText(tempValues.getFieldName());
				holder.fieldType.setText(tempValues.getFieldType());
				holder.image.setImageResource(R.drawable.tblsidemenu);
				holder.deleteImg.setImageResource(R.drawable.tbldelicon);
				final int mPosition = position;
				Log.i("Item::", String.valueOf(mPosition));
				if (mPosition >= 0) {
					holder.deleteImg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							AddNewForm add = (AddNewForm) context;
							add.deleteField(mPosition);
						}
					});
				} else {
					holder.deleteImg.setClickable(false);
					Toast.makeText(
							context,
							SingleInstance.mainContext.getResources()
									.getString(R.string.no_data_delete),
							Toast.LENGTH_LONG).show();
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vi;
	}

}
