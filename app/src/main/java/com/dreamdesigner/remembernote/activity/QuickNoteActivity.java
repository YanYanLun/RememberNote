package com.dreamdesigner.remembernote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamdesigner.library.Utils.AppOnForegroundUtils;
import com.dreamdesigner.library.Utils.DisplayUtil;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.models.Note;
import com.dreamdesigner.remembernote.database.NoteDao;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;
import com.jaeger.library.StatusBarUtil;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

/**
 * Created by XIANG on 2016/12/12.
 */

public class QuickNoteActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et, et1;
    private NoteDao noteDao;
    private LinearLayout quick_write;
    private ImageView voice;

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
        quick_write = (LinearLayout) findViewById(R.id.quick_write);
        voice = (ImageView) findViewById(R.id.voice);
        TextView enter = (TextView) findViewById(R.id.enter);
        TextView cancel = (TextView) findViewById(R.id.cancel);
        TextView menu = (TextView) findViewById(R.id.menu);
        enter.setOnClickListener(this);
        cancel.setOnClickListener(this);
        menu.setOnClickListener(this);
        voice.setOnClickListener(this);

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {//键盘弹出时
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    params.topMargin = DisplayUtil.dip2px(QuickNoteActivity.this, 40);
                    params.leftMargin = DisplayUtil.dip2px(QuickNoteActivity.this, 20);
                    params.rightMargin = DisplayUtil.dip2px(QuickNoteActivity.this, 20);
                    quick_write.setLayoutParams(params);
                } else { //键盘隐藏时
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    params.leftMargin = DisplayUtil.dip2px(QuickNoteActivity.this, 20);
                    params.rightMargin = DisplayUtil.dip2px(QuickNoteActivity.this, 20);
                    quick_write.setLayoutParams(params);
                }
            }
        });
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
            case R.id.voice:
                Intent newsListIntent = new Intent(this, HomeActivity.class);
                newsListIntent.putExtra(StaticValueUtils.SCHEME_VOICE_KEY, StaticValueUtils.VOICE_OPEN);
                startActivity(newsListIntent);
                finish();
                break;
        }
    }

    private void write() {
        String title = et.getText().toString().trim();
        String content = et1.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, getString(R.string.dialog_hint_title), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, getString(R.string.dialog_hint_content), Toast.LENGTH_SHORT).show();
            return;
        }
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setYear(StaticValueUtils.getYear());
        note.setMonth(StaticValueUtils.getMonth());
        note.setDay(StaticValueUtils.getDay());
        note.setTime(StaticValueUtils.getDataTime());
        note.setType(0);
        long status = noteDao.insert(note);
        if (status > 0) {
            Toast.makeText(this, getString(R.string.prompt_new_note_success), Toast.LENGTH_SHORT).show();
            et.setText("");
            et1.setText("");
            Intent intent = new Intent();
            intent.setAction(StaticValueUtils.ACTION_REFRESH_MANUAL);
            sendBroadcast(intent);
        } else {
            Toast.makeText(this, getString(R.string.prompt_new_note_fail), Toast.LENGTH_SHORT).show();
        }
    }

    protected void setStatusBar() {
        StatusBarUtil.setTransparent(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!AppOnForegroundUtils.isAppOnForeground(this)) {
            //app 进入后台
            //全局变量isActive = false 记录当前已经进入后台
            Intent intent = new Intent();
            intent.setAction("activity.isAppOnForeground");
            sendBroadcast(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setAction("activity.isAppUnForeground");
        sendBroadcast(intent);
    }
}
