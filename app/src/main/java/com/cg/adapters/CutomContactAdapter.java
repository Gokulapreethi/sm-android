package com.cg.adapters;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.forms.accessSyncBean;

public class CutomContactAdapter extends ArrayAdapter<accessSyncBean> {

	private List<accessSyncBean> list;
	private LayoutInflater inflator;
	private EditText query = null;
	boolean isreject = false;
	boolean issaveandsync = false;
	private boolean issyncbasedRule = false;
	private LinearLayout layout_query = null;
	private Button update, cancel = null;
	private Spinner accessspinner, syncspinner = null;
	private int selecteposition, syncselected;
	private String updatedaccess, updatedsync;

	public CutomContactAdapter(Activity context, List<accessSyncBean> list) {
		super(context, R.layout.accesssyncrow, list);
		this.list = list;
		inflator = context.getLayoutInflater();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflator.inflate(R.layout.accesssyncrow, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.myself);
			holder.permistion = (TextView) convertView
					.findViewById(R.id.permission);
			holder.setAccess = (TextView) convertView
					.findViewById(R.id.tv_access);
			holder.permistion.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					showdialog(Integer.parseInt(v.getTag().toString()));
				}
			});
			holder.setSync = (TextView) convertView.findViewById(R.id.tv_sync);

			holder.setQuery = (TextView) convertView.findViewById(R.id.tv_quey);

			holder.chk = (CheckBox) convertView
					.findViewById(R.id.select_checkBox);
			holder.chk
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton view,
								boolean isChecked) {

							int getPosition = (Integer) view.getTag();
							list.get(getPosition).setSelected(view.isChecked());
							notifyDataSetChanged();
						}
					});
			convertView.setTag(holder);
			convertView.setTag(R.id.title, holder.title);
			convertView.setTag(R.id.checkbox, holder.chk);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.chk.setTag(position);
		holder.permistion.setTag(position);
		holder.setAccess.setText(list.get(position).getaccess());
		holder.setSync.setText(list.get(position).getsync());

		if (list.get(position).isRulebased()) {
			holder.setQuery.setText(list.get(position).getQuery());
			holder.setQuery.setVisibility(View.VISIBLE);
		} else {
			holder.setQuery.setVisibility(View.GONE);

		}

		holder.title.setText(list.get(position).getName());
		holder.chk.setChecked(list.get(position).isSelected());

		return convertView;
	}

	private void showdialog(final int pos) {
		final String[] choiceList = { "View", "No Access", "Add", "Complete",
				"Read & Add Own" };
		final String[] synclist = { "Always sync", "Never Sync",
				"Sync Based on Rule" };

		final Dialog dialog = new Dialog(getContext());
		dialog.setContentView(R.layout.editdialog);
		dialog.setTitle("Access and sync ");
		layout_query = (LinearLayout) dialog.findViewById(R.id.layout_query);
		accessspinner = (Spinner) dialog.findViewById(R.id.access_spinner);
		syncspinner = (Spinner) dialog.findViewById(R.id.sync_spinner);
		query = (EditText) dialog.findViewById(R.id.query);

		ArrayAdapter<String> aa = new ArrayAdapter<String>(getContext(),
				R.layout.spinnerxml, choiceList);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accessspinner.setAdapter(aa);
		accessspinner.setSelection(selecteposition);

		accessspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				updatedaccess = choiceList[position].toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		ArrayAdapter<String> sync = new ArrayAdapter<String>(getContext(),
				R.layout.spinnerxml, synclist);
		sync.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		syncspinner.setAdapter(sync);
		syncspinner.setSelection(syncselected);

		syncspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				updatedsync = synclist[position].toString();

				if (position == 2) {

					layout_query.setVisibility(View.VISIBLE);
					issyncbasedRule = true;

				}

				else {

					layout_query.setVisibility(View.GONE);
					issyncbasedRule = false;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		update = (Button) dialog.findViewById(R.id.update);
		update.setText("Submit");
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				list.get(pos).setAccess(updatedaccess);
				list.get(pos).setsync(updatedsync);
				list.get(pos).setQuery(query.getText().toString());
				list.get(pos).setRulebased(issyncbasedRule);
				notifyDataSetChanged();
				dialog.dismiss();

			}

		});
		cancel = (Button) dialog.findViewById(R.id.cancel_update);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

			}

		});

		dialog.show();
	}

	static class ViewHolder {
		protected TextView title;
		protected TextView permistion;
		protected TextView setAccess;

		protected TextView setSync;

		protected TextView setQuery;

		protected CheckBox chk;
	}
}