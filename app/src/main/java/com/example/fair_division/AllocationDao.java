package com.example.fair_division;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.flow.Flow;

@Dao
public interface AllocationDao {

    @Insert
    Completable InsertRankingAsync(Allocation alloc);

    @Query("SELECT * FROM allocation")
    Flowable<List<Allocation>> getAllAllocationsAsync();

    @Query("SELECT * FROM allocation WHERE session_id LIKE :session")
    Flowable<List<Allocation>> getAllocationsAsync(String session);



}
