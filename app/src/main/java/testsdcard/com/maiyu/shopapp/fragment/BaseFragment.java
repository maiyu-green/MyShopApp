package testsdcard.com.maiyu.shopapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

import testsdcard.com.maiyu.shopapp.activity.CniaoApplication;
import testsdcard.com.maiyu.shopapp.activity.LoginActivity;
import testsdcard.com.maiyu.shopapp.bean.User;

/**
 * Created by maiyu on 2017/3/23.
 */

public abstract class BaseFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView(inflater , container , savedInstanceState);

        ViewUtils.inject(this , view);

        initToolBars();
        init();

        return view ;


    }

    public abstract void init();

    public void initToolBars() {

    }

    public abstract  View createView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState) ;


    public void startActivity(Intent intent , boolean isNeedLogin){

        if(isNeedLogin){

            User user = CniaoApplication.getInstance().getUser();
            if(user != null){
                super.startActivity(intent);
            }else {

                CniaoApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity() , LoginActivity.class);
                super.startActivity(loginIntent);
            }

        }else {
            super.startActivity(intent);
        }
    }

}
