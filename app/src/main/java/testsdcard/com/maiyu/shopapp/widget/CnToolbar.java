package testsdcard.com.maiyu.shopapp.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import testsdcard.com.maiyu.shopapp.R;


/**
 * 继承Toolbar的类
 */
public class CnToolbar extends Toolbar {

    private LayoutInflater mInflater;       //获取布局

    private View mView;                     //View对象
    private TextView mTextTitle;            //显示文本信息
    private EditText mSearchView;           //编辑是否出现搜索框
    private Button mRightButton;            //右边按钮


    /**
     * 构造函数
     * @param context
     */
    public CnToolbar(Context context) {
        this(context,null);
    }

    public CnToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CnToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        initView();     //初始化布局
        setContentInsetsRelative(10,10);        //设置布局大小


        /**
         * 判断布局是否为空
         */
        if(attrs !=null) {
            //分别获取R.styleable.中的标题
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CnToolbar, defStyleAttr, 0);

            //获取R.styleable中的右边按钮
            final Drawable rightIcon = a.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
            if (rightIcon != null) {
                //setNavigationIcon(navIcon);
                setRightButtonIcon(rightIcon);
            }

            //设置搜索图片是否隐藏起来
            boolean isShowSearchView = a.getBoolean(R.styleable.CnToolbar_showSearchView,false);
            if(isShowSearchView){

                showSearchView();
                hideTitleView();

            }

            CharSequence rightButtonText = a.getText(R.styleable.CnToolbar_rightButtonText);
            if(rightButtonText != null){
                setRightButtonText(rightButtonText);
            }

            a.recycle();        //回收资源
        }

    }

    /**
     * 初始化布局
     */
    private void initView() {

        //判断当前View是否为空
        if(mView == null) {
            //获取布局对象
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);

            //为当前view对象绑定ID
            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);

            //设置布局参数
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            //为当前VIEW设置布局参数
            addView(mView, lp);
        }

    }

    /**
     * 根据int参数设置右边按钮图片
     * @param icon
     */
    public void  setRightButtonIcon(int icon){

        setRightButtonIcon(getResources().getDrawable(icon));
    }

    /**
     * 根据Drawable参数设置右边按钮图片
     * @param icon
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void  setRightButtonIcon(Drawable icon){

        if(mRightButton !=null){
            //设置右边按钮图片，并显示出来
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }

    }

    /**
     * 为右边按钮设置监听
     * @param li
     */
    public  void setRightButtonOnClickListener(OnClickListener li){

        mRightButton.setOnClickListener(li);
    }

    /**
     * 设置右边按钮文字
     * @param text
     */
    public void setRightButtonText(CharSequence text){
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    /**
     * 为右边按钮文字设置监听
     * @param id
     */
    public void setRightButtonText(int id){
        setRightButtonText(getResources().getString(id));
    }

    /**
     * 获取右边按钮，返回Button对象
     * @return
     */
    public Button getRightButton(){

        return this.mRightButton;
    }


    /**
     * 根据Int类型的resId设置标题
     * @param resId
     */
    @Override
    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    /**
     * 根据CharSequence类型设置标题
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {

        initView();     //初始化布局
        if(mTextTitle !=null) {
            mTextTitle.setText(title);
            showTitleView();        //显示标题
        }

    }

    /**
     * 显示搜索图标
     */
    public  void showSearchView(){

        if(mSearchView !=null)
            mSearchView.setVisibility(VISIBLE);

    }

    /**
     * 隐藏搜索图标
     */
    public void hideSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(GONE);
    }

    /**
     * 显示搜索标题
     */
    public void showTitleView(){
        if(mTextTitle !=null)
            mTextTitle.setVisibility(VISIBLE);
    }

    /**
     * 隐藏搜索标题
     */
    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }

}
