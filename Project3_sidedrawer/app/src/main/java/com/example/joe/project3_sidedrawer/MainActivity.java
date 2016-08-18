package com.example.joe.project3_sidedrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private int id1;
    private MainFragment mainFragment = new MainFragment();
    private GalleryFragment galleryFragment = new GalleryFragment();
    private ContactsFragment contactsFragment = new ContactsFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();

    public enum userChoice {
        CAMERA,
        GALLERY,
        CONTACTS
    }
    private userChoice choice;

    private void createNewFrag(Fragment fragment) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            createNewFrag(mainFragment);
            Log.d(TAG, "Fragment Main created in onCreate");
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final int id = item.getItemId();
        id1 = id;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                if (id == R.id.nav_camera && choice != userChoice.CAMERA) {
                    choice = userChoice.CAMERA;
                    createNewFrag(mainFragment);
                    Log.d(TAG, "Fragment Main created in \"onNavigationItemSelected\"");

                } else if (id == R.id.nav_gallery && choice != userChoice.GALLERY) {
                    choice = userChoice.GALLERY;
                    createNewFrag(galleryFragment);
                    Log.d(TAG, "Fragment Gallery created in \"onNavigationItemSelected\"");

                } else if (id == R.id.nav_contacts && choice != userChoice.CONTACTS) {
                    choice = userChoice.CONTACTS;
                    createNewFrag(contactsFragment);
                    Log.d(TAG, "Fragment Contacts created in \"onNavigationItemSelected\"");

                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.setDrawerListener(actionBarDrawerToggle);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
