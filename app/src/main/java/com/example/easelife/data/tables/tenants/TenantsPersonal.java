package com.example.easelife.data.tables.tenants;

import com.example.easelife.data.tables.meters.AllMetersData;
import com.example.easelife.data.tables.rooms.RoomNoNameId;

import java.sql.Date;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class TenantsPersonal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo()
    public int tenantId;

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    @ColumnInfo(defaultValue = "NULL")
    public int houseId;

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @ColumnInfo(defaultValue = "NULL")
    public int roomId;

    @ColumnInfo(defaultValue = "false")
    public boolean isRoomAlloted;

    @ColumnInfo
    public Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate() {
        this.createDate = new Date(System.currentTimeMillis());
        allMetersData.date = createDate;
    }

    public void setIsPersonalInfo(boolean personalInfo) {
        isPersonalInfo = personalInfo;
    }
    /*
     * Tenant personal information
     */
    @ColumnInfo()
    public boolean isPersonalInfo;

    @ColumnInfo()
    public String tenantName;

    @ColumnInfo(defaultValue = "NULL")
    public int age;

    public int getAge() {
        return age;
    }

    public boolean setAge(int age) {
        if (age < 0) {
            return false;
        } else {
            this.age = age;
            return true;
        }
    }

    @ColumnInfo(defaultValue = "0")
    public int gender;

    @Ignore
    public static final int NOGENDER = 0;
    @Ignore
    public static final int MALE = 100;

    @Ignore
    public static final int FEMALE = 101;

    @Ignore
    public static final int OTHER = 102;

    public String getGender(int gender) {
        switch (gender) {
            case TenantsPersonal.MALE:
                return "Male";

            case TenantsPersonal.FEMALE:
                return "Female";

            case TenantsPersonal.OTHER:
                return "Other";
            default:
                return "0";
        }
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @ColumnInfo
    public long familyMembers;

    @ColumnInfo
    public long phoneNumber;

    public void setMeterPay(boolean meterPay) {
        this.meterPay = meterPay;
        this.nonMeterPay = !meterPay;
    }

    /*
     * Payment Scheme
     * for electric charges.
     */
    @ColumnInfo(defaultValue = "false")
    public boolean meterPay;

    public void setNonMeterPay(boolean nonMeterPay) {
        this.nonMeterPay = nonMeterPay;
        this.meterPay = !nonMeterPay;
    }

    @ColumnInfo(defaultValue = "false")
    public boolean nonMeterPay;

    @Ignore
    public void setDiscardAllMeterPay() {
        nonMeterPay = false;
        meterPay = false;
    }

    @ColumnInfo(defaultValue = "0")
    public float mFixedCharges;

    @Ignore
    public AllMetersData getAllMetersData() {
        return allMetersData;
    }

    @Ignore
    public AllMetersData allMetersData = new AllMetersData();

    @ColumnInfo
    public int totalBills;

    @ColumnInfo
    public int paidBills;

}
