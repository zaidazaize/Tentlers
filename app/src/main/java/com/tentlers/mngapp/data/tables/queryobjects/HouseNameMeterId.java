package com.tentlers.mngapp.data.tables.queryobjects;

import androidx.room.ColumnInfo;

public class HouseNameMeterId {
    @ColumnInfo(name = "houseName")
    public String housename;

    @ColumnInfo(name = "meterid")
    public long meterId;
}
