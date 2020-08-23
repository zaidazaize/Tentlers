package com.example.easelife.data.database;

import com.example.easelife.data.tables.meters.AllMetersData;
import com.example.easelife.data.tables.queryobjects.HouseNameId;
import com.example.easelife.data.tables.queryobjects.HouseNameMeterId;
import com.example.easelife.data.tables.TableHouse;
import com.example.easelife.data.tables.TableRooms;
import com.example.easelife.data.tables.rooms.RoomNoName;
import com.example.easelife.data.tables.rooms.RoomNoNameId;
import com.example.easelife.data.tables.tenants.TenantNameHouseRoom;
import com.example.easelife.data.tables.tenants.TenantsPersonal;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface HouseDao {

    /*
     * For inserting new House
     */
    @Insert()
    void insertHouseRecord(TableHouse tableHouse);

    @Delete
    void deleteHouse(TableHouse tableHouse);

    /* Deleting all the records related to the house.*/
    @Query("DELETE FROM tablerooms WHERE houseId = :houseId")
    void deleteRoomsOfTheHouse(int houseId);

    @Query("DELETE FROM tenantspersonal WHERE houseId = :houseId")
    void deleteTenantsOfTheHouse(int houseId);

    @Update
    void updateHouseData(TableHouse tableHouse);

    @Query("SELECT * FROM TableHouse ORDER BY houseId")
    LiveData<List<TableHouse>> getAllTableHouse();

    @Query("SELECT houseName,meterid FROM TABLEHOUSE ORDER BY houseId DESC")
    LiveData<HouseNameMeterId[]> gethouseNameMeterId();

    /*
     * for updating no of rooms of the house
     */
    @Query("UPDATE tablehouse SET noOfRooms =noOfRooms + :increment  WHERE houseId = :houseid")
    void updateNoOfRoomsInTableHosue(int increment, int houseid);

    /*
     *For rooms
     */
    @Insert()
    void insetNewRoomRecord(TableRooms rooms);

    @Update
    void updateRoomData(TableRooms rooms);

    @Delete
    void deleteRoom(TableRooms rooms);

    @Query("SELECT * FROM TableRooms WHERE houseId = :givenhouseId ORDER BY date")
    LiveData<List<TableRooms>> getAllRoomsOfHouse(int givenhouseId);

    @Query("SELECT * FROM TableRooms  WHERE houseId = :houseidgot ORDER BY date LIMIT 3 ")
    LiveData<List<TableRooms>> getThreerooms(int houseidgot);

    /*
     *For displaying house names in the spinner of rooms fragment.
     */
    @Query("SELECT houseName , houseId, noOfRooms FROM TableHouse WHERE noOfRooms > 0")
    LiveData<List<HouseNameId>> getHouseNameId();

    /*
     * For extracting all meter ids used for comparison while entering meterno for rooms.
     */
    @Query("SELECT meterid FROM tablerooms")
    LiveData<List<Long>> getRoomMeter();

    @Query("SELECT meterId FROM tablehouse")
    LiveData<List<Long>> getHouseMeterid();


    /*
     *For managing uniquness of room name in room entry fragment for a specific house.
     */
    @Query("SELECT roomNo,roomName FROM tablerooms WHERE houseId = :houseid ORDER BY roomNo DESC")
    LiveData<List<RoomNoName>> getroomNoName(long houseid);

    /*
     * For Tenants Personal Table
     */
    @Insert()
    void insertNewTenant(TenantsPersonal tenantsPersonal);

    @Update
    void updateTenant(TenantsPersonal tenantsPersonal);

    @Delete
    void deleteTenant(TenantsPersonal tenantsPersonal);

    /*for tenant entry fragment*/

    /*for showing the empty rooms in the spinner of tenant entry fragment*/
    /* 0 is the integer value of boolean false.*/
    @Query("SELECT roomId,roomName,isMeterEnabled, meterId FROM tablerooms WHERE houseId = :thehouseId AND isOcupied = 0")
    LiveData<List<RoomNoNameId>> getroomNoNameId(int thehouseId);

    /* For entering meter reading.*/
    @Insert()
    void insertMeterReading(AllMetersData metersData);

    @Query("SELECT lastMeterReading FROM allmetersdata WHERE meterId = :givenMeterId ORDER BY date LIMIT :noOfReadings ")
    LiveData<Long[]> getLastMeterEntry(long givenMeterId, int noOfReadings);

    @Query("SELECT allmetersdata.lastMeterReading "
            + "FROM allmetersdata , tablehouse" +
            " WHERE  tablehouse.meterid = allmetersdata.meterId AND tablehouse.houseId = :gotHouseid " +
            "ORDER BY allmetersdata.date LIMIT :noOfreadings ")
    LiveData<Long[]> getLastMeterReadingForHouse(long gotHouseid, int noOfreadings);

    @Query("SELECT allmetersdata.lastMeterReading "
            + "FROM allmetersdata , tablerooms" +
            " WHERE tablerooms.meterId = allmetersdata.meterId" +
            " AND tablerooms.roomId = :gotRoomid " +
            "ORDER BY allmetersdata.date LIMIT :noOfReadings ")
    LiveData<Long[]> getLastMeterReadingForRoom(int gotRoomid, int noOfReadings);

    /* update no of occupied rooms*/
    @Query("UPDATE tablehouse SET emptyrooms = emptyrooms + :updaterooms WHERE houseId = :gothouseId")
    void updateNoOfEmptyRoomsInTable(int updaterooms, int gothouseId);

    /* update the room status to occupied */
    @Query("UPDATE tablerooms SET isOcupied = :gotIsOccupied WHERE roomId = :gotRoomid")
    void updatetheRoomOccupiedStatus(boolean gotIsOccupied, int gotRoomid);

    /* Tenant fragment. Getting the all tenant information.*/
    @Query("SELECT tenantName, houseName,roomName " +
            "FROM tenantspersonal,tablehouse , tablerooms " +
            "WHERE tenantspersonal.houseId = tablehouse.houseId " +
            "AND tenantspersonal.roomId = tablerooms.roomId " +
            "AND tenantspersonal.isRoomAlloted = :gotisRoomAlloted")
    LiveData<List<TenantNameHouseRoom>> getAllTenantNHR(boolean gotisRoomAlloted);

}

