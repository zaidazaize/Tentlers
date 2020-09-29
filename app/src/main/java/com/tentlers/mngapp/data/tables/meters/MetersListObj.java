package com.tentlers.mngapp.data.tables.meters;

import com.tentlers.mngapp.data.tables.TableRooms;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

/*this is used to pass the choosen meter's data via a viewmodal to the specific meter fragment.*/
/*It is also use to fetch the house Name , room name , meter id for the specific tenant fragment.*/

public class MetersListObj {
    @Ignore
    public int houseId;

    public long meterId;
    public String houseName;
    public String roomName;
    @Ignore
    public boolean isOnlyHouse;

    public MetersListObj() {
    }

    /*constructor for specific house*/
    public MetersListObj(long meterId, String houseName, String roomName, boolean isOnlyHouse) {
        this.meterId = meterId;
        this.houseName = houseName;
        this.roomName = roomName;
        this.isOnlyHouse = isOnlyHouse;
    }

    public MetersListObj setMetersDataFromRoom(@NonNull TableRooms room) {
        meterId = room.getMeterId();
        roomName = room.getRoomName();
        houseId = room.getHouseId();
        isOnlyHouse = false;
        return this;
    }

}
