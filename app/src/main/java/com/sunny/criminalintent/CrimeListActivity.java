package com.sunny.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by SUNny on 2016/9/12 0012.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
