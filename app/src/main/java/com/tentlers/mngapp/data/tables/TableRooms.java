package com.tentlers.mngapp.data.tables;

import com.tentlers.mngapp.data.tables.meters.AllMetersData;

import java.sql.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class TableRooms {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    public int roomId;

    @ColumnInfo
    public int houseId;

    public int roomNo;

    @ColumnInfo(defaultValue = "NULL")
    public String roomName;

    @ColumnInfo
    public boolean isOcupied;

    @Ignore
    public AllMetersData allMetersData = new AllMetersData();

    @ColumnInfo
    public long meterId;

    @ColumnInfo
    public boolean isMeterEnabled;

    @Ignore
    public boolean isSystemDeside;
    @ColumnInfo
    public Date date;
    @ColumnInfo(defaultValue = "NULL")
    public String tenantsName;
    @ColumnInfo(defaultValue = "NULL")
    public int tenantId;

    public void setMeterId(long meterId) {
        this.meterId = meterId;
        allMetersData.meterId = meterId;
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
        allMetersData.date = date;
    }
}
