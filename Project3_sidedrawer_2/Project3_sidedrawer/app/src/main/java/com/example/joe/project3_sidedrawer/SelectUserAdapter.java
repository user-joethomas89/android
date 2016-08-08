package com.example.joe.project3_sidedrawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joe.project3_sidedrawer.R;
//import com.example.joe.project3_sidedrawer.SelectUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectUserAdapter extends BaseAdapter {
    public List<ContactsFragment.ContactInfo> data;
    private ArrayList<ContactsFragment.ContactInfo> arrayList;
    Context context;
    ViewHolder v;

    public SelectUserAdapter(List<ContactsFragment.ContactInfo> contactInfos, Context contxt) {
        data = contactInfos;
        context = contxt;
        this.arrayList = new ArrayList<ContactsFragment.ContactInfo>();
        this.arrayList.addAll(data);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.fragment_layout, null);
        } else {
            view = convertView;
        }
        v = new ViewHolder();
        v.title = (TextView) view.findViewById(R.id.name);
        v.phone = (TextView) view.findViewById(R.id.no);
        v.imageView = (ImageView) view.findViewById(R.id.pic);
        final ContactsFragment.ContactInfo contactInfo = (ContactsFragment.ContactInfo) data.get(i);
        v.title.setText(contactInfo.getName());
        int count = 0;
        StringBuilder builder = new StringBuilder();
        for (String value : contactInfo.getPhone()) {
            builder.append(value);
            builder.append("\n");
            count++;
        }
        String text = builder.toString();
        if(count>1) {
            v.phone.setText( count +" Numbers");
        }
        else {
            v.phone.setText(text);
        }
        try {
            if (contactInfo.getThumb() != null) {
                //.imageView.setImageBitmap(data.getThumb());
                Glide.with(v.imageView.getContext())
                        .load(contactInfo.getThumb())
                        .into(v.imageView);
            } else {
                v.imageView.setImageResource(R.drawable.image);
            }
        } catch (OutOfMemoryError e) {
            v.imageView.setImageDrawable(this.context.getDrawable(R.drawable.image));
            e.printStackTrace();
        }
        view.setTag(contactInfo);
        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView title, phone;
    }
}
