package com.yx.framework.utils

import android.os.Environment
import android.text.TextUtils
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.ConsolePrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import com.yx.framework.common.App
import java.io.File

/**
 * Created by yangxiong on 2020/7/3.
 */
class FileUtil {
    companion object{
        fun getLogDirPath(): String? {
            var path: String =
                createLogDir().getAbsolutePath()
            if (!TextUtils.isEmpty(path) && !path.endsWith(File.separator)) {
                path = path + File.separator
            }
            return path
        }
         fun createLogDir(): File {
            val dir = File(
                getAppRootDir(),
                "/xlog/"
            )
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return dir

        }

        fun getAppRootDir(): File? {
            val externalCacheDir: File? = App.context.externalCacheDir
            return if (null == Environment.getExternalStorageState() || externalCacheDir == null) {
                App.context.cacheDir
            } else {
                val externalAppRootDir = externalCacheDir.parentFile
                if (!externalAppRootDir.exists()) {
                    externalAppRootDir.mkdirs()
                }
                externalAppRootDir
            }
        }
        fun initXLog(){
            val config=  LogConfiguration.Builder()
                .logLevel(LogLevel.ALL)
                .tag("XLog-TAG")
                .t()                                                   // 允许打印线程信息，默认禁止
                .st(2)                                                 // 允许打印深度为2的调用栈信息，默认禁止
                .b()                                                   // 允许打印日志边框，默认禁止
                .build()
            val androidPrinter =  AndroidPrinter()             // 通过 android.util.Log 打印日志的打印器
            val consolePrinter =  ConsolePrinter()            // 通过 System.out 打印日志到控制台的打印器
            val filePrinter =  FilePrinter                      // 打印日志到文件的打印器
                .Builder(getLogDirPath())                              // 指定保存日志文件的路径
                .fileNameGenerator( DateFileNameGenerator())        // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
                .backupStrategy( NeverBackupStrategy() )             // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
                .build();
            XLog.init(                                                 // 初始化 XLog
                config,                                                // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
                androidPrinter,                                        // 添加任意多的打印器。如果没有添加任何打印器，会默认使用 AndroidPrinter(Android)/ConsolePrinter(java)
                consolePrinter,
                filePrinter);
        }
    }
}