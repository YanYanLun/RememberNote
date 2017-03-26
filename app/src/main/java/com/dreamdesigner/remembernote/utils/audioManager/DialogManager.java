package com.dreamdesigner.remembernote.utils.audioManager;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.models.MediaRecorderEvent;
import com.dreamdesigner.remembernote.widget.VoiceLineView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by cooffee on 15/10/19.
 */
public class DialogManager {
    private Dialog mDialog;

    private TextView mLabel;

    private Context mContext;
    private VoiceLineView voiceLine;
    private MediaRecorder mMediaRecorder;
    private boolean isAlive = true;
    private View view;

    public DialogManager(Context context) {
        mContext = context;
    }

    public void showRecordingDialog() {
        Log.d("LONG", "showRecordingDialog");
        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.dialog_recorder, null);
        mDialog.setContentView(view);
        mLabel = (TextView) mDialog.findViewById(R.id.id_recorder_dialog_label);
        voiceLine = (VoiceLineView) mDialog.findViewById(R.id.voiceLine);
        mDialog.show();
    }

    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mLabel.setVisibility(View.VISIBLE);
            mLabel.setText(mContext.getString(R.string.str_recorder_will_cancel));
        }
    }

    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mLabel.setVisibility(View.VISIBLE);
            mLabel.setText(mContext.getString(R.string.str_recorder_want_cancel));
        }
    }

    public void tooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            mLabel.setVisibility(View.VISIBLE);
            mLabel.setText(mContext.getString(R.string.str_recorder_time_short));
        }
    }

    public void dimissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 通过更新level来更新voice上的图片
     *
     * @param level 1~7
     */
    public void updateVoiceLevel(int level) {
        if (mDialog != null && mDialog.isShowing()) {
            int resId = mContext.getResources().getIdentifier("v" + level, "drawable",
                    mContext.getPackageName());
            try {
                if (mMediaRecorder == null)
                    return;
                double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
                double db = 0;// 分贝
                if (ratio > 1)
                    db = 30 * Math.log10(ratio);
                if (voiceLine == null)
                    voiceLine = (VoiceLineView) mDialog.findViewById(R.id.voiceLine);
                if (voiceLine != null)
                    voiceLine.setVolume((int) (db));
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public void onMessageEvent(MediaRecorder event) {
        if (event == null)
            return;
        showRecordingDialog();
        this.mMediaRecorder = event;
        if (voiceLine == null)
            voiceLine = (VoiceLineView) mDialog.findViewById(R.id.voiceLine);
        if (voiceLine != null)
            voiceLine.start();
    }
}
