package com.example.stefao.smsreader.view.dashboard;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.view.recommend.RecommendActivity;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.example.stefao.smsreader.view.categories.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by stefao on 5/28/2018.
 */

public class DashboardActivity extends AppCompatActivity {
    private UserSessionManager session;
    private ProgressDialog pDialog;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        session = new UserSessionManager(getApplicationContext());
        ButterKnife.bind(this);
    }



    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick(R.id.categoriesCardView)
    public void goToGalleriesActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.recommendCardView)
    public void goToRecommendActivity() {
        Intent i = new Intent(getApplicationContext(), RecommendActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnLogout)
    public void logoutUser() {
        session.logoutUser();
    }

}
