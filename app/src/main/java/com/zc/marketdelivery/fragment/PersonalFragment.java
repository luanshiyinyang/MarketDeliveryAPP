package com.zc.marketdelivery.fragment;
/**
 * Created by 16957 on 2018/7/16.
 */
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import android.widget.Toast;


import java.io.File;
import java.io.IOException;

import com.zc.marketdelivery.activity.UserLoginActivity;
import com.zc.marketdelivery.bean.User;
import com.zc.marketdelivery.manager.UserStateManager;
import com.zc.marketdelivery.utils.FileUtil;

import static com.zc.marketdelivery.utils.FileUtil.getRealFilePathFromUri;
import com.zc.marketdelivery.R;
import com.zc.marketdelivery.infoalter.*;
import com.zc.marketdelivery.BuildConfig;
import com.zc.marketdelivery.utils.JsonUtil;
import com.zc.marketdelivery.utils.Md5Util;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private Button btnCancelLogin;
    //调用照相机返回图片文件
    private File tempFile;
    private int type;
    private View nowView;


    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        nowView = view;
        bindViews(view);
        initViews();
        title.setText("个人中心");
        back.setVisibility(View.GONE);
        tvUserLoginOrRegister.setOnClickListener(new MyClickListener());
        headImage.setOnClickListener(new MyClickListener());
        tvUserName.setOnClickListener(new MyClickListener());
        tvUserAddress.setOnClickListener(new MyClickListener());
        tvUserPhone.setOnClickListener(new MyClickListener());
        tvUserPassword.setOnClickListener(new MyClickListener());
        btnCancelLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (! new UserStateManager().getUserState()){
                    Toast.makeText(mContext, "未登录", Toast.LENGTH_SHORT).show();
                }
                else {
                    new UserStateManager().updateUserState("noLogin");
                    Toast.makeText(mContext, "注销成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        btnCancelLogin = (Button) view.findViewById(R.id.btn_cancel_login);
    }

    private void initViews(){
        new UserInfoTask().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    String returnedData = intent.getStringExtra("data_return");
                    TextView textView = (TextView)nowView.findViewById(R.id.tv_userName);
                    textView.setText(returnedData);
                    new updateUserInfoTask().execute("name", returnedData);

                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    String returnedData = intent.getStringExtra("data_return");
                    TextView textView=(TextView)getActivity().findViewById(R.id.tv_userAddress);
                    textView.setText(returnedData);
                    new updateUserInfoTask().execute("address", returnedData);
                }
                break;
            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    String returnedData = intent.getStringExtra("data_return");
                    TextView textView=(TextView)getActivity().findViewById(R.id.tv_userPhone);
                    textView.setText(returnedData);
                }
                break;
            case 4:
                if (resultCode == Activity.RESULT_OK) {
                    String returnedData = intent.getStringExtra("data_return");
                    TextView textView=(TextView)getActivity().findViewById(R.id.tv_userPassword);
                    textView.setText(returnedData);
                    new updateUserInfoTask().execute("password", returnedData);
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
                    Intent intent4 = new Intent(mContext, Pensonal_PasswordAlter.class);
                    startActivityForResult(intent4,4);
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


    private void uploadHeadImage() {
        /**
         * 上传头像
         */
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.fragment_personal, null);
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



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        /**
         * 外部存储权限申请返回
         */
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



    private void gotoPhoto() {
        /**
         * 跳转到相册
         */
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }



    private void gotoCamera() {
        /**
         * 跳转到照相机
         */
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


    public void gotoClipActivity(Uri uri) {
        /**
         * 打开截图界面
         */
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getContext(), ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    class UserInfoTask extends AsyncTask<String, String, User>{

        @Override
        protected User doInBackground(String... strings) {
            String baseUrl = "http://13.250.1.159:8000/api/users/";
            String userID = new UserStateManager().getUserID();
            if (userID.equals("0")){
                return null;
            }
            else {
                baseUrl += (userID + "/");
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(baseUrl).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        return JsonUtil.parseUserJsonObject(response.body() != null ? response.body().string() : null);
                    }
                    return null;

                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user == null){
                Toast.makeText(mContext, "请先登录或确认网络连接", Toast.LENGTH_SHORT).show();
            }
            else {
                tvUserName.setText(user.getName());
                StringBuilder stringBuilder = new StringBuilder(user.getPhone());
                stringBuilder.replace(4, 8, "****");
                tvUserPhone.setText(stringBuilder.toString());
            }
        }
    }

    class updateUserInfoTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            if (strings == null){
                return null;
            }
            String userID = new UserStateManager().getUserID();
            if (userID.equals("0")){
                return "请先登录";
            }

            // 更新用户名
            if (strings[0].equals("name")){
                try {
                    String baseUrl = "http://13.250.1.159:8000/api/users/" + userID + "/";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(baseUrl).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                         if (response.body() != null) {
                             User user =JsonUtil.parseUserJsonObject(response.body().string());
                             if (user != null) {
                                 user.setName(strings[1]);
                             }
                             else {
                                 return "输入用户名";
                             }
                             RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),JsonUtil.jsonUserToString(user));
                             Request request1 = new Request.Builder().url(baseUrl).patch(requestBody).build();
                             Response response1 = client.newCall(request1).execute();
                             if (response1.isSuccessful()){
                                 return "用户名更新成功";
                             }
                             else {
                                 return "更新失败";
                             }
                        }
                        else {
                            return "请求异常";
                         }
                    }
                    else {
                        return "网络异常";
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
            }
            // 更新密码
            if (strings[0].equals("password")){
                try {
                    String baseUrl = "http://13.250.1.159:8000/api/users/" + userID + "/";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(baseUrl).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        if (response.body() != null) {
                            User user =JsonUtil.parseUserJsonObject(response.body().string());
                            if (user != null) {
                                user.setPassword(Md5Util.md5(strings[1]));
                            }
                            else {
                                return "输入密码";
                            }
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),JsonUtil.jsonUserToString(user));
                            Request request1 = new Request.Builder().url(baseUrl).patch(requestBody).build();
                            Response response1 = client.newCall(request1).execute();
                            if (response1.isSuccessful()){
                                return "密码更新成功";
                            }
                            else {
                                return "更新失败";
                            }
                        }
                        else {
                            return "请求异常";
                        }
                    }
                    else {
                        return "网络异常";
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }

            }

            // 更新密码
            if (strings[0].equals("address")){
                try {
                    String baseUrl = "http://13.250.1.159:8000/api/users/" + userID + "/";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(baseUrl).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        if (response.body() != null) {
                            User user =JsonUtil.parseUserJsonObject(response.body().string());
                            if (user != null) {
                                user.setAddress(strings[1]);
                            }
                            else {
                                return "输入地址";
                            }
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),JsonUtil.jsonUserToString(user));
                            Request request1 = new Request.Builder().url(baseUrl).patch(requestBody).build();
                            Response response1 = client.newCall(request1).execute();
                            if (response1.isSuccessful()){
                                return "地址更新成功";
                            }
                            else {
                                return "更新失败";
                            }
                        }
                        else {
                            return "请求异常";
                        }
                    }
                    else {
                        return "网络异常";
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null){
                Toast.makeText(mContext, "数据传入异常", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

}


