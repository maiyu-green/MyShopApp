package testsdcard.com.maiyu.shopapp.utils;

import android.content.Context;
import android.text.TextUtils;

import testsdcard.com.maiyu.shopapp.bean.User;
import testsdcard.com.maiyu.shopapp.widget.Contants;


/**
 * 用户的当前数据类:包括User对象和token认证
 */
public class UserLocalData {


    /**
     * 存放User对象
     * @param context
     * @param user
     */
    public static void putUser(Context context,User user){

        //把User对象转化为String
        String user_json =  JSONUtil.toJSON(user);
        //根据Context存放该user_json到Contans.USER_JSON
        PreferencesUtils.putString(context, Contants.USER_JSON,user_json);

    }

    /**
     * 存放token
     * @param context
     * @param token
     */
    public static void putToken(Context context,String token){

        PreferencesUtils.putString(context, Contants.TOKEN,token);
    }


    /**
     * 获取User对象
     * @param context
     * @return
     */
    public static User getUser(Context context){

        //根据Context对象，获取Contants.USER_JSON
        String user_json= PreferencesUtils.getString(context,Contants.USER_JSON);
        if(!TextUtils.isEmpty(user_json)){

            //利用JSONUtil.fromJson()把字符串转化为user类
            return  JSONUtil.fromJson(user_json,User.class);
        }
        return  null;
    }


    /**
     * 获取token
     * @param context
     * @return
     */
    public static  String getToken(Context context){

        return  PreferencesUtils.getString( context,Contants.TOKEN);

    }


    /**
     * 清理存放的User对象
     * @param context
     */
    public static void clearUser(Context context){

        PreferencesUtils.putString(context, Contants.USER_JSON,"");

    }

    /**
     * 清理token
     * @param context
     */
    public static void clearToken(Context context){

        PreferencesUtils.putString(context, Contants.TOKEN,"");
    }


}
