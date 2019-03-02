package com.zc.marketdelivery.fragment;
/**
 * Created by 16957 on 2018/7/16.
 */
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;


import java.io.File;

import com.zc.marketdelivery.activity.UserLoginActivity;
import com.zc.marketdelivery.bean.Merchant;
import com.zc.marketdelivery.task.RequestForMerchantItemTask;
import com.zc.marketdelivery.task.RequestForMerchantListTask;
import com.zc.marketdelivery.utils.FileUtil;

import static com.zc.marketdelivery.utils.FileUtil.getRealFilePathFromUri;
import com.zc.marketdelivery.R;
import com.zc.marketdelivery.infoalter.*;
import com.zc.marketdelivery.BuildConfig;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends Fragment {
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    private Context mContext;
    private TextView tvUserLoginOrRegister;
    private TextView tvUserName;
    private TextView tvUserAddress;
    private TextView tvUserPhone;
    private TextView tvUserPassword;
    private ImageButton back;
    private TextView title;
    private CircleImageView headImage;
    //调用照相机返回图片文件
    private File tempFile;
    private int type;
    private TextView tv;


    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        bindViews(view);
        title.setText("个人中心");
        back.setVisibility(View.GONE);
        tvUserLoginOrRegister.setOnClickListener(new MyClickListener());
        headImage.setOnClickListener(new MyClickListener());
        tvUserName.setOnClickListener(new MyClickListener());
        tvUserAddress.setOnClickListener(new MyClickListener());
        tvUserPhone.setOnClickListener(new MyClickListener());
        tvUserPassword.setOnClickListener(new MyClickListener());
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        return view;
    }

    private void bindViews(View view) {
        back = (ImageButton) view.findViewById(R.id.personalback);
        title = (TextView) view.findViewById(R.id.titlename);
        tvUserLoginOrRegister = (TextView) view.findViewById(R.id.tv_user_login_or_register);
        headImage = (CircleImageView) view.findViewById(R.id.iv_headImage);
        tvUserName = (TextView) view.findViewById(R.id.tv_userName);
        tvUserAddress = (TextView) view.findViewById(R.id.tv_userAddress);
        tvUserPhone = (TextView) view.findViewById(R.id.tv_userPhone);
        tvUserPassword = (TextView) view.findViewById(R.id.tv_userPassword);
        tv= (TextView) view.findViewById(R.id.tv_test);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    String returnedData = intent.getStringExtra("data_return");
                    TextView textView=(TextView)getActivity().findViewById(R.id.user_name_text);
                    textView.setText(returnedData);
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    String returnedData = intent.getStringExtra("data_return");
                    TextView textView=(TextView)getActivity().findViewById(R.id.user_address_text);
                    textView.setText(returnedData);
                }
                break;
            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    String returnedData = intent.getStringExtra("data_return");
                    TextView textView=(TextView)getActivity().findViewById(R.id.user_phone_text);
                    textView.setText(returnedData);
                }
                break;
            case 4:
                if (resultCode == Activity.RESULT_OK) {
                    String returnedData = intent.getStringExtra("data_return");
                    TextView textView=(TextView)getActivity().findViewById(R.id.alter_user_pwd);
                    textView.setText(returnedData);
                }
                break;

            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == Activity.RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == Activity.RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(mContext.getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    headImage.setImageBitmap(bitMap);
                }
                break;
            default:
        }
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_user_login_or_register:
                    Intent intent = new Intent(mContext, UserLoginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_userName:
                    Intent intent1 = new Intent(mContext, Pensonal_NameAlter.class);
                    startActivityForResult(intent1,1);
                    break;
                case R.id.tv_userAddress:
                    Intent intent2 = new Intent(mContext, Pensonal_AddressAlter.class);
                    startActivityForResult(intent2,2);
                    break;
                case R.id.tv_userPhone:
                    Intent intent3 = new Intent(mContext,Pensonal_NumberAlter.class);
                    startActivityForResult(intent3,3);
                    break;
                case R.id.tv_userPassword:
//                    Intent intent4 = new Intent(mContext, Pensonal_PasswordAlter.class);
//                    startActivityForResult(intent4,4);
                    RequestForMerchantItemTask apiTask = new RequestForMerchantItemTask(mContext,tv );

                    String id = Integer.toString(2);
                    apiTask.execute(id);
                    break;
                case R.id.iv_headImage:
                    type = 1;
                    uploadHeadImage();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.activity_personal, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCamera();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            }
        }
    }


    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getContext(), ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

}


