package com.example.stefao.smsreader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stefao.smsreader.Entities.CategoryDTO;
import com.example.stefao.smsreader.Entities.TransactionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefao on 5/1/2018.
 */

public class TransactionAdapter extends ArrayAdapter<TransactionDTO> {
    private Context mContext;
    private List<TransactionDTO> transactionDTOS = new ArrayList<>();

    public TransactionAdapter(@NonNull Context context, ArrayList<TransactionDTO> list) {
        super(context, 0 , list);
        mContext = context;
        transactionDTOS= list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_transaction, parent, false);

        TransactionDTO currentTransaction = transactionDTOS.get(position);

        TextView date = (TextView) listItem.findViewById(R.id.textView_date);
        date.setText(currentTransaction.getDate());
        TextView amount = (TextView) listItem.findViewById(R.id.textView_amount);
        amount.setText(String.valueOf(currentTransaction.getAmount()));
//        TextView poi = (TextView) listItem.findViewById(R.id.textView_poi);
//        poi.setText(currentTransaction.getPoiDTO().getName());

        return listItem;
    }
}
