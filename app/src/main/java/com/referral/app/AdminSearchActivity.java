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

public class AdminSearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextInputLayout adminSearchPhoneL;
    Button adminSearch;
    String phoneAdminSearch;
    DatabaseReference DBref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search);
        getWindow().setStatusBarColor(ContextCompat.getColor(AdminSearchActivity.this,R.color.yelight));




        /*------------------------------Hooks start---------------------------------------*/

        drawerLayout = findViewById(R.id.drawer_admin_search);
        navigationView = findViewById(R.id.nav_admin_search);
        toolbar = findViewById(R.id.toolbar_admin_search);
        adminSearch = findViewById(R.id.btn_admin_search);
        adminSearchPhoneL = findViewById(R.id.admin_search_phone);



        /*------------------------------Hooks end---------------------------------------*/


        /*------------------------------Variables---------------------------------------*/
        phoneAdminSearch = adminSearchPhoneL.getEditText().getText().toString();
        DBref = FirebaseDatabase.getInstance().getReference("users");

        /*------------------------------Variables Ends---------------------------------------*/

        /*------------------------------Navigation Part Starts---------------------------------------*/

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin");
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AdminSearchActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(AdminSearchActivity.this);
        navigationView.setCheckedItem(R.id.nav_admin_search);
        navigationView.bringToFront();
        navigationView.requestLayout();


        /*------------------------------Navigation Part Ends---------------------------------------*/



        adminSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUser();
            }
        });




    }



    public void searchUser(){
        String phone = adminSearchPhoneL.getEditText().getText().toString();
        Query checkUser = DBref.orderByChild("phone").equalTo(phone);



        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    adminSearchPhoneL.setError(null);
                    adminSearchPhoneL.setErrorEnabled(false);

                Intent intent =  new Intent(AdminSearchActivity.this,AdminSearchResultActivity.class);
                intent.putExtra("phone",phone);

                startActivity(intent);


                }else{
                    adminSearchPhoneL.setError("No such user exist");
                    adminSearchPhoneL.requestFocus();


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
                startActivity(new Intent(AdminSearchActivity.this,NinjaActivity.class));
                finish();
                break;

            case R.id.nav_tutorial:
                startActivity(new Intent(AdminSearchActivity.this,TutorialActivity.class));
                break;

            case R.id.nav_admin:

                break;


            case R.id.nav_profile:
                startActivity(new Intent(AdminSearchActivity.this,UserProfileActivity.class));

                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                new SessionManager(AdminSearchActivity.this).logoutSession();

                startActivity(new Intent(AdminSearchActivity.this,LoginActivity.class));
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


    public void checkUpdatesbtn(){

        DatabaseReference UPref =  FirebaseDatabase.getInstance().getReference("AppStatus");
        Query checkUser = UPref.child("LatestVersion");
        SessionManager sessionManager = new SessionManager(AdminSearchActivity.this);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Double latestVersion = snapshot.getValue(Double.class);
                Double currentVersion = Double.parseDouble(sessionManager.getUsersDetailsFromSessions().get(sessionManager.VERSION));
                if(latestVersion>currentVersion){


                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminSearchActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminSearchActivity.this);
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

