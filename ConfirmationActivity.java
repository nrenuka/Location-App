package com.example.android.locationreached;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        //have not created object on the database yet - this is just for confirmation from the user
        TextView confirmation = (TextView) findViewById(R.id.confirmRequest);
        String lat = getIntent().getStringExtra("LATITUDE");
        String lon = getIntent().getStringExtra("LONGITUDE");
        String username = getIntent().getStringExtra("TARGETUSERNAME");
        confirmation.setText("Latitude: " + lat + "\nLongitude: " + lon + "\nTarget Username: " + username);
    }


    /**+
     * creates a new LocationLog object to load on the database
     *
     * continues to send username and key back to homescreen
     * @param view
     */
    public void onClickConfirmToCreate (View view){
        Double lat = Double.parseDouble(getIntent().getStringExtra("LATITUDE").toString());
        Double lon = Double.parseDouble(getIntent().getStringExtra("LONGITUDE"));
        String targetUserKey = getIntent().getStringExtra("TARGETUSERKEY");
        String targetUserName = getIntent().getStringExtra("TARGETUSERNAME");
        String userKey = getIntent().getStringExtra("USERKEY");
        String username = getIntent().getStringExtra("USERNAME");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference locations = database.getReference("locations");

        DatabaseReference newLocation = locations.push();

        LocationLog locationLog = new LocationLog(lat, lon, targetUserKey,userKey, targetUserName, username , newLocation.getKey());
        newLocation.setValue(locationLog);

        Intent intent = new Intent(getBaseContext(), HomeScreenActivity.class);
        intent.putExtra("USERKEY", userKey);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }
}
