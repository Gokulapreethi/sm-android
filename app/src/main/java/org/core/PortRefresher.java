package org.core;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;




public class PortRefresher extends BroadcastReceiver  
{   

	
    @Override 
    public void onReceive(Context context, Intent intent)  
    {  
    	Log.d("PORTREFERESH","onreceive test  ");
    	

    	 
    	
    	
        PowerManager.WakeLock wl=null;
    	try{
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE); 
         wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG"); 
        wl.acquire();
       
    	}
    	catch (Exception e) {
    		Log.d("PORTREFERESH","onreceive "+e);	
		}
       
    } 

public void makeStart(Context context)
{ 
	try{
	
    AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE); 
    Intent i = new Intent(context, PortRefresher.class); 
    PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0); 
    am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 15000, pi); // Millisec * Second * Minute 
	}
	catch (Exception e) {
		
	}
} 

public void makeStop(Context context) 
{ 
	
	 PowerManager.WakeLock wl=null;
 	try{
 		Log.d("PORTREFERESH","onreceive wl "+wl);	
 		Log.d("PORTREFERESH","onreceive "+context);	
     PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
     Log.d("PORTREFERESH","onreceive "+pm);	
      wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
      Log.d("PORTREFERESH","onreceive "+wl);	
     wl.acquire();
     wl.release();
    
 	}
 	catch (Exception e) {
 		Log.d("PORTREFERESH","onreceive "+e);	
		}
	
	try{
    Intent intent = new Intent(context, PortRefresher.class); 
    PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0); 
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); 
    alarmManager.cancel(sender);
	}
	catch (Exception e) {
		
	}
	
	
} 
} 