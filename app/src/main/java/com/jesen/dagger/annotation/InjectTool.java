package com.jesen.dagger.annotation;

import android.util.Log;
import android.view.View;

import com.jesen.dagger.annotation.jianrong.OnBaseCommon;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectTool {

    public static void inject(Object obj) {
        injectSetContentView(obj);

        injectBindView(obj);

        injectClick(obj);

        injectEvnent(obj); // 兼容Android一系列事件
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

    /**
     * 兼容Android一系列事件，考虑到扩展
     */
    private static void injectEvnent(final Object mainActivityObject) {

        Class<?> mainActivityClass = mainActivityObject.getClass();

        Method[] declaredMethods = mainActivityClass.getDeclaredMethods();

        for (final Method declaredMethod : declaredMethods) { // 遍历Activity的方法
            declaredMethod.setAccessible(true);

            Annotation[] annotations = declaredMethod.getAnnotations();//  @Deprecated   @OnClickCommon(R.id.bt_t1)
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();

                // 寻找是否有 OnBaseCommon
                OnBaseCommon onBaseCommon = annotationType.getAnnotation(OnBaseCommon.class);
                if (onBaseCommon == null) {
                    // 结束本次循环，进入下一个循环
                    Log.d("InjectTool---", "OnBaseCommon == null ");
                    continue;
                }

                // 证明已经找到了 含有OnBaseCommon的注解
                // 获取事件三要素
                String setCommonListener = onBaseCommon.setCommonListener(); // setOnClickListener
                Class setCommonObjectListener = onBaseCommon.setCommonObjectListener(); // View.OnClickListener.class
                String callbackMethod = onBaseCommon.callbackMethod(); // onClick(View v) {}

                // 之前的方式,，由于是动态变化的，不能这样拿，所以才使用反射
                // annotationType.getAnnotation(OnClickLongCommon.class).value();

                // get R.id.bt_t1 == 8865551
                try {
                    Method valueMethod = annotationType.getDeclaredMethod("value");
                    valueMethod.setAccessible(true);
                    int value = (int) valueMethod.invoke(annotation);

                    // 实例化 R.id.bt_t1 得到View
                    // findViewById(8865551);
                    Method findViewByIdMethod = mainActivityClass.getMethod("findViewById", int.class);
                    // View view = findViewById(8865551);
                    Object viewObject = findViewByIdMethod.invoke(mainActivityObject, value);

                    // Method mViewMethod = view.getClass().getMethod("setOnClickListener", View.OnClickListener.class);
                    Method mViewMethod = viewObject.getClass().getMethod(setCommonListener, setCommonObjectListener);

                    // view.setOnClickListener(new View.OnClickListener...)

                    // 动态代理
                    Object proxy =  Proxy.newProxyInstance(
                            setCommonObjectListener.getClassLoader(),
                            new Class[]{setCommonObjectListener}, // OnClickListener
                            new InvocationHandler() {
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    // 执行MainActivity里面的方法
                                    return declaredMethod.invoke(mainActivityObject, null);
                                }
                            });
                    mViewMethod.invoke(viewObject, proxy);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }

    }

}
