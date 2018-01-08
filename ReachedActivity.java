package com.example.android.locationreached;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReachedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reached);


    }
    public void onClickToHome(View view){
        Intent intent = new Intent(getBaseContext(), HomeScreenActivity.class);
        intent.putExtra("USERKEY", getIntent().getStringExtra("USERKEY"));
        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
        startActivity(intent);

    }


}
