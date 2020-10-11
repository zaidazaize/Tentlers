package com.tentlers.mngapp.ui.tenants.specifictenant;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.tenants.TenantsPersonal;
import com.tentlers.mngapp.databinding.FragmentTenantEditBinding;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


public class TenantEditFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    FragmentTenantEditBinding tenantEditBinding;
    HouseViewModal viewModal;
    TenantsPersonal choosenTenant;

    public TenantEditFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        choosenTenant = viewModal.getTenantForEdit();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tenantEditBinding = FragmentTenantEditBinding.inflate(getLayoutInflater(), container, false);

        /*on cancel button clicked*/
        tenantEditBinding.toolbarEditTenant.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExitDialog().show();
            }
        });

        /*On save button clicked*/
        tenantEditBinding.toolbarEditTenant.setOnMenuItemClickListener(this);

        /*set the name*/
        tenantEditBinding.editTenantTenantName.setText(choosenTenant.getTenantName());

        /*set the age*/
        tenantEditBinding.editTenantAge.setText(choosenTenant.getAge() == 0 ? "" : String.valueOf(choosenTenant.getAge()));

        /*set the gender choosen listener*/
        tenantEditBinding.radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_male:
                        choosenTenant.setGender(TenantsPersonal.MALE);
                        break;
                    case R.id.radioButton_female:
                        choosenTenant.setGender(TenantsPersonal.FEMALE);
                        break;
                    case R.id.radioButton_other:
                        choosenTenant.setGender(TenantsPersonal.OTHER);
                        break;
                    default:
                        choosenTenant.setGender(TenantsPersonal.NOGENDER);
                }
            }
        });

        /*update the familly members*/
        tenantEditBinding.editTenantFamilyMembers
                .setText(choosenTenant.familyMembers == 0 ? "" : String.valueOf(choosenTenant.familyMembers));

        /*update the fixed charge*/
        tenantEditBinding.editTenantMonthlyFixedCharge.setText(choosenTenant.familyMembers == 0 ? "" : String.valueOf(choosenTenant.mFixedCharges));

        return tenantEditBinding.getRoot();
    }

    private boolean isDataValid() {
        String tenantname, age, totalfamily, fixedcharge;

        tenantname = Objects.requireNonNull(tenantEditBinding.editTenantTenantName.getText()).toString();
        age = Objects.requireNonNull(tenantEditBinding.editTenantAge.getText()).toString();
        totalfamily = Objects.requireNonNull(tenantEditBinding.editTenantFamilyMembers.getText()).toString();
        fixedcharge = Objects.requireNonNull(tenantEditBinding.editTenantMonthlyFixedCharge.getText()).toString();
        if (tenantname.length() == 0) {
            tenantEditBinding.editTenantOutlinedTenantName.setError(getString(R.string.error_field_recquired));
            return false;
        }
        choosenTenant.setTenantName(tenantname);
        choosenTenant.setAge(age.length() == 0 ? 0 : Integer.parseInt(age));
        choosenTenant.familyMembers = totalfamily.length() == 0 ? 0 : Integer.parseInt(totalfamily);
        choosenTenant.mFixedCharges = fixedcharge.length() == 0 ? 0 : Float.parseFloat(fixedcharge);

        return true;
    }

    private MaterialAlertDialogBuilder getExitDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        dialogBuilder.setTitle(getString(R.string.exit))
                .setMessage(R.string.sure_to_discard)
                .setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_delete_outline_24))
                .setPositiveButton("Discard Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Navigation.findNavController(tenantEditBinding.getRoot()).navigateUp();
                    }
                })
                .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return dialogBuilder;
    }

    private MaterialAlertDialogBuilder getSaveDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        dialogBuilder.setTitle(getString(R.string.save_changes))
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        viewModal.updateTenant(choosenTenant);
                        Navigation.findNavController(tenantEditBinding.getRoot()).navigateUp();

                    }
                })
                .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return dialogBuilder;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (isDataValid()) {
            getSaveDialog().show();
        }
        return true;
    }
}