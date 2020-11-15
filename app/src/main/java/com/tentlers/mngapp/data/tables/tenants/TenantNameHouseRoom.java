package com.tentlers.mngapp.data.tables.tenants;

import com.tentlers.mngapp.data.tables.queryobjects.HouseAndRoomName;

import androidx.room.ColumnInfo;

public class TenantNameHouseRoom {
    @ColumnInfo(name = "tenantId")
    public int tenantId;

    @ColumnInfo(name = "unpaidAmt")
    public double unpaindAmt;

    @ColumnInfo(name = "tenantName")
    public String tenantName;
    @ColumnInfo(name = "roomId")
    public long roomId;

    /*  @ColumnInfo(name = "houseName")*/
    public String houseName;

/*
    @ColumnInfo(name = "roomName")
*/
    public String roomName;

    @ColumnInfo(name = "isRoomAlloted")
    public boolean isRoomAlloted;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setHosueNameRoomName(HouseAndRoomName nameRoomName) {
        this.roomName = nameRoomName.roomName;
        this.houseName = nameRoomName.houseName;
    }
}
