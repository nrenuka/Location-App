package com.example.android.locationreached;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SentRequestsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_background);
        Intent intent = getIntent();
        final String key = intent.getStringExtra("USERKEY");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference locations = database.getReference("locations");

        final FirebaseRecyclerAdapter<LocationLog, SentRequestsHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<LocationLog, SentRequestsHolder>(LocationLog.class, R.layout.sent_requests_activity,
                        SentRequestsHolder.class, locations.orderByChild("requesterKey").equalTo(key)) {
                    @Override
                    protected void populateViewHolder(SentRequestsHolder holder, LocationLog LOCATION, int position) {
                        holder.userView.setText(LOCATION.requesteeUN);
                        holder.latView.setText("" + LOCATION.latitude);
                        holder.longView.setText("" + LOCATION.longitude);
                        holder.statusView.setText(LOCATION.status);
                    }
                };

        mRecyclerView = (RecyclerView) findViewById(R.id.rvSent);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class SentRequestsHolder extends RecyclerView.ViewHolder {

        public TextView userView;
        public TextView latView;
        public TextView longView;
        public TextView statusView;

        public SentRequestsHolder(View itemView) {
            super(itemView);
            userView = (TextView) itemView.findViewById(R.id.username);
            latView = (TextView) itemView.findViewById(R.id.latitude);
            longView = (TextView) itemView.findViewById(R.id.longitude);
            statusView = (TextView) itemView.findViewById(R.id.status);

        }

    }
}
