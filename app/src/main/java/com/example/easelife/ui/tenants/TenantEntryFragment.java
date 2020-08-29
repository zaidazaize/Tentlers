package com.example.easelife.ui.tenants;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.example.easelife.R;
import com.example.easelife.data.HouseViewModal;
import com.example.easelife.data.tables.meters.GetLastMeterReading;
import com.example.easelife.data.tables.queryobjects.HouseNameAndId;
import com.example.easelife.data.tables.rooms.RoomNoNameId;
import com.example.easelife.data.tables.tenants.TenantsPersonal;
import com.example.easelife.databinding.FragmentTenantEntryBinding;
import com.example.easelife.ui.home.SaveDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class TenantEntryFragment extends Fragment implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemSelectedListener {

    public TenantEntryFragment() {
    }

    FragmentTenantEntryBinding tenantEntryBinding;
    HouseViewModal viewModal;

    /*
     * object Holding the verified tenant data
     */
    TenantsPersonal tenantsPersonal = new TenantsPersonal();

    /* Array adapters for the room and house spinner. And the */
    ArrayAdapter<String> roomAdapter, houseAdapter;

    /*
    the variables hold the all the house id and name  And the rooms id  room name of the selected house
     * These objects are used to fetch the ids of houses and rooms selected in the spinner.
     * */
    List<HouseNameAndId> mhouseNameAndIdList;
    List<RoomNoNameId> mroomNoNameIds;

    /* This object holds the room chosen to be allotted to tenant*/
    RoomNoNameId choosenRoom;
    private boolean isHosueAvailable;/* Tells whether is there any house available that has some rooms which can be allotted.*/

    /* Snack bar object*/
    Snackbar snackbar;

    /* Variable holding last meter reading of the room.*/
    long lastMeterReading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);
        /*
         * Handle back button pressed event by using on back pressed dispatcher.
         * show the exit dialogue when  back button is pressed.
         */
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getExitDialoge().show();
            }
        });

        /* Initialise the array adapters*/
        roomAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
        houseAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        /*
         * Instantiate the binding class object.
         */
        tenantEntryBinding = FragmentTenantEntryBinding.inflate(getLayoutInflater(), container, false);

        /*
         * Setting the toolbar and navigation item click listener
         * hide the app bar and bottom navigation via a callback in main activity
         */
        tenantEntryBinding.toolbarTenantEnter.setOnMenuItemClickListener(this);
        /*
         * Show the exit dialogue when the navigation icon is clicked.
         */
        tenantEntryBinding.toolbarTenantEnter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExitDialoge().show();
            }
        });
        /*
         * Switch checked change listeners.
         * switch for controlling the visibility of the personal info fields.
         * If the switch is closed the
         * the personal info in tenant object is set to false;
         * Initialise the switch to true after adding the listener. At the end of oncreateview;
         */
        tenantEntryBinding.switchTenantPersonalInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tenantEntryBinding.lenearLayoutAddTenantTenantPersonalInfo.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                tenantsPersonal.setIsPersonalInfo(isChecked);
            }
        });


        /* Listener for Setting the gender when radio button in clicked default gender in Male
         * The value is set to male at the end of this method.
         * Note in save method if the personal info is not enabled then gender is set to No gender*/
        tenantEntryBinding.radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_male:
                        tenantsPersonal.setGender(TenantsPersonal.MALE);
                        break;
                    case R.id.radioButton_female:
                        tenantsPersonal.setGender(TenantsPersonal.FEMALE);
                        break;
                    case R.id.radioButton_other:
                        tenantsPersonal.setGender(TenantsPersonal.OTHER);
                        break;
                    default:
                        tenantsPersonal.setGender(TenantsPersonal.NOGENDER);
                }
            }
        });

        /*
         * Switch for controlling the visibility of add rooms.
         * Resets the electricity meter switch when this switch state is changed.
         */
        tenantEntryBinding.switchTenantAllotRooms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tenantEntryBinding.linearLayoutAllotRooms.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                setAutoGenerateSwitchAndInput();
            }
        });

        /* update the room and house spinner data*/
        /* This adds a house list in which those houses are listed which have atleast one room */
        viewModal.getHouseNameIdTEspinner().observe(getViewLifecycleOwner(), new Observer<List<HouseNameAndId>>() {
            @Override
            public void onChanged(List<HouseNameAndId> houseNameAndIdList) {
                //TODO: Show snack bar that there are no houses that can be added.
                if (houseNameAndIdList.isEmpty()) {/*If there is no house available then allot meter will be set to false.*/
                    isHosueAvailable = false;
                    tenantEntryBinding.switchTenantAllotRooms.setChecked(false);
                    tenantEntryBinding.switchTenantAllotRooms.setEnabled(false);
                }else isHosueAvailable = true;
                houseAdapter.addAll(getHouseNamearray(houseNameAndIdList));
                houseAdapter.notifyDataSetChanged();
                mhouseNameAndIdList = houseNameAndIdList;
            }

            private ArrayList<String> getHouseNamearray(List<HouseNameAndId> houseNameId) {
                ArrayList<String> houseArray = new ArrayList<>();
                for (HouseNameAndId s : houseNameId) {
                    houseArray.add(s.houseName);
                }
                return houseArray;
            }
        });

        /* set the spinner values in the room and house spinner
         * Add the the item select listener to the house spinner which updates the room names*/
        tenantEntryBinding.spinnerAddHouse.setAdapter(houseAdapter);
        tenantEntryBinding.spinnerAddHouse.setOnItemSelectedListener(this);

        /*
         *Set the adapter and click listener on the room spinner
         * The values of switch in add electric meter will be re enitialised if room is changed.
         */
        tenantEntryBinding.spinnerAddRoom.setAdapter(roomAdapter);
        tenantEntryBinding.spinnerAddRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tenantsPersonal.setRoomId(mroomNoNameIds.get(position).roomId);
                choosenRoom = mroomNoNameIds.get(position);
                setAutoGenerateSwitchAndInput();
                if (choosenRoom.isMeterEnabled) {//fetch for reading only if the meter is enabled;
                    viewModal.getLastEnteredMeterEntry(new GetLastMeterReading().setRoomId(choosenRoom.roomId)).observe(getViewLifecycleOwner(),
                            new Observer<Long[]>() {
                                @Override
                                public void onChanged(Long[] longs) {
                                    if (longs.length != 0) {
                                        lastMeterReading = longs[0];
                                        // TODO: update the last meter reading in the text view.
                                        // TODO: add a text view below the meter initial meter reading to show last meter reading.
                                    }
                                }
                            });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /*
         * Switch for payment scheme. This controls the visibility of whole payment scheme.
         */
        tenantEntryBinding.switchTenantPaymentSheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tenantEntryBinding.linearLayoutPaymentScheme.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        /*
         * Switch for electricity charges controlling the visibility of whole electricity charge entry layout.
         */
        tenantEntryBinding.switchTenantAddElectricityCharges.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tenantEntryBinding.linearLayoutAddElectricityCharges.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });


        /*
         * controlling the auto or manual entry of the electricity charges.
         */
        tenantEntryBinding.switchTenantAddElectricityChargesAutoGenerate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    tenantEntryBinding.switchTenantAddElectricityEnterManually.setChecked(true);
                    tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setVisibility(View.GONE);
                    return;
                }
                if (!isHosueAvailable) {/* Shows snack bar if there are no houses to which tenant can be added.*/
                    tenantEntryBinding.switchTenantAddElectricityChargesAutoGenerate.setChecked(false);
                    snackbar = Snackbar.make(tenantEntryBinding.coordinatorLayoutTenantEntry,
                            R.string.no_house_available_to_add_tenant,
                            BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                if (!tenantEntryBinding.switchTenantAllotRooms.isChecked()) {/* The room is not alloted Show snack bar to allot the room*/
                    snackbar = Snackbar.make(tenantEntryBinding.coordinatorLayoutTenantEntry,
                            R.string.room_not_alloted,
                            BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.setAction("Allot Rooms", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tenantEntryBinding.switchTenantAllotRooms.setChecked(true);
                        }
                    })
                            .show();
                    tenantEntryBinding.switchTenantAddElectricityChargesAutoGenerate.setChecked(false);
                    return;
                }
                if (choosenRoom != null && choosenRoom.isMeterEnabled) {/* Make the visibility of input field visible and set the manual switch to off*/
                    tenantEntryBinding.switchTenantAddElectricityEnterManually.setChecked(false);
                    tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setVisibility(View.VISIBLE);
                } else {/* Meter is not enabled in the room. snack bar to enable the room*/
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    snackbar = Snackbar.make(tenantEntryBinding.coordinatorLayoutTenantEntry, R.string.meter_is_missing_add_meter_to_the_selected_room, BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.setAction("Add Meter", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /* Show the dialog here. to add the meter.*/
                        }
                    })
                            .show();
                    tenantEntryBinding.switchTenantAddElectricityChargesAutoGenerate.setChecked(false);
                }
            }
        });

        /*tenantEntryBinding.switchTenantAddElectricityEnterManually.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tenantsPersonal.setMeterPay(!isChecked);
                tenantsPersonal.setNonMeterPay(isChecked);
                tenantEntryBinding.switchTenantAddElectricityChargesAutoGenerate.setChecked(!isChecked);
            }
        });*/

        /*
         * This watcher inspects the meter reading entered in the edittext and show error if the reading is less than
         * the previous reading
         */
