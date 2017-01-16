package com.dreamdesigner.remembernote.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dreamdesigner.library.BaseActivity.NoCollapsingActivity;
import com.dreamdesigner.library.funswitch.FunSwitch;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.database.Note;
import com.dreamdesigner.remembernote.database.NoteData;
import com.dreamdesigner.remembernote.utils.ExcelUtils;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends NoCollapsingActivity implements View.OnClickListener {
    private FunSwitch switchButton, shortcutButton;
    private SharedPreferences preferences;
    private RelativeLayout action_data_backup, action_data_reduction;
    private String[] title = {"Id", "Title", "Content", "Images", "Url", "Year", "Month", "Day", "Time"};
    private ArrayList<ArrayList<String>> notes2List = new ArrayList<>();
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onFocusChanged() {
        setTitle(getString(R.string.action_settings));
        preferences = getSharedPreferences("NoteState", 0);
        switchButton = (FunSwitch) findViewById(R.id.switchButton);
        switchButton.setState(preferences.getBoolean("SwithcState", false));
        switchButton.setOnFunSwitchLinstener(new FunSwitch.OnFunSwitchLinstener() {
            @Override
            public void OnChangeState(Boolean state) {
                preferences.edit().putBoolean("SwithcState", state).commit();
            }
        });

        shortcutButton = (FunSwitch) findViewById(R.id.shortcutButton);
        shortcutButton.setState(preferences.getBoolean("ShortcutState", false));
        shortcutButton.setOnFunSwitchLinstener(new FunSwitch.OnFunSwitchLinstener() {
            @Override
            public void OnChangeState(Boolean state) {
                preferences.edit().putBoolean("ShortcutState", state).commit();
                if (state) {
                    NoteAppliction.getInstance().addShortcut();
                } else {
//                    ShortcutUtils.deleteShortCut(SettingActivity.this);
//                    Toast.makeText(SettingActivity.this, "Delete Shortcut!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        action_data_backup = (RelativeLayout) findViewById(R.id.action_data_backup);
        action_data_reduction = (RelativeLayout) findViewById(R.id.action_data_reduction);
        action_data_backup.setOnClickListener(this);
        action_data_reduction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_data_backup:
                List<Note> noteList = NoteData.getNoteList();
                if (noteList == null) {
                    Toast.makeText(this, getString(R.string.prompt_no_notes), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (noteList.size() <= 0) {
                    Toast.makeText(this, getString(R.string.prompt_no_notes), Toast.LENGTH_SHORT).show();
                    return;
                }
                file = new File(StaticValueUtils.getSDPath() + "/RememberNotesBackup");
                StaticValueUtils.makeDir(file);
                ExcelUtils.initExcel(file.toString() + "/NotesBackup.xls", title);
                ExcelUtils.writeObjListToExcel(getNotesData(noteList), file.toString() + "/NotesBackup.xls", this);
                break;
            case R.id.action_data_reduction:
                ExcelUtils.read2Excel(this);
                break;
        }
    }

    private ArrayList<ArrayList<String>> getNotesData(List<Note> noteList) {
        for (Note item : noteList) {
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(String.valueOf(item.getId()));
            beanList.add(item.getTitle());
            beanList.add(item.getContent());
            beanList.add(item.getImages());
            beanList.add(item.getUrl());
            beanList.add(String.valueOf(item.getYear()));
            beanList.add(String.valueOf(item.getMonth()));
            beanList.add(String.valueOf(item.getDay()));
            beanList.add(item.getTime());
            notes2List.add(beanList);
        }
        return notes2List;
    }

}
