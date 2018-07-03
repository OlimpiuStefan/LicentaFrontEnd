package com.example.stefao.smsreader.viewmodel.transactions;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.model.PoiDTO;
import com.example.stefao.smsreader.model.TransactionDTO;
import com.example.stefao.smsreader.view.transactions.TransactionAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stefao on 6/8/2018.
 */

public class TransactionsViewModel {

    JSONArray transactionsResponse = new JSONArray();

    ProgressDialog pDialogFetch ;

    public TransactionsViewModel() {
    }

    public void getTransactions(String url, Context context, final TransactionAdapter transactionAdapter) {
        //String url = "https://rocky-wave-99733.herokuapp.com/demo";
        Log.d("==>", "INAINTEEEEEE");
        RequestQueue queue = Volley.newRequestQueue(context);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        pDialogFetch = new ProgressDialog(context);
        pDialogFetch.setTitle("Fetching your transactions");
        pDialogFetch.setMessage("Please wait...");
        pDialogFetch.setCancelable(false);
        pDialogFetch.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("==>", response.toString());
                        transactionsResponse = response;
                        ArrayList<TransactionDTO> transactionsArray = new Gson().fromJson(transactionsResponse.toString(), new TypeToken<List<TransactionDTO>>() {
                        }.getType());
                        transactionAdapter.clear();
                        transactionAdapter.addAll(transactionsArray);
                        transactionAdapter.notifyDataSetChanged();
                        pDialogFetch.hide();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //((TextView) findViewById(R.id.text_view_id)).setText("try again");
                        Log.e("==>", "EROAREEEEEE");
                        pDialogFetch.hide();
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

    public void getPoiForTransaction(String url, Context mContext, final TransactionAdapter.TransactionViewHolder holder) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("==>", response.toString());
                        PoiDTO poiDTO = new Gson().fromJson(response.toString(), new TypeToken<PoiDTO>() {
                        }.getType());
                        holder.nameTextView.setText(poiDTO.getName());
                        //pDialogFetch.hide();
                        //pDialogFetch.cancel();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("==>", "EROAREEEEEE");
                        //pDialogFetch.hide();
                        //pDialogFetch.hide();
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
}
