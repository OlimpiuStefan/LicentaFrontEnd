package com.example.stefao.smsreader.application;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import android.os.Binder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.RegisterActivity;
import com.example.stefao.smsreader.SmsListener;
import com.example.stefao.smsreader.SmsReceiver;
import com.example.stefao.smsreader.location.fetcher.LocationUtils;
import com.example.stefao.smsreader.location.fetcher.UserLocation;
import com.example.stefao.smsreader.service.LoginService;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.VolleyUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stefao on 4/15/2018.
 */

public class PersistService extends Service {
    private final IBinder mBinder = new MyBinder();
    public static final String AVAILABLE_DATA="com.example.stefao.smsreader.AVAILABLE_DATA";
    private Intent result = new Intent(AVAILABLE_DATA);
    LocationUtils locationUtils = new LocationUtils(this);
    UserLocation userLocation = new UserLocation();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        processLocationAndPoi();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        processLocationAndPoi();
        return mBinder;
    }

    public class MyBinder extends Binder {
        public PersistService getService() {
            return PersistService.this;
        }
    }

    public Intent getResult() {
        if (!result.hasExtra("userLocationLatitude")) {
            while (!result.hasExtra("userLocationLatitude")) {
                continue;
            }
        }
        return result;
    }

    private void processLocationAndPoi() {
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.e("==>", messageText);
                userLocation.setLocationSetted(false);
                getLocation();
            }
        });
    }

    public void getLocation() {


        locationUtils.getLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Double currentLatitude = location.getLatitude();
                    Double currentLongitude = location.getLongitude();
                    userLocation.setLatitude(location.getLatitude());
                    userLocation.setLongitude(location.getLongitude());
                    userLocation.setLocationSetted(true);
                    Log.e("==>", currentLatitude.toString());
                    Log.e("==>", currentLongitude.toString());
                    Log.e("==>", userLocation.getLatitude() + "");
                    String nominatimQuery = "https://nominatim.openstreetmap.org/reverse?email=so5olimpiu@yahoo.com&format=json&lat=" + userLocation.getLatitude() + "&lon=" + userLocation.getLongitude() + "&extratags=1&namedetails=1";
                    getData(nominatimQuery);
                    Toast.makeText(getApplicationContext(), currentLatitude.toString(), Toast.LENGTH_LONG).show();
                    result.putExtra("userLocationLatitude",userLocation.getLatitude());
                    result.putExtra("userLocationLongitude",userLocation.getLongitude());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("==>", "INAINTEEROARE");
                e.printStackTrace();
                Log.e("==>", "DUPAEROARE");
            }
        });
        //return locationResult.getResult();
    }

    public void getData(String url) {
        //String url = "https://rocky-wave-99733.herokuapp.com/demo";
        Log.d("==>", "INAINTEEEEEE");
        RequestQueue queue = Volley.newRequestQueue(this);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("==>", response.toString());
                        result.putExtra("responseJson",response.toString());
                        sendBroadcast(result);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("==>", "EROAREEEEEE");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        // SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
        queue.add(jsonObjectRequest);
        Log.e("==>", "DUPAAA");
    }

    public void addPoi(double latitude, double longitude, JSONObject response) {

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        final JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("latitude", latitude);
            requestBody.put("longitude", longitude);
            requestBody.put("name", response.getString("display_name"));
            requestBody.put("type",response.getString(response.getJSONObject("address").keys().next()));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String URL = Constants.ADD_POI_URL;

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        {
                            Log.e("==>",response.toString());
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
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        requestQueue.add(request);
    }
}