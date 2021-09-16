package com.jesen.dagger.annotation;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    /**
     * 把布局里面的控件ID 和 Activity方法绑定起来，建立事件
     * @param object == MainActivity
     */
    private static void injectClick(final Object object) {

        Class<?> mainActivityClass = object.getClass();

        // 遍历MainActivity所有的方法
        Method[] declaredMethods = mainActivityClass.getDeclaredMethods();

        for (final Method declaredMethod : declaredMethods) { // ...  private void show() {}  onCreate  test111  test222
            declaredMethod.setAccessible(true);

            Click click = declaredMethod.getAnnotation(Click.class);

            if (click == null) {
                Log.d("InjectTool---", "Click == null ");
                continue;
            }

            // get R.id.bt_test3
            int viewID = click.value();

            try {
                Method findViewByIdMethod = mainActivityClass.getMethod("findViewById", int.class);
                Object resultView = findViewByIdMethod.invoke(object, viewID); // findViewById(viewID);

                View view = (View) resultView; // View view = findViewById(viewID == R.id.bt_test3);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 执行此方法 private void show() {}
                        // declaredMethod == show
                        try {
                            declaredMethod.invoke(object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
