package com.comincini_micheli.quest4run.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Equipment;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.other.ViewPagerAdapter;

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
        //-----------------------------------------------------
        DatabaseHandler db = new DatabaseHandler(getContext());
        Equipment e_A = new Equipment("Attacco",0,5,4,3,50,"icon_A");
        Equipment e_A2 = new Equipment("Attacco3",0,5,4,3,70,"icon_A3");
        Equipment e_D = new Equipment("Difesa",1,5,4,3,20,"icon_D");
        Equipment e_M = new Equipment("Magia",2,5,4,3,200,"icon_M");
        Equipment e_B = new Equipment("Attacco2",0,5,4,3,2,"icon_A2");
        e_B.setBought(true);
        db.addEquipment(e_A);
        db.addEquipment(e_A2);
        db.addEquipment(e_D);
        db.addEquipment(e_M);
        db.addEquipment(e_B);
        //-----------------------------------------------------
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.shop_tab_layout);
        ViewPager shopViewPager = (ViewPager) getActivity().findViewById(R.id.shop_view_pager);
        setupViewPager(shopViewPager);
        tabLayout.setupWithViewPager(shopViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        ShopListFragment shopListFragmentAttack = new ShopListFragment();
        shopListFragmentAttack.setEquipmentTypeId(Constants.ID_TYPE_ATTACK);
        adapter.addFragment(shopListFragmentAttack, getResources().getString(R.string.attack_label));
        ShopListFragment shopListFragmenDefense = new ShopListFragment();
        shopListFragmenDefense.setEquipmentTypeId(Constants.ID_TYPE_DEFENSE);
        adapter.addFragment(shopListFragmenDefense,  getResources().getString(R.string.defense_label));
        ShopListFragment shopListFragmentMagic = new ShopListFragment();
        shopListFragmentMagic.setEquipmentTypeId(Constants.ID_TYPE_MAGIC);
        adapter.addFragment(shopListFragmentMagic,  getResources().getString(R.string.magic_label));
        viewPager.setAdapter(adapter);
    }
}
