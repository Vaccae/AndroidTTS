package dem.vac.ttsdemo

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import java.lang.Exception

/**
 * 作者：Vaccae
 * 邮箱：3657447@qq.com
 * 创建时间：2019-11-26 11:35
 * 功能模块说明：
 */

class CheckAppInstall {
    companion object StaticFun {
        fun isAppInstalled(context: Context, uri: String): Boolean {
            var pm: PackageManager = context.packageManager
            var installed = false
            if(TextUtils.isEmpty(uri)) return installed
            try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
                installed = true
            } catch (ex: Exception) {
                Log.i("install", ex.message)
                installed = false
            }
            return installed
        }
    }
}