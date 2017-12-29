package com.bumslap.bum.settings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumslap.bum.POSproject.MainActivity;
import com.bumslap.bum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.auth.ISessionCallback;

import java.util.ArrayList;

public class UserSettingActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView user_name;
    FirebaseAuth.AuthStateListener listener;
    FirebaseDatabase firebaseDatabase;
    TextView user_email, user_gender, user_PNum, user_birth, user_SName;
    ArrayList<info> infos;
    String email;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        setTitle("설정");

        infos = new ArrayList<>();



        출처: http://superwony.tistory.com/12 [개발자 키우기]
        if (FirebaseAuth.getInstance().equals(null)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            mAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();

            user_name = findViewById(R.id.textView12);

            user_email = (TextView) findViewById(R.id.textView17);
            user_gender = (TextView) findViewById(R.id.textView18);
            user_PNum = (TextView) findViewById(R.id.textView19);
            user_birth = (TextView) findViewById(R.id.textView20);
            user_SName = (TextView) findViewById(R.id.textView21);

            String name = user.getDisplayName();
            email = user.getEmail();
            final String user_id = user.getUid();

            progressBar = (ProgressBar) findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.VISIBLE);

            firebaseDatabase.getReference().child("signUpInfos").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    infos.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        info Info = snapshot.getValue(info.class);
                        if (Info.user_Email.equals(email)) {
                            infos.add(Info);
                            user_name.setText(infos.get(0).user_Name);
                            user_email.setText(infos.get(0).user_Email);
                            user_gender.setText(infos.get(0).user_Gender);
                            user_PNum.setText(infos.get(0).user_PhoneNumber);
                            user_birth.setText(infos.get(0).user_Birthday);
                            user_SName.setText(infos.get(0).user_StoreName);

                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {


               Toast.makeText(UserSettingActivity.this,"카카오톡 로그인은 정보조회가 불가능합니다.",Toast.LENGTH_SHORT).show();

            }




        }
    }


