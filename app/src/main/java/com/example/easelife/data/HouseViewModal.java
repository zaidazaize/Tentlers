package com.example.easelife.data;

import android.app.Application;
import android.text.style.TtsSpan;

import com.example.easelife.data.database.Repository;
import com.example.easelife.data.tables.meters.AllMetersData;
import com.example.easelife.data.tables.meters.GetLastMeterReading;
import com.example.easelife.data.tables.queryobjects.HouseNameId;
import com.example.easelife.data.tables.queryobjects.HouseNameMeterId;
import com.example.easelife.data.tables.TableHouse;
import com.example.easelife.data.tables.TableRooms;
import com.example.easelife.data.tables.rooms.RoomNoName;
import com.example.easelife.data.tables.rooms.RoomNoNameId;
import com.example.easelife.data.tables.tenants.TenantNameHouseRoom;
import com.example.easelife.data.tables.tenants.TenantsPersonal;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class HouseViewModal extends AndroidViewModel {

    private TableHouse mShowHouse;
    private Repository mRepository;
    private LiveData<List<TableHouse>> mAllHouse;
    private int houseIdForRoomEntry;
    public int lastEnteredHouseId;

    public HouseViewModal(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllHouse = mRepository.mgetAllHouse();
    }

    public LiveData<List<TableHouse>> getAllHouse() {
        return mAllHouse;
    }

    public void insertHouse(TableHouse tableHouse) {
        mRepository.insertNewHouse(tableHouse);
    }

    public void deleteHosue(TableHouse tableHouse) {
        mRepository.deleteHouse(tableHouse);
    }

    public LiveData<HouseNameMeterId[]> getHouseNameMeterId() {
        return mRepository.mgetHousenameMeterId();

    }


    /* For displaying specific House details. */
    public TableHouse getmShowHouse() {
        return mShowHouse;
    }

    public void setmShowHouse(TableHouse mShowHouse) {
        this.mShowHouse = mShowHouse;
    }


    /* For displaying data in rooms fragment */
    public LiveData<List<HouseNameId>> getHouseNameIdforRooms() {
        return mRepository.getHouseNameId();
    }

    /* For displaying rooms details*/

    /*
    * this is used to display the three rooms in the specific house fragemnt
    */
    public LiveData<List<TableRooms>> getThreeRooms(int houseId) {
        return mRepository.getThreeRooms(houseId);
    }
    /*
    * This is used to display all the rooms in the Room fragment
    */
    public LiveData<List<TableRooms>> getAllRooms(int houseId) {
        return mRepository.getAllRooms(houseId);
    }

    /* For transfering the house id to the room entry fragment*/

    public int getHouseIdForRoomEntry() {
        return houseIdForRoomEntry;
    }

    public void setHouseIdForRoomEntry(int houseIdForRoomEntry) {
        this.houseIdForRoomEntry = houseIdForRoomEntry;
    }

     /*
     *Data for inserting new room in room Entry Fragment
     */
    public LiveData<List<Long>> getAllHousemeterids() {
        return mRepository.getallHosueMeterids();
    }

    public LiveData<List<Long>> getAllroomhouseids() {
        return mRepository.getAllroomids();
    }

    public LiveData<List<RoomNoName>> getRoomNoName(long parentid) {
        return mRepository.getRoomNoName(parentid);
    }

    /*
     * Manupulating the Room Table for the room entry modification adition and deletion of room data.
     */
    public void insertNewRoom(TableRooms rooms) {

        mRepository.insetNewRoom(rooms);
    }

    public void deleteTheRoom(TableRooms rooms) {
        mRepository.deleteRoom(rooms);
    }

    public void updateTheRoom(TableRooms rooms) {
        mRepository.deleteRoom(rooms);
    }

    /*
     * For manipulating the tenant table
     */
    public void insertNewtenant(TenantsPersonal tenantsPersonal) {
        mRepository.insertNewTenant(tenantsPersonal);
    }

    public void deleteTenant(TenantsPersonal tenantsPersonal) {
        mRepository.deleteTenant(tenantsPersonal);
    }

    public void updateTenant(TenantsPersonal tenantsPersonal) {
        mRepository.updateTenant(tenantsPersonal);
    }

    /*Tenant entry table */
    /* Getting the value of room name and id for entering in the room spinner*/
    public LiveData<List<RoomNoNameId>> getRoomNoNameID(int houseId) {
       return mRepository.getRoomNoNameId(houseId);
    }

    /* modifying all meters data table*/
    public void insertNewMeterReading(AllMetersData metersData) {
        mRepository.insertNewMeterReading(metersData);
    }

    public LiveData<Long[]> getLastEnteredMeterEntry(GetLastMeterReading lastMeterReading) {
        return mRepository.getLastEnteredMeterReading(lastMeterReading);
    }

    public LiveData<List<TenantNameHouseRoom>> getAllTenantNHR(boolean withRoomAlloted) {
        return mRepository.getAllTenantNHR(withRoomAlloted);
    }
}
