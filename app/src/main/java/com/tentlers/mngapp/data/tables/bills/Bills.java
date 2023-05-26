package com.tentlers.mngapp.data.tables.bills;

import com.tentlers.mngapp.data.tables.meters.AllMetersData;

import java.sql.Date;
import java.util.Formatter;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Bills {
    public static final String CREATE_DATE = "createDate";
    public static final String TOTAL_AMT = "totalAmt";

    public Bills() {
        metersData = new AllMetersData();
    }
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo()
    private long billId;

    @ColumnInfo
    private long tenantId;

    @ColumnInfo
    private long initialMeterR;
    @ColumnInfo
    private long endMeterR;
    @ColumnInfo
    private Date createDate;
    @ColumnInfo
    private Date billPaymentDate;
    @ColumnInfo
    private boolean isBillPaid;
    @ColumnInfo
    private float electricCost;
    @ColumnInfo
    private double monthlyCharge;
    @ColumnInfo
    private float additionalCharge;
    @ColumnInfo
    private float perUnitCost;
    @ColumnInfo
    private float manuallyEnteredElectricCost;
    @ColumnInfo
    private double totalAmt;
    @Ignore
    private final AllMetersData metersData;

    @Ignore
    private boolean isMeterPay;/* to enter data in meter table if it is true.*/

    public void setEndMeterR(long endMeterR) {
        this.endMeterR = endMeterR;
        getMetersData().setLastMeterReading(endMeterR);
        setMeterPay(true);
    }

    public void setCreateDate() {
        this.setCreateDate(new Date(System.currentTimeMillis()));
        getMetersData().setDate(getCreateDate());
    }

    public void setManuallyEnteredElectricCost(float manuallyEnteredElectricCost) {
        this.manuallyEnteredElectricCost = manuallyEnteredElectricCost;
        setMeterPay(false);
    }

    public void setTotalAmt() {
        this.setTotalAmt(getTotalAmt());
    }

    public double getTotalAmt() {
        return getMonthlyCharge() + getAdditionalCharge() + getMeteredElectricityCost() + getManuallyEnteredElectricCost();
    }

    @Ignore
    public AllMetersData getMetersData() {
        return metersData;
    }

    @Ignore
    public float getMeteredElectricityCost() {
        return (getEndMeterR() - getInitialMeterR()) * getPerUnitCost();
    }

    @Ignore
    public static String getBillDate(java.util.Date createDate) {
        Formatter formatter = new Formatter();
        return formatter.format("%tr , %ta, %td %tb, %tY", createDate, createDate, createDate, createDate, createDate).toString();
    }


    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public long getInitialMeterR() {
        return initialMeterR;
    }

    public void setInitialMeterR(long initialMeterR) {
        this.initialMeterR = initialMeterR;
    }

    public long getEndMeterR() {
        return endMeterR;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getBillPaymentDate() {
        return billPaymentDate;
    }

    public void setBillPaymentDate(Date billPaymentDate) {
        this.billPaymentDate = billPaymentDate;
    }

    public boolean isBillPaid() {
        return isBillPaid;
    }

    public void setBillPaid(boolean billPaid) {
        isBillPaid = billPaid;
    }

    public float getElectricCost() {
        return electricCost;
    }

    public void setElectricCost(float electricCost) {
        this.electricCost = electricCost;
    }

    public double getMonthlyCharge() {
        return monthlyCharge;
    }

    public void setMonthlyCharge(double monthlyCharge) {
        this.monthlyCharge = monthlyCharge;
    }

    public float getAdditionalCharge() {
        return additionalCharge;
    }

    public void setAdditionalCharge(float additionalCharge) {
        this.additionalCharge = additionalCharge;
    }

    public float getPerUnitCost() {
        return perUnitCost;
    }

    public void setPerUnitCost(float perUnitCost) {
        this.perUnitCost = perUnitCost;
    }

    public float getManuallyEnteredElectricCost() {
        return manuallyEnteredElectricCost;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public boolean isMeterPay() {
        return isMeterPay;
    }

    public void setMeterPay(boolean meterPay) {
        isMeterPay = meterPay;
    }
}
