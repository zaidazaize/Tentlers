package com.example.easelife.data.tables.meters;

import java.sql.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AllMetersData {
    @PrimaryKey()
    public long meterId;

    public Date date;

    public long lastMeterReading;
}
