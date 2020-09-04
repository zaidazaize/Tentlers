package com.example.easelife.ui.home;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.easelife.R;
import com.example.easelife.data.HouseViewModal;
import com.example.easelife.data.tables.TableHouse;
import com.example.easelife.data.tables.queryobjects.HouseForHomeFragment;
import com.example.easelife.databinding.FragmentHomeBinding;
import com.example.easelife.ui.home.specifichouse.GetDeleteRoomDialog;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

public class HomeFragment extends Fragment implements HomeRecycleViewAdapter.OnItemClickListener, PopupMenu.OnMenuItemClickListener {

    private HouseViewModal houseViewModal;
    private FragmentHomeBinding homeBinding;
    HomeRecycleViewAdapter homeRecycleViewAdapter;
    private int imageclicked;

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

        homeRecycleViewAdapter = new HomeRecycleViewAdapter(getContext(), this);
        homeBinding.recycleViewHome.setLayoutManager(
                new LinearLayoutManager(getContext()));

        homeBinding.recycleViewHome.setAdapter(homeRecycleViewAdapter);

        houseViewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);
        houseViewModal.getAllHouseForHomeFragment().observe(getViewLifecycleOwner(), new Observer<List<HouseForHomeFragment>>() {

            @Override
            public void onChanged(List<HouseForHomeFragment> tableHouses) {
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
    public void onItemClicked(int position, View v) {
        houseViewModal.setmShowHouse(homeRecycleViewAdapter.getHouseAtPosition(position));
        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_specificHouseFragment);
    }

    @Override
    public void onImageCliked(int position, View view) {
        imageclicked = position;
        showPopupMenu(view);
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.home_fragment_popup_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homw_fragment_popup_add_rooms:
                houseViewModal.setHouseIdForRoomEntry(homeRecycleViewAdapter.getHouseAtPosition(imageclicked).houseId);
                Navigation.findNavController(homeBinding.getRoot()).navigate(R.id.action_global_nav_roomEnteyFragment);
                return true;

            case R.id.home_fragment_delete_House:
                new GetDeleteRoomDialog().getdeleteRoomDilog(requireContext(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                houseViewModal.deleteHosue(new TableHouse()/*set the house id and delete housebyid to true so that repository can process the further information.*/
                                        .setHouseId(homeRecycleViewAdapter.getHouseAtPosition(imageclicked).houseId)
                                        .setDeleteHouseByid(true));
                            }
                        })
                        .show();
                return true;
            default:
                return false;
        }

    }
}