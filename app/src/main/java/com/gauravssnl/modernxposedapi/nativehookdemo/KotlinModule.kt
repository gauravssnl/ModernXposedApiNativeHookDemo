package com.gauravssnl.modernxposedapi.nativehookdemo

import android.content.pm.ApplicationInfo
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam

private lateinit var module: KotlinModule


class KotlinModule(base: XposedInterface, param: ModuleLoadedParam): XposedModule(base, param) {
    init {
        log("MainModule at :: " + param.processName)
        module = this
    }

    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        super.onPackageLoaded(param)
        log("onPackageLoaded :: ${param.packageName}")
        log("param classloader is ::  ${param.classLoader}")
        log("module apk path is :: ${this.applicationInfo.sourceDir}")
        log("----------------------------")
        if (param.isFirstPackage) {
            // Note : If the hook does not work here, move the load lib code in constructor
            // Required when app loads native code even fore the App packages are loaded
            log("First package loaded")
            log("Loading native lib for hooking")
            System.loadLibrary("nativehookdemo");
            log(" Native lib loaded for hooking :)")
        } else {
            // Do something with other packages loaded by the app if we need
        }
    }

    override fun getApplicationInfo(): ApplicationInfo {
        return super.getApplicationInfo()
    }
}