package com.example.casquenet.main_screen_fragments.settings_frags;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<CharSequence> {

    private Context context;
    private int dropdownLayoutResourceId;
    private int selectedLayoutResourceId;

    public CustomSpinnerAdapter(Context context, int resource, int dropdownResource, CharSequence[] items) {
        super(context, resource, items);
        this.context = context;
        this.dropdownLayoutResourceId = dropdownResource;
        this.selectedLayoutResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, selectedLayoutResourceId);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, dropdownLayoutResourceId);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent, int layoutResourceId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        TextView view = (TextView) inflater.inflate(layoutResourceId, parent, false);
        view.setText(getItem(position));
        return view;
    }
}
