package com.example.easelife.ui.home.specifichouse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easelife.R;
import com.example.easelife.data.HouseViewModal;
import com.example.easelife.data.tables.TableHouse;
import com.example.easelife.data.tables.TableRooms;
import com.example.easelife.databinding.FragmentHouseRoomsListItemBinding;
import com.example.easelife.databinding.FragmentSpecificHouseBinding;

import java.util.List;

public class SpecificHouseFragment extends Fragment {
    FragmentSpecificHouseBinding binding;
    HouseViewModal viewModal;
    TableHouse house;
    List<TableRooms> threeRooms;

    public SpecificHouseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        // Get the house selected.
        house = viewModal.getmShowHouse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSpecificHouseBinding.inflate(getLayoutInflater(), container, false);

        binding.specificHouseName.setText(house.getHouseName());
        binding.specificHouseDate.setText(house.getDate().toString());

        if (house.address != null) {
            String houseno = String.valueOf(house.address.houseNo);
            binding.specificHouseTextviewHouseno.setText(houseno.length() == 0 ? getString(R.string.not_provided) : houseno);

            String loacality = house.address.locality;
            binding.specificHouseLocality.setText(loacality == null ? getString(R.string.not_provided) : loacality);

            String postalcode = house.address.postalcode;
            binding.specificHousePostalcode.setText(postalcode == null ? getString(R.string.not_provided) : postalcode);

            String city = house.address.city;
            binding.specificHouseCity.setText(city == null ? getString(R.string.not_provided) : city);

            String country = house.address.country;
            binding.specificHouseCountry.setText(country == null ? getString(R.string.not_provided) : country);
        } else {
            binding.specificHouseTextviewHouseno.setText(getString(R.string.not_provided));
            binding.specificHouseLocality.setText(getString(R.string.not_provided));
            binding.specificHousePostalcode.setText(getString(R.string.not_provided));
            binding.specificHouseCity.setText(getString(R.string.not_provided));
            binding.specificHouseCountry.setText(getString(R.string.not_provided));
        }

        long meterno = house.meterid;
        binding.specificHouseMeterNo.setText(meterno == 0 ? getString(R.string.not_provided) : String.valueOf(meterno));

        viewModal.getThreeRooms(house.getHouseId()).observe(getViewLifecycleOwner(), new Observer<List<TableRooms>>() {
            @Override
            public void onChanged(List<TableRooms> tableRooms) {
                setThreeRooms();
            }
        });

        return binding.getRoot();
    }

    public void setThreeRooms() {
        if (threeRooms != null) {
            int listsize = threeRooms.size();
            FragmentHouseRoomsListItemBinding[] list = new FragmentHouseRoomsListItemBinding[]{binding.roomItemFirst, binding.roomItemSecond, binding.roomItemThird};
            switch (listsize) {
                case 0:
                    for (int i = 0; i < 3; i++) {
                        setfirstRoom(false, list[i], null);
                    }
                case 1:
                    setfirstRoom(true, list[0], threeRooms.get(0));
                    for (int i = 1; i < 3; i++) {
                        setfirstRoom(false, list[i], null);
                    }
                case 2:
                    for (int i = 0; i < 2; i++) {
                        setfirstRoom(true, list[i], threeRooms.get(i));
                    }
                    setfirstRoom(false, list[2], null);
                case 3:
                    for (int i = 0; i < 3; i++) {
                        setfirstRoom(true, list[i], null);
                    }
            }
        }

    }

    public void setfirstRoom(boolean isSet, FragmentHouseRoomsListItemBinding v, TableRooms rooms) {
        if (isSet) {
            v.roomListItemContainer.setVisibility(View.VISIBLE);
            v.houseRoomListitemRoomNo.setText(String.valueOf(rooms.roomNo));
            v.houseRoomListitemRoomName.setText(rooms.roomName);
            v.houseRoomListitemRoomsDate.setText(rooms.date.toString());
            v.houseRoomListitemRoomTenant.setText(rooms.tenantsName);
            v.houseRoomListitemRoomsTenantStatus.setBackground(rooms.isOcupied ?
                    getResources().getDrawable(R.drawable.ic_baseline_yes_tenant_24)
                    : getResources().getDrawable(R.drawable.ic_baseline_no_tenants_24));
        } else v.roomListItemContainer.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}