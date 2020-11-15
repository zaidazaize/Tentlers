package com.tentlers.mngapp.ui.home;


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
import com.tentlers.mngapp.data.tables.Address;
import com.tentlers.mngapp.data.tables.TableHouse;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.databinding.FragmentHouseEntryBinding;

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

public class HouseEntryFragment extends Fragment {

    /*
     * This object holds the vielmodal which in turn holds the communication b/w the
     * UI and the data. It is also acts as the layer b/w the repository class and the UI classes
     */
    HouseViewModal houseViewModal;
    /*
     * The object holds the refference to the system generated binding class for this
     * the layout specific to this fragment. It helps binding data to the the Ui and also extracting data
     * for the UI.
     */
    FragmentHouseEntryBinding houseEntryBinding;
    /*
     * This object holds the list of houseNameMeterid which in turn holds the list for
     * all the house name and the meterids entered by the user
     * It is used for enshuring the uniqueness of the inserted house name and the
     * meter ids.
     **/
    List<String> allHouseName;
    List<Long> allMeterNos;
    long lastEnteredHouseId;

    /*
     * This object holds all the data related to the house and
     * is used create a new row in the TableHouse
     */
    private TableHouse tableHouse = new TableHouse();
    /*
     *The Object Holds the data of address enterd while creating the house
     */
    private Address address = new Address();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Enitialising the viewModal here.
         */
        houseViewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        /*Getting the list of all the house Names  from the database
         * these are specific to the users and can be null.*/
        houseViewModal.getAllHouseNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> houseNames) {
                allHouseName = houseNames;
            }
        });
        /*update all meter nos in the field*/
        houseViewModal.getAllMeterNos().observe(this, new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> longs) {
                allMeterNos = longs;
            }
        });

        /*
         *Defining the callback and attaching it to the backpresseddispather
         * to handle the back button pressed events.
         */
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getExitDialoge().show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        /* detaching the binding  for avoiding the memory leak.*/
        houseEntryBinding = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*
         * Initialize the binding objecty and inflate the layout here.
         */
        houseEntryBinding = FragmentHouseEntryBinding.inflate(inflater, container, false);

        /* Set the house name based on the last entered house id stored in the SharedPreferences.
         * Incrementing 1 in the layout of the list*/
        SharedPreferences sharedPreferences = requireActivity()
                .getSharedPreferences(getString(R.string.base_ids_sharedpreferences_file), Context.MODE_PRIVATE);

        lastEnteredHouseId = sharedPreferences.getLong(getString(R.string.LastEnteredHouseId), 0);
        houseEntryBinding.textInputEditTextOutlinedHousename.setText(String.format(getString(R.string.updateHouseName), (lastEnteredHouseId + 1)));

        /*Assining the handler tho the cross sign .
         * It is responsible for showing the exit dialogue.*/
        houseEntryBinding.toolbarHouseEnter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExitDialoge().show();
            }
        });

        /*
         * Assign the handler to the save sign .
         * It is responsibel for showing the save option and
         * also passing the value to the viewmodal so as the write in
         * the database.
         */
        houseEntryBinding.toolbarHouseEnter.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuitem_house_save) {
                    if (checkForDataValidity()) {
                        getSaveDialoge().show();
                    }
                }
                return true;
            }
        });

        /*
         * Switch for handling the address layout visibility.
         */
        houseEntryBinding.switchAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                houseEntryBinding.linearLayoutAddressLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });


        /*
         * Switch for handlign the edit text visibility for entering the no of rooms
         */
        houseEntryBinding.switchNoOfRooms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                houseEntryBinding.textInputLayoutHouseNoofRooms.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });

        /*set error on rooms entry if the no of rooms is more than 99*/
        houseEntryBinding.textInputEditTextHouseNoOfRooms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                houseEntryBinding.textInputLayoutHouseNoofRooms.setError(
                        tableHouse.setNoOfRoomsFromString(s.toString()) > 99 ? getString(R.string.no_of_rooms_must_be_less_than_100) : "");

            }
        });

        /* Switch for handling the permission for assigning the meter.*/
        houseEntryBinding.switchElectricMeterPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                houseEntryBinding.layoutMeterNumber.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        /*handle the auto generate or manually enter the meter no state it contrls the visibility of edittext(meter entry) and checked state of (auto generate meter no ) switch.*/
        houseEntryBinding.switchManualEnterElectricMeterNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                houseEntryBinding.textInputLayoutManualEnterHouseMeterNo.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                houseEntryBinding.switchElectricMeterNumberSystemDecide.setChecked(!isChecked);

            }
        });

        /*set the watcher on the reading entered by the user and reading all meters data is updated*/
        houseEntryBinding.textInputEditTextHouseInitialMeterReading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tableHouse.getAllMetersData().setLastMeterReadingFromString(s.toString());
            }
        });

        return houseEntryBinding.getRoot();
    }

    /* This method checks for the validity for the data entered.*/
    /*TODO: move the comparison to background thread.*/
    private boolean checkForDataValidity() {

        return isHouseNamevalid() & isAdressValid() & isMeterNumberValid() & isHouseValid();
    }

    /*Checks the validity and nullability hosue name
     * it is also responsible for showing the error messages.*/
    private boolean isHouseValid() {
        if (!houseEntryBinding.switchNoOfRooms.isChecked()) {/*if no of rooms are not enabled then set auto generate room to false and return true*/
            tableHouse.setRoomAutoGenerated(false);
            return true;
        }

        String houseRooms = Objects.requireNonNull(houseEntryBinding.textInputEditTextHouseNoOfRooms.getText()).toString();

        if (houseRooms.length() == 0) {/*set the error for recquired field if text size is 0*/
            houseEntryBinding.textInputLayoutHouseNoofRooms.setError(getString(R.string.error_field_recquired));
            return false;
        }
        if (tableHouse.setNoOfRoomsFromString(houseRooms) < 100) {
            houseEntryBinding.textInputLayoutHouseNoofRooms.setError("");
            tableHouse.setRoomAutoGenerated(true);
            return true;
        } else {
            houseEntryBinding.textInputLayoutHouseNoofRooms.setError(getString(R.string.no_of_rooms_must_be_less_than_100));
            return false;
        }
    }

    private boolean isMeterNumberValid() {
        if (!houseEntryBinding.switchElectricMeterPermission.isChecked()) {/*return true if does not wnat to enclude meter not checked.*/
            tableHouse.setIsMeterIncluded(false);
            return true;
        }
        tableHouse.setIsMeterIncluded(true);

        if (houseEntryBinding.switchManualEnterElectricMeterNumber.isChecked()) {

            String housemeterno = houseEntryBinding.textInputEditTextManuallyEnterHouseElectricMeterNo.getText().toString();

            if (housemeterno.length() == 0) {/*return false if meter no is null*/
                houseEntryBinding.textInputLayoutManualEnterHouseMeterNo.setError(getString(R.string.error_field_recquired));
                return false;
            }
            long housemeternolong = Long.parseLong(housemeterno);
            if (isMeterNounique(housemeternolong)) {
                tableHouse.getAllMeters().setMeterNo(housemeternolong);/*set the meter no on in the allmeters obj*/
                tableHouse.getAllMetersData().setOnlyReadingState(AllMetersData.CREATE); /* Set the reading state*/
                houseEntryBinding.textInputLayoutManualEnterHouseMeterNo.setError("");
                return true;

            } else {
                houseEntryBinding.textInputLayoutManualEnterHouseMeterNo.setError(getString(R.string.meter_no_already_exits));
                return false;
            }

        }

        /*set the autogenerated boolean value to the state of the switch of is system decide */
        tableHouse.setIsmetersystemgenerated(houseEntryBinding.switchElectricMeterNumberSystemDecide.isChecked());


        return true;

    }

    private boolean isHouseNamevalid() {
        String housename = houseEntryBinding.textInputEditTextOutlinedHousename.getText().toString();
        if (housename.length() != 0) {
            if (isHouseunique(housename)) {
                houseEntryBinding.textInputLayoutOutlinedHouseName.setError("");
                tableHouse.setHouseName(housename);
                return true;
            } else {
                houseEntryBinding.textInputLayoutOutlinedHouseName.setError("!House name already exists");
                return false;
            }

        } else {
            houseEntryBinding.textInputLayoutOutlinedHouseName.setError(getString(R.string.error_field_recquired));
            return false;
        }
    }

    private boolean isAdressValid() {
        if (houseEntryBinding.switchAddress.isChecked()) {
            if (isAdressCityValid() & isAdressCountryValid() & isAdressPostalAdressValid()) {
                String houseNo = houseEntryBinding.textInputEditTextOutlinedHousenumber.getText().toString();
                address.setHouseNo(houseNo.length() == 0 ? 0 : Integer.parseInt(houseNo));
                String houseLocality = houseEntryBinding.textInputEditTextOutlinedHouseArea.getText().toString();
                address.setLocality(houseLocality.length() == 0 ? null : houseLocality);
                tableHouse.setAddress(address);
                return true;

            } else return false;
        } else return true;
    }

    private boolean isAdressCityValid() {
        String houseCity = houseEntryBinding.textInputEditTextHouseCity.getText().toString();
        if (houseCity.length() != 0) {
            address.setCity(houseCity);
            houseEntryBinding.layoutHouseCity.setError("");
            return true;
        } else {
            houseEntryBinding.layoutHouseCity.setError(getString(R.string.error_field_recquired));
            return false;
        }
    }

    private boolean isAdressCountryValid() {
        String houseCountry = houseEntryBinding.textInputEditTextHouseCountry.getText().toString();
        if (houseCountry.length() != 0) {
            houseEntryBinding.layoutHouseCountry.setError("");
            address.setCountry(houseCountry);
            return true;
        } else {
            houseEntryBinding.layoutHouseCountry.setError(getString(R.string.error_field_recquired));
            return false;
        }
    }

    private boolean isAdressPostalAdressValid() {
        String housePostal = houseEntryBinding.textInputEditTextOutlinedPostal.getText().toString();
        if (housePostal.length() != 0) {
            houseEntryBinding.layoutHousePostalcode.setError("");
            address.setPostalcode(housePostal);
            return true;
        } else {
            houseEntryBinding.layoutHousePostalcode.setError(getString(R.string.error_field_recquired));
            return false;
        }
    }

    /*check for unique house name*/
    private boolean isHouseunique(String gothousename) {
        if (allHouseName != null) {
            for (String s : allHouseName) {
                if (gothousename.equals(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    /*check for unique meter number*/
    private boolean isMeterNounique(long gotmeterno) {
        if (allMeterNos != null) {
            for (long s : allMeterNos) {
                if (gotmeterno == s) {
                    return false;
                }
            }
        }
        return true;
    }

    // Save the House information
    private void saveHouseData() {

        /* this will be generate new meter id based on the last id stored in the shared Prefferences.
         * If the rooms are to be auto generated then it will be done in the repository.*/
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.base_ids_sharedpreferences_file), Context.MODE_PRIVATE);
        if (tableHouse.isIsmetersystemgenerated()) {
            tableHouse.getAllMeters().setMeterNo(sharedPreferences.getLong(getString(R.string.system_generated_meterid_last_entry), 100000) + 1);
            tableHouse.getAllMetersData().setReadingState(AllMetersData.CREATE);
            sharedPreferences.edit()
                    .putLong(getString(R.string.system_generated_meterid_last_entry), tableHouse.getAllMeters().getMeterNo())
                    .apply();

        }

        /*update shore preffernce of house id*/
        sharedPreferences.edit()
                .putLong(getString(R.string.LastEnteredHouseId), lastEnteredHouseId + 1)
                .apply();
        tableHouse.setHouseIdForAutoRoom((lastEnteredHouseId + 1));
        houseViewModal.insertHouse(tableHouse);
    }

    // Get the dialogs
    private MaterialAlertDialogBuilder getSaveDialoge() {

        return (new SaveDialog(requireActivity(), houseEntryBinding.getRoot()))
                .getSaveDialogue(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveHouseData();
                                dialog.dismiss();
                                Navigation.findNavController(houseEntryBinding.getRoot()).navigateUp();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Navigation.findNavController(houseEntryBinding.getRoot()).navigateUp();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

    }

    private MaterialAlertDialogBuilder getExitDialoge() {

        return (new SaveDialog(requireActivity(), houseEntryBinding.getRoot()))
                .getCancelDialog(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Navigation.findNavController(houseEntryBinding.getRoot()).navigateUp();
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