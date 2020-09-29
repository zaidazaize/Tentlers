package com.tentlers.mngapp.ui.tenants.specifictenant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.meters.MetersListObj;
import com.tentlers.mngapp.data.tables.tenants.TenantsPersonal;
import com.tentlers.mngapp.databinding.FragmentSpecificTenantBinding;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


public class SpecificTenantFragment extends Fragment {

    FragmentSpecificTenantBinding tenantBinding;
    HouseViewModal viewModal;
    TenantsPersonal choosenTenant;
    MetersListObj alloteRoom;
    boolean personalDescVisibility;

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

        /*Fetch all the data of choosen tenant*/
        Log.d("selectedTenant", String.valueOf(viewModal.getTenantIdForSpecificTenant()));
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
                            alloteRoom = metersListObj;
                            if (alloteRoom == null) {
                                return;
                            }
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
        tenantBinding.specificTenantShowImagePersonalInfoDesc.setOnClickListener(new View.OnClickListener() {
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


        return tenantBinding.getRoot();
    }

    private void setTenantData() {
        /*Null is handled in the observer*/

        /*Set the teanant name and create date*/
        tenantBinding.specificTenantTextviewTenantName.setText(choosenTenant.getTenantName());
        tenantBinding.specificTenantTenantEntryDate.setText(choosenTenant.getCreateDate().toString());

        /*Set personal data age,gender,family members*/
        tenantBinding.specificTenantTextviewAge.setText(
                choosenTenant.getAge() == 0 ? getString(R.string.not_provided) : String.valueOf(choosenTenant.getAge()));

        tenantBinding.specificTenantTextviewGender.setText(choosenTenant.getGender());

        tenantBinding.specificTenantTextviewFamilyMembers.setText(
                choosenTenant.familyMembers == 0 ? getString(R.string.not_provided) : String.valueOf(choosenTenant.familyMembers));

        /*TODO:set the phone no here*/

        /*house and room info is updated in the second observer*/

        /*Update the payment scheme here*/
        /*TODO:change the currency sign as per the state*/

        /*monthly charge*/
        tenantBinding.specificTenantTextviewMonthlyCharge.setText(
                choosenTenant.mFixedCharges == 0 ? "" : String.valueOf(choosenTenant.mFixedCharges));

        /*set the electric charge*/
        /*if not enabled show that charge mode is not enabled*/
        if (choosenTenant.isElectricChageEnabled()) {
            /*set the image icon for meter pay*/
            tenantBinding.specificTenantShowImageElecticityChargeMode.setImageDrawable(
                    choosenTenant.meterPay ? ContextCompat.getDrawable(requireContext(), R.drawable.ic_system_decide_24px) :
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_manually_enter_24px));

            /*set the text for electric pay mode*/
            tenantBinding.specificTenantTextviewElectricChargeMode.setText(
                    choosenTenant.meterPay ? "Metered Charge" : "Manually Charged");
        } else {
            tenantBinding.specificTenantTextviewElectricChargeMode.setText(getString(R.string.not_provided));
            tenantBinding.specificTenantShowImageElecticityChargeMode.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_about_24px));
        }

    }

    private void setRoomInfo() {
        /*null is checked in the calling observer.*/
        tenantBinding.specificTenantTextviewHosueName.setText(alloteRoom.houseName);
        tenantBinding.specificTenantTextviewRoomName.setText(alloteRoom.roomName);
    }
}