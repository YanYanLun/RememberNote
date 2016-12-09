package com.dreamdesigner.remembernote.database;

import com.dreamdesigner.remembernote.application.NoteAppliction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANG on 2016/11/23.
 */

public class NoteData {
    private static List<Note> NoteList;
    private static NoteDao noteDao;

    /**
     * 查询所有
     *
     * @return
     */
    public static List<NoteYear> loadDate() {
        noteDao = NoteAppliction.getInstance().getDaoSession().getNoteDao();
        NoteList = noteDao.loadAll();
        return notesSort(NoteList);
    }

    /**
     * 数据排序
     *
     * @return
     */
    public static List<NoteYear> notesSort(List<Note> data) {
        if (data == null) {
            return null;
        }
        List<NoteYear> listYear = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Note note = data.get(i);
            if (note == null) {
                break;
            }
            if (listYear.size() <= 0) {
                NoteYear noteYear = new NoteYear();
                noteYear.Year = note.getYear();
                NoteMonth noteMonth = new NoteMonth();
                noteMonth.Month = note.getMonth();
                noteMonth.dayList.add(note);
                noteYear.monthList.add(noteMonth);
                listYear.add(noteYear);
            } else {
                boolean bool = false;
                for (int j = 0; j < listYear.size(); j++) {
                    NoteYear noteYear1 = listYear.get(j);
                    if (noteYear1.Year == note.getYear()) {
                        List<NoteMonth> noteMonths = noteYear1.monthList;
                        boolean monthTrue = false;
                        for (int s = 0; s < noteMonths.size(); s++) {
                            NoteMonth noteMonth = noteMonths.get(s);
                            if (noteMonth.Month == note.getMonth()) {
                                noteMonth.dayList.add(note);
                                listYear.remove(note);
                                monthTrue = true;
                            }
                        }
                        if (!monthTrue) {
                            NoteMonth noteMonth = new NoteMonth();
                            noteMonth.Month = note.getMonth();
                            noteMonth.dayList.add(note);
                            noteYear1.monthList.add(noteMonth);
                            listYear.remove(note);
                        }
                        bool = true;
                    }
                }
                if (!bool) {
                    NoteYear noteYear = new NoteYear();
                    noteYear.Year = note.getYear();
                    NoteMonth noteMonth = new NoteMonth();
                    noteMonth.Month = note.getMonth();
                    noteMonth.dayList.add(note);
                    noteYear.monthList.add(noteMonth);
                    listYear.add(noteYear);
                    listYear.remove(note);
                }
            }
        }
        return listYear;
    }
}

