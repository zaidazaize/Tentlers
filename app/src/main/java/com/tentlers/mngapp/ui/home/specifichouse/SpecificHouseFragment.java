package com.tentlers.mngapp.ui.home.specifichouse;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.TableHouse;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.meters.GetLastMeterReading;
import com.tentlers.mngapp.data.tables.meters.LastReadingWithDate;
import com.tentlers.mngapp.data.tables.meters.MetersListObj;
import com.tentlers.mngapp.data.tables.rooms.RoomForRoomList;
import com.tentlers.mngapp.databinding.FragmentHouseRoomsListItemBinding;
import com.tentlers.mngapp.databinding.FragmentSpecificHouseBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class SpecificHouseFragment extends Fragment {

    FragmentSpecificHouseBinding binding;
    HouseViewModal viewModal;

    /*
     * This object holds the data of the house chosed by the user
     * for displaying the house Details.
     */
    int houseId;
    TableHouse choosenHouse;

    /*visibility controling varibles*/
    boolean isAddressVisible;
    boolean isMeterDetailsVisible;

    List<RoomForRoomList> theThreeRooms;

    public SpecificHouseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        /* Get the House the user selected */
        houseId = viewModal.getHouseIdForSpecificHouse();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSpecificHouseBinding.inflate(getLayoutInflater(), container, false);
        isMeterDetailsVisible = true;

        /*
         * Bind the toolbar and set the buttons
         */
        binding.toolbarSpecificHosue.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_item_specific_house_edit) {
                    if (choosenHouse != null) {
                        viewModal.setHouseForEdit(choosenHouse);
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_specificHouseFragment_to_editHouseDialog);
                    }
                }
                return true;
            }
        });

        viewModal.getHouseFromHouseId(houseId).observe(getViewLifecycleOwner(), new Observer<TableHouse>() {
            @Override
            public void onChanged(TableHouse tableHouse) {

                choosenHouse = tableHouse;

                /* Set the house Name and Date on the textView */
                binding.specificHouseName.setText(tableHouse.getHouseName());
                binding.specificHouseDate.setText(TableHouse.getHouseDate(tableHouse.getDate()));

                /* Set the address on the address layout.*/
                if (tableHouse.getAddress() != null) {/*TODO:update the text style if address is not provided*/
                    String houseno = String.valueOf(tableHouse.getAddress().houseNo);
                    binding.specificHouseTextviewHouseno.setText(houseno.length() == 0 ? getString(R.string.not_provided) : houseno);

                    binding.specificHouseLocality.setText(
                            tableHouse.getAddress().locality == null ? getString(R.string.not_provided)
                                    : tableHouse.getAddress().locality);


                    binding.specificHousePostalcode.setText(tableHouse.getAddress().postalcode == null ?
                            getString(R.string.not_provided) : tableHouse.getAddress().postalcode);

                    binding.specificHouseCity.setText(tableHouse.getAddress().city == null ?
                            getString(R.string.not_provided) : tableHouse.getAddress().city);

                    binding.specificHouseCountry.setText(
                            tableHouse.getAddress().country == null ? getString(R.string.not_provided)
                                    : tableHouse.getAddress().country);
                }

                /*Set the meter number and reading (reading date)*/
                if (tableHouse.getIsMeterIncluded()) {
                    binding.specificHouseMeterNo.setText(String.valueOf(tableHouse.getMeterid()));
                    viewModal.getLastEnteredMeterEntry(new GetLastMeterReading().setMeterId(tableHouse.getMeterid())).observe(getViewLifecycleOwner(),
                            new Observer<LastReadingWithDate>() {
                                @Override
                                public void onChanged(LastReadingWithDate lastReadingWithDate) {

                                    /*set last meter reading*/
                                    binding.specificHouseLastReading.setText(String.valueOf(lastReadingWithDate.getLastMeterReading()));

                                    /*set last meter reading Date*/
                                    binding.specificHouseLastReadingDate.setText(AllMetersData.getMeterDate(lastReadingWithDate.getDate()));

                                    binding.specificHosueRelativeLayoutLastmeterReading.setVisibility(View.VISIBLE);

                                }
                            });
                } else {
                    binding.specificHouseMeterNo.setText(getText(R.string.not_provided));
                    binding.specificHouseMeterNo.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorButtonText));
                    binding.specificViewButtonMeter.setEnabled(false);
                }

                /*Set the total rooms and occupied rooms*/
                binding.specificHouseTotalrooms.setText(String.valueOf(tableHouse.getNoOfRooms()));
                binding.specificHouseOccupiedrooms.setText(String.valueOf(tableHouse.getOccupiedRooms()));
            }
        });

        /* On getting the three rooms update its value in setting up those three list items of the rooms. */
        viewModal.getThreeRooms(houseId).observe(getViewLifecycleOwner(), new Observer<List<RoomForRoomList>>() {
            @Override
            public void onChanged(List<RoomForRoomList> roomForRoomLists) {
                theThreeRooms = roomForRoomLists;
                setThreeRooms(roomForRoomLists);
            }
        });

        /*set listener to the three floating action butons*/

        /*handle add room button click listner*/
        binding.specificHouseFabAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choosenHouse.getNoOfRooms() > 99) {
                    /*show snack bar that max room limit is reached*/
                    Snackbar.make(binding.specificHouseCoordinatorLayout, getString(R.string.max_room_limit_reached), BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                } else {
                    viewModal.setHouseIdForRoomEntry(houseId);
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_nav_roomEnteyFragment);
                }
            }
        });

        /*handle add tenant button*/
        binding.specificHouseFabAddTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_tenantEntryFragment);
            }
        });

        /*Handle seleted house button */
        binding.specificHouseFabDeleteHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeleteAlertDialog().show();
            }
        });

        /*set the address button click listener*/
        binding.specificHouseRelativeLayoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*update the image to show more or show less*/
                binding.specificHouseShowImageAddressDesc.setImageDrawable(/*initially show more icon is visible
                if it is visible show less icon is show if it is not visible show more icon will be show */
                        !isAddressVisible ? ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_expand_less_24)
                                : ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_expand_more_24)
                );

                if (choosenHouse != null && choosenHouse.getAddress() == null) {
                    binding.specificHouseTextviewNoAddress.setVisibility(isAddressVisible ? View.GONE : View.VISIBLE);
                    isAddressVisible = !isAddressVisible;
                    return;
                }
                /*set the address desc visibility*/
                binding.specificHouseRelativeLayoutAddressDesc.setVisibility(/*initially address is not visible*/
                        isAddressVisible ? View.GONE : View.VISIBLE);

                isAddressVisible = !isAddressVisible;
            }
        });

        /*set the meter details view click listener*/
        binding.specificHouseRelativeLayoutShowMeterDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.specificHouseRelativeLayoutMeterDetailsDesc.setVisibility(
                        isMeterDetailsVisible ? View.GONE : View.VISIBLE);

                binding.specificHouseShowImageMeterDetailsDesc.setImageDrawable(
                        !isMeterDetailsVisible ?
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_expand_less_24) :
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_expand_more_24));

                isMeterDetailsVisible = !isMeterDetailsVisible;
            }
        });

        /* Add the listener to "view All" meters button which transfers the user to the specific meter fragment or the meter history
         * if no meter is added then button is made unclickable in observer*/
        binding.specificViewButtonMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choosenHouse != null) {
                    viewModal.setMetersListObj(new MetersListObj(choosenHouse.getMeterid(), choosenHouse.getHouseName(), null, true));
                    Navigation.findNavController(v).navigate(R.id.action_global_metersFragment);
                }
            }
        });

        /*Add the listener to "view All" rooms button which transefers the user to the room fragment*/
        binding.specificViewButtonRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_global_houseRooms);
            }
        });

        binding.roomItemFirst.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSpecificRoom(theThreeRooms.get(0).roomId);
            }
        });
        binding.roomItemSecond.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSpecificRoom(theThreeRooms.get(1).roomId);
            }
        });
        binding.roomItemThird.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSpecificRoom(theThreeRooms.get(2).roomId);
            }
        });

        return binding.getRoot();
    }

    /*
     * This meathod is responsible for setting the three rooms in the specific house fragment.
     */
    public void setThreeRooms(List<RoomForRoomList> threeRooms) {
        /* threeRooms : This list object holds the information about the the three rooms of the house*/
        int listsize = threeRooms.size();
        /*
         * array of the binding objects for the three rooms
         * The all three list items are from the room fragment list item they are invisible their visibility is on the basis of the room
         * room data availabel . if mare than three of three rooms are present then all three are shown
         * else as per the data .ie two for 2 rooms and 1 for one rooms.
         */
        FragmentHouseRoomsListItemBinding[] list = new FragmentHouseRoomsListItemBinding[]{binding.roomItemFirst, binding.roomItemSecond, binding.roomItemThird};

        if (listsize != 0) {
            for (int i = 0; i < listsize; i++) {
                setfirstRoom(true, list[i], threeRooms.get(i));
            }
        }


    }

    /* This meathod one by one sets the room visibility and the text.*/
    public void setfirstRoom(boolean isSet, FragmentHouseRoomsListItemBinding v, RoomForRoomList rooms) {
        v.getRoot().setVisibility(View.VISIBLE);
        /* Get a refference to all the values in the room object use that in lambda expressions to
         * handle the empty value.*/
        String roomTenant = rooms.tenantName;
        v.houseRoomIstitemImagePopupMenu.setVisibility(View.INVISIBLE);/*make the popup icon invisible*/

        v.houseRoomListitemRoomNo.setText(String.valueOf(rooms.roomNo));

        v.houseRoomListitemRoomName.setText(rooms.roomName);

        v.houseRoomListitemRoomTenant.setText(roomTenant == null ?
                getString(R.string.no_tenant_added) : roomTenant);

        v.houseRoomListitemRoomsTenantStatus.setVisibility(rooms.ocupiedStatus ? View.VISIBLE : View.GONE);
    }

    /* Dialog for confirming the delete of the house.*/
    public MaterialAlertDialogBuilder getDeleteAlertDialog() {

        return new GetDeleteHouseDialog().getdeleteHouseDilog(requireContext(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (choosenHouse != null) {
                    viewModal.deleteHosue(choosenHouse);
                    dialog.dismiss();
                    Navigation.findNavController(binding.getRoot()).navigateUp();
                } else dialog.cancel();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void selectSpecificRoom(int roomid) {
        viewModal.setRoomIdForSpecificRoom(roomid);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_nav_specificRoomFragment);
    }

}