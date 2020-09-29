package com.tentlers.mngapp.ui.tenants;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.tenants.TenantNameHouseRoom;
import com.tentlers.mngapp.databinding.FragmentTenantsListBinding;

import java.util.List;

import androidx.annotation.NonNull;
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
    FragmentTenantsListBinding listBinding;
    HouseViewModal viewModal;
    MyTenantsRecyclerViewAdapter adapter;

    public TenantsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listBinding = FragmentTenantsListBinding.inflate(getLayoutInflater(), container, false);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        listBinding.floatingActionButtonAddTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(listBinding.getRoot()).navigate(R.id.action_nav_tenants_to_tenantEntryFragment);
            }
        });


        adapter = new MyTenantsRecyclerViewAdapter(this);
        listBinding.recycleViewTenants.setLayoutManager(new LinearLayoutManager(getContext()));
        listBinding.recycleViewTenants.setAdapter(adapter);

        viewModal.getAllTenantNHR(true).observe(getViewLifecycleOwner(), new Observer<List<TenantNameHouseRoom>>() {
            @Override
            public void onChanged(List<TenantNameHouseRoom> tenantNameHouseRooms) {
                adapter.setTenantList(tenantNameHouseRooms);
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
}