package com.dreamdesigner.remembernote.utils.art;

import android.support.v4.app.ActivityCompat;

import com.dreamdesigner.remembernote.activity.HomeActivity;
import com.dreamdesigner.remembernote.activity.SettingActivity;

import java.lang.ref.WeakReference;

import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

/**
 * Created by XIANG on 2017/3/23.
 */

public class HomeActivityPermissionsDispatcher {
    private static final int REQUEST_SHOWSTORAGE = 0;

    private static final String[] PERMISSION_SHOWCAMERA = new String[]{"android.permission.RECORD_AUDIO"};

    private static final int REQUEST_SHOWCONTACTS = 1;

    private static final String[] PERMISSION_SHOWCONTACTS = new String[]{"android.permission.RECORD_AUDIO"};

    private HomeActivityPermissionsDispatcher() {
    }

    public static void showSorageWithCheck(HomeActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {
            target.onSuccess();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {
                target.showRationaleForAudio(new ShowCameraPermissionRequest(target));
            } else {
                ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWSTORAGE);
            }
        }
    }

    public static void showContactsWithCheck(SettingActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCONTACTS)) {
            target.onSuccess();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCONTACTS)) {
                target.showRationaleForStorage(new ShowContactsPermissionRequest(target));
            } else {
                ActivityCompat.requestPermissions(target, PERMISSION_SHOWCONTACTS, REQUEST_SHOWCONTACTS);
            }
        }
    }

    public static void onRequestPermissionsResult(HomeActivity target, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SHOWSTORAGE:
                if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {
                    target.onAudioDenied();
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.onSuccess();
                } else {
                    if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {
                        target.onAudioNeverAskAgain();
                    } else {
                        target.onAudioDenied();
                    }
                }
                break;
            case REQUEST_SHOWCONTACTS:
                if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCONTACTS)) {
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.onSuccess();
                }
                break;
            default:
                break;
        }
    }

    private static final class ShowCameraPermissionRequest implements PermissionRequest {
        private final WeakReference<HomeActivity> weakTarget;

        private ShowCameraPermissionRequest(HomeActivity target) {
            this.weakTarget = new WeakReference<HomeActivity>(target);
        }

        @Override
        public void proceed() {
            HomeActivity target = weakTarget.get();
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWSTORAGE);
        }

        @Override
        public void cancel() {
            HomeActivity target = weakTarget.get();
            if (target == null) return;
            target.onAudioDenied();
        }
    }

    private static final class ShowContactsPermissionRequest implements PermissionRequest {
        private final WeakReference<SettingActivity> weakTarget;

        private ShowContactsPermissionRequest(SettingActivity target) {
            this.weakTarget = new WeakReference<SettingActivity>(target);
        }

        @Override
        public void proceed() {
            SettingActivity target = weakTarget.get();
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCONTACTS, REQUEST_SHOWCONTACTS);
        }

        @Override
        public void cancel() {
        }
    }
}
