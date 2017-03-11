package slzjandroid.slzjapplication.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.activity.CertificationTwoActivity;
import slzjandroid.slzjapplication.activity.photo.PhotoFile;
import slzjandroid.slzjapplication.customView.MenuPopupWindow;
import slzjandroid.slzjapplication.dto.EnterprisesRealnameRequest;
import slzjandroid.slzjapplication.dto.PicDomin;
import slzjandroid.slzjapplication.helper.SPLoginUser;
import slzjandroid.slzjapplication.utils.CommonUtils;
import slzjandroid.slzjapplication.utils.ToastUtils;

/**
 * 企业
 */
public class EnterpriseFragment extends BaseFragment implements View.OnClickListener {

    /*用来标识请求照相功能的activity*/
    private static final int CAMERA_WITH_DATA = 1001;
    /*用来标识请求gallery的activity*/
    private static final int PHOTO_PICKED_WITH_DATA = 1002;
    private static final int BACK_CODE = 2;
    private static final String KEY = "yingye";
    private Button btn_next;
    private Button btn_take_picture;
    private ScrollView layout;
    private MenuPopupWindow menu;
    public Bitmap bitMap;       //用来保存图片
    private PhotoFile photoFile;
    private ImageView show_iv;

    private EditText et_enterprisename;
    private EditText et_registerno;
    private EditText et_city;

    private String tag;
    private String picurl;
    private PicDomin picDomin;

