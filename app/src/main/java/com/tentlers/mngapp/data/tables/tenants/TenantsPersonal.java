package com.tentlers.mngapp.data.tables.tenants;

import com.tentlers.mngapp.data.tables.meters.AllMetersData;

import java.io.File;
import java.sql.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class TenantsPersonal {
    @ColumnInfo(name = "tenantName")
    public String tenantName;
    @Ignore
    public static final int NOGENDER = 0;
    @Ignore
    public static final int MALE = 100;
    @Ignore
    public static final int FEMALE = 101;
    @Ignore
    public static final int OTHER = 102;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo()
    public int tenantId;
    @ColumnInfo(defaultValue = "NULL")
    public int houseId;
    @ColumnInfo(defaultValue = "NULL")
    public int roomId;
    @ColumnInfo(defaultValue = "false")
    public boolean isRoomAlloted;
    @ColumnInfo
    public Date createDate;
    /*
     * Tenant personal information
     */
    @ColumnInfo()
    public boolean isPersonalInfo;
    @Ignore
    public AllMetersData allMetersData;
    @ColumnInfo(defaultValue = "NULL")
    public int age;
    @ColumnInfo(defaultValue = "0")
    public int gender;
    @ColumnInfo
    public long familyMembers;
    @ColumnInfo
    public long phoneNumber;
    /*
     * Payment Scheme
     * for electric charges.
     */
    @ColumnInfo(defaultValue = "false")
    public boolean meterPay;
    @ColumnInfo(defaultValue = "false")
    public boolean nonMeterPay;
    @ColumnInfo(defaultValue = "0")
    public float mFixedCharges;

    public TenantsPersonal() {
        allMetersData = new AllMetersData();
    }

    @ColumnInfo
    public int totalBills;
    @ColumnInfo
    public int paidBills;

    @ColumnInfo(defaultValue = "NULL")
    public String imageName;
    @Ignore
    public File tenantPhoto;

    public String getImageName() {
        return imageName;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate() {
        this.createDate = new Date(System.currentTimeMillis());
        allMetersData.setDate(createDate);
    }

    public void setIsPersonalInfo(boolean personalInfo) {
        isPersonalInfo = personalInfo;
    }

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

    public void setMeterPay(boolean meterPay) {
        this.meterPay = meterPay;
        this.nonMeterPay = !meterPay;
    }

    public void setNonMeterPay(boolean nonMeterPay) {
        this.nonMeterPay = nonMeterPay;
        this.meterPay = !nonMeterPay;
    }

    @Ignore
    public void setDiscardAllMeterPay() {
        nonMeterPay = false;
        meterPay = false;
    }

    @Ignore
    public AllMetersData getAllMetersData() {
        return allMetersData;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public File getTenantPhoto() {
        return tenantPhoto;
    }

    public File setTenantPhoto(File tenantPhoto) {
        this.tenantPhoto = tenantPhoto;
        return this.tenantPhoto;
    }

}
