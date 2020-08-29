package com.example.easelife.ui.home;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.easelife.R;
import com.example.easelife.data.HouseViewModal;
import com.example.easelife.data.tables.Address;
import com.example.easelife.data.tables.TableHouse;
import com.example.easelife.data.tables.queryobjects.HouseNameMeterId;
import com.example.easelife.databinding.FragmentHouseEntryBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
     * This object holds all the data related to the house and
     * is used create a new row in the TableHouse
     */
    private TableHouse tableHouse = new TableHouse();

    /*
     *The Object Holds the data of address enterd while creating the house
     */
    private Address address = new Address();

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
    HouseNameMeterId[] houseNameMeterIdList;

    /*
     *  This callback is attached to the OnBackPressedDidpatcher which is responsible for showing the
     * exit dialogue when the user pressed the back button.
     */
    OnBackPressedCallback callback;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Enitialising the viewModal here.
         */
        houseViewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        /*
         * Getting the list of all the house Names and meterids from the database
         * these are specific to the users and can be null.*/
        houseViewModal.getHouseNameMeterId().observe(this, new Observer<HouseNameMeterId[]>() {
            @Override
            public void onChanged(HouseNameMeterId[] houseNameMeterIds) {
                houseNameMeterIdList = houseNameMeterIds;
            }
        });

        /*
         *Defining the callback and attaching it to the backpresseddispather
         * to handle the back button pressed events.
         */
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getExitDialoge().show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*
         * detaching the binding  for avoiding the memory leak.
         **/
        houseEntryBinding = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*
         * Initialize the binding objecty and inflate the layout here.
         */
        houseEntryBinding = FragmentHouseEntryBinding.inflate(inflater, container, false);

        /*
         * Set the house name based on the last entered house id stored in the view modal.
         * Incrementing 1 in the layout of the list
         */

        if (houseViewModal.lastEnteredHouseId != 0) {
            houseEntryBinding.textInputEditTextOutlinedHousename.setText(String.format("House%d", houseViewModal.lastEnteredHouseId + 1));
        } else {
            houseEntryBinding.textInputEditTextOutlinedHousename.setText("House1");
        }



        /*
         * Assing the hangler to the cances sign .
         * It is responsible for showing the exit dialogue.
         */
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
                if (item.getItemId() == R.id.meuitem_house_save) {
                    if (checkForDataValidity()) {
                        getSaveDialoge().show();
                    }
                }
                return true;
            }
        });
