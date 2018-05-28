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
import com.example.stefao.smsreader.Entities.PoiDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefao on 5/23/2018.
 */

public class PoiAdapter extends ArrayAdapter<PoiDTO> {
    private Context mContext;
    private List<PoiDTO> poiList = new ArrayList<>();

    public PoiAdapter(@NonNull Context context, ArrayList<PoiDTO> list) {
        super(context, 0 , list);
        mContext = context;
        poiList= list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_poi, parent, false);

        PoiDTO currentPoi = poiList.get(position);

        //ImageView image = (ImageView) listItem.findViewById(R.id.imageView_poster);
        //image.setImageResource(currentCategory.getmImageDrawable());

        TextView name = (TextView) listItem.findViewById(R.id.textViewPoi_name);
        name.setText(currentPoi.getName());


        return listItem;
    }
}
