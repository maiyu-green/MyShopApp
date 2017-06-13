package testsdcard.com.maiyu.shopapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;
import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.User;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;
import testsdcard.com.maiyu.shopapp.msg.LoginRespMsg;
import testsdcard.com.maiyu.shopapp.utils.CountTimerView;
import testsdcard.com.maiyu.shopapp.utils.ManifestUtil;
import testsdcard.com.maiyu.shopapp.utils.ToastUtils;
import testsdcard.com.maiyu.shopapp.widget.ClearEditText;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;
import testsdcard.com.maiyu.shopapp.widget.Contants;
import testsdcard.com.maiyu.shopapp.widget.DESUtil;

/**注册时的第二个Activity:获取并验证手机验证码
 * Created by maiyu on 2017/4/1.
 */

public class RegisSecondActivity extends BaseActivity {

    //toolbar
    @ViewInject(R.id.toolbar)
    private CnToolbar cnToolbar ;

    //用于显示已发送验证码的显示文本
    @ViewInject(R.id.txtTip)
    private TextView txtTip;

    //获取输入验证码
    @ViewInject(R.id.edittxt_code)
    private ClearEditText myCode;

    //发送按钮
    @ViewInject(R.id.btn_reSend)
    private Button btnResend;

    //手机号码
    private String phone;
    //密码
    private String pwd;
    //区号
    private String countryCode;

    //创建SMSEventHandler线程
    private SMSEventHanlder evenHandler ;

    //SpotsDialog对象
    private SpotsDialog dialog;

    //创建OkHttpHelper实例
    private OkHttpHelper okHttpHelper  = OkHttpHelper.getInstance();
    //定义CountTimerView对象
    private CountTimerView countTimerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_register);
        ViewUtils.inject(this);

        //初始化toolbar
        initToolbars();

        //分别获取手机号码，密码，区号
        phone   =   getIntent().getStringExtra("phone");
        pwd     =   getIntent().getStringExtra("pwd");
        countryCode =   getIntent().getStringExtra("countryCode");

        //格式化手机号码
        String  formatedPhone   =   "+" + countryCode + " " + splitPhoneNum(phone);

        //设置发送文本的显示
        String  text = getString(R.string.smssdk_send_mobile_detail) + formatedPhone ;
        txtTip.setText(Html.fromHtml(text));

        //时间计时器
        CountTimerView timerView = new CountTimerView(btnResend);
        timerView.start();

        //SMSSDK.initSDK(this, "1c37dae017700", "a1229fb6f4699552ac07d4336aab4b8a");

        //初始化短信sdk
        SMSSDK.initSDK(this, ManifestUtil.getMetaDataValue(this, "mob_sms_appKey"),
                ManifestUtil.getMetaDataValue(this, "mob_sms_appSecrect"));


        //创建短信发送线程
        evenHandler = new SMSEventHanlder();
        //利用SMSSDK注册线程
        SMSSDK.registerEventHandler(evenHandler);

        //窗口显示
        dialog  =   new SpotsDialog(this);
        dialog  =   new SpotsDialog(this , "正在校验验证码");

    }

    /**
     * toolbar初始化
     */
    private void initToolbars() {
        //设置提交监听
        cnToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCode();
            }
        });
    }

    /**
     * 重写发送验证码监听
     * @param view
     */
    @OnClick(R.id.btn_reSend)
    public void reSendCode(View view){

        //获取手机号码
        SMSSDK.getVerificationCode("+"+countryCode, phone);
        //时间计时器
        countTimerView  =   new CountTimerView(btnResend , R.string.smssdk_resend_identify_code);
        countTimerView.start();

        //窗口显示
        dialog.setMessage("正在重新获取验证码");
        dialog.show();

    }

    /**
     * 提交注册监听
     */
    private void submitCode() {

        //获取手机验证码
        String mCode = myCode.getText().toString().trim();

        //判空，如为空则填写手机号码
        if(TextUtils.isEmpty(mCode)){
            ToastUtils.show(this , R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countryCode , phone , mCode);
        dialog.show();
    }


    /**
     * 格式手机号码
     * @param phone
     * @return
     */
    private String splitPhoneNum(String phone) {

        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for(int i= 4 , len = builder.length() ; i < len ; i+=5){
            builder.insert(i , ' ');
        }
        builder.reverse();
        return builder.toString();

    }


    /**
     * 短信发送线程
     */
    class  SMSEventHanlder extends EventHandler {


        @Override
        public void afterEvent(final int event, final int result, final Object data) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //判断窗口
                    if(dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    if (result == SMSSDK.RESULT_COMPLETE) {

                        //回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功,跳转到填写验证码页面
                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();

                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                        }
                    } else {
                        ((Throwable) data).printStackTrace();

                        Throwable throwable = (Throwable)data;
                        try {
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String detas = object.optString("detail");
                            if(!TextUtils.isEmpty(detas)){
                                //ToastUtils.show(RegisterActivity.this , detas);
                                return;
                            }
                        } catch (JSONException e) {
                            SMSLog.getInstance().w(e);
                            //e.printStackTrace();
                        }
                    }


                }
            });
        }
    }

    /**
     * 填写验证码
     */
    private void doReg() {

        //存储手机号码和密码
        Map<String  , Object> params = new HashMap<>(2);
        params.put("phone" , phone);
        params.put("password" , DESUtil.encode(Contants.DES_KEY , pwd));

        okHttpHelper.post(Contants.API.REG, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                if(dialog != null && dialog.isShowing())
                    dialog.dismiss();

                //判断请状态
                if(userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR){
                    ToastUtils.show(RegisSecondActivity.this , "注册失败:" + userLoginRespMsg.getMessage());
                    return;
                }

                //获取Application实例
                CniaoApplication    application = CniaoApplication.getInstance();
                //存储d登录的ata和token
                application.putUser(userLoginRespMsg.getData() , userLoginRespMsg.getToken());

                //跳转到MainActivity
                startActivity(new Intent(RegisSecondActivity.this , MainActivity.class));
                finish();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //消耗SMSSDK注册的线程
        SMSSDK.unregisterEventHandler(evenHandler);
    }
}
