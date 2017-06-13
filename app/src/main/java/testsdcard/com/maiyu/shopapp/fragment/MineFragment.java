package testsdcard.com.maiyu.shopapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.activity.AddMyLocationActivity;
import testsdcard.com.maiyu.shopapp.activity.CniaoApplication;
import testsdcard.com.maiyu.shopapp.activity.LoginActivity;
import testsdcard.com.maiyu.shopapp.activity.MyFavoriteActivity;
import testsdcard.com.maiyu.shopapp.activity.MyOrderActivity;
import testsdcard.com.maiyu.shopapp.bean.User;
import testsdcard.com.maiyu.shopapp.widget.Contants;

/**个人设置fragment
 * Created by maiyu on 2017/2/23.
 */
public class MineFragment extends BaseFragment {

    @ViewInject(R.id.user_img)
    private CircleImageView mUserImg;

    @ViewInject(R.id.txt_login)
    private TextView txtLogin;

    @ViewInject(R.id.mine_exit)
    private Button mBtnExit;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine , container , false);
    }


    @Override
    public void init() {

        //初始化用户
        User    user    = CniaoApplication.getInstance().getUser();
        showUser(user);
    }

    /**
     * 显示用户信息
     * @param user
     */
    private void showUser(User user) {

        if(user != null){

            if(!TextUtils.isEmpty(user.getLogo_url()))
                showUserImg(user.getLogo_url());

            txtLogin.setText(user.getUsername());
            mBtnExit.setVisibility(View.VISIBLE);
        }else {
            txtLogin.setText(R.string.login);
            mUserImg.setImageResource(R.drawable.default_head);
            mBtnExit.setVisibility(View.GONE);
        }
    }

    /**
     * 显示图片
     * @param logo_url
     */
    private void showUserImg(String logo_url) {
        Picasso.with(getActivity()).load(logo_url).into(mUserImg);
    }


    /**
     * 登录监听
     * @param view
     */
    @OnClick(value = {R.id.txt_login , R.id.user_img})
    public void Login(View view){

        if(CniaoApplication.getInstance().getUser() == null){

            Intent  intent  =   new Intent(getContext() , LoginActivity.class);
            startActivityForResult(intent , Contants.REQUEST_CODE);
        }

    }

    /**
     * 退出登录监听
     * @param view
     */
    @OnClick(R.id.mine_exit)
    public void exitLogin(View view){

        CniaoApplication.getInstance().clearUser();
        showUser(null);
    }


    /**
     * 登陆后的回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        User user = CniaoApplication.getInstance().getUser();
        showUser(user);
        //super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 收货地址
     * @param view
     */
    @OnClick(R.id.txt_my_location)
    public void addMyLocation(View view){

        startActivity(new Intent(getActivity(),AddMyLocationActivity.class) , true);
    }

    @OnClick(R.id.txt_my_orders)
    public void toMyOrderActivity(View view){
        startActivity(new Intent(getActivity() , MyOrderActivity.class) , true);
    }
    @OnClick(R.id.txt_my_collect)
    public void toMyFavoriteActivity(View view){
        startActivity(new Intent(getActivity() , MyFavoriteActivity.class) , true);
    }
}
