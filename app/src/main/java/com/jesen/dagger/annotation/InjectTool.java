package com.jesen.dagger.annotation;

import android.util.Log;

import java.lang.reflect.Method;

public class InjectTool {

    public static void inject(Object obj) {
        injectSetContentView(obj);
    }

    // 把布局注入Activity
    private static void injectSetContentView(Object obj) {
        Class<?> mMainActivity = obj.getClass();

        // 拿到Activity的注解
        ContentView mContentView = mMainActivity.getAnnotation(ContentView.class);
        if (mContentView == null) {
            Log.d("Injectool---", " layout is null.");
            return;
        }

        // 拿到设置的布局id
        int layoutId = mContentView.value();

        // 布局设置给Activity
        try {
            Method setContentView = mMainActivity.getMethod("setContentView", int.class);
            setContentView.invoke(obj, layoutId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
