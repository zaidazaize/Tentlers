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

    var tenantId: Long = 0
    var roomId = 0
    var isBillSpecificTenant = false
    var isBillSpecificRoom = false
    fun setBillNormalPaid(billNormalPaid: Boolean): BillEntryTypeObject {
        isBillNormalPaid = billNormalPaid
        return this
    }

    var isBillNormalPaid = false
}