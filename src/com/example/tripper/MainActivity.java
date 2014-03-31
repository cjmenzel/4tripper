package com.example.tripper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;




import java.net.URLConnection;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements OnCommunicateWithMainActivity, LocationListener{

	public final static String FRAG1_TAG = "FRAG1";

	private InputScreen inputscreen;
	private LocationManager locationManager;
	private String provider;

	float lat;
	float lng;

	private URL mapsURL;

	OnCommunicateWithInputScreen inputlistener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inputscreen = new InputScreen();
		LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!enabled) {
			// creates new activity with settings for GPS
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();

		provider = locationManager.getBestProvider(criteria, false);

		Location location = locationManager.getLastKnownLocation(provider);

		lat = (float) (location.getLatitude());
		lng = (float) (location.getLongitude());

		getFragmentManager().beginTransaction()
		.add(R.id.fragment1, inputscreen, FRAG1_TAG).commit();

		inputlistener = (OnCommunicateWithInputScreen) inputscreen;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onGoButtonPressed(String address) {
		//TODO Send this address to google maps
		mapsURL = null;
		try {
			mapsURL = new URL(formatAddress(address));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new OnNetwork().execute(mapsURL);
	}

	private String formatAddress(String address) {
		String newAddress = null;
		mapsURL = null;

		newAddress = address.replace(' ', '+');
		System.out.println(newAddress);
		System.out.println(lat);
		System.out.println(lng);

		return "https://maps.googleapis.com/maps/api/directions/json?origin="+lat+","+lng+"&destination="+newAddress+"&sensor=true&key=AIzaSyAF6wW8hogpzGNl_3qr1VNbMNl3OiT1yJg";
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	private class OnNetwork extends AsyncTask<URL, Void, String> {

		protected void onPostExecute(Bitmap result) {

		}

		protected String doInBackground(URL... params) {

			URL url = params[0];
			String json = null;
			try {

				URLConnection uc = url.openConnection();
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
								uc.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) 
					System.out.println(inputLine);
				in.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}
	}

}
