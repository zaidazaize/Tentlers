package com.tentlers.mngapp.ui.tenants.specifictenant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.bills.BillEntryTypeObject;
import com.tentlers.mngapp.data.tables.meters.MetersListObj;
import com.tentlers.mngapp.data.tables.tenants.TenantsPersonal;
import com.tentlers.mngapp.databinding.FragmentSpecificTenantBinding;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


public class SpecificTenantFragment extends Fragment {

    FragmentSpecificTenantBinding tenantBinding;
    HouseViewModal viewModal;
    TenantsPersonal choosenTenant;
    MetersListObj allotedRoom;
    boolean personalDescVisibility, paymentSchemeVisibility;

    public SpecificTenantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tenantBinding = FragmentSpecificTenantBinding.inflate(getLayoutInflater(), container, false);

        /*Fetch all the data of chosen tenant*/
        viewModal.getTenantFromId(viewModal.getTenantIdForSpecificTenant()).observe(getViewLifecycleOwner(), new Observer<TenantsPersonal>() {
            @Override
            public void onChanged(TenantsPersonal tenantsPersonal) {
                if (tenantsPersonal == null) {
                    return;
                }
                choosenTenant = tenantsPersonal;
                setTenantData();

                /*Get the room and house data.*/
                if (tenantsPersonal.isRoomAlloted) {
                    viewModal.getHouseRoomNameFromRoomId(tenantsPersonal.roomId).observe(getViewLifecycleOwner(), new Observer<MetersListObj>() {
                        @Override
                        public void onChanged(MetersListObj metersListObj) {
                            if (metersListObj == null) {
                                return;
                            }
                            allotedRoom = metersListObj;
                            setRoomInfo();
                        }
                    });
                } else {
                    tenantBinding.specificTenantLinearLayoutAllotedRoom.setVisibility(View.GONE);
                    tenantBinding.specificTenantShowRoomNotAlloted.setVisibility(View.VISIBLE);
                }
            }
        });

        /*Handle the personal info layout visibility*/
        tenantBinding.specificTenantRelativeLayoutPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personalDescVisibility) {/*if it is visible .Make the layout insvisible*/
                    personalDescVisibility = false;
                    tenantBinding.specificTenantRelativeLayoutPersonalInfoDesc.setVisibility(View.GONE);
                    tenantBinding.specificTenantShowImagePersonalInfoDesc.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_expand_more_24));
                } else {
                    personalDescVisibility = true;
                    tenantBinding.specificTenantRelativeLayoutPersonalInfoDesc.setVisibility(View.VISIBLE);
                    tenantBinding.specificTenantShowImagePersonalInfoDesc.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_expand_less_24));
                }
            }
        });

        tenantBinding.specificTenantRelativeLayoutPaymentScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tenantBinding.specificTenantImagePaymentSchemeShowMore.setImageDrawable(
                        !paymentSchemeVisibility ? ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_expand_less_24) :
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_expand_more_24));

                tenantBinding.specificTenantRelativeLayoutPaymentSchemeDesc.setVisibility(
                        paymentSchemeVisibility ? View.GONE : View.VISIBLE);
                paymentSchemeVisibility = !paymentSchemeVisibility;


            }
        });

        /*set the fab click listener*/
        /*handle the edit tenant button*/
        tenantBinding.specificTenantFabEditTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choosenTenant != null) {
                    viewModal.setTenantForEdit(choosenTenant);
                    Navigation.findNavController(tenantBinding.getRoot()).navigate(R.id.action_nav_specificTenantFragment_to_tenantEditFragment);
                }
            }
        });
        /*handle the adding of create bill*/
        tenantBinding.specificTenantFabCreateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choosenTenant != null && choosenTenant.isRoomAlloted) {
                    viewModal.setBillEntryType(new BillEntryTypeObject().setTenantId(choosenTenant.tenantId));/*set the tenant id for creating  bill.*/
                    Navigation.findNavController(tenantBinding.getRoot()).navigate(R.id.action_global_nav_billEntryFragment);
                } else {
                    Snackbar.make(tenantBinding.specificTenantCoordinatorLayout, getString(R.string.oops_no_room_alloted), BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                }
            }
        });
        return tenantBinding.getRoot();
    }

    private void setTenantData() {
        /*Null is handled in the observer*/

        /*Set the teanant name and create date*/
        tenantBinding.specificTenantTextviewTenantName.setText(choosenTenant.getTenantName());
        tenantBinding.specificTenantTenantEntryDate.setText(TenantsPersonal.getTenantDate(choosenTenant.getCreateDate()));

        /*Set personal data age,gender,family members*/
        tenantBinding.specificTenantTextviewAge.setText(
                choosenTenant.getAge() == 0 ? getString(R.string.not_provided) : String.valueOf(choosenTenant.getAge()));

        tenantBinding.specificTenantTextviewGender.setText(choosenTenant.getGender(requireContext()));

        tenantBinding.specificTenantTextviewFamilyMembers.setText(
                choosenTenant.familyMembers == 0 ? getString(R.string.not_provided) : String.valueOf(choosenTenant.familyMembers));

        /*TODO:set the phone no here*/

        /*house and room info is updated in the second observer*/

        /*Update the payment scheme here*/
        /*TODO:change the currency sign as per the state*/

        /*monthly charge*/
        tenantBinding.specificTenantTextviewMonthlyCharge.setText(
                choosenTenant.mFixedCharges == 0 ? getString(R.string.not_provided) : String.valueOf(choosenTenant.mFixedCharges));

        /*set the electric charge*/
        /*if not enabled show that charge mode is not enabled*/
        if (choosenTenant.isElectricChargeEnabled()) {
            /*set the image icon for meter pay*/
            tenantBinding.specificTenantShowImageElecticityChargeMode.setImageDrawable(
                    choosenTenant.meterPay ? ContextCompat.getDrawable(requireContext(), R.drawable.ic_system_decide_24px) :
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_manually_enter_24px));

            /*set the text for electric pay mode*/
            tenantBinding.specificTenantTextviewElectricChargeMode.setText(
                    choosenTenant.meterPay ? getString(R.string.metered_charge) : getString(R.string.manually_charged));
        } else {
            tenantBinding.specificTenantTextviewElectricChargeMode.setText(getString(R.string.not_provided));
            tenantBinding.specificTenantShowImageElecticityChargeMode.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_sentiment_dissatisfied_24px));
        }

    }

    private void setRoomInfo() {
        /*null is checked in the calling observer.*/
        tenantBinding.specificTenantTextviewHosueName.setText(allotedRoom.houseName);
        tenantBinding.specificTenantTextviewRoomName.setText(allotedRoom.roomName);
    }

}