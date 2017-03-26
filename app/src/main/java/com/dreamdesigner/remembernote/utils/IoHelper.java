package com.dreamdesigner.remembernote.utils;

/**
 * Created by XIANG on 2017/3/23.
 */

public class IoHelper {
    public static boolean isAsk = false;
    public static boolean isAudioAsk = false;

    public static void putIoDontAsk(boolean bool) {
        isAsk = bool;
    }

    public static boolean getIoDontAsk() {
        return isAsk;
    }

    public static void putAudioIoDontAsk(boolean bool) {
        isAudioAsk = bool;
    }

    public static boolean getAudioIoDontAsk() {
        return isAudioAsk;
    }
}
