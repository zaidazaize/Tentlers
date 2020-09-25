package com.tentlers.mngapp.data.tables.rooms;

import androidx.room.ColumnInfo;

public class RoomForRoomList {
    @ColumnInfo(name = "roomId")
    public int roomId;

    @ColumnInfo(name = "roomNo")
    public int roomNo;

    @ColumnInfo(name = "roomName")
    public String roomName;

    @ColumnInfo(name = "ocupiedStatus")
    public boolean ocupiedStatus;

    @ColumnInfo(name = "tenantName")
    public String tenantName;
}
