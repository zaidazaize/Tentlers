package com.tentlers.mngapp.ui.tenants;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.FilterObj;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameAndId;
import com.tentlers.mngapp.data.tables.tenants.TenantNameHouseRoom;
import com.tentlers.mngapp.databinding.DualFilterListBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * A fragment representing a list of Items.
 */
public class TenantsFragment extends Fragment implements MyTenantsRecyclerViewAdapter.OnTenantClickListener {
    /* Binding object for layout.*/
    DualFilterListBinding listBinding;
    HouseViewModal viewModal;
    MyTenantsRecyclerViewAdapter adapter;
    FilterObj filterObj;
    private boolean isHouseGrpChip;
    List<HouseNameAndId> allHouseNameAndId;

    public TenantsFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listBinding = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listBinding =DualFilterListBinding.inflate(getLayoutInflater(), container, false);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        /*filter object for handling the filters choosen by the user. */
        filterObj = new FilterObj(FilterObj.TABLE_TENANT);

        /*update the list holding all the house names*/
        viewModal.getAllHouseNameAndId().observe(getViewLifecycleOwner(), new Observer<List<HouseNameAndId>>() {
            @Override
            public void onChanged(List<HouseNameAndId> houseNameAndIds) {
                allHouseNameAndId = houseNameAndIds;
            }
        });

