package com.dreamdesigner.remembernote.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.dreamdesigner.remembernote.R;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

/**
 * Created by XIANG on 2017/2/27.
 */

public class AlertInfoUtils {
    private Context mContext;

    public AlertInfoUtils(Context context) {
        this.mContext = context;
    }

    public void Warning(String title, String content) {
        LemonHello.getInformationHello(title, content)
                .addAction(new LemonHelloAction(mContext.getString(R.string.prompt_cancel), new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        if (click != null)
                            click.onCancel();
                    }
                }))
                .addAction(new LemonHelloAction(mContext.getString(R.string.prompt_enter), Color.RED, new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        if (click != null)
                            click.onAgree(helloInfo.getTitle());
                    }
                }))
                .show(mContext);
    }

    public interface OnInformationClick {
        public void onAgree(String title);

        public void onCancel();
    }

    public OnInformationClick click;

    public void setOnInformationClick(OnInformationClick click) {
        this.click = click;
    }
}
