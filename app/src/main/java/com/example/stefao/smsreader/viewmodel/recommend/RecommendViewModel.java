package com.example.stefao.smsreader.viewmodel.recommend;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.utils.Constants;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stefao on 6/8/2018.
 */

public class RecommendViewModel {

    public RecommendViewModel(){};

    public void recommendByRating(String subcategory, Context mContext) {

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");


//        final JSONObject requestBody = new JSONObject();
//
//        try {
//            requestBody.put("subcategory", subcategory);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        String URL = Constants.RECOMMEND_BY_RATING+"/"+subcategory;

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("rating", response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //((TextView) findViewById(R.id.text_view_id)).setText("try again");
                        Log.e("==>", "EROAREEEEEE");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }
}
