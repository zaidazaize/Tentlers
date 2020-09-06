package com.tentlers.mngapp.ui.rooms;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.HouseViewModal;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameIdNoRooms;
import com.tentlers.mngapp.data.tables.rooms.RoomForRoomList;
import com.tentlers.mngapp.databinding.FragmentHouseRoomsListBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


/**
 * A fragment representing a list of Items.
 * Implement the AdapterView.OnItemSelectListener so that it can handle the
 * items selected on the spinner
 */
public class HouseRooms extends Fragment implements AdapterView.OnItemSelectedListener {

    /*
     * binding object for the home layout
     */
    FragmentHouseRoomsListBinding bindingRoom;

    /*
     * viewModal object holding the ui data
     */
    HouseViewModal viewModal;

    /*
     * list of all house names along with their ids stored in the object of "HouseNameIdNoRooms"
     */
    List<HouseNameIdNoRooms> nameIdNoofRoomsList;

    /*
     * The spinner adapter is initialised. It updates the list of the houses in the spinner
     * when ever a change in data base occurs.The array list which sets the list of house.
     */
    ArrayAdapter<String> arrayAdapter;
    int noOfRoomsForChosenHouse;

    /*
     *  The recycle view adapter the drawabel passed is used to
     * set on the list items telling about the vacancy of the room.
     */
    MyroomsRecyclerViewAdapter recyclerViewAdapter;


    public HouseRooms() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Initialise the view modal for for holding the data
         */
        viewModal = new ViewModelProvider(requireActivity()).get(HouseViewModal.class);

        /*
         * Initialise the Array adapter
         */
        arrayAdapter = new ArrayAdapter<String>
                (requireContext(), android.R.layout.simple_list_item_1);

        /*
         * Initialise the recycle view adapter.
         */
        recyclerViewAdapter = new MyroomsRecyclerViewAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
         * this meathod trasfers the appbar item click from main activity to this fragment
         */
        setHasOptionsMenu(true);

        /*
         * Specific binding class object for the layout related to this activity.
         */
        bindingRoom = FragmentHouseRoomsListBinding.inflate(getLayoutInflater(), container, false);
        /*
         * Set the spinner in the toolbar
         * add the on item chose listener to the spinner
         * the item listener is implemented in the
         */
        bindingRoom.spinnerToolbarRooms.setAdapter(arrayAdapter);
        bindingRoom.spinnerToolbarRooms.setOnItemSelectedListener(this);

        /*
         * get all the house names even if no room exists with the ids and update the spinner with the house names.
         */
        viewModal.getHouseNameIdforRooms().observe(getViewLifecycleOwner(), new Observer<List<HouseNameIdNoRooms>>() {
            @Override
            public void onChanged(List<HouseNameIdNoRooms> houseNameIds) {

                setArrayListonSpinner(getHouseNamearray(houseNameIds), houseNameIds);
            }

            /* the meathod prepares the array of the house names*/
            private ArrayList<String> getHouseNamearray(List<HouseNameIdNoRooms> houseNameId) {
                ArrayList<String> houseArray = new ArrayList<>();
                for (HouseNameIdNoRooms s : houseNameId) {
                    houseArray.add(s.houseName);
                }
                return houseArray;
            }
        });

        /*
         * Set the adapter to the recycle view.
         */
        bindingRoom.recycleViewRooms.setAdapter(recyclerViewAdapter);

        return bindingRoom.getRoot();
    }

    /*
     * Sets the array list on the array adapter of the spinner
     * It also checks for the emptyness of the array list
     */
    private void setArrayListonSpinner(ArrayList<String> houseNamearray, List<HouseNameIdNoRooms> houseNameIds) {
        nameIdNoofRoomsList = houseNameIds;
        if (!houseNamearray.isEmpty()) {
            noOfRoomsForChosenHouse = nameIdNoofRoomsList.get(0).noOfRooms;
            arrayAdapter.clear();
            arrayAdapter.addAll(houseNamearray);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    /*
     * Inflates the menu.
     * Hide the setting options from the app bar.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_rooms, menu);
        menu.findItem(R.id.action_settings).setVisible(false);

    }

    /*
     * Handle the menu item selection for entering the new room and moving the user to that fragment
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_room_addrooms) {
            if (nameIdNoofRoomsList.size() == 0) {
                Toast.makeText(getContext(), "No house available to add room.", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (noOfRoomsForChosenHouse < 99) {
                Navigation.findNavController(bindingRoom.getRoot()).navigate(R.id.action_nav_rooms_to_roomEnteyFragment);
            } else {
                Toast.makeText(getContext(), "Max room limit reached.", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }

    /*
     * These both handles the item selection of the listener.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        /*
         * set the house id value in the view modal to be used for entering new room.
         * And no of rooms to the global variable for checking the entry permission.
         */
        viewModal.setHouseIdForRoomEntry(nameIdNoofRoomsList.get(position).houseId);
        noOfRoomsForChosenHouse = nameIdNoofRoomsList.get(position).noOfRooms;
        viewModal.getAllRooms(nameIdNoofRoomsList.get(position).houseId)
                .observe(getViewLifecycleOwner(), new Observer<List<RoomForRoomList>>() {
                    @Override
                    public void onChanged(List<RoomForRoomList> roomsForRoomList) {
                        Log.d("roomlistsize", String.valueOf(roomsForRoomList.size()));
                        recyclerViewAdapter.setmRoomList(roomsForRoomList);
                    }
                });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}