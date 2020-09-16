package com.tentlers.mngapp.data.tables;

import com.tentlers.mngapp.data.tables.meters.AllMetersData;

import java.sql.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class TableRooms {
    @Ignore
    public AllMetersData allMetersData;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    public int roomId;

    @ColumnInfo
    public int houseId;

    @ColumnInfo
    public int roomNo;

    @ColumnInfo(defaultValue = "NULL")
    public String roomName;

    @ColumnInfo
    public boolean isOcupied;
    @ColumnInfo(defaultValue = "NULL")
    public String tenantName;

    @ColumnInfo
    public long meterId;

    @ColumnInfo
    public boolean isMeterEnabled;

    @Ignore
    public boolean isSystemDeside;

    @ColumnInfo
    public Date date;

    public TableRooms() {
        allMetersData = new AllMetersData();
    }

    @ColumnInfo(defaultValue = "NULL")
    public int tenantId;

    public void setMeterId(long meterId) {
        this.meterId = meterId;
        allMetersData.setMeterId(meterId);
    }

    @Ignore
    public AllMetersData getAllMetersData() {
        return this.allMetersData;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        allMetersData.setDate(date);
    }
}
