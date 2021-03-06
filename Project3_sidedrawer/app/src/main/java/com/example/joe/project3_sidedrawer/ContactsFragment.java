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

    private static final String TAG = ContactsFragment.class.getSimpleName();
    private ArrayList<ContactInfo> contactInfos;
    private int position = -1;
    private Dialog dialog;
    private ListView listView;
    private SelectUserAdapter adapter;
    private boolean isActive;

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
        listView = (ListView) rootView.findViewById(R.id.contacts_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                displayContactInfoDialog();
            }
        });
        adapter = new SelectUserAdapter(contactInfos, getActivity());
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
        Log.d(TAG, "now in onstart!!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "now in onresume!!");
        contactInfos.clear();
        LoadContactAsyncTask loadContactInformation = new LoadContactAsyncTask();
        loadContactInformation.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "now in onpause!!!");
    }

    @Override
    public void onStop() {
        isActive = false;
        if (dialog != null) {
            dialog.dismiss();
        }
        Log.d(TAG, "now in onstop!!");
        super.onStop();
    }

    private class LoadContactAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor contactRecords;
            contactRecords = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            if (contactRecords != null) {
                HashMap<String, ContactInfo> hashMap = new HashMap<>();
                if (contactRecords.getCount() == 0) {
                    Toast.makeText(getActivity(), R.string.Alert_for_no_contacts_found, Toast.LENGTH_LONG).show();
                }
                while (contactRecords.moveToNext()) {
                    String id = contactRecords.getString(contactRecords.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = contactRecords.getString(contactRecords.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = contactRecords.getString(contactRecords.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String imageUri = contactRecords.getString(contactRecords.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                    if (hashMap.containsKey(id)) {
                        hashMap.get(id).numbers.add(phoneNumber);
                    } else {
                        ContactInfo contactInfo = new ContactInfo(name, phoneNumber, imageUri);
                        hashMap.put(id, contactInfo);
                        contactInfos.add(contactInfo);
                    }
                }
            } else {
                contactRecords.close();
                Log.d(TAG, "Cursor is null, cursor closed.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listView.setAdapter(adapter);

            if (position != -1) {
                displayContactInfoDialog();
            }
        }
    }

    private void displayContactInfoDialog() {
        if (position != -1) {
            ContactInfo data = contactInfos.get(position);
            dialog = new Dialog(ContactsFragment.this.getActivity());
            dialog.setContentView(R.layout.alertlayout);
            dialog.setTitle(R.string.contact_detail_title);
            StringBuilder builder = new StringBuilder();
            for (String value : data.getPhoneNumbers()) {
                builder.append(value);
                builder.append("\n");
            }
            String contactDetails = builder.toString();
            TextView text1 = (TextView) dialog.findViewById(R.id.text1);
            text1.setText(data.getName());
            TextView text2 = (TextView) dialog.findViewById(R.id.text2);
            text2.setText(contactDetails);
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (isActive) {
                        position = -1;
                        Log.d(TAG, "Position reset to -1");
                    }
                }
            });
        } else {
            Log.d(TAG, "Position is -1 within displayContactInfoDialog()");
        }
    }

    public class ContactInfo {
        private String name;
        private List<String> numbers;
        private String thumbUri;

        public ContactInfo(String name, String number, String uri) {
            numbers = new ArrayList<>();
            this.name = name;
            this.numbers.add(number);
            this.thumbUri = uri;
        }

        public String getThumbUri() {
            return thumbUri;
        }

        public List<String> getPhoneNumbers() {
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
        Log.d(TAG, "Position saved in \"onSavedInstanceState\"");
        super.onSaveInstanceState(outState);
    }
}



