package slzjandroid.slzjapplication.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.photo.PhotoFile;
import slzjandroid.slzjapplication.customView.LoadingDialog;
import slzjandroid.slzjapplication.customView.MenuPopupWindow;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.customView.YesOrNoDialog;
import slzjandroid.slzjapplication.dto.EnterpriseInfo;
import slzjandroid.slzjapplication.dto.EnterprisesRealnameRequest;
import slzjandroid.slzjapplication.dto.EnterprisesRealnameResponse;
import slzjandroid.slzjapplication.dto.LoginUser;
import slzjandroid.slzjapplication.dto.PicDomin;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.service.ServiceProvider;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.DateUtil;
import slzjandroid.slzjapplication.utils.DialogUtil;
import slzjandroid.slzjapplication.utils.NumbersUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

public class CertificationTwoActivity extends BasicActivity implements View.OnClickListener, NavigationView.ClickCallback {

    /*用来标识请求照相功能的activity*/
    private static final int CAMERA_WITH_DATA = 1001;
    /*用来标识请求gallery的activity*/
    private static final int PHOTO_PICKED_WITH_DATA = 1002;

    /*用来标识请求照相功能的activity*/
    private static final int CAMERA_WITH_DATA_TWO = 1003;
    /*用来标识请求gallery的activity*/
    private static final int PHOTO_PICKED_WITH_DATA_TWO = 1004;
    private LinearLayout layout;
    private EnterprisesRealnameRequest data;

    private PhotoFile photoFile;

    private TextView tv_info_title;
    private TextView tv_chose_city;
    private EditText et_agentidcard;

    private ImageView im_show_pic_one;
    private ImageView im_show_pic_two;

    private Bitmap gbitmap;
    private Bitmap wbitmap;

    private MenuPopupWindow menu;
    private PicDomin picData;

