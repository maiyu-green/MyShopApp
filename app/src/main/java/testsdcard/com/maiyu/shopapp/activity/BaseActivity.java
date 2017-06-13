package testsdcard.com.maiyu.shopapp.activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import testsdcard.com.maiyu.shopapp.bean.User;

/**
 * BaseActivity:继承AppCompatActivity
 */

public class BaseActivity extends AppCompatActivity {


    /**
     * 启动inent
     * @param intent
     * @param isNeedLogin
     */
    public void startActivity(Intent intent,boolean isNeedLogin){


        //判断是否需要登录
        if(isNeedLogin){

            //获取登录用户
            User user =CniaoApplication.getInstance().getUser();
            if(user !=null){
                //已经登录的用户就启动intent
                super.startActivity(intent);
            }
            else{

                //若用户为空，把当前intent存入实例栈中，然后跳转到LoginActivity,
                CniaoApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this
                        , LoginActivity.class);
                super.startActivity(loginIntent);

            }

        }
        else{
            super.startActivity(intent);
        }

    }
}

