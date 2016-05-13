package com.cg.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lib.model.SignalingBean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.Databean;
import com.cg.commonclass.WebServiceReferences;

/**
 * To create the adapter for the Listview.It will differentiate the online and
 * off line state to the user
 * 
 * 
 * 
 */
public class CountryArrayAdapter extends ArrayAdapter {
	private Context context;
	private ImageView userIcon;
	private TextView userName, instant;
	private TextView userStatus;
	ImageView img, bluedot;
	private List<Databean> countries = new ArrayList<Databean>();
	CallDispatcher calldisp = null;
	Bitmap bitmap = null;
	private TextView tv_distance;
	private TextView tv_address;
	private Button im_view;

	/**
	 * Constructor method to create the instance to the adapter to populate the
	 * list of buddies
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public CountryArrayAdapter(Context context, int textViewResourceId,
			List objects) {
		super(context, textViewResourceId, objects);
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			this.calldisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			this.calldisp = new CallDispatcher(context);
//		List<Databean> finalList = getNewList(objects);

		this.context = context;
		this.countries = objects;
	}

	/**
	 * To get the count of the listview items
	 */
	public int getCount() {
		return this.countries.size();
	}

	private List getNewList(List<Databean> contactList) {
		List cList = new ArrayList();
		HashMap<String, Databean> contactHashMap = new HashMap<String, Databean>();
		for (Databean databean : contactList) {
			contactHashMap.put(databean.getname(), databean);
		}
		Set contactsSet = (Set) contactHashMap.entrySet();
		Log.i("files123", "selected size1 : " + contactsSet.size());
		Iterator mapIterator = contactsSet.iterator();
		while (mapIterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) mapIterator.next();
			Databean clBean = (Databean) mapEntry.getValue();
			cList.add(clBean);
		}
		return cList;

	}

	/**
	 * To get the list of buddies
	 * 
	 * @return
	 */
	public List<Databean> getListofBuddy() {
		return this.countries;
	}

	/**
	 * To get the Databean instance of the particular item
	 */
	public Object getItem(int index) {
		return this.countries.get(index);
	}

	/**
	 * Get the user name of the particular index
	 * 
	 * @param index
	 * @return
	 */
	public String getUser(int index) {
		Databean data = this.countries.get(index);
		return data.getname().trim();
	}

	public View getView(int position, final View convertView,
			final ViewGroup parent) {
		View row = convertView;
		try {
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.relate, null, false);

			}
			Databean country = (Databean) getItem(position);
			userIcon = (ImageView) row.findViewById(R.id.icon1);
			userName = (TextView) row.findViewById(R.id.txtViewTitle);
			userStatus = (TextView) row.findViewById(R.id.txtViewStatus);
			instant = (TextView) row.findViewById(R.id.instantmessage);
			img = (ImageView) row.findViewById(R.id.icon2);
			img.setVisibility(View.VISIBLE);
			im_view = (Button) row.findViewById(R.id.im_view);
			bluedot = (ImageView) row.findViewById(R.id.icon3);
			userName.setText((String) country.getname());
			tv_distance = (TextView) row.findViewById(R.id.txtdistance);
			tv_distance.setVisibility(View.GONE);
			tv_address = (TextView) row.findViewById(R.id.tv_baddress);
			tv_address.setVisibility(View.GONE);
			calldisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
			String message = CallDispatcher.message_map.get(country.getname()
					.trim());

			if (message != null) {
				if (message.trim().length() > 0) {
					instant.setText(message);
					instant.setVisibility(View.VISIBLE);
				} else
					instant.setVisibility(View.GONE);
			} else {
				String InstantMessage = (String) country.getInstantmessage();

				if (InstantMessage == null) {
					instant.setVisibility(View.GONE);
				} else if (InstantMessage.equalsIgnoreCase("No Messages"))
					instant.setVisibility(View.GONE);
				else {
					if (country.getInstantmessage().trim().length() > 0) {
						instant.setText((String) country.getInstantmessage());
						instant.setVisibility(View.VISIBLE);
					} else
						instant.setVisibility(View.GONE);

				}
			}
			userName.setTextColor(Color.BLACK);
			String path = null;
