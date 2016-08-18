package com.example.joe.bigner_frags;

import android.support.v4.app.Fragment;

/**
 * Created by joe on 8/17/2016.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
