package com.referral.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AdminEditActivity extends AppCompatActivity {
    TextInputLayout adminKey,adminEarning,adminHold,adminTotal,adminUsed;
    DatabaseReference DBref;
    Button updateDetails;
    Integer reward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);
        getWindow().setStatusBarColor(ContextCompat.getColor(AdminEditActivity.this,R.color.yelight));


        /*------------------------------Hooks start---------------------------------------*/

        adminKey = findViewById(R.id.admin_key_edit);
        adminEarning = findViewById(R.id.admin_Earning_edit);
        adminTotal = findViewById(R.id.admin_total_edit);
        adminUsed = findViewById(R.id.admin_used_edit);
        adminHold = findViewById(R.id.admin_hold_edit);
        updateDetails = findViewById(R.id.btn_update_coupons_edit);

        /*------------------------------Hooks end---------------------------------------*/


        /*------------------------------Variables---------------------------------------*/

        DBref = FirebaseDatabase.getInstance().getReference("users");


        /*------------------------------Variables end---------------------------------------*/


        adminKey.getEditText().setText(getIntent().getStringExtra("key"));
        adminEarning.getEditText().setText(getIntent().getStringExtra("earning"));
        adminTotal.getEditText().setText(getIntent().getStringExtra("total"));
        adminUsed.getEditText().setText(getIntent().getStringExtra("used"));
        adminHold.getEditText().setText(getIntent().getStringExtra("hold"));



        getReward();


        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processupdate();
                startActivity(new Intent(AdminEditActivity.this,AdminSearchResultActivity.class).putExtra("phone",getIntent().getStringExtra("phone")));
                finish();
            }
        });
    }

    public void getReward(){

        DatabaseReference UPref =  FirebaseDatabase.getInstance().getReference("Admin");
        Query checkUser = UPref.child("reward");


        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                reward = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }


    public void processupdate(){

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        Boolean checkKey = intent.getStringExtra("key").equals(adminKey.getEditText().getText().toString());
        Boolean checkTotal = intent.getStringExtra("total").equals(adminTotal.getEditText().getText().toString());
        Boolean checkUsed = intent.getStringExtra("used").equals(adminUsed.getEditText().getText().toString());
        Boolean checkHold = intent.getStringExtra("hold").equals(adminHold.getEditText().getText().toString());
        Boolean checkEarning = intent.getStringExtra("earning").equals(adminEarning.getEditText().getText().toString());


        if(checkKey && checkTotal && checkUsed && checkHold && checkEarning){
            Toast.makeText(AdminEditActivity.this, "Nothing to update", Toast.LENGTH_SHORT).show();

            return;
        }else{

            DBref.child(phone).child("keyHelper").child("used").setValue(Integer.parseInt(adminUsed.getEditText().getText().toString()));
            DBref.child(phone).child("keyHelper").child("key").setValue(adminKey.getEditText().getText().toString());


            //DBref.child(phone).child("keyHelper").child("earning").setValue(Integer.parseInt(adminUsed.getEditText().getText().toString())*reward);
            DBref.child(phone).child("keyHelper").child("earning").setValue(Integer.parseInt(adminEarning.getEditText().getText().toString()));
            DBref.child(phone).child("keyHelper").child("onHold").setValue(Integer.parseInt(adminHold.getEditText().getText().toString()));
            DBref.child(phone).child("keyHelper").child("totalKeys").setValue(Integer.parseInt(adminTotal.getEditText().getText().toString()));


            Toast.makeText(AdminEditActivity.this, "Details Updated", Toast.LENGTH_SHORT).show();

        }



    }

}