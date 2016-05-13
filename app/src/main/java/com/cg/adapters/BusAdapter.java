package com.cg.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.quickaction.ContactLogicbean;
import com.cg.quickaction.QuickActionTitlecalls;

public class BusAdapter extends ArrayAdapter {
	private static final String tag = "CountryArrayAdapter";

	private TextView userName, action;
	ImageView img;
	// ImageView navigator;
	Button edit;
	Button run;
	// private TextView userStatus;
	// ImageView img;
	private List<ContactLogicbean> countries = new ArrayList<ContactLogicbean>();
	private CallDispatcher callDisp = null;

	/**
	 * Constructor method to create the instance to the adapter to populate the
	 * list of buddies
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public BusAdapter(Context context, int textViewResourceId, List objects) {
		super(context, textViewResourceId, objects);
		this.countries = objects;
		if (callDisp == null) {
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		}
	}

	/**
	 * To get the count of the listview items
	 */
	public int getCount() {
		try {
			return this.countries.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * To get the list of buddies
	 * 
	 * @return
	 */
	public List<ContactLogicbean> getListofBuddy() {
		try {
			return this.countries;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * To get the Databean instance of the particular item
	 */
	public Object getItem(int index) {
		try {
			return this.countries.get(index);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the user name of the particular index
	 * 
	 * @param index
	 * @return
	 */

	public int getId(int index) {
		try {
			ContactLogicbean data = this.countries.get(index);
			return data.getId();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public ContactLogicbean getData(int index) {
		try {
			ContactLogicbean data = this.countries.get(index);
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Create the view of the every row in the Listview
	 */
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		try {
			View row = convertView;
			if (row == null) {
				// ROW INFLATION
				// Log.d(tag, "Starting XML Row Inflation ... "+position);
				LayoutInflater inflater = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.busadpter, parent, false);
				row.setBackgroundColor(Color.TRANSPARENT);

			}
			// Log.d(tag, "Successfully completed XML Row Inflation!");
			row.setBackgroundColor(Color.TRANSPARENT);

			final ContactLogicbean country = (ContactLogicbean) getItem(position);
			run = (Button) row.findViewById(R.id.run);
			edit = (Button) row.findViewById(R.id.edit);
			userName = (TextView) row.findViewById(R.id.txtViewTitle);
			action = (TextView) row.findViewById(R.id.actiontype);
			img = (ImageView) row.findViewById(R.id.icon2);
			img.getLayoutParams().width = 58;
			img.getLayoutParams().height = 65;
			img.setBackgroundResource(R.drawable.icon_buddy_aoffline);
			userName.setText((String) country.getLabel());
			if (country.getAction().equals("SP")) {
				img.setBackgroundResource(R.drawable.icons_share);
				action.setText("Share photo note");

			} else if (country.getAction().equals("SA")) {
				img.setBackgroundResource(R.drawable.icons_share);
				action.setText("Share audio note");

			} else if (country.getAction().equals("ST")) {
				img.setBackgroundResource(R.drawable.icons_share);
				action.setText("Share text note");
			} else if (country.getAction().equals("SV")) {
				img.setBackgroundResource(R.drawable.icons_share);
				action.setText("Share video note");

			} else if (country.getAction().equals("SHS")) {
				img.setBackgroundResource(R.drawable.icons_share);
				action.setText("Share sketch note");

			} else if (country.getAction().equals("AC")) {
				img.setBackgroundResource(R.drawable.icons_call);
				action.setText("Audio call");

			} else if (country.getAction().equals("VC")) {
				img.setBackgroundResource(R.drawable.icons_call);
				action.setText("Video call");
			} else if (country.getAction().equals("ABC")) {
				img.setBackgroundResource(R.drawable.icons_broadcast);
				action.setText("Audio Broadcast");

			} else if (country.getAction().equals("VBC")) {
				img.setBackgroundResource(R.drawable.icons_broadcast);
				action.setText("Video Broadcast");

			} else if (country.getAction().equals("ACF")) {
				img.setBackgroundResource(R.drawable.icons_call);
				action.setText("Audio conference");

			} else if (country.getAction().equals("VCF")) {
				img.setBackgroundResource(R.drawable.icons_call);
				action.setText("Video conference");

			} else if (country.getAction().equals("HC")) {
				img.setBackgroundResource(R.drawable.icons_call);
				action.setText("Hosted Conference Call");

			} else if (country.getAction().equals("Dynamic Audio Call")) {
				img.setBackgroundResource(R.drawable.icons_call);
				action.setText("Dynamic Audio Call");

			} else if (country.getAction().equals("Dynamic Video Call")) {
				img.setBackgroundResource(R.drawable.icons_call);
				action.setText("Dynamic Video Call");

			} else if (country.getAction()
					.equalsIgnoreCase("show results form")) {
				img.setBackgroundResource(R.drawable.icons_report);
				action.setText("Show Results Form");

			}

			edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						int id = getId(position);
						ContactLogicbean bean = callDisp.getdbHeler(
								getContext()).getQucikActionById(
								String.valueOf(id));
						Intent intent = new Intent(getContext(),
								QuickActionTitlecalls.class);
						intent.putExtra("edit", true);
						intent.putExtra("quickAction", bean);
						getContext().startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			run.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						Log.i("blogic", "=======> " + position);
						int id = getId(position);
						ContactLogicbean country = callDisp.getdbHeler(
								getContext()).getQucikActionById(
								String.valueOf(id));

						callDisp.doAction(country.getEditlable(),
								country.getfromuser(), country.getEditToUser(),
								country.getFtpPath(), country.getcontent(),
								country.getAction(), country.getEditconditon());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			return row;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Delete the particular row from the list view with respect to the index of
	 * the row
	 * 
	 * @param x
	 */
	public void deleteRecord(int x) {
		try {
			// Log.e("sel","Before name delete:"+x);
			ContactLogicbean data = countries.get(x);

			// Log.e("sel","Name to be delete:"+data.getname());

			countries.remove(x);
			notifyDataSetChanged();
		} catch (Exception e) {
			// Log.e("sel","Exception on name Delete:"+e.getMessage());
		}

	}

}
