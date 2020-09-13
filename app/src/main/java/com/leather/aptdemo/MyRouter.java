package com.leather.aptdemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyRouter {
    private volatile static MyRouter instance;
    private Map<String, Class<?>> routerMap = new HashMap<>();
    private Map<String, String> stringParams = new HashMap<>();
    private Map<String, Integer> intParams = new HashMap<>();
    private Map<String, Boolean> booleanParams = new HashMap<>();
    private Context context;

    public static MyRouter getInstance() {
        if (instance == null) {
            synchronized (MyRouter.class) {
                if (instance == null) {
                    instance = new MyRouter();
                }
            }
        }
        return instance;
    }

    public void init(Application application) {
        this.context = application;

        Set<String> fileNames = ClassUtil.getFileNameByPackageName(context, "com.leather.aptdemo");
        for (String name : fileNames) {
            Class<?> clazz;
            try {
                clazz = Class.forName(name);
                Object obj = clazz.newInstance();
                if (obj instanceof IRouter) {
                    IRouter route = (IRouter) obj;
                    route.loadInto();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void addRouter(String path, Class<?> clazz) {
        if (!routerMap.containsKey(path)) {
            routerMap.put(path, clazz);
        }
    }

    public MyRouter withString(String key, String value) {
        if (!stringParams.containsKey(key) && !TextUtils.isEmpty(key)) {
            stringParams.put(key, value);
        }
        return this;
    }

    public MyRouter withInt(String key, int value) {
        if (!intParams.containsKey(key) && !TextUtils.isEmpty(key)) {
            intParams.put(key, value);
        }
        return this;
    }

    public MyRouter withBool(String key, boolean value) {
        if (!booleanParams.containsKey(key) && !TextUtils.isEmpty(key)) {
            booleanParams.put(key, value);
        }
        return this;
    }

    public void navigation(String path) {
        Class<?> aClass = routerMap.get(path);
        if (aClass != null) {
            Intent intent = new Intent(context, aClass);
            if (!stringParams.isEmpty()) {
                for (String key : stringParams.keySet()) {
                    intent.putExtra(key, stringParams.get(key));
                }
            }
            if (!intParams.isEmpty()) {
                for (String key : intParams.keySet()) {
                    intent.putExtra(key, intParams.get(key));
                }
            }
            if (!booleanParams.isEmpty()) {
                for (String key : booleanParams.keySet()) {
                    intent.putExtra(key, booleanParams.get(key));
                }
            }
            context.startActivity(intent);
        }
    }
}
