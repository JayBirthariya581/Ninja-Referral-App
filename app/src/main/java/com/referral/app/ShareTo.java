package com.referral.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShareTo {
    Context context;
    String couponCode;
    SessionManager sessionManager;

    public ShareTo(Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);
        couponCode = sessionManager.getUsersDetailsFromSessions().get(sessionManager.KeyHelper_key);
    }





    public void shareToApp(String packageName){

        if(isPackageInstalled(packageName,context.getPackageManager())){
            Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            FirebaseDatabase.getInstance().getReference().child("AppStatus").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        String shareBody = "Hey, \nNow Two-Wheeler servicing is possible at home | office or at any place \n\nVisit : "+snapshot.child("webLink").getValue(String.class) + "\n\n"+ "And use coupon code : \""+couponCode+ "\" to get Rs."+snapshot.child("discount").getValue(String.class)+" OFF on your first vehicle servicing";


                        sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                        sendIntent.setPackage(packageName);

                        context.startActivity(Intent.createChooser(sendIntent, "Share via"));
                    }else{
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }else{
            try {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/details?id="+packageName));
                context.startActivity(viewIntent);
            }catch(Exception e) {
                Toast.makeText(context,"Unable to Connect Try Again...",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

    }



    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
