package com.dreamdesigner.remembernote.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.dreamdesigner.library.BaseActivity.NoCollapsingActivity;
import com.dreamdesigner.library.Utils.ShortcutUtils;
import com.dreamdesigner.library.funswitch.FunSwitch;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;

public class SettingActivity extends NoCollapsingActivity {
    private FunSwitch switchButton, shortcutButton;
    private SharedPreferences preferences;

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
    }
}
