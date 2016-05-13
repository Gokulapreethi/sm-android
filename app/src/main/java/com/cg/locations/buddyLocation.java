package com.cg.locations;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.utilities.UtilityBuyer;
import com.cg.utilities.UtilitySeller;
import com.cg.utilities.UtilityServiceNeeder;
import com.cg.utilities.UtilityServiceProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.group.chat.GroupChatActivity;
import com.util.SingleInstance;

public class buddyLocation extends FragmentActivity {
	private Context context;

	double lati = 0;

	double longi = 0;

	private GoogleMap map;

	String address;

	private String latitude = null;

	private String longtidue = null;

	private TextView locationAddress = null;

	@Override
	protected void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.im_location_map);
		context = this;
		latitude = getIntent().getStringExtra("latitude");
		longtidue = getIntent().getStringExtra("longitude");
		locationAddress = (TextView) findViewById(R.id.address);
		CallDispatcher.pdialog = new ProgressDialog(context);
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		map.getUiSettings().setZoomControlsEnabled(false);
		cancelProgressDialog();
		/** GetDisply Screen Height and Width */
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		String locAddress = null;
		double lat = Double.parseDouble(latitude);
		double lon = Double.parseDouble(longtidue);
		locAddress = getLocationAddress(lat, lon);
		locationAddress.setText(locAddress);
		findLocation(locAddress);
		
		

	}

	public GoogleMap findLocation(String strLocation) {
		Geocoder geocoder = new Geocoder(this);

		try {
			map.clear();
			List<Address> addresses = null;
			addresses = geocoder.getFromLocationName(strLocation, 1);
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (addresses != null) {
				if (addresses.size() > 0) {

					lati = addresses.get(0).getLatitude();
					longi = addresses.get(0).getLongitude();

					LatLng latlongpos = new LatLng(lati, longi);
					Marker marker = map.addMarker(new MarkerOptions()
							.position(latlongpos)
							.title(strLocation)
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.marker)));
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(
							latlongpos, 15));

					map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000,
							null);
				} else {
					latitude = getIntent().getStringExtra("latitude");
					longtidue = getIntent().getStringExtra("longitude");
					double lat = Double.parseDouble(latitude);
					double lon = Double.parseDouble(longtidue);
					String locAddress = getLocationAddress(lat, lon);
					findLocation(locAddress);

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public String getLocationAddress(double lati, double longi) {
		String add = "";
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);

			if (addresses != null && addresses.size() != 0) {
				Address obj = addresses.get(0);
				String strAddress = obj.getAddressLine(0);
				String strCountryName = obj.getCountryName();
				String strState = obj.getAdminArea();
				String strDist = obj.getSubAdminArea();
				String strArea = obj.getLocality();

				if (strAddress != null) {
					add = strAddress;
				}
				if (strArea != null && strDist != null) {
					if (strDist.trim().equalsIgnoreCase(strArea.trim())) {
						add = add + "," + strDist;
						if (strState != null) {
							add = add + "," + strState;
						}
						if (strCountryName != null) {
							add = add + "," + strCountryName;
						}
					} else {
						if (strArea != null) {
							add = add + "," + strArea;
						}
						if (strDist != null) {
							add = add + "," + strDist;
						}
						if (strState != null) {
							add = add + "," + strState;
						}
						if (strCountryName != null) {
							add = add + "," + strCountryName;
						}
					}
				} else {
					if (strState != null) {
						add = add + "," + strState;
					}
					if (strCountryName != null) {
						add = add + "," + strCountryName;
					}
				}

				// add=strArea;

				Log.v("IGA", "Address" + add);

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

		return add;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void cancelProgressDialog(){
		GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
				.get("groupchat");
		if (gChat != null) {
			gChat.cancelDialog();
		}
		UtilityBuyer buyer = (UtilityBuyer)WebServiceReferences.contextTable.get("utilitybuyer");
		if(buyer!=null){
			buyer.cancelDialog();
		}
		UtilitySeller seller = (UtilitySeller)WebServiceReferences.contextTable.get("utilityseller");
		if(seller!=null){
			seller.cancelDialog();
		}
		UtilityServiceNeeder needer = (UtilityServiceNeeder)WebServiceReferences.contextTable.get("utilityneeder");
		if(needer!=null){
			needer.cancelDialog();
		}
		UtilityServiceProvider provider = (UtilityServiceProvider)WebServiceReferences.contextTable.get("utilityprovider");
		if(provider!=null){
			provider.cancelDialog();
		}
	}
}
