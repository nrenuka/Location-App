package com.example.android.locationreached;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class RequestLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_request);
    }

    /**+
     * retrieves all the information user just submitted
     * @param view
     */
    public void onClickConfirm(View view){
        EditText latitudeView = (EditText) findViewById(R.id.targetLatitude);
        String  latitudeStr = latitudeView.getText().toString();

        EditText longitudeView = (EditText) findViewById(R.id.targetLongitude);
        String  longitudeStr = longitudeView.getText().toString();

        EditText usernameView = (EditText) findViewById(R.id.targetUser);
        String  username = usernameView.getText().toString();


        if(latitudeStr.isEmpty() || longitudeStr.isEmpty() || username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Some fields are incomplete", Toast.LENGTH_SHORT).show();
        }
        else {
            confirmUsername(username, latitudeStr, longitudeStr);
        }
    }


    /**+
     * confirms if target user is a valid user, then sends information to the confirmation screen
     * @param username
     * @param latitude
     * @param longitude
     */
    public void confirmUsername(final String username, final String latitude, final String longitude){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("users");

        users.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean userExists = false;
                String targetUserKey = "";
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();

                while (it.hasNext()) {
                    DataSnapshot user = it.next();
                    if (user.getValue(User.class).username.equalsIgnoreCase(username)) {
                        userExists = true;
                        targetUserKey = user.getKey();
                        break;
                    }
                }

                if (userExists) {
                    Intent intent = new Intent(getBaseContext(), ConfirmationActivity.class);
                    intent.putExtra("USERKEY", getIntent().getStringExtra("USERKEY"));
                    intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                    intent.putExtra("LATITUDE", latitude);
                    intent.putExtra("LONGITUDE", longitude);
                    intent.putExtra("TARGETUSERNAME", username);
                    intent.putExtra("TARGETUSERKEY", targetUserKey);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Username: " + username + " Invalid", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DATABASE", "error");
            }
        });
    }
}



