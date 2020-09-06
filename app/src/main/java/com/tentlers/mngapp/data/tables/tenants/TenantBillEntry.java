package com.tentlers.mngapp.data.tables.tenants;

import androidx.room.ColumnInfo;

public class TenantBillEntry {

    @ColumnInfo(name = "roomId")
    public int roomId;

    @ColumnInfo(name = "meterPay")
    public boolean meterPay;

    @ColumnInfo(name = "nonMeterPay")
    public boolean nonMeterPay;

    @ColumnInfo(name = "mFixedCharges")
    public float mFixedCharges;
}
