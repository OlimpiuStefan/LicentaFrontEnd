package com.example.stefao.smsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stefao on 6/3/2018.
 */

public class NewTransactionReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();

        String username =intent.getStringExtra("username");
        String categoryName =intent.getStringExtra("categoryName");
        double amount =intent.getDoubleExtra("amount",0);
        String date =intent.getStringExtra("date");
        String message =intent.getStringExtra("message");

        addTransaction(username,categoryName,amount,date,message, context);

        //This is used to close the notification tray
        //Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //context.sendBroadcast(it);
    }

    public void addTransaction(final String username, final String subcategory, double amount, String date, String message, Context context) {

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

        final RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                            //getUserCategoryBudget(url);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

