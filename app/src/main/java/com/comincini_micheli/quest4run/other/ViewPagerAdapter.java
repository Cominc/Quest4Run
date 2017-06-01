package com.comincini_micheli.quest4run.other;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniele on 01/06/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> fragmentListTitle;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentListTitle = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title)
    {
        fragmentList.add(fragment);
        fragmentListTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitle.get(position);
    }
}
