package com.example.stefao.smsreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.Entities.CategoryDTO;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stefao on 5/1/2018.
 */
import static com.example.stefao.smsreader.utils.UserSessionManager.KEY_EMAIL;
public class CategoryAdapter extends ArrayAdapter<CategoryDTO> {
    private Context mContext;
    private List<CategoryDTO> categoryList = new ArrayList<>();
    UserSessionManager userSessionManager;
    float expenses =0;

    public CategoryAdapter(@NonNull Context context, ArrayList<CategoryDTO> list) {
        super(context, 0 , list);
        mContext = context;
        categoryList= list;
        userSessionManager = new UserSessionManager(mContext);
    }

    static class ViewHolder {
        public TextView nameTextView;
        public Button buttonSetBudget;
        public ProgressBar progressBar;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_category, parent, false);

        CategoryDTO currentCategory = categoryList.get(position);

        //ImageView image = (ImageView) listItem.findViewById(R.id.imageView_poster);
        //image.setImageResource(currentCategory.getmImageDrawable());

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.nameTextView = (TextView) listItem.findViewById(R.id.textView_name);
        //TextView name = (TextView) listItem.findViewById(R.id.textView_name);
        //Button setBudgetButton = (Button) listItem.findViewById(R.id.button_setBudget);
        viewHolder.buttonSetBudget = (Button) listItem.findViewById(R.id.button_setBudget);
        viewHolder.progressBar = (ProgressBar) listItem.findViewById(R.id.determinateBar);
        listItem.setTag(viewHolder);

        ViewHolder holder = (ViewHolder) listItem.getTag();
        holder.nameTextView.setText(currentCategory.getSubcategory());
        holder.buttonSetBudget.setText("Set Budget");

        String URL = Constants.GET_EXPENSES_BY_CATEGORY_URL+"/"+currentCategory.getId()+"/"+userSessionManager.getUserDetails().get(KEY_EMAIL);
        float expenses = getExpensesByCategory(URL, holder);
       // holder.progressBar.setProgress(Math.round(expenses));
        holder.progressBar.setMax(100);



        return listItem;
    }


    public float getExpensesByCategory(String url, final ViewHolder holder) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");


        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.GET, url,  new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        expenses = Float.parseFloat(response.toString());

                        Log.e("expenses",String.valueOf(expenses));
                        holder.progressBar.setProgress(Math.round(expenses));
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


}
