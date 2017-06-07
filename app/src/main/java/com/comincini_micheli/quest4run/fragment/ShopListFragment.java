package com.comincini_micheli.quest4run.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Equipment;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.other.EquipmentShopAdapter;

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
        //TODO perchè ridichiaro db
        DatabaseHandler db = new DatabaseHandler(getContext());
        equipmentList = db.getAllEquipments(equipmentTypeId,false);
        return inflater.inflate(R.layout.fragment_shop_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHandler(getContext());

        //equipmentList = db.getAllEquipments(equipmentTypeId,false);

        list = (ListView) getView().findViewById(R.id.list_equipment_shop);


        // Getting adapter by passing xml data ArrayList
        adapter = new EquipmentShopAdapter(getActivity(), equipmentList, db);
        list.setAdapter(adapter);
    }
}
