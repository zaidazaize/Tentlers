package com.tentlers.mngapp.data.tables.tenants;

import androidx.room.ColumnInfo;

public class TenantNameHouseRoom {
    @ColumnInfo(name = "tenantId")
    public int tenantId;

    @ColumnInfo(name = "tenantName")
    public String tenantName;

    @ColumnInfo(name = "houseName")
    public String houseName;

    @ColumnInfo(name = "roomName")
    public String roomName;
}
