package com.dreamdesigner.remembernote.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANG on 2016/11/23.
 */

public class NoteMonth implements Serializable{
    public int Month;
    public List<Note> dayList = new ArrayList<>();
}
