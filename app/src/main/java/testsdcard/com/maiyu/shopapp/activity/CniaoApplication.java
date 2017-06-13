package testsdcard.com.maiyu.shopapp.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;

import testsdcard.com.maiyu.shopapp.bean.User;
import testsdcard.com.maiyu.shopapp.utils.UserLocalData;

/**自定义Application
 * Created by maiyu on 2017/2/27.
 */
public class CniaoApplication extends Application {

    //定义User类
    private User    user ;

    //创建CniaoApplicaiton的静态单例
    private static CniaoApplication mInstance;

    public static CniaoApplication getInstance(){
        return mInstance ;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化cinaApplication
        mInstance   =   this ;
        //初始化用户
        initUser();

        //初始化Fresco框架
        Fresco.initialize(this);
    }

    /**
     * 初始化用户
     */
    private void initUser() {
        //获取存储的User对象赋给this.user
        this.user   =   UserLocalData.getUser(this);
    }

    /**
     * 获取User对象
     * @return
     */
    public User getUser(){
        return user;
    }

    /**
     * 存入user和token
     * @param user
     * @param token
     */
    public void putUser(User user , String token){

        this.user = user ;
        UserLocalData.putUser(this , user);
        UserLocalData.putToken(this , token);
    }

    /**
     * 清除user和token
     */
    public void clearUser(){
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    /**
     * 获取token
     * @return
     */
    public String getToken(){
        return UserLocalData.getToken(this);
    }

    //定义intent对象
    private Intent  mIntent ;

    /**
     *存放intent
     * @param intent
     */
    public void putIntent(Intent intent){
        this.mIntent = intent;
    }

    /**
     * 获取intent
     * @return
     */
    public Intent getIntent(){
        return this.mIntent ;
    }

    /**
     * 跳转到当前的intent
     * @param context
     */
    public void jumpToTargetActivity(Context context){

        //启动context的intent
        context.startActivity(mIntent);
        //把当前intent赋空
        this.mIntent = null;
    }
}
