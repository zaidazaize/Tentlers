package com.example.easelife.data.tables;

import java.security.PublicKey;
import java.sql.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class TableRooms {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    public int roomId;

    @ColumnInfo
    @NonNull
    public int houseId;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int roomNo;

    @ColumnInfo(defaultValue = "NULL")
    public String roomName;

    @ColumnInfo
    public boolean isOcupied;

    @ColumnInfo
    public long meterId;

    @ColumnInfo
    public boolean isMeterEnabled;

    @Ignore
    public boolean isSystemDeside;

    @ColumnInfo(defaultValue = "NULL")
    public String tenantsName;

    public Date date;

}
