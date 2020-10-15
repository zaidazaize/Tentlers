package com.tentlers.mngapp.data.tables.meters;

/*this object is used to generate the querry and fetch the meter reading.*/
public class GetLastMeterReading {
    public GetLastMeterReading() {
        noOfReadings = 1;
    }

    public boolean isHouseIdForhouseMeter;
    public boolean isRoomId;
    public boolean isMeterid;

    public int noOfReadings;
    public long tenantid;
    public long houseId;
    public long roomId;
    public long meterId;

    public GetLastMeterReading setHouseIdForhouseMeter(int houseIdForhouseMeter) {
        this.houseId = houseIdForhouseMeter;
        isHouseIdForhouseMeter = true;
        return this;
    }

    public GetLastMeterReading setRoomId(long roomId) {
        this.roomId = roomId;
        isRoomId = true;
        return this;
    }

    public GetLastMeterReading setMeterId(long meterId) {
        this.meterId = meterId;
        isMeterid = true;
        return this;
    }

}
