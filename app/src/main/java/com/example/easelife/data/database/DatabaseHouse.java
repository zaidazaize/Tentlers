package com.example.easelife.data.database;
import android.content.Context;

import com.example.easelife.data.tables.dataconvertes.Converters;
import com.example.easelife.data.tables.TableHouse;
import com.example.easelife.data.tables.TableRooms;
import com.example.easelife.data.tables.meters.AllMetersData;
import com.example.easelife.data.tables.tenants.TenantsPersonal;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {TableHouse.class , TableRooms.class, TenantsPersonal.class, AllMetersData.class},version = 11 ,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DatabaseHouse extends RoomDatabase{

    public abstract HouseDao mHouseDao();
    private static volatile DatabaseHouse INSTANCE;

    public static DatabaseHouse getDatabase(final Context context) {
        if (INSTANCE == null) {

            //synchronized so that only one thread can access the database
            synchronized (DatabaseHouse.class) {
                if (INSTANCE == null) {

                    // The context.getApplication context returns the context of process rather than returnig the context of activity
                    // The application context remains till the process dies irrespecive of the component activity
                    // useful for featching system services

                    // Name of the database;

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseHouse.class,"housedatabase")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
