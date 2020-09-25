package com.tentlers.mngapp.data.tables.meters;

/*this is used to pass the choosen meter's data via a viewmodal to the specific meter fragment.*/
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
