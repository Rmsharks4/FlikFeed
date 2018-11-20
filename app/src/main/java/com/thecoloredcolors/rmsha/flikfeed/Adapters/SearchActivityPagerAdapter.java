package com.thecoloredcolors.rmsha.flikfeed.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.thecoloredcolors.rmsha.flikfeed.Fragments.PeopleFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.TagsFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.TopFragment;

/**
 * Created by rmsha on 11/28/2017.
 */

public class SearchActivityPagerAdapter extends FragmentPagerAdapter {

    private int nooftabs;
    private Fragment top = new TopFragment();
    private Fragment people = new PeopleFragment();
    private Fragment tags = new TagsFragment();

    public SearchActivityPagerAdapter(FragmentManager fm, int nooftabs) {
        super(fm);
        this.nooftabs = nooftabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return top;
            case 1:
                return people;
            case 2:
                return tags;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nooftabs;
    }

    public Fragment getTop() {
        return top;
    }

    public Fragment getPeople() {
        return people;
    }

    public Fragment getTags() {
        return tags;
    }
}
