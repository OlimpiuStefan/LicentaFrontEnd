package com.example.stefao.smsreader.view.transactions;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.stefao.smsreader.model.TransactionDTO;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.UserSessionManager;
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

public class TransactionsActivity extends AppCompatActivity {
    JSONArray transactionsResponse = new JSONArray();
    UserSessionManager userSessionManager;
    TransactionAdapter transactionAdapter;
    ListView listViewTransactions;
    Context mContext;
    TransactionsViewModel transactionsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transactions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        transactionsViewModel = new TransactionsViewModel();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        userSessionManager = new UserSessionManager(this);
        listViewTransactions = (ListView) findViewById(R.id.transactions_list);
        ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.transactions_header, listViewTransactions,false);
        listViewTransactions.addHeaderView(headerView);
        Log.e("==>",userSessionManager.getUserDetails().get(KEY_EMAIL));
        CategoryDTO clickedCategory = (CategoryDTO)getIntent().getExtras().get("CategoryDTO");
        //getTransactions(Constants.BASE_URL+Constants.GET_TRANSACTIONS_URL+userSessionManager.getUserDetails().get(KEY_EMAIL)+"/"+clickedCategory.getId());
        Log.e("==>>>>",transactionsResponse.toString());
        ArrayList<TransactionDTO> transactionsArray = new Gson().fromJson(transactionsResponse.toString(), new TypeToken<List<TransactionDTO>>(){}.getType());
        transactionAdapter = new TransactionAdapter(this,transactionsArray);
        listViewTransactions.setAdapter(transactionAdapter);
        transactionsViewModel.getTransactions(Constants.BASE_URL+Constants.GET_TRANSACTIONS_URL+userSessionManager.getUserDetails().get(KEY_EMAIL)+"/"+clickedCategory.getId(),this,transactionAdapter);



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




//    public void getTransactions(String url) {
//        //String url = "https://rocky-wave-99733.herokuapp.com/demo";
//        Log.d("==>", "INAINTEEEEEE");
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type","application/json");
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//                        Log.e("==>", response.toString());
//                        transactionsResponse=response;
//                        ArrayList<TransactionDTO> transactionsArray = new Gson().fromJson(transactionsResponse.toString(), new TypeToken<List<TransactionDTO>>(){}.getType());
//                        transactionAdapter.clear();
//                        transactionAdapter.addAll(transactionsArray);
//                        transactionAdapter.notifyDataSetChanged();
//
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
//        queue.add(jsonArrayRequest);
//        Log.e("==>", "DUPAAA");
//    }
}
