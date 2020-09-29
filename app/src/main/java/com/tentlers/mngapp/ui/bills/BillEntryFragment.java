package com.tentlers.mngapp.ui.bills;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.bills.Bills;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.meters.GetLastMeterReading;
import com.tentlers.mngapp.data.tables.meters.LastReadingWithDate;
import com.tentlers.mngapp.data.tables.tenants.TenantBillEntry;
import com.tentlers.mngapp.data.tables.tenants.TenantNameHouseRoom;
import com.tentlers.mngapp.databinding.FragmentBillEntryBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class BillEntryFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    HouseViewModal viewModal;
    FragmentBillEntryBinding billEntryBinding;
    ArrayAdapter<String> arrayAdapter;
    List<TenantNameHouseRoom> tenantNameHouseRoomsList;
    TenantBillEntry choosenTenant;

    Bills createdbill = new Bills();

    public BillEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModal = new ViewModelProvider(this).get(HouseViewModal.class);
        arrayAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getCancesDialog().show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        billEntryBinding = FragmentBillEntryBinding.inflate(getLayoutInflater(), container, false);

        billEntryBinding.toolbarBillEnter.setNavigationOnClickListener(this);
        billEntryBinding.toolbarBillEnter.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (isBillDataValid()) {//Show save data only when Data is valid.
                    getSaveDialog().show();
                } else {
                    Snackbar.make(billEntryBinding.billEntryCoordinatorLayout,
                            "Bill data entered is invalid", BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                }
                return true;
            }
        });

        billEntryBinding.spinnerBillsTenantsName.setAdapter(arrayAdapter);
        billEntryBinding.spinnerBillsTenantsName.setOnItemSelectedListener(this);
        /*
         * Get all the tenants and add all the tenants name to spinner.
         * In on click method handle when a item is clicked.
         */
        viewModal.getAllTenantNHR(true).observe(getViewLifecycleOwner(),
                new Observer<List<TenantNameHouseRoom>>() {
                    @Override
                    public void onChanged(List<TenantNameHouseRoom> tenantNameHouseRooms) {
                        //TODO: show dialog if no tenant is added.
                        tenantNameHouseRoomsList = tenantNameHouseRooms;
                        arrayAdapter.addAll(getHouseNamearray(tenantNameHouseRooms));
                    }

                    private ArrayList<String> getHouseNamearray(List<TenantNameHouseRoom> tenantNameHouseRooms) {
                        ArrayList<String> tenantNameArray = new ArrayList<>();
                        for (TenantNameHouseRoom s : tenantNameHouseRooms) {
                            tenantNameArray.add(s.tenantName);
                        }
                        return tenantNameArray;
                    }
                });
        /* updates the total amount in the total amt view.*/
        /* by fixed charge input field.*/
        billEntryBinding.textInputEditTextOutlinedMonthlyFixedCharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0) {
                    createdbill.monthlycharge = Float.parseFloat(s.toString());
                    setTotalAmount();
                }
            }
        });

        /* By aditional charge field*/
        billEntryBinding.textInputEditTextOutlinedAditionalCharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0) {
                    createdbill.additionalcharge = Float.parseFloat(s.toString());
                    setTotalAmount();
                }
            }
        });

        /* by Manual electricity charge entry*/
        billEntryBinding.textInputEditTextOutlinedMonthlyElectricCharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0) {
                    createdbill.manuallyEnteredElectricCost = Float.parseFloat(s.toString());
                    setTotalAmount();
                }
            }
        });

        /* by per unit cost*/
        billEntryBinding.textInputEditTextOutlinedMonthlyElectricPerUnitCharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0) {
                    createdbill.perUnitcoat = Float.parseFloat(s.toString());
                    setTotalAmount();
                }
            }
        });

        /* By meter reading*/
        // checks whether the initial reading is correct or not then updates the total amount.
        billEntryBinding.billEntryTextInputEditTextFinalMeterReading.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    return;
                }
                if (Long.parseLong(s.toString()) < createdbill.initialMeterR) {
                    billEntryBinding.billEntryTextInputEditTextOutlinedFinalMeterReading
                            .setError(getString(R.string.invalid_entry_meter_reading_is_less_than_last_entered_reading));
                } else {
                    billEntryBinding.billEntryTextInputEditTextOutlinedFinalMeterReading.setError("");
                    createdbill.endMeterR = Long.parseLong(s.toString());
                    setTotalAmount();
                }

            }
        });

        return billEntryBinding.getRoot();
    }

    /* responce to the tenant name selected.
     * makes visible the meter or bills data.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final TenantNameHouseRoom tenantNHR = tenantNameHouseRoomsList.get(position);
        billEntryBinding.billEntryHouseName.setText(tenantNHR.houseName);
        billEntryBinding.billEntryRoomName.setText(tenantNHR.roomName);
        createdbill.tenantId = tenantNHR.tenantId;
        viewModal.getSelectedTenantForBill(tenantNHR.tenantId).observe(getViewLifecycleOwner(),
                new Observer<TenantBillEntry>() {
                    @Override
                    public void onChanged(TenantBillEntry tenantBillEntry) {
                        choosenTenant = tenantBillEntry;
                        //TODO: Optional: change the helper string if the charge is fixed charge.
                        /*Set the fixed charge.*/
                        billEntryBinding.textInputEditTextOutlinedMonthlyFixedCharge.setText(String.valueOf(tenantBillEntry.mFixedCharges));
                        createdbill.monthlycharge = tenantBillEntry.mFixedCharges;

                        /*Electricity charge entry, heading visibility*/
                        billEntryBinding.billEntryTextviewElectricityCharges.setVisibility(/* if any one of the either auto or manual electricity is enabledn then the heading of the add electricity charge will be visible.*/
                                tenantBillEntry.meterPay | tenantBillEntry.nonMeterPay ? View.VISIBLE : View.GONE);

                        /*set manual electric charge heading visibility.*/
                        billEntryBinding.billEntryManuallyEnterElectricCost.setVisibility(tenantBillEntry.nonMeterPay ? View.VISIBLE : View.GONE);

                        /*set manual charge entry edit text visibility. */
                        billEntryBinding.textInputLayoutOutlinedMonthlyElectricCharge.setVisibility(
                                tenantBillEntry.nonMeterPay ? View.VISIBLE : View.GONE);

                        /*Set auto generate charge heading visibility*/
                        billEntryBinding.billEntryAutoGenerateElectricCost.setVisibility(tenantBillEntry.meterPay ? View.VISIBLE : View.GONE);

                        /*Set auto generate charge edittext visibility */
                        billEntryBinding.textInputLayoutOutlinedMonthlyElectricPerUnitCharge.setVisibility(
                                tenantBillEntry.meterPay ? View.VISIBLE : View.GONE);

                        /**/
                        billEntryBinding.billEntryTextInputEditTextOutlinedFinalMeterReading.setVisibility(
                                tenantBillEntry.meterPay ? View.VISIBLE : View.GONE);

                        billEntryBinding.billEntryLiniarlayoutLastmeterReading.setVisibility(
                                tenantBillEntry.meterPay ? View.VISIBLE : View.GONE);

                        if (tenantBillEntry.meterPay) {
                            viewModal.getLastEnteredMeterEntry(new GetLastMeterReading().setRoomId(tenantBillEntry.roomId))
                                    .observe(getViewLifecycleOwner(), new Observer<LastReadingWithDate>() {
                                        @Override
                                        public void onChanged(LastReadingWithDate lastReadingWithDate) {
                                            billEntryBinding.billEntryStartMeterReading.setText(String.valueOf(lastReadingWithDate.getLastMeterReading()));
                                            createdbill.initialMeterR = lastReadingWithDate.getLastMeterReading();
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setTotalAmount() {//set nothing if total is less than 0;
        billEntryBinding.billEntryTotalAmt.setText(createdbill.getTotalAmt() < 0 ? "" : String.valueOf(createdbill.getTotalAmt()));
    }

    private boolean isBillDataValid() {
        String monthlyCharge = Objects.requireNonNull(billEntryBinding.textInputEditTextOutlinedMonthlyFixedCharge.getText()).toString();
        String aditionalcharge = Objects.requireNonNull(billEntryBinding.textInputEditTextOutlinedAditionalCharge.getText()).toString();
        if (monthlyCharge.length() != 0) {
            createdbill.monthlycharge = Float.parseFloat(monthlyCharge);
        }
        if (aditionalcharge.length() != 0) {
            createdbill.additionalcharge = Float.parseFloat(aditionalcharge);
        } else createdbill.additionalcharge = 0;

        if (choosenTenant != null) {
            if (choosenTenant.meterPay) {
                createdbill.metersData.setRoomid(choosenTenant.roomId);
                return isMeterPayValid() && isperUnitvalid();
            }
            if (choosenTenant.nonMeterPay) {
                return isNonMeterPayValid();
            }

        }
        return true;
    }

    private boolean isMeterPayValid() {
        //TODO: add switch to either add metered bill or unmetered bill optionally.
        String value = Objects.requireNonNull(billEntryBinding.billEntryTextInputEditTextFinalMeterReading.getText()).toString();
        if (value.length() != 0) {
            /* Set the meter pay of bill here.*/
            createdbill.setEndMeterR(Long.parseLong(value));
            if (createdbill.endMeterR < createdbill.initialMeterR) {
                billEntryBinding.billEntryTextInputEditTextOutlinedFinalMeterReading
                        .setError(getText(R.string.invalid_entry_meter_reading_is_less_than_last_entered_reading));
                return false;
            } else {
                billEntryBinding.billEntryTextInputEditTextOutlinedFinalMeterReading.setError("");
                createdbill.getMetersData().setOnlyReadingState(AllMetersData.BILLED);/* Set the state of the meter reading.*/
                Log.d("allmetersdata", String.valueOf(createdbill.getMetersData().getReadingState()));
                return true;
            }
        } else return true;

    }

    private boolean isNonMeterPayValid() {
        if (choosenTenant.nonMeterPay) {
            String value2 = Objects.requireNonNull(billEntryBinding.textInputEditTextOutlinedMonthlyElectricCharge.getText()).toString();
            if (value2.length() != 0) {
                createdbill.setManuallyEnteredElectricCost(Float.parseFloat(value2));
            }
        }
        return true;
    }

    private boolean isperUnitvalid() {
        String perunitcost = Objects.requireNonNull(billEntryBinding.textInputEditTextOutlinedMonthlyElectricPerUnitCharge.getText()).toString();
        if (perunitcost.length() == 0) {
            billEntryBinding.textInputEditTextOutlinedMonthlyElectricPerUnitCharge.requestFocus();
            billEntryBinding.textInputLayoutOutlinedMonthlyElectricPerUnitCharge.setError(getText(R.string.error_field_recquired));
            return false;
        } else {
            createdbill.perUnitcoat = Float.parseFloat(perunitcost);
            billEntryBinding.textInputLayoutOutlinedMonthlyElectricPerUnitCharge.setError("");
            return true;
        }
    }

    private void generateBill() {
        createdbill.setCreateDate();
        if (createdbill.ismeterPay) {
            createdbill.electricCost = createdbill.getMeteredElectricityCost();
            Log.d("billentry", String.valueOf(createdbill.electricCost));
            Log.d("billEntry", String.valueOf(createdbill.metersData.getLastMeterReading()));
        }
        createdbill.setTotalAmt();
        viewModal.insertNewBill(createdbill);
    }

    @Override
    public void onClick(View v) {
        getCancesDialog().show();
    }

    @NonNull
    private MaterialAlertDialogBuilder getSaveDialog() {
        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Generate Bill ?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        generateBill();
                        dialog.dismiss();
                        Navigation.findNavController(billEntryBinding.getRoot()).navigateUp();
                    }
                })
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
    }

    @NonNull
    private MaterialAlertDialogBuilder getCancesDialog() {

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Cancel Bill ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Navigation.findNavController(billEntryBinding.getRoot()).navigateUp();
                    }
                })
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

    }

}