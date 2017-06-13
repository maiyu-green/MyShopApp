package testsdcard.com.maiyu.shopapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.utils.ManifestUtil;
import testsdcard.com.maiyu.shopapp.utils.ToastUtils;
import testsdcard.com.maiyu.shopapp.widget.ClearEditText;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;

/**短信验证android支持智能验证，在mob平台后台上可以开启智能验证
 * 智能验证：智能验证是指您的手机号最近一次已经在Mob平台的SMSSDK验证过。
 * 此时就不再下发短信到该手机上，直接通过验证。当用户手机未通过智能验证时，
 * 则依然需要下发短信/语音验证码的方式验证手机号的有效性。
 如果你不想使用此功能，可以在短信的后台关闭，默认是打开。
 * Created by maiyu on 2017/2/23.
 */
public class RegisterActivity extends BaseActivity {

    //toolbar
    @ViewInject(R.id.toolbar)
    private CnToolbar cnToolbar ;

    //城市
    @ViewInject(R.id.txtCountry)
    private TextView txtCountry;

    //城市吗
    @ViewInject(R.id.txtCountryCode)
    private TextView txtCountryCode;

    //手机号码
    @ViewInject(R.id.edittxt_phone)
    private ClearEditText myPhone;

    //密码
    @ViewInject(R.id.edittxt_pwd)
    private ClearEditText myPwd;


    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    //SMSEventHandler
    private SMSEventHanlder evenHandler ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);


        //初始化toolbar
        initToolBar();

        //SMSSDK初始化
        //SMSSDK.initSDK(this, "1ca6bb352490a", "2c9e492f866b3b0ade265d6287716de5");
        SMSSDK.initSDK(this, ManifestUtil.getMetaDataValue(this, "mob_sms_appKey"),
                ManifestUtil.getMetaDataValue(this, "mob_sms_appSecrect"));


        //创建SMSEventHandler线程
        evenHandler = new SMSEventHanlder();
        //注册
        SMSSDK.registerEventHandler(evenHandler);


        ////设置
        String[] country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if(country != null){
            txtCountryCode.setText("+"+country[1]);
            txtCountry.setText(country[0]);
        }
        //SMSSDK.getSupportedCountries();

    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {

        cnToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取注册写的信息
                getCode();
            }
        });

    }

    /**
     * 获取注册写的信息，好进行下一步
     */
    private void getCode() {

        //手机号码，密码，城市吗
        String phone = myPhone.getText().toString().trim().replaceAll("\\s*" ,"");
        String pwd   = myPwd.getText().toString().trim();
        String code  = txtCountryCode.getText().toString().trim();

        //检查手机号码和区号
        checkPhoneNumber(phone , code);

        //请求获取短信验证码，在监听中返回
        //2.0.0以下版本第一个参数为手机号。getVoiceVerifyCode(String phone , String country)为了与发送文本保持一致。2.0.0与之后的改成第一个为国家代码。
        //getVoiceVerifyCode(String country，String phone)
        //请求语音验证码，在监听中返回
        //SMSSDK.getVerificationCode(phone , code);
        SMSSDK.getVerificationCode(code , phone);

    }

    /**
     * 检查手机号码的合法性
     * @param phone
     * @param code
     */
    private void checkPhoneNumber(String phone, String code) {

        if(code.startsWith("+")){
            code = code.substring(1);
        }

        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this , "请输入手机号码");
            return;
        }
        if(code == "86"){
            if(phone.length() != 11){
                ToastUtils.show(this , "手机号码位数不对");
                return;
            }
        }

        //利用正则表达式：
        //正则表达式--验证手机号码:1[3|5|7|8|]\d{9}
        //实现手机号前带86或是+86的情况:^((\+86)|(86))?(13)\d{9}$
        //电话号码与手机号码同时验证:(^(\d{3,4}-)?\d{7,8})$|(1[3|5|7|8]\d{9})
        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p   = Pattern.compile(rule);
        Matcher m   = p.matcher(phone);

        if(!m.matches()){
            ToastUtils.show(this , "您输入的手机号码格式不正确");
            return;
        }


    }


    /**
     * SMSEventHandler线程
     */
    class  SMSEventHanlder extends  EventHandler{


        @Override
        public void afterEvent(final int event, final int result, final Object data) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (result == SMSSDK.RESULT_COMPLETE) {

                        //回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功,跳转到填写验证码页面
                            afterVerticationCodeRequested((Boolean) data);

                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                            onCountryListGot((ArrayList< HashMap<String , Object>>) data);
                        }
                    } else {
                        ((Throwable) data).printStackTrace();

                        Throwable throwable = (Throwable)data;
                        try {
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String detas = object.optString("detail");
                            if(!TextUtils.isEmpty(detas)){
                                ToastUtils.show(RegisterActivity.this , detas);
                                return;
                            }
                        } catch (JSONException e) {
                            SMSLog.getInstance().w(e);
                            e.printStackTrace();
                        }
                    }


                }
            });
        }
    }

    /**
     * 返回国家列表
     * @param countries
     */
    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {

        for(HashMap<String, Object> country : countries){
            String code = (String)country.get("zone");
            String rule = (String)country.get("rule");

            if(TextUtils.isEmpty(rule)||TextUtils.isEmpty(code)){
                continue;
            }

            Log.d("RegisterActivity" , "code:"+code + " rule:" + rule);

        }


    }


    /**
     * 跳转到填写验证码页面
     * @param data
     */
    private void afterVerticationCodeRequested(Boolean data) {

        String phone = myPhone.getText().toString().trim().replaceAll("\\s*" , "");
        String pwd   = myPwd.getText().toString().trim();
        String code  = txtCountryCode.getText().toString().trim();

        if(code.startsWith("+")){
            code = code.substring(1);
        }

        Intent  intent = new Intent(this , RegisSecondActivity.class);
        intent.putExtra("phone" , phone);
        intent.putExtra("pwd" , pwd);
        intent.putExtra("countryCode" , code);
        startActivity(intent);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHandler);
    }
}














