package com.example.easelife.data.tables.meters;

public class GetLastMeterReading {
    public boolean isHouseIdForhouseMeter;
    public boolean isRoomId;
    public boolean isMeterid;

    public int noOfReadings = 1;
    public int houseId;
    public int roomId;
    public long meterId;

    public GetLastMeterReading setHouseIdForhouseMeter(int houseIdForhouseMeter) {
        this.houseId = houseIdForhouseMeter;
        isHouseIdForhouseMeter = true;
        return this;
    }

    public GetLastMeterReading setRoomId(int roomId) {
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
