//
// Created by gauravssnl.

#include "hooker.h"
#include "helper.h"

#include <dlfcn.h>

static HookFunType hook_func = nullptr;

// we will focus mainly on this hook example
jstring fake(JNIEnv *env, jobject) {
    std::string hello = "Hello from Modern Xposed API Native  lib:)";
    return env->NewStringUTF(hello.c_str());
}

void on_library_loaded(const char *name, void *handle) {
    // hooks on `libtarget.so`
    if (ends_with(std::string(name), "hellojni.so")) {
        // hook target function
        void *target = dlsym(handle, "Java_com_gauravssnl_hellojni_MainActivity_stringFromJNI");
        hook_func(target, (void *) fake, (void **) &original);
    }
}

// Note : native_init is mandatory; do not change signature
extern "C" [[gnu::visibility("default")]] [[gnu::used]]
NativeOnModuleLoaded native_init(const NativeAPIEntries *entries) {
    hook_func = entries->hook_func;
    // system hooks if want; optional
    hook_func((void *) fopen, (void *) fake_fopen, (void **) &original_fopen);
    return on_library_loaded;
}


jclass fake_FindClass(JNIEnv *env, const char *name) {
    if (!strcmp(name, "dalvik/system/BaseDexClassLoader"))
        return nullptr;
    return original_FindClass(env, name);
}

// If we need JNI Env hooks; optional
extern "C" [[gnu::visibility("default")]] [[gnu::used]]
jint JNI_OnLoad(JavaVM *jvm, void *) {
    JNIEnv *env = nullptr;
    jvm->GetEnv((void **) &env, JNI_VERSION_1_6);
    hook_func((void *) env->functions->FindClass, (void *) fake_FindClass,
              (void **) &original_FindClass);
    return JNI_VERSION_1_6;
}


FILE *fake_fopen(const char *filename, const char *mode) {
    if (strstr(filename, "banned")) return nullptr;
    return original_fopen(filename, mode);
}