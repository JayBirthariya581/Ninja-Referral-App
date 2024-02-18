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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

public class AdminSearchResultActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    DatabaseReference DBref;
    String phoneAdminSearch;
    Button updateCouponDetails;
    TextInputLayout adminName,adminPhone,adminEmail,adminPassword;//personal details
    TextInputLayout adminKey,adminEarning,adminHold,adminTotal,adminUsed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_result);
        getWindow().setStatusBarColor(ContextCompat.getColor(AdminSearchResultActivity.this,R.color.yelight));


        syncSessionManager();




        /*------------------------------Hooks start---------------------------------------*/
        drawerLayout = findViewById(R.id.drawer_admin_search_result);
        navigationView = findViewById(R.id.nav_admin_search_result);
        toolbar = findViewById(R.id.toolbar_admin_search_result);
        updateCouponDetails = findViewById(R.id.btn_update_coupons);
        adminName = findViewById(R.id.admin_FullName);
        adminEmail = findViewById(R.id.admin_email);
        adminPhone = findViewById(R.id.admin_phone);
        adminPassword = findViewById(R.id.admin_password);
        adminKey = findViewById(R.id.admin_key);
        adminEarning = findViewById(R.id.admin_Earning);
        adminTotal = findViewById(R.id.admin_total);
        adminUsed = findViewById(R.id.admin_used);
        adminHold = findViewById(R.id.admin_hold);
        /*------------------------------Hooks end---------------------------------------*/





        /*------------------------------Variables---------------------------------------*/
        phoneAdminSearch = getIntent().getStringExtra("phone");
        DBref = FirebaseDatabase.getInstance().getReference("users");
        /*------------------------------Variables Ends---------------------------------------*/








        /*------------------------------Navigation Part Starts---------------------------------------*/

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Details");
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AdminSearchResultActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(AdminSearchResultActivity.this);
        navigationView.setCheckedItem(R.id.nav_admin);
        navigationView.bringToFront();
        navigationView.requestLayout();


        /*------------------------------Navigation Part Ends---------------------------------------*/

        showUserDetails();

        updateCouponDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminSearchResultActivity.this,AdminEditActivity.class);
                intent.putExtra("key",adminKey.getEditText().getText().toString());
                intent.putExtra("earning",adminEarning.getEditText().getText().toString());
                intent.putExtra("total",adminTotal.getEditText().getText().toString());
                intent.putExtra("used",adminUsed.getEditText().getText().toString());
                intent.putExtra("hold",adminHold.getEditText().getText().toString());
                intent.putExtra("phone",adminPhone.getEditText().getText().toString());


                startActivity(intent);
                finish();

            }
        });




    }


    public void showUserDetails(){


        String phone = getIntent().getStringExtra("phone");



        Query checkUser = DBref.orderByChild("phone").equalTo(phone);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){


                        String usernameFromDB = snapshot.child(phone).child("userName").getValue(String.class);
                        String phoneFromDB = snapshot.child(phone).child("phone").getValue(String.class);
                        String fullNameFromDB = snapshot.child(phone).child("firstName").getValue(String.class)+" "+snapshot.child(phone).child("lastName").getValue(String.class);
                        String emailFromDB = snapshot.child(phone).child("email").getValue(String.class);
                        String keyStatusFromDB = snapshot.child(phone).child("keyStatus").getValue(String.class);
                        String passwordFromDB = snapshot.child(phone).child("password").getValue(String.class);


                        String keyFromDB = snapshot.child(phone).child("keyHelper").child("key").getValue(String.class);
                        String earningFromDB = snapshot.child(phone).child("keyHelper").child("earning").getValue(Integer.class).toString();
                        String onHoldFromDB = snapshot.child(phone).child("keyHelper").child("onHold").getValue(Integer.class).toString();
                        String usedFromDB = snapshot.child(phone).child("keyHelper").child("used").getValue(Integer.class).toString();
                        String totalFromDB = snapshot.child(phone).child("keyHelper").child("totalKeys").getValue(Integer.class).toString();


                        adminName.getEditText().setText(fullNameFromDB);
                        adminEmail.getEditText().setText(emailFromDB);
                        adminPhone.getEditText().setText(phoneFromDB);
                        adminPassword.getEditText().setText(passwordFromDB);
                        adminKey.getEditText().setText(keyFromDB);
                        adminEarning.getEditText().setText(earningFromDB);
                        adminHold.getEditText().setText(onHoldFromDB);
                        adminTotal.getEditText().setText(totalFromDB);
                        adminUsed.getEditText().setText(usedFromDB);






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
        SessionManager sessionManager = new SessionManager(AdminSearchResultActivity.this);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Double latestVersion = snapshot.getValue(Double.class);
                Double currentVersion = Double.parseDouble(sessionManager.getUsersDetailsFromSessions().get(sessionManager.VERSION));
                if(latestVersion>currentVersion){


                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminSearchResultActivity.this);
                    builder.setIcon(R.drawable.ic_ninja);
                    builder.setTitle("Ninja's | GearToCare");

                    builder.setMessage("Update Available \nVersion : " + latestVersion);

                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminSearchResultActivity.this);
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











    /*Navigation Panel*/

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
                startActivity(new Intent(AdminSearchResultActivity.this,NinjaActivity.class));
                finish();
                break;

            case R.id.nav_tutorial:
                startActivity(new Intent(AdminSearchResultActivity.this,TutorialActivity.class));
                break;

            case R.id.nav_admin:

                break;


            case R.id.nav_profile:
                startActivity(new Intent(AdminSearchResultActivity.this,UserProfileActivity.class));

                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                new SessionManager(AdminSearchResultActivity.this).logoutSession();

                startActivity(new Intent(AdminSearchResultActivity.this,LoginActivity.class));
                finish();

                break;

            case R.id.nav_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;

            case R.id.nav_rate:
                checkUpdatesbtn();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }



    public void syncSessionManager(){

        SessionManager sessionManager = new SessionManager(AdminSearchResultActivity.this);

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


                    SessionManager sessionManager = new SessionManager(AdminSearchResultActivity.this);



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



}

