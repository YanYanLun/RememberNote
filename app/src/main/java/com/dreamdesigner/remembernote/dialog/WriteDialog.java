package com.dreamdesigner.remembernote.dialog;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.database.Note;
import com.dreamdesigner.remembernote.database.NoteDao;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import cn.carbs.android.library.MDDialog;

/**
 * Created by XIANG on 2016/11/22.
 */

public class WriteDialog {
    private Context mContext;
    private MDDialog.Builder mdDialog;
    private NoteDao noteDao;

    public WriteDialog(Context context) {
        super();
        this.mContext = context;
        mdDialog = new MDDialog.Builder(context);
        noteDao = NoteAppliction.getInstance().getDaoSession().getNoteDao();
    }

    public void show() {
        mdDialog
                .setContentView(R.layout.content_dialog)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        EditText et = (EditText) contentView.findViewById(R.id.edit0);
                        et.setHint("输入随记标题");
                        EditText et1 = (EditText) contentView.findViewById(R.id.edit1);
                        et1.setHint("输入随记内容");
                    }
                })
//                      .setMessages(messages)
                .setTitle("新随记")
                .setNegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {

                    @Override
                    public void onClick(View clickedView, View contentView) {
                        EditText et = (EditText) contentView.findViewById(R.id.edit0);
                        EditText et1 = (EditText) contentView.findViewById(R.id.edit1);
                        String title = et.getText().toString().trim();
                        String content = et1.getText().toString().trim();
                        if (TextUtils.isEmpty(title)) {
                            Toast.makeText(mContext, "请输入随记标题", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(content)) {
                            Toast.makeText(mContext, "请输入随记内容", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Note note = new Note();
                        note.setTitle(title);
                        note.setContent(content);
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH) + 1;
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);
                        note.setYear(year);
                        note.setMonth(month);
                        note.setDay(day);
//                        note.setId(Long.parseLong(year + month + day + hour + minute + c.get(Calendar.SECOND) + ""));
                        note.setTime(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                        long status = noteDao.insert(note);
                        if (status > 0) {
//                            Toast.makeText(mContext, "新增数据成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction(StaticValueUtils.HomeNoteChangeValue);
                            mContext.sendBroadcast(intent);
                        } else {
                            Toast.makeText(mContext, "新增数据失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButtonMultiListener(new MDDialog.OnMultiClickListener() {

                    @Override
                    public void onClick(View clickedView, View contentView) {

                    }
                })
                .setOnItemClickListener(new MDDialog.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int index) {
                        if (index == 0) {
                            Toast.makeText(mContext, "index 0", Toast.LENGTH_SHORT).show();
                        } else if (index == 1) {
                            Toast.makeText(mContext, "index 1", Toast.LENGTH_SHORT).show();
                        } else if (index == 2) {
                            Toast.makeText(mContext, "index 2", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setWidthMaxDp(600)
//                      .setShowTitle(false)
                .setShowButtons(true)
                .create()
                .show();
    }
}
