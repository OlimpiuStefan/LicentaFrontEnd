package com.example.stefao.smsreader.view.recommend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.model.PoiDTO;
import com.example.stefao.smsreader.model.TransactionDTO;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.viewmodel.transactions.TransactionsViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stefao on 5/1/2018.
 */

public class PoiRatingAdapter extends ArrayAdapter<PoiDTO> {
    private Context mContext;
    private List<PoiDTO> poiDTOS = new ArrayList<>();

    public PoiRatingAdapter(@NonNull Context context, ArrayList<PoiDTO> list) {
        super(context, 0 , list);
        mContext = context;
        poiDTOS= list;
    }

    public static class PoiFrequencyViewHolder {
        public TextView nameTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_rating_poi, parent, false);

        PoiDTO currentPoi = poiDTOS.get(position);

        PoiFrequencyViewHolder viewHolder = new PoiFrequencyViewHolder();

        viewHolder.nameTextView = (TextView) listItem.findViewById(R.id.poi_rating_name);
        listItem.setTag(viewHolder);

//        TextView date = (TextView) listItem.findViewById(R.id.textView_date);
//        date.setText(currentTransaction.getDate());
//        TextView amount = (TextView) listItem.findViewById(R.id.textView_amount);
//        amount.setText(String.valueOf(currentTransaction.getAmount()));
//        //TextView poi = (TextView) listItem.findViewById(R.id.textView_poi);
//        //poi.setText(currentTransaction.getPoiDTO().getName());

        PoiFrequencyViewHolder holder = (PoiFrequencyViewHolder) listItem.getTag();
        holder.nameTextView.setText(currentPoi.getName());
        return listItem;
    }


}
