package testsdcard.com.maiyu.shopapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import testsdcard.com.maiyu.shopapp.R;

/**自定义类ClearEditText继承自EditText,主要为最右边的删除按钮添加监听事件
 * 和设置密码，用户名错误时的动画效果
 * Created by maiyu on 2017/3/20.
 */

@SuppressLint("AppCompatCustomView")
public class ClearEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable ;       //定义删除图标
    private boolean hasFocus ;              //判断控件是否聚焦

    public ClearEditText(Context context) {
        this(context , null);
    }


    public ClearEditText(Context context, AttributeSet attrs) {
        //在这里不加这个属性，不加这个很多属性不能在XML里面定义
        this(context, attrs , android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();         //在这里初始化
    }

    /**
     * 初始化
     */
    private void init() {

        //获取EditText的DrawableRight,假设没有则设置，我们就默认使用的图片
        mClearDrawable  =   getCompoundDrawables()[2];

        if(mClearDrawable == null){

            mClearDrawable  =   getResources().getDrawable(R.drawable.icon_delete_32);
        }

        //设置位置大小
        mClearDrawable.setBounds(0 , 0, mClearDrawable.getIntrinsicWidth() , mClearDrawable.getIntrinsicHeight());
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);


    }

    /**
     * 设置清除图标的隐藏于显示
     * @param visible
     */
    private void setClearIconVisible(boolean visible) {
        //根据visible获取右图标
        Drawable    right = visible ? mClearDrawable : null;
        //设置右图标
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1] ,
                right ,getCompoundDrawables()[3]);

    }


    /**
     * 当ClearEditText焦点发生改变时，判断里面字符串长度并设置清除图标的显示与隐藏
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus   =   hasFocus ;
        //判断是否控件聚焦，是则根据长度判断是否隐藏删除图标
        if(hasFocus){
            setClearIconVisible(getText().length() > 0);
        }else {
            setClearIconVisible(false);
        }
    }


    /**
     * 在输入框里面内容改变前回调的方法
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 当输入框里面内容发生改变时回调的方法
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(hasFocus){
            setClearIconVisible(s.length() > 0);
        }

    }

    /**
     * 在输入框里面内容改变后回调的方法
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置震动动画
     */
    public void setShakeAnimation(){
        this.setAnimation(shakeAnimation(5));
    }

    /**
     * 设置动画相关属性
     * @param counts
     * @return
     */
    private Animation shakeAnimation(int counts) {
        //获取动画
        Animation   translateAnimation  =   new TranslateAnimation(0 , 10 , 0 ,0);
        //设置震动的次数
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        //设置震动时长
        translateAnimation.setDuration(1000);
        //返回动画
        return translateAnimation;
    }


    /**
     * 在这里对删除图标进行监听处理：
     *
     *  因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距
     * 之间我们就算点击了图标，竖直方向就没有考虑
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //判断触摸动作
        if(event.getAction() == MotionEvent.ACTION_UP){
            //判断
            if(getCompoundDrawables()[2] != null){

                //判断是否触发了删除图标
                boolean touchable   =   event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < (getWidth() - getPaddingRight()));
                if(touchable){
                    this.setText("");       //设空，即为删除效果
                }
            }
        }

        return super.onTouchEvent(event);
    }
}
