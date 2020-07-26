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
    AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        HouseViewModal houseViewModal = new ViewModelProvider(this).get(HouseViewModal.class);

        // Write meter base id to shared priference
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        // Adding Toolbar
        setSupportActionBar(mainBinding.toolbar);


     /*   For bottom navigation
        Get the nav controler
               Bottom Navigation
         Add the bottom navigation with by usig databinding to navcontroller */
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
// Adding Navigation drawer
        drawerToggle = new ActionBarDrawerToggle(this, mainBinding.drawerLayout, R.string.openDrawerContentDescRes, R.string.closeDrawerContentDescRes);
        // Add listener to the drawer layout
        mainBinding.drawerLayout.addDrawerListener(drawerToggle);


//        // Adding back buttom on top in place of drawer icon
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the navigation
        // Nav_view is the id of navigaton holder in drawer layout
        mainBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });


        /*// Set the navigation change listener to update the full screen views*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //for drawer layuout
    @Override
    public void onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerVisible(Gravity.LEFT)) {
            mainBinding.drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }
}

