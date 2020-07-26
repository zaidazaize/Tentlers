package com.example.easelife.data.database;

import com.example.easelife.data.tables.queryobjects.HouseNameId;
import com.example.easelife.data.tables.queryobjects.HouseNameMeterId;
import com.example.easelife.data.tables.TableHouse;
import com.example.easelife.data.tables.TableRooms;
import com.example.easelife.data.tables.rooms.RoomNoName;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface HouseDao {
    @Insert()
    void insertHouseRecord(TableHouse tableHouse);

    @Delete
    void deleteHouse(TableHouse tableHouse);

    @Update
    void updateHouseData(TableHouse tableHouse);

    @Query("SELECT * FROM TableHouse")
    LiveData<List<TableHouse>> getAllTableHouse();

    @Query("SELECT houseName,meterid FROM TABLEHOUSE ORDER BY date DESC")
    HouseNameMeterId[] gethouseNameMeterId();

    // For rooms
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

    // For displaying rooms specific to the house data is shown in spinner
    @Query("SELECT houseName , houseId FROM TableHouse ")
    LiveData<List<HouseNameId>> getHouseNameId();

    /*
    * For extracting all meter ids used for comparison while entering meterno for rooms.
    */
    @Query("SELECT meterid FROM tablerooms")
    LiveData<List<Long>> getRoomMeter();

    @Query("SELECT meterId FROM tablehouse")
    LiveData<List<Long>> getHouseMeterid();


    // For managing uniquness of room name in room entry fragment
    @Query("SELECT roomNo,roomName FROM tablerooms WHERE houseId = :houseid ORDER BY roomNo DESC")
    LiveData<List<RoomNoName>> getroomNoName(long houseid);
}

