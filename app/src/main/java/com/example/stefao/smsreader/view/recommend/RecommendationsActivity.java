package com.example.stefao.smsreader.view.recommend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.model.CategoryDTO;
import com.example.stefao.smsreader.model.PoiDTO;
import com.example.stefao.smsreader.model.TransactionDTO;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.example.stefao.smsreader.view.recommend.PoiFrequencyAdapter;
import com.example.stefao.smsreader.view.recommend.PoiRatingAdapter;
import com.example.stefao.smsreader.view.settings.SettingsActivity;
import com.example.stefao.smsreader.viewmodel.recommend.RecommendViewModel;
import com.example.stefao.smsreader.viewmodel.transactions.TransactionsViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.stefao.smsreader.utils.UserSessionManager.KEY_EMAIL;

/**
 * Created by stefao on 5/1/2018.
 */

public class RecommendationsActivity extends AppCompatActivity {
    JSONArray poiFrequencyResponse = new JSONArray();
    JSONArray poiRatingResponse = new JSONArray();
    UserSessionManager userSessionManager;
    PoiFrequencyAdapter poiFrequencyAdapter;
    PoiRatingAdapter poiRatingAdapter;
    ListView listViewPoiFrequency;
    ListView listViewPoiRating;
    Context mContext;
    RecommendViewModel recommendViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actity_recommendations);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.action_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


        recommendViewModel = new RecommendViewModel();




        userSessionManager = new UserSessionManager(this);
        listViewPoiFrequency = (ListView) findViewById(R.id.poi_frequency_list);
        ViewGroup headerViewFreqeuncy = (ViewGroup)getLayoutInflater().inflate(R.layout.poi_frequency_header, listViewPoiFrequency,false);
        listViewPoiFrequency.addHeaderView(headerViewFreqeuncy);
        ArrayList<PoiDTO> poiArray = new Gson().fromJson(poiFrequencyResponse.toString(), new TypeToken<List<PoiDTO>>(){}.getType());
        poiFrequencyAdapter = new PoiFrequencyAdapter(this,poiArray);
        listViewPoiFrequency.setAdapter(poiFrequencyAdapter);
        String selectedCategory = (String)getIntent().getExtras().get("subcategory");
        recommendViewModel.recommendByFrequency(selectedCategory,mContext,poiFrequencyAdapter);


        listViewPoiRating = (ListView) findViewById(R.id.poi_rating_list);
        ViewGroup headerViewRating = (ViewGroup)getLayoutInflater().inflate(R.layout.poi_rating_header, listViewPoiRating,false);
        listViewPoiRating.addHeaderView(headerViewRating);
        ArrayList<PoiDTO> poiArray2 = new Gson().fromJson(poiRatingResponse.toString(), new TypeToken<List<PoiDTO>>(){}.getType());
        poiRatingAdapter = new PoiRatingAdapter(this,poiArray2);
        listViewPoiRating.setAdapter(poiRatingAdapter);
        recommendViewModel.recommendByRating(selectedCategory,mContext,poiRatingAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
