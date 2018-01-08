package com.example.android.locationreached;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReceivedRequestsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<LocationLog> locationList = new ArrayList<>();
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.received_background);
        Intent intent = getIntent();
        final String key = intent.getStringExtra("USERKEY");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference locations = database.getReference("locations");

        final FirebaseRecyclerAdapter<LocationLog, SentRequestsHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<LocationLog, SentRequestsHolder>(LocationLog.class, R.layout.received_requests_activity,
                        SentRequestsHolder.class, locations.orderByChild("requesteeKey").equalTo(key)) {
                    @Override
                    protected void populateViewHolder(final SentRequestsHolder holder, final LocationLog LOCATION, int position) {
                        holder.userView.setText(LOCATION.requesterUN);
                        holder.latView.setText("" + LOCATION.latitude);
                        holder.longView.setText("" + LOCATION.longitude);


                        //depending on the status - display button accordingly
                        final Button butt = holder.acceptView;
                        if (LOCATION.status.equals("accepted") ) {
                            butt.setVisibility(View.GONE);
                            holder.accepted.setVisibility(View.VISIBLE);
                        }
                        if (LOCATION.status.equals("pending") ) {
                            butt.setVisibility(View.VISIBLE);
                            holder.accepted.setVisibility(View.INVISIBLE);
                        }
                        if (LOCATION.status.equals("reached")){
                            butt.setVisibility(View.GONE);
                            holder.accepted.setText("REACHED");
                            holder.accepted.setVisibility(View.VISIBLE);
                        }

                        butt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                locations.child(LOCATION.key).child("status").setValue("accepted");

                                locations.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Intent intent = new Intent(getBaseContext(), LocationCheckActivity.class);
                                        intent.putExtra("LAT", LOCATION.latitude.toString());
                                        intent.putExtra("LONG", LOCATION.longitude.toString());
                                        intent.putExtra("LOCKEY", LOCATION.key);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                butt.setText("ACCEPTED");
                                butt.setVisibility(View.GONE);
                                holder.accepted.setVisibility(View.VISIBLE);

                            }
                        });

                    }
                };

        mRecyclerView = (RecyclerView) findViewById(R.id.rvReceived);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class SentRequestsHolder extends RecyclerView.ViewHolder {

        public TextView userView;
        public TextView latView;
        public TextView accepted;
        public TextView longView;
        public Button acceptView;

        public SentRequestsHolder(View itemView) {
            super(itemView);
            userView = (TextView) itemView.findViewById(R.id.username);
            latView = (TextView) itemView.findViewById(R.id.latitude);
            longView = (TextView) itemView.findViewById(R.id.longitude);
            acceptView = (Button) itemView.findViewById(R.id.acceptor);
            accepted = (TextView)itemView.findViewById(R.id.accepted);

        }

    }
}