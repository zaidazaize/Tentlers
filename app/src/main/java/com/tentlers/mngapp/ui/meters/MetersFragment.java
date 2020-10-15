package com.tentlers.mngapp.ui.meters;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.meters.GetLastMeterReading;
import com.tentlers.mngapp.data.tables.meters.MetersListObj;
import com.tentlers.mngapp.databinding.FragmentMeterListBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * A fragment representing a list of Items.
 */
public class MetersFragment extends Fragment {
    FragmentMeterListBinding meterListBinding;
    HouseViewModal houseViewModal;
    private MetersListObj chossenMeter;

    public MetersFragment() {
    }

    public static MetersFragment newInstance(int columnCount) {
        return new MetersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        houseViewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);
        chossenMeter = houseViewModal.getMetersListObj();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        meterListBinding = FragmentMeterListBinding.inflate(getLayoutInflater(), container, false);

        /*set room name if meter is attached to room else set only house name*/
        if (!chossenMeter.isOnlyHouse) {
            meterListBinding.meterRoomName.setText(chossenMeter.roomName);

            /*set the house name after fetching it from the table*/
            houseViewModal.getHouseNameFromHouseId(chossenMeter.houseId).observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    meterListBinding.meterHouseName.setText(s);
                }
            });
        } else meterListBinding.meterHouseName.setText(chossenMeter.houseName);

        /*fetch the meter no and set it as the title of the toolbar*/
        houseViewModal.getMeterNoFromMeterId(new GetLastMeterReading().setMeterId(chossenMeter.meterId)).observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                meterListBinding.meterToolbar.setTitle(String.valueOf(aLong));
            }
        });

        final MetersRecyclerViewAdapter metersRecyclerViewAdapter = new MetersRecyclerViewAdapter();

        meterListBinding.meterRecycleView.setAdapter(metersRecyclerViewAdapter);

        /* Add all the meters reading.*/
        houseViewModal.getAllMeterReadings(chossenMeter.meterId).observe(getViewLifecycleOwner(), new Observer<List<AllMetersData>>() {
            @Override
            public void onChanged(List<AllMetersData> allMetersData) {
                metersRecyclerViewAdapter.setAllMetersList(allMetersData);
            }
        });
        return meterListBinding.getRoot();
    }
}