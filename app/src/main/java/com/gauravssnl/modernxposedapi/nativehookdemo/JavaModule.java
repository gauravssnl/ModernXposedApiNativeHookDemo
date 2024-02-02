package com.gauravssnl.modernxposedapi.nativehookdemo;

import androidx.annotation.NonNull;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;

@SuppressWarnings("unused")
public class JavaModule extends XposedModule {

    public JavaModule(@NonNull XposedInterface base, @NonNull ModuleLoadedParam param) {
        super(base, param);
        log("MainModule at " + param.getProcessName());
    }

    @Override
    public void onPackageLoaded(@NonNull PackageLoadedParam param) {
        super.onPackageLoaded(param);
        log("onPackageLoaded: " + param.getPackageName());
        log("param classloader is " + param.getClassLoader());
        log("module apk path: " + this.getApplicationInfo().sourceDir);
        log("----------");
        if (param.isFirstPackage()) {
            log("First package ...)");
            log("Loading native lib for hooking");
            System.loadLibrary("nativehookdemo");
            log("Library for hooking loaded");
        } else {
            log("Not the first package...");
            // we can hook other package loaded by the Android app here if we need to do so.
        }
    }
}
