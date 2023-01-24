package com.tentlers.mngapp.data;

import android.app.Application;

import com.tentlers.mngapp.data.database.Repository;
import com.tentlers.mngapp.data.tables.house.TableHouse;
import com.tentlers.mngapp.data.tables.rooms.TableRooms;
import com.tentlers.mngapp.data.tables.bills.BillEntryTypeObject;
import com.tentlers.mngapp.data.tables.bills.BillItemForCard;
import com.tentlers.mngapp.data.tables.bills.Bills;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.meters.GetLastMeterReading;
import com.tentlers.mngapp.data.tables.meters.LastReadingWithDate;
import com.tentlers.mngapp.data.tables.meters.MeterEditType;
import com.tentlers.mngapp.data.tables.meters.MetersListObj;
import com.tentlers.mngapp.data.tables.queryobjects.HouseAndRoomName;
import com.tentlers.mngapp.data.tables.queryobjects.HouseForHomeFragment;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameAndId;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameIdNoRooms;
import com.tentlers.mngapp.data.tables.rooms.RoomForRoomList;
import com.tentlers.mngapp.data.tables.rooms.RoomNoName;
import com.tentlers.mngapp.data.tables.rooms.RoomNoNameId;
import com.tentlers.mngapp.data.tables.tenants.TenantBillEntry;
import com.tentlers.mngapp.data.tables.tenants.TenantNameHouseRoom;
import com.tentlers.mngapp.data.tables.tenants.TenantNameId;
import com.tentlers.mngapp.data.tables.tenants.TenantsPersonal;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class HouseViewModal extends AndroidViewModel {

    private long houseIdForSpecificHouse;
    private String userName;
    private TableHouse houseForEdit;/*keeps the house choosen to be eddited.*/

    private final Repository mRepository;
    private LiveData<List<HouseForHomeFragment>> mAllHouse;
    private long houseIdForRoomEntry;

    /*transfers data about which meter reading history is to be shown*/
    private MetersListObj metersListObj;

    private MeterEditType meterEditType;

    private long roomIdForSpecificRoom;

    private long tenantIdForSpecificTenant;

    private TenantsPersonal tenantForEdit;

    /*filter object that is used to monitor the filters in the different fields*/
    MutableLiveData<FilterObj> tenantFilterObj = new MutableLiveData<>();

    /*defines which from where bill is triggered*/
    private BillEntryTypeObject billEntryType;

    public HouseViewModal(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllHouse = mRepository.mgetAllHouseForHomeFragment();
    }


    public void setTenantFilterObj(FilterObj tenantFilterObj) {
        this.tenantFilterObj.setValue(tenantFilterObj);
    }

    public MeterEditType getMeterEditType() {
        return meterEditType;
    }

    public void setMeterEditType(MeterEditType meterEditType) {
        this.meterEditType = meterEditType;
    }

    public TenantsPersonal getTenantForEdit() {
        return tenantForEdit;
    }

    public void setTenantForEdit(TenantsPersonal tenantForEdit) {
        this.tenantForEdit = tenantForEdit;
    }


    public BillEntryTypeObject getBillEntryType() {
        return billEntryType;
    }

    public void setBillEntryType(BillEntryTypeObject billEntryType) {
        this.billEntryType = billEntryType;
    }

    public TableHouse getHouseForEdit() {
        return houseForEdit;
    }

    public void setHouseForEdit(TableHouse houseForEdit) {
        this.houseForEdit = houseForEdit;
    }

    public MetersListObj getMetersListObj() {
        return metersListObj;
    }

    public void setMetersListObj(MetersListObj metersListObj) {
        this.metersListObj = metersListObj;
    }


    public LiveData<List<HouseForHomeFragment>> getAllHouseForHomeFragment() {
        return mAllHouse;
    }

    public LiveData<TableHouse> getHouseFromHouseId(long houseId) {
        return mRepository.getHosueFromHouseId(houseId);
    }

    public void insertHouse(TableHouse tableHouse) {
        mRepository.insertNewHouse(tableHouse);
    }

    public void updateHouse(TableHouse tableHouse) {
        mRepository.updateHouse(tableHouse);
    }

    public void deleteHosue(TableHouse tableHouse) {
        mRepository.deleteHouse(tableHouse);
    }

    public LiveData<List<String>> getAllHouseNames() {
        return mRepository.getAllHouseNames();

    }

    /* For displaying specific House details. */
    public long getHouseIdForSpecificHouse() {
        return houseIdForSpecificHouse;
    }

    public void setHouseIdForSpecificHouse(long houseid) {
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
    public LiveData<List<RoomForRoomList>> getThreeRooms(long houseId) {
        return mRepository.getThreeRooms(houseId);
    }

    /*
     * This is used to display all the rooms in the Room fragment
     */
    public LiveData<List<RoomForRoomList>> getAllRooms(int houseId) {
        return mRepository.getAllRooms(houseId);
    }

    /* For transfering the house id to the room entry fragment*/

    public long getHouseIdForRoomEntry() {
        return houseIdForRoomEntry;
    }

    public void setHouseIdForRoomEntry(long houseIdForRoomEntry) {
        this.houseIdForRoomEntry = houseIdForRoomEntry;
    }

    public LiveData<List<Long>> getAllMeterIdOfState(int readingState) {
        return mRepository.getAllMeterIdOfState(readingState);
    }

    public LiveData<List<RoomNoName>> getRoomNoName(long parentid) {
        return mRepository.getRoomNoName(parentid);
    }

    /*get the room data for specific room fragment*/
    public LiveData<TableRooms> getRoomFromRoomId(long roomId) {
        return mRepository.getRoomFromRoomId(roomId);
    }

    public long getRoomIdForSpecificRoom() {
        return roomIdForSpecificRoom;
    }

    public void setRoomIdForSpecificRoom(long roomIdForSpecificRoom) {
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
    public long getTenantIdForSpecificTenant() {
        return tenantIdForSpecificTenant;
    }

    public void setTenantIdForSpecificTenant(long gotid) {
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

    public LiveData<List<TenantNameHouseRoom>> getTenantForTenantList() {

        return Transformations.switchMap(tenantFilterObj, new Function<FilterObj, LiveData<List<TenantNameHouseRoom>>>() {
            @Override
            public LiveData<List<TenantNameHouseRoom>> apply(FilterObj input) {
                return mRepository.getTenantForTenantList(input);
            }
        });
    }

    /*get all the house name and house id */
    public LiveData<List<HouseNameAndId>> getAllHouseNameAndId() {
        return mRepository.getAllHouseNameAndId();
    }

    /*get all of selected tenant*/
    public LiveData<TenantsPersonal> getTenantFromId(long tenantIdForSpecificTenant) {
        return mRepository.getTenantFromId(tenantIdForSpecificTenant);
    }

    /*Get room name and house name for the specific tenant fragment*/
    public LiveData<MetersListObj> getHouseRoomNameFromRoomId(long roomid) {
        return mRepository.getHouseRoomNameFromRoomId(roomid);
    }


    /*get the house name and room name from roomid*/
    public HouseAndRoomName getHouseNameRoomNameFromRoomId(long roomid) {
        return mRepository.getHouseNameRoomNameFromRoomId(roomid);
    }

    /* Get all the reading of the meter.*/
    public LiveData<List<AllMetersData>> getAllMeterReadings(long meterId) {
        return mRepository.getAllMetersReading(meterId);
    }

    /* Get house name from house id*/
    public LiveData<String> getHouseNameFromHouseId(long houseid) {
        return mRepository.getHouseNameFromHouseId(houseid);
    }

    /* Bill entery fragment*/

    public LiveData<List<TenantNameId>> getAllTenantNameid(boolean isAlloted) {
        return mRepository.getAllTenantNameId(isAlloted);
    }

    public LiveData<Integer> getTenantIdFromRoomId(int roomId) {
        return mRepository.getTenantIdFromRoomId(roomId);
    }

    public LiveData<TenantBillEntry> getSelectedTenantForBill(long tenantId) {
        return mRepository.getSelectedTenantForBill(tenantId);
    }

    public void insertNewBill(Bills bills) {
        mRepository.insertNewBills(bills);
    }

    /*for updating bill paid status*/
    public void updateBillPaidStatus(BillItemForCard billItemForCard) {
        mRepository.updateBillStatus(billItemForCard);
    }
    public LiveData<Boolean> getIsAnyActivetenant(boolean isactive) {
        return mRepository.getIsAntActiveTenant(isactive);
    }

    public LiveData<List<BillItemForCard>> getAllBillForCard(boolean isBillpaid) {
        return mRepository.getAllBillForCard(isBillpaid);
    }

    public LiveData<List<BillItemForCard>> getThreeBillForCard(long roomId) {
        return mRepository.getThreeBillForCard(roomId);
    }

    /*for geting all meter nos*/
    public LiveData<List<Long>> getAllMeterNos() {
        return mRepository.getAllMeterNos();
    }

    /*get meter no from meter id*/
    public LiveData<Long> getMeterNoFromMeterId(GetLastMeterReading readingType) {
        return mRepository.getMeterNoFromMeteId(readingType);
    }

    /*for editing meter details*/
    public void updateMeterDetails(MeterEditType editDetails) {
        mRepository.updateMeterDetails(editDetails);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
