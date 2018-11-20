package com.thecoloredcolors.rmsha.flikfeed.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thecoloredcolors.rmsha.flikfeed.Fragments.DiscoverFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.HomeScreenFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.NotificationsFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.ProfileFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.UploadFragment;

/**
 * Created by rmsha on 9/29/2017.
 */

public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    private int nooftabs;
    private Fragment home = new HomeScreenFragment();
    private Fragment discover = new DiscoverFragment();
    private Fragment upload = new UploadFragment();
    private Fragment notify = new NotificationsFragment();
    private Fragment profile = new ProfileFragment();

    public MainActivityPagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        nooftabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return home;
            case 1:
                return notify;
            case 2:
                return upload;
            case 3:
                return discover;
            case 4:
                return profile;
            default:
                return null;
        }
    }
    
    @Override
    public int getCount() {
        return nooftabs;
    }
}
