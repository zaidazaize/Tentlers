package com.example.easelife.ui.bills;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easelife.R;
import com.example.easelife.data.HouseViewModal;
import com.example.easelife.data.tables.bills.BillItemForCard;
import com.example.easelife.databinding.FragmentBillsListBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class BillsFragment extends Fragment {
    HouseViewModal viewModal;
    FragmentBillsListBinding billsListBinding;
    boolean isAnyActiveTenant;

    /*
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BillsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new  ViewModelProvider(requireActivity()).get(HouseViewModal.class);
        viewModal.getIsAnyActivetenant(true).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    isAnyActiveTenant = aBoolean;
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        billsListBinding = FragmentBillsListBinding.inflate(getLayoutInflater(), container, false);
        /*add bill button listener*/
        billsListBinding.floatingActionButtonGenerateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnyActiveTenant) {
                    Navigation.findNavController(v).navigate(R.id.action_nav_bills_to_billEntryFragment);
                } else {
                    Toast.makeText(getContext(), getString(R.string.no_active_tenant_found), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        /* add adapter to the list recycle view*/
        final MyBillsRecyclerViewAdapter adapter = new MyBillsRecyclerViewAdapter(R.drawable.ic_baseline_check_circle_outline_24,
                R.drawable.ic_baseline_error_outline_24,
                R.drawable.ic_baseline_expand_more_24,
                R.drawable.ic_baseline_expand_less_24);

        billsListBinding.recycleViewBills.setLayoutManager(new GridLayoutManager(getContext(),1));
        billsListBinding.recycleViewBills.setAdapter(adapter);

        /* Set the data */
        viewModal.getAllBillForCard(false).observe(getViewLifecycleOwner(),
                new Observer<List<BillItemForCard>>() {
                    @Override
                    public void onChanged(List<BillItemForCard> billItemForCards) {
                        adapter.setBillsList(billItemForCards);
                    }
                });
        return billsListBinding.getRoot();
    }
}