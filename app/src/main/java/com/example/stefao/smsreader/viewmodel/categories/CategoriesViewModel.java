package com.example.stefao.smsreader.viewmodel.categories;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.model.CategoryDTO;
import com.example.stefao.smsreader.view.categories.CategoryAdapter;
import com.example.stefao.smsreader.view.categories.MainActivity;
import com.example.stefao.smsreader.view.utils.MultiSpinner;
import com.example.stefao.smsreader.view.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stefao on 6/8/2018.
 */

public class CategoriesViewModel {

    float expenses =0;
    float categoryBudget=0;
    ProgressDialog pDialogFetch;
    JSONArray categoriesResponse = new JSONArray();
//    MainActivity mainActivity = new MainActivity();
//    ListView listview;


    public CategoriesViewModel(){
        //listview = mainActivity.getListview();
    };

    public float getExpensesByCategory(String url, final CategoryAdapter.ViewHolder holder, final Context mContext) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");


        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.GET, url,  new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        char[] responseChar = response.toCharArray();
                        String expensesString="";
                        String budgetString="";
                        boolean comma = false;
                        for (int i=1; i<responseChar.length; i++){
                            if (responseChar[i]==','){
                                comma = true;
                            }
                            else{
                                if (responseChar[i]==']'){
                                    break;
                                }
                                if (comma == false){
                                    expensesString+=responseChar[i];
                                }
                                if (comma==true){
                                    budgetString+=responseChar[i];
                                }
                            }
                        }

                        expenses = Float.parseFloat(budgetString);
                        categoryBudget = Float.parseFloat(expensesString);

                        Log.e("expenses",String.valueOf(expenses));
                        if (expenses>0) {
                            holder.progressBar.setProgress(Math.round(expenses*100/categoryBudget));
                            holder.progressBar.setText(Math.round(expenses*100/categoryBudget)+"%");
                        }
                        else{
                            holder.progressBar.setProgress(0);
                            holder.progressBar.setText("0%");
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
        return expenses;
    }

    public void getCategories(String url, final Context context, final ListView listview, final MultiSpinner spinnerSelectInitialCategories, final CategoryAdapter categoryAdapter ) {
        //String url = "https://rocky-wave-99733.herokuapp.com/demo";
        Log.d("==>", "INAINTEEEEEE");
        RequestQueue queue = Volley.newRequestQueue(context);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");

        pDialogFetch = new ProgressDialog(context);
        pDialogFetch.setTitle("Fetching your categories");
        pDialogFetch.setMessage("Please wait...");
        pDialogFetch.setCancelable(false);
        pDialogFetch.show();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("==>", response.toString());
                        categoriesResponse=response;
                        ArrayList<CategoryDTO> categoriesArray = new Gson().fromJson(categoriesResponse.toString(), new TypeToken<List<CategoryDTO>>(){}.getType());
                        if (categoriesArray.size()==0){
                            listview.setVisibility(View.GONE);
                            spinnerSelectInitialCategories.setVisibility(View.VISIBLE);
                            Toast.makeText(context,"Choose your first categories",
                                    Toast.LENGTH_LONG).show();
                        }
                        categoryAdapter.clear();
                        categoryAdapter.addAll(categoriesArray);
                        categoryAdapter.notifyDataSetChanged();
                        Utility.setListViewHeightBasedOnChildren(listview);
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
