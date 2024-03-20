package com.example.fair_division;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "allocation")
public class Allocation {

    @NonNull
    @ColumnInfo(name = "session_id")
    private String sessionId;

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "allocation_id")
    private int allocId;


    @ColumnInfo(name = "person_name")
    private String personName;

    @ColumnInfo(name = "good_name")
    private String goodName;

    @ColumnInfo(name = "credits")
    private int credits;


    // Constructor, getters, and setters
    public Allocation(String personName, String goodName, int credits, @NonNull String sessionId) {
        this.personName = personName;
        this.goodName = goodName;
        this.credits = credits;
        this.sessionId = sessionId;
    }

    @NonNull
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(@NonNull String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public int getAllocId() { return allocId; }

    public void setAllocId(int id) { this.allocId = id;}


    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
