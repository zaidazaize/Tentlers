package com.tentlers.mngapp.data.tables.meters;


public class MetersListObj {

    public long meterId;
    public String houseName;
    public String roomName;
    public boolean isOnlyHouse;

    public MetersListObj(long meterId, String houseName, String roomName, boolean isOnlyHouse) {
        this.meterId = meterId;
        this.houseName = houseName;
        this.roomName = roomName;
        this.isOnlyHouse = isOnlyHouse;
    }
}
