package com.example.easelife.data.tables.queryobjects;

import java.sql.Date;

import androidx.room.ColumnInfo;

public class HouseForHomeFragment extends HouseNameIdNoRooms {
    @ColumnInfo(name = "occupiedRooms")
    public int occupiedRooms;


    @ColumnInfo(name = "date")
    public Date date;
}
