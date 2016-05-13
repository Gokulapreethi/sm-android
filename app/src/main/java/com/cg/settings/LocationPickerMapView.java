package com.cg.settings;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.locations.GPSLocationChanges;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.util.SingleInstance;

public class LocationPickerMapView extends FragmentActivity {
	private Context context;

	private CallDispatcher dispatcher;

	double lati = 0;

	double longi = 0;

	private GoogleMap map;

	String address;

	Button btnSearch;

	EditText edSearchLocation;

	String[] locs;

	private TextView heading = null;

	private Button btn_back = null;

	private String location = null;

	@Override
	protected void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title1);

		context = this;
		if (!WebServiceReferences.callDispatch.containsKey("calldisp")) {
			dispatcher = new CallDispatcher(this);
		} else {
			dispatcher = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		}
		location = getIntent().getStringExtra("loc");
		btn_back = (Button) findViewById(R.id.settings);
		btn_back.setBackgroundResource(R.drawable.ic_action_back);
		heading = (TextView) findViewById(R.id.note_date);
		heading.setText(SingleInstance.mainContext.getResources().getString(R.string.location));
		setContentView(R.layout.location_picker_view);
		btnSearch = (Button) findViewById(R.id.btn_search_location);
		edSearchLocation = (EditText) findViewById(R.id.ed_search_location);
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		/** GetDisply Screen Height and Width */
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		String locAddress = null;
		if (location == null) {
			locs = dispatcher.getcurrentLocation();
			if (locs != null) {
				double lat = Double.parseDouble(locs[0]);
				double lon = Double.parseDouble(locs[1]);
				locAddress = getLocationAddress(lat, lon);
				edSearchLocation.setText(locAddress);
				findLocation(locAddress);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.location_services_not_active));
				builder.setMessage(SingleInstance.mainContext.getResources().getString(R.string.please_enable_location_services_gps));
				builder.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialogInterface, int i) {
								// Show location settings when the user
								// acknowledges
								// the alert dialog
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(intent);
							}
						});
				Dialog alertDialog = builder.create();
				alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.show();

			}
		} else {
			edSearchLocation.setText(location);
			findLocation(location);
		}

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String str = edSearchLocation.getText().toString();

				if (str.length() != 0) {
					findLocation(str);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							edSearchLocation.getWindowToken(), 0);
				} else {
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.place_to_search),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (lati != 0 && longi != 0) {
					Intent i = new Intent();
					Bundle bun = new Bundle();
					bun.putString("latitude", "" + lati);
					bun.putString("longitude", "" + longi);
					bun.putString("address",
							"" + getLocationAddress(lati, longi));

					i.putExtra("location", bun);
					setResult(-8, i);
					finish();
				} else {
					showCloseScreenSaveAlert();
				}
			}
		});

	}

	private void showSearchWindow() {

		final AlertDialog adb = new AlertDialog.Builder(context).create();
		adb.setTitle("Map Search");

		LinearLayout llay = new LinearLayout(this);
		llay.setOrientation(LinearLayout.VERTICAL);

		final EditText edSearch = new EditText(this);
		edSearch.setHint(SingleInstance.mainContext.getResources().getString(R.string.place_to_search));

		Button btnSearch = new Button(this);
		btnSearch.setText(SingleInstance.mainContext.getResources().getString(R.string.find));
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String str = edSearch.getText().toString();

				if (str.length() != 0) {
					findLocation(str);
					adb.dismiss();
				} else {
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.place_to_search),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		llay.addView(edSearch);
		llay.addView(btnSearch);
		adb.setView(llay);
		adb.show();
	}

	private void findLocation(String strLocation) {
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
							.title("Position")
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.marker)));
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(
							latlongpos, 15));

					map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000,
							null);

				} else {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							LocationPickerMapView.this);
					adb.setTitle(SingleInstance.mainContext.getResources().getString(R.string.google_map));
					adb.setMessage(SingleInstance.mainContext.getResources().getString(R.string.please_provide_the_proper_place));
					adb.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.close), null);

					adb.show();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Menu m_menu = menu;
		m_menu.add(Menu.NONE, Menu.FIRST + 3, 0, SingleInstance.mainContext.getResources().getString(R.string.set_location));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case Menu.FIRST + 0:

			showSearchWindow();
			break;

		case Menu.FIRST + 1:
			Toast.makeText(this, SingleInstance.mainContext.getResources().getString(R.string.satellite_view_selected), Toast.LENGTH_LONG)
					.show();

			break;
		case Menu.FIRST + 2:
			Toast.makeText(this,SingleInstance.mainContext.getResources().getString(R.string.street_view_selected), Toast.LENGTH_LONG)
					.show();

			break;

		case Menu.FIRST + 3:
			Intent i = new Intent();
			Bundle bun = new Bundle();
			bun.putString("latitude", "" + lati);
			bun.putString("longitude", "" + longi);
			// bun.putString("address", "" + getLocationAddress(lati, longi));

			i.putExtra("location", bun);
			setResult(-8, i);
			finish();
			break;

		}
		return super.onOptionsItemSelected(item);

	}

	// private void showSearchWindow() {
	//
	// final AlertDialog adb = new AlertDialog.Builder(context).create();
	// adb.setTitle("Map Search");
	//
	// LinearLayout llay = new LinearLayout(this);
	// llay.setOrientation(LinearLayout.VERTICAL);
	//
	// final EditText edSearch = new EditText(this);
	// edSearch.setHint("Enter search place");
	//
	// Button btnSearch = new Button(this);
	// btnSearch.setText("Find");
	// btnSearch.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// String str = edSearch.getText().toString();
	//
	// if (str.length() != 0) {
	// findLocation(str);
	// adb.dismiss();
	// } else {
	// Toast.makeText(context, "Please enter place to search",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	// });
	//
	// llay.addView(edSearch);
	// llay.addView(btnSearch);
	// adb.setView(llay);
	// adb.show();
	// }

	// protected void createMarker(GeoPoint geoPoint) {
	//
	// Log.d("loc", "getLatitudeE6 :" + geoPoint.getLatitudeE6());
	//
	// Log.d("loc", "getLongitudeE6 :" + geoPoint.getLongitudeE6());
	//
	// // mapView.getOverlays().clear();
	// // mapView.invalidate();
	//
	// // OverlayItem overlayitem = new OverlayItem(geoPoint, "", "");
	// // itemizedoverlay.addOverlay(overlayitem);
	//
	// // mapView.getOverlays().add(itemizedoverlay);
	// }
	//
	// private void createMarker(int lati, int lngti) {
	//
	// Log.e("loc", "Loc:" + lati + "," + lngti);
	// final GeoPoint geo = new GeoPoint(lati, lngti);
	// // mapView.post(new Runnable() {
	// //
	// // @Override
	// // public void run() {
	// //
	// // mapController.animateTo(geo);
	// // mapController.setZoom(12);
	// //
	// // }
	// // });
	//
	// // OverlayItem overlayitem = new OverlayItem(geo, "", "");
	// // itemizedoverlay.addOverlay(overlayitem);
	// // mapView.getOverlays().add(itemizedoverlay);
	// }
	//
	private String getLocationAddress(double lati, double longi) {
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (lati != 0 && longi != 0) {
				Intent i = new Intent();
				Bundle bun = new Bundle();
				bun.putString("latitude", "" + lati);
				bun.putString("longitude", "" + longi);
				bun.putString("address", "" + getLocationAddress(lati, longi));

				i.putExtra("location", bun);
				setResult(-8, i);
				finish();
			} else {
				showCloseScreenSaveAlert();
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	protected void showCloseScreenSaveAlert() {

		AlertDialog.Builder closeAlert = new AlertDialog.Builder(this);
		closeAlert.setTitle(SingleInstance.mainContext.getResources().getString(R.string.close_confirmation));
		closeAlert
				.setMessage(SingleInstance.mainContext.getResources().getString(R.string.search_location_is_not_saved_exit));
		closeAlert.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						finish();
					}
				});
		closeAlert.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		closeAlert.show();

	}

	private void showToast(String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		double lat;
		double lon;
		locs = dispatcher.getcurrentLocation();
		if (locs != null) {
			lat = Double.parseDouble(locs[0]);
			lon = Double.parseDouble(locs[1]);
			String locAddress = getLocationAddress(lat, lon);
			findLocation(locAddress);
		} else {
			GPSLocationChanges gps = new GPSLocationChanges(context, 1000, 9);
			gps.Start();
			if (locs != null) {
				lat = Double.parseDouble(locs[0]);
				lon = Double.parseDouble(locs[1]);
				String locAddress = getLocationAddress(lat, lon);
				findLocation(locAddress);
			}
		}
	}

}
