package com.tentlers.mngapp.data.tables.tenants;

import android.content.Context;
import android.net.Uri;

import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;

import java.io.File;
import java.util.Date;
import java.util.Formatter;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class TenantsPersonal {
    @ColumnInfo(name = "tenantName")
    private String tenantName;
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
    public File tenantPhotoFile;


    @Ignore
    public Uri tenantPhotoUri;

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

    public void setAge(int age) {
        this.age = age;

    }

    public String getGender(Context context) {
        switch (this.gender) {
            case TenantsPersonal.MALE:
                return context.getString(R.string.male);

            case TenantsPersonal.FEMALE:
                return context.getString(R.string.female);

            case TenantsPersonal.OTHER:
                return context.getString(R.string.other);
            default:
                return context.getString(R.string.not_specified);
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

    public File getTenantPhotoFile() {
        return tenantPhotoFile;
    }

    public void setTenantPhotoFile(File tenantPhotoFile) {
        this.tenantPhotoFile = tenantPhotoFile;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public boolean isElectricChargeEnabled() {
        return meterPay || nonMeterPay;
    }

    @Ignore
    public static String getTenantDate(java.util.Date createDate) {
        Formatter formatter = new Formatter();
        return formatter.format("%td %th, %tY", createDate, createDate, createDate).toString();
    }

    public Uri getTenantPhotoUri() {
        return tenantPhotoUri;
    }

    public void setTenantPhotoUri(Uri tenantPhotoUri) {
        this.tenantPhotoUri = tenantPhotoUri;
    }
}

