package com.tentlers.mngapp.data.database;

import android.app.Application;
import android.os.AsyncTask;

import com.tentlers.mngapp.data.tables.TableHouse;
import com.tentlers.mngapp.data.tables.TableRooms;
import com.tentlers.mngapp.data.tables.bills.BillItemForCard;
import com.tentlers.mngapp.data.tables.bills.Bills;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.meters.GetLastMeterReading;
import com.tentlers.mngapp.data.tables.queryobjects.HouseForHomeFragment;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameAndId;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameIdNoRooms;
import com.tentlers.mngapp.data.tables.queryobjects.HouseNameMeterId;
import com.tentlers.mngapp.data.tables.rooms.RoomNoName;
import com.tentlers.mngapp.data.tables.rooms.RoomNoNameId;
import com.tentlers.mngapp.data.tables.tenants.TenantBillEntry;
import com.tentlers.mngapp.data.tables.tenants.TenantNameHouseRoom;
import com.tentlers.mngapp.data.tables.tenants.TenantsPersonal;

import java.sql.Date;
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
    public LiveData<TableHouse> getHosueForSpecificHouse(int houseid) {
        return mdao.getHouseForSpecificHouse(houseid);
    }

    // Meathod to insert new house in database
    /* Also created new rooms in the rooms table*/
    public void insertNewHouse(TableHouse dataTable) {
        new AsyncNewInsertDatabase(mdao).execute(dataTable);
    }

    public void deleteHouse(TableHouse tableHouse) {
        new AsyncDeleteHouse(mdao).execute(tableHouse);
    }

    //Meathod to delete the house

    /*
     * This is used  for checking the uniqueness of house name and meter id in house Entry fragment
     */
    public LiveData<HouseNameMeterId[]> mgetHousenameMeterId() {
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
    public LiveData<List<TableRooms>> getThreeRooms(int mhouseId) {
        return mdao.getThreerooms(mhouseId);
    }

    /*
     * The data from this query is use to show all the rooms specific to hosue.
     */
    public LiveData<List<TableRooms>> getAllRooms(int mhouseId) {
        return mdao.getAllRoomsOfHouse(mhouseId);
    }

    /* Quarries for room tables*/

    /*
     * All the data recquired to check the uniquenes of the room
     * meterid and room name for a specific house
     * in room  entry fragment.
     */
    public LiveData<List<Long>> getallHosueMeterids() {
        return mdao.getHouseMeterid();
    }

    public LiveData<List<Long>> getAllroomids() {
        return mdao.getRoomMeter();
    }

    public LiveData<List<RoomNoName>> getRoomNoName(long parentid) {
        return mdao.getroomNoName(parentid);
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
    public LiveData<Long[]> getLastEnteredMeterReading(GetLastMeterReading meterDataObj) {
        if (meterDataObj.isMeterid) {
            return mdao.getLastMeterEntry(meterDataObj.meterId, meterDataObj.noOfReadings);
        } else if (meterDataObj.isRoomId) {
            return mdao.getLastMeterReadingForRoom(meterDataObj.roomId, meterDataObj.noOfReadings);
        } else if (meterDataObj.isHouseIdForhouseMeter) {
            return mdao.getLastMeterReadingForRoom(meterDataObj.houseId, meterDataObj.noOfReadings);
        }
        return null;
    }

    /* Get all tenant list for tenant fragment*/
    public LiveData<List<TenantNameHouseRoom>> getAllTenantNHR(boolean withRoomAlloted) {
        return mdao.getAllTenantNHR(withRoomAlloted);
    }

    /* for bill entry fragment.*/
    /* Get the selected tenant's object.*/
    public LiveData<TenantBillEntry> getSelectedTenant(int tenantid) {
        return mdao.getSelectedTenant(tenantid);
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

    //private parallel thread for inserting
    private static class AsyncNewInsertDatabase extends AsyncTask<TableHouse, Void, Void> {
        private final HouseDao asyncDao;

        public AsyncNewInsertDatabase(HouseDao mdao) {
            asyncDao = mdao;
        }

        @Override
        protected Void doInBackground(TableHouse... tableHouses) {
            TableHouse house = tableHouses[0];
            Date date = new Date(System.currentTimeMillis());
            house.setDate(date);
            asyncDao.insertHouseRecord(house);

            if (house.isRoomAutoGenerated) {
                for (int i = 1; i <= house.noOfRooms; i++) {
                    TableRooms rooms1 = new TableRooms();
                    rooms1.roomName = "Room" + i;
                    rooms1.roomNo = i;
                    rooms1.houseId = house.houseIdForAutoRoom;
                    rooms1.setDate(date);
                    asyncDao.insetNewRoomRecord(rooms1);
                }
            }

            if (house.isIncludeMeter()) {/*Insert meter reading in the meters table*/
                asyncDao.insertMeterReading(house.getAllMetersData());
            }
            return null;
        }
    }

    private static class AsyncDeleteHouse extends AsyncTask<TableHouse, Void, Void> {
        private HouseDao asyncDao;

        public AsyncDeleteHouse(HouseDao mdao) {
            asyncDao = mdao;
        }


        @Override
        protected Void doInBackground(TableHouse... tableHouses) {
            TableHouse house = tableHouses[0];
            if (house.isDeleteHouseByid) {
                asyncDao.deleteHouseById(house.houseId);
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
            mAsyncRoomDao.updateNoOfRoomsInTableHosue(+1, tableRooms[0].houseId);
            if (tableRooms[0].isMeterEnabled) {
                mAsyncRoomDao.insertMeterReading(tableRooms[0].getAllMetersData());
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
            mAsyncDeleteRoomDao.deleteRoom(tableRooms[0]);
            mAsyncDeleteRoomDao.updateNoOfRoomsInTableHosue(-1, tableRooms[0].houseId);
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
            /*
             * This will insert the current date
             */
            tenantsPersonals[0].setCreateDate();
            mAsyncCreateTenantDao.insertNewTenant(tenantsPersonals[0]);

            if (tenantsPersonals[0].isRoomAlloted) {
                mAsyncCreateTenantDao.updateNoOfEmptyRoomsInTable(1, tenantsPersonals[0].houseId);
                mAsyncCreateTenantDao.updatetheRoomOccupiedStatus(true, tenantsPersonals[0].roomId);
            }
            if (tenantsPersonals[0].meterPay) {
                mAsyncCreateTenantDao.insertMeterReading(tenantsPersonals[0].getAllMetersData());
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
    //TODO:add meathod to update the paid bills;

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
                bills[0].metersData.meterId = asyncinsertnewbilldao.getMeterId(bills[0].metersData.Roomid);
                asyncinsertnewbilldao.insertMeterReading(bills[0].getMetersData());
            }
            return null;
        }
    }

}


