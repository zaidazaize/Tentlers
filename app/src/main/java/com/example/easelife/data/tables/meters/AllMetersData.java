package com.example.easelife.data.tables.meters;

import java.sql.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class AllMetersData {
    @PrimaryKey(autoGenerate = true)
    public long entryId;

    @ColumnInfo(defaultValue = "NULL")
    public long meterId;

    public Date date;

    @ColumnInfo(defaultValue = "NULL")
    public long lastMeterReading;

    public void setLastMeterReadingFromString(String lastMeterReading) {
        if (!lastMeterReading.isEmpty()) {
            this.lastMeterReading = Long.parseLong(lastMeterReading);
        } else this.lastMeterReading = 0;
    }

    @Ignore
    public int Roomid;/* for entering the meter reading while creating the bill.*/
}
