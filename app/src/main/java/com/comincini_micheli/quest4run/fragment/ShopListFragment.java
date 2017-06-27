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
import android.widget.ListView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Equipment;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.adapter.EquipmentShopAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopListFragment extends Fragment {
    private int equipmentTypeId;
    ListView list;
    EquipmentShopAdapter adapter;
    List<Equipment> equipmentList;
    DatabaseHandler db;

    float x1,x2;
    int width;

    public ShopListFragment() {
        // Required empty public constructor
    }

    public int getEquipmentTypeId() {
        return equipmentTypeId;
    }

    public void setEquipmentTypeId(int equipmentTypeId) {
        this.equipmentTypeId = equipmentTypeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = new DatabaseHandler(getContext());
        equipmentList = db.getAllEquipments(equipmentTypeId,false);
        return inflater.inflate(R.layout.fragment_shop_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHandler(getContext());

        //equipmentList = db.getAllEquipments(equipmentTypeId,false);

        list = (ListView) getView().findViewById(R.id.list_equipment_shop);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width =(int) Math.floor(metrics.widthPixels/Constants.PERCENT_SCREEN_SWIPE);

        list.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
//                        Log.w("down list",x1+"");
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
//                        Log.w("up list",x1 + ", " +x2+"");
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

        // Getting adapter by passing xml data ArrayList
        adapter = new EquipmentShopAdapter(getActivity(), equipmentList, db);
        list.setAdapter(adapter);
        list.setEmptyView(getActivity().findViewById(R.id.empty_list));
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
}
