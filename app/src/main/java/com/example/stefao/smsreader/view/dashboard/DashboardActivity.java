package com.example.stefao.smsreader.view.dashboard;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.view.feedback.PoisActivity;
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
    android.support.design.widget.CollapsingToolbarLayout toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        toolbar = findViewById(R.id.collapsing_toolbar);
        toolbar.setContentScrimColor(Color.parseColor("#363232"));
        toolbar.setBackgroundColor(Color.parseColor("#363232"));
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

    @OnClick(R.id.poiCardView)
    public void goToPoisActivity() {
        Intent i = new Intent(getApplicationContext(), PoisActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnLogout)
    public void logoutUser() {
        session.logoutUser();
    }

}
