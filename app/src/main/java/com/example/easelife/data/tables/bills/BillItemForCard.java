package com.example.easelife.data.tables.bills;


import java.sql.Date;

import androidx.room.ColumnInfo;

/* this is used to handle all the date used in the each card of bill.*/
public class BillItemForCard {

    @ColumnInfo(name = "billId")
    public long billId;

    @ColumnInfo(name = "createDate")
    public Date createDate;

    @ColumnInfo(name = "isBillPaid")
    public boolean isBillPaid;

    @ColumnInfo(name = "electricCost")
    public float electricCost;

    @ColumnInfo(name = "monthlycharge")
    public float monthlycharge;

    @ColumnInfo(name = "additionalcharge")
    public float additionalcharge;

    @ColumnInfo(name = "totalAmt")
    public float totalAmt;

    @ColumnInfo(name = "tenantName")
    public String tenantName;

    @ColumnInfo(name = "houseName")
    public String houseName;

    @ColumnInfo(name = "roomName")
    public String roomName;
}
