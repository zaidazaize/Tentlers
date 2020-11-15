package com.tentlers.mngapp.ui.meters.editmeter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.meters.MeterEditType;
import com.tentlers.mngapp.databinding.FragmentEditMeterBinding;

import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class EditMeterFragment extends Fragment implements Toolbar.OnMenuItemClickListener, DialogInterface.OnClickListener {

    HouseViewModal viewModal;
    MeterEditType choosenMeter;
    List<Long> allMetersList;
    FragmentEditMeterBinding binding;

    public EditMeterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*set the back button dispatcher*/
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getExitDialog().show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentEditMeterBinding.inflate(getLayoutInflater(), container, false);

        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);
        choosenMeter = viewModal.getMeterEditType();
        viewModal.getAllMeterNos().observe(getViewLifecycleOwner(), new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> longs) {
                allMetersList = longs;
            }
        });

        switch (choosenMeter.getMeterEntrytype()) {
            case MeterEditType.ENTRY_NEW:
                setNewEntrytype();
                break;
            case MeterEditType.ENTRY_OLD:
                setOldMeterEntryType();
                break;
        }


        binding.editmeterInclude.switchElectricMeterNumberManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.editmeterInclude.editOutlineMeterMeterNo.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                binding.editmeterInclude.switchElectricMeterNumberSystemDecide.setChecked(!isChecked);
            }
        });

        /*set on cancel button click listener*/
        binding.editmeterInclude.editMeterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExitDialog().show();
            }
        });

        /*set positive buttonclick listner*/
        binding.editmeterInclude.editMeterToolbar.setOnMenuItemClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean isDataValid() {
        switch (choosenMeter.getMeterEntrytype()) {
            case MeterEditType.ENTRY_NEW:
                String lastreading = binding.editmeterInclude.editInitialMeterReading.getText().toString();
                if (lastreading.length() != 0) {
                    choosenMeter.getUpdatedMeter().lastReading = Long.parseLong(lastreading);
                }
                return isNewMeterDataValid();

            case MeterEditType.ENTRY_OLD:
                return isOldMeterDataValid();
            default:
                return false;
        }
    }

    private boolean isNewMeterDataValid() {
        if (binding.editmeterInclude.switchElectricMeterNumberSystemDecide.isChecked()) {
            /*the system generated meter will be unique*/
            choosenMeter.getUpdatedMeter().isSystemdecide = true;
            return true;
        }
        if (binding.editmeterInclude.switchElectricMeterNumberManual.isChecked()) {
            String meterno = binding.editmeterInclude.editMeterNo.getText().toString();
            if (meterno.length() != 0) {/*if length is zero raise the error*/
                long meterlong = Long.parseLong(meterno);
                if (isMeterUnique(meterlong)) {/*if meter is not unique show an error.*/
                    choosenMeter.getUpdatedMeter().setMeterNo(meterlong);
                    binding.editmeterInclude.editOutlineMeterMeterNo.setError("");
                    return true;
                } else {
                    binding.editmeterInclude.editOutlineMeterMeterNo.setError(getString(R.string.meter_no_already_exits));
                    return false;
                }
            } else {
                /*if the meter no is null then show a error for required field*/
                binding.editmeterInclude.editOutlineMeterMeterNo.setError(getString(R.string.error_field_recquired));
                binding.editmeterInclude.editMeterNo.requestFocus();
                return false;
            }
        }
        return true;
    }

    private boolean isOldMeterDataValid() {
        String meterno = binding.editmeterInclude.editMeterNo.getText().toString();
        if (meterno.length() != 0) {
            long meterlong = Long.parseLong(meterno);
            if (meterlong == choosenMeter.getMeterNo()) {
                choosenMeter.isMeterSame = true;
                return true;
            } else choosenMeter.isMeterSame = false;

            if (isMeterUnique(meterlong)) {/*if meter is not unique show an error.*/
                choosenMeter.getUpdatedMeter().setMeterNo(meterlong);
                binding.editmeterInclude.editOutlineMeterMeterNo.setError("");
                return true;
            } else {
                binding.editmeterInclude.editOutlineMeterMeterNo.setError(getString(R.string.meter_no_already_exits));
                return false;
            }

        } else {
            binding.editmeterInclude.editOutlineMeterMeterNo.setError(getString(R.string.error_field_recquired));
            return false;
        }
    }

    private boolean isMeterUnique(long meterno) {
        if (allMetersList != null && allMetersList.size() != 0) {
            for (long i : allMetersList) {
                if (i == meterno) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setNewEntrytype() {
        switch (choosenMeter.getEntryFor()) {
            case MeterEditType.ENTRY_HOUSE:
                choosenMeter.getUpdatedMeter().setOnlyHouse(true);
                setTitleOnToolbar(true, choosenMeter.getHouseName());
                break;
            case MeterEditType.ENTRY_ROOM:
                setTitleOnToolbar(true, choosenMeter.getRoomName());
                break;
        }
    }

    private void setOldMeterEntryType() {
        changeForOldMeter();
        updateMeterNo(choosenMeter.getMeterNo());
        switch (choosenMeter.getEntryFor()) {
            case MeterEditType.ENTRY_HOUSE:
                setTitleOnToolbar(false, choosenMeter.getHouseName());
                break;
            case MeterEditType.ENTRY_ROOM:
                setTitleOnToolbar(false, choosenMeter.getRoomName());
        }
    }

    /*makes the switches invisible*/
    private void changeForOldMeter() {
        binding.editmeterInclude.switchElectricMeterNumberManual.setVisibility(View.GONE);
        binding.editmeterInclude.switchElectricMeterNumberSystemDecide.setVisibility(View.GONE);
        binding.editmeterInclude.editOutlineInitialMeterReading.setVisibility(View.GONE);
        binding.editmeterInclude.editOutlineMeterMeterNo.setVisibility(View.VISIBLE);
    }

    private void updateMeterNo(long meterno) {
        binding.editmeterInclude.editMeterNo.setText(String.valueOf(meterno));
    }

    private void setTitleOnToolbar(boolean isnew, String subtiltle) {
        binding.editmeterInclude.editMeterToolbar.setTitle(isnew ? R.string.create_new_meter : R.string.update_meter);
        binding.editmeterInclude.editMeterToolbar.setSubtitle("For " + subtiltle);
    }

    private MaterialAlertDialogBuilder getExitDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        dialogBuilder.setTitle(getString(R.string.exit))
                .setMessage(R.string.all_changes_you_mde_will_be_dicarded)
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Navigation.findNavController(binding.getRoot()).navigateUp();
                    }
                })
                .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return dialogBuilder;
    }

    private MaterialAlertDialogBuilder getSaveDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        dialogBuilder.setTitle(getString(R.string.save_changes))
                .setPositiveButton(getString(R.string.save), this)
                .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return dialogBuilder;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menuitem_house_save) {
            if (isDataValid()) {
                getSaveDialog().show();
            }
        }
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        if (choosenMeter.getUpdatedMeter().isSystemdecide) { /*upadate the meter no if if meter is system decide*/
            SharedPreferences sh = requireContext().getSharedPreferences(getString(R.string.base_ids_sharedpreferences_file), Context.MODE_PRIVATE);
            choosenMeter.getUpdatedMeter()
                    .setMeterNo(sh.getLong(getString(R.string.system_generated_meterid_last_entry), 100000) + 1);

            sh.edit().putLong(getString(R.string.system_generated_meterid_last_entry), choosenMeter.getUpdatedMeter().getMeterNo())
                    .apply();
        }

        /*if the meter no is same as the previous meter no then don't update the field and navigate up*/
        if (choosenMeter.isMeterSame) {
            Navigation.findNavController(binding.getRoot()).navigateUp();
        }

        viewModal.updateMeterDetails(choosenMeter);
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

}