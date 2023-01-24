package com.tentlers.mngapp.data.database;

import com.tentlers.mngapp.data.tables.house.TableHouse;
import com.tentlers.mngapp.data.tables.rooms.TableRooms;
import com.tentlers.mngapp.data.tables.bills.BillItemForCard;
import com.tentlers.mngapp.data.tables.bills.Bills;
import com.tentlers.mngapp.data.tables.meters.AllMeters;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.meters.LastReadingWithDate;
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

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public interface HouseDao {

    /*For inserting new House*/
    @Insert()
    void insertHouseRecord(TableHouse tableHouse);

    @Delete
    void deleteHouse(TableHouse tableHouse);

    @Query("DELETE FROM TableHouse WHERE houseId = :gotHouseId")
    void deleteHouseById(long gotHouseId);

    /* Deleting all the records related to the house.*/
    @Query("DELETE FROM tablerooms WHERE houseId = :houseId")
    void deleteRoomsOfTheHouse(long houseId);

    @Query("DELETE FROM tenantspersonal WHERE houseId = :houseId")
    void deleteTenantsOfTheHouse(long houseId);

    @Update
    void updateHouseData(TableHouse tableHouse);

    @Query("UPDATE TableHouse SET meterid = :gotmeterid , isMeterIncluded = :isIncluded WHERE houseId = :gothouseId")
    void updateMeterIdInTableHouse(long gotmeterid, boolean isIncluded, long gothouseId);

    /*select all the house to display in the list on home fragment.*/
    @Query("SELECT houseId, houseName, noOfRooms,occupiedRooms,date FROM TableHouse ORDER BY houseId")
    LiveData<List<HouseForHomeFragment>> getAllHouseForHomeFragment();

    /*for displaying specific house.*/
    @Query("SELECT * FROM TableHouse WHERE houseId = :gotHouseid")
    LiveData<TableHouse> getHouseFromHouseId(long gotHouseid);

    /*for house entry fragment*/
    /*selects all the house names for comparison*/
    @Query("SELECT houseName FROM TABLEHOUSE ")
    LiveData<List<String>> getAllHouseNames();

    /* for updating no of rooms of the house*/
    @Query("UPDATE tablehouse SET noOfRooms =noOfRooms + :increment  WHERE houseId = :houseid")
    void updateNoOfRoomsInTableHosue(int increment, long houseid);

    @Query("UPDATE tablehouse " +
            "SET houseName = :gothouseName " +
            "AND houseNo = :gothouseNo AND " +
            "locality = :gotLocality AND " +
            "postalcode = :gotpostalcode " +
            "AND city = :gotcity AND " +
            "country = :gotcountry")
    void updateHouseRecord(String gothouseName, int gothouseNo, String gotLocality, String gotpostalcode, String gotcity, String gotcountry);

    /*For rooms*/
    @Insert()
    void insetNewRoomRecord(TableRooms rooms);

    @Update
    void updateRoomData(TableRooms rooms);

    @Delete
    void deleteRoom(TableRooms rooms);

    /*gets all the rooms of a house for room list*/
    @Query("SELECT tablerooms.roomId,tablerooms.roomNo,tablerooms.roomName,tablerooms.ocupiedStatus,tablerooms.tenantName" +
            " FROM tableRooms" +
            " WHERE tablerooms.houseId = :givenhouseId " +
            "ORDER BY date")
    LiveData<List<RoomForRoomList>> getAllRoomsOfHouse(int givenhouseId);

    /*gets three rooms of the house for specific house list*/
    @Query("SELECT tablerooms.roomId,tablerooms.roomNo,tablerooms.roomName,tablerooms.ocupiedStatus,tablerooms.tenantName" +
            " FROM TableRooms " +
            "WHERE tablerooms.houseId = :houseidgot" +
            " ORDER BY date LIMIT 3 ")
    LiveData<List<RoomForRoomList>> getThreerooms(long houseidgot);

    /*
     *For displaying house names in the spinner of rooms fragment.
     */
    @Query("SELECT houseName , houseId, noOfRooms FROM TableHouse")
    LiveData<List<HouseNameIdNoRooms>> getHouseNameId();

    /* For extracting all meter ids used for comparison while entering meterno for rooms.*/
    @Query("SELECT meterid FROM allmetersdata WHERE readingState = :gotReadingState")
    LiveData<List<Long>> getAllMeterIdOfState(int gotReadingState);

    /*
     *For managing uniquness of room name in room entry fragment for a specific house.
     */
    @Query("SELECT roomNo,roomName FROM tablerooms WHERE houseId = :houseid ORDER BY roomNo DESC")
    LiveData<List<RoomNoName>> getroomNoName(long houseid);

    /*Get all the room data for specific room fragment.*/
    @Query("SELECT * FROM tablerooms WHERE roomId = :gotRoomId")
    LiveData<TableRooms> getRoomFromRoomId(long gotRoomId);

    /*update the tenant  occupied status */
    @Query("UPDATE tenantspersonal SET houseId = :gotHouseId , roomId = :gotRoomId AND isRoomAlloted = :isAlloted WHERE tenantId = :gotTenantId")
    void upDateTenantOccupiedStatus(int gotHouseId, int gotRoomId, boolean isAlloted, int gotTenantId);

    /*for updating meter id in table rooms*/
    @Query("UPDATE tablerooms SET meterId = :gotMeterId , isMeterEnabled = :isMeterEnabled WHERE roomId = :gotRoomId")
    void updateMeterInTableRoom(long gotMeterId, boolean isMeterEnabled, long gotRoomId);

    /* For Tenants Personal Table*/
    @Insert()
    void insertNewTenant(TenantsPersonal tenantsPersonal);

    @Update
    void updateTenant(TenantsPersonal tenantsPersonal);

    /*update tenant name in room record*/
    @Query("UPDATE tablerooms set tenantName = :gottenantname WHERE roomId = :gotroomid")
    void updateTenantNameInRoom(String gottenantname, long gotroomid);

    @Delete
    void deleteTenant(TenantsPersonal tenantsPersonal);

    /*for tenant entry fragment*/

    /*
     * For showing the eligible houses to which tenant can be added.
     * such having atleast one room available that can be added.
     */
    @Query("SELECT houseName,houseId FROM tablehouse WHERE noOfRooms >0 AND occupiedRooms < noOfRooms")
    LiveData<List<HouseNameAndId>> getHouseNameIdForTEspinner();

    /*
     * for showing the empty rooms in the spinner of tenant entry fragment
     */
    @Query("SELECT roomId,roomName,isMeterEnabled, meterId FROM tablerooms WHERE houseId = :thehouseId AND ocupiedStatus = :gotOccupied")
    LiveData<List<RoomNoNameId>> getroomNoNameId(int thehouseId, boolean gotOccupied);

    /*
     * update number of occupied rooms.
     */
    @Query("UPDATE tablehouse SET occupiedRooms = occupiedRooms + :updaterooms WHERE houseId = :gothouseId")
    void updateNoOfEmptyRoomsInTable(int updaterooms, long gothouseId);

    /* update the room status to occupied*/
    @Query("UPDATE tablerooms SET ocupiedStatus = :gotIsOccupied, tenantId = :gottenantid, tenantName = :gotTenantName,tenantEntryDate = :gotEntryDate WHERE roomId = :gotRoomid")
    void updatetheRoomOccupiedStatusAndName(boolean gotIsOccupied, long gotRoomid, int gottenantid, String gotTenantName, Date gotEntryDate);

    /*get the new tenant if from the roomid*/
    @Query("SELECT tenantId FROM tenantspersonal WHERE roomId = :gotroomid")
    Integer getTenantIdFromRoomIdFromTenantPersonal(long gotroomid);

//    /*udate the tenant name in the rooms table.*/
//    @Query("UPDATE tablerooms SET tenantName = :gotTenantName AND tenantEntryDate = :gotEntrydate WHERE roomId = :gotroomId")
//    void updateTenantNameInRoom(int gotroomId, String gotTenantName, Date gotEntrydate);


    /* Tenant fragment.
     * Getting the all tenant information.
     * This is can handle both the querries for currently active tenant and previously active tenant.
     * in the houses
     */

    /*get all house name and house id*/
    @Query("SELECT houseName, houseId FROM tablehouse")
    LiveData<List<HouseNameAndId>> getAllHouseNameAndId();
/*
    @Deprecated
    @Query("SELECT tenantspersonal.tenantId,isRoomAlloted, tenantspersonal.tenantName, tenantspersonal.unpaidAmt, houseName,roomName " +
            "FROM tenantspersonal,tablehouse , tablerooms " +
            "WHERE tenantspersonal.houseId = tablehouse.houseId " +
            "AND tenantspersonal.roomId = tablerooms.roomId " +
            "AND tenantspersonal.isRoomAlloted = :gotisRoomAlloted")
    LiveData<List<TenantNameHouseRoom>> getAllTenantForList(boolean gotisRoomAlloted);*/

    /*tenant list fragment*/
    /* this raw querry is used to generate the runtime querries as per the filter options*/
    @RawQuery(observedEntities = TenantsPersonal.class)
    LiveData<List<TenantNameHouseRoom>> getTenantForList(SupportSQLiteQuery supportSQLiteQuery);

    /*Specific tenant Fragment*/
    /*get all tenants data for specifec tenant fragment*/
    @Query("SELECT * FROM tenantspersonal WHERE tenantId = :gotTenantId")
    LiveData<TenantsPersonal> getTenantFromId(long gotTenantId);


    /*Get room name and house name for the specific tenant fragment*/
    @Query("SELECT houseName,roomName,tablerooms.meterId " +
            "From TableHouse,TableRooms where tablerooms.roomId = :gotRoomId AND " +
            "tablerooms.houseId = tablehouse.houseId LIMIT 1")
    LiveData<MetersListObj> getHouseRoomNameFromRoomId(long gotRoomId);

    /*get the house name and room name from the room id*/
    @Query("SELECT roomName,houseName FROM tablerooms ,tablehouse " +
            "WHERE tablehouse.houseId = tablerooms.houseId AND " +
            "tablerooms.roomid = :gotroomId")
    HouseAndRoomName getHouseNameRoomNameFromRoomId(long gotroomId);

    /* Bills Fragment*/

    /*get all the tenant for bill entry fragment*/
    @Query("SELECT tenantId,tenantName FROM TenantsPersonal WHERE isRoomAlloted = :isAlloted ")
    LiveData<List<TenantNameId>> getAllTenantNameId(boolean isAlloted);

    @Query("SELECT tenantId FROM tablerooms WHERE roomId = :gotroomid ")
    LiveData<Integer> getTenantIdFromRoomID(int gotroomid);

    /* Bill entry fragment.
     * returns the selected tenant's :  tenantsPersonal object */
    @Transaction
    @Query("SELECT tenantspersonal.tenantName, tenantspersonal.roomId, meterPay, nonMeterPay, mFixedCharges , roomName , houseName " +
            "FROM TenantsPersonal ,tableRooms, tablehouse " +
            "WHERE tenantspersonal.tenantId = :gotTenantId AND tablerooms.roomId = tenantsPersonal.roomId AND tableHouse.houseId = tenantspersonal.houseId ")
    LiveData<TenantBillEntry> getSelectedTenantForBill(long gotTenantId);

    /* For creating new bill.*/
    @Insert
    void insertNewBill(Bills bills);

    @Update
    void updateBill(Bills bills);

    /* Update the Total bills total unpaid amt in the table while creating the bill*/
    @Query("UPDATE tenantspersonal SET totalBills = totalBills + :gotTotalbills , unpaidAmt = unpaidAmt + :gotamt WHERE tenantId = :gotTenantId")
    void updateTotalBillsAndUnpaidAmtinTenant(int gotTotalbills, double gotamt, long gotTenantId);

    /* Update the paid bills*/
    @Query("UPDATE tenantspersonal SET paidBills = paidBills + :paidBills , unpaidAmt = unpaidAmt + :gotAmt WHERE tenantId = :gotTenantId")
    void updateNoOfPaidBillsAndUnpaidAmtInTenant(int paidBills, double gotAmt, long gotTenantId);

    /*update the total unpaid amt*/
    @Query("UPDATE tenantspersonal SET unpaidAmt = :gotamt WHERE tenantId = :gottenantId")
    void updateUnpaidAmt(long gottenantId, double gotamt);

    /*for updating paid status*/
    @Query("UPDATE bills SET isBillPaid = :gotPaidStatus WHERE billId = :gotBillId")
    void updateBillPaidStatus(long gotBillId, boolean gotPaidStatus);

    /* Getting to know if atleast one tenant is active*/
    @Query("SELECT isRoomAlloted FROM tenantspersonal WHERE isRoomAlloted = :getbolean LIMIT 1 ")
    LiveData<Boolean> getIsAnyTenantActive(boolean getbolean);

    /*Showing bills in recycle view of bills page*/
    @Transaction
    @Query("SELECT billId, bills.createDate, bills.monthlycharge,bills.tenantId, bills.additionalcharge, bills.totalAmt, " +
            "bills.electricCost, bills.isBillPaid, tenantspersonal.tenantName, tablehouse.houseName, tablerooms.roomName " +
            "FROM bills,tenantspersonal,tablehouse,tablerooms " +
            "WHERE bills.tenantId =  tenantspersonal.tenantId  " +
            "AND TenantsPersonal.roomId = tablerooms.roomId " +
            "AND TenantsPersonal.houseId = tableHouse.houseId " +
            "AND bills.isBillPaid = :gotBillPaid ORDER BY bills.createDate DESC")
    LiveData<List<BillItemForCard>> getAllBillForCard(boolean gotBillPaid);

    /*get three bills for card.*/
    @Transaction
    @Query("SELECT billId, bills.createDate, bills.monthlycharge,bills.tenantId, bills.additionalcharge, bills.totalAmt, " +
            "bills.electricCost, bills.isBillPaid, tenantspersonal.tenantName, tablehouse.houseName, tablerooms.roomName " +
            "FROM bills,tenantspersonal,tablehouse,tablerooms " +
            "WHERE bills.tenantId =  tenantspersonal.tenantId  " +
            "AND TenantsPersonal.roomId = tablerooms.roomId " +
            "AND TenantsPersonal.houseId = tableHouse.houseId " +
            " AND tenantspersonal.roomId = :gotRoomId ORDER BY bills.createDate DESC LIMIT 3")
    LiveData<List<BillItemForCard>> getThreeBillForRoom(long gotRoomId);

    /* For meters*/
    /* For entering meter reading.*/
    @Insert()
    void insertMeterReading(AllMetersData metersData);

    /* getting last meter reading based on the either house id , room id or meter id
     * logic of handling which querry to call is handled in repository.*/
    /* new date will be larger than the previous date.*/

    @Query("SELECT lastMeterReading,allmetersdata.date FROM allmetersdata WHERE meterId = :givenMeterId ORDER BY date DESC LIMIT :noOfReadings ")
    LiveData<LastReadingWithDate> getLastMeterEntry(long givenMeterId, int noOfReadings);

    @Query("SELECT allmetersdata.lastMeterReading,allmetersdata.date "
            + "FROM allmetersdata , tablehouse" +
            " WHERE  tablehouse.meterid = allmetersdata.meterId AND tablehouse.houseId = :gotHouseid " +
            "ORDER BY allmetersdata.date DESC LIMIT :noOfreadings ")
    LiveData<LastReadingWithDate> getLastMeterReadingForHouse(long gotHouseid, int noOfreadings);

    @Query("SELECT allmetersdata.lastMeterReading,allmetersdata.date "
            + "FROM allmetersdata , tablerooms" +
            " WHERE tablerooms.meterId = allmetersdata.meterId" +
            " AND tablerooms.roomId = :gotRoomid " +
            "ORDER BY allmetersdata.date DESC LIMIT :noOfReadings ")
    LiveData<LastReadingWithDate> getLastMeterReadingForRoom(long gotRoomid, int noOfReadings);

    /*get create date of meter.It returns the date on which meter was added to the room/house*/
    @Query("SELECT createdate FROM allmeters where meterId = :gotMeterId ")
    LiveData<Date> getMeterCreateDate(long gotMeterId);

    /* get Meter no from room id*/
    @Query("SELECT meterid FROM tablerooms WHERE roomId = :gotRoomId")
    long getMeterId(int gotRoomId);

    /* Get all the reading of a meter*/
    @Query("SELECT * FROM AllMetersData WHERE meterId = :gotMeterid")
    LiveData<List<AllMetersData>> getAllMeterReading(long gotMeterid);

    /*get house name from house id*/
    @Query("SELECT houseName FROM tablehouse WHERE houseId = :gothouseId")
    LiveData<String> getHouseNameFromHosueId(long gothouseId);

    /*for all meters table*/
    @Insert
    long insertNewMeter(AllMeters meter);

    @Update
    void updateMeter(AllMeters meters);

    @Query("SELECT meterNo FROM allmeters ;")
    LiveData<List<Long>> getAllMeterNos();

    /*get meter no from the meter  id*/
    @Query("SELECT meterNo FROM allmeters WHERE meterId = :meterId")
    LiveData<Long> getMeterNoFromMeterId(long meterId);

    /*get meter no from room id */
    @Query("SELECT meterNo FROM allmeters,tablerooms WHERE " +
            "allmeters.meterId = tablerooms.meterId AND roomId = :gotroomId")
    LiveData<Long> getMeterNofromRoomId(long gotroomId);

    @Query("SELECT meterNo FROM allmeters,tablerooms " +
            "WHERE allmeters.meterId = tablerooms.meterId AND " +
            "tablerooms.tenantId = :gottenantid")
    LiveData<Long> getMeterNoFromTenantId(long gottenantid);

    /*update the meter no in the all meters id*/
    @Query("UPDATE allmeters SET meterNo = :gotmeterno WHERE meterId = :gotmeterid")
    void updateMeterNoFromMeterId(long gotmeterno, long gotmeterid);
}

