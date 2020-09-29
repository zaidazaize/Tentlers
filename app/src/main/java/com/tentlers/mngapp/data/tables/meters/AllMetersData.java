package com.tentlers.mngapp.data.tables.meters;

import android.view.Gravity;

import com.tentlers.mngapp.R;

import java.sql.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class AllMetersData {
    @Ignore
    public static final String TENANT_ENTRY_STRING = "New Tenant Added";
    @Ignore
    public static final String TENANT_EXIT_STRING = "Tenant Left";

    @ColumnInfo(defaultValue = "NULL")
    private long meterId;
    @Ignore
    public static final String CREATE_METER_STRING = "Meter Created";
    @Ignore
    public static final String BILLED_STRING = "Bill Id";
    @Ignore
    public static final String BILLING_STATUS = "From Billing";
    @Ignore
    public static final int CREATE = 100;
    @Ignore
    public static final int BILLED = 101;
    @Ignore
    public static final int TENANT_ENTRY = 102;
    @Ignore
    public static final int TENANT_EXIT = 103;
    @PrimaryKey(autoGenerate = true)
    private long entryId;
    @ColumnInfo
    private Date date;
    @ColumnInfo(defaultValue = "NULL")
    private int readingState;
    @ColumnInfo
    private long billId;
    @ColumnInfo(defaultValue = "NULL")
    private long lastMeterReading;
    @Ignore
    private int Roomid;/* for entering the meter reading while creating the bill.*/
    @Ignore
    private int colorState;
    @Ignore
    private int textStyle;
    @Ignore
    private int orientationState;
    @Ignore
    private String headingText;

    public String getHeadingText() {
        return headingText;
    }

    public int getOrientationState() {
        return orientationState;
    }

    public int getTextStyle() {
        return textStyle;
    }

    public int getReadingState() {
        return readingState;
    }

    /*also sets the color values for different states.
     * Orientation states and the textStyles for the different values.*/
    public void setReadingState(int readingState) {
        this.readingState = readingState;
        switch (readingState) {
            case CREATE:
                colorState = R.color.reading_state_CREATE;
                textStyle = R.style.TextAppearance_MaterialComponents_Subtitle1;
                orientationState = Gravity.CENTER_HORIZONTAL;
                headingText = CREATE_METER_STRING;
                break;
            case BILLED:
                colorState = R.color.reading_state_BILLED;
                textStyle = R.style.TextAppearance_MaterialComponents_Subtitle2;
                orientationState = Gravity.START;
                headingText = BILLED_STRING;
                break;
            case TENANT_ENTRY:
                colorState = R.color.reading_state_TENANT_ENTRY;
                textStyle = R.style.TextAppearance_MaterialComponents_Subtitle1;
                orientationState = Gravity.END;
                headingText = TENANT_ENTRY_STRING;
                break;
            case TENANT_EXIT:
                colorState = R.color.reading_state_TENANT_EXIT;
                textStyle = R.style.TextAppearance_MaterialComponents_Subtitle1;
                orientationState = Gravity.END;
                headingText = TENANT_EXIT_STRING;
                break;
        }
    }

    public void setOnlyReadingState(int state) {
        this.readingState = state;
    }

    public void setLastMeterReadingFromString(String lastMeterReading) {
        if (!lastMeterReading.isEmpty()) {
            this.setLastMeterReading(Long.parseLong(lastMeterReading));
        } else this.setLastMeterReading(0);
    }

    public int getColorState() {
        return colorState;
    }

    public long getEntryId() {
        return entryId;
    }

    public long getMeterId() {
        return meterId;
    }

    public void setMeterId(long meterId) {
        this.meterId = meterId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getLastMeterReading() {
        return lastMeterReading;
    }

    public void setLastMeterReading(long lastMeterReading) {
        this.lastMeterReading = lastMeterReading;
    }

    public int getRoomid() {
        return Roomid;
    }

    public void setRoomid(int roomid) {
        Roomid = roomid;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }


}
