package com.tentlers.mngapp.data.tables.bills

class BillEntryTypeObject {
    fun setTenantId(tenantId: Long): BillEntryTypeObject {
        this.tenantId = tenantId
        isBillSpecificTenant = true
        return this
    }

    fun setRoomId(roomId: Int): BillEntryTypeObject {
        this.roomId = roomId
        isBillSpecificRoom = true
        return this
    }

    @JvmField
    var tenantId: Long = 0
    @JvmField
    var roomId = 0
    @JvmField
    var isBillSpecificTenant = false
    @JvmField
    var isBillSpecificRoom = false
    fun setBillNormalPaid(billNormalPaid: Boolean): BillEntryTypeObject {
        isBillNormalPaid = billNormalPaid
        return this
    }

    @JvmField
    var isBillNormalPaid = false
}