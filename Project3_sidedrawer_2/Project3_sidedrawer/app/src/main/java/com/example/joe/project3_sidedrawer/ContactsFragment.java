package com.example.joe.project3_sidedrawer;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsFragment extends Fragment {
    ArrayList<ContactInfo> contactInfos;
    private int position = -1;
    Dialog dialog;
    ListView listView;
    Cursor phones;
    ContentResolver resolver;
    SelectUserAdapter adapter;

    public ContactsFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactInfos = new ArrayList<ContactInfo>();
        resolver = this.getActivity().getContentResolver();
        listView = (ListView) rootView.findViewById(R.id.contacts_list);
        phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadContact loadContact = new LoadContact();
        loadContact.execute();
    }

    @Override
    public void onStop() {
        if (dialog != null) {
            dialog.dismiss();
        }
        super.onStop();
    }

    class LoadContact extends AsyncTask<Void, Void, Void> {

        HashMap<String, ContactInfo> hashMap = new HashMap<>();

        @Override
        protected Void doInBackground(Void... voids) {
            if (phones != null) {
                if (phones.getCount() == 0) {
                    Toast.makeText(getActivity(), "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }
                while (phones.moveToNext()) {
                    String bit_thumb = null;
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                    try {
                        if (image_thumb != null) {
                            bit_thumb = image_thumb;
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (hashMap.containsKey(id)) {
                        hashMap.get(id).numbers.add(phoneNumber);
                    } else {
                        ContactInfo contactInfo = new ContactInfo(name, phoneNumber, bit_thumb);
                        hashMap.put(id, contactInfo);
                        contactInfos.add(contactInfo);
                    }
                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new SelectUserAdapter(contactInfos, getActivity());
            listView.setAdapter(adapter);

            if (position != -1) {
                alertWindow();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        position = -1;
                    }
                });
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    position = i;
                   alertWindow();
                }
            });
        }

        private void alertWindow() {
            ContactInfo data = contactInfos.get(position);
            dialog = new Dialog(ContactsFragment.this.getActivity());
            dialog.setContentView(R.layout.alertlayout);
            dialog.setTitle("Contact Information");
            StringBuilder builder = new StringBuilder();
            for (String value : data.getPhone()) {
                builder.append(value);
                builder.append("\n");
            }
            String contactDetails = builder.toString();
            TextView text1 = (TextView) dialog.findViewById(R.id.text1);
            text1.setText(data.getName());
            TextView text2 = (TextView) dialog.findViewById(R.id.text2);
            text2.setText(contactDetails);
            dialog.show();
        }
    }

    class ContactInfo {
        private String name;
        private List<String> numbers;
        String thumb;

        public ContactInfo(String name, String number, String thumb) {
            numbers = new ArrayList<>();
            this.name = name;
            this.numbers.add(number);
            this.thumb = thumb;
        }

        public String getThumb() {
            return thumb;
        }

        public List<String> getPhone() {
            return numbers;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("position", position);
        super.onSaveInstanceState(outState);
    }
}



