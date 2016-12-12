package com.dreamdesigner.remembernote.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.database.Note;
import com.dreamdesigner.remembernote.database.NoteDao;
import com.dreamdesigner.remembernote.dialog.WriteDialog;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.Calendar;

/**
 * Created by XIANG on 2016/12/12.
 */

public class QuickNoteActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et, et1;
    private NoteDao noteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_write);
        setStatusBar();
        noteDao = NoteAppliction.getInstance().getDaoSession().getNoteDao();
        load();
    }

    private void load() {
        et = (EditText) this.findViewById(R.id.edit0);
        et1 = (EditText) this.findViewById(R.id.edit1);
        TextView enter = (TextView) findViewById(R.id.enter);
        TextView cancel = (TextView) findViewById(R.id.cancel);
        TextView menu = (TextView) findViewById(R.id.menu);
        enter.setOnClickListener(this);
        cancel.setOnClickListener(this);
        menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter:
                write();
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.menu:
                Intent intent = new Intent();
                intent.setClass(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void write() {
        String title = et.getText().toString().trim();
        String content = et1.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请输入随记标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入随记内容", Toast.LENGTH_SHORT).show();
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
        note.setTime(year + "-" + month + "-" + day + " " + hour + ":" + minute);
        long status = noteDao.insert(note);
        if (status > 0) {
            Toast.makeText(this, "新随记已保存", Toast.LENGTH_SHORT).show();
            et.setText("");
            et1.setText("");
        } else {
            Toast.makeText(this, "随记保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    protected void setStatusBar() {
        StatusBarUtil.setTransparent(this);
    }
}
