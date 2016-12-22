package com.dreamdesigner.library.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.dreamdesigner.library.R;

import java.util.List;

/**
 * Created by XIANG on 2016/12/22.
 */

public class ShortcutUtils {
    private static String AUTHORITY = null;

    /**
     * 为程序创建桌面快捷方式
     *
     * @param context
     * @param title
     * @param iconRes
     * @param shortcutIntent
     */
    public static void addShortcut(Context context, String title, Intent.ShortcutIconResource iconRes, Intent shortcutIntent) {
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra("duplicate", false); // 不允许重复创建
        context.sendBroadcast(intent);
    }

    /**
     * 是否快捷方式
     *
     * @param context
     * @param title
     * @return
     */
    public static boolean hasShortcut(Context context, String title, String title2) {
        boolean isInstalled = false;
        if (null == context || TextUtils.isEmpty(title) || TextUtils.isEmpty(title2)) {
            return isInstalled;
        }

        if (TextUtils.isEmpty(AUTHORITY)) {
            AUTHORITY = getAuthorityFromPermission(context);
        }

        final ContentResolver cr = context.getContentResolver();
        if (!TextUtils.isEmpty(AUTHORITY)) {
            try {
                final Uri CONTENT_URI = Uri.parse(AUTHORITY);
                Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?", new String[]{title}, null);

                if (c != null && c.getCount() > 0) {
                    isInstalled = true;
                }

                if (null != c && !c.isClosed()) {
                    c.close();
                }
            } catch (Exception e) {
                Log.e("hasShortcut", e.getMessage());
            }
        }
        Log.i("hasShortcutChineseName", "找到中文名字");
        if (!isInstalled) {
            try {
                final Uri CONTENT_URI = Uri.parse(AUTHORITY);
                Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?", new String[]{title2}, null);

                if (c != null && c.getCount() > 0) {
                    isInstalled = true;
                }

                if (null != c && !c.isClosed()) {
                    c.close();
                }
            } catch (Exception e) {
                Log.e("hasShortcut", e.getMessage());
            }
        }
        Log.i("hasShortcutEnglishName", "找到英文名字");
        return isInstalled;
    }

    /**
     * @param context
     * @return
     */
    public static String getAuthorityFromPermission(Context context) {
        // 获取默认
        String authority = getAuthorityFromPermissionDefault(context);
        // 获取特殊第三方
        if (authority == null || authority.trim().equals("")) {
            String packageName = getCurrentLauncherPackageName(context);
            packageName += ".permission.READ_SETTINGS";
            authority = getThirdAuthorityFromPermission(context, packageName);
        }

        if (TextUtils.isEmpty(authority)) {
            int sdkInt = android.os.Build.VERSION.SDK_INT;
            if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                authority = "com.android.launcher.settings";
            } else if (sdkInt < 19) {// Android 4.4以下
                authority = "com.android.launcher2.settings";
            } else {// 4.4以及以上
                authority = "com.android.launcher3.settings";
            }
        }
        authority = "content://" + authority + "/favorites?notify=true";

        return authority;
    }

    /**
     * @param context
     * @return
     */
    public static String getAuthorityFromPermissionDefault(Context context) {
        return getThirdAuthorityFromPermission(context, "com.android.launcher.permission.READ_SETTINGS");
    }

    /**
     * @param context
     * @return
     */
    public static String getCurrentLauncherPackageName(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res == null || res.activityInfo == null) {
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }

    /**
     * @param context
     * @param permission
     * @return
     */
    public static String getThirdAuthorityFromPermission(Context context, String permission) {
        if (TextUtils.isEmpty(permission)) {
            return "";
        }

        try {
            List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packageInfos == null) {
                return "";
            }

            for (PackageInfo pack : packageInfos) {
                ProviderInfo[] providers = pack.providers;

                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission) || permission.equals(provider.writePermission)) {
                            if (!TextUtils.isEmpty(provider.authority)// 精准匹配launcher.settings，再一次验证
                                    && (provider.authority).contains(".launcher.settings")) {
                                return provider.authority;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 35      * 删除当前应用的桌面快捷方式
     * 36      *
     * 37      * @param context
     * 38
     */
    public static void deleteShortCut(Context context) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }
        // 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        Intent shortcutIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        context.sendBroadcast(shortcut);

    }
}
