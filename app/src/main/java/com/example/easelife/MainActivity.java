package com.example.easelife;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.easelife.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private ActionBarDrawerToggle drawerToggle;
    ActivityMainBinding mainBinding;
    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * Generating a shared preference object for saving the base id of the meter
        * if the app is running for the first time then the base meterid will get saved
        * or else will be ignored.
        */
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.first_launch_shared_prefferences), Context.MODE_PRIVATE);
        if (sharedPref.getBoolean(getString(R.string.is_app_first_launch), true)) {
            sharedPref.edit().putBoolean(getString(R.string.is_app_first_launch), false);
            this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()
                    .putLong(getString(R.string.system_generated_meterid_last_entry), 100000)
                    .apply();
        }

        /*
        * Generating the binding class for the main Activiy .
        * this controls the nav_hostfragment, bottom Nav and the
        * top navigation bar.
        */
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        /*
        * Set the toolbar to the main activity
        */
        setSupportActionBar(mainBinding.toolbar);

        /*
        * Get the navControler and add the bottom Navigation to the Main activity
        */
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mainBinding.bottomNav, navController);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

//        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_bills, R.id.nav_tenants)
//                .setDrawerLayout(mainBinding.drawerLayout)
//                .build();
//        // Add the action bar
//        NavigationUI.setupWithNavController(mainBinding.navView,navController);

//
        /*
        * Adding The Navigation drawer to the app
        * and Adding the toggle manager to the drawer layout
        * toggle is the icon shown on the drawer and aswell as supports the
        * left swipe to open the drawer.
        */
        drawerToggle = new ActionBarDrawerToggle(this, mainBinding.drawerLayout, R.string.openDrawerContentDescRes, R.string.closeDrawerContentDescRes);
        mainBinding.drawerLayout.addDrawerListener(drawerToggle);


//        // Adding back buttom on top in place of drawer icon
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      /*
       *Set up the navigation item listener so that it supports the navigation
       * the selected item id is caught here.
       * we can manage the navigation by listenenig to those ids.
       *
       * Nav_view is the id of navigaton holder in drawer layout
       */
        mainBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });


        /*
        *Set the navigation change listener to update the full screen views
        */
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                switch (destination.getId()) {
                    case R.id.nav_houseEntryFragment:
                    case R.id.nav_roomEnteyFragment:
                    case R.id.nav_specificHouseFragment:
                        hideAllNavigation(false);
                        break;
                    default:
                        hideAllNavigation(true);
                }
            }
        });

    }

    /*
    * This meathosd hides all the navigation components from the screen
    * Generally used in entry screens.
    */
    private void hideAllNavigation(boolean ishide) {
        setBottomNavigationVisibility(ishide);
        setAppbarVisibility(ishide);
        setDraderVisibility(ishide);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            /*
            * If the item selected belongs to the drawer layout it is transffered to
            * drawer item click listener assingned in oncreate meathod.
            */
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * this handles the closing of the drawer if it is open when the back button is pressed
    */
    @Override
    public void onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerVisible(Gravity.LEFT)) {
            mainBinding.drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }
}

