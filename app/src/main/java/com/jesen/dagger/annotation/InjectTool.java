package com.jesen.dagger.annotation;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InjectTool {

    public static void inject(Object obj) {
        injectSetContentView(obj);

        injectBindView(obj);
    }

    // 把布局注入Activity
    private static void injectSetContentView(Object obj) {
        Class<?> mMainActivity = obj.getClass();

        // 拿到Activity的注解
        MeContentView mContentView = mMainActivity.getAnnotation(MeContentView.class);
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

    // 把控件注入Activity
    private static void injectBindView(Object object){
        Class<?> mainActivityClass = object.getClass();

        // 遍历布局所有控件
        Field[] fields = mainActivityClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            MeBindView meBindView = field.getAnnotation(MeBindView.class);
            if (meBindView == null){
                continue;
            }
            int viewId = meBindView.value();

            try {
                Method findViewById = mainActivityClass.getMethod("findViewById", int.class);
                Object resultView = findViewById.invoke(object, viewId);
                field.set(object,resultView); // button = findViewById(R.id.xxx)
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
