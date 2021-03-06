package com.example.stefao.smsreader.view.feedback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.model.PoiDTO;
import com.example.stefao.smsreader.model.PoiRatingDTO;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.VolleyUtils;
import com.example.stefao.smsreader.view.settings.SettingsActivity;
import com.example.stefao.smsreader.view.utils.CustomOnItemSelectedListener;
import com.example.stefao.smsreader.viewmodel.feedback.FeedbackViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stefao on 5/23/2018.
 */

public class FeedbackActivity extends AppCompatActivity {
    private Spinner spinner1, spinner2;
    private Button btnSubmit;
    private Activity mActivity;
    private PoiDTO clickedPoi;
    FeedbackViewModel feedbackViewModel;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
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
        mActivity=this;
        clickedPoi = (PoiDTO)getIntent().getExtras().get("PoiDTO");
        addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
        feedbackViewModel = new FeedbackViewModel();
        mContext=this;
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
    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.spinnerFeedback2);

        List<String> list = new ArrayList<String>();
        list.add("bad");
        list.add("notbad");
        list.add("good");
        list.add("verygood");
        list.add("verybad");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinnerFeedback1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinnerFeedback1);
        spinner2 = (Spinner) findViewById(R.id.spinnerFeedback2);
        btnSubmit = (Button) findViewById(R.id.btnSubmitFeedback);
        final PoiRatingDTO poiRating = new PoiRatingDTO();
        spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String field = String.valueOf(spinner1.getSelectedItem());
                if (field.equals("quality")){
                    poiRating.setQuality(String.valueOf(spinner2.getSelectedItem()));
                }
                if (field.equals("price")){
                    poiRating.setPrice(String.valueOf(spinner2.getSelectedItem()));
                }
                if (field.equals("accesibility")){
                    poiRating.setAccesibility(String.valueOf(spinner2.getSelectedItem()));
                }
                if (field.equals("amability")){
                    poiRating.setAmability(String.valueOf(spinner2.getSelectedItem()));
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(mActivity,"Successfully added rating",
                        Toast.LENGTH_SHORT).show();
                Log.e("poi",poiRating.toString());
                Log.e("poi",clickedPoi.toString());
                feedbackViewModel.addPoiRatingToPoi(clickedPoi.getName(),mContext,poiRating.getPrice(),poiRating.getQuality(),poiRating.getAccesibility(),poiRating.getAmability());
                //addPoiRatingToPoi(clickedPoi.getName(),poiRating.getPrice(),poiRating.getQuality(),poiRating.getAccesibility(),poiRating.getAmability());
            }

        });
    }

    public void addPoiRatingToPoi(String name, String price, String quality, String accesibility, String amability) {

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        final JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("price", price);
            requestBody.put("quality", quality);
            requestBody.put("accesibility", accesibility);
            requestBody.put("amability", amability);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String URL = Constants.ADD_POI_RATING_TO_POI_URL+"/"+name;

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        {
                            Log.e("==>", response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.NO_CONNECTION, getApplicationContext());
                        } else {
                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                if (statusCode >= 500) {
                                    VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.SERVER_DOWN, getApplicationContext());
                                }
                            }
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
