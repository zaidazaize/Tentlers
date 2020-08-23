package com.example.easelife.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easelife.R;
import com.example.easelife.data.HouseViewModal;
import com.example.easelife.data.tables.TableHouse;
import com.example.easelife.databinding.FragmentHomeBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

public class HomeFragment extends Fragment implements HomeRecycleViewAdapter.OnItemClickListener {

    private HouseViewModal houseViewModal;
    private FragmentHomeBinding homeBinding;
     HomeRecycleViewAdapter homeRecycleViewAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        homeBinding = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeBinding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        // Inflate the menu items
        setHasOptionsMenu(true);

        // Set the list items

        homeRecycleViewAdapter = new HomeRecycleViewAdapter(getContext(),this);
        homeBinding.recycleViewHome.setLayoutManager(
                new LinearLayoutManager(getContext()));

        homeBinding.recycleViewHome.setAdapter(homeRecycleViewAdapter);

        houseViewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);
        houseViewModal.getAllHouse().observe(getViewLifecycleOwner(), new Observer<List<TableHouse>>() {

            @Override
            public void onChanged(List<TableHouse> tableHouses) {
                if (tableHouses.size() != 0) {/*update the last house id in the view modal so as to auto generate the house name*/
                    houseViewModal.lastEnteredHouseId = (tableHouses.get(tableHouses.size()-1)).houseId;
                }else{ houseViewModal.lastEnteredHouseId = 0;}

                homeRecycleViewAdapter.setHouses(tableHouses);
            }
        });

        // Setting Up Floating action button
        homeBinding.floatingActionButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here to start dialogue
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_houseEntryDilog);
            }
        });
        return homeBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_home, menu);
    }

    @Override
    public void onItemClicked(int position ,View v) {
        houseViewModal.setmShowHouse(homeRecycleViewAdapter.getHouseAtPosition(position));
        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_specificHouseFragment);
    }

}