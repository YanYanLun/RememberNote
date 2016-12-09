package com.dreamdesigner.remembernote.models;

import com.dreamdesigner.remembernote.database.Note;

/**
 * Created by ishratkhan on 24/02/16.
 */
public class DataModel {
    int level;
    String name;
    Note note;

    public DataModel(int level, String name, Note note) {
        this.level = level;
        this.name = name;
        this.note = note;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
