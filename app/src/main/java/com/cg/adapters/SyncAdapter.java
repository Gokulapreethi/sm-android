package com.cg.adapters;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.forms.SyncBean;


public class SyncAdapter extends ArrayAdapter<SyncBean> {
	private AlertDialog alert = null;

	private List<SyncBean> list;
	private LayoutInflater inflator;
	Context context;

	public SyncAdapter(Activity context, List<SyncBean> list) {
		super(context, R.layout.sync_row, list);
		this.list = list;
		inflator = context.getLayoutInflater();
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflator.inflate(R.layout.sync_row, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.myself);
			holder.permistion = (TextView) convertView
					.findViewById(R.id.permission);
			holder.query = (LinearLayout) convertView
					.findViewById(R.id.syncrowbuddies);

			holder.sql = (EditText) convertView.findViewById(R.id.sql_buddies);

			convertView.setTag(holder);
			convertView.setTag(R.id.title, holder.title);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.permistion.setText(list.get(position).getsync());
		holder.sql.setText(list.get(position).getquery());
		holder.title.setText(list.get(position).getName());

		if (holder.permistion.getText().equals("Sync Based on Rule")) {

			holder.query.setVisibility(View.VISIBLE);

		} else {
			holder.query.setVisibility(View.GONE);

		}
		holder.sql.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					final EditText Caption = (EditText) v;

					list.get(position).setQuery(Caption.getText().toString());
				}

			}
		});
		holder.permistion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSingleSelectBuddy1(position);
			}
		});
		return convertView;
	}

	void showSingleSelectBuddy1(final int position) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();

			final CharSequence[] choiceList = { "Always sync", "Never Sync",
					"Sync Based on Rule" };

			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							list.get(position).setsync(
									choiceList[which].toString());

							notifyDataSetChanged();
							alert.dismiss();
						}

					});
			alert = builder.create();
			if (choiceList != null) {
				if (choiceList.length != 0) {
					alert.show();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ViewHolder {
		protected TextView title;
		protected TextView permistion;
		protected LinearLayout query;
		protected EditText sql;

	}
}
