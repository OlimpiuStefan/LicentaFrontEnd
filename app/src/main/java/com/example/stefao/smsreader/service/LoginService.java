package com.example.stefao.smsreader.service;

import android.content.Context;
import android.content.Intent;

import com.example.stefao.smsreader.MainActivity;
import com.example.stefao.smsreader.utils.UserSessionManager;

/**
 * Created by stefao on 4/17/2018.
 */

public class LoginService {


    public void handleResponse(Context context, UserSessionManager session, String email, String password) {

        session.createUserLoginSession(email, password);

        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
