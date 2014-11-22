package de.impressive.artworx.tutorials.gps;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This demo program displays the current GPS location. This information is updated every 10 seconds when the
 * position changes for at least 10 meters. Status changes of the GPS signal are also reported.
 *
 * When the GPS is disabled the user is asked to enable it through the settings dialog.
 *
 * This tutorial is based on the example given in http://hejp.co.uk/android/android-gps-example/
 * 
 * Copyright 2k11 Impressive Artworx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Manuel Schwarz (m.schwarz[at]impressive-artworx.de)
 */
public class GPSTest extends Activity implements LocationListener {
   
   private TextView mInfoText;
   private LocationManager mLoc;
   
   private static final Integer MINIMUM_UPDATE_INTERVAL = 10000; // update every 10 seconds
   private static final Integer MINIMUM_UPDATE_DISTANCE = 10;    // update every 10 meters
   
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      
      // get a handle to the text view to display the GPS location data
      mInfoText = (TextView) findViewById(R.id.infotext);
      
      // the location manager allows access to the current location and GPS status
      mLoc = (LocationManager) getSystemService(LOCATION_SERVICE);
   }
   
   @Override
   /**
    * onResume is is always called after onStart, even if the app hasn't been paused
    */
   protected void onResume() {
      // add a location listener and request updates every 10000ms or 10m
      mLoc.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_UPDATE_INTERVAL, 
            MINIMUM_UPDATE_DISTANCE, this);
      super.onResume();
   }
   
   @Override
   protected void onPause() {
      // GPS, as it turns out, consumes battery like crazy
      mLoc.removeUpdates(this);
      super.onPause();
   }
   
   @Override
   protected void onStop() {
      // may as well just finish since saving the state is not important for this toy app
      finish();
      super.onStop();
   }
   
   @Override
   public void onLocationChanged(Location loc) {
      // display some information based on the current position
      StringBuilder sb = new StringBuilder("Your current location is:\n\n");

      sb.append("Longitude: ");
      sb.append(loc.getLongitude());
      sb.append('\n');

      sb.append("Latitude: ");
      sb.append(loc.getLatitude());
      sb.append('\n');

      sb.append("Altitiude: ");
      sb.append(loc.getAltitude());
      sb.append('\n');

      sb.append("Accuracy: ");
      sb.append(loc.getAccuracy());
      sb.append('\n');

      sb.append("Timestamp: ");
      Date timestamp = new Date(loc.getTime());
      sb.append(new SimpleDateFormat().format(timestamp));

      mInfoText.setText(sb.toString());
   }
   
   @Override
   public void onProviderDisabled(String provider) {
      // called if/when the GPS is disabled in settings
      Toast.makeText(this, "GPS disabled", Toast.LENGTH_LONG).show();

      // end program since we depend on GPS
      AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
      alertbox.setMessage("This demo app requires GPS. Please activate it first!");
      alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface arg0, int arg1) {
             finish();
          }
      });
      alertbox.show();
   }
   
   @Override
   public void onProviderEnabled(String provider) {
      Toast.makeText(this, "GPS enabled", Toast.LENGTH_LONG).show();
   }
   
   @Override
   public void onStatusChanged(String provider, int status, Bundle extras) {
      // called upon GPS status changes
      switch (status) {
      case LocationProvider.OUT_OF_SERVICE:
         Toast.makeText(this, "Status changed: out of service", Toast.LENGTH_LONG).show();
         break;
      case LocationProvider.TEMPORARILY_UNAVAILABLE:
         Toast.makeText(this, "Status changed: temporarily unavailable", Toast.LENGTH_LONG).show();
         break;
      case LocationProvider.AVAILABLE:
         Toast.makeText(this, "Status changed: available", Toast.LENGTH_LONG).show();
         break;
      }
   }
}