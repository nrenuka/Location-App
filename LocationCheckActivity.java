package com.example.android.locationreached;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by renuka on 5/2/17.
 */

public class LocationCheckActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location target;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_location);

        this.target = new Location("provider");
        this.target.setLatitude(Double.parseDouble(getIntent().getStringExtra("LAT")));
        this.target.setLongitude(Double.parseDouble(getIntent().getStringExtra("LONG")));

        // Create the location client to start receiving updates
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }

    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }


    protected void onStop() {
        // Disconnecting the client invalidates it.
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,  this);

        // only stop if it's connected, otherwise we crash
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onConnected(Bundle dataBundle) {
        Log.d("DO YOU WORK HERE4", "im guessing no");
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("PERMISSIONS NOT MET", "help");
            /*requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.INTERNET
            }, 10);*/
            return;

        }

        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        Log.d("DO YOU WORK HERE7", "im guessing no");
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            Log.d("LOCATIONS", ""+mCurrentLocation.getLatitude() + "   ,   "+mCurrentLocation.getLongitude());

        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("CONNECTION", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**+
     * Every location update is checked with target location, if reached, update the database and return to the home screen
     * @param location
     */
    public void onLocationChanged(Location location) {
        // New location has now been determined
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference locations = database.getReference("locations");

        TextView targetStatus = (TextView) findViewById(R.id.textView8);
        TextView userloc = (TextView) findViewById(R.id.user);
        TextView targetloc = (TextView) findViewById(R.id.target);
        userloc.setText("CURRENT: "+location.getLatitude()+", " + location.getLongitude());
        targetloc.setText("TARGET: "+target.getLatitude()+", " + target.getLongitude());
        targetStatus.setText("NOT REACHED");
        Toast.makeText(this, "DISTANCE TO TARGET: "+reachedTargetLocationdistance(location, target), Toast.LENGTH_SHORT).show();

        if(reachedTargetLocation(location, target)){
            //targetStatus.setText("REACHED");


            //update database
            String locationKey = getIntent().getStringExtra("LOCKEY");
            locations.child(locationKey).child("status").setValue("reached");

            Intent intent = new Intent(getBaseContext(), ReachedActivity.class);
            intent.putExtra("USERKEY", getIntent().getStringExtra("USERKEY"));
            intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
            startActivity(intent);
        }
    }

    /**+
     * returns true if user's location is within 40 meters on the target location
     * @param user
     * @param target
     * @return
     */
    public boolean reachedTargetLocation(Location user, Location target){
        double distance = 0;
        distance = user.distanceTo(target);
        return (distance <= 40);
    }
    public double reachedTargetLocationdistance(Location user, Location target){
        double distance = 0;
        distance = user.distanceTo(target);
        return (distance);
    }


}
