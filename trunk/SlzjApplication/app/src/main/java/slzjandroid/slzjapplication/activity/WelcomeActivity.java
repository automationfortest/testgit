package slzjandroid.slzjapplication.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.db.CityDBManager;
import slzjandroid.slzjapplication.db.UserDBManage;
import slzjandroid.slzjapplication.dto.CityResponse;
import slzjandroid.slzjapplication.dto.CityResult;
import slzjandroid.slzjapplication.dto.EnterpriseInfo;
import slzjandroid.slzjapplication.dto.Hotvalue;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.TokenRefresh;
import slzjandroid.slzjapplication.dto.Value;
import slzjandroid.slzjapplication.dto.VersionResponse;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

public class WelcomeActivity extends BasicActivity {
    private Handler _handler = new Handler();
    private LoginUser loginUser;
    private ArrayList<Hotvalue> hotvalues = new ArrayList<>();
    private ArrayList<Value> cityValues = new ArrayList<>();
    private UserDBManage manager;

    private static final int DOWN_ERROR = 100;
    private static final int UPDATA_CLIENT = 200;
    private static final int GET_UNDATAINFO_ERROR = 300;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void findViews() {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void init() {
        manager = new UserDBManage(WelcomeActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showToast(WelcomeActivity.this, "钱包行云需要GPS权限来打车定位");
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showToast(WelcomeActivity.this, "钱包行云需要网络权限");
        }
        //得到ActionBar
        AppContext.getInstance().addActivity(this);
        updataVersion();
    }

    @Override
    protected void bindViews() {

    }

    private void startNextActivity() {
        String token = SPLoginUser.getToken(WelcomeActivity.this);
        if (CommonUtils.hasText(token) && !token.equals("0")) {
            loginUser = LoginUser.getUser();
        }
        if (loginUser == null || !CommonUtils.hasText(loginUser.getAccessToken())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    WelcomeActivity.this.finish();
                }
            }, 800);

        } else {
            tokenRefresh(loginUser, token);
        }
    }

    /**
     * token刷新
     */
    private void tokenRefresh(final LoginUser user, String token) {
        ServiceProvider.tokenService.tokenRefresh(token, new HashMap<String, String>())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<TokenRefresh>() {
                               @Override
                               public void call(TokenRefresh tokenRefresh) {
                                   String status = tokenRefresh.getStatus();
                                   if (status.equals("200")) {
                                       TokenRefresh.Result result = tokenRefresh.getResult();
                                       EnterpriseInfo enterpriseInfo = result.getEnterpriseInfo();

                                       String accessToken = result.getUsrToken().getAccessToken();
                                       SPLoginUser.saveTokenTo(WelcomeActivity.this, accessToken);

                                       if (result.getUsrType().equals("1")) {
                                           user.setAccessToken(accessToken);
                                       } else {
                                           user.setBalance(result.getBalance());
                                           user.setLoginName(result.getUsrName());
                                           user.setAccessToken(accessToken);
                                           user.setEnterpriseInfo(LoginUser.getEnterpriseInfo(enterpriseInfo));
                                       }
                                       getCityList();
                                   } else if (status.equals("401")) {
                                       //缺失是否存在订单判断，存在订单直接进入订单栏，否则进入登录
                                       startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                                       WelcomeActivity.this.finish();
                                   }
                                   LoginUser.saveUserToDB(user, WelcomeActivity.this);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {

                               }
                           }
                );
    }

    /**
     * 获取城市列表
     */
    private void getCityList() {
        String token = SPLoginUser.getToken(WelcomeActivity.this);
        try {
            ServiceProvider.cityService.getCity(token).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<CityResponse>() {
                        @Override
                        public void call(CityResponse cityResponse) {
                            if (cityResponse.getStatus().equals("200")) {
                                for (int i = 0; i < cityResponse.getResult().size(); i++) {

                                    CityResult cityResult = cityResponse.getResult().get(i);
                                    if (cityResult.getHotvalue() != null) {
                                        hotvalues.addAll(cityResult.getHotvalue());
                                    }
                                    if (cityResult.getValue() != null) {
                                        for (int j = 0; j < cityResult.getValue().size(); j++) {
                                            Value value = cityResult.getValue().get(j);
                                            value.setPinyin(cityResult.getLetter());
                                            cityValues.add(value);
                                        }
                                    }
                                }
                                try {
                                    setCityData();
                                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                                    WelcomeActivity.this.finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtils.showToast(WelcomeActivity.this, "初始化数据失败，请重新启动");
                                }
                            } else {
                                ToastUtils.showToast(WelcomeActivity.this, "初始化数据失败，请重新启动");
                            }


                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            ToastUtils.showToast(WelcomeActivity.this, "初始化数据失败，请重新启动");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(WelcomeActivity.this, "初始化数据失败，请重新启动");
        } finally {

        }

    }

    private void setCityData() {
        CityDBManager cityDbManager = new CityDBManager(WelcomeActivity.this);
        cityDbManager.deleteDB();
        cityDbManager.addHotcity(hotvalues);
        cityDbManager.add(cityValues);
        cityDbManager.closeDB();
    }


    /**
     * 获取版本更新
     */
    private void updataVersion() {
        String token = SPLoginUser.getToken(WelcomeActivity.this);
        Map<String, String> par = new HashMap<>();
        par.put("type", "1");
        ServiceProvider.updataVersionService.getVersion(token, par).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VersionResponse>() {
                    @Override
                    public void call(VersionResponse versionResponse) {

                        String status = versionResponse.getStatus();
                        VersionResponse.Result result = versionResponse.getResult();
                        if (status.equals("200")) {
                            if (isUpdataVersion(result)) {
                                showUpdateDialog(result);
                            } else {
                                intentActvity();
                            }
                        } else if (status.equals("401")) {
                            //缺失是否存在订单判断，存在订单直接进入订单栏，否则进入登录
                            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                            WelcomeActivity.this.finish();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        intentActvity();
                    }
                });
    }


    /**
     * 判断是否要升级
     *
     * @param par
     */
    private boolean isUpdataVersion(VersionResponse.Result par) {
        int versionCode = Integer.valueOf(par.getVersionCode());
        int currentVersion = AppContext.getVersionCode(this);
        if (currentVersion != 0 && currentVersion < versionCode) {
            return true;
        }
        return false;
    }


    /**
     * 弹出更新的对话框
     *
     * @param par
     */
    private void showUpdateDialog(final VersionResponse.Result par) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this)
                .setPositiveButton("立刻跟新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            downLoadApk(par.getUrl());
                        } catch (Exception e) {

                        }
                    }
                })
                .setTitle(getString(R.string.msg_app_upgrade_title) + "\t" + par.getVersionName());
        String message = CommonUtils.getSplitString(par.getDescription());
        builder.setMessage(message);
        if (par.getIsFlag().equals(0)) {
            builder.setNegativeButton(R.string.upgrade_later,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            intentActvity();
                        }
                    });

        }
        builder.setCancelable(false);
        builder.create().show();

    }


    private void intentActvity() {
        String tag = SPLoginUser.getTag(this);
        if (tag.equals("0")) {
            SPLoginUser.setTag(this, "1");
            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                    WelcomeActivity.this.finish();
                }
            }, 800);

        } else {
            startNextActivity();
        }
    }

    /**
     * 从服务器中下载APK
     */
    protected void downLoadApk(final String uri) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressNumberFormat("%1d KB/%2d KB");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(uri, pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(50000);
            //获取到文件的大小
            pd.setMax((int) conn.getContentLength() / 1024);
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "app-release.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress((int) total / 1024);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_CLIENT:
                    //对话框通知用户升级程序
                    VersionResponse.Result par = (VersionResponse.Result) msg.obj;
                    showUpdateDialog(par);
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    ToastUtils.showToast(WelcomeActivity.this, "获取服务器更新信息失败");
                    startNextActivity();
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    ToastUtils.showToast(WelcomeActivity.this, "下载新版本失败");
                    startNextActivity();
                    break;
            }
        }
    };


    //安装apk
    public void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");//编者按：此处Android应为android，否则造成安装不了
        startActivity(intent);
    }
}
