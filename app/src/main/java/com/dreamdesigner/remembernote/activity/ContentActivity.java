package com.dreamdesigner.remembernote.activity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamdesigner.library.BaseActivity.NoWriteActivity;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.database.Note;

public class ContentActivity extends NoWriteActivity {
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        note = (Note) getIntent().getSerializableExtra("Note");
        if (note == null) {
            setContentView(R.layout.activity_no_data);
            return;
        }
        setContentView(R.layout.content_content);
        if (TextUtils.isEmpty(note.getTitle())) {
            setTitle("记.随记");
        } else {
            setTitle(note.getTitle());
        }
    }

    @Override
    protected void onFocusChanged() {
        setVisibleFloatingActionButton(View.INVISIBLE);
        if (TextUtils.isEmpty(note.getContent())) {
            getNoteText().setText("暂无内容！");
        } else {
            getNoteText().setText(note.getContent());
        }

    }

    private TextView getNoteText() {
        return (TextView) findViewById(R.id.note_text);
    }
}
