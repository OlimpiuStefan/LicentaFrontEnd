package com.example.stefao.smsreader.view.recommend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.VolleyUtils;
import com.example.stefao.smsreader.view.transactions.TransactionsActivity;
import com.example.stefao.smsreader.view.utils.CustomOnItemSelectedListener;
import com.example.stefao.smsreader.viewmodel.recommend.RecommendViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stefao on 5/28/2018.
 */

public class RecommendActivity extends AppCompatActivity {

    private Spinner spinner1;
    Context mContext;
    Activity mActivity;
    private Button btnSubmit;
    RecommendViewModel recommendViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        mActivity=this;
        super.onCreate(savedInstanceState);
        recommendViewModel = new RecommendViewModel();
        //locationUtils = new LocationUtils(this, this);
        //userLocation = new UserLocation();

        setContentView(R.layout.activity_recommend);

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.travelreasons, R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        //spinner2 = (Spinner) findViewById(R.id.spinner2);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(mActivity,
                        "OnClickListener : " +
                                "\nSpinner 1 : " + String.valueOf(spinner1.getSelectedItem()),
                        //+
                        //"\nSpinner 2 : " + String.valueOf(spinner2.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();

                //recommendByCategory(String.valueOf(spinner1.getSelectedItem()));
                //recommendByRating(String.valueOf(spinner1.getSelectedItem()));
                Intent intent = new Intent(mContext, RecommendationsActivity.class);
                intent.putExtra("subcategory",String.valueOf(spinner1.getSelectedItem()));
                startActivity(intent);
                //recommendViewModel.recommendByRating(String.valueOf(spinner1.getSelectedItem()),mContext);
            }

        });
    }

}