//        tenantEntryBinding.textInputEditTextInitialMeterReading.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().length() != 0) {
//                    if (Integer.parseInt(s.toString()) <= lastMeterReading) {
//                        tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setError(getString(R.string.invalid_entry_meter_reading_is_less_than_previous_reading));
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.toString().length() != 0) {
//                    if (Integer.parseInt(s.toString()) >= lastMeterReading) {
//                        tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setError("");
//                    }
//                }
//            }
//        });

        /* Initialisig all the switch buttons.*/
        tenantEntryBinding.switchTenantPersonalInfo.setChecked(true);
        tenantEntryBinding.radioGroupGender.check(R.id.radioButton_male);

        return tenantEntryBinding.getRoot();
    }

    /* Visibility controling meathods*/
    private void setAutoGenerateSwitchAndInput() {
        tenantEntryBinding.switchTenantAddElectricityChargesAutoGenerate.setChecked(false);
        tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setVisibility(View.GONE);
        tenantEntryBinding.switchTenantAddElectricityEnterManually.setChecked(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tenantEntryBinding = null;
    }

    /*
     * handling Click on Save button
     * Show the save dialogue if the entered data is valid.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.meuitem_house_save) {
            if (isTenantValid()) {
                getSaveDialoge().show();
            }
        }
        return true;
    }

    /*
     * Check For data Validity.
     */
    private boolean isTenantValid() {
        return isNameValid() && isPaymentSchemeValid();
    }

    /*
     * Check for validity of tenant name
     */
    private boolean isNameValid() {
        /*
         * name entered in the name entry field should not be null.
         */
        String tenantName = Objects.requireNonNull(tenantEntryBinding.textInputEditTextOutlinedTenantName.getText()).toString();
        if (tenantName.length() == 0) {
            tenantEntryBinding.textInputLayoutOutlinedTenantName.setError(getString(R.string.error_field_recquired));
            tenantEntryBinding.textInputLayoutOutlinedTenantName.requestFocus();
            return false;
        } else {
            tenantsPersonal.tenantName = tenantName;
            tenantEntryBinding.textInputLayoutOutlinedTenantName.setError("");
        }

        return true;
    }

    /* Meathods Checks and saves the payment information entered by the user in tenant object.*/
    private boolean isPaymentSchemeValid() {
        if (!tenantEntryBinding.switchTenantPaymentSheme.isChecked()) {
            tenantsPersonal.mFixedCharges = 0;
            tenantsPersonal.setDiscardAllMeterPay();
            return true;
        }

        String monthlyCost = Objects.requireNonNull(tenantEntryBinding.textInputEditTextOutlinedMonthlyFixedCharge.getText()).toString();
        if (monthlyCost.length() != 0) {
            tenantsPersonal.mFixedCharges = Float.parseFloat(monthlyCost);
        }
        if (!tenantEntryBinding.switchTenantAddElectricityCharges.isChecked()) {
            return true;
        }
        if (tenantEntryBinding.switchTenantAddElectricityChargesAutoGenerate.isChecked()) {
            tenantsPersonal.setMeterPay(true);
            String initialMeterReading = Objects.requireNonNull(tenantEntryBinding.textInputEditTextInitialMeterReading.getText()).toString();
            if (initialMeterReading.length() == 0) {/* Initial reading field cannot be empty*/
                tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setError(getString(R.string.error_field_recquired));
                return false;
            } else {/* Initial reading cannot be less than previous reading*/
                if (Integer.parseInt(initialMeterReading) < lastMeterReading) {
                    tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setError(getString(R.string.invalid_entry_meter_reading_is_less_than_last_entered_reading));
                    return false;
                } else {
                    /* enter the meter informations in the tenant personal table this is used to
                     * Meters table if meter pay is selected.*/
                    tenantsPersonal.allMetersData.lastMeterReading = Integer.parseInt(initialMeterReading);
                    tenantsPersonal.allMetersData.meterId = choosenRoom.meterId;
                    return true;
                }
            }
        } else {
            tenantsPersonal.setNonMeterPay(true);
            return true;
        }
    }

    private void setFamilyMemberAgeGender() {
        if (tenantEntryBinding.switchTenantPersonalInfo.isChecked()) {
            String age = Objects.requireNonNull(tenantEntryBinding.textInputEditTextOutlinedTenantAge.getText()).toString();
            if (age.length() != 0) {
                tenantsPersonal.setAge(Integer.parseInt(age));
            }
            String familymembers = Objects.requireNonNull(tenantEntryBinding.textInputEditTextOutlinedTenantFamilyMembers.getText()).toString();
            if (familymembers.length() != 0) {
                tenantsPersonal.familyMembers = Integer.parseInt(familymembers);
            }
        } else {
            tenantsPersonal.setAge(0);
            tenantsPersonal.setGender(TenantsPersonal.NOGENDER);
            tenantsPersonal.familyMembers = 0;
        }
    }

    /* If the switch is closed set the values to 0.*/
    private void setAllotedRoom() {
        if (!tenantEntryBinding.switchTenantAllotRooms.isChecked()) {
            tenantsPersonal.setRoomId(0);
            tenantsPersonal.setHouseId(0);
            tenantsPersonal.isRoomAlloted = false;
        } else {
            tenantsPersonal.isRoomAlloted = true;
        }
    }

    /*
     * Method for handling the saving of Tenant data
     */
    private void saveTenantData() {
        setFamilyMemberAgeGender();
        setAllotedRoom();
        viewModal.insertNewtenant(tenantsPersonal);
    }

    /*
     * Getting save or exit dilogue
     */
    private MaterialAlertDialogBuilder getSaveDialoge() {

        return (new SaveDialog(requireActivity(), tenantEntryBinding.getRoot()))
                .getSaveDialogue(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveTenantData();
                                dialog.dismiss();
                                Navigation.findNavController(tenantEntryBinding.getRoot()).navigateUp();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Navigation.findNavController(tenantEntryBinding.getRoot()).navigateUp();
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

        return (new SaveDialog(requireActivity(), tenantEntryBinding.getRoot()))
                .getCancelDialog(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Navigation.findNavController(tenantEntryBinding.getRoot()).navigateUp();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
    }

    /* these listener methods updates the room spinner.*/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /* Quarry the room info based on the house chosen and change the values in the spinner*/
        tenantsPersonal.setHouseId(mhouseNameAndIdList.get(position).houseId);
        setAutoGenerateSwitchAndInput();
        roomAdapter.clear();
        viewModal.getRoomNoNameID(mhouseNameAndIdList.get(position).houseId, false)
                .observe(getViewLifecycleOwner(), new Observer<List<RoomNoNameId>>() {
            @Override
            public void onChanged(List<RoomNoNameId> roomNoNameIds) {
                if (!roomNoNameIds.isEmpty()) {/* if the got room list is empty no need to add the list to spinner.*/
                    roomAdapter.addAll(getHouseNamearray(roomNoNameIds));
                    roomAdapter.notifyDataSetChanged();
                }
                mroomNoNameIds = roomNoNameIds;
            }

            private ArrayList<String> getHouseNamearray(List<RoomNoNameId> roomnonameid) {
                ArrayList<String> houseArray = new ArrayList<>();
                for (RoomNoNameId s : roomnonameid) {
                    houseArray.add(s.roomName);
                }
                return houseArray;
            }

        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}