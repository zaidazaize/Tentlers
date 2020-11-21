package com.tentlers.mngapp.data.tables.bills;


import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

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
    /*TODO: change electric cost to electric charges.*/

    @ColumnInfo(name = "monthlycharge")
    public float monthlycharge;

    @ColumnInfo(name = "additionalcharge")
    public float additionalcharge;

    @ColumnInfo(name = "totalAmt")
    public float totalAmt;

    @ColumnInfo(name = "tenantId")
    public long tenantId;

    @ColumnInfo(name = "tenantName")
    public String tenantName;

    @ColumnInfo(name = "houseName")
    public String houseName;

    @ColumnInfo(name = "roomName")
    public String roomName;

    public static final boolean VIEW_MORE = true;
    public static final boolean VIEW_LESS = false;
    @Ignore
    public boolean amtDescState;


}
