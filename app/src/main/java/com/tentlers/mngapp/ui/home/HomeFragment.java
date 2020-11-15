package com.tentlers.mngapp.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.queryobjects.HouseForHomeFragment;
import com.tentlers.mngapp.databinding.FragmentHomeBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

public class HomeFragment extends Fragment implements HomeRecycleViewAdapter.OnItemClickListener, PopupMenu.OnMenuItemClickListener {

    HomeRecycleViewAdapter homeRecycleViewAdapter;
    private HouseViewModal houseViewModal;
    private FragmentHomeBinding homeBinding;
    private int imageclicked;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

                if (tableHouses != null && tableHouses.size() != 0) {
                    homeRecycleViewAdapter.setHouses(tableHouses);
                    homeBinding.recycleViewHome.setVisibility(View.VISIBLE);
                    homeBinding.homeListEmptyView.getRoot().setVisibility(View.GONE);
                } else {
                    homeBinding.recycleViewHome.setVisibility(View.GONE);
                    homeBinding.homeListEmptyView.getRoot().setVisibility(View.VISIBLE);
                    homeBinding.homeListEmptyView.emptyViewDataNotAvailable.setText(R.string.oh_you_havent_added_any_house);

                    /*set the drawable of no house available*/
                    homeBinding.homeListEmptyView.emptyViewImageNotAvailable.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_domain_disabled_24px));

                    /*show the snack bar above the add house button to add a house */
                    Snackbar snackbar = Snackbar.make(homeBinding.homeCoordinatorLayout, "Click to add new House", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.setAnchorView(homeBinding.floatingActionButtonHome);
                    snackbar.show();
                }
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
        houseViewModal.setHouseIdForSpecificHouse(homeRecycleViewAdapter.getHouseAtPosition(position).houseId);/*set house id to view it in specific house fragment*/
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
        if (item.getItemId() == R.id.homw_fragment_popup_add_rooms) {/*if no of rooms is equal to 99 then show snack bar for max room reached.*/
            if (homeRecycleViewAdapter.getHouseAtPosition(imageclicked).noOfRooms < 99) {
                houseViewModal.setHouseIdForRoomEntry(homeRecycleViewAdapter.getHouseAtPosition(imageclicked).houseId);
                Navigation.findNavController(homeBinding.getRoot()).navigate(R.id.action_global_nav_roomEnteyFragment);
            } else {
                Snackbar.make(homeBinding.homeCoordinatorLayout, R.string.max_room_limit_reached, BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        }
        return true;

    }
}