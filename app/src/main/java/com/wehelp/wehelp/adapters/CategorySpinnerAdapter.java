package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomaspessato on 04/12/16.
 */

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    private Context context;
    ArrayList<Category> list = null;

    public CategorySpinnerAdapter(Context context, int resource, ArrayList<Category> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_spinner_category, null);
        }

        Category category = list.get(position);
        String categoryName = category.getDescricao();
        TextView txtSpinner = (TextView) convertView.findViewById(R.id.txt_spinner);
        txtSpinner.setText(categoryName);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        super.getDropDownView(position, convertView, parent);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_spinner_category, null);
        }

        Category category = list.get(position);
        String categoryName = category.getDescricao();
        TextView txtSpinner = (TextView) convertView.findViewById(R.id.txt_spinner);
        txtSpinner.setText(categoryName);
//        System.out.println("CATEGORY INSIDE ADAPTER: "+list.get(position).getDescricao());

        return convertView;
    }
}
