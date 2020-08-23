package com.example.easelife.data.tables.queryobjects;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class HouseNameId {
    @ColumnInfo(name = "houseId")
    public int houseId;

    @ColumnInfo(name = "houseName")
    public String houseName;

    @ColumnInfo(name = "noOfRooms")
    public int noOfRooms;
}
