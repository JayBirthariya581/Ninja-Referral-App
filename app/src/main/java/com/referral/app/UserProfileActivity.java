package com.referral.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SessionManager sessionManager;
    TextView profile_full_name,profile_user_key,earn_label,used_label;
    TextInputLayout profile_phone,profile_email,profile_FullName,account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        syncSessionManager();
        getWindow().setStatusBarColor(ContextCompat.getColor(UserProfileActivity.this,R.color.black));
        /*update = findViewById(R.id.update_user);*/


        /*------------------------------Hooks start---------------------------------------*/
        profile_full_name = findViewById(R.id.profile_full_name);
        profile_user_key = findViewById(R.id.profile_user_key);
        profile_FullName = findViewById(R.id.profile_FullName);
        profile_email = findViewById(R.id.profile_email);
        profile_phone = findViewById(R.id.profile_phone);
        used_label= findViewById(R.id.used_label);
        account = findViewById(R.id.AccountDetails);
        earn_label=findViewById(R.id.earn_label);
        drawerLayout = findViewById(R.id.drawer_layout_user_profile);
        navigationView = findViewById(R.id.nav_view_user_profile);
        toolbar = findViewById(R.id.toolbar_user_profile);
        /*------------------------------Hooks end---------------------------------------*/



        /*------------------------------Variables---------------------------------------*/
        sessionManager = new SessionManager(UserProfileActivity.this);
        /*------------------------------Variables Ends---------------------------------------*/




        findViewById(R.id.update_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(UserProfileActivity.this,UpdatePaymentDetailsActivity.class));

            }
        });


        showAllUSerData();



        /*------------------------------Navigation Part Starts---------------------------------------*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        menu.findItem(R.id.nav_admin).setVisible(false);
        menu.findItem(R.id.nav_tutorial).setVisible(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(UserProfileActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(UserProfileActivity.this);
        navigationView.setCheckedItem(R.id.nav_profile);
        navigationView.bringToFront();
        navigationView.requestLayout();
        /*------------------------------Navigation Part Ends---------------------------------------*/




        earn_label.setText(sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_earning).toString());
        used_label.setText(sessionManager.getNinjaDetailsFromSessions().get(sessionManager.KeyHelper_used).toString());

    }

    public void showAllUSerData(){



        profile_full_name.setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_FIRSTNAME)+" "+sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_lASTNAME));
        profile_email.getEditText().setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_EMAIL));
        profile_phone.getEditText().setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_PHONENUMBER));

        profile_FullName.getEditText().setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_FIRSTNAME)+" "+sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_lASTNAME));
        profile_user_key.setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KeyHelper_key));

        FirebaseDatabase.getInstance().getReference("users").child(sessionManager.getUsersDetailsFromSessions().get(SessionManager.KEY_PHONENUMBER)).child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    account.getEditText().setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }






    /*private boolean isNameChanged() {
         if(!profile_FullName.getEditText().toString().equals(fullname)){
             DBref.child(phone).child("fullName").setValue(profile_FullName.getEditText().getText().toString());
             sessionManager.editor.putString(sessionManager.KEY_FULLNAME,profile_FullName.getEditText().getText().toString());
             sessionManager.editor.apply();
             return true;
         }else
         {
             return  false;
         }
    }


    private boolean isEmailChanged() {
         if(!profile_email.getEditText().toString().equals(email)){
             DBref.child(phone).child("email").setValue(profile_email.getEditText().getText().toString());
             sessionManager.editor.putString(sessionManager.KEY_EMAIL,profile_email.getEditText().getText().toString());
             sessionManager.editor.apply();
             return true;
         }else
         {
             return  false;
         }
    }



    private boolean isPhoneChanged() {
         if(!profile_phone.getEditText().toString().equals(phone)){
             phone=profile_phone.getEditText().toString();
             DBref.child(phone).child("phone").setValue(profile_phone.getEditText().getText().toString());
             sessionManager.editor.putString(sessionManager.KEY_PHONENUMBER,profile_phone.getEditText().getText().toString());
             sessionManager.editor.apply();

            *//* UserHelperClass UserHelper =  new UserHelperClass(profile_user_name.getText().toString(),
                     profile_FullName.getEditText().getText().toString(),
                     profile_email.getEditText().getText().toString(),
                     profile_phone.getEditText().getText().toString(),
                     profile_password.getEditText().getText().toString()
             );

             DBref.child(profile_phone.getEditText().toString()).setValue(UserHelper);*//*


             return true;
         }else
         {
             return  false;
         }
    }


    private boolean isPasswordChanged() {
         if(!profile_password.getEditText().toString().equals(password)){
             DBref.child(phone).child("password").setValue(profile_password.getEditText().getText().toString());
             sessionManager.editor.putString(sessionManager.KEY_PASSWORD,profile_password.getEditText().getText().toString());
             sessionManager.editor.apply();
             return true;
         }else
         {
             return  false;
         }
    }*/



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_ninja:
                startActivity(new Intent(UserProfileActivity.this,NinjaActivity.class));
                finish();
                break;

            case R.id.nav_tutorial:
                startActivity(new Intent(UserProfileActivity.this, TutorialActivity.class));
                break;
            case R.id.nav_profile:

                break;

            case R.id.nav_admin:
                startActivity(new Intent(UserProfileActivity.this,AdminSearchActivity.class));
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                new SessionManager(UserProfileActivity.this).logoutSession();

                startActivity(new Intent(UserProfileActivity.this,LoginActivity.class));
                finish();

                break;

            case R.id.nav_share:
                FirebaseDatabase.getInstance().getReference("AppStatus").child("download").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Download GearToCare | Ninja's app and earn credit upto Rs 20,000/- \n\nLink : "+snapshot.getValue(String.class);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;

            case R.id.nav_rate:
                checkUpdatesbtn();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

}



    public void syncSessionManager(){

        SessionManager sessionManager = new SessionManager(UserProfileActivity.this);

        DatabaseReference DBref = FirebaseDatabase.getInstance().getReference("users");

        String phone = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_PHONENUMBER);



        Query checkUser = DBref.orderByChild("phone").equalTo(phone);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
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


                    SessionManager sessionManager = new SessionManager(UserProfileActivity.this);



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

                    sessionManager.editor.commit();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });





    }

    public void checkUpdatesbtn(){

        DatabaseReference UPref =  FirebaseDatabase.getInstance().getReference("AppStatus");
        Query checkUser = UPref.child("LatestVersion");
        SessionManager sessionManager = new SessionManager(UserProfileActivity.this);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Double latestVersion = snapshot.getValue(Double.class);
                Double currentVersion = Double.parseDouble(sessionManager.getUsersDetailsFromSessions().get(sessionManager.VERSION));
                if(latestVersion>currentVersion){


                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                    builder.setIcon(R.drawable.ic_ninja);
                    builder.setTitle("Ninja's | GearToCare");

                    builder.setMessage("Update Available \nVersion : " + latestVersion);

                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference("AppStatus").child("updateLink").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(snapshot.getValue(String.class)));
                                    startActivity(browserIntent);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });

                    builder.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                    builder.setIcon(R.drawable.ic_ninja);
                    builder.setTitle("Ninja's | GearToCare");

                    builder.setMessage("You are using the latest version");

                    builder.show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




    }


}