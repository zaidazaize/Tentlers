package com.example.easelife.data.database;

import android.app.Application;
import android.os.AsyncTask;

import com.example.easelife.data.tables.queryobjects.HouseNameId;
import com.example.easelife.data.tables.queryobjects.HouseNameMeterId;
import com.example.easelife.data.tables.TableHouse;
import com.example.easelife.data.tables.TableRooms;
import com.example.easelife.data.tables.rooms.RoomNoName;

import java.sql.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class Repository {

    /* This the repository class that handles communication between different data sources
     * And hadling different database thread*/

    private LiveData<List<TableHouse>> mAllhousData;
    private HouseDao mdao;

    // Repository constructor
    public Repository(Application application) {

        //Initialise the database pass the applecation context
        // get the dao class from it
        //and then get the all houese live data from it
        DatabaseHouse db = DatabaseHouse.getDatabase(application.getApplicationContext());
        mdao = db.mHouseDao();
        mAllhousData = mdao.getAllTableHouse();
    }


    // Return the live data of all rooms.
    public LiveData<List<TableHouse>> mgetAllHouse() {
        return mAllhousData;
    }

    // Meathod to insert new house in database
    public void insertNewHouse(TableHouse dataTable) {
        new AsyncNewInsertDatabase(mdao).execute(dataTable);
    }

    //private parallel thread for inserting
    private static class AsyncNewInsertDatabase extends AsyncTask<TableHouse, Void, Void> {
        private final HouseDao asyncDao;

        public AsyncNewInsertDatabase(HouseDao mdao) {
            asyncDao = mdao;
        }

        @Override
        protected Void doInBackground(TableHouse... tableHouses) {
            tableHouses[0].setDate(new Date(System.currentTimeMillis()));
            asyncDao.insertHouseRecord(tableHouses[0]);
            return null;
        }
    }


    //Meathod to delete the house

    public void deleteHouse(TableHouse tableHouse) {
        new AsyncDeleteHouse(mdao).execute(tableHouse);
    }

    private static class AsyncDeleteHouse extends AsyncTask<TableHouse, Void, Void> {
        private HouseDao asyncDao;

        public AsyncDeleteHouse(HouseDao mdao) {
            asyncDao = mdao;
        }


        @Override
        protected Void doInBackground(TableHouse... tableHouses) {
            asyncDao.deleteHouse(tableHouses[0]);
            return null;
        }
    }

    /*
    * This is used  for checking the uniqueness of house name and meter id in house Entry fragment
    */
    public HouseNameMeterId[] mgetHousenameMeterId() {
        return mdao.gethouseNameMeterId();
    }


    /*
     * getting all houses name for diaplaying in "Rooms fragment" in the spinner
     */
    public LiveData<List<HouseNameId>> getHouseNameId() {
        return mdao.getHouseNameId();
    }


    /* Querries for room tables*/

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


    /* Mnipulating room Table*/

    // Inserting room
    public void insetNewRoom(TableRooms rooms) {
        new InsertNewRoom(mdao).execute(rooms);
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
            return null;
        }
    }


    // Deleting Rooms
    public void deleteRoom(TableRooms rooms) {
        new AsyncDleterRoom(mdao).execute(rooms);
    }

    private static class AsyncDleterRoom extends AsyncTask<TableRooms, Void, Void> {
        private final HouseDao mAsyncDeleteRoomDao;

        AsyncDleterRoom(HouseDao dao) {
            mAsyncDeleteRoomDao = dao;
        }

        @Override
        protected Void doInBackground(TableRooms... tableRooms) {
            mAsyncDeleteRoomDao.deleteRoom(tableRooms[0]);
            return null;
        }
    }


    // Updating rooms
    public void updateRoom(TableRooms rooms) {
        new AyncUpdateRoom(mdao).execute(rooms);
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

}