//			if (WebServiceReferences.buddyList.containsKey(country.getname()))
//				path = WebServiceReferences.buddyList.get(country.getname())
//						.getProfile_picpath();

			if (path != null) {
				if (path.length() > 0) {
					bitmap = calldisp.setProfilePicture(path,
							R.drawable.icon_buddy_aoffline);
				} else
					bitmap = BitmapFactory.decodeResource(
							context.getResources(),
							R.drawable.icon_buddy_aoffline);
			} else {
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.icon_buddy_aoffline);
			}
			if (bitmap == null)
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.icon_buddy_aoffline);

			// if (country.getDistance() != null) {
			// if (country.getDistance().equalsIgnoreCase("nil")
			// || country.getDistance().equalsIgnoreCase("0.0")
			// || country.getDistance().trim().length() == 0)
			// tv_distance.setVisibility(View.INVISIBLE);
			// else {
			// tv_distance.setText(country.getDistance());
			// tv_distance.setVisibility(View.VISIBLE);
			// }
			// }
			//
			
			// if (country.getDistance() != null) {
			// if (country.getDistance().equalsIgnoreCase("0.0")) {
			// tv_distance.setText("Near");
			// tv_distance.setVisibility(View.VISIBLE);
			// } else if (country.getDistance().equalsIgnoreCase("disabled")
			// || country.getDistance().equalsIgnoreCase("nil")) {
			// tv_distance.setVisibility(View.INVISIBLE);
			// } else {
			// tv_distance.setText(country.getDistance());
			// tv_distance.setVisibility(View.VISIBLE);
			// }
			// }
			//
			// if (country.getAddress() != null) {
			// tv_address.setText(country.getAddress());
			// if (country.getDistance().equalsIgnoreCase("disabled")) {
			// tv_address.setVisibility(View.GONE);
			// } else {
			// tv_address.setVisibility(View.VISIBLE);
			// }
			//
			// } else
			// tv_address.setVisibility(View.GONE);
			/**
			 * Ends here
			 */
			if (country.getStatus().startsWith("Off")) {
				userStatus.setText("Offline");
				userIcon.setImageBitmap(bitmap);
				img.setBackgroundResource(R.drawable.n_offline);
			} else if (country.getStatus().equalsIgnoreCase("Virtual")) {
				row.setBackgroundColor(Color.GRAY);
				userStatus.setText("");
				userIcon.setImageBitmap(bitmap);
				img.setBackgroundResource(R.drawable.n_offline);
			} else if (country.getStatus().startsWith("Onli")) {
				// bitmap = BitmapFactory.decodeResource(context.getResources(),
				// R.drawable.icon_buddy_available);
				userStatus.setText("Online");
				userIcon.setImageBitmap(bitmap);
				img.setBackgroundResource(R.drawable.n_online);
			} else if (country.getStatus().startsWith("Away")) {
				userIcon.setImageBitmap(bitmap);
				userStatus.setText("Away");
				img.setBackgroundResource(R.drawable.m_away);
			} else if (country.getStatus().startsWith("Ste")) {
				userIcon.setImageBitmap(bitmap);
				userStatus.setText("Offline");
				img.setBackgroundResource(R.drawable.m_offline);
			} else if (country.getStatus().startsWith("Airport")) {
				userIcon.setImageBitmap(bitmap);
				userStatus.setText("Busy");
				img.setBackgroundResource(R.drawable.m_busy);
			} else if (country.getStatus().equalsIgnoreCase("Pending")) {
				userIcon.setImageBitmap(bitmap);
				userStatus.setText("Pending");
				img.setBackgroundResource(R.drawable.n_offline);

			}
			/**
			 * Im view
			 */
			if (WebServiceReferences.activeSession.size() > 0) {

				String session_id = WebServiceReferences.activeSession
						.get(country.getname());
				if (session_id != null) {
					if (WebServiceReferences.Imcollection
							.containsKey(session_id)) {
						ArrayList<SignalingBean> sbean = WebServiceReferences.Imcollection
								.get(session_id);
						if (sbean != null) {
							im_view.setVisibility(View.VISIBLE);
							im_view.setText(Integer.toString(sbean.size()));
						} else
							im_view.setVisibility(View.GONE);
					} else
						im_view.setVisibility(View.GONE);
				} else
					im_view.setVisibility(View.GONE);
			} else {
				im_view.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return row;
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
			Databean data = countries.get(x);

			// Log.e("sel","Name to be delete:"+data.getname());

			countries.remove(x);
		} catch (Exception e) {
			// Log.e("sel","Exception on name Delete:"+e.getMessage());
		}

	}

	public void AddallItems(ArrayList<Databean> list) {
		this.countries.clear();
		this.countries.addAll(list);
		notifyDataSetChanged();
	}

	public void removeItem(int pos) {
		countries.remove(pos);
	}

//	public void deleteUser(String name) {
//		for (int i = 0; i < countries.size(); i++) {
//			try {
//				Databean data = countries.get(i);
//				if (data.getname().equalsIgnoreCase(name)) {
//					countries.remove(i);
//					Log.i("contacts123", "pendinglist size 1 : "
//							+ calldisp.pendinglist.size());
//					calldisp.pendinglist.remove(data);
//					Log.i("contacts123", "pendinglist size 2 : "
//							+ calldisp.pendinglist.size());
//				}
//
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
//	}

	public boolean ContainsUser(String name) {
		for (int i = 0; i < countries.size(); i++) {
			try {
				Databean data = countries.get(i);
				if (data.getname().equalsIgnoreCase(name)) {

					return true;
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return false;
	}

}
