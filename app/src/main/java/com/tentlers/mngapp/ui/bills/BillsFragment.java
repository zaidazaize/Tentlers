package com.tentlers.mngapp.ui.bills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.bills.BillEntryTypeObject;
import com.tentlers.mngapp.data.tables.bills.BillItemForCard;
import com.tentlers.mngapp.databinding.DualFilterListBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

/*A fragment representing a list of Items.*/
public class BillsFragment extends Fragment implements View.OnClickListener, MyBillsRecyclerViewAdapter.OnBillStatusClickedListener, PopupMenu.OnMenuItemClickListener {
    HouseViewModal viewModal;
    DualFilterListBinding billsListBinding;
    boolean isAnyActiveTenant;
    int billClicked;
    BillItemForCard selectedBill;
    MyBillsRecyclerViewAdapter adapter;

    /*
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BillsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);
        viewModal.getIsAnyActivetenant(true).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    isAnyActiveTenant = aBoolean;
                } else isAnyActiveTenant = false;
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        billsListBinding = DualFilterListBinding.inflate(getLayoutInflater(), container, false);

        /*add drawable to fab*/
        billsListBinding.dualFilterAddFab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_bill_pay_24));

        /*add bill button listener*/
        billsListBinding.dualFilterAddFab.setOnClickListener(this);

        /* add adapter to the list recycle view*/
        adapter = new MyBillsRecyclerViewAdapter(R.drawable.ic_baseline_check_circle_outline_24,
                R.drawable.ic_baseline_error_outline_24,
                R.drawable.ic_baseline_expand_more_24,
                R.drawable.ic_baseline_expand_less_24
                , this);

        billsListBinding.dualFilterRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        billsListBinding.dualFilterRecycleView.setAdapter(adapter);

        /* Set the data */
        viewModal.getAllBillForCard(false).observe(getViewLifecycleOwner(),
                new Observer<List<BillItemForCard>>() {
                    @Override
                    public void onChanged(List<BillItemForCard> billItemForCards) {
                        if (billItemForCards != null && billItemForCards.size() != 0) {
                            adapter.setBillsList(billItemForCards);
                            /*adjust the layout visibility */
                            billsListBinding.dualFilterListEmptyView.getRoot().setVisibility(View.GONE);
                            billsListBinding.dualFilterRecycleView.setVisibility(View.VISIBLE);
                        } else {
                            billsListBinding.dualFilterListEmptyView.getRoot().setVisibility(View.VISIBLE);
                            billsListBinding.dualFilterRecycleView.setVisibility(View.GONE);
                            billsListBinding.dualFilterListEmptyView.emptyViewDataNotAvailable.setText(R.string.sorry_you_have_no_bills_to_collect);
                            billsListBinding.dualFilterListEmptyView.emptyViewImageNotAvailable.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_no_luggage_24px));
                        }
                    }
                });
        return billsListBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        billsListBinding = null;
    }

    @Override
    public void onClick(View v) {
        if (isAnyActiveTenant) {
            /*set the bill entry type*/
            viewModal.setBillEntryType((new BillEntryTypeObject()).setBillNormalPaid(true));
            Navigation.findNavController(v).navigate(R.id.action_nav_bills_to_billEntryFragment);
        } else {
            /*shows snack bar if no tenant is found*/
            Toast.makeText(requireContext(),getString(R.string.no_active_tenant_found),Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_bills, popup.getMenu());
        popup.show();
    }

    @Override
    public void onBillStatusClicked(View v, int position) {
        /*billClicked = position;*/
        selectedBill = adapter.getItemAtPosition(position);
        if (selectedBill.isBillPaid) {/*if the bill status is paid then show the snackbar that bill is paid else show menu to mark it as paid.*/
            Snackbar.make(billsListBinding.dualFilterCoordinatorLayout, R.string.fells_bill_is_already_paid, BaseTransientBottomBar.LENGTH_SHORT)
                    .show();
        } else {
            showPopupMenu(v);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_mark_as_paid) {
            viewModal.updateBillPaidStatus(selectedBill);
        }
        return true;
    }
}