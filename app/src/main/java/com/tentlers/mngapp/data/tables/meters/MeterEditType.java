package com.tentlers.mngapp.data.tables.meters;

public class MeterEditType extends GetLastMeterReading {
    public MeterEditType() {
        updatedMeter = new AllMeters();
    }

    public long meterId;
    /*for managing the edit of meter.this class transfers the data fo the unit to wich meter is attached*/
    private int meterEntrytype;
    public static final int ENTRY_OLD = 10;
    public static final int ENTRY_NEW = 11;

    private int entryFor;
    public static final int ENTRY_HOUSE = 12;
    public static final int ENTRY_ROOM = 13;

    private String houseName;
    private String roomName;
    private long meterNo;/*recquired only for old meters*/
    public boolean isMeterSame;

    private AllMeters updatedMeter;

    public MeterEditType setForOldMeter(int entryFor, String name, long meterId, long meterNo) {
        this.meterEntrytype = ENTRY_OLD;
        this.entryFor = entryFor;
        switch (entryFor) {
            case ENTRY_HOUSE:
                this.houseName = name;
                break;
            case ENTRY_ROOM:
                this.roomName = name;
                break;
        }
        this.updatedMeter.setMeterId(meterId);
        this.meterNo = meterNo;
        return this;
    }

    public MeterEditType setForNewMeter(int entryFor, long entryid, String name) {
        this.meterEntrytype = ENTRY_NEW;
        this.entryFor = entryFor;
        switch (entryFor) {
            case ENTRY_HOUSE:
                this.houseName = name;
                this.houseId = entryid;
                break;
            case ENTRY_ROOM:
                this.roomId = entryid;
                this.roomName = name;
                break;
        }

        return this;
    }

    @Deprecated
    public MeterEditType setMeterEntrytype(int entrytype, int entryFor, long entryid, String name) {
        meterEntrytype = entrytype;
        this.entryFor = entryFor;
        switch (entryFor) {
            case ENTRY_HOUSE:
                this.houseName = name;
                this.houseId = entryid;
                break;
            case ENTRY_ROOM:
                this.roomId = entryid;
                this.roomName = name;
                break;
        }
        return this;
    }

    public int getMeterEntrytype() {
        return meterEntrytype;
    }

    public int getEntryFor() {
        return entryFor;
    }

    public String getHouseName() {
        return houseName;
    }

    public String getRoomName() {
        return roomName;
    }

    public AllMeters getUpdatedMeter() {
        return updatedMeter;
    }

    public long getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(long meterNo) {
        this.meterNo = meterNo;
    }

    /*to upadate the meter reading*/
    public AllMetersData getReadingObj() {
        AllMetersData allMetersData = new AllMetersData();
        allMetersData.setMeterId(updatedMeter.meterId);
        allMetersData.setLastMeterReading(updatedMeter.lastReading);
        allMetersData.setDate(updatedMeter.getCreatedate());
        allMetersData.setReadingState(AllMetersData.CREATE);
        return allMetersData;
    }
}
