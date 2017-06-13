package testsdcard.com.maiyu.shopapp.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.utils.ToastUtils;

/**自定义组合控件：购买数量选择器
 * Created by maiyu on 2017/3/8.
 */
public class NumberAddSubView extends LinearLayout implements View.OnClickListener{

    private Button mBtnAdd ;            //增加按钮
    private Button mBtnSun ;            //减少按钮
    private TextView mTextNum ;         //数量显示

    private int value ;                 //当前数量
    private int minValue ;              //最小限制数量
    private int maxValue ;              //最大限制数量

    private LayoutInflater  mInflater ;     //定义LayoutInflater

    private OnButtonClickListener mOnButtonClickListener ;      //定义监听器

    /**
     * 定义按钮监听器接口：用于在增加按钮或减少按钮触发时调用
     */
    public interface OnButtonClickListener{
        void onButtonAddClick(View view, int value);
        void onButtonSubClick(View view, int value);
    }

    /**
     * 设置按钮监听器
     * @param onButtonClickListener
     */
    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener){
        this.mOnButtonClickListener =   onButtonClickListener ;
    }

    //初始化上下文
    public NumberAddSubView(Context context) {
        this(context , null);   //调用本地方法
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);       //调用本地方法
    }

    /**
     * 初始化上下文
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取LayoutInflater对象
        mInflater   =   LayoutInflater.from(context);

        initView();     //初始化视图

        //获取布局数据
        if(attrs != null){      //判空
            //获取TintTypeArray数组
            TintTypedArray a    =   TintTypedArray.obtainStyledAttributes(context ,
                    attrs , R.styleable.NumberAddSubView , defStyleAttr , 0);

            //分别获取：当前数量，最小限制值，最大限制值，并设置（初始化）到当前页面中
            int val = a.getInt(R.styleable.NumberAddSubView_value , 0);
            setValue(val);
            int minVal = a.getInt(R.styleable.NumberAddSubView_minValue , 0);
            setMinValue(minVal);
            int maxVal = a.getInt(R.styleable.NumberAddSubView_maxValue , 0);
            setMaxValue(maxVal);

            //分别获取：增加，减少，文本显示的背景，并显示出来
            Drawable drawableBtnAdd =   a.getDrawable(R.styleable.NumberAddSubView_buttonAddBackgroud);
            Drawable drawableBtnSub =   a.getDrawable(R.styleable.NumberAddSubView_buttonSubBackgroud);
            Drawable drawableTextView = a.getDrawable(R.styleable.NumberAddSubView_editBackground);

            setTextViewBackground(drawableTextView);
            setBtnAddBackground(drawableBtnAdd);
            setBtnSubBackground(drawableBtnSub);


        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //获取view
        View view  = mInflater.inflate(R.layout.widget_number_add_sub , this , true);

        //根据view为各个控件绑定id
        mBtnAdd =   (Button)view.findViewById(R.id.btn_add);
        mBtnSun =   (Button)view.findViewById(R.id.btn_sub);
        mTextNum    =   (TextView)view.findViewById(R.id.etxt_view);

        //为增加和减少按钮设置监听
        mBtnAdd.setOnClickListener(this);
        mBtnSun.setOnClickListener(this);

    }


    /**
     * 按钮监听器触发的方法
     * @param v
     */
    @Override
    public void onClick(View v) {
        //判断是不是添加数量
        if(v.getId() == R.id.btn_add){
            numAdd();   //添加数量
            if(mOnButtonClickListener != null){
                //添加数量监听
                mOnButtonClickListener.onButtonAddClick(v , value);
            }
        }else  if(v.getId() == R.id.btn_sub){   //判断是不是减少数量
            numSub();       //减少数量
            if(mOnButtonClickListener != null){
                //减少数量监听
                mOnButtonClickListener.onButtonSubClick(v , value);
            }
        }

    }

    /**
     * 减少商品数量
     */
    private void numSub() {
        //判断是否已达到最小值（空）
        if(value > minValue){
            value-- ;       //减少数量
        }
        //并显示出来
        mTextNum.setText(value + "");
    }

    /**
     * 增加商品数量
     */
    private void numAdd() {
        //判断当前数量是否也达到最大限制值
        if(value < maxValue){
            value++ ;       //增加数量
        }else {
            ToastUtils.show(getContext() , "已经达到最大数量了");
        }
        //显示当前值
        mTextNum.setText(value + "");
    }

    /**
     * 设置最小商品数量
     * @param minVal
     */
    public void setMinValue(int minVal) {
        this.minValue   =   minVal ;
    }

    /**
     * 设置当前数量，并显示出来
     * @param val
     */

    public void setValue(int val) {
        mTextNum.setText(val + "");
        this.value = val ;
    }
    /**
     * 设置最大商品数量
     * @param maxValue
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * 设置显示数量的文本的背景
     * @param textViewBackground
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setTextViewBackground(Drawable textViewBackground) {
         mTextNum.setBackground(textViewBackground);
    }

    /**
     * 设置显示增加商品数量的按钮的背景
     * @param btnAddBackground
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBtnAddBackground(Drawable btnAddBackground) {
        this.mBtnAdd.setBackground(btnAddBackground);
    }

    /**
     * 设置减少数量的按钮的背景
     * @param btnSubBackground
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBtnSubBackground(Drawable btnSubBackground) {
        this.mBtnSun.setBackground(btnSubBackground) ;
    }

    /**
     * 获取值
     * @return
     */
    public int getValue() {

        //获取当前数量
        String val =  mTextNum.getText().toString();
        //判空，
        if(val !=null && !"".equals(val)) {
            //初始化显示
            this.value = Integer.parseInt(val);
        }
        return value;
    }

    /**
     * 获取最小值
     * @return
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * 获取最大值
     * @return
     */
    public int getMaxValue() {
        return maxValue;
    }
}
