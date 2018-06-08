package com.example.stefao.smsreader.viewmodel.feedback;

import android.content.Context;
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
 * Created by stefao on 6/8/2018.
 */

public class FeedbackViewModel {

    public FeedbackViewModel(){}

    public void addPoiRatingToPoi(String name, final Context context, String price, String quality, String accesibility, String amability) {

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
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.NO_CONNECTION, context);
                        } else {
                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                if (statusCode >= 500) {
                                    VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.SERVER_DOWN, context);
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
