package com.tentlers.mngapp.data.tables.meters;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class AllMeters {
    /* TODO: to view all meters data.*/

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    public long meterId;

    @ColumnInfo
    private long meterNo;

    @ColumnInfo
    public boolean isOnlyHouse;
    @ColumnInfo
    public Date createdate;
    @ColumnInfo
    private Date attachDate;

    @Ignore
    public long roomId;

    @Ignore
    public long houseId;

    @Ignore
    public boolean isSystemdecide;

    @Ignore
    public long lastReading;

    @Ignore
    private MeterEditType choosemMeter;

    public long getMeterId() {
        return meterId;
    }

    public void setMeterId(long meterId) {
        this.meterId = meterId;
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

    public long getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(long meterNo) {
        this.meterNo = meterNo;
    }

    public Date getAttachDate() {
        return attachDate;
    }

    public void setAttachDate(Date attachDate) {
        this.attachDate = attachDate;
    }

    public MeterEditType getChoosemMeter() {
        return choosemMeter;
    }

    public void setChoosemMeter(MeterEditType choosemMeter) {
        this.choosemMeter = choosemMeter;
    }
}
