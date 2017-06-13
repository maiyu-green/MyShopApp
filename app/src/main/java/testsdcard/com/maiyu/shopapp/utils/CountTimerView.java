package testsdcard.com.maiyu.shopapp.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import testsdcard.com.maiyu.shopapp.R;

/**
 * Created by maiyu on 2017/4/1.
 */

public class CountTimerView extends CountDownTimer {

    public static final int TIME_COUNT = 61000;
    private TextView btn;
    private int endStrRid;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountTimerView(long millisInFuture, long countDownInterval , TextView btn , int endStrRid) {
        super(millisInFuture, countDownInterval);
        this.btn = btn ;
        this.endStrRid = endStrRid;
    }

    public CountTimerView(TextView btn , int endStrRid){
        super(TIME_COUNT ,1000);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }
    public CountTimerView(TextView btn) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStrRid = R.string.smssdk_resend_identify_code;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setEnabled(false);
        btn.setText(millisUntilFinished/1000 + "秒后可重新发送");
    }

    @Override
    public void onFinish() {
        btn.setText(endStrRid);
        btn.setEnabled(true);

    }
}
