package com.example.easelife.data.tables.bills;

import com.example.easelife.data.tables.meters.AllMetersData;

import java.sql.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Bills {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo()
    public long billId;

    @ColumnInfo
    public int tenantId;

    @ColumnInfo
    public long initialMeterR;

    public void setEndMeterR(long endMeterR) {
        this.endMeterR = endMeterR;
        metersData.lastMeterReading = endMeterR;
        ismeterPay = true;
    }

    @ColumnInfo
    public long endMeterR;

    public void setCreateDate() {
        this.createDate = new Date(System.currentTimeMillis());
        metersData.date = createDate;
    }

    @ColumnInfo
    public Date createDate;

    @ColumnInfo
    public Date billpaymentDate;

    @ColumnInfo
    public boolean isBillPaid;

    @ColumnInfo
    public float electricCost;

    @ColumnInfo
    public float monthlycharge;

    @ColumnInfo
    public float additionalcharge;

    @ColumnInfo
    public float perUnitcoat;

    public void setManuallyEnteredElectricCost(float manuallyEnteredElectricCost) {
        this.manuallyEnteredElectricCost = manuallyEnteredElectricCost;
        ismeterPay = false;
    }

    @ColumnInfo
    public float manuallyEnteredElectricCost;

    public void setTotalAmt() {
        this.totalAmt = getTotalAmt();
    }

    public float getTotalAmt() {
        return monthlycharge + additionalcharge + getMeteredElectricityCost() + manuallyEnteredElectricCost;
    }

    @ColumnInfo
    public float totalAmt;

    @Ignore
    public AllMetersData getMetersData() {
        return metersData;
    }

    @Ignore
    public AllMetersData metersData = new AllMetersData();

    @Ignore
    public boolean ismeterPay;/* to enter data in meter table if it is true.*/

    @Ignore
    public float getMeteredElectricityCost() {
        return (endMeterR - initialMeterR) * perUnitcoat;
    }


}
