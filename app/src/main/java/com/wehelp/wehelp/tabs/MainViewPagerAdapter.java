package com.wehelp.wehelp.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by temp on 9/15/16.
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {


    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new FragmentTimeline();
            case 1:
                return new FragmentTimeline();
            case 2:
                return new FragmentTimeline();
            default:
                return new FragmentTimeline();
        }

    }

    @Override
    public int getCount() {
        return 0;
    }
}
