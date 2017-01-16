package com.dreamdesigner.remembernote.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dreamdesigner.library.BaseActivity.NoCollapsingActivity;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.database.Note;

public class ContentActivity extends NoCollapsingActivity {
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
            setTitle(getString(R.string.app_name));
        } else {
            setTitle(note.getTitle());
        }
    }

    @Override
    protected void onFocusChanged() {
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
