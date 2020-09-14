package com.tentlers.mngapp.data.tables.meters;

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
    private long meterId;

    public Date date;

    @ColumnInfo(defaultValue = "NULL")
    public long lastMeterReading;
    @Ignore
    public int Roomid;/* for entering the meter reading while creating the bill.*/

    public void setLastMeterReadingFromString(String lastMeterReading) {
        if (!lastMeterReading.isEmpty()) {
            this.lastMeterReading = Long.parseLong(lastMeterReading);
        } else this.lastMeterReading = 0;
    }

    public long getMeterId() {
        return meterId;
    }

    public void setMeterId(long meterId) {
        this.meterId = meterId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getLastMeterReading() {
        return lastMeterReading;
    }

    public void setLastMeterReading(long lastMeterReading) {
        this.lastMeterReading = lastMeterReading;
    }

    public int getRoomid() {
        return Roomid;
    }

    public void setRoomid(int roomid) {
        Roomid = roomid;
    }


}
