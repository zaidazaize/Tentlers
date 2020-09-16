package com.tentlers.mngapp.ui.rooms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.TableRooms;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.rooms.RoomNoName;
import com.tentlers.mngapp.databinding.FragmentRoomEnteyBinding;
import com.tentlers.mngapp.ui.home.SaveDialog;

import java.util.List;
import java.util.Objects;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class RoomEnteryFragment extends Fragment {
    FragmentRoomEnteyBinding enteyBinding;
    HouseViewModal viewModal;

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

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);
        /*
         * Add back pressed callback for handling the back button using the back pressed dispatcher.
         */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getExitDialoge().show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        /*
         *  Assing the house id from the view modal tho tableroom object.
         */
        tableRooms.houseId = viewModal.getHouseIdForRoomEntry();

        /*assign the observer to the all house meter ids  and all room meter ids.*/
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

                /*if list of rooms is empty set the room number to 1 */
                generatedroomNOName.roomNo = roomNoNames.size() == 0 ? generatedroomNOName.roomNo = 1 : roomNoNames.get(0).roomNo + 1;
                generatedroomNOName.roomName = ("Room" + generatedroomNOName.roomNo);

                enteyBinding.textInputEditTextOutlinedRoomNo.setText(String.valueOf(generatedroomNOName.roomNo));
                enteyBinding.textInputEditTextOutlinedRoomName.setText(generatedroomNOName.roomName);
            }
        });

        /*
         * Handle the navigation icon and menu item click
         * both displaying the exit and save dialog resp. when clicked.
         */
        enteyBinding.toolbarRoomEnter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExitDialoge().show();
            }
        });

        enteyBinding.toolbarRoomEnter.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.meuitem_house_save) {
                    if (checkforDataValidity()) {
                        getSaveDialoge().show();
                    }
                }
                return true;
            }
        });

        /*
         * Manges the switches for including meters and meter numbers
         * the switch is initialised at the end of onCreateView.
         */
        enteyBinding.switchRoomElectricMeterPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tableRooms.isMeterEnabled = isChecked;
                enteyBinding.layoutRoomMeterNumber.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });

        enteyBinding.switchRoomElectricMeterNumberManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                tableRooms.isSystemDeside = !isChecked; /*  This is use for auto generating meter number*/
                enteyBinding.textInputLayoutRoomMeterNo.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                enteyBinding.switchRoomElectricMeterNumberSystemDecide.setChecked(!isChecked);
            }
        });

        /*Set the initial meter reding in the allmeteres data object of tablerooms*/
        enteyBinding.textInputEditTextRoomInitialMeterReading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tableRooms.allMetersData.setLastMeterReadingFromString(s.toString());
            }
        });

        /*initialise the meter entry switch*/
        enteyBinding.switchRoomElectricMeterPermission.setChecked(true);
        return enteyBinding.getRoot();
    }

    /*
     * This is responsible for validating the data entered for the room
     * for the specific house.
     */
    private boolean checkforDataValidity() {
        return (checkForRoomNameValidity() && isMeterValid() && checkForRoomnoVAlidiy());
    }

    /*
     * The Meathod handles the meter no entry data
     * also manages the error shown on the input layout of the edit text.
     */
    private boolean isMeterValid() {
        if (enteyBinding.switchRoomElectricMeterPermission.isChecked()) {
            if (enteyBinding.switchRoomElectricMeterNumberSystemDecide.isChecked()) {/*system generated meter number will be unique*/
                return true;
            }
            if (enteyBinding.switchRoomElectricMeterNumberManual.isChecked()) {
                String meterNumber = Objects.requireNonNull(enteyBinding.textInputEditTextRoomElectricMeterNo.getText()).toString();
                if (meterNumber.length() != 0) { /*Check for uniqueness*/

                    long meterNumberLong = Long.parseLong(meterNumber);
                    if (isMeterNoUnique(meterNumberLong)) {/*set the error to null and add the meter no in the Tableroom object*/
                        tableRooms.setMeterId(meterNumberLong);
                        tableRooms.getAllMetersData().setOnlyReadingState(AllMetersData.CREATE);/* Set the reading state to create*/
                        enteyBinding.textInputLayoutRoomMeterNo.setError("");
                        return true;
                    } else {/*set the error that meter is not unique*/
                        enteyBinding.textInputLayoutRoomMeterNo.setError(getString(R.string.meter_no_already_exits));
                        return false;
                    }

                } else { /* show the error that meter no cannot be empty. error field recquired*/
                    enteyBinding.textInputLayoutRoomMeterNo.setError(getString(R.string.error_field_recquired));
                    return false;
                }
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
         *  Check for uniquenes in house meter ids
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

        String roomName = enteyBinding.textInputEditTextOutlinedRoomName.getText().toString();

        if (roomName.length() == 0) { /* First check for the room name should not be empty. */
            enteyBinding.textInputLayoutOutlinedRoomName.setError(getString(R.string.room_name_cannot_be_empty));
            return false;
        }

        if (checkForUniqueRoomName(roomName)) {/* Check for the uiquness of the room name */
            /*
             * if name is unique asign error text to be null else assign to enter any other name
             * Add the room name in the table room object
             */
            enteyBinding.textInputLayoutOutlinedRoomName.setError("");
            tableRooms.roomName = roomName;
            return true;
        } else {
            enteyBinding.textInputLayoutOutlinedRoomName.setError(getString(R.string.room_name_already_exists));
            return false;
        }

    }

    /*
     * method for checking the uniquness of the room name
     * entered for the given house only.If the room name is system generated
     * it returns true , else loops through the whole room names.
     */
    private boolean checkForUniqueRoomName(String roomnaeminputed) {
        /*
         * If the room is system generated it will be unique. Return true.
         */

        if (roomnaeminputed.equals(generatedroomNOName.roomName)) {
            return true;
        }

        if (gotAllRoomNoName != null && gotAllRoomNoName.size() != 0) {
            for (RoomNoName n : gotAllRoomNoName) {   // Here it loops to check for uniqueness
                if (n.roomName.equals(roomnaeminputed)) {
                    return false;
                }
            }
        }
        return true;
    }

    /* Meathod checks for room name validity and also handles the error shown */
    private boolean checkForRoomnoVAlidiy() {
        String roomNo = enteyBinding.textInputEditTextOutlinedRoomNo.getText().toString();

        if (roomNo.length() != 0) {
            int roomNoInt = Integer.parseInt(roomNo);
            if (isRoomNoUnique(roomNoInt)) {
                /*
                 * Set error to null and add the value to the tableroom object
                 */
                enteyBinding.textInputLayoutOutlinedRoomNo.setError("");
                tableRooms.roomNo = roomNoInt;
                return true;
            } else {
                enteyBinding.textInputLayoutOutlinedRoomNo.setError(getString(R.string.room_no_already_used));
                return false;
            }

        } else {
            enteyBinding.textInputLayoutRoomMeterNo.setError(getString(R.string.error_field_recquired));
            return false;
        }
    }

    /* This method checks for the uniqueness of room no entered by the user */
    private boolean isRoomNoUnique(int roomNo) {
        if (roomNo == generatedroomNOName.roomNo) {
            return true;
        }
        if (gotAllRoomNoName != null) {
            for (RoomNoName n : gotAllRoomNoName) {   // Here it loops to check for uniqueness
                if (n.roomNo == roomNo) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * Insert the room in the data base . This meathod also generates the
     * meter id if user has enabled the system generated meter id.
     */
    private void saveRoomData() {
        if (tableRooms.isSystemDeside) {
            tableRooms.getAllMetersData().setReadingState(AllMetersData.CREATE);
            SharedPreferences sh = requireActivity().getSharedPreferences(getString(R.string.base_ids_sharedpreferences_file), Context.MODE_PRIVATE);
            tableRooms.setMeterId(sh.getLong(getString(R.string.system_generated_meterid_last_entry), 100000) + 1);
            sh.edit().putLong(getString(R.string.system_generated_meterid_last_entry), tableRooms.meterId)
                    .apply();
        }
        viewModal.insertNewRoom(tableRooms);
    }

    private MaterialAlertDialogBuilder getSaveDialoge() {
        return (new SaveDialog(requireActivity(), enteyBinding.getRoot()))
                .getSaveDialogue(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveRoomData();
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