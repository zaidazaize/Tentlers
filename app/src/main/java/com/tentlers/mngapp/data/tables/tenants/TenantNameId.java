package com.tentlers.mngapp.data.tables.tenants;

import androidx.room.ColumnInfo;

public class TenantNameId {

    @ColumnInfo(name = "tenantId")
    public int tenantId;

    @ColumnInfo(name = "tenantName")
    public String tenantName;
}
