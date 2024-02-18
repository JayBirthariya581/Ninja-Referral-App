package com.referral.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;
    public static  final String VERSION = "version";
    public static  final String IS_LOGIN = "IsLoggedIn";
    public static  final String KEY_FIRSTNAME = "firstName";
    public static  final String KEY_lASTNAME = "lastName";
    public static  final String KEY_EMAIL = "email";
    public static  final String KEY_PHONENUMBER = "phoneNumber";
    public static  final String KEY_PASSWORD = "password";
    public static  final String KEY_Status = "false";
    public static  final String KeyHelper_earning = "earning";
    public static  final String KeyHelper_key = "key";
    public static  final String KeyHelper_onHold = "hold";
    public static  final String KeyHelper_used = "used";
    public static  final String KeyHelper_total = "total";


    public SessionManager(Context _context){
        context = _context;
        usersSession = _context.getSharedPreferences("usersloginSession",Context.MODE_PRIVATE);
        editor = usersSession.edit();

    }




    public void createLoginSession(String version,String fullName,String lastName,String email,String phone,String password,String keyStatus,KeyHelper keyHelper){
        editor.putBoolean(IS_LOGIN,true);

        /* Personal*/
        editor.putString(KEY_FIRSTNAME,fullName);
        editor.putString(KEY_lASTNAME,lastName);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PHONENUMBER,phone);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(VERSION,version);


        /* Ninja*/
        editor.putString(KEY_Status,keyStatus);
        editor.putString(KeyHelper_key,keyHelper.getKey());
        editor.putInt(KeyHelper_earning,keyHelper.getEarning());
        editor.putInt(KeyHelper_onHold,keyHelper.getOnHold());
        editor.putInt(KeyHelper_used,keyHelper.getUsed());
        editor.putInt(KeyHelper_total,keyHelper.getTotalKeys());


        editor.commit();
    }

    public HashMap<String,String> getUsersDetailsFromSessions(){
        HashMap<String,String> userData = new HashMap<String,String>();

        userData.put(KEY_FIRSTNAME,usersSession.getString(KEY_FIRSTNAME,null));
        userData.put(KEY_lASTNAME,usersSession.getString(KEY_lASTNAME,null));
        userData.put(KEY_EMAIL,usersSession.getString(KEY_EMAIL,null));
        userData.put(KEY_PHONENUMBER,usersSession.getString(KEY_PHONENUMBER,null));
        userData.put(KEY_PASSWORD,usersSession.getString(KEY_PASSWORD,null));
        userData.put(KEY_Status,usersSession.getString(KEY_Status,null));
        userData.put(VERSION,usersSession.getString(VERSION,null));


        userData.put(KeyHelper_key,usersSession.getString(KeyHelper_key,null));



        return  userData;
    }


    public HashMap<String,Integer> getNinjaDetailsFromSessions(){
        HashMap<String,Integer> ninjaData = new HashMap<String,Integer>();





        ninjaData.put(KeyHelper_earning,usersSession.getInt(KeyHelper_earning,0));
        ninjaData.put(KeyHelper_onHold,usersSession.getInt(KeyHelper_onHold,0));
        ninjaData.put(KeyHelper_used,usersSession.getInt(KeyHelper_used,0));
        ninjaData.put(KeyHelper_total,usersSession.getInt(KeyHelper_total,0));


        return  ninjaData;
    }



    public Boolean checkLogin(){
        if(usersSession.getBoolean(IS_LOGIN,false)){
                return true;
        }else {
            return false;
        }

    }

    public void logoutSession(){
        editor.clear();
        editor.commit();
    }


}
