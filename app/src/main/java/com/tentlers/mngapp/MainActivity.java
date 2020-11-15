package com.tentlers.mngapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.databinding.ActivityMainBinding;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private int RC_SIGNIN = 34;
    ActivityMainBinding mainBinding;
    NavController navController;
    HouseViewModal viewModal;
    private ActionBarDrawerToggle drawerToggle;
    public InterstitialAd interstitialAd;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGNIN) {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            if (requestCode != RESULT_OK) {
                if (idpResponse == null) {/*user has pressed back button*/
                    finish();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        viewModal = new ViewModelProvider(this).get(HouseViewModal.class);

        /*authenticate the user*/
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            authenticateUser();
        } else {
            viewModal.setUserName(auth.getCurrentUser().getDisplayName());
            TextView username = mainBinding.navView.getHeaderView(0).findViewById(R.id.app_user_name);
            username.setText(viewModal.getUserName() == null || viewModal.getUserName().length() == 0 ? auth.getCurrentUser().getPhoneNumber() : viewModal.getUserName());
           /* username.setOnClickListener(this);*//*TODO: set the listeer to edit the user name*/
        }

        /* Initialise the adds.*/

        MobileAds.initialize(this);

        /*add the divice as for test adds*//*
        final List<String> testDeviceIds = Collections.singletonList("33BE2250B43518CCDA7DE426D04EE231");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();

        MobileAds.setRequestConfiguration(configuration);*/

        interstitialAd = new InterstitialAd(this);
        /*TODO: replace the interstitial add id */
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        /*add listener to load new add*/
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        /*
         * Generating a shared preference object for saving the base id of the meter
         * if the app is running for the first time then the base meterid will get saved
         * or else will be ignored.
         */
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.first_launch_shared_prefferences),
                MODE_PRIVATE);

        if (sharedPref.getBoolean(getString(R.string.is_app_first_launch), true)) {
            sharedPref.edit().putBoolean(getString(R.string.is_app_first_launch), false)
                    .apply();
            /*
             * The app is running for the first time then set the base ids for all the unique ids to be
             * generated while the app is running.
             */
            getSharedPreferences(getString(R.string.base_ids_sharedpreferences_file), MODE_PRIVATE).edit()
                    .putLong(getString(R.string.system_generated_meterid_last_entry), 100000)
                    .putLong(getString(R.string.LastEnteredHouseId), 0)
                    .apply();

            /* Add last entered house id to be 1 if it is run for the first time*/
        }


        setContentView(mainBinding.getRoot());

        /* Set the toolbar to the main activity */
        setSupportActionBar(mainBinding.toolbar);
      /*  Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /* AppBarConfiguration  mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_bills, R.id.nav_tenants,R.id.nav_rooms)
                .setOpenableLayout(mainBinding.drawerLayout)
                .build();*/

        /* Get the navControler and add the bottom Navigation to the Main activity*/
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mainBinding.bottomNav, navController);

        /*
         * Adding The Navigation drawer to the app
         * and Adding the toggle manager to the drawer layout
         * toggle is the icon shown on the drawer and aswell as supports the
         * left swipe to open the drawer.
         */
        drawerToggle = new ActionBarDrawerToggle(this, mainBinding.drawerLayout, R.string.openDrawerContentDescRes, R.string.closeDrawerContentDescRes);
        mainBinding.drawerLayout.addDrawerListener(drawerToggle);

        /*handle the openning of the drawer layout when navigation icon is clicked.*/
        mainBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainBinding.drawerLayout.open();
            }
        });

        mainBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });


        /*
         *Set the navigation change listener to update the full screen views
         */
        /*TODO: add andimation to hide navigation items*/
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                switch (destination.getId()) {
                    case R.id.nav_billEntryFragment:
                    case R.id.nav_editMeterFragment:
                        hideAllNavigation(true);
                        if (interstitialAd.isLoaded()) {
                            interstitialAd.show();
                        }
                        break;
                    case R.id.loginFragment:
                    case R.id.nav_editHouseDialog:
                    case R.id.nav_tenantEditFragment:
                    case R.id.nav_houseEntryFragment:
                    case R.id.nav_roomEnteyFragment:
                    case R.id.nav_specificHouseFragment:
                    case R.id.nav_tenantEntryFragment:
                    case R.id.nav_metersFragment:
                    case R.id.nav_specificTenantFragment:
                    case R.id.nav_specificRoomFragment:
                        hideAllNavigation(true);
                        break;
                    default:
                        hideAllNavigation(false);
                }
            }
        });
    }

    /*method to authenticate the usesr*/
    private void authenticateUser() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build()))
                        .build(),
                RC_SIGNIN);
    }

    /*
     * This meathosd hides all the navigation components from the screen
     * Generally used in entry screens.
     */
    private void hideAllNavigation(boolean ishide) {
        setBottomNavigationVisibility(!ishide);
        setAppbarVisibility(!ishide);
        setDraderVisibility(!ishide);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    // Setting Bottom navigation visibility
    private void setBottomNavigationVisibility(boolean isvisible) {
        if (!isvisible) {
            mainBinding.bottomNav.setVisibility(View.GONE);
        } else {
            mainBinding.bottomNav.setVisibility(View.VISIBLE);

        }
    }

    private void setAppbarVisibility(boolean isvisible) {
        if (!isvisible) {
            getSupportActionBar().hide();
        } else getSupportActionBar().show();
    }

    private void setDraderVisibility(boolean visibility) {
        if (!visibility) {
            mainBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else mainBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    /*
     * this method sets the setting option visibility in main menu
     */
//    private void setMenuItemSettingVisibility(boolean isvisible) {
//        if (isvisible) {
//            findViewById(R.id.action_settings).setVisibility(View.VISIBLE);
//        }else findViewById(R.id.action_settings).setVisibility(View.GONE);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
     * this handles the closing of the drawer if it is open when the back button is pressed
     */
    @Override
    public void onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

