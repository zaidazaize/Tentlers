package com.tentlers.mngapp.data;

import android.app.Application;

import com.tentlers.mngapp.data.database.Repository;
import com.tentlers.mngapp.data.tables.TableHouse;
import com.tentlers.mngapp.data.tables.TableRooms;
import com.tentlers.mngapp.data.tables.bills.BillItemForCard;
import com.tentlers.mngapp.data.tables.bills.Bills;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.meters.GetLastMeterReading;
import com.tentlers.mngapp.data.tables.meters.LastReadingWithDate;
import com.tentlers.mngapp.data.tables.meters.MetersListObj;
import com.tentlers.mngapp.data.tables.queryobjects.HouseForHomeFragment;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameAndId;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameIdNoRooms;
import com.tentlers.mngapp.data.tables.rooms.RoomForRoomList;
import com.tentlers.mngapp.data.tables.rooms.RoomNoName;
import com.tentlers.mngapp.data.tables.rooms.RoomNoNameId;
import com.tentlers.mngapp.data.tables.tenants.TenantBillEntry;
import com.tentlers.mngapp.data.tables.tenants.TenantNameHouseRoom;
import com.tentlers.mngapp.data.tables.tenants.TenantsPersonal;

import java.sql.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class HouseViewModal extends AndroidViewModel {

    private int houseIdForSpecificHouse;
    private Repository mRepository;
    private LiveData<List<HouseForHomeFragment>> mAllHouse;
    private int houseIdForRoomEntry;

    private MetersListObj metersListObj;
    private int roomIdForSpecificRoom;

    private int tenantIdForSpecificTenant;

    public MetersListObj getMetersListObj() {
        return metersListObj;
    }

    public void setMetersListObj(MetersListObj metersListObj) {
        this.metersListObj = metersListObj;
    }

    public HouseViewModal(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllHouse = mRepository.mgetAllHouseForHomeFragment();
    }

    public LiveData<List<HouseForHomeFragment>> getAllHouseForHomeFragment() {
        return mAllHouse;
    }

    public LiveData<TableHouse> getHouseFromHouseId(int houseId) {
        return mRepository.getHosueFromHouseId(houseId);
    }

    public void insertHouse(TableHouse tableHouse) {
        mRepository.insertNewHouse(tableHouse);
    }

    public void deleteHosue(TableHouse tableHouse) {
        mRepository.deleteHouse(tableHouse);
    }

    public LiveData<List<String>> getHouseNameMeterId() {
        return mRepository.mgetHousenameMeterId();

    }

    /* For displaying specific House details. */
    public int getHouseIdForSpecificHouse() {
        return houseIdForSpecificHouse;
    }

    public void setHouseIdForSpecificHouse(int houseid) {
        this.houseIdForSpecificHouse = houseid;
    }


    /* For displaying data in rooms fragment */
    public LiveData<List<HouseNameIdNoRooms>> getHouseNameIdforRooms() {
        return mRepository.getHouseNameId();
    }

    /* For displaying rooms details*/

    /*
     * this is used to display the three rooms in the specific house fragemnt
     */
    public LiveData<List<RoomForRoomList>> getThreeRooms(int houseId) {
        return mRepository.getThreeRooms(houseId);
    }

    /*
     * This is used to display all the rooms in the Room fragment
     */
    public LiveData<List<RoomForRoomList>> getAllRooms(int houseId) {
        return mRepository.getAllRooms(houseId);
    }

    /* For transfering the house id to the room entry fragment*/

    public int getHouseIdForRoomEntry() {
        return houseIdForRoomEntry;
    }

    public void setHouseIdForRoomEntry(int houseIdForRoomEntry) {
        this.houseIdForRoomEntry = houseIdForRoomEntry;
    }

    public LiveData<List<Long>> getAllMeterIdOfState(int readingState) {
        return mRepository.getAllMeterIdOfState(readingState);
    }

    public LiveData<List<RoomNoName>> getRoomNoName(long parentid) {
        return mRepository.getRoomNoName(parentid);
    }

    /*get the room data for specific room fragment*/
    public LiveData<TableRooms> getRoomFromRoomId(int roomId) {
        return mRepository.getRoomFromRoomId(roomId);
    }

    public int getRoomIdForSpecificRoom() {
        return roomIdForSpecificRoom;
    }

    public void setRoomIdForSpecificRoom(int roomIdForSpecificRoom) {
        this.roomIdForSpecificRoom = roomIdForSpecificRoom;
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

    /*For specific tenant Frogment*/
    public int getTenantIdForSpecificTenant() {
        return tenantIdForSpecificTenant;
    }

    public void setTenantIdForSpecificTenant(int gotid) {
        this.tenantIdForSpecificTenant = gotid;
    }

    /*Tenant entry table */
    /*Getting the values of House name and id which meet the condition.*/
    public LiveData<List<HouseNameAndId>> getHouseNameIdTEspinner() {
        return mRepository.getHouseNameIdTEspinner();
    }

    /* Getting the value of room name and id for entering in the room spinner*/
    public LiveData<List<RoomNoNameId>> getRoomNoNameID(int houseId, boolean isOccupied) {
        return mRepository.getRoomNoNameId(houseId, isOccupied);
    }

    /* modifying all meters data table*/
    public void insertNewMeterReading(AllMetersData metersData) {
        mRepository.insertNewMeterReading(metersData);
    }

    public LiveData<LastReadingWithDate> getLastEnteredMeterEntry(GetLastMeterReading lastMeterReading) {
        return mRepository.getLastEnteredMeterReading(lastMeterReading);
    }

    public LiveData<Date> getMeterCreateDate(long meterId) {
        return mRepository.getMeterCreateDate(meterId);
    }

    public LiveData<List<TenantNameHouseRoom>> getAllTenantNHR(boolean withRoomAlloted) {
        return mRepository.getAllTenantNHR(withRoomAlloted);
    }

    /*get all of selected tenant*/
    public LiveData<TenantsPersonal> getTenantFromId(int tenantIdForSpecificTenant) {
        return mRepository.getTenantFromId(tenantIdForSpecificTenant);
    }

    /*Get room name and house name for the specific tenant fragment*/
    public LiveData<MetersListObj> getHouseRoomNameFromRoomId(int roomid) {
        return mRepository.getHouseRoomNameFromRoomId(roomid);
    }

    /* Get all the reading of the meter.*/
    public LiveData<List<AllMetersData>> getAllMeterReadings(long meterId) {
        return mRepository.getAllMetersReading(meterId);
    }

    /* Get house name from house id*/
    public LiveData<String> getHouseNameFromHouseId(int houseid) {
        return mRepository.getHouseNameFromHouseId(houseid);
    }

    /* Bill entery fragment*/
    public LiveData<TenantBillEntry> getSelectedTenantForBill(int tenantId) {
        return mRepository.getSelectedTenantForBill(tenantId);
    }

    public void insertNewBill(Bills bills) {
        mRepository.insertNewBills(bills);
    }

    public LiveData<Boolean> getIsAnyActivetenant(boolean isactive) {
        return mRepository.getIsAntActiveTenant(isactive);
    }

    public LiveData<List<BillItemForCard>> getAllBillForCard(boolean isBillActive) {
        return mRepository.getAllBillForCard(isBillActive);
    }

    public LiveData<List<BillItemForCard>> getThreeBillForCard(int roomId) {
        return mRepository.getThreeBillForCard(roomId);
    }
}
