package com.tentlers.mngapp.data.tables.rooms;

import androidx.room.ColumnInfo;

/* This class is for the tenant entry fragment showing room name in the spinner*/
public class RoomNoNameId {
    @ColumnInfo(name = "roomName")
    public String roomName;

    @ColumnInfo(name = "roomId")
    public int roomId;

    @ColumnInfo(name = "isMeterEnabled")
    public boolean isMeterEnabled;

    @ColumnInfo(name = "meterId")
    public long meterId;
}