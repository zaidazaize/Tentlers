package com.tentlers.mngapp.data.tables.tenants;

import android.content.Context;
import android.net.Uri;

import com.tentlers.mngapp.R;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;

import java.io.File;
import java.util.Date;
import java.util.Formatter;
import java.util.jar.Attributes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class TenantsPersonal {
    public static final String TABLE_NAME = "TenantsPersonal";
    public static final String CREATE_DATE = "createDate";
    public static final String UNPAID_AMOUNT = "unpaidAmt";
    public static final String MFIXED_CHARGES = "mFixedCharges";
    public static final String TOTAL_BILLS = "totalBills";
    public static final String IS_ROOM_ALLOTTED = "isRoomAlloted";
    public static final String TENANT_ID = "tenantId";
    public static final String TENANT_NAME = "tenantName";

    @Ignore
    public static final int NOGENDER = 0;
    @Ignore
    public static final int MALE = 100;
    @Ignore
    public static final int FEMALE = 101;
    @Ignore
    public static final int OTHER = 102;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tenantId")
    public long tenantId;
    @ColumnInfo(name = "tenantName")
    private String tenantName;
    @ColumnInfo(defaultValue = "NULL")
    public long houseId;
    @ColumnInfo(defaultValue = "NULL")
    public long roomId;
    @ColumnInfo(defaultValue = "false")
    public boolean isRoomAlloted;
    @ColumnInfo(name = "createDate")
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
    public double mFixedCharges;

    public TenantsPersonal() {
        allMetersData = new AllMetersData();
    }

    @ColumnInfo
    public int totalBills;
    @ColumnInfo
    public int paidBills;
    @ColumnInfo
    private double unpaidAmt;

    @ColumnInfo(defaultValue = "NULL")
    public String imageName;
    @Ignore
    public File tenantPhotoFile;

    @Ignore
    public Uri tenantPhotoUri;

    public String getImageName() {
        return imageName;
    }

    public void setHouseId(long houseId) {
        this.houseId = houseId;
    }

    public void setRoomId(long roomId) {
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

    public double getUnpaidAmt() {
        return unpaidAmt;
    }

    public void setUnpaidAmt(double unpaidAmt) {
        this.unpaidAmt = unpaidAmt;
    }
}

