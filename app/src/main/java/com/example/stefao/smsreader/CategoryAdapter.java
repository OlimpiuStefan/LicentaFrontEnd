package com.example.stefao.smsreader;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stefao.smsreader.Entities.CategoryDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefao on 5/1/2018.
 */

public class CategoryAdapter extends ArrayAdapter<CategoryDTO> {
    private Context mContext;
    private List<CategoryDTO> categoryList = new ArrayList<>();

    public CategoryAdapter(@NonNull Context context, ArrayList<CategoryDTO> list) {
        super(context, 0 , list);
        mContext = context;
        categoryList= list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_category, parent, false);

        CategoryDTO currentCategory = categoryList.get(position);

        ImageView image = (ImageView) listItem.findViewById(R.id.imageView_poster);
        //image.setImageResource(currentCategory.getmImageDrawable());

        TextView name = (TextView) listItem.findViewById(R.id.textView_name);
        name.setText(currentCategory.getSubcategory());


        return listItem;
    }
}
