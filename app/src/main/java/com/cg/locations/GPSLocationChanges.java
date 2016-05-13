package com.cg.locations;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;

public class GPSLocationChanges {

	public Context context = null;

	private long minTime;

	private float minDistance;

	public boolean isStarted = false;

	private LocationManager locationManager;

	private LocationListener locationListener = new MyLocationListener();

	private boolean network_enabled = false;

	private CallDispatcher calldisp = null;

	private Timer timer = null;

	private Handler handler = new Handler();

	public GPSLocationChanges(Context context, long minTime, float minDistance) {
		this.context = context;
		this.minTime = minTime;
		this.minDistance = minDistance;

	}

	public void Start() {

		calldisp = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldisp");
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean gps_enabled = false;
		boolean network_enabled = false;

		try {
			gps_enabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			Log.e("loc", "gps_enabled:" + gps_enabled);
		} catch (Exception ex) {
		}
		try {
			network_enabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			Log.e("loc", "network_enabled:" + network_enabled);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (gps_enabled) {
			startTimer();
			requestGpsProvider();
		} else if (network_enabled) {
			requestNetworkProvider();
		}
		Log.d("DDD", "onstart Gps");
		isStarted = true;
	}

	public void Stop() {
		isStarted = false;
		locationManager.removeUpdates(locationListener);

	}

	class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			Log.d("UMBX", " $$$$$$$$$$$$$$$$$$ location picket" + location);
			try {
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
				if (location != null) {

					calldisp.sendLocationtoServer(location.getLatitude(),
							location.getLongitude());
					calldisp.setcurrentlatLong(
							Double.toString(location.getLatitude()),
							Double.toString(location.getLongitude()));
					Log.i("Points", Double.toString(location.getLatitude())
							+ Double.toString(location.getLongitude()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.AVAILABLE:
				// Toast.makeText(Satellite_NetworkProvider.this,"GPS available again",3000).show();
				break;
			case LocationProvider.OUT_OF_SERVICE:

				// /
				// Toast.makeText(Satellite_NetworkProvider.this,"GPS out of service",3000).show();
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				// Toast.makeText(Satellite_NetworkProvider.this,"GPS temporarily unavailable",3000).show();
				break;
			}

		}

	}

	void startTimer() {

		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {

					requestNetworkProvider();// Then this method will execute.
					timer = null;
					handler.post(new Runnable() {

						public void run() {
							Toast.makeText(
									context,
									"GPS provider Failed, switching to network provider",
									3000).show();

						}
					});

					Log.d("GPSS", "Timer Elapsed ");
				}
			}, 10000);
		}

	}

	private void requestNetworkProvider() {
		try {

			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 1000 * 60 * 5, 0,
					locationListener);
			Log.d("GPSS", "Network Provider Enabled");

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void requestGpsProvider() {
		try {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1000 * 60 * 5, 0,
					locationListener);
			Log.d("GPSS", "GPS Provider Enabled");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
