package com.example.easelife.ui.rooms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.easelife.R;
import com.example.easelife.data.HouseViewModal;
import com.example.easelife.data.tables.TableRooms;
import com.example.easelife.data.tables.rooms.RoomNoName;
import com.example.easelife.databinding.FragmentRoomEnteyBinding;
import com.example.easelife.ui.home.SaveDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class RoomEnteryFragment extends Fragment {
    FragmentRoomEnteyBinding enteyBinding;
    HouseViewModal viewModal;
    /*
     * bolean value discribing the uniqueeness of the roomNo and ROom name
     */
    private boolean isTheENteredRoomNoUnique = false, isTheENteredRoomNameUnique = false;

    /*
     * this object holds the meter ids of all the rooms in the database.
     */
    private List<Long> gotAllroomMeterids;

    /*
     * This object holds  all the meter ids of houses in the database.
     */
    private List<Long> gotAllHouseMeterIds;

    /*
     * This object holds all the room names in the given house
     */
    private List<RoomNoName> gotAllRoomNoName;

    /*
     * This object holds all the valid data entered in for the room creation.
     */
    private TableRooms tableRooms = new TableRooms();

    /*
     * This variable stores the system generated room number and room name
     */
    private RoomNoName generatedroomNOName = new RoomNoName();

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModal = null;
    }


    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        /*
         *  Assing the house id from the view modal tho tableroom object.
         */
        tableRooms.houseId = viewModal.getHouseIdForRoomEntry();

        /*
         * asign the observer to the all house meter ids  and all room meter ids.
         */
        viewModal.getAllHousemeterids().observe(this, new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> longs) {
                gotAllHouseMeterIds = longs;
            }
        });
        viewModal.getAllroomhouseids().observe(this, new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> longs) {
                gotAllroomMeterids = longs;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        enteyBinding = FragmentRoomEnteyBinding.inflate(getLayoutInflater(), container, false);

        /*
         * Pre assign the room no and name in for the room
         * It also ensures the room number to be 1
         * And room name Tobe "Room1" when first room is enterd for the house
         */
        viewModal.getRoomNoName(viewModal.getHouseIdForRoomEntry()).observe(getViewLifecycleOwner(), new Observer<List<RoomNoName>>() {
            @Override
            public void onChanged(List<RoomNoName> roomNoNames) {
                gotAllRoomNoName = roomNoNames;
                if (roomNoNames.size() != 0) {
                    generatedroomNOName.roomNo = roomNoNames.get(0).roomNo + 1;
                    generatedroomNOName.roomName = String.format("Room%d", roomNoNames.get(0).roomNo + 1);

                } else {
                    generatedroomNOName.roomNo = 1;
                    generatedroomNOName.roomName = "Room1";
                }

                enteyBinding.textInputEditTextOutlinedRoomNo.setText(String.valueOf(generatedroomNOName.roomNo));
                enteyBinding.textInputEditTextOutlinedRoomName.setText(generatedroomNOName.roomName);
            }
        });

        /*
         * Handle the navigation icon and menu item click
         * both displaying the exit and save dialog resp. when clicked.
         */
        enteyBinding.toolbarHouseEnter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExitDialoge().show();
            }
        });

        enteyBinding.toolbarHouseEnter.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("the clicked item id", String.valueOf(item.getItemId()));
                switch (item.getItemId()) {
                    case R.id.meuitem_house_save:
                        if (checkforDataValidity()) {
                            getSaveDialoge().show();
                        }
                        break;
                }
                return true;
            }
        });

        /*
         * Manges the switches for encluding meters and meter numbers
         */
        enteyBinding.switchRoomElectricMeterPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tableRooms.isMeterEnabled = true;
                    enteyBinding.layoutRoomMeterNumber.setVisibility(View.VISIBLE);
                } else {
                    tableRooms.isMeterEnabled = false;
                    enteyBinding.layoutRoomMeterNumber.setVisibility(View.GONE);
                }
            }
        });

        enteyBinding.switchRoomElectricMeterNumberManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tableRooms.isSystemDeside = false;
                    enteyBinding.textInputEditTextRoomElectricMeterNo.setVisibility(View.VISIBLE);
                    enteyBinding.switchRoomElectricMeterNumberSystemDecide.setChecked(false);
                } else {
                    tableRooms.isSystemDeside = true;
                    enteyBinding.textInputEditTextRoomElectricMeterNo.setVisibility(View.GONE);
                    enteyBinding.switchRoomElectricMeterNumberSystemDecide.setChecked(true);
                }
            }
        });


        return enteyBinding.getRoot();
    }

    /*
     * This is responsible for validating the data entered for the room
     * for the specific house.
     */
    private boolean checkforDataValidity() {
        return (checkForRoomNameValidity() & isMeterValid());
    }

    /*
     * The Meathod handles the meter no entry data
     * also manages the error shown on the input layout of the edit text.
     */
    private boolean isMeterValid() {
        if (tableRooms.isMeterEnabled) {
            if (tableRooms.isSystemDeside) {
                /*
                 * system generated meter number will be unique
                 */
                return true;
            }

            String meterNumber = enteyBinding.textInputEditTextRoomElectricMeterNo.getText().toString();
            if (meterNumber.length() != 0) {
                long meterNumberLong = Long.parseLong(meterNumber);
                /*
                 *Check for uniqueness
                 */
                if (isMeterNoUnique(meterNumberLong)) {

                    /*
                     *set the error to null and add the meter no in the Tableroom object
                     */
                    tableRooms.meterId = meterNumberLong;
                    enteyBinding.textInputLayoutRoomMeterNo.setError("");
                    return true;
                } else {
                    /*
                     *set the error that meter is not unique
                     */
                    enteyBinding.textInputLayoutRoomMeterNo.setError(getString(R.string.meter_no_already_exits));
                    return false;
                }

            } else {
                /*
                 * show the error that meter no cannot be empty. error field recquired
                 */
                enteyBinding.textInputLayoutRoomMeterNo.setError(getString(R.string.error_field_recquired));
                return false;
            }
        }

        return true;
    }

    /*
     * This meathod is responsible for checking the uniquenes of the meter number inserted
     */
    private boolean isMeterNoUnique(long getmeterno) {
        /*
         * Check for uniqueness in room ids
         */
        if (gotAllroomMeterids != null) {
            for (long i : gotAllroomMeterids) {
                if (i == getmeterno) {
                    return false;
                }
            }
        }
        /*
         *  Check for uniquenes in house ids
         */
        if (gotAllHouseMeterIds != null) {
            for (long i : gotAllHouseMeterIds) {
                if (i == getmeterno) {
                    return false;
                }
            }
        }

        return true;
    }

    /*
     * This meathod is responsible for checking for the validity of the room name
     * And assigning the errors to the edit text field of room name entry.
     */
    private boolean checkForRoomNameValidity() {
        boolean isroomprsent = true;
        boolean isroomnopresent = true;
        String roomName = enteyBinding.textInputEditTextOutlinedRoomName.getText().toString();
        String roomNo = enteyBinding.textInputEditTextOutlinedRoomNo.getText().toString();
        if (roomName.length() == 0) {
            enteyBinding.textInputLayoutOutlinedRoomName.setError("!Room Name Can't be Empty");
            isroomprsent = false;
        }
        if (roomNo.length() == 0) {
            enteyBinding.textInputLayoutRoomMeterNo.setError(getString(R.string.error_field_recquired));
            isroomnopresent = false;
        }
        /*
         * if both are empty then it should return false
         */
        if (!isroomprsent & !isroomnopresent) {
            return false;
        }
        int roomnoInt = Integer.parseInt(roomNo);
        /*
        * It Checks for the room no and room Name uniquness
        */
        checkForUniqueRoomNameandNo(roomName, roomnoInt);
        if (isTheENteredRoomNameUnique & isTheENteredRoomNoUnique) {
            enteyBinding.textInputLayoutOutlinedRoomName.setError("");
            tableRooms.roomName = roomName;
            tableRooms.roomNo = roomnoInt;
            return true;
        } else return false;
    }

    /*
     * meathod for checking the uniquness of the room name
     * enterd for the given house only.If the room name is system generated
     * it returns true , else loops through the whole room names.
     */
    private void checkForUniqueRoomNameandNo(String roomnaeminputed, int roomNo) {

        if (roomnaeminputed.equals(generatedroomNOName.roomName)) {    // Here it checks for system generated name and no
            isTheENteredRoomNameUnique = true;
        }
        if (roomNo == generatedroomNOName.roomNo) {
            isTheENteredRoomNoUnique = true;
        }
        if (!isTheENteredRoomNoUnique || !isTheENteredRoomNameUnique) {
            if (gotAllRoomNoName != null & gotAllRoomNoName.size() != 0) {
                for (RoomNoName n : gotAllRoomNoName) {   // Here it loops to check for uniqueness
                    if (n.roomName.equals(roomnaeminputed)) {
                        enteyBinding.textInputLayoutOutlinedRoomName.setError("!Room Name Already Exists");
                        isTheENteredRoomNameUnique = false;
                    }
                    if (n.roomNo == roomNo) {
                        enteyBinding.textInputLayoutOutlinedRoomNo.setError("!Room Number Already Exists");
                        isTheENteredRoomNoUnique = false;
                    }
                }


            }
        }
    }

    /*
     * Insert the room in the data base . This meathod also generates the
     * meter id if user has enabled the system generated meter id.
     */
    private void saveHouseData() {
        if (tableRooms.isSystemDeside) {
            SharedPreferences sh = requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            tableRooms.meterId = sh.getLong(getString(R.string.system_generated_meterid_last_entry), 0) + 1;
            sh.edit().putLong(getString(R.string.system_generated_meterid_last_entry), tableRooms.meterId).apply();
        }
        viewModal.insertNewRoom(tableRooms);
    }

    private MaterialAlertDialogBuilder getSaveDialoge() {

        return (new SaveDialog(requireActivity(), enteyBinding.getRoot()))
                .getSaveDialogue(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveHouseData();
                                dialog.dismiss();
                                Navigation.findNavController(enteyBinding.getRoot()).navigateUp();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Navigation.findNavController(enteyBinding.getRoot()).navigateUp();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

    }

    private MaterialAlertDialogBuilder getExitDialoge() {

        return (new SaveDialog(requireActivity(), enteyBinding.getRoot()))
                .getCancelDialog(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Navigation.findNavController(enteyBinding.getRoot()).navigateUp();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
    }
}