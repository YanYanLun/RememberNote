package com.dreamdesigner.remembernote.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.dreamdesigner.library.BaseActivity.NoCollapsingActivity;
import com.dreamdesigner.library.funswitch.FunSwitch;
import com.dreamdesigner.remembernote.R;

public class SettingActivity extends NoCollapsingActivity {
    private FunSwitch switchButton;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onFocusChanged() {
        setTitle("随记设置");
        preferences = getSharedPreferences("NoteState", 0);
        switchButton = (FunSwitch) findViewById(R.id.switchButton);
        switchButton.setState(preferences.getBoolean("SwithcState", false));
        switchButton.setOnFunSwitchLinstener(new FunSwitch.OnFunSwitchLinstener() {
            @Override
            public void OnChangeState(Boolean state) {
                preferences.edit().putBoolean("SwithcState", state).commit();
            }
        });
    }
}
