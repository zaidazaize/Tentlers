package com.tentlers.mngapp.ui.tenants;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.MyAuthorities;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.meters.GetLastMeterReading;
import com.tentlers.mngapp.data.tables.meters.LastReadingWithDate;
import com.tentlers.mngapp.data.tables.meters.MeterEditType;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameAndId;
import com.tentlers.mngapp.data.tables.rooms.RoomNoNameId;
import com.tentlers.mngapp.data.tables.tenants.TenantsPersonal;
import com.tentlers.mngapp.databinding.FragmentTenantEntryBinding;
import com.tentlers.mngapp.ui.home.SaveDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class TenantEntryFragment extends Fragment implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemSelectedListener {
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tenantEntryBinding = null;
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
    /* Snack bar object*/
    Snackbar snackbar;
    /* Variable holding last meter reading of the room.*/
    long lastMeterReading;
    private boolean isHosueAvailable;/* Tells whether is there any house available that has some rooms which can be allotted.*/

    public TenantEntryFragment() {
    }

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

        tenantEntryBinding = FragmentTenantEntryBinding.inflate(getLayoutInflater(), container, false);

        /*check from where the tenant entry is triggered*/


        /* hide the app bar and bottom navigation via a callback in main activity */
        /*show save dialog when save button is clicked.*/
        tenantEntryBinding.toolbarTenantEnter.setOnMenuItemClickListener(this);

        /*Show the exit dialogue when the navigation icon is clicked.Means to cancel the add tenant task.;*/
        tenantEntryBinding.toolbarTenantEnter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExitDialoge().show();
            }
        });

        /*Switch checked change listeners.
         * switch for controlling the visibility of the personal info fields.
         * If the switch is closed the personal info in tenant object is set to false;
         * Initialise the switch to true after adding the listener. At the end of oncreateview;*/
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

        /* Add the the item select listener to the house spinner which updates the room names*/
        tenantEntryBinding.spinnerAddHouse.setAdapter(houseAdapter);
        tenantEntryBinding.spinnerAddHouse.setOnItemSelectedListener(this);

        /* update the room and house spinner data*/
        /* This adds a house list in which those houses are listed which have atleast one room */
        viewModal.getHouseNameIdTEspinner().observe(getViewLifecycleOwner(), new Observer<List<HouseNameAndId>>() {
            @Override
            public void onChanged(List<HouseNameAndId> houseNameAndIdList) {

                if (houseNameAndIdList.isEmpty()) {/*If there is no house available then allot meter will be set to false.
                 which is controlled via a variable isHouseAvailable*/
                    isHosueAvailable = false;/*responsible for showing snackbar that no house is available.*/
                    tenantEntryBinding.switchTenantAllotRooms.setChecked(false);
                    tenantEntryBinding.switchTenantAllotRooms.setEnabled(false);
                } else isHosueAvailable = true;

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

        /*Set the adapter and click listener on the room spinner
         * The values of switch in add electric meter will be re enitialised if room is changed.*/
        tenantEntryBinding.spinnerAddRoom.setAdapter(roomAdapter);

        tenantEntryBinding.spinnerAddRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tenantsPersonal.setRoomId(mroomNoNameIds.get(position).roomId);
                choosenRoom = mroomNoNameIds.get(position);
                setAutoGenerateSwitchAndInput();
                setLastMeterReaing();/*fetches the meter reading and sets it in text view*/
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


        /* controlling the auto or manual entry of the electricity charges.*/
        tenantEntryBinding.switchTenantAddElectricityChargesAutoGenerate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {/* make the input field of initial meter reading and last meter reading invisible. Set the manual switch to off.*/
                    tenantEntryBinding.switchTenantAddElectricityEnterManually.setChecked(true);
                    tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setVisibility(View.GONE);
                    tenantEntryBinding.tenantEntryLiniarlayoutLastmeterReading.setVisibility(View.GONE);
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
                    snackbar.setAction(R.string.allot_rooms, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tenantEntryBinding.switchTenantAllotRooms.setChecked(true);
                        }
                    })
                            .show();
                    tenantEntryBinding.switchTenantAddElectricityChargesAutoGenerate.setChecked(false);
                    return;
                }
                if (choosenRoom != null && choosenRoom.isMeterEnabled) {/* Make the input field and last meter reading field visible and set the manual switch to off*/
                    tenantEntryBinding.switchTenantAddElectricityEnterManually.setChecked(false);
                    tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setVisibility(View.VISIBLE);
                    tenantEntryBinding.tenantEntryLiniarlayoutLastmeterReading.setVisibility(View.VISIBLE);
                } else {/* Meter is not enabled in the room. snack bar to enable the room*/
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    snackbar = Snackbar.make(tenantEntryBinding.coordinatorLayoutTenantEntry, R.string.meter_is_missing_add_meter_to_the_selected_room, BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.setAction(R.string.add_meter, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO Show the dialog here. to add the meter.*/
                            if (choosenRoom != null) {
                                viewModal.setMeterEditType(new MeterEditType()
                                        .setForNewMeter(MeterEditType.ENTRY_ROOM, choosenRoom.roomId, choosenRoom.roomName));
                                Navigation.findNavController(tenantEntryBinding.getRoot()).navigate(R.id.action_global_nav_editMeterFragment);
                            }
                        }
                    })
                            .show();
                    tenantEntryBinding.switchTenantAddElectricityChargesAutoGenerate.setChecked(false);
                }
            }
        });


        /* This watcher inspects the meter reading entered in the edittext and show error if the reading is less than
         * the previous reading*/
        tenantEntryBinding.textInputEditTextInitialMeterReading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tenantsPersonal.allMetersData.setLastMeterReadingFromString(s.toString());
                if (tenantsPersonal.allMetersData.getLastMeterReading() <= lastMeterReading) {
                    tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.
                            setError(getString(R.string.invalid_entry_meter_reading_is_less_than_last_entered_reading));

                } else
                    tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setError("");
            }
        });

        tenantEntryBinding.tenantEntryFabAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 29) {
                    selectImage(requireContext());
                }

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    selectImage(requireContext());

                } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    /*show the snack bar why we need permission to write external storage*/
                    Snackbar.make(tenantEntryBinding.coordinatorLayoutTenantEntry, "Permission needed to create new Image File", BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                } else {
                    /*we are not using system magaged request code */
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MyAuthorities.REQUEST_WRITE_EXTERNAL_PERMISION);
                }
            }
        });

        /* Initialisig all the switch buttons.*/
        tenantEntryBinding.switchTenantPersonalInfo.setChecked(true);
        tenantEntryBinding.radioGroupGender.check(R.id.radioButton_male);

        return tenantEntryBinding.getRoot();
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_from_gallery), getString(R.string.cancel)};

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(R.string.choose_tenants_profile_picture);

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.take_photo))) {
                   /* Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);*/
                    dispatchPhoto();

                } else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    pickPhoto();
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /*use to pick photo from the library*/
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, MyAuthorities.REQUEST_GET_SELECTED_IMAGE);
    }

    /*send intent to capture the photo.*/
    public void dispatchPhoto() {
        Intent takeimage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeimage.resolveActivity(requireActivity().getPackageManager()) != null) {
            File imagefile = null;

            try {
                imagefile = getImageName();
            } catch (Exception e) {
                Log.d("exception", Arrays.toString(e.getStackTrace()));

            }
            if (imagefile != null) {
                tenantsPersonal.setTenantPhotoUri(FileProvider.getUriForFile(requireContext(), MyAuthorities.CONTENT_AUTORITY, imagefile));
                takeimage.putExtra(MediaStore.EXTRA_OUTPUT, tenantsPersonal.getTenantPhotoUri());
                startActivityForResult(takeimage, MyAuthorities.REQUEST_IMAGE_CAPTURE);
            } else {
                Snackbar.make(tenantEntryBinding.coordinatorLayoutTenantEntry, "Cannot create the file", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        }
    }

    /*get the unique photo name. Stores the name in the tenantspersonal object.*/
    private File getImageName() throws IOException {
        String timestamp = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG).format(new Date());
        tenantsPersonal.imageName = "JPEG_" + timestamp + "_";

        /*if the android level  is less than equal  to 9 then save the image in image public directory
         * else save it in "MediaStore.Images" directory*/
        if (Build.VERSION.SDK_INT <= 28) {
            tenantsPersonal.setTenantPhotoFile(File.createTempFile(tenantsPersonal.getImageName(),
                    ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
        } else {
            tenantsPersonal.setTenantPhotoFile(
                    File.createTempFile(tenantsPersonal.getImageName(), ".jpg", requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        }
        return tenantsPersonal.getTenantPhotoFile();

    }

    /*add the photo to gallery
     * show the image icon on the photo. if data is not equal to null.*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("settingImage", "startactivitybefore");
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("settingImage", "startActivity");

        if (requestCode != Activity.RESULT_CANCELED && data != null) {
            Uri goturi = data.getData();
            Log.d("settingImage", "here uri");
            if (goturi != null) {
                Log.d("settingImage", "here before set");
                tenantEntryBinding.tenantEntryImageViewTenantPhoto.setImageURI(goturi);
                Log.d("settingImage", "seted image");
            }
        }
        /*if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }*/
      /*  switch (requestCode) {
            case MyAuthorities.REQUEST_IMAGE_CAPTURE:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap image = (Bitmap) extras.get("data");
                    tenantEntryBinding.tenantEntryImageViewTenantPhoto.setImageBitmap(image);
                }
                break;

            case MyAuthorities.REQUEST_GET_SELECTED_IMAGE:
                if (data != null) {
                    Uri imageUri = data.getData();
                    String[] filepath = {MediaStore.Images.Media.DATA};

                    if (imageUri != null) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT >= 29) {
                        Bitmap photo = null;
                        try {
                            photo = requireContext().getContentResolver().loadThumbnail(tenantsPersonal.getTenantPhotoUri(),new Size(200,200),null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (photo != null) {
                            tenantEntryBinding.tenantEntryImageViewTenantPhoto.setImageURI();}
                    }
                    else {


                    }


                        if (cursor != null) {
                            cursor.moveToNext();
                            int columnIndex = cursor.getColumnIndexOrThrow(filepath[0]);
                            String picturePath = cursor.getString(columnIndex);
                            tenantEntryBinding.tenantEntryImageViewTenantPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                            cursor.close();
                        }

                }


        }*/

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode != MyAuthorities.REQUEST_WRITE_EXTERNAL_PERMISION) {
            /* handle the request to take photo only if the write external storage permission is provided.*/
            return;
        }
        if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            /* todo dispatchPhoto();*/
            pickPhoto();
        }
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
        if (item.getItemId() == R.id.menuitem_house_save) {
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
        String tenantName = tenantEntryBinding.textInputEditTextOutlinedTenantName.getText().toString();
        if (tenantName.length() == 0) {
            tenantEntryBinding.textInputLayoutOutlinedTenantName.setError(getString(R.string.error_field_recquired));
            tenantEntryBinding.textInputLayoutOutlinedTenantName.requestFocus();
            return false;
        } else {
            tenantsPersonal.setTenantName(tenantName);
            Log.d("tenantName", tenantsPersonal.getTenantName());
            tenantEntryBinding.textInputLayoutOutlinedTenantName.setError("");
        }

        return true;
    }

    /* Meathods Checks and saves the payment information entered by the user in tenant object.*/
    private boolean isPaymentSchemeValid() {
        /*check wheter paument scheme is available*/
        if (!tenantEntryBinding.switchTenantPaymentSheme.isChecked()) {
            tenantsPersonal.mFixedCharges = 0;
            tenantsPersonal.setDiscardAllMeterPay();
            return true;
        }

        String monthlyCost = Objects.requireNonNull(tenantEntryBinding.textInputEditTextOutlinedMonthlyFixedCharge.getText()).toString();
        if (monthlyCost.length() != 0) {
            tenantsPersonal.mFixedCharges = Float.parseFloat(monthlyCost);
        }
        /*check if electric pay is enabled*/
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
                    tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.requestFocus();
                    tenantEntryBinding.textInputEditTextLayoutOutlinedInitialMeterReading.setError(getString(R.string.invalid_entry_meter_reading_is_less_than_last_entered_reading));
                    return false;
                } else {
                    /* enter the meter informations in the tenant personal table this is used to
                     * Meters table if meter pay is selected.*/
                    tenantsPersonal.allMetersData.setLastMeterReading(Integer.parseInt(initialMeterReading));
                    tenantsPersonal.allMetersData.setMeterId(choosenRoom.meterId);
                    tenantsPersonal.getAllMetersData().setOnlyReadingState(AllMetersData.TENANT_ENTRY);
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

    /*sets last meter reading for updating reading when tenant is added*/
    private void setLastMeterReaing() {
        if (choosenRoom.isMeterEnabled) {//fetch for reading only if the meter is enabled;
            viewModal.getLastEnteredMeterEntry(new GetLastMeterReading().setRoomId(choosenRoom.roomId)).observe(getViewLifecycleOwner(),
                    new Observer<LastReadingWithDate>() {
                        @Override
                        public void onChanged(LastReadingWithDate lastReadingWithDate) {
                            lastMeterReading = lastReadingWithDate.getLastMeterReading();
                            tenantEntryBinding.tenantEntryStartMeterReading.setText(String.valueOf(lastMeterReading));


                        }
                    });
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