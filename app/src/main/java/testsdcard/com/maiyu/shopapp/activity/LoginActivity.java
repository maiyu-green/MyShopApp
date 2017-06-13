package testsdcard.com.maiyu.shopapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.User;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;
import testsdcard.com.maiyu.shopapp.msg.LoginRespMsg;
import testsdcard.com.maiyu.shopapp.utils.ToastUtils;
import testsdcard.com.maiyu.shopapp.widget.ClearEditText;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;
import testsdcard.com.maiyu.shopapp.widget.Contants;
import testsdcard.com.maiyu.shopapp.widget.DESUtil;

/**登录Activity,
 * Created by maiyu on 2017/3/20.
 */

public class LoginActivity extends AppCompatActivity {

    //定义toolbar
    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar;

    //用户名
    @ViewInject(R.id.edt_user)
    private ClearEditText edtUser ;

    //密码
    @ViewInject(R.id.edt_password)
    private ClearEditText edtPswd;

    //登录按钮
    @ViewInject(R.id.btn_login)
    private Button btnLogin ;

    //注册
    @ViewInject(R.id.register_user)
    private TextView tvRegister;

    //忘记密码
    @ViewInject(R.id.forget_password)
    private TextView tvForgetPswrd;


    //创建OkHttpHelper的单例实例
    private OkHttpHelper    okHttpHelper    =   OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewUtils.inject(this); //必须初始化

        //初始化toolbar
        initToolBar();

    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {

        //设置返回监听
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });

    }

    /**
     * 按系统的返回键
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    /**
     * 对登录按钮进行监听
     * @param view
     */
    @OnClick(R.id.btn_login)
    public void login(View view){

        //获取输入的用户的手机号码
        String phone = edtUser.getText().toString().trim();

        //判断手机号码是否为空
        if(TextUtils.isEmpty(phone)){
            //设置震动
            edtUser.setShakeAnimation();
            ToastUtils.show(this , "用户名不能为空");
            return;
        }


        //判断密码是否为空
        String pwd = edtPswd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)){
            edtPswd.setShakeAnimation();
            ToastUtils.show(this , "密码不能为空");
            return;
        }



        //创建Map集合，存放输入的手机号码和密码，去登录菜鸟商城，
        Map<String , Object >   params  =   new HashMap<>(2);
        params.put("phone" , phone);
        //对密码进行加密编码
        params.put("password" , DESUtil.encode(Contants.DES_KEY, pwd));


        //okHttpHelper的post请求
        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            /**
             * 登录成功
             * @param response
             * @param userLoginRespMsg
             */
            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                //获取CniaoApplication的实例
                CniaoApplication    application =   CniaoApplication.getInstance();
                //存入数据和token到application中
                application.putUser(userLoginRespMsg.getData() , userLoginRespMsg.getToken());

                //判断application是否为空
                if(application.getIntent() == null){
                    Log.d("Login" , "getIntent ok");
                    //设置RESULT_OK
                    setResult(RESULT_OK);
                    finish();
                }else {

                    Log.d("Login" , "getIntent null");

                    //跳转到当前登录LoginActivity
                    application.jumpToTargetActivity(LoginActivity.this);
                    finish();
                    //ToastUtils.show(getApplication() , "用户名或密码出错");

                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

                //ToastUtils.show(getApplication()  , "访问出错");
            }


        });




//        //登录成功，跳转到MainActivity
//        ToastUtils.show(this , "登录成功");
//        Intent intent = new Intent(LoginActivity.this , MainActivity.class);
//        startActivity(intent);



    }


    /**
     * 注册按钮的监听
     * @param view
     */
    @OnClick(R.id.register_user)
    public void regist(View view){

        //跳转到注册Activity
        Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
        startActivity(intent);

    }
}
