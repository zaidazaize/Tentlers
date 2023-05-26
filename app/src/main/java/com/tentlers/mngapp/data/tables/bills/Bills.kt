package com.tentlers.mngapp.data.tables.bills

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.tentlers.mngapp.data.tables.meters.AllMetersData
import java.sql.Date
import java.util.Formatter

@Entity
class Bills {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var billId: Long = 0

    @ColumnInfo
    var tenantId: Long = 0

    @ColumnInfo
    var initialMeterR: Long = 0

    @ColumnInfo
    var endMeterR: Long = 0
        set(endMeterR) {
            field = endMeterR
            metersData.lastMeterReading = endMeterR
            isMeterPay = true
        }

    @ColumnInfo
    var createDate: Date? = null

    @ColumnInfo
    var billPaymentDate: Date? = null

    @ColumnInfo
    var isBillPaid = false

    @ColumnInfo
    var electricCost = 0f

    @ColumnInfo
    var monthlyCharge = 0.0

    @ColumnInfo
    var additionalCharge = 0f

    @ColumnInfo
    var perUnitCost = 0f

    @ColumnInfo
    var manuallyEnteredElectricCost = 0f
        set(manuallyEnteredElectricCost) {
            field = manuallyEnteredElectricCost
            isMeterPay = false
        }

    @ColumnInfo
    var totalAmt = 0.0
        get() = monthlyCharge + additionalCharge + meteredElectricityCost + manuallyEnteredElectricCost

    @get:Ignore
    @Ignore
    val metersData: AllMetersData

    @Ignore
    var isMeterPay /* to enter data in meter table if it is true.*/ = false

    init {
        metersData = AllMetersData()
    }

    fun setCreateDate() {
        createDate = Date(System.currentTimeMillis())
        metersData.date = createDate
    }

    fun setTotalAmt() {
        totalAmt = totalAmt
    }

    @get:Ignore
    val meteredElectricityCost: Float
        get() = (endMeterR - initialMeterR) * perUnitCost

    companion object {
        const val CREATE_DATE = "createDate"
        const val TOTAL_AMT = "totalAmt"

        @JvmStatic
        @Ignore
        fun getBillDate(createDate: java.util.Date?): String {
            val formatter = Formatter()
            return formatter.format(
                "%tr , %ta, %td %tb, %tY",
                createDate,
                createDate,
                createDate,
                createDate,
                createDate
            ).toString()
        }
    }
}