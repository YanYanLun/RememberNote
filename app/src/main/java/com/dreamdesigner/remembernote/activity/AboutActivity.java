package com.dreamdesigner.remembernote.activity;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamdesigner.library.BaseActivity.NoCollapsingActivity;
import com.dreamdesigner.library.funswitch.FunSwitch;
import com.dreamdesigner.remembernote.BuildConfig;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;

import java.util.Locale;

public class AboutActivity extends NoCollapsingActivity {
    private ImageView aboutImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        aboutImages = (ImageView) findViewById(R.id.aboutImages);
        setImageView();
    }

    @Override
    protected void onFocusChanged() {
        setTitle("关于");
        TextView version = (TextView) findViewById(R.id.version);
        String VERSION_NAME = BuildConfig.VERSION_NAME;
        version.setText(getString(R.string.action_version) + " V" + VERSION_NAME);
    }

    private void setImageView() {
        if (isLunarSetting())
            aboutImages.setImageDrawable(getDrawable(R.mipmap.welcome_zh));
        else
            aboutImages.setImageDrawable(getDrawable(R.mipmap.welcome_en));
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
}
