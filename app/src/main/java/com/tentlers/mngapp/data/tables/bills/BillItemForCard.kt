package com.tentlers.mngapp.data.tables.bills

import androidx.room.ColumnInfo
import androidx.room.Ignore
import java.util.Date

/* this is used to handle all the date used in the each card of bill.*/
class BillItemForCard {
    @ColumnInfo(name = "billId")
    var billId: Long = 0

    @ColumnInfo(name = "createDate")
    var createDate: Date? = null

    @ColumnInfo(name = "isBillPaid")
    var isBillPaid = false

    @ColumnInfo(name = "electricCost")
    var electricCost = 0f

    /*TODO: change electric cost to electric charges.*/
    @ColumnInfo(name = "monthlyCharge")
    var monthlyCharge = 0f

    @ColumnInfo(name = "additionalCharge")
    var additionalCharge = 0f

    @ColumnInfo(name = "totalAmt")
    var totalAmt = 0f

    @ColumnInfo(name = "tenantId")
    var tenantId: Long = 0

    @ColumnInfo(name = "tenantName")
    var tenantName: String? = null

    @ColumnInfo(name = "houseName")
    var houseName: String? = null

    @ColumnInfo(name = "roomName")
    var roomName: String? = null

    @JvmField
    @Ignore
    var amtDescState = false

    companion object {
        const val VIEW_MORE = true
        const val VIEW_LESS = false
    }
}