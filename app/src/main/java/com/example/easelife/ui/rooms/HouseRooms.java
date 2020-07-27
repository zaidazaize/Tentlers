package com.example.easelife.ui.rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.easelife.R;
import com.example.easelife.data.HouseViewModal;
import com.example.easelife.data.tables.TableRooms;
import com.example.easelife.data.tables.queryobjects.HouseNameId;
import com.example.easelife.databinding.FragmentHouseRoomsListBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;



/**
 * A fragment representing a list of Items.
 * Implement the AdapterView.OnItemSelectListener so that it can handle the
 * items selected on the spinner
 */
public class HouseRooms extends Fragment implements AdapterView.OnItemSelectedListener {

    /*
     * binding object for the homw layout
     */
    FragmentHouseRoomsListBinding bindingRoom;

    /*
     * viewModal object holding the ui data
     */
    HouseViewModal viewModal;

    /*
     * Array of house Names to be shown in spinner
     */
    ArrayList<String> houseNamearray = new ArrayList<>();

    /*
     * list of all house names along with their ids stored in the object of "HouseNameId"
     */
    List<HouseNameId> nameIdList;

    /*
     * The spinner adapter is initialised. It updates the list of the houses in the spinner
     * when ever a change in data base occurs.
     */
    ArrayAdapter<String> arrayAdapter;

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
        recyclerViewAdapter = new MyroomsRecyclerViewAdapter(
                requireActivity().getDrawable(R.drawable.ic_baseline_yes_tenant_24),
                requireActivity().getDrawable(R.drawable.ic_baseline_no_tenants_24));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
         * get all the house names with the ids and update the spinner with the house names.
         */
        viewModal.getHouseNameIdforRooms().observe(getViewLifecycleOwner(), new Observer<List<HouseNameId>>() {
            @Override
            public void onChanged(List<HouseNameId> houseNameIds) {
                nameIdList = houseNameIds;
                arrayAdapter.addAll(getHouseNamearray());
            }

            /*
             * the meathod prepares the array of the house names
             */
            private ArrayList<String> getHouseNamearray() {
                if (nameIdList == null || nameIdList.size() == 0) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add("no item found");
                    return list;
                }

                for (HouseNameId s : nameIdList) {
                    houseNamearray.add(s.houseName);
                }
                return houseNamearray;
            }
        });

        /*
         * Set the adapter to the recycle view.
         */
        bindingRoom.recycleViewRooms.setAdapter(recyclerViewAdapter);
        return bindingRoom.getRoot();
    }

    /*
     * Inflates the menu containing the spinner.
     * Add the item select listener.
     * Add the adapter to the spinner.
     * Hide the setting options from the app bar.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_rooms, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        Spinner spinner = (Spinner) menu.findItem(R.id.menu_item_room_spinner).getActionView();
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    /*
     * These both handles the item selection of the listener.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        viewModal.getAllRooms(nameIdList.get(position).houseId)
                .observe(getViewLifecycleOwner(), new Observer<List<TableRooms>>() {
                    @Override
                    public void onChanged(List<TableRooms> tableRooms) {
                        recyclerViewAdapter.setmRoomList(tableRooms);
                    }
                });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}