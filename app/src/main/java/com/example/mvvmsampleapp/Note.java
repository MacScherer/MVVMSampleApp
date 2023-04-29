package com.example.mvvmsampleapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "note_table") // defines name of table
public class Note {

    @PrimaryKey (autoGenerate = true) // increment ID each new row editing ID column
    private int id;
    private String title;
    private String description;
    private int priority;

    // Declare var and generate Getter method and make a constructor without ID
    // because ID is auto generated, don't need to be a parameter

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) { //set ID to another object case you make mistakes
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

}
