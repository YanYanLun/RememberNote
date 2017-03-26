package com.dreamdesigner.remembernote.utils.art;

import android.support.v4.app.ActivityCompat;

import com.dreamdesigner.remembernote.activity.AudioRecorderActivity;

import java.lang.ref.WeakReference;

import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

/**
 * Created by XIANG on 2017/3/23.
 */

public class AudioRecorderActivityPermissionsDispatcher {
    private static final int REQUEST_SHOWSTORAGE = 0;

    private static final String[] PERMISSION_SHOWCAMERA = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};

    private static final int REQUEST_SHOWCONTACTS = 1;

    private static final String[] PERMISSION_SHOWCONTACTS = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};

    private AudioRecorderActivityPermissionsDispatcher() {
    }

    public static void showSorageWithCheck(AudioRecorderActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {
            target.onSuccess();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {
                target.showRationaleForStorage(new ShowCameraPermissionRequest(target));
            } else {
                ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWSTORAGE);
            }
        }
    }

    public static void showContactsWithCheck(AudioRecorderActivity target) {
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

    public static void onRequestPermissionsResult(AudioRecorderActivity target, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SHOWSTORAGE:
                if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {
                    target.onStorageDenied();
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.onSuccess();
                } else {
                    if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {
                        target.onStorageNeverAskAgain();
                    } else {
                        target.onStorageDenied();
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
        private final WeakReference<AudioRecorderActivity> weakTarget;

        private ShowCameraPermissionRequest(AudioRecorderActivity target) {
            this.weakTarget = new WeakReference<AudioRecorderActivity>(target);
        }

        @Override
        public void proceed() {
            AudioRecorderActivity target = weakTarget.get();
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWSTORAGE);
        }

        @Override
        public void cancel() {
            AudioRecorderActivity target = weakTarget.get();
            if (target == null) return;
            target.onStorageDenied();
        }
    }

    private static final class ShowContactsPermissionRequest implements PermissionRequest {
        private final WeakReference<AudioRecorderActivity> weakTarget;

        private ShowContactsPermissionRequest(AudioRecorderActivity target) {
            this.weakTarget = new WeakReference<AudioRecorderActivity>(target);
        }

        @Override
        public void proceed() {
            AudioRecorderActivity target = weakTarget.get();
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCONTACTS, REQUEST_SHOWCONTACTS);
        }

        @Override
        public void cancel() {
        }
    }
}
