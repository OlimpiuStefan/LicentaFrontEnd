package com.example.stefao.smsreader.application;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import android.os.Binder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.model.TransactionDTO;
import com.example.stefao.smsreader.utils.NewTransactionReceiver;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.utils.sms.SmsListener;
import com.example.stefao.smsreader.utils.sms.SmsReceiver;
import com.example.stefao.smsreader.utils.location.LocationUtils;
import com.example.stefao.smsreader.utils.location.UserLocation;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.example.stefao.smsreader.utils.VolleyUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.stefao.smsreader.utils.UserSessionManager.KEY_EMAIL;

/**
 * Created by stefao on 4/15/2018.
 */

public class PersistService extends Service {
    private final IBinder mBinder = new MyBinder();
    public static final String AVAILABLE_DATA = "com.example.stefao.smsreader.AVAILABLE_DATA";
    private Intent result = new Intent(AVAILABLE_DATA);
    LocationUtils locationUtils = new LocationUtils(this);
    UserLocation userLocation = new UserLocation();
    TransactionDTO userTransaction = new TransactionDTO();
    UserSessionManager userSessionManager;
    String categoryName;
    AlertDialog.Builder builder;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        processLocationAndPoi();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //processLocationAndPoi();
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

    public void processLocationAndPoi() {
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.e("==>", messageText);
                userLocation.setLocationSetted(false);
                String[] result = parseMessage(messageText);
                Log.e("===>",result[0]);
                Log.e("===>",result[0].getClass().toString());
                Log.e("===>",result[1]);
                userTransaction.setAmount(Double.parseDouble(result[0]));
                userTransaction.setMessage(messageText);
                userTransaction.setDate(result[1]);
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
                    String nominatimQuery = "https://nominatim.openstreetmap.org/reverse?email=so5olimpiu@yahoo.com&format=json&lat=" + "46.76843" + "&lon=" + "23.58898" + "&extratags=1&namedetails=1";
                    //String nominatimQuery = "https://nominatim.openstreetmap.org/reverse?email=so5olimpiu@yahoo.com&format=json&lat=" + "46.76833" + "&lon=" + "23.58923" + "&extratags=1&namedetails=1";
                    getData(nominatimQuery);
                    Toast.makeText(getApplicationContext(), currentLatitude.toString(), Toast.LENGTH_LONG).show();
                    result.putExtra("userLocationLatitude", userLocation.getLatitude());
                    result.putExtra("userLocationLongitude", userLocation.getLongitude());

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
        headers.put("Content-Type", "application/json");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("==>", response.toString());
                        result.putExtra("responseJson", response.toString());
                        sendBroadcast(result);
                        userSessionManager = new UserSessionManager(getApplicationContext());
                        Log.e("sesiunea",userSessionManager.getUserDetails().get(KEY_EMAIL));
                        //String categoryName="";
                        try {
                            categoryName = response.getJSONObject("address").keys().next();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        addPoi(46.76843,23.58898,response,userSessionManager.getUserDetails().get(KEY_EMAIL));
                       // addPoi(46.76833,23.58923,response,userSessionManager.getUserDetails().get(KEY_EMAIL));
                        String url = Constants.IS_CATEGORY_PRESENT+"/"+userSessionManager.getUserDetails().get(KEY_EMAIL)+"/"+categoryName;
                        isCategoryPresent(url);
                        //addTransaction(userSessionManager.getUserDetails().get(KEY_EMAIL),categoryName,userTransaction.getAmount(),userTransaction.getDate(),userTransaction.getMessage());
//                        String poiName ="";
//                        try {
//                            poiName= response.getString("display_name");
//                            Log.e("====>",poiName);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        addTransactionToPoi(poiName,userTransaction.getAmount(),userTransaction.getDate(),userTransaction.getMessage());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("==>", "EROAREEEEEE");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        // SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
        queue.add(jsonObjectRequest);
        Log.e("==>", "DUPAAA");
    }

    public void addPoi(double latitude, double longitude, JSONObject response, String username) {

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        final JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("latitude", latitude);
            requestBody.put("longitude", longitude);
            requestBody.put("name", response.getString("display_name"));
            requestBody.put("type", response.getJSONObject("address").keys().next()).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String URL = Constants.ADD_POI_URL+"/"+username;

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

    public void addTransaction(final String username, final String subcategory, double amount, String date, String message) {

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        final JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("amount", amount);
            requestBody.put("date", date);
            requestBody.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String URL = Constants.ADD_TRANSACTION_TO_USER_URL+"/"+username+"/"+subcategory;

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
                            String url = Constants.GET_USER_CATEGORY_BUDGET_URL+"/"+subcategory+"/"+username;
                            getUserCategoryBudget(url);

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

    public void addTransactionToPoi(String name, double amount, String date, String message) {

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        final JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("amount", amount);
            requestBody.put("date", date);
            requestBody.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String URL = Constants.ADD_TRANSACTION_TO_POI_URL+"/"+name;

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

    public void isCategoryPresent (String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");


        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.GET, url,  new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        boolean isCategoryPresent = Boolean.parseBoolean(response.toString());
                        if (isCategoryPresent==false){
                            Intent intentAction = new Intent(getApplicationContext(),NewTransactionReceiver.class);

                            //This is optional if you have more than one buttons and want to differentiate between two
                            intentAction.putExtra("username",userSessionManager.getUserDetails().get(KEY_EMAIL));
                            intentAction.putExtra("categoryName", categoryName);
                            intentAction.putExtra("amount", userTransaction.getAmount());
                            intentAction.putExtra("date",userTransaction.getDate());
                            intentAction.putExtra("message",userTransaction.getMessage());

                            PendingIntent newTransactionPendingIntent =
                                    PendingIntent.getBroadcast(getApplicationContext(), 0, intentAction, 0);

                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                                    .setSmallIcon(R.drawable.spinner_background)
                                    .setContentTitle("new category detected")
                                    .setContentText("You made a transaction in a new category: "+categoryName+". Do you want it to be added to your categories")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(newTransactionPendingIntent)
                                    .addAction(R.drawable.spinner_background, "yes",
                                            newTransactionPendingIntent);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                            notificationManager.notify(2, mBuilder.build());
//                            builder = new AlertDialog.Builder(getApplicationContext());
//                            builder.setTitle("Do you want the new category "+categoryName+"?" );
//
//                            final EditText input = new EditText(getApplicationContext());
//                            input.setInputType(InputType.TYPE_CLASS_TEXT);
//                            builder.setView(input);
//
//                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Log.e("budget",input.getText().toString());
//                                    addTransaction(userSessionManager.getUserDetails().get(KEY_EMAIL),categoryName,userTransaction.getAmount(),userTransaction.getDate(),userTransaction.getMessage());
//                                }
//                            });
//
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                            builder.show();
                        }
                        else{
                            addTransaction(userSessionManager.getUserDetails().get(KEY_EMAIL),categoryName,userTransaction.getAmount(),userTransaction.getDate(),userTransaction.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void getUserCategoryBudget (String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");


        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.GET, url,  new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                            float remainedBudget = Float.parseFloat(response.toString());
                            if (remainedBudget<0) {
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                                        .setSmallIcon(R.drawable.spinner_background)
                                        .setContentTitle("BANII MAA")
                                        .setContentText("nu mai ai bani ma muie pentru "+categoryName)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                                notificationManager.notify(1, mBuilder.build());
                            }

                        }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public String[] parseMessage(String message) {
        char[] messageChar = message.toCharArray();
        int dotCount = 0;
        String amount = "";
        String date = "";
        boolean dotCount1=false;
        boolean dotCount5=false;
        for (int i = 0; i < messageChar.length; i++) {
            if (dotCount == 1 && dotCount1==false) {
                int j = i;
                while (messageChar[j] != 'a') {
                    j++;
                }
                j++;
                j++;
                while (messageChar[j] != ' ') {
                    amount += messageChar[j];
                    j++;
                }
                dotCount1=true;
            }
            if (dotCount == 5 && dotCount5==false) {
                int j = i + 1;
                while (messageChar[j] != ' ') {
                    date += messageChar[j];
                    j++;
                }
                dotCount5=true;

            }
            if (messageChar[i] == '.') {
                dotCount++;
            }
        }
        String[] result = new String[2];
        result[0]=amount;
        result[1]=date;
        return result;
    }
}