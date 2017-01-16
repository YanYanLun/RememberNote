package com.dreamdesigner.remembernote.dialog;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dreamdesigner.dialoglibrary.MDDialog;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.database.Note;
import com.dreamdesigner.remembernote.database.NoteDao;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;

import org.greenrobot.greendao.rx.RxDao;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by XIANG on 2016/11/22.
 */

public class WriteDialog {
    private Context mContext;
    private MDDialog.Builder mdDialog;
    private NoteDao noteDao;
    private MDDialog mMDDialog;
    private Note mNote;

    public WriteDialog(Context context) {
        super();
        this.mContext = context;
        mdDialog = new MDDialog.Builder(context);
        noteDao = NoteAppliction.getInstance().getDaoSession().getNoteDao();
    }

    public void setNotes(Note notes) {
        this.mNote = notes;
    }

    public void updateShow() {
        HaveNotes();
    }

    public void show() {
        NohaveNotes();
    }

    private void HaveNotes() {
        mdDialog
                .setContentView(R.layout.content_dialog)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        EditText et = (EditText) contentView.findViewById(R.id.edit0);
                        et.setText(mNote.getTitle());
                        if (TextUtils.isEmpty(mNote.getTitle()))
                            et.setHint(mContext.getString(R.string.dialog_hint_title));
                        else
                            et.setText(mNote.getTitle());
                        EditText et1 = (EditText) contentView.findViewById(R.id.edit1);
                        if (TextUtils.isEmpty(mNote.getContent()))
                            et1.setHint(mContext.getString(R.string.dialog_hint_content));
                        else
                            et1.setText(mNote.getContent());
                    }
                })
//                      .setMessages(messages)
                .setTitle(mContext.getString(R.string.action_update))
                .setNegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMDDialog.dismiss();
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //确定第一步
                    }
                })
                .setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {

                    @Override
                    public void onClick(View clickedView, View contentView) {
                        //确定第二步
                        EditText et = (EditText) contentView.findViewById(R.id.edit0);
                        EditText et1 = (EditText) contentView.findViewById(R.id.edit1);
                        String title = et.getText().toString().trim();
                        String content = et1.getText().toString().trim();
                        if (TextUtils.isEmpty(title)) {
                            Toast.makeText(mContext, mContext.getString(R.string.prompt_dialog_title), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(content)) {
                            Toast.makeText(mContext, mContext.getString(R.string.prompt_dialog_content), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mNote.setTitle(title);
                        mNote.setContent(content);
                        mNote.setYear(StaticValueUtils.getYear());
                        mNote.setMonth(StaticValueUtils.getMonth());
                        mNote.setDay(StaticValueUtils.getDay());
                        mNote.setTime(StaticValueUtils.getDataTime());
                        noteDao.update(mNote);
                        Intent intent = new Intent();
                        intent.setAction(StaticValueUtils.HomeNoteChangeValue);
                        mContext.sendBroadcast(intent);
                        intent = new Intent();
                        intent.setAction(StaticValueUtils.ACTION_REFRESH_MANUAL);
                        mContext.sendBroadcast(intent);
                        mMDDialog.dismiss();
                    }
                })
                .setNegativeButtonMultiListener(new MDDialog.OnMultiClickListener() {

                    @Override
                    public void onClick(View clickedView, View contentView) {
                        mMDDialog.dismiss();
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
                .create();
        mMDDialog = mdDialog.show();
    }

    private void NohaveNotes() {
        mdDialog
                .setContentView(R.layout.content_dialog)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        EditText et = (EditText) contentView.findViewById(R.id.edit0);
                        et.setHint(mContext.getString(R.string.dialog_hint_title));
                        EditText et1 = (EditText) contentView.findViewById(R.id.edit1);
                        et1.setHint(mContext.getString(R.string.dialog_hint_content));
                    }
                })
//                      .setMessages(messages)
                .setTitle(mContext.getString(R.string.dialog_title))
                .setNegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMDDialog.dismiss();
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //确定第一步
                    }
                })
                .setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {

                    @Override
                    public void onClick(View clickedView, View contentView) {
                        //确定第二步
                        EditText et = (EditText) contentView.findViewById(R.id.edit0);
                        EditText et1 = (EditText) contentView.findViewById(R.id.edit1);
                        String title = et.getText().toString().trim();
                        String content = et1.getText().toString().trim();
                        if (TextUtils.isEmpty(title)) {
                            Toast.makeText(mContext, mContext.getString(R.string.prompt_dialog_title), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(content)) {
                            Toast.makeText(mContext, mContext.getString(R.string.prompt_dialog_content), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Note note = new Note();
                        note.setTitle(title);
                        note.setContent(content);
                        note.setYear(StaticValueUtils.getYear());
                        note.setMonth(StaticValueUtils.getMonth());
                        note.setDay(StaticValueUtils.getDay());
                        note.setTime(StaticValueUtils.getDataTime());
                        long status = noteDao.insert(note);
                        if (status > 0) {
                            Intent intent = new Intent();
                            intent.setAction(StaticValueUtils.HomeNoteChangeValue);
                            mContext.sendBroadcast(intent);
                            intent = new Intent();
                            intent.setAction(StaticValueUtils.ACTION_REFRESH_MANUAL);
                            mContext.sendBroadcast(intent);
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.prompt_new_note_fail), Toast.LENGTH_SHORT).show();
                        }
                        mMDDialog.dismiss();
                    }
                })
                .setNegativeButtonMultiListener(new MDDialog.OnMultiClickListener() {

                    @Override
                    public void onClick(View clickedView, View contentView) {
                        mMDDialog.dismiss();
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
                .create();
        mMDDialog = mdDialog.show();
    }
}
