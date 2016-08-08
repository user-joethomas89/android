package com.example.joe.project3_sidedrawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SelectUserAdapter extends BaseAdapter {
    private List<ContactsFragment.ContactInfo> data;
    Context context;

    public SelectUserAdapter(List<ContactsFragment.ContactInfo> contactInfos, Context contxt) {
        data = contactInfos;
        context = contxt;
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

        ViewHolder v = new ViewHolder();
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
        if (count > 1) {
            String displayText;
            displayText = String.valueOf(count) + " " + context.getString(R.string.print_numbers_on_screen);
            v.phone.setText(displayText);
        } else {
            v.phone.setText(text);
        }

        if (contactInfo.getThumbUri() != null) {
            Glide.with(v.imageView.getContext())
                    .load(contactInfo.getThumbUri())
                    .into(v.imageView);
        } else {
            v.imageView.setImageResource(R.drawable.image);
        }

        view.setTag(contactInfo);
        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView title, phone;
    }
}
