package com.example.easelife.data.tables;

import java.sql.Date;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class TableHouse {

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    public int houseId;

    @ColumnInfo(defaultValue = "NULL")
    public String houseName;

    @ColumnInfo()
    public Date date;

    @ColumnInfo(defaultValue = "0")
    public int noOfRooms;

    @ColumnInfo(defaultValue = "0")
    public int emptyrooms;

    @ColumnInfo(defaultValue = "false")
    public boolean includeMeter;

    @ColumnInfo()
    public long meterid;


    @Embedded
    public Address address;

    @Ignore
    public boolean ismetersystemgenerated;

    @Ignore
    public boolean firstEntry;


    public int getHouseId() {
        return houseId;
    }

    public String getHouseName() {
        return houseName;
    }

    public Date getDate() {
        return date;
    }

    public int getNoOfRooms() {
        return noOfRooms;
    }

    public int getEmptyrooms() {
        return emptyrooms;
    }

    public long getMeterid() {
        return meterid;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNoOfRooms(int noOfRooms) {
        this.noOfRooms = noOfRooms;
    }

    public void setEmptyrooms(int emptyrooms) {
        this.emptyrooms = emptyrooms;
    }

    public void setMeterid(long meterid) {
        this.meterid = meterid;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isIncludeMeter() {
        return includeMeter;
    }

    public void setIncludeMeter(boolean includeMeter) {
        this.includeMeter = includeMeter;
    }

    @Override
    public String toString() {
        return "TableHouse{" +
                "houseId=" + houseId +
                ", houseName='" + houseName  +
                ", date='" + date  +
                ", noOfRooms=" + noOfRooms +
                ", emptyrooms=" + emptyrooms +
                ", includeMeter=" + includeMeter +
                ", meterid=" + meterid +
                ", address=" + getAddress() +
                "}";
    }

    private String getAddress() {
        if (address != null) {
            return address.toString();
        }else return "slkdjflsjf";
    }
}
