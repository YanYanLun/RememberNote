package com.dreamdesigner.remembernote.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANG on 2016/11/23.
 */

public class NoteYear implements Serializable {
    public int Year;
    public List<NoteMonth> monthList = new ArrayList<>();
}
