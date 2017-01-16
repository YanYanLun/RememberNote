package com.dreamdesigner.remembernote.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.database.Note;
import com.dreamdesigner.remembernote.database.NoteDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtils {
    public static WritableFont arial14font = null;

    public static WritableCellFormat arial14format = null;
    public static WritableFont arial10font = null;
    public static WritableCellFormat arial10format = null;
    public static WritableFont arial12font = null;
    public static WritableCellFormat arial12format = null;

    public final static String UTF8_ENCODING = "UTF-8";
    public final static String GBK_ENCODING = "GBK";
    public static NoteDao noteDao;
    public static boolean bool = false;

    public static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14,
                    WritableFont.BOLD);
            arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
            arial10font = new WritableFont(WritableFont.ARIAL, 10,
                    WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(jxl.format.Colour.LIGHT_BLUE);
            arial12font = new WritableFont(WritableFont.ARIAL, 12);
            arial12format = new WritableCellFormat(arial12font);
            arial12format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
        } catch (WriteException e) {

            e.printStackTrace();
        }
    }

    public static void initExcel(String fileName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("NotesBackup", 0);
            sheet.addCell((WritableCell) new Label(0, 0, fileName,
                    arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(List<T> objList,
                                               String fileName, Context c) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName),
                        workbook);
                WritableSheet sheet = writebook.getSheet(0);
                for (int j = 0; j < objList.size(); j++) {
                    ArrayList<String> list = (ArrayList<String>) objList.get(j);
                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j + 1, list.get(i),
                                arial12format));
                    }
                }
                writebook.write();
                Toast.makeText(c, c.getString(R.string.prompt_backup_success), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(c, c.getString(R.string.prompt_backup_fail), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * 还原数据
     *
     * @param context
     */
    public static void read2Excel(Context context) {
        bool = false;
        noteDao = NoteAppliction.getInstance().getDaoSession().getNoteDao();
        try {
            Workbook course = null;
            course = Workbook.getWorkbook(new File(StaticValueUtils.getSDPath() + "/RememberNotesBackup/NotesBackup.xls"));
            Sheet sheet = course.getSheet(0);
            Cell cell = null;
            for (int i = 1; i < sheet.getRows(); i++) {
                Note note = new Note();
                cell = sheet.getCell(0, i);
                note.setId(Long.parseLong(cell.getContents()));
                cell = sheet.getCell(1, i);
                note.setTitle(cell.getContents());
                cell = sheet.getCell(2, i);
                note.setContent(cell.getContents());
                cell = sheet.getCell(3, i);
                note.setImages(cell.getContents());
                cell = sheet.getCell(4, i);
                note.setUrl(cell.getContents());
                cell = sheet.getCell(5, i);
                note.setYear(Integer.parseInt(cell.getContents()));
                cell = sheet.getCell(6, i);
                note.setMonth(Integer.parseInt(cell.getContents()));
                cell = sheet.getCell(7, i);
                note.setDay(Integer.parseInt(cell.getContents()));
                cell = sheet.getCell(8, i);
                note.setTime(cell.getContents());
                List<Note> list = noteDao.QueryNotesExistence(note.getTime());
                if (list == null) {
                    long sum = noteDao.insert(note);
                    bool = true;
                } else if (list.size() <= 0) {
                    long sum = noteDao.insert(note);
                    bool = true;
                }
                if (i == sheet.getRows() - 1) {
                    if (bool) {
                        Intent intent = new Intent();
                        intent.setAction(StaticValueUtils.HomeNoteChangeValue);
                        context.sendBroadcast(intent);
                        intent = new Intent();
                        intent.setAction(StaticValueUtils.ACTION_REFRESH_MANUAL);
                        context.sendBroadcast(intent);
                        Toast.makeText(context, context.getString(R.string.prompt_reduction_success), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.prompt_not_changed), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.prompt_reduction_fail), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static Object getValueByRef(Class cls, String fieldName) {
        Object value = null;
        fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
                .substring(0, 1).toUpperCase());
        String getMethodName = "get" + fieldName;
        try {
            Method method = cls.getMethod(getMethodName);
            value = method.invoke(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
