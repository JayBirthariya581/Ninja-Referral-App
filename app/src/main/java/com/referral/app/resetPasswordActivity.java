package com.referral.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class resetPasswordActivity extends AppCompatActivity {

    Button resetPassword;
    String phone,password,confirmPassword;
    TextInputLayout passwordL,confirmPasswordL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        /*------------------------------Hooks start---------------------------------------*/
        passwordL = findViewById(R.id.passwordResetL);
        resetPassword = findViewById(R.id.ResetPasswordBtn);
        confirmPasswordL = findViewById(R.id.confirmPasswordResetL);
        /*------------------------------Hooks Ends---------------------------------------*/

        /*------------------------------Variables---------------------------------------*/
        phone = getIntent().getStringExtra("phone");
        /*------------------------------Variables---------------------------------------*/




        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!validatePassword()){
                    return;
                }


                DatabaseReference DBref = FirebaseDatabase.getInstance().getReference("users");


                if(passwordL.getEditText().getText().toString().equals(confirmPasswordL.getEditText().getText().toString())){
                    confirmPasswordL.setError(null);
                    DBref.child(phone).child("password").setValue(confirmPasswordL.getEditText().getText().toString());
                    syncSessionManager();
                    Toast.makeText(resetPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                    /*startActivity(new Intent(resetPasswordActivity.this,LoginActivity.class));*/
                    finish();

                }else {
                    confirmPasswordL.setError("Both password should be same");
                    
                    confirmPasswordL.requestFocus();

                }



            }
        });



    }


    public Boolean validatePassword(){
        String password = passwordL.getEditText().getText().toString();

        if(password.length()<5){
            passwordL.setError("atleast 5 characters");
            passwordL.requestFocus();
            return false;
        }else{
            passwordL.setError(null);
            return true;
        }

    }






    public void syncSessionManager(){

        SessionManager sessionManager = new SessionManager(resetPasswordActivity.this);

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


                    SessionManager sessionManager = new SessionManager(resetPasswordActivity.this);



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