    private boolean tag;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_certificationtwo;
    }

    @Override
    protected void findViews() {

        tv_info_title = (TextView) findViewById(R.id.tv_info_title);
        tv_chose_city = (TextView) findViewById(R.id.tv_chose_city);
        et_agentidcard = (EditText) findViewById(R.id.et_agentidcard);

        im_show_pic_one = (ImageView) findViewById(R.id.im_show_pic_one);
        im_show_pic_two = (ImageView) findViewById(R.id.im_show_pic_two);

        layout = (LinearLayout) findViewById(R.id.layout);

    }

    @Override
    protected void init() {
        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("实名认证");
        navigationView.setRightViewIsShow(false);
        navigationView.setClickCallback(this);
        photoFile = PhotoFile.getInstance();

        data = (EnterprisesRealnameRequest) getIntent().getSerializableExtra("data");

        picData = (PicDomin) getIntent().getSerializableExtra("picData");
        String tag = picData.getTag();

        String uriPath = picData.getPicUri();
        if (tag.equals("1")) {
            Bitmap licencePic2 = getBitmapforCrmera(uriPath);
            String bicMapBase64 = photoFile.bitmapToBase64(licencePic2);
            data.setLicencePic(bicMapBase64);
        } else {
            Uri uri = Uri.parse(uriPath);
            if (null != uri) {
                Bitmap licencePic1 = getLicencePic1(uri);
                String bicMapBase64 = photoFile.bitmapToBase64(licencePic1);
                data.setLicencePic(bicMapBase64);
            }
        }
        if (CommonUtils.hasText(data.getEnterpriseName())) {
            tv_info_title.setText(this.data.getEnterpriseName());
        }
        if (CommonUtils.hasText(data.getCity())) {
            tv_chose_city.setText(this.data.getCity());
        }


    }

    @Override
    protected void bindViews() {
        findViewById(R.id.btn_take_picture_one).setOnClickListener(this);
        findViewById(R.id.btn_take_picture_two).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_picture_one:
                showSliderMenu(true);
                break;
            case R.id.btn_take_picture_two:
                showSliderMenu(false);
                break;
            case R.id.btn_commit:
                if (isPost()) {
                    postData();
                }
                break;
            case R.id.txt_done:
                if (tag) {
                    doSelectImageFromLoacal(PHOTO_PICKED_WITH_DATA);
                } else {
                    doSelectImageFromLoacal(PHOTO_PICKED_WITH_DATA_TWO);
                }
                break;
            case R.id.txt_operate:
                if (tag) {
                    doTakePhoto(CAMERA_WITH_DATA, "xingyun_scsf_pic_one.jpg");
                } else {
                    doTakePhoto(CAMERA_WITH_DATA_TWO, "xingyun_scsf_pic_two.jpg");
                }
                break;
            case R.id.txt_cancle:
                menu.dismiss();
            default:
                break;
        }
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    /**
     * 拍照获取图片
     */
    public void doTakePhoto(int flag, String imageName) {
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageName));
            //指定照片保存路径（SD卡），workupload.jpg为一个临时文件，每次拍照后这个图片都会被替换
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, flag);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地手机中选择图片
     */
    public void doSelectImageFromLoacal(int flag) {
        Intent localIntent = new Intent();
        localIntent.setType("image/*");
        localIntent.setAction("android.intent.action.GET_CONTENT");
        Intent intent = Intent.createChooser(localIntent, "选择图片");
        startActivityForResult(intent, flag);
    }

    /**
     * 显示底部菜单
     */
    private void showSliderMenu(boolean flag) {
        tag = flag;
        SPLoginUser.saveRenZheng(this, tag);

        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.poupwindow_layout, null);
        menu = new MenuPopupWindow(this, view, layout);

        TextView txt_done = (TextView) view.findViewById(R.id.txt_done);
        TextView txt_operate = (TextView) view.findViewById(R.id.txt_operate);
        TextView txt_cancle = (TextView) view.findViewById(R.id.txt_cancle);
        txt_done.setOnClickListener(this);
        txt_operate.setOnClickListener(this);
        txt_cancle.setOnClickListener(this);
    }

    private boolean isPost() {
        String agentidCard = et_agentidcard.getText().toString().trim();
        if (!CommonUtils.hasText(agentidCard)) {
            ToastUtils.showToast(this, "经办人的身份账号不能为空！");
            return false;
        } else {
            if (!NumbersUtils.isIdNumber(agentidCard)) {
                ToastUtils.showToast(this, "输入的身份账号格式不合法！");
                return false;
            }
        }
        if (null == gbitmap) {
            ToastUtils.showToast(this, "经办人手持身份证照片不能为空");
            return false;
        }
        if (null == wbitmap) {
            ToastUtils.showToast(this, "经办人授权委托书照片不能为空！");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent datas) {
        super.onActivityResult(requestCode, resultCode, datas);
        menu.dismiss();
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAMERA_WITH_DATA:
                //经办人身份证照片
                if (null != gbitmap && !gbitmap.isRecycled()) {
                    gbitmap.recycle();
                }
                gbitmap = getBitmapforCrmera(Environment.getExternalStorageDirectory() + "/xingyun_scsf_pic_one.jpg");
                String picBase64 = photoFile.bitmapToBase64(gbitmap);
                data.setIdCardPic(picBase64);
                im_show_pic_one.setImageBitmap(gbitmap);
                break;
            case CAMERA_WITH_DATA_TWO:
                //经办人身份证照片
                if (null != wbitmap && !wbitmap.isRecycled()) {
                    wbitmap.recycle();
                }
                wbitmap = getBitmapforCrmera(Environment.getExternalStorageDirectory() + "/xingyun_scsf_pic_two.jpg");
                String picBase = photoFile.bitmapToBase64(wbitmap);
                data.setProxyPic(picBase);
                im_show_pic_two.setImageBitmap(wbitmap);
                break;
            case PHOTO_PICKED_WITH_DATA:
                if (null != gbitmap && !gbitmap.isRecycled()) {
                    gbitmap.recycle();
                }
                gbitmap = getBitmapforPicked(gbitmap, datas);
                String pic64 = photoFile.bitmapToBase64(gbitmap);
                data.setIdCardPic(pic64);
                im_show_pic_one.setImageBitmap(gbitmap);
                break;

            case PHOTO_PICKED_WITH_DATA_TWO:
                if (null != wbitmap && !wbitmap.isRecycled()) {
                    wbitmap.recycle();
                }
                wbitmap = getBitmapforPicked(wbitmap, datas);
                String bitmap_64 = photoFile.bitmapToBase64(wbitmap);
                data.setProxyPic(bitmap_64);
                im_show_pic_two.setImageBitmap(wbitmap);
                break;
        }
    }


    /**
     * 获取bitmap图片
     *
     * @param url
     * @return
     */
    private Bitmap getBitmapforCrmera(String url) {
        return photoFile.getimage(url);
    }


    /**
     * 获取bitmap图片
     *
     * @return
     */
    private Bitmap getBitmapforPicked(Bitmap bitmap, Intent data) {

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        Uri selectedImageUri = data.getData();
        if (selectedImageUri != null) {
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。有关图片的处理将重新写文章来介绍。
            int mScale = photoFile.getScale(bitmap);
            bitmap = photoFile.PicZoom(bitmap, (int) (bitmap.getWidth() / mScale), (int) (bitmap.getHeight() / mScale));
        }
        return bitmap;
    }


    /**
     * 上传数据
     */
    private void postData() {
        String agentidCard = et_agentidcard.getText().toString().trim();
        data.setAgentIdCard(agentidCard);

        LoginUser user = LoginUser.getUser();
        EnterpriseInfo enterpriseInfo = user.getEnterpriseInfo();
        Map<String, String> parmes = new HashMap<>();

        parmes.put("enterpriseIdx", enterpriseInfo.getEnterpriseIdx());
        parmes.put("enterpriseName", data.getEnterpriseName());
        parmes.put("enterpriseType", picData.getType());
        parmes.put("registerNo", data.getRegisterNo());
        parmes.put("agentName", user.getLoginName());
        parmes.put("agentIdCard", data.getAgentIdCard());
        parmes.put("licencePic", data.getLicencePic());
        parmes.put("idCardPic", data.getIdCardPic());
        parmes.put("proxyPic", data.getProxyPic());
        parmes.put("submitDate", DateUtil.getCurrentTime(DateUtil.YMDHMS_STYLE_TOW));

        final LoadingDialog dialog = DialogUtil.getLoadingDialog(this, "正在上传数据...");
        dialog.show();
        ServiceProvider.enterpriseInfoService.postEnterprisesRealname(user.getAccessToken(), parmes).
                observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Action1<EnterprisesRealnameResponse>() {
                    @Override
                    public void call(EnterprisesRealnameResponse response) {
                        dialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            ToastUtils.showToast(CertificationTwoActivity.this, response.getMessage());
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            showDialog("您好：", response.getMessage());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dialog.dismiss();
                        ToastUtils.showToast(CertificationTwoActivity.this, "添加失败");
                    }
                });
    }

    private void showDialog(String title, String message) {
        YesOrNoDialog yesOrNoDialog = new YesOrNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString(YesOrNoDialog.TITLE, title);
        bundle.putString(YesOrNoDialog.MESSAGE, message);
        yesOrNoDialog.setArguments(bundle);
        yesOrNoDialog.setOnClick(new YesOrNoDialog.OnClick() {
                                     @Override
                                     public void onPositive() {
                                         setResult(RESULT_OK);
                                         finish();
                                     }
                                 }

        );
        yesOrNoDialog.show(CertificationTwoActivity.this.getSupportFragmentManager(), "yesOrNoDialog");
    }

    private Bitmap getLicencePic1(Uri selectedImageUri) {
        if (selectedImageUri != null) {
            Bitmap bitMap = null;
            try {
                bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。有关图片的处理将重新写文章来介绍。
            int mScale = photoFile.getScale(bitMap);
            return photoFile.PicZoom(bitMap, (int) (bitMap.getWidth() / mScale), (int) (bitMap.getHeight() / mScale));
        }
        return null;
    }
}
