package com.example.joe.listview_image_glide;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class ListViewAdapter extends BaseAdapter {

    private Activity activity;
    private String[] filePath;
    private String[] fileName;
    private String[] fileDate;


    private static LayoutInflater inflater = null;

    public ListViewAdapter(Activity a, String[] path, String[] name, String[] date) {
        activity = a;
        filePath = path;
        fileName = name;
        fileDate = date;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    static class ViewHolder {
        TextView text;
        TextView text1;
        ImageView image;
    }

    public int getCount() {
        int count = 0;
        try{
            count = filePath.length;
        } catch (Exception e){
            e.printStackTrace();
        }
        return count;
        }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout,parent,false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(fileName[position]);
        holder.text1.setText(fileDate[position]);

        Glide.with(holder.image.getContext())
             .load(filePath[position])
             .into(holder.image);

        return convertView;
    }
}