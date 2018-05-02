package com.example.stefao.smsreader.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.stefao.smsreader.LoginActivity;

import java.util.HashMap;

/**
 * Created by stefao on 4/17/2018.
 */

public class UserSessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private static final String PREFER_NAME = "AndroidExamplePref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    //public static final String KEY_FIRST_NAME = "firstName";
    //public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    //public static final String KEY_ACCESS_TOKEN = "access_token";
    //public static final String KEY_REFRESH_TOKEN = "refresh_token";
    //private static final String KEY_ACCESS_TOKEN_EXPIRE_TIME = "expires_in";

    public UserSessionManager(Context context){
        // Shared pref mode
        int PRIVATE_MODE = 0;

        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    //Create login session
    public void createUserLoginSession(String email, String password){
        editor.putBoolean(IS_USER_LOGIN, true);
        //editor.putString(KEY_FIRST_NAME, firstName);
        //editor.putString(KEY_LAST_NAME, lastName);
        editor.putString(KEY_EMAIL, email);
        Log.e("==>>>",email);
        editor.putString(KEY_PASSWORD, password);
        //editor.putString(KEY_ACCESS_TOKEN, accessToken);
        //editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        //editor.putLong(KEY_ACCESS_TOKEN_EXPIRE_TIME, System.currentTimeMillis() / 1000L + expiresIn);

        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<>();

        // user name
        //user.put(KEY_FIRST_NAME, pref.getString(KEY_FIRST_NAME, null));
        //user.put(KEY_LAST_NAME, pref.getString(KEY_LAST_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        Log.e("==>>>>",pref.getString(KEY_EMAIL,null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user access token
        //user.put(KEY_ACCESS_TOKEN, pref.getString(KEY_ACCESS_TOKEN, null));
        //user.put(KEY_REFRESH_TOKEN, pref.getString(KEY_REFRESH_TOKEN, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    private boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

//    public boolean hasTokenExpired() {
//        long expireTime = pref.getLong(KEY_ACCESS_TOKEN_EXPIRE_TIME, 0L);
//        long systemTime = System.currentTimeMillis() / 1000L;
//
//        return expireTime != 0 && systemTime > expireTime;
//    }
}
