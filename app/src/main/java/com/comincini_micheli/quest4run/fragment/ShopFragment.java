package com.comincini_micheli.quest4run.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.other.Constants;

public class ShopFragment extends Fragment
{
    private float x1 = 0;
    private float x2 = 0;
    private int width;

    public ShopFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width =(int) Math.floor(metrics.widthPixels/Constants.PERCENT_SCREEN_SWIPE);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
//                        Log.w("down",x1+"");
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
//                        Log.w("up",x1 + ", " +x2+"");
                        float deltaX = x2 - x1;
                        if (deltaX < -width)
                        {
                            TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.shop_tab_layout);
                            int pos = tabLayout.getSelectedTabPosition();
                            int newPos = -1;
                            switch (pos)
                            {
                                case 0:
                                    newPos = 1;
                                    break;
                                case 1:
                                    newPos = 2;
                                    break;
                                case 2:
                                    break;
                                default:
                                    break;
                            }
                            if(newPos > -1)
                            {
                                changeTab(tabLayout, newPos);
                            }
                        }
                        else if(deltaX > width)
                        {
                            TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.shop_tab_layout);
                            int pos = tabLayout.getSelectedTabPosition();
                            int newPos = -1;
                            switch (pos)
                            {
                                case 0:
                                    break;
                                case 1:
                                    newPos = 0;
                                    break;
                                case 2:
                                    newPos = 1;
                                    break;
                                default:
                                    break;
                            }
                            if(newPos > -1)
                            {
                                changeTab(tabLayout, newPos);
                            }
                        }
                        break;
                }
                return true;
            }
        });
        return view;
    }

    private void changeTab(TabLayout tabLayout, int newPos)
    {
        TabLayout.Tab tab = tabLayout.getTabAt(newPos);
        tab.select();
        ShopListFragment shopListFragment = new ShopListFragment();
        shopListFragment.setEquipmentTypeId(tab.getPosition());
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_shop_container, shopListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ShopListFragment shopListFragment = new ShopListFragment();
        shopListFragment.setEquipmentTypeId(Constants.ID_TYPE_ATTACK);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_shop_container, shopListFragment);
        fragmentTransaction.commit();
        //-----------------------------------------------------
        /*DatabaseHandler db = new DatabaseHandler(getContext());
        Equipment e_A = new Equipment("Attacco",0,5,4,3,50,"icon_A");
        Equipment e_A2 = new Equipment("Attacco3",0,5,4,3,70,"icon_A3");
        Equipment e_D = new Equipment("Difesa",1,5,4,3,20,"icon_D");
        Equipment e_M = new Equipment("Magia",2,5,4,3,30,"icon_M");
        Equipment e_B = new Equipment("Attacco2",0,5,4,3,2,"icon_A2");
        e_B.setBought(true);
        db.addEquipment(e_A);
        db.addEquipment(e_A2);
        db.addEquipment(e_D);
        db.addEquipment(e_M);
        db.addEquipment(e_B);*/
        //-----------------------------------------------------
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.shop_tab_layout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ShopListFragment shopListFragment = new ShopListFragment();
                shopListFragment.setEquipmentTypeId(tab.getPosition());
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_shop_container, shopListFragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
