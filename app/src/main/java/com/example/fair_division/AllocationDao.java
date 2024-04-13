package com.example.fair_division;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface AllocationDao {

    @Insert
    Completable InsertRankingAsync(Allocation alloc);

    @Query("SELECT * FROM allocation")
    Flowable<List<Allocation>> getAllAllocationsAsync();

    @Query("SELECT * FROM allocation WHERE session_id LIKE :session")
    Flowable<List<Allocation>> getAllocationsAsync(String session);

    @Query("SELECT * FROM allocation WHERE session_id LIKE :session")
    List<Allocation> getAllocationsSync(String session);

    @Query("SELECT * FROM allocation WHERE allocation_id >= :num")
    Single<List<Allocation>> getAllocationsAfter(int num);

    @Query("SELECT DISTINCT session_id FROM allocation WHERE allocation_id >= :num")
    Flowable<List<String>> getAllocationSessionsAsync(int num);

    @Query("SELECT DISTINCT session_id FROM allocation WHERE allocation_id >= :num")
    List<String> getAllocationSessionsSync(int num);



}
