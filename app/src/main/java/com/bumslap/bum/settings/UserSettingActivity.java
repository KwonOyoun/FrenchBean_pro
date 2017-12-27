package com.bumslap.bum.settings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumslap.bum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserSettingActivity extends AppCompatActivity {
    Typeface mTypeface;
    FirebaseAuth mAuth;
    TextView user_name;
    FirebaseAuth.AuthStateListener listener;
    FirebaseDatabase firebaseDatabase;
    TextView uid, gender, PNum, birth, SName;
    ArrayList<info> infos;
    String email;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        setTitle("설정");

        infos = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        user_name = findViewById(R.id.text_username);
        uid = findViewById(R.id.textView12);

        gender = (TextView)findViewById(R.id.textView17);
        PNum = (TextView)findViewById(R.id.textView18);
        birth = (TextView)findViewById(R.id.textView19);
        SName = (TextView)findViewById(R.id.textView20);
        String name = user.getDisplayName();
        email = user.getEmail();
        final String user_id = user.getUid();

        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        firebaseDatabase.getReference().child("signUpInfos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                infos.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    info Info = snapshot.getValue(info.class);
                    if(Info.user_Email.equals(email)) {
                        infos.add(Info);
                        user_name.setText(infos.get(0).user_Name);
                        uid.setText(infos.get(0).user_Email);
                        gender.setText(infos.get(0).user_Gender);
                        PNum.setText(infos.get(0).user_PhoneNumber);
                        birth.setText(infos.get(0).user_Birthday);
                        SName.setText(infos.get(0).user_StoreName);

                    }
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
