package com.solarbabes.heliopot;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        Log.d("position",Integer.toString(position));
        if (convertView == null) {
            convertView = thisInflater.inflate(R.layout.plant_list_item, parent, false);

            TextView plantName = (TextView) convertView.findViewById(R.id.textView_Name);
            TextView plantWateringTime = (TextView) convertView.findViewById(R.id.textView_WateringTime);
            ImageView plantImage = (ImageView) convertView.findViewById(R.id.imageView_plant);

            PlantListItem current = (PlantListItem) getItem(position);

            plantName.setText(current.getName());
            plantWateringTime.setText(current.getWateringTime());
            plantImage.setImageResource(current.getImageID());
        }

        return convertView;
    }




}
