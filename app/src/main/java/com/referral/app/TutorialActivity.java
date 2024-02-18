package com.referral.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.ui.PlayerView;

import com.google.android.exoplayer2.util.Util;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class TutorialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    PlayerView playerView;
    SimpleExoPlayer player;
    ProgressBar progressBar;
    ImageView btnFullscreen;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        getWindow().setStatusBarColor(ContextCompat.getColor(TutorialActivity.this,R.color.yelight));


/*------------------------------Hooks start---------------------------------------*/

        drawerLayout = findViewById(R.id.drawer_layout_dash);
        navigationView = findViewById(R.id.nav_view_dash);
        toolbar = findViewById(R.id.toolbar_dash);



/*------------------------------Hooks end---------------------------------------*/




/*------------------------------Navigation Part Starts---------------------------------------*/

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tutorials");
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(TutorialActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(TutorialActivity.this);
        navigationView.setCheckedItem(R.id.nav_tutorial);
        navigationView.bringToFront();
        navigationView.requestLayout();


/*------------------------------Navigation Part Ends---------------------------------------*/



/*------------------------------Exo Player Start---------------------------------------*/

        playerView = findViewById(R.id.video_view);
        progressBar = findViewById(R.id.progress_bar);
        btnFullscreen = playerView.findViewById(R.id.bt_fullscreen);







/*------------------------------Exo Player End---------------------------------------*/




    }


    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri("https://imgur.com/7bMqysJ.mp4");
        player.setMediaItem(mediaItem);

        playerView.setKeepScreenOn(true);

        player.prepare();
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);






        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playbackState == Player.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                }else if(playbackState == Player.STATE_READY){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag){
                    btnFullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
                    toolbar.setVisibility(View.VISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    flag=false;
                }else {

                    btnFullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    toolbar.setVisibility(View.GONE);

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    flag=true;
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
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
                startActivity(new Intent(TutorialActivity.this,NinjaActivity.class));
                finish();
                break;

            case R.id.nav_tutorial:

                break;

            case R.id.nav_admin:
                startActivity(new Intent(TutorialActivity.this,AdminSearchActivity.class));
                finish();
                break;


            case R.id.nav_profile:
                startActivity(new Intent(TutorialActivity.this,UserProfileActivity.class));

                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                new SessionManager(TutorialActivity.this).logoutSession();

                startActivity(new Intent(TutorialActivity.this,LoginActivity.class));
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
        SessionManager sessionManager = new SessionManager(TutorialActivity.this);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Double latestVersion = snapshot.getValue(Double.class);
                Double currentVersion = Double.parseDouble(sessionManager.getUsersDetailsFromSessions().get(sessionManager.VERSION));
                if(latestVersion>currentVersion){


                    AlertDialog.Builder builder = new AlertDialog.Builder(TutorialActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(TutorialActivity.this);
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