    public EnterpriseFragment() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_enterprise;
    }

    @Override
    protected void findViews() {
        layout = (ScrollView) findViewById(R.id.layout);
        btn_next = (Button) findViewById(R.id.btn_next);
        et_enterprisename = (EditText) findViewById(R.id.et_enterprisename);

        et_registerno = (EditText) findViewById(R.id.et_registerno);
        et_city = (EditText) findViewById(R.id.et_city);

        btn_take_picture = (Button) findViewById(R.id.btn_take_picture);
        show_iv = (ImageView) findViewById(R.id.show_iv);

    }

    @Override
    protected void bindViews() {
        btn_next.setOnClickListener(this);
        btn_take_picture.setOnClickListener(this);

    }

    @Override
    protected void init() {
        photoFile = PhotoFile.getInstance();
        picDomin = new PicDomin();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                intentNextActivity();
                break;
            case R.id.btn_take_picture:
                showSliderMenu();
                break;
            case R.id.txt_done:
                doSelectImageFromLoacal();
                break;
            case R.id.txt_operate:
                doTakePhoto();
                break;
            case R.id.txt_cancle:
                menu.dismiss();
                break;
        }
    }


    private void showSliderMenu() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.poupwindow_layout, null);
        menu = new MenuPopupWindow(getActivity(), view, layout);
        TextView txt_done = (TextView) view.findViewById(R.id.txt_done);
        TextView txt_operate = (TextView) view.findViewById(R.id.txt_operate);
        TextView txt_cancle = (TextView) view.findViewById(R.id.txt_cancle);
        txt_done.setOnClickListener(this);
        txt_operate.setOnClickListener(this);
        txt_cancle.setOnClickListener(this);
    }

    /**
     * 从本地手机中选择图片
     */
    public void doSelectImageFromLoacal() {
        Intent localIntent = new Intent();
        localIntent.setType("image/*");
        localIntent.setAction("android.intent.action.GET_CONTENT");
        Intent intent = Intent.createChooser(localIntent, "选择图片");
        startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
    }

    /**
     * 拍照获取图片
     */
    protected void doTakePhoto() {
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "xingyun_yingye.jpg"));
            //指定照片保存路径（SD卡），workupload.jpg为一个临时文件，每次拍照后这个图片都会被替换
            if (photoFile.isOpenPermissionforCamera(getActivity())) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_WITH_DATA);
            } else {
                ToastUtils.showToast(getActivity(), "如果想拍照，您的打开您手机相机权限！");
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        picDomin = SPLoginUser.getPicUri(getActivity(), KEY);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (picDomin.getTag().equals("1")) {
                    Bitmap image = photoFile.getimage(picDomin.getPicUri());
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = image;
                    handler.sendMessage(msg);
                } else {
                    Bitmap bitmap = getPhotoForAlbum(picDomin.getPicUri());
                    Message msg = Message.obtain();
                    msg.what = 2;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Bitmap image = (Bitmap) msg.obj;
                if (null != image) {
                    show_iv.setImageBitmap(image);
                }
            } else {

                Bitmap image = (Bitmap) msg.obj;
                if (null != image) {
                    show_iv.setImageBitmap(image);
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        menu.dismiss();
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAMERA_WITH_DATA:
                if (bitMap != null && !bitMap.isRecycled()) {
                    bitMap.recycle();
                }
                picurl = Environment.getExternalStorageDirectory() + "/xingyun_yingye.jpg";
                bitMap = photoFile.getimage(picurl);
                tag = "1";
                picDomin.setPicUri(picurl);
                picDomin.setTag(tag);
                SPLoginUser.savePicUri(getActivity(), picDomin, KEY);
                show_iv.setImageBitmap(bitMap);
                break;
            case PHOTO_PICKED_WITH_DATA:
                if (bitMap != null && !bitMap.isRecycled()) {
                    bitMap.recycle();
                }
                picurl = data.getData().toString();
                try {
                    bitMap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(Uri.parse(picurl)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tag = "2";
                picDomin.setPicUri(picurl);
                picDomin.setTag(tag);
                SPLoginUser.savePicUri(getActivity(), picDomin, KEY);
                //下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。有关图片的处理将重新写文章来介绍。
                int mScale = photoFile.getScale(bitMap);
                bitMap = photoFile.PicZoom(bitMap, (int) (bitMap.getWidth() / mScale), (int) (bitMap.getHeight() / mScale));
                show_iv.setImageBitmap(bitMap);
                break;
            case BACK_CODE:
                getActivity().finish();
                break;
        }
    }


    private Bitmap getPhotoForAlbum(String picuri) {

        try {
            bitMap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(Uri.parse(picuri)));
            int mScale = photoFile.getScale(bitMap);
            bitMap = photoFile.PicZoom(bitMap, (int) (bitMap.getWidth() / mScale), (int) (bitMap.getHeight() / mScale));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitMap;

    }

    private void intentNextActivity() {
        EnterprisesRealnameRequest data = new EnterprisesRealnameRequest();
        String enterprisename = et_enterprisename.getText().toString().trim();
        String registerno = et_registerno.getText().toString().trim();
        String city = et_city.getText().toString().trim();
        if (!CommonUtils.hasText(enterprisename)) {
            ToastUtils.showToast(getActivity(), "企业名称不能为空！");
            return;
        }
        if (!CommonUtils.hasText(registerno)) {
            ToastUtils.showToast(getActivity(), "注册号不能为空！");
            return;
        }
        if (!CommonUtils.hasText(city)) {
            ToastUtils.showToast(getActivity(), "城市地址不能为空！");
            return;
        } else if (city.length() < 2) {
            ToastUtils.showToast(getActivity(), "输入城市地址必须大于2个字符！");
            return;
        }
        if (null == bitMap) {
            ToastUtils.showToast(getActivity(), "您还没有设置营业执照片了！");
            return;
        }

        data.setEnterpriseName(enterprisename);
        data.setRegisterNo(registerno);
        data.setCity(city);
        //营业执照照片
        // data.setLicencePic(bicMapBase64);

        //城市web端没有这个字段，待添加
        Intent intent = new Intent(getActivity(), CertificationTwoActivity.class);
        intent.putExtra("data", data);
        picDomin.setType("1");
        intent.putExtra("picData", picDomin);
        startActivityForResult(intent, 2);

    }
}
