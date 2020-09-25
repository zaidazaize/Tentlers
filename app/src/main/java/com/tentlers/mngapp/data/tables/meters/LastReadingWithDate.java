package com.tentlers.mngapp.data.tables.meters;

import java.sql.Date;

import androidx.room.ColumnInfo;

public class LastReadingWithDate {

    @ColumnInfo(name = "lastMeterReading")
    private long lastMeterReading;

    @ColumnInfo(name = "date")
    private Date date;

    public long getLastMeterReading() {
        return lastMeterReading;
    }

    public void setLastMeterReading(long lastMeterReading) {
        this.lastMeterReading = lastMeterReading;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
