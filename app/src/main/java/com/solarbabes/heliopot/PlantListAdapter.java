package com.solarbabes.heliopot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlantListAdapter extends BaseAdapter {

    private ArrayList<PlantListItem> plantList;
    private LayoutInflater thisInflater;

    public PlantListAdapter(Context context, ArrayList<PlantListItem> plantList) {
        this.plantList = plantList;
        this.thisInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return plantList.size();
    }

    @Override
    public Object getItem(int position) {
        return plantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Each plant item (row) will have an imageView and two textViews.

        convertView = thisInflater.inflate(R.layout.plant_list_item, parent, false);

        TextView plantName = (TextView) convertView.findViewById(R.id.textView_Name);
        TextView plantMessage = (TextView) convertView.findViewById(R.id.textView_message);
        ImageView plantImage = (ImageView) convertView.findViewById(R.id.imageView_plant);

        PlantListItem current = (PlantListItem) getItem(position);

        plantName.setText(current.getName());
        plantMessage.setText(current.getWateringTime());
        plantImage.setImageResource(current.getImageID());

        return convertView;
    }




}
