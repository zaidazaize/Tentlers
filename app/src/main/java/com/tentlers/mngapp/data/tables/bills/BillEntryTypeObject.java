package com.tentlers.mngapp.data.tables.bills;

public class BillEntryTypeObject {

    public BillEntryTypeObject setTenantId(long tenantId) {
        this.tenantId = tenantId;
        this.isBillSpecificTenant = true;
        return this;
    }

    public BillEntryTypeObject setRoomId(int roomId) {
        this.roomId = roomId;
        this.isBillSpecificRoom = true;
        return this;
    }

    public long tenantId;
    public int roomId;
    public boolean isBillSpecificTenant;
    public boolean isBillSpecificRoom;

    public BillEntryTypeObject setBillNormalPaid(boolean billNormalPaid) {
        isBillNormalPaid = billNormalPaid;
        return this;
    }

    public boolean isBillNormalPaid;
}
