package es.eucm.eadandroid.ecore.control;

import java.util.List;
import java.util.Vector;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import es.eucm.eadandroid.common.data.chapter.GpsRule;
import es.eucm.eadandroid.ecore.GameThread;
import es.eucm.eadandroid.ecore.ECoreActivity.ActivityHandlerMessages;
import es.eucm.eadandroid.ecore.control.functionaldata.FunctionalConditions;
import es.eucm.eadandroid.ecore.control.functionaldata.functionaleffects.FunctionalEffects;

public class GpsManager {
	

	private static GpsManager singleton = null;
	private Vector<GpsRule> gpsActive;
	private Vector<GpsRule> allGpsRules;
	GpsListener listener;
	boolean activeGps=false;
	LocationManager locationManager=null;



	public static GpsManager getInstance() {

		return singleton;
	}

	private GpsManager()  {
		gpsActive = new Vector<GpsRule>();
		allGpsRules = new Vector<GpsRule>();
		listener=new GpsListener(this);
	}

	public static void create() {
		singleton = new GpsManager();
	}
	
	public void addallgpsrules(List<GpsRule> list)
	{
		synchronized(GpsManager.class) {
		this.allGpsRules.removeAllElements();
		
		for(int i=0;i<list.size();i++)
		{
			this.allGpsRules.add(list.get(i));
		}
		
		
		//loads global gpsrules
		for(int i=0;i<list.size();i++)
		{
			if (list.get(i).getSceneName().equals(""))
				this.gpsActive.add(list.get(i));
		}
		}

	}

	public void addgpsrules(GpsRule rule) {
		allGpsRules.add(rule);
	}
	
	public void changeOfScene(String scene)
	{
		
		synchronized(GpsManager.class) {
	//first removes from gpsActive all gps related to other scenes
		for (int i=this.gpsActive.size()-1;i>=0;i=i-1)
		{
			if(!this.gpsActive.elementAt(i).getSceneName().equals(""))
				gpsActive.remove(i);
		}
//then adds gpsrules from new scene		
		for (int i=0;i<this.allGpsRules.size();i++)
		{
			if (this.allGpsRules.elementAt(i).getSceneName().equals(scene))
				gpsActive.add(this.gpsActive.elementAt(i));
		}
		}
	}
	


	public void updategps(Location location) {
		
		location.setAccuracy(1);

		synchronized (GpsManager.class) {
			for (int i = 0; i < gpsActive.size(); i++) {

				if (new FunctionalConditions(gpsActive.elementAt(i)
						.getEndCond()).allConditionsOk()) {
					double distance = distance(location.getLatitude(), location
							.getLongitude(), gpsActive.elementAt(i)
							.getLatitude(), gpsActive.elementAt(i)
							.getLongitude());

					Location d = new Location("");
					d.setLatitude(gpsActive.elementAt(i).getLatitude());
					d.setLongitude(gpsActive.elementAt(i).getLongitude());
					d.setAccuracy(1);

					float metros = location.distanceTo(d);

					String a = new String("logitud " + location.getLongitude()
							+ "latitud " + location.getLatitude()
							+ "longitud2 " + d.getLongitude() + "latitud2 "
							+ d.getLatitude() + "distancia" + metros
							+ "distancia2 " + distance);
					/*
					 * Log.d("PUNTO1", " " +
					 * location.getLongitude()+"   "+location.getLatitude() );
					 * Log.d("punto2", " " +
					 * d.getLongitude()+"   "+d.getLatitude() );
					 * Log.d("DISTANCIA", "" + distance+"   "+metros);
					 */

					Handler handler = GameThread.getInstance().getHandler();

					Message msg = handler.obtainMessage();

					Bundle b = new Bundle();
					b.putString("toast", a);
					msg.what = ActivityHandlerMessages.SHOW_TOAST;
					msg.setData(b);

					msg.sendToTarget();

					if (metros < gpsActive.elementAt(i).getRadio()) {
						FunctionalEffects.storeAllEffects(gpsActive
								.elementAt(i).getEffects());
						this.gpsActive.remove(i);

					}
				}

			}
		}

	}
	
	public GpsListener getListener() {
		return listener;
	}
	
	public void finishgps(){
		listener.finish();
	};

	// ///////////////////////////////////////////////////
	// to calculate de distance between two gsp locations

	

	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		dist = dist * 1000;

		return (dist);
		// return 40;
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
	
	
	public boolean isActiveGps() {
		boolean result=false;
		if (activeGps)
		{
			result=true;
			activeGps=false;
		}
		return result;
	}

	public void setActiveGps(boolean activeGps) {
		this.activeGps = activeGps;
	}
	
	
	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

}
