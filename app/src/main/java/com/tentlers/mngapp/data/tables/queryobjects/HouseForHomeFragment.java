package com.tentlers.mngapp.data.tables.queryobjects;


import java.util.Date;

import androidx.room.ColumnInfo;

public class HouseForHomeFragment extends HouseNameIdNoRooms {
    @ColumnInfo(name = "occupiedRooms")
    public int occupiedRooms;


    @ColumnInfo(name = "date")
    public Date date;


}
