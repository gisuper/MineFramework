package com.yx.framework.ext

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File

/**
 * Created by luyao
 * on 2019/6/17 9:09
 */

/**
 * Return the Intent with [Settings.ACTION_APPLICATION_DETAILS_SETTINGS]
 */
fun Context.getAppInfoIntent(packageName: String = this.packageName): Intent =
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

/**
 * Jump to the app info page
 */
fun Context.goToAppInfoPage(packageName: String = this.packageName) {
    startActivity(getAppInfoIntent(packageName))
}

/**
 * Return the Intent with [Settings.ACTION_DATE_SETTINGS]
 */
fun Context.getDateAndTimeIntent(): Intent =
    Intent(Settings.ACTION_DATE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra("packageName", packageName)
    }

/**
 * Jump to the data and time page
 */
fun Context.goToDateAndTimePage() {
    startActivity(getDateAndTimeIntent())
}

/**
 * Return the Intent with [Settings.ACTION_LOCALE_SETTINGS]
 */
fun Context.getLanguageIntent() =
    Intent(Settings.ACTION_LOCALE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra("packageName", packageName)
    }

/**
 * Jump to the language page
 */
fun Context.goToLanguagePage() {
    startActivity(getLanguageIntent())
}

/**
 * Return the Intent for install apk
 */
fun Context.getInstallIntent(apkFile: File): Intent? {
    if (!apkFile.exists()) return null
    val intent = Intent(Intent.ACTION_VIEW)
    val uri: Uri

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        uri = Uri.fromFile(apkFile)
    } else {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val authority = "$packageName.fileprovider"
        uri = FileProvider.getUriForFile(this, authority, apkFile)
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return intent
}

/**
 * Jump to the accessibility page
 */
fun Context.goToAccessibilitySetting() =
    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).run { startActivity(this) }


/**
 * install apk
 * @note need android.permission.REQUEST_INSTALL_PACKAGES after N
 */
fun Context.installApk(apkFile: File) {
    val intent = getInstallIntent(apkFile)
    intent?.run { startActivity(this) }
}

/**
 * Visit the specific url with browser
 */
fun Context.openBrowser(url: String) {
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).run { startActivity(this) }
}

/**
 * Visit app in app store
 * @param packageName default value is current app
 */
fun Context.openInAppStore(packageName: String = this.packageName) {
    val intent = Intent(Intent.ACTION_VIEW)
    try {
        intent.data = Uri.parse("market://details?id=$packageName")
        startActivity(intent)
    } catch (ifPlayStoreNotInstalled: ActivityNotFoundException) {
        intent.data =
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        startActivity(intent)
    }
}

/**
 * Open app by [packageName]
 */
fun Context.openApp(packageName: String) =
    packageManager.getLaunchIntentForPackage(packageName)?.run { startActivity(this) }

/**
 * Uninstall app by [packageName]
 */
fun Context.uninstallApp(packageName: String) {
    Intent(Intent.ACTION_DELETE).run {
        data = Uri.parse("package:$packageName")
        startActivity(this)
    }
}

/**
 * Send email
 * @param email the email address be sent to
 * @param subject a constant string holding the desired subject line of a message, @see [Intent.EXTRA_SUBJECT]
 * @param text a constant CharSequence that is associated with the Intent, @see [Intent.EXTRA_TEXT]
 */
fun Context.sendEmail(email: String, subject: String?, text: String?) {
    Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")).run {
        subject?.let { putExtra(Intent.EXTRA_SUBJECT, subject) }
        text?.let { putExtra(Intent.EXTRA_TEXT, text) }
        startActivity(this)
    }
}

fun Activity.startFileChooser(requestCode: Int, allowMultiple: Boolean = false) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "*/*"
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    if (fromSpecificVersion(Build.VERSION_CODES.JELLY_BEAN_MR2)) intent.putExtra(
        Intent.EXTRA_ALLOW_MULTIPLE,
        allowMultiple
    )
    startActivityForResult(Intent.createChooser(intent, "Choose File"), requestCode)
}

fun fromSpecificVersion(version: Int): Boolean = Build.VERSION.SDK_INT >= version
