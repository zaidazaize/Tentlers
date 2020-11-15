package com.tentlers.mngapp.data.tables.queryobjects;

import androidx.room.ColumnInfo;

public class HouseAndRoomName {
    @ColumnInfo(name = "houseName")
    public String houseName;

    @ColumnInfo(name = "roomName")
    public String roomName;
}
