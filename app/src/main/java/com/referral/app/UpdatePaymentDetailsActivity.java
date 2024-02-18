package com.referral.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePaymentDetailsActivity extends AppCompatActivity {
    TextInputLayout accountMethod;
    Button update;
    ProgressDialog dialog;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_payment_details);
        dialog = new ProgressDialog(UpdatePaymentDetailsActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.setIcon(R.drawable.ic_ninja);
        accountMethod = findViewById(R.id.AccountDetailsL);
        update = findViewById(R.id.update_account);
        sessionManager = new SessionManager(UpdatePaymentDetailsActivity.this);
        getWindow().setStatusBarColor(ContextCompat.getColor(UpdatePaymentDetailsActivity.this,R.color.black));
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetails();
            }
        });





    }

    private void updateDetails() {
        dialog.show();
        if(accountMethod.getEditText().getText().toString().isEmpty()){
            accountMethod.setError("Invalid details");
            dialog.dismiss();
            return;
        }


        FirebaseDatabase.getInstance().getReference("users").child(sessionManager.getUsersDetailsFromSessions().get(SessionManager.KEY_PHONENUMBER))
                .child("Account").setValue(accountMethod.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    dialog.dismiss();
                    Intent i = new Intent(UpdatePaymentDetailsActivity.this,UserProfileActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }else{
                    dialog.dismiss();
                }

            }
        });




    }
}