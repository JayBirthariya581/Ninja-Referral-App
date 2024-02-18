package com.referral.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EarningActivity extends AppCompatActivity {
    MaterialCardView cdEarning,cdHold,cdUsed;
    SessionManager sessionManager;
    HashMap<String,String> userData;
    DatabaseReference DBref;
    TextView used,earning,onHold;
    HashMap<String,Integer> NinjaData;
    TextView fullName;
    TextView t;

    ImageButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning);
        getWindow().setStatusBarColor(ContextCompat.getColor(EarningActivity.this,R.color.black));
        syncSessionManager();


        cdEarning = findViewById(R.id.cdEarning);
        cdHold = findViewById(R.id.cdHold);
        cdUsed = findViewById(R.id.cdUsed);


        /*------------------------------Variables---------------------------------------*/
        DBref= FirebaseDatabase.getInstance().getReference("users");
        sessionManager = new SessionManager(EarningActivity.this);
        userData = sessionManager.getUsersDetailsFromSessions();
        NinjaData = sessionManager.getNinjaDetailsFromSessions();
        refresh = findViewById(R.id.ref);
        used=findViewById(R.id.used_ninja);
        earning=findViewById(R.id.earning_ninja);
        onHold=findViewById(R.id.onHold_ninja);
        fullName = findViewById(R.id.profile_full_name);

        t=findViewById(R.id.payment_label);
        /*------------------------------Variables end---------------------------------------*/


        FirebaseDatabase.getInstance().getReference("users").child(sessionManager.getUsersDetailsFromSessions().get(SessionManager.KEY_PHONENUMBER)).child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    t.setText("Payment Method : \n"+snapshot.getValue(String.class));
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String name = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_FIRSTNAME);
        String phone = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_PHONENUMBER);
        String key = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KeyHelper_key);
        String keystatus = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_Status);
        String earningString  = sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_earning).toString();
        String usedString  = sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_used).toString();
        String holdString  =sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_onHold).toString();

        fullName.setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_FIRSTNAME)+" "+sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_lASTNAME));
        earning.setText("₹ "+sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_earning).toString()+"/-");
        used.setText(sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_used).toString());
        onHold.setText(sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_onHold).toString());

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncSessionManager();
                earning.setText("₹ "+sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_earning).toString()+"/-");
                used.setText(sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_used).toString());
                onHold.setText(sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_onHold).toString());
            }
        });

        findViewById(R.id.update_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EarningActivity.this,UpdatePaymentDetailsActivity.class));

            }
        });

    }


    public void syncSessionManager(){

        SessionManager sessionManager = new SessionManager(EarningActivity.this);

        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference("users");

        String phone = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_PHONENUMBER);



        Query checkUser = DBref.orderByChild("phone").equalTo(phone);

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){


                    String passwordFromDB = snapshot.child(phone).child("password").getValue(String.class);

                    String lastNameFromDB = snapshot.child(phone).child("lastName").getValue(String.class);
                    String phoneFromDB = snapshot.child(phone).child("phone").getValue(String.class);
                    String firstNameFromDB = snapshot.child(phone).child("firstName").getValue(String.class);
                    String emailFromDB = snapshot.child(phone).child("email").getValue(String.class);
                    String keyStatusFromDB = snapshot.child(phone).child("keyStatus").getValue(String.class);

                    String keyFromDB = snapshot.child(phone).child("keyHelper").child("key").getValue(String.class);
                    Integer earningFromDB = snapshot.child(phone).child("keyHelper").child("earning").getValue(Integer.class);
                    Integer onHoldFromDB = snapshot.child(phone).child("keyHelper").child("onHold").getValue(Integer.class);
                    Integer usedFromDB = snapshot.child(phone).child("keyHelper").child("used").getValue(Integer.class);
                    Integer totalFromDB = snapshot.child(phone).child("keyHelper").child("totalKeys").getValue(Integer.class);


                    SessionManager sessionManager = new SessionManager(EarningActivity.this);



                    /*Sync Personal Details*/
                    sessionManager.editor.putString(sessionManager.KEY_FIRSTNAME,firstNameFromDB);
                    sessionManager.editor.putString(sessionManager.KEY_EMAIL,emailFromDB);
                    sessionManager.editor.putString(sessionManager.KEY_lASTNAME,lastNameFromDB);
                    sessionManager.editor.putString(sessionManager.KEY_PHONENUMBER,phoneFromDB);
                    sessionManager.editor.putString(sessionManager.KEY_Status,keyStatusFromDB);
                    sessionManager.editor.putString(sessionManager.KEY_PASSWORD,passwordFromDB);

                    /*Sync Ninja Details*/
                    sessionManager.editor.putString(sessionManager.KeyHelper_key,keyFromDB);
                    sessionManager.editor.putInt(sessionManager.KeyHelper_earning,earningFromDB);
                    sessionManager.editor.putInt(sessionManager.KeyHelper_total,totalFromDB);
                    sessionManager.editor.putInt(sessionManager.KeyHelper_used,usedFromDB);
                    sessionManager.editor.putInt(sessionManager.KeyHelper_onHold,onHoldFromDB);
                    sessionManager.editor.apply();
                    sessionManager.editor.commit();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });





    }
}