package com.example.stefao.smsreader.view.transactions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.stefao.smsreader.view.feedback.PoisActivity;
import com.example.stefao.smsreader.view.settings.SettingsActivity;
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
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transactions);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.action_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        transactionsViewModel = new TransactionsViewModel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }


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

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        //Intent i = new Intent(getApplicationContext(), TransactionsActivity.class);
                        //startActivity(i);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
        );

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
