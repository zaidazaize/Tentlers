package com.tentlers.mngapp.data.tables.meters;

import java.sql.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AllMeters {
    /* TODO: to view all meters data.*/
    @PrimaryKey
    @ColumnInfo
    public long MeterId;
    @ColumnInfo
    public int houseId;
    @ColumnInfo
    public int roomId;
    @ColumnInfo
    public boolean isOnlyHouse;
    @ColumnInfo
    public Date createdate;

    public long getMeterId() {
        return MeterId;
    }

    public void setMeterId(long meterId) {
        MeterId = meterId;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public boolean isOnlyHouse() {
        return isOnlyHouse;
    }

    public void setOnlyHouse(boolean onlyHouse) {
        isOnlyHouse = onlyHouse;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

}