//        MainActivity mainActivity = (MainActivity)getActivity();-
//        (mainActivity).setSupportActionBar(houseEntryBinding.toolbarHouseEnter);
//        mainActivity.

        /*
         * Switch for handling the address layout visibility.
         */
        houseEntryBinding.switchAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    houseEntryBinding.linearLayoutAddressLayout.setVisibility(View.VISIBLE);
                } else houseEntryBinding.linearLayoutAddressLayout.setVisibility(View.GONE);
            }
        });


        /*
         * Switch for handlign the edit text visibility for entering the no of rooms
         */
        houseEntryBinding.switchNoOfRooms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    houseEntryBinding.textInputLayoutHouseNoofRooms.setVisibility(View.VISIBLE);
                } else houseEntryBinding.textInputLayoutHouseNoofRooms.setVisibility(View.GONE);
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
                if (tableHouse.setNoOfRoomsFromString(s.toString()) > 99) {
                    houseEntryBinding.textInputLayoutHouseNoofRooms.setError(getString(R.string.no_of_rooms_must_be_less_than_100));
                } else {
                    houseEntryBinding.textInputLayoutHouseNoofRooms.setError("");
                }
            }
        });

        /*
         * Switch for handlign the permission for assigning the meter.
         */
        houseEntryBinding.switchElectricMeterPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    houseEntryBinding.layoutMeterNumber.setVisibility(View.VISIBLE);
                } else houseEntryBinding.layoutMeterNumber.setVisibility(View.GONE);
            }
        });

        /*
         * Switch for either auto generate meter number or manually enter the meter number.
         */
        houseEntryBinding.switchElectricMeterNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    houseEntryBinding.textInputLayoutHouseMeterNo.setVisibility(View.VISIBLE);
                    houseEntryBinding.switchElectricMeterNumberSystemDecide.setChecked(false);
                } else {
                    houseEntryBinding.textInputLayoutHouseMeterNo.setVisibility(View.GONE);
                    houseEntryBinding.switchElectricMeterNumberSystemDecide.setChecked(true);
                }
            }
        });

        return houseEntryBinding.getRoot();
    }

    /*
     * This method checks for the validity for the data entered.
     */
    private boolean checkForDataValidity() {

        return isHouseNamevalid() & isAdressValid() & isMeterNumberValid() & isHouseValid();
    }

    /*
     * Checks the validity and nullability hosue name
     * it is also responsible for showing the error messages.
     */
    private boolean isHouseValid() {
        if (houseEntryBinding.switchNoOfRooms.isChecked()) {
            String houseRooms = houseEntryBinding.textInputEditTextHouseNoOfRooms.getText().toString();

            if (houseRooms.length() != 0) {
                if (Integer.parseInt(houseRooms) < 100) {
                    houseEntryBinding.textInputLayoutHouseNoofRooms.setError("");
                    tableHouse.setNoOfRooms(Integer.parseInt(houseRooms));
                    tableHouse.isRoomAutoGenerated = true;
                    return true;
                } else {
                    houseEntryBinding.textInputLayoutHouseNoofRooms.setError(getString(R.string.no_of_rooms_must_be_less_than_100));
                    return false;
                }

            } else {
                houseEntryBinding.textInputLayoutHouseNoofRooms.setError(getString(R.string.error_field_recquired));
                return false;
            }

        } else {
            tableHouse.isRoomAutoGenerated = false;
        }
        return true;
    }

    private boolean isMeterNumberValid() {
        if (houseEntryBinding.switchElectricMeterPermission.isChecked()) {
            tableHouse.setIncludeMeter(true);

            if (houseEntryBinding.switchElectricMeterNumber.isChecked()) {

                String housemeterno = houseEntryBinding.textInputEditTextHouseElectricMeterNo.getText().toString();

                if (housemeterno.length() != 0) {
                    long housemeternolong = Long.parseLong(housemeterno);
                    if (isHouseMeterunique(housemeternolong)) {
                        tableHouse.setMeterid(housemeternolong);
                        houseEntryBinding.textInputLayoutHouseMeterNo.setError("");
                        return true;

                    } else {
                        houseEntryBinding.textInputLayoutHouseMeterNo.setError(getString(R.string.meter_no_already_exits));
                        return false;
                    }

                } else {
                    houseEntryBinding.textInputLayoutHouseMeterNo.setError(getString(R.string.error_field_recquired));
                    return false;
                }

            }
            if (houseEntryBinding.switchElectricMeterNumberSystemDecide.isChecked()) {
                tableHouse.ismetersystemgenerated = true;
            }
        }
        return true;

    }

    private boolean isHouseNamevalid() {
        String housename = houseEntryBinding.textInputEditTextOutlinedHousename.getText().toString();
        if (housename.length() != 0) {
            houseEntryBinding.textInputLayoutOutlinedHouseName.setError("");
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

    private boolean isHouseunique(String gothousename) {
        if (houseNameMeterIdList != null) {
            for (HouseNameMeterId s : houseNameMeterIdList) {
                if (gothousename.equals(s.housename)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isHouseMeterunique(long gotmeterno) {
        if (houseNameMeterIdList != null) {
            for (HouseNameMeterId s : houseNameMeterIdList) {
                if (gotmeterno == s.meterId) {
                    return false;
                }
            }
        }
        return true;
    }


    // Save the House information
    private void saveHouseData() {

        /*
         * this will be generate new meter id based on the last id stored in the shared Prefferences.
         * If the rooms are to be auto generated then it will be done in the repository.
         */
        if (tableHouse.ismetersystemgenerated) {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.base_ids_sharedpreferences_file), Context.MODE_PRIVATE);
            tableHouse.setMeterid(sharedPreferences.getLong(getString(R.string.system_generated_meterid_last_entry), 100000) + 1);
            sharedPreferences.edit()
                    .putLong(getString(R.string.system_generated_meterid_last_entry), tableHouse.meterid)
                    .apply();
        }
        tableHouse.houseIdForAutoRoom = (houseViewModal.lastEnteredHouseId + 1);
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