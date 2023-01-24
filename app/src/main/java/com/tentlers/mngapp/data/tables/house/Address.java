package com.tentlers.mngapp.data.tables.house;

import androidx.room.ColumnInfo;

public class Address {

    @ColumnInfo(defaultValue = "NULL")
    public int houseNo;

    @ColumnInfo(defaultValue = "NULL")
    public String city;

    @ColumnInfo(defaultValue = "Null")
    public String country;

    @ColumnInfo(defaultValue = "NULL")
    public String postalcode;

    @ColumnInfo(defaultValue = "NULL")
    public String locality;

    public int getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(int houseNo) {
        this.houseNo = houseNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override
    public String toString() {
        if (locality != null) {
            return "Address: " + postalcode + " : " + locality;
        } else return "Address: " + postalcode + " : " + city;
    }
}
