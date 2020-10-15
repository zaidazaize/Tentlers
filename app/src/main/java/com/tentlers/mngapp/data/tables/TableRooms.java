package com.tentlers.mngapp.data.tables;

import com.tentlers.mngapp.data.tables.meters.AllMeters;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;

import java.util.Date;
import java.util.Formatter;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class TableRooms {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private long roomId;

    @ColumnInfo
    private long houseId;

    @ColumnInfo
    private int roomNo;

    @ColumnInfo(defaultValue = "NULL")
    private String roomName;

    @ColumnInfo
    private boolean ocupiedStatus;

    @ColumnInfo
    private long meterId;
    @Ignore
    public long meterNo;

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
        getAllMeters().setCreatedate(date);
    }

    @Ignore
    private AllMetersData allMetersData;

    @Ignore/*now this varialble stores data of for entering the new meter*/
    private AllMeters allMeters;


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

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getHouseId() {
        return houseId;
    }

    public void setHouseId(long houseId) {
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

    @Ignore
    public static String getRoomDate(java.util.Date createDate) {
        Formatter formatter = new Formatter();
        return formatter.format("%td %th, %tY", createDate, createDate, createDate).toString();
    }

    public AllMeters getAllMeters() {
        if (allMeters == null) {
            this.allMeters = new AllMeters();
        }
        return allMeters;
    }

    public void setAllMeters(AllMeters allMeters) {
        this.allMeters = allMeters;
    }
}
