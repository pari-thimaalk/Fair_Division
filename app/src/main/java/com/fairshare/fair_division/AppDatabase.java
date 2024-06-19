package com.fairshare.fair_division;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Allocation.class}, version = 9)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AllocationDao allocationDao();

}
