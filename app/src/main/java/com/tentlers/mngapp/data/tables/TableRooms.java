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
    private AllMetersData allMetersData;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private int roomId;

    @ColumnInfo
    private int houseId;

    @ColumnInfo
    private int roomNo;

    @ColumnInfo(defaultValue = "NULL")
    private String roomName;

    @ColumnInfo
    private boolean ocupiedStatus;

    @ColumnInfo
    private long meterId;

    @ColumnInfo
    private boolean isMeterEnabled;

    @Ignore
    private boolean isSystemDeside;

    @ColumnInfo
    private Date date;

    @ColumnInfo(defaultValue = "NULL")
    private Date tenantEntryDate;

    @ColumnInfo(defaultValue = "NULL")
    private int tenantId;

    @ColumnInfo()
    private String tenantName;

    public TableRooms() {
        setAllMetersData(new AllMetersData());
    }

    public void setDate(Date date) {
        this.date = date;
        getAllMetersData().setDate(date);
    }

    @Ignore
    public AllMetersData getAllMetersData() {
        return this.allMetersData;
    }

    public Date getDate() {
        return date;
    }

    public void setAllMetersData(AllMetersData allMetersData) {
        this.allMetersData = allMetersData;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean isOcupiedStatus() {
        return ocupiedStatus;
    }

    public void setOcupiedStatus(boolean ocupiedStatus) {
        this.ocupiedStatus = ocupiedStatus;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public long getMeterId() {
        return meterId;
    }

    public void setMeterId(long meterId) {
        this.meterId = meterId;
        getAllMetersData().setMeterId(meterId);
    }

    public String getMeterIdString() {
        return String.valueOf(meterId);
    }

    public boolean isMeterEnabled() {
        return isMeterEnabled;
    }

    public void setMeterEnabled(boolean meterEnabled) {
        isMeterEnabled = meterEnabled;
    }

    public boolean isSystemDeside() {
        return isSystemDeside;
    }

    public void setSystemDeside(boolean systemDeside) {
        isSystemDeside = systemDeside;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public Date getTenantEntryDate() {
        return tenantEntryDate;
    }

    public void setTenantEntryDate(Date tenantEntryDate) {
        this.tenantEntryDate = tenantEntryDate;
    }
}
