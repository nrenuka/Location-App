package com.example.android.locationreached;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

    }

    public void onClickLogin(View view){
        EditText usernameView = (EditText) findViewById(R.id.username);
        String  username = usernameView.getText().toString();

        if (username.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_SHORT).show();
        }
        else {
            UserExistence(username);
        }
    }

    /**+
     * Checks if username exists in database - if so, opens that account, if the database
     * does't have it, it creates a new user
     *
     * moves user key and username to the next activity
     * @param usernameLogin
     */
    public void UserExistence(final String usernameLogin){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("users");

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean userExists = false;
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                String key = "";

                while (it.hasNext()) {
                    DataSnapshot user = it.next();
                    if (user.getValue(User.class).username.equalsIgnoreCase(usernameLogin)) {
                        userExists = true;
                        key = user.getKey();
                        break;
                    }
                }

                if (!userExists) {
                    User user = new User(usernameLogin);
                    DatabaseReference newUser = users.push();
                    newUser.setValue(user);
                    key = newUser.getKey();
                }

                Intent intent = new Intent(getBaseContext(), HomeScreenActivity.class);
                intent.putExtra("USERKEY", key);
                intent.putExtra("USERNAME", usernameLogin);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DATABASE", "not connected");
            }
        });
    }

}
