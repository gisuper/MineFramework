package com.yx.framework.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * function:简单Permission工具方法，具体获取权限可使用第三方库
 */
public final class PermissionUtil {

    @NonNull
    public static String[] hasNotPermissions(Context context, String[] perms) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            for(String perm : perms) {
                if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, perm)) {//没有权限
                    permissions.add(perm);
                }
            }
            return permissions.toArray(new String[0]);
        } else {
            return new String[]{};
        }
    }
}
