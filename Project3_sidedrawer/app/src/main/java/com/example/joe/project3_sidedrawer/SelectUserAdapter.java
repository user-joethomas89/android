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
    private Context context;

    public SelectUserAdapter(List<ContactsFragment.ContactInfo> contactInfos, Context contxt) {
        data = contactInfos;
        context = contxt;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (data != null) {
            return data.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        final ContactsFragment.ContactInfo contactInfo = (ContactsFragment.ContactInfo) data.get(i);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.fragment_layout, null);
            ViewHolder v = new ViewHolder();
            v.title = (TextView) view.findViewById(R.id.name);
            v.phone = (TextView) view.findViewById(R.id.no);
            v.imageView = (ImageView) view.findViewById(R.id.pic);
            view.setTag(v);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(contactInfo.getName());
        if (contactInfo.getPhoneNumbers().size() > 1) {
            holder.phone.setText(context.getString(R.string.print_numbers_on_screen, contactInfo.getPhoneNumbers().size()));
        } else {
            holder.phone.setText(contactInfo.getPhoneNumbers().toString().replace("[", "").replace("]", ""));
        }
        if (contactInfo.getThumbUri() != null) {
            Glide.with(holder.imageView.getContext())
                    .load(contactInfo.getThumbUri())
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.default_contact_image);
        }
        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView title, phone;
    }
}
