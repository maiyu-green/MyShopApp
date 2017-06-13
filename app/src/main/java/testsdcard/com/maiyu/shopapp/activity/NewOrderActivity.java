package testsdcard.com.maiyu.shopapp.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import testsdcard.com.maiyu.shopapp.R;

/**
 * 新订单Activity
 * Created by maiyu on 2017/3/28.
 */

public class NewOrderActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_test);
    }
}
