package com.tentlers.mngapp.data.database;

import android.content.Context;

import com.tentlers.mngapp.data.tables.house.TableHouse;
import com.tentlers.mngapp.data.tables.rooms.TableRooms;
import com.tentlers.mngapp.data.tables.bills.Bills;
import com.tentlers.mngapp.data.tables.dataconvertes.Converters;
import com.tentlers.mngapp.data.tables.meters.AllMeters;
import com.tentlers.mngapp.data.tables.meters.AllMetersData;
import com.tentlers.mngapp.data.tables.tenants.TenantsPersonal;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {TableHouse.class, TableRooms.class, TenantsPersonal.class, AllMetersData.class, AllMeters.class, Bills.class},
        version = 4, exportSchema = false)

@TypeConverters({Converters.class})
public abstract class DatabaseHouse extends RoomDatabase {

    private static volatile DatabaseHouse INSTANCE;
/*
    *//*TODO:edit migration*//*

    static final Migration migration2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };*/
    public static DatabaseHouse getDatabase(final Context context) {
        if (INSTANCE == null) {

            //synchronized so that only one thread can access the database
            synchronized (DatabaseHouse.class) {
                if (INSTANCE == null) {

                    // The context.getApplication context returns the context of process rather than returnig the context of activity
                    // The application context remains till the process dies irrespecive of the component activity
                    // useful for featching system services

                    // Name of the database;

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseHouse.class, "housedatabase")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract HouseDao mHouseDao();
}
