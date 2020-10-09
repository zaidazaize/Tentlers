package com.tentlers.mngapp.data.tables.tenants;

import androidx.room.ColumnInfo;

public class TenantBillEntry {
    /*this object is use to update the ui with billing data */

    @ColumnInfo(name = "roomId")
    public int roomId;

    @ColumnInfo(name = "tenantName")
    public String tenantName;

    @ColumnInfo(name = "meterPay")
    public boolean meterPay;

    @ColumnInfo(name = "nonMeterPay")
    public boolean nonMeterPay;

    @ColumnInfo(name = "mFixedCharges")
    public float mFixedCharges;

    @ColumnInfo(name = "houseName")
    public String houseName;

    @ColumnInfo(name = "roomName")
    public String roomName;
}
