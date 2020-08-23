package com.example.easelife.ui.tenants;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easelife.R;
import com.example.easelife.data.HouseViewModal;
import com.example.easelife.data.tables.tenants.TenantNameHouseRoom;
import com.example.easelife.databinding.FragmentTenantsListBinding;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TenantsFragment extends Fragment {
    /* Binding object for layout.*/
    FragmentTenantsListBinding listBinding ;
    HouseViewModal viewModal ;

    public TenantsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listBinding = FragmentTenantsListBinding.inflate(getLayoutInflater(), container, false);
        viewModal = new ViewModelProvider(this).get(HouseViewModal.class);


        final MyTenantsRecyclerViewAdapter adapter = new MyTenantsRecyclerViewAdapter();
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
}