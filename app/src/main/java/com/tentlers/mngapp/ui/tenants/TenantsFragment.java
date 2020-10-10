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
                if (tenantNameHouseRooms != null && tenantNameHouseRooms.size() != 0) {
                    listBinding.recycleViewTenants.setVisibility(View.VISIBLE);
                    listBinding.tenantsListEmptyView.getRoot().setVisibility(View.GONE);
                    adapter.setTenantList(tenantNameHouseRooms);
                } else {
                    listBinding.recycleViewTenants.setVisibility(View.GONE);
                    listBinding.tenantsListEmptyView.getRoot().setVisibility(View.VISIBLE);
                    /*TODO: make add person diabled*/
                    listBinding.tenantsListEmptyView.emptyViewImageNotAvailable.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_person_add_disabled_24px));
                    listBinding.tenantsListEmptyView.emptyViewDataNotAvailable.setText(getString(R.string.no_active_tenant_found));
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
}