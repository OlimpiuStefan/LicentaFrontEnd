package com.example.stefao.smsreader;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.Entities.CategoryDTO;
import com.example.stefao.smsreader.application.PersistService;
import com.example.stefao.smsreader.location.fetcher.LocationUtils;
import com.example.stefao.smsreader.location.fetcher.UserLocation;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.stefao.smsreader.utils.UserSessionManager.KEY_EMAIL;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    private int PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;
    LocationUtils locationUtils;
    UserLocation userLocation;
    JSONArray categoriesResponse = new JSONArray();
    UserSessionManager userSessionManager;
    CategoryAdapter categoryAdapter;
    ListView listview;
    Context mContext;
    private PersistService persistService;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.e("==>>>>>>>>",bundle.getString("userLocationLongitude"));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
        super.onCreate(savedInstanceState);
        //locationUtils = new LocationUtils(this, this);
        //userLocation = new UserLocation();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.e("==>", messageText);
//                userLocation.setLocationSetted(false);
//                getLocation();
//            }
//        });
//
//        final Button button = findViewById(R.id.button_id);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                //getData(v);
//            }
//        });

        userSessionManager = new UserSessionManager(this);
        listview = (ListView) findViewById(R.id.categories_list);
        Log.e("==>",userSessionManager.getUserDetails().get(KEY_EMAIL));
        getCategories(Constants.BASE_URL+Constants.GET_CATEGORIES_URL+userSessionManager.getUserDetails().get(KEY_EMAIL));
        Log.e("==>>>>",categoriesResponse.toString());
        ArrayList<CategoryDTO> categoriesArray = new Gson().fromJson(categoriesResponse.toString(), new TypeToken<List<CategoryDTO>>(){}.getType());
        categoryAdapter = new CategoryAdapter(this,categoriesArray);
        listview.setAdapter(categoryAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryDTO item = (CategoryDTO)parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext,TransactionsActivity.class);
                intent.putExtra("CategoryDTO",item);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, PersistService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        PersistService.MyBinder b = (PersistService.MyBinder) binder;
        persistService = b.getService();
        Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        persistService = null;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
            Log.i("TAG", "MY_PERMISSIONS_REQUEST_SMS_RECEIVE --> YES");
        }
    }

//    public void getLocation() {
//
////        Double latitude = locationUtils.getLocation().getLatitude();
////        Double longitude = locationUtils.getLocation().getLongitude();
////        Log.e("==>", latitude.toString());
////        Log.e("==>", longitude.toString());
////        ((TextView) findViewById(R.id.text_view_id)).setText(latitude.toString());
////        Toast.makeText(getApplicationContext(), latitude.toString(), Toast.LENGTH_LONG).show();
//
//
//        locationUtils.getLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                // Got last known location. In some rare situations this can be null.
//                if (location != null) {
//                    Double currentLatitude = location.getLatitude();
//                    Double currentLongitude = location.getLongitude();
//                    userLocation.setLatitude(location.getLatitude());
//                    userLocation.setLongitude(location.getLongitude());
//                    userLocation.setLocationSetted(true);
//                    Log.e("==>", currentLatitude.toString());
//                    Log.e("==>", currentLongitude.toString());
//                    Log.e("==>", userLocation.getLatitude() + "");
//                    String nominatimQuery = "https://nominatim.openstreetmap.org/reverse?email=so5olimpiu@yahoo.com&format=json&lat=" + userLocation.getLatitude() + "&lon=" + userLocation.getLongitude() + "&extratags=1&namedetails=1";
//                    getData(nominatimQuery);
//                    ((TextView) findViewById(R.id.text_view_id)).setText(currentLatitude.toString());
//                    Toast.makeText(getApplicationContext(), currentLatitude.toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }).addOnFailureListener(this, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("==>", "INAINTEEROARE");
//                e.printStackTrace();
//                Log.e("==>", "DUPAEROARE");
//            }
//        });
//        //return locationResult.getResult();
//    }
//
//    public void getData(String url) {
//        //String url = "https://rocky-wave-99733.herokuapp.com/demo";
//        Log.d("==>", "INAINTEEEEEE");
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type","application/json");
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        ((TextView) findViewById(R.id.text_view_id)).setText(response.toString());
//                        Log.e("==>", response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        ((TextView) findViewById(R.id.text_view_id)).setText("try again");
//                        Log.e("==>", "EROAREEEEEE");
//                    }
//                }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return headers;
//            }
//        };
//        // SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
//        queue.add(jsonObjectRequest);
//        Log.e("==>", "DUPAAA");
//    }

    public void getCategories(String url) {
        //String url = "https://rocky-wave-99733.herokuapp.com/demo";
        Log.d("==>", "INAINTEEEEEE");
        RequestQueue queue = Volley.newRequestQueue(this);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("==>", response.toString());
                        categoriesResponse=response;
                        ArrayList<CategoryDTO> categoriesArray = new Gson().fromJson(categoriesResponse.toString(), new TypeToken<List<CategoryDTO>>(){}.getType());
                        categoryAdapter.clear();
                        categoryAdapter.addAll(categoriesArray);
                        categoryAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        ((TextView) findViewById(R.id.text_view_id)).setText("try again");
                        Log.e("==>", "EROAREEEEEE");
                    }
                }){
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
