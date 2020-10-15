package com.tentlers.mngapp.ui.home.specifichouse;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.Address;
import com.tentlers.mngapp.data.tables.TableHouse;
import com.tentlers.mngapp.databinding.EditHouseDialogBinding;

import java.util.List;
import java.util.Objects;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class EditHouseDialog extends Fragment {

    HouseViewModal viewModal;
    List<String> allHouseNames;
    EditHouseDialogBinding dialogBinding;
    TableHouse choosenHouse, updatedHouse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updatedHouse = new TableHouse();
        updatedHouse.setAddress(new Address());
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        choosenHouse = viewModal.getHouseForEdit();

        viewModal.getHouseNameMeterId().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                allHouseNames = strings;
                Log.d("alladdress", strings.toString());
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getExitDialog().show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogBinding = EditHouseDialogBinding.inflate(getLayoutInflater(), null, false);


        /*set the tool bar buttons*/
        dialogBinding.editHouseToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExitDialog().show();
            }
        });

        /*set the save button*/
        dialogBinding.editHouseToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuitem_house_save) {
                    if (isDataValid()) {
                        getSaveDialog().show();
                    }
                }
                return true;
            }
        });
        /*House Name*/
        dialogBinding.editHouseHousename.setText(choosenHouse.getHouseName());

        /*house Address*/
        if (choosenHouse.getAddress() != null) {
            Address address = choosenHouse.getAddress();
            dialogBinding.editHouseCity.setText(address.city);
            dialogBinding.editHouseCountry.setText(address.country);
            dialogBinding.editHouseNumber.setText(address.getHouseNo() == 0 ? null : String.valueOf(address.getHouseNo()));
            dialogBinding.eidtHousePostal.setText(address.getPostalcode());
            dialogBinding.editHouseArea.setText(address.getLocality());
        }


        return dialogBinding.getRoot();
    }

    /*  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

     *//*update the input fields*//*

     *//*create the dialog*//*
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(requireActivity());

        *//*passing null in the parent view because it is going in the dialog view*//*
        dialogbuilder.setView(getLayoutInflater().inflate(R.layout.edit_house_dialog, null));
        dialogbuilder.setTitle("Edit House Details");

        dialogbuilder.setIcon(R.drawable.ic_baseline_edit_24)
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return dialogbuilder.create();
    }*/

    private boolean isDataValid() {
        return checkForUniqueHouse() & checkForAddress();
    }

    private boolean checkForUniqueHouse() {

        String houseName = dialogBinding.editHouseHousename.getText().toString();
        if (houseName.length() != 0) {
            if (houseName.equals(choosenHouse.getHouseName())) {
                updatedHouse.setHouseName(houseName);
                return true;
            }
            if (isHouseunique(houseName)) {
                updatedHouse.setHouseName(houseName);
                dialogBinding.textInputLayoutOutlinedHouseName.setError("");
                return true;
            }
        } else {
            dialogBinding.textInputLayoutOutlinedHouseName.setError(getString(R.string.error_field_recquired));
            return false;
        }

        return true;
    }

    private boolean isHouseunique(String gothousename) {
        if (allHouseNames != null) {
            for (String s : allHouseNames) {
                if (gothousename.equals(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkForAddress() {
        String city, country, postalcode, locality, housenoString;

        city = Objects.requireNonNull(dialogBinding.editHouseCity.getText()).toString();
        country = Objects.requireNonNull(dialogBinding.editHouseCountry.getText()).toString();
        postalcode = Objects.requireNonNull(dialogBinding.eidtHousePostal.getText()).toString();
        locality = Objects.requireNonNull(dialogBinding.editHouseArea.getText()).toString();
        housenoString = Objects.requireNonNull(dialogBinding.editHouseNumber.getText()).toString();
        updatedHouse.getAddress().setLocality(locality);
        updatedHouse.getAddress().setHouseNo(housenoString.length() == 0 ? 0 : Integer.parseInt(housenoString));

        return iscityValid(city) & iscountryvalid(country) & isPostalcodeValid(postalcode);

    }

    private boolean isPostalcodeValid(String postalcode) {
        if (postalcode.length() != 0) {
            Log.d("postalcode", postalcode);
            updatedHouse.getAddress().setPostalcode(postalcode);
            dialogBinding.layoutHousePostalcode.setError("");
            return true;
        } else {
            dialogBinding.layoutHousePostalcode.setError(getString(R.string.error_field_recquired));
            return false;
        }
    }

    private boolean iscountryvalid(String country) {
        if (country.length() != 0) {
            updatedHouse.getAddress().setCountry(country);
            dialogBinding.layoutHouseCountry.setError("");
            return true;
        } else {
            dialogBinding.layoutHouseCountry.setError(getString(R.string.error_field_recquired));
            return false;
        }
    }

    private boolean iscityValid(String city) {
        if (city.length() != 0) {
            updatedHouse.getAddress().setCity(city);
            dialogBinding.layoutEditHouseCity.setError("");
            return true;
        } else {
            dialogBinding.layoutEditHouseCity.setError(getString(R.string.error_field_recquired));
            return false;
        }
    }

    private MaterialAlertDialogBuilder getExitDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        dialogBuilder.setTitle(getString(R.string.exit))
                .setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_delete_outline_24))
                .setPositiveButton("Discard Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Navigation.findNavController(dialogBinding.getRoot()).navigateUp();
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
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*update the changed fields in the */
                        choosenHouse.setHouseName(updatedHouse.getHouseName());
                        choosenHouse.setAddress(updatedHouse.getAddress());
                        viewModal.updateHouse(choosenHouse);
                        dialog.dismiss();
                        Navigation.findNavController(dialogBinding.getRoot()).navigateUp();

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

}
