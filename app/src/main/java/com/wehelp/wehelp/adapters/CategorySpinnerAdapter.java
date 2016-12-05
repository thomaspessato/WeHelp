package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Category;

import java.util.List;

/**
 * Created by thomaspessato on 04/12/16.
 */

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    private Context context;


    public CategorySpinnerAdapter(Context context, List<Category> list) {
        super(context, R.layout.row_comment, list);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.support_simple_spinner_dropdown_item, null);
        }

        TextView tvText1 = (TextView) convertView.findViewById(android.R.id.text1);
        tvText1.setText("Teste");

        return convertView;
    }
}
