package com.example.stefao.smsreader.view.transactions;

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

public class TransactionAdapter extends ArrayAdapter<TransactionDTO> {
    private Context mContext;
    private List<TransactionDTO> transactionDTOS = new ArrayList<>();
    TransactionsViewModel transactionsViewModel;

    public TransactionAdapter(@NonNull Context context, ArrayList<TransactionDTO> list) {
        super(context, 0 , list);
        mContext = context;
        transactionDTOS= list;
        transactionsViewModel = new TransactionsViewModel();
    }

    public static class TransactionViewHolder {
        public TextView dateTextView;
        public TextView amountTextView;
        public TextView nameTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_transaction, parent, false);

        TransactionDTO currentTransaction = transactionDTOS.get(position);

        TransactionViewHolder viewHolder = new TransactionViewHolder();

        viewHolder.amountTextView = (TextView) listItem.findViewById(R.id.textView_amount);
        viewHolder.dateTextView = (TextView) listItem.findViewById(R.id.textView_date);
        viewHolder.nameTextView = (TextView) listItem.findViewById(R.id.textView_poi);
        listItem.setTag(viewHolder);

//        TextView date = (TextView) listItem.findViewById(R.id.textView_date);
//        date.setText(currentTransaction.getDate());
//        TextView amount = (TextView) listItem.findViewById(R.id.textView_amount);
//        amount.setText(String.valueOf(currentTransaction.getAmount()));
//        //TextView poi = (TextView) listItem.findViewById(R.id.textView_poi);
//        //poi.setText(currentTransaction.getPoiDTO().getName());

        TransactionViewHolder holder = (TransactionViewHolder) listItem.getTag();
        holder.dateTextView.setText(currentTransaction.getDate());
        holder.amountTextView.setText(String.valueOf(currentTransaction.getAmount()));

        String url = Constants.GET_POI_FOR_TRANSACTION_URL+"/"+currentTransaction.getId();
        //getPoiForTransaction(url,holder);
        transactionsViewModel.getPoiForTransaction(url,mContext,holder);
        return listItem;
    }

//    public void getPoiForTransaction(String url, final TransactionViewHolder holder) {
//        RequestQueue queue = Volley.newRequestQueue(mContext);
//        final Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type","application/json");
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        Log.e("==>", response.toString());
//                        PoiDTO poiDTO = new Gson().fromJson(response.toString(), new TypeToken<PoiDTO>(){}.getType());
//                        holder.nameTextView.setText(poiDTO.getName());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        Log.e("==>", "EROAREEEEEE");
//                        //pDialogFetch.hide();
//                    }
//                }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return headers;
//            }
//        };
//        // SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
//        queue.add(jsonObjectRequest);
//        Log.e("==>", "DUPAAA");
//    }
}
