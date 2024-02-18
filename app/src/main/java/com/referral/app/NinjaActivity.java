package com.referral.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class NinjaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference DBref;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    AppCompatButton generate,earnings,shareMore;
    ImageButton shareWhatsapp,shareInsta,shareFace;

    SessionManager sessionManager;
    HashMap<String,String> userData;
    HashMap<String,Integer> NinjaData;
    MaterialCardView cdKey;
    TextView ninjaKey;
    TextView fullName;
    ImageButton send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninja);

        getWindow().setStatusBarColor(ContextCompat.getColor(NinjaActivity.this,R.color.black));
        syncSessionManager();
        checkUpdates();



         /*------------------------------Hooks start---------------------------------------*/
            drawerLayout = findViewById(R.id.drawer_layout_ninja);
            navigationView = findViewById(R.id.nav_view_ninja);
            toolbar = findViewById(R.id.toolbar_ninja);

            generate = findViewById(R.id.generate);
            earnings = findViewById(R.id.earnings);
            ninjaKey = findViewById(R.id.Ninja_key);

            cdKey = findViewById(R.id.cdkey);
            fullName = findViewById(R.id.profile_full_name);
            shareWhatsapp = findViewById(R.id.shareWhatsapp);
            shareInsta = findViewById(R.id.shareInsta);
            shareFace = findViewById(R.id.shareFaceBook);
            shareMore = findViewById(R.id.shareMore);

            send = findViewById(R.id.send);


        /*------------------------------Hooks end---------------------------------------*/






        /*------------------------------Variables---------------------------------------*/
            DBref= FirebaseDatabase.getInstance().getReference("users");
            sessionManager = new SessionManager(NinjaActivity.this);
            userData = sessionManager.getUsersDetailsFromSessions();
            NinjaData = sessionManager.getNinjaDetailsFromSessions();


        /*------------------------------Variables end---------------------------------------*/




        /*------------------------------Navigation Part Starts---------------------------------------*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        menu.findItem(R.id.nav_admin).setVisible(false);
        menu.findItem(R.id.nav_tutorial).setVisible(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(NinjaActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(NinjaActivity.this);
        navigationView.setCheckedItem(R.id.nav_ninja);
        navigationView.bringToFront();
        navigationView.requestLayout();

        /*------------------------------Navigation Part Ends---------------------------------------*/

        cdKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KeyHelper_key);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Key", shareBody);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(NinjaActivity.this, "Coupon copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        shareWhatsapp.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                ShareTo shareTo = new ShareTo(NinjaActivity.this);

                shareTo.shareToApp("com.whatsapp");

            }
        });


        shareInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareTo shareTo = new ShareTo(NinjaActivity.this);

                shareTo.shareToApp("com.instagram.android");

            }
        });


        shareFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareTo shareTo = new ShareTo(NinjaActivity.this);

                shareTo.shareToApp("com.facebook.katana");

            }
        });

        shareMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String couponCode = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KeyHelper_key);


                FirebaseDatabase.getInstance().getReference().child("AppStatus").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            String shareBody = "Hey, \nNow Two-Wheeler servicing is possible at home | office or at any place \n\nVisit : "+snapshot.child("webLink").getValue(String.class) + "\n\n"+ "And use coupon code : \""+couponCode+ "\" to get Rs."+snapshot.child("discount").getValue(String.class)+" OFF on your first vehicle servicing";




                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ninjas");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        }else{
                            Toast.makeText(NinjaActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });



        String name = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_FIRSTNAME);
        String phone = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_PHONENUMBER);
        String key = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KeyHelper_key);
        String keystatus = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_Status);





        fullName.setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_FIRSTNAME)+" "+sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_lASTNAME));

        if(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_Status).equals("false")){
            ninjaKey.setVisibility(View.GONE);

            cdKey.setVisibility(View.GONE);
        }else{
            ninjaKey.setText(key);

            generate.setVisibility(View.GONE);


        }






        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(keystatus.equals("false")){
                    FirebaseDatabase.getInstance().getReference("Admin").child("couponList").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {







                                  DBref.child(phone).child("keyHelper").child("key").setValue("GTC"+String.valueOf((snapshot.getChildrenCount()+1)+3)+"NIN"+String.valueOf(snapshot.getChildrenCount()+1))
                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {
                                                  if(task.isSuccessful()){

                                                      FirebaseDatabase.getInstance().getReference("Admin").child("couponList").child(sessionManager.getUsersDetailsFromSessions().get(SessionManager.KEY_PHONENUMBER))
                                                              .setValue("GTC"+String.valueOf((snapshot.getChildrenCount()+1)+3)+"NIN"+String.valueOf(snapshot.getChildrenCount()+1));

                                                      sessionManager.editor.putString(sessionManager.KeyHelper_key,"GTC"+String.valueOf((snapshot.getChildrenCount()+1)+3)+"NIN"+String.valueOf(snapshot.getChildrenCount()+1));
                                                      sessionManager.editor.putString(sessionManager.KEY_Status,"true");
                                                      sessionManager.editor.commit();
                                                      ninjaKey.setVisibility(View.VISIBLE);
                                                      userData.put(sessionManager.KEY_Status,"true");
                                                      ninjaKey.setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KeyHelper_key));

                                                      cdKey.setVisibility(View.VISIBLE);
                                                      generate.setVisibility(View.GONE);

                                                      DBref.child(phone).child("keyStatus").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<Void> task) {
                                                              if(task.isSuccessful()){
                                                                  Toast.makeText(NinjaActivity.this, "Keys generated successfully", Toast.LENGTH_SHORT).show();
                                                              }
                                                          }
                                                      });

                                                      //Toast.makeText(NinjaActivity.this, "Keys generated successfully", Toast.LENGTH_SHORT).show();


                                                  }
                                              }
                                          });














                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                }else{
                    Toast.makeText(NinjaActivity.this, "Keys are already generated", Toast.LENGTH_SHORT).show();


                }
            }
        });




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String couponCode = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KeyHelper_key);


                FirebaseDatabase.getInstance().getReference().child("AppStatus").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            String shareBody = "Hey, \nNow Two-Wheeler servicing is possible at home | office or at any place \n\nVisit : "+snapshot.child("webLink").getValue(String.class) + "\n\n"+ "And use coupon code : \""+couponCode+ "\" to get Rs."+snapshot.child("discount").getValue(String.class)+" OFF on your first vehicle servicing";




                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ninjas");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        }else{
                            Toast.makeText(NinjaActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






            }
        });


        earnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NinjaActivity.this,EarningActivity.class)

                );
            }
        });

    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public void syncSessionManager(){

        SessionManager sessionManager = new SessionManager(NinjaActivity.this);

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


                    SessionManager sessionManager = new SessionManager(NinjaActivity.this);



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

    public void checkUpdates(){
        DatabaseReference UPref =  FirebaseDatabase.getInstance().getReference("AppStatus");
        Query checkUser = UPref.child("LatestVersion");


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Double latestVersion = snapshot.getValue(Double.class);

                if(latestVersion>Double.parseDouble(sessionManager.getUsersDetailsFromSessions().get(sessionManager.VERSION))){


                    AlertDialog.Builder builder = new AlertDialog.Builder(NinjaActivity.this);
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

                }
                sessionManager.editor.putString(sessionManager.VERSION,latestVersion.toString());


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




    }

    public void checkUpdatesbtn(){

        DatabaseReference UPref =  FirebaseDatabase.getInstance().getReference("AppStatus");
        Query checkUser = UPref.child("LatestVersion");


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Double latestVersion = snapshot.getValue(Double.class);

                if(latestVersion>Double.parseDouble(sessionManager.getUsersDetailsFromSessions().get(sessionManager.VERSION))){


                    AlertDialog.Builder builder = new AlertDialog.Builder(NinjaActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(NinjaActivity.this);
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
                break;

            case R.id.nav_tutorial:
                startActivity(new Intent(NinjaActivity.this,TutorialActivity.class));
                break;

            case R.id.nav_admin:
                startActivity(new Intent(NinjaActivity.this,AdminSearchActivity.class));

                break;


            case R.id.nav_profile:
                startActivity(new Intent(NinjaActivity.this,UserProfileActivity.class));


                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                sessionManager.logoutSession();
                startActivity(new Intent(NinjaActivity.this,LoginActivity.class));
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
}