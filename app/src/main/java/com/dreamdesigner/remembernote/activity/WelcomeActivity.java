package com.dreamdesigner.remembernote.activity;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dreamdesigner.library.Utils.AppOnForegroundUtils;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.dialog.WriteDialog;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.Locale;

/**
 * Created by XIANG on 2016/12/12.
 */

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private RelativeLayout root;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        root = (RelativeLayout) findViewById(R.id.root);
        preferences = getSharedPreferences("NoteState", 0);
        imageView = (ImageView) findViewById(R.id.welcomImages);
        setStatusBar();
        if (preferences == null) {
            root.setVisibility(View.VISIBLE);
            setImageView();
            root.setBackground(NoteAppliction.getInstance().getDrawable());
            mHandler.sendEmptyMessageDelayed(1, 2000);
            return;
        }
        if (!preferences.contains("SwithcState")) {
            root.setVisibility(View.VISIBLE);
            setImageView();
            root.setBackground(NoteAppliction.getInstance().getDrawable());
            mHandler.sendEmptyMessageDelayed(1, 2000);
            return;
        }
        boolean bool = preferences.getBoolean("SwithcState", false);
        if (bool) {
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, QuickNoteActivity.class);
            startActivity(intent);
            finish();
        } else {
            root.setVisibility(View.VISIBLE);
            setImageView();
            root.setBackground(NoteAppliction.getInstance().getDrawable());
            mHandler.sendEmptyMessageDelayed(1, 2000);
        }

    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    private Handler mHandler = new Handler() {
        // 注意：在各个case后面不能做太耗时的操作，否则出现ANR对话框
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void Reg() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticValueUtils.HomeNoteChangeValue);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (action.equals(StaticValueUtils.HomeNoteChangeValue)) {
                finish();
            }
        }
    };

    private void setImageView() {
        if (isLunarSetting())
            imageView.setImageDrawable(getDrawable(R.mipmap.welcome_zh));
        else
            imageView.setImageDrawable(getDrawable(R.mipmap.welcome_en));
    }

    public boolean isLunarSetting() {
        String language = getLanguageEnv();
        if (language != null
                && (language.trim().equals("zh-CN") || language.trim().equals("zh-TW")))
            return true;
        else
            return false;
    }

    private String getLanguageEnv() {
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        String country = l.getCountry().toLowerCase();
        if ("zh".equals(language)) {
            if ("cn".equals(country)) {
                language = "zh-CN";
            } else if ("tw".equals(country)) {
                language = "zh-TW";
            }
        } else if ("pt".equals(language)) {
            if ("br".equals(country)) {
                language = "pt-BR";
            } else if ("pt".equals(country)) {
                language = "pt-PT";
            }
        }
        return language;
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
