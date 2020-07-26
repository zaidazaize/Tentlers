package com.example.easelife.ui.rooms;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import com.example.easelife.R;
import com.example.easelife.data.HouseViewModal;
import com.example.easelife.data.tables.TableRooms;
import com.example.easelife.data.tables.queryobjects.HouseNameId;
import com.example.easelife.databinding.FragmentHouseRoomsListBinding;
import com.example.easelife.databinding.FragmentHouseRoomsListItemBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * A fragment representing a list of Items.
 */
public class HouseRooms extends Fragment {

    FragmentHouseRoomsListBinding bindingRoom;
    HouseViewModal viewModal;
    ArrayList<String> houseNamearray = new ArrayList<>();
    List<HouseNameId> nameIdList;

    public HouseRooms() {
    }


    public static HouseRooms newInstance(int columnCount) {
        HouseRooms fragment = new HouseRooms();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bindingRoom = FragmentHouseRoomsListBinding.inflate(getLayoutInflater(), container, false);


        /* Setting the adapter the spinner*/

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1);
        bindingRoom.spinnerToolbarRooms.setAdapter(arrayAdapter);

        viewModal.getHouseNameIdforRooms().observe(getViewLifecycleOwner(), new Observer<List<HouseNameId>>() {
            @Override
            public void onChanged(List<HouseNameId> houseNameIds) {
                nameIdList = houseNameIds;
                arrayAdapter.addAll(getHouseNamearray());
            }
        });

        final MyroomsRecyclerViewAdapter recyclerViewAdapter = new MyroomsRecyclerViewAdapter(
                requireActivity().getDrawable(R.drawable.ic_baseline_yes_tenant_24),
                requireActivity().getDrawable(R.drawable.ic_baseline_no_tenants_24));

        // Set the adapter to the recycle view
        bindingRoom.recycleViewRooms.setAdapter(recyclerViewAdapter);

        // Add spinner item click listener
        bindingRoom.spinnerToolbarRooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModal.getAllRooms(nameIdList.get(position).houseId)
                        .observe(getViewLifecycleOwner(), new Observer<List<TableRooms>>() {
                    @Override
                    public void onChanged(List<TableRooms> tableRooms) {
                        recyclerViewAdapter.setmRoomList(tableRooms);
                    }
                });
                Log.d("the spinner selected", String.valueOf(nameIdList.get(position).houseId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recyclerViewAdapter.setmRoomList(null);
            }
        });

        return bindingRoom.getRoot();
    }

    private ArrayList<String> getHouseNamearray() {
        if (nameIdList == null || nameIdList.size() ==0) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("no item found");
            return list;
        }

        for( HouseNameId s: nameIdList){
            houseNamearray.add(s.houseName);
        }
        return houseNamearray;
    }
}