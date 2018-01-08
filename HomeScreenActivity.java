package com.example.android.locationreached;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

    }

    /**+
     * takes user to create a new request
     * @param view
     */
    public void onClickRequest(View view){
        Intent intent = new Intent(getBaseContext(), RequestLocationActivity.class);
        intent.putExtra("USERKEY", getIntent().getStringExtra("USERKEY"));
        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
        startActivity(intent);

    }

    /**+
     * takes user to see all their received requests
     * @param view
     */
    public void onClickReceived(View view){
        Intent intent = new Intent(getBaseContext(), ReceivedRequestsActivity.class);
        intent.putExtra("USERKEY", getIntent().getStringExtra("USERKEY"));
        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
        startActivity(intent);

    }

    /**+
     * takes user to see all their sent requests
     * @param view
     */
    public void onClickSent(View view){
        Intent intent = new Intent(getBaseContext(), SentRequestsActivity.class);
        intent.putExtra("USERKEY", getIntent().getStringExtra("USERKEY"));
        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
        startActivity(intent);
    }

    /**+
     * back to the login page
     * @param view
     */
    public void onClickLogout(View view){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
}
