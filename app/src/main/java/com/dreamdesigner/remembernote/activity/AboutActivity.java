package com.dreamdesigner.remembernote.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.dreamdesigner.library.BaseActivity.NoCollapsingActivity;
import com.dreamdesigner.library.funswitch.FunSwitch;
import com.dreamdesigner.remembernote.BuildConfig;
import com.dreamdesigner.remembernote.R;

public class AboutActivity extends NoCollapsingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void onFocusChanged() {
        setTitle("关于");
        TextView version = (TextView) findViewById(R.id.version);
        String VERSION_NAME = BuildConfig.VERSION_NAME;
        version.setText("当前版本 V" + VERSION_NAME);
    }
}
