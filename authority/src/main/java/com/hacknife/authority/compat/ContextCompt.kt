package com.hacknife.authority.compat

import android.content.Context
import android.content.pm.PackageManager

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */

val Context.appInfo
    get() = packageManager.getApplicationInfo(
        packageName,
        PackageManager.GET_META_DATA
    )