        /*add icon to the floating action button*/
        listBinding.dualFilterAddFab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_add_tenant));

        /*add click listener for adding new tenant it takes user to the add tenant fragment*/
        listBinding.dualFilterAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(listBinding.getRoot()).navigate(R.id.action_nav_tenants_to_tenantEntryFragment);
            }
        });


        adapter = new MyTenantsRecyclerViewAdapter(this, viewModal);
        listBinding.dualFilterRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        listBinding.dualFilterRecycleView.setAdapter(adapter);
        viewModal.getTenantForTenantList()
                .observe(getViewLifecycleOwner(), new Observer<List<TenantNameHouseRoom>>() {
                    @Override
                    public void onChanged(List<TenantNameHouseRoom> tenantNameHouseRooms) {
                        if (tenantNameHouseRooms != null && tenantNameHouseRooms.size() != 0) {
                            listBinding.dualFilterRecycleView.setVisibility(View.VISIBLE);
                            listBinding.dualFilterListEmptyView.getRoot().setVisibility(View.GONE);
                            adapter.setTenantList(tenantNameHouseRooms);
                        } else {
                            listBinding.dualFilterRecycleView.setVisibility(View.GONE);
                            listBinding.dualFilterListEmptyView.getRoot().setVisibility(View.VISIBLE);
                            listBinding.dualFilterListEmptyView.emptyViewImageNotAvailable.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_person_add_disabled_24px));
                            listBinding.dualFilterListEmptyView.emptyViewDataNotAvailable.setText(getString(R.string.no_active_tenant_found));
                        }
                    }
                });

        /*update the filter object with the primary filter of the tenants so that filtered data can be shown.*/
        viewModal.setTenantFilterObj(filterObj.getPrimarySearchForTenant());

        /*this shows the choice on the for adding the filter on tenants data*/
        String[] choice = getResources().getStringArray(R.array.tenant_filter);
        for (int i = 0; i < choice.length; i++) {
            Chip chip = new Chip(requireContext());
            chip.setText(choice[i]);
            chip.setClickable(true);
            chip.setCheckable(true);
            chip.setId(i+1);
            /*TODO: add the theme for the chips*/
            if (choice[i].equals(choice[3])) {/*set the "alloted tenant" filter button checked*/
                chip.setChecked(true);
            }
            listBinding.chipGroup1.addView(chip);
        }

        listBinding.chipGroup1.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {/*the checked id is the int position in the view hirachy starting from 1*/
                /*make the chip group 2 invisible each time when new chip is clicked.*/
                if (listBinding.chipGroup2.getVisibility() != View.GONE) {
                    /*TODO add animation for invisibility */
                    listBinding.chipGroup2.setVisibility(View.GONE);
                }
                Log.d("chipid", String.valueOf(checkedId));
                switch (checkedId) {
                    case 1:/*sort by*/
                        isHouseGrpChip = false;
                        showChipGroup2();
                        break;
                    case 2:/*All*/
                        viewModal.setTenantFilterObj(filterObj.setFilterField(FilterObj.TYPE_ALL));
                        break;
                    case 3:/*house*/
                        isHouseGrpChip = true;
                        showChipGroup2();
                        break;
                    case 4:/*with room alloted*/
                        viewModal.setTenantFilterObj(filterObj.setFilterField(FilterObj.TYPE_ROOM_ALLOTED));
                        break;
                    case 5:/*With room not alloted*/
                        viewModal.setTenantFilterObj(filterObj.setFilterField(FilterObj.TYPE_ROOM_UNALLOTED));
                        break;
                    case 6:/*payment left*/
                        viewModal.setTenantFilterObj(filterObj.setFilterField(FilterObj.TYPE_PAYMENT_LEFT));
                        break;
                    case 7:/*payment completer*/
                        viewModal.setTenantFilterObj(filterObj.setFilterField(FilterObj.TYPE_PAYMENT_COMPLETE));
                        break;
                }
            }
        });

        /*listener handling sorting order and the tenants specific to the  house*/
        listBinding.chipGroup2.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Log.d("chipid", String.valueOf(checkedId));
                if (checkedId == View.NO_ID) {/*if nothing selected  then return */
                    return;
                }

                if (isHouseGrpChip) {/*the chip is selected for viewing tenants specific to house.*/
                    Log.d("houseid", String.valueOf(allHouseNameAndId.get(checkedId - 1).houseId));
                    viewModal.setTenantFilterObj(filterObj.setHouseId(allHouseNameAndId.get(checkedId-1).houseId));

                } else {/*chip is selected for implementing sorting*/
                    switch (checkedId) {
                        case 1:/*earliest first*/
                           filterObj.setOnlyFilterOrder(FilterObj.TYPE_EARLIEST_FIRST);
                            break;
                        case 2:/*earliest last*/
                            filterObj.setOnlyFilterOrder(FilterObj.TYPE_EARLIEST_LAST);
                            break;
                        case 3:/*lowest first*/
                            filterObj.setOnlyFilterOrder(FilterObj.TYPE_AMT_LOWEST_FIRST);
                            break;
                        case 4:/*lowest last*/
                            filterObj.setOnlyFilterOrder(FilterObj.TYPE_AMT_LOWEST_LAST);
                            break;
                        case 5:/*lh_totalbills*/
                            filterObj.setOnlyFilterOrder(FilterObj.TYPE_LH_TOTAL_BILLS);
                            break;
                        case 6:/*hl_totalbills*/
                            filterObj.setOnlyFilterOrder(FilterObj.TYPE_HL_TOTAL_BILLS);
                            break;
                    }
                    /*udate the filter object*/
                    viewModal.setTenantFilterObj(filterObj);
                }
            }
        });
        return listBinding.getRoot();
    }

    @Override
    public void onTenantClicked(View v, int position) {
        viewModal.setTenantIdForSpecificTenant((adapter.getItemAtPosition(position)).tenantId);
        Log.d("selectedTenant", String.valueOf(viewModal.getTenantIdForSpecificTenant()));
        Navigation.findNavController(listBinding.getRoot()).navigate(R.id.action_nav_tenants_to_specificTenantFragment);
    }

    /*it handles the showing of the chipgroup 2  based on the user selection */
    private void showChipGroup2() {
        listBinding.chipGroup2.setVisibility(View.VISIBLE);
        listBinding.chipGroup2.removeAllViews();
        String[] choice;
        if (isHouseGrpChip) {/*TODO: request house names at runtime instead of making it preloaded available in the memory*/
            choice = getHouseNameArray();
            if (choice == null) {/*if no house is available then show snack bar that no house is available*/
                Toast.makeText(requireContext(),"No House is Available",Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            choice = getResources().getStringArray(R.array.filter_orders_tenants);
        }
        int y=1;
        for (String i : choice) {
            Chip chip = new Chip(requireContext());
            chip.setText(i);
            chip.setClickable(true);
            chip.setCheckable(true);
            chip.setId(y);
            /*TODO: add the theme for the chips*/
            if (!isHouseGrpChip && i.equals(choice[0])) {/*set the "earliest first" button checked if house is not checked filter button checked*/
                chip.setChecked(true);
            }
            chip.setTag(i);
            listBinding.chipGroup2.addView(chip);
            y++;
        }
    }

    /*returns the array containing names of the all the houses*/
    private String[] getHouseNameArray() {
        if (allHouseNameAndId != null) {
            String[] housenames = new String[allHouseNameAndId.size()];/*create a array whose size is equal to the no of the houses*/
            for (int i = 0; i < allHouseNameAndId.size(); i++) {
                housenames[i] = allHouseNameAndId.get(i).houseName;
            }
            return housenames;
        } else return null;
    }
}