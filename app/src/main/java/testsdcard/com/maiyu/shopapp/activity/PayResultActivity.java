package testsdcard.com.maiyu.shopapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import testsdcard.com.maiyu.shopapp.R;

/**支付后跳转的activity
 * Created by maiyu on 2017/4/5.
 */

public class PayResultActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
    }

    @Override
    public void onBackPressed() {

    }
}
