package com.example.easelife.data.tables.queryobjects;

import androidx.room.ColumnInfo;

public class HouseNameIdNoRooms extends HouseNameAndId {

    @ColumnInfo(name = "noOfRooms")
    public int noOfRooms;
}
