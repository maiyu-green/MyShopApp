package testsdcard.com.maiyu.shopapp.http;

import android.content.Context;
import android.content.Intent;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.activity.CniaoApplication;
import testsdcard.com.maiyu.shopapp.activity.LoginActivity;
import testsdcard.com.maiyu.shopapp.utils.ToastUtils;

/**
 * Created by maiyu on 2017/3/24.
 */

public abstract class SimpleCallback<T> extends BaseCallback <T>{

    protected Context mContext ;

    public SimpleCallback(Context context){
        mContext    =   context;
    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext , mContext.getString(R.string.token_error));

        Intent  intent  =   new Intent();
        intent.setClass(mContext , LoginActivity.class);
        mContext.startActivity(intent);

        CniaoApplication.getInstance().clearUser();
    }
}
