package com.tentlers.mngapp.data.database;

import android.app.Application;
import android.os.AsyncTask;

import com.tentlers.mngapp.data.tables.TableHouse;
import com.tentlers.mngapp.data.tables.TableRooms;
import com.tentlers.mngapp.data.tables.bills.BillItemForCard;
import com.tentlers.mngapp.data.tables.bills.Bills;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.meters.GetLastMeterReading;
import com.tentlers.mngapp.data.tables.meters.LastReadingWithDate;
import com.tentlers.mngapp.data.tables.meters.MetersListObj;
import com.tentlers.mngapp.data.tables.queryobjects.HouseForHomeFragment;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameAndId;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameIdNoRooms;
import com.tentlers.mngapp.data.tables.rooms.RoomForRoomList;
import com.tentlers.mngapp.data.tables.rooms.RoomNoName;
import com.tentlers.mngapp.data.tables.rooms.RoomNoNameId;
import com.tentlers.mngapp.data.tables.tenants.TenantBillEntry;
import com.tentlers.mngapp.data.tables.tenants.TenantNameHouseRoom;
import com.tentlers.mngapp.data.tables.tenants.TenantNameId;
import com.tentlers.mngapp.data.tables.tenants.TenantsPersonal;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class Repository {

    /* This the repository class that handles communication between different data sources
     * And hadling different database thread*/

    private LiveData<List<HouseForHomeFragment>> mAllhousData;
    private HouseDao mdao;

    // Repository constructor
    public Repository(Application application) {

        //Initialise the database pass the applecation context
        // get the dao class from it
        //and then get the all houese live data from it
        DatabaseHouse db = DatabaseHouse.getDatabase(application.getApplicationContext());
        mdao = db.mHouseDao();
        mAllhousData = mdao.getAllHouseForHomeFragment();
    }

    // Return the live data of all rooms.
    public LiveData<List<HouseForHomeFragment>> mgetAllHouseForHomeFragment() {
        return mAllhousData;
    }

    /*return the selected house data object*/
    public LiveData<TableHouse> getHosueFromHouseId(int houseid) {
        return mdao.getHouseFromHouseId(houseid);
    }

    // Meathod to insert new house in database
    /* Also created new rooms in the rooms table*/
    public void insertNewHouse(TableHouse dataTable) {
        new AsyncInsertNewHouse(mdao).execute(dataTable);
    }

    public void updateHouse(TableHouse tableHouse) {
        new AsyncUpdateHouse(mdao).execute(tableHouse);
    }

    public void deleteHouse(TableHouse tableHouse) {
        new AsyncDeleteHouse(mdao).execute(tableHouse);
    }

    /* Get all the readings of a meter.*/
    public LiveData<List<AllMetersData>> getAllMetersReading(long meterId) {
        return mdao.getAllMeterReading(meterId);
    }

    /*Get house name from house id */
    public LiveData<String> getHouseNameFromHouseId(int houseId) {
        return mdao.getHouseNameFromHosueId(houseId);
    }

    /*
     * This is used  for checking the uniqueness of house name and meter id in house Entry fragment
     */
    public LiveData<List<String>> mgetHousenameMeterId() {
        return mdao.gethouseNameMeterId();
    }

    /*
     * getting all houses name for diaplaying in "Rooms fragment" in the spinner
     */
    public LiveData<List<HouseNameIdNoRooms>> getHouseNameId() {
        return mdao.getHouseNameId();
    }

    /*
     * Getting the three rooms for displaying in the "Specific house Fragement
     */
    public LiveData<List<RoomForRoomList>> getThreeRooms(int mhouseId) {
        return mdao.getThreerooms(mhouseId);
    }

    /*
     * The data from this query is use to show all the rooms specific to hosue.
     */
    public LiveData<List<RoomForRoomList>> getAllRooms(int mhouseId) {
        return mdao.getAllRoomsOfHouse(mhouseId);
    }

    /* Quarries for room tables*/

    /*getting all the meter ids for comparison while creating new room*/
    public LiveData<List<Long>> getAllMeterIdOfState(int state) {
        return mdao.getAllMeterIdOfState(state);
    }

    public LiveData<List<RoomNoName>> getRoomNoName(long parentid) {
        return mdao.getroomNoName(parentid);
    }

    /* Get the specific room selected.*/
    public LiveData<TableRooms> getRoomFromRoomId(int roomId) {
        return mdao.getRoomFromRoomId(roomId);
    }

    /*update tenant ocuupied status*/
    public void updateTenantOccupiedStatus(int houseid, int roomid, boolean isOccupied, int tenantId) {
        mdao.upDateTenantOccupiedStatus(houseid, roomid, isOccupied, tenantId);
    }

    /*
     * It inserts new room in the house aswell as updates the no of room in the house tables.
     */
    public void insetNewRoom(TableRooms rooms) {
        new InsertNewRoom(mdao).execute(rooms);
    }

    /*
     * It deletes the room data from the room table as well as updates the no of rooms in the house table
     * for the specific hosue.
     */
    public void deleteRoom(TableRooms rooms) {
        new AsyncDleterRoom(mdao).execute(rooms);
    }


    /* Manipulating room Table*/

    // Updating rooms
    public void updateRoom(TableRooms rooms) {
        new AyncUpdateRoom(mdao).execute(rooms);
    }

    /*
     * For inserting New Tenant.
     */
    public void insertNewTenant(TenantsPersonal tenantsPersonal) {
        new AsyncCreateTenant(mdao).execute(tenantsPersonal);
    }

    /*
     * For Updating Tenant.
     */
    public void updateTenant(TenantsPersonal tenantsPersonal) {
        new AsyncUpdateTenant(mdao).execute(tenantsPersonal);
    }

    /*
     * For Deleting Tenant.
     */
    public void deleteTenant(TenantsPersonal tenantsPersonal) {
        new AsyncDeleteTenant(mdao).execute(tenantsPersonal);
    }

    /*For tenant Entry entry table.*/
    /*
     * For entering house names in the spinner which meets the adecuate set conditions.
     */
    public LiveData<List<HouseNameAndId>> getHouseNameIdTEspinner() {
        return mdao.getHouseNameIdForTEspinner();
    }

    /*
     * entering the room name in the room name spinner.
     */
    public LiveData<List<RoomNoNameId>> getRoomNoNameId(int houseId, boolean isOccupied) {
        return mdao.getroomNoNameId(houseId, isOccupied);
    }

    /*
     * For manipulating TenantPersonal Table.
     */

    /* For modifying the allmetersdata table*/
    public void insertNewMeterReading(AllMetersData metersData) {
        new AsyncInsertNewMeterReading(mdao).execute(metersData);
    }

    /* Based on the data given repository will call the respective dao function to fetch the last meter reading.*/
    public LiveData<LastReadingWithDate> getLastEnteredMeterReading(GetLastMeterReading meterDataObj) {
        if (meterDataObj.isMeterid) {
            return mdao.getLastMeterEntry(meterDataObj.meterId, meterDataObj.noOfReadings);
        } else if (meterDataObj.isRoomId) {
            return mdao.getLastMeterReadingForRoom(meterDataObj.roomId, meterDataObj.noOfReadings);
        } else if (meterDataObj.isHouseIdForhouseMeter) {
            return mdao.getLastMeterReadingForHouse(meterDataObj.houseId, meterDataObj.noOfReadings);
        }
        return null;
    }

    /* Get the date on which meter was created From all meters table.*/
    public LiveData<Date> getMeterCreateDate(long meterid) {
        return mdao.getMeterCreateDate(meterid, AllMetersData.CREATE);
    }

    /* Get all tenant list for tenant fragment*/
    public LiveData<List<TenantNameHouseRoom>> getAllTenantNHR(boolean withRoomAlloted) {
        return mdao.getAllTenantNHR(withRoomAlloted);
    }

    /* Get all data of tenant for specific tenant fragment*/
    public LiveData<TenantsPersonal> getTenantFromId(int teanantId) {
        return mdao.getTenantFromId(teanantId);
    }

    /*Get room name and house name for the specific tenant fragment*/
    public LiveData<MetersListObj> getHouseRoomNameFromRoomId(int roomid) {
        return mdao.getHouseRoomNameFromRoomId(roomid);
    }

    /* for bill entry fragment.*/

    /*get all the tenant name and id*/
    public LiveData<List<TenantNameId>> getAllTenantNameId(boolean isAlloted) {
        return mdao.getAllTenantNameId(isAlloted);
    }

    public LiveData<Integer> getTenantIdFromRoomId(int roomid) {
        return mdao.getTenantIdFromRoomID(roomid);
    }

    /* Get the selected tenant's object.*/
    public LiveData<TenantBillEntry> getSelectedTenantForBill(int tenantid) {
        return mdao.getSelectedTenantForBill(tenantid);
    }

    /* Creating new bills*/
    /* updates the no of bills in the tenant table*/
    /* Inserts meter reading if paid through meter reading.*/
    public void insertNewBills(Bills bills) {
        new AsyncInsertNewBill(mdao).execute(bills);
    }

    //  returns whether alleast any tenant is active or not.
    public LiveData<Boolean> getIsAntActiveTenant(boolean isactivetenant) {
        return mdao.getIsAnyTenantActive(isactivetenant);
    }

    /* gets data for card in bills*/
    public LiveData<List<BillItemForCard>> getAllBillForCard(boolean isBillPaid) {
        return mdao.getAllBillForCard(isBillPaid);
    }

    public LiveData<List<BillItemForCard>> getThreeBillForCard(int roomId) {
        return mdao.getThreeBillForRoom(roomId);
    }

    private static class AsyncDeleteHouse extends AsyncTask<TableHouse, Void, Void> {
        private HouseDao asyncDao;

        public AsyncDeleteHouse(HouseDao mdao) {
            asyncDao = mdao;
        }


        @Override
        protected Void doInBackground(TableHouse... tableHouses) {
            TableHouse house = tableHouses[0];
            if (house.isDeleteHouseByid()) {
                asyncDao.deleteHouseById(house.getHouseId());
            } else asyncDao.deleteHouse(house);

            asyncDao.deleteRoomsOfTheHouse(house.getHouseId());
            asyncDao.deleteTenantsOfTheHouse(house.getHouseId());
            return null;
        }
    }

    private static class InsertNewRoom extends AsyncTask<TableRooms, Void, Void> {
        private final HouseDao mAsyncRoomDao;

        InsertNewRoom(HouseDao dao) {
            mAsyncRoomDao = dao;
        }

        @Override
        protected Void doInBackground(TableRooms... tableRooms) {
            tableRooms[0].setDate(new Date(System.currentTimeMillis()));
            mAsyncRoomDao.insetNewRoomRecord(tableRooms[0]);
            mAsyncRoomDao.updateNoOfRoomsInTableHosue(+1, tableRooms[0].getHouseId());

            /*add first meter reading if meter is enabled*/
            if (tableRooms[0].isMeterEnabled()) {
                mAsyncRoomDao.insertMeterReading(tableRooms[0].getAllMetersData());

                /*updates all meters table with new meters data*/
            }
            return null;
        }
    }

    private static class AsyncDleterRoom extends AsyncTask<TableRooms, Void, Void> {
        private final HouseDao mAsyncDeleteRoomDao;

        AsyncDleterRoom(HouseDao dao) {
            mAsyncDeleteRoomDao = dao;
        }

        @Override
        protected Void doInBackground(TableRooms... tableRooms) {

            TableRooms rooms = tableRooms[0];
            mAsyncDeleteRoomDao.deleteRoom(rooms);
            mAsyncDeleteRoomDao.updateNoOfRoomsInTableHosue(-1, rooms.getHouseId());
            if (rooms.isOcupiedStatus()) {
                mAsyncDeleteRoomDao.upDateTenantOccupiedStatus(0, 0, false, rooms.getTenantId());
            }
            return null;
        }
    }

    private static class AyncUpdateRoom extends AsyncTask<TableRooms, Void, Void> {
        private final HouseDao mAsyncUpdateDao;

        AyncUpdateRoom(HouseDao dao) {
            mAsyncUpdateDao = dao;
        }

        @Override
        protected Void doInBackground(TableRooms... tableRooms) {
            mAsyncUpdateDao.updateRoomData(tableRooms[0]);
            return null;
        }
    }

    private static class AsyncCreateTenant extends AsyncTask<TenantsPersonal, Void, Void> {
        private final HouseDao mAsyncCreateTenantDao;

        AsyncCreateTenant(HouseDao dao) {
            mAsyncCreateTenantDao = dao;
        }

        @Override
        protected Void doInBackground(TenantsPersonal... tenantsPersonals) {
            /*This will insert the current date*/
            TenantsPersonal tenantsPersonals1 = tenantsPersonals[0];
            tenantsPersonals1.setCreateDate();
            mAsyncCreateTenantDao.insertNewTenant(tenantsPersonals1);

            if (tenantsPersonals1.isRoomAlloted) {
                mAsyncCreateTenantDao.updateNoOfEmptyRoomsInTable(1, tenantsPersonals1.houseId);
                mAsyncCreateTenantDao.updatetheRoomOccupiedStatusAndName(true, tenantsPersonals1.roomId,
                        mAsyncCreateTenantDao.getTenantIdFromRoomIdFromTenantPersonal(tenantsPersonals1.roomId),/*fetch the tenant id and add it in room table*/
                        tenantsPersonals1.getTenantName(), tenantsPersonals1.getCreateDate());
            }
            if (tenantsPersonals1.meterPay) {
                mAsyncCreateTenantDao.insertMeterReading(tenantsPersonals1.getAllMetersData());
            }
            return null;
        }
    }

    private static class AsyncUpdateTenant extends AsyncTask<TenantsPersonal, Void, Void> {
        private final HouseDao mAsyncUpdateTenantDao;

        AsyncUpdateTenant(HouseDao dao) {
            mAsyncUpdateTenantDao = dao;
        }

        @Override
        protected Void doInBackground(TenantsPersonal... tenantsPersonals) {
            mAsyncUpdateTenantDao.updateTenant(tenantsPersonals[0]);
            return null;
        }
    }

    private static class AsyncDeleteTenant extends AsyncTask<TenantsPersonal, Void, Void> {
        private final HouseDao mAsyncDeleteTenantDao;

        AsyncDeleteTenant(HouseDao dao) {
            mAsyncDeleteTenantDao = dao;
        }

        @Override
        protected Void doInBackground(TenantsPersonal... tenantsPersonals) {
            mAsyncDeleteTenantDao.deleteTenant(tenantsPersonals[0]);
            return null;
        }
    }

    private static class AsyncInsertNewMeterReading extends AsyncTask<AllMetersData, Void, Void> {
        private final HouseDao asyncInsertNewMeterReadingDao;

        AsyncInsertNewMeterReading(HouseDao dao) {
            asyncInsertNewMeterReadingDao = dao;
        }

        @Override
        protected Void doInBackground(AllMetersData... allMetersData) {
            asyncInsertNewMeterReadingDao.insertMeterReading(allMetersData[0]);
            return null;
        }
    }

    //private parallel thread for inserting
    private static class AsyncInsertNewHouse extends AsyncTask<TableHouse, Void, Void> {
        private final HouseDao asyncDao;

        public AsyncInsertNewHouse(HouseDao mdao) {
            asyncDao = mdao;
        }

        @Override
        protected Void doInBackground(TableHouse... tableHouses) {
            TableHouse house = tableHouses[0];
            Date date = new Date(System.currentTimeMillis());
            house.setDate(date);
            asyncDao.insertHouseRecord(house);

            if (house.isRoomAutoGenerated()) {
                for (int i = 1; i <= house.getNoOfRooms(); i++) {
                    TableRooms rooms1 = new TableRooms();
                    rooms1.setRoomName("Room" + i);
                    rooms1.setRoomNo(i);
                    rooms1.setHouseId(house.getHouseIdForAutoRoom());
                    rooms1.setDate(date);
                    asyncDao.insetNewRoomRecord(rooms1);
                }
            }

            if (house.getIsMeterIncluded()) {/*Insert meter reading in the meters table*/
                asyncDao.insertMeterReading(house.getAllMetersData());

            }
            return null;
        }
    }

    /*for updating the house*/
    private static class AsyncUpdateHouse extends AsyncTask<TableHouse, Void, Void> {
        private final HouseDao updateHouseDao;

        AsyncUpdateHouse(HouseDao dao) {
            updateHouseDao = dao;
        }

        @Override
        protected Void doInBackground(TableHouse... tableHouses) {
            updateHouseDao.updateHouseData(tableHouses[0]);
            return null;
        }
    }

    //TODO:add meathod to update the no of paid bills in tenantPersonal;
    private static class AsyncInsertNewBill extends AsyncTask<Bills, Void, Void> {
        private final HouseDao asyncinsertnewbilldao;

        AsyncInsertNewBill(HouseDao dao) {
            asyncinsertnewbilldao = dao;
        }

        @Override
        protected Void doInBackground(Bills... bills) {
            bills[0].setCreateDate();
            asyncinsertnewbilldao.insertNewBill(bills[0]);
            asyncinsertnewbilldao.updateTotalBillsinTenant(+1, bills[0].tenantId);
            if (bills[0].ismeterPay) {
                /* fetch meter id using room id and set in the meters table sub-object bills table.*/
                bills[0].metersData.setMeterId(asyncinsertnewbilldao.getMeterId(bills[0].metersData.getRoomid()));
                asyncinsertnewbilldao.insertMeterReading(bills[0].getMetersData());
            }
            return null;
        }
    }
}


