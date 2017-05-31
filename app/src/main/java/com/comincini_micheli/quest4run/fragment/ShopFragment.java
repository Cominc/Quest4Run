package com.comincini_micheli.quest4run.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comincini_micheli.quest4run.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment
{
    public ShopFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.shop_tab_layout);


    }
}
