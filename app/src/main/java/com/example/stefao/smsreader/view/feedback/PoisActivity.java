package com.example.stefao.smsreader.view.feedback;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.view.categories.PoiAdapter;
import com.example.stefao.smsreader.view.feedback.FeedbackActivity;
import com.example.stefao.smsreader.view.settings.SettingsActivity;
import com.example.stefao.smsreader.view.utils.MultiSpinner;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.view.transactions.TransactionsActivity;
import com.example.stefao.smsreader.view.utils.Utility;
import com.example.stefao.smsreader.model.CategoryDTO;
import com.example.stefao.smsreader.model.PoiDTO;
import com.example.stefao.smsreader.application.PersistService;
import com.example.stefao.smsreader.utils.location.LocationUtils;
import com.example.stefao.smsreader.utils.location.UserLocation;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.example.stefao.smsreader.utils.VolleyUtils;
import com.example.stefao.smsreader.viewmodel.categories.CategoriesViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.LocationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

import static com.example.stefao.smsreader.utils.UserSessionManager.KEY_EMAIL;

public class PoisActivity extends AppCompatActivity  {


    JSONArray poisResponse = new JSONArray();
    UserSessionManager userSessionManager;
    PoiAdapter poiAdapter;
    ListView poiListView;
    Context mContext;
    Activity mActivity;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        mActivity = this;

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_pois);
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


        userSessionManager = new UserSessionManager(this);

        poiListView = (ListView) findViewById(R.id.pois_list);
        ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.poi_header, poiListView,false);
        poiListView.addHeaderView(headerView);
        getPoisForLoggedUser(Constants.BASE_URL + Constants.GET_POIS_FOR_LOGGED_USER_URL + userSessionManager.getUserDetails().get(KEY_EMAIL));
        Log.e("==>>>>", poisResponse.toString());
        ArrayList<PoiDTO> poiArray = new Gson().fromJson(poisResponse.toString(), new TypeToken<List<PoiDTO>>() {
        }.getType());
        poiAdapter = new PoiAdapter(this, poiArray);
        poiListView.setAdapter(poiAdapter);
        poiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiDTO item = (PoiDTO) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, FeedbackActivity.class);
                intent.putExtra("PoiDTO", item);
                startActivity(intent);
            }
        });


        ButterKnife.bind(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
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

    public void getPoisForLoggedUser(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Getting your visited POIs");
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("==>", response.toString());
                        poisResponse = response;
                        ArrayList<PoiDTO> poisArray = new Gson().fromJson(poisResponse.toString(), new TypeToken<List<PoiDTO>>() {
                        }.getType());
                        if (poisArray.size() == 0) {
                            poiListView.setVisibility(View.GONE);
                        }
                        poiAdapter.clear();
                        poiAdapter.addAll(poisArray);
                        poiAdapter.notifyDataSetChanged();
                        Utility.setListViewHeightBasedOnChildren(poiListView);
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //((TextView) findViewById(R.id.text_view_id)).setText("try again");
                        Log.e("==>", "EROAREEEEEE");
                        pDialog.hide();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        // SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
        queue.add(jsonArrayRequest);
        Log.e("==>", "DUPAAA");
    }

}
