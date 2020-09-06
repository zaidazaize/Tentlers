package com.tentlers.mngapp.data.tables.queryobjects;

import androidx.room.ColumnInfo;

public class HouseNameAndId {
    @ColumnInfo(name = "houseId")
    public int houseId;

    @ColumnInfo(name = "houseName")
    public String houseName;
}
