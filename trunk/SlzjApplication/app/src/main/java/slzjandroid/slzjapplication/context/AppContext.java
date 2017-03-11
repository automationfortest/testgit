package slzjandroid.slzjapplication.context;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppContext extends Application {
    private static AppContext instance;
    private ExecutorService executor = Executors.newFixedThreadPool(5);
    private static CopyOnWriteArrayList<Activity> activityStack;
    private static Context xContext;

    public static AppContext getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.instance = this;
        xContext = getApplicationContext();
        openUmeng();
    }

    private void openUmeng() {
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public static void setInstance(AppContext instance) {
        AppContext.instance = instance;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new CopyOnWriteArrayList<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity(堆栈中最后一个压入的)
     */
    public Activity currentActivity() {
        Activity activity = activityStack.get(activityStack.size() - 1);
        return activity;
    }

    /**
     * 结束当前Activity(堆栈中最后一个压入的)
     */
    public void finishActivity() {
        Activity activity = null;
        if (activityStack != null) {
            activity = activityStack.get(activityStack.size() - 1);
        }
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束除了当前的Activity之外所有的Activity
     */
    public void finishALLExceptLastActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                if (this.currentActivity().equals(activityStack.get(i))) {
                    // 保留最后一个Activity,便于启动Login界面
                } else {
                    activityStack.get(i).finish();
                }
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getContextObject() {
        return xContext;
    }

    //获取版本号
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }
}
