package testsdcard.com.maiyu.shopapp.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.city.XmlParserHandler;
import testsdcard.com.maiyu.shopapp.city.model.CityModel;
import testsdcard.com.maiyu.shopapp.city.model.DistrictModel;
import testsdcard.com.maiyu.shopapp.city.model.ProvinceModel;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;
import testsdcard.com.maiyu.shopapp.msg.BaseRespMsg;
import testsdcard.com.maiyu.shopapp.widget.ClearEditText;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;
import testsdcard.com.maiyu.shopapp.widget.Contants;

/**
 * Created by maiyu on 2017/4/10.
 */

/**
 * 新建收货地址类：
 * 采用picakerView来自定义实现省市县三级的选择
 */
public class CreateNewLocationActivity extends BaseActivity {


    //toolbar
    @ViewInject(R.id.toolbar)
    private CnToolbar cnToolbar;

    //收货人
    @ViewInject(R.id.edittxt_consignee)
    private ClearEditText mEtConsignee;

    //手机号码
    @ViewInject(R.id.edittxt_phone)
    private ClearEditText mEtPhone;

    //收货地址
    @ViewInject(R.id.txt_address)
    private TextView txtAddress;

    //增加
    @ViewInject(R.id.edittxt_add)
    private ClearEditText mEtAdd;

    //OptionsPickerView对象，用于选择三级省市区
    private OptionsPickerView mCityPikerView;

    //OkHtppHelper实例
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    //省级
    private List<ProvinceModel> mProvinces;
    //城市
    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
    //区域（镇，县等）
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);

        //初始化inject
        ViewUtils.inject(this);

        //初始化toolbar
        initToolbars();

        //初始化数据
        init();

    }


    /**
     * 初始化toolbars
     */
    private void initToolbars() {
        //设置取消监听
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置右边按钮监听
        cnToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建地址
                createAddress();
            }
        });

    }


    /**
     * 创建收货地址
     */
    private void createAddress() {

       // Log.d("CreateNewLocation" , "开始点击保存按钮");


        //获取收货人名字，手机号码，收货地址
        String consignee = mEtConsignee.getText().toString();
        String phone = mEtPhone.getText().toString();
        String address = txtAddress.getText().toString() + mEtAdd.getText().toString();


        //对应MyLocation类：分别存放:用户id,用收货人，手机号码，收货地址，邮政编码
        Map<String,Object> params = new HashMap<>(1);
        params.put("user_id",CniaoApplication.getInstance().getUser().getId());
        params.put("consignee",consignee);
        params.put("phone",phone);
        params.put("addr",address);
        params.put("zip_code","000000");

       // Log.d("CreateNewLocation" , "开始进行网络请求");
        //请求网络
        mHttpHelper.post(Contants.API.ADDRESS_CREATE, params, new SpotsCallBack<BaseRespMsg>(this) {


            //请求成功
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS){
                    setResult(RESULT_OK);
                    finish();

                    //Log.d("CreateNewLocation" , "保存成功");

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

                //Log.d("CreateNewLocation" , "保存失败" + e.toString());
                //Toast.makeText(CreateNewLocationActivity.this , "保存失败" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 三级城市按钮选择监听
     * @param view
     */
    @OnClick(R.id.ll_city_picker)
    public void showCityPickerView(View view){
        mCityPikerView.show();
    }

    /**
     * 初始化数据
     */
    private void init() {

        //初始化省级数据
        initProvinceDatas();

        mCityPikerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {

                String addresss = mProvinces.get(options1).getName() +"  "
                        + mCities.get(options1).get(option2)+"  "
                        + mDistricts.get(options1).get(option2).get(options3);

                txtAddress.setText(addresss);
            }
        })
                //设置标题
        .setTitleText("选择城市")
                //设置是否循环滚动
        .setCyclic(false , false ,false)
                //build()执行
        .build();
        mCityPikerView.setPicker((ArrayList) mProvinces,mCities,mDistricts);


        /**
         * 以下是2.x时的pickerView的使用
         */
        //创建省，市，县，选择器
   //     mCityPikerView = new OptionsPickerView(this);

//        //设置
//        mCityPikerView.setPicker((ArrayList) mProvinces,mCities,mDistricts,true);
//        mCityPikerView.setTitle("选择城市");
//        mCityPikerView.setCyclic(false,false,false);
        //mCityPikerView.setSelectOptions(1,1,1);
//        mCityPikerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3) {
//
//                String addresss = mProvinces.get(options1).getName() +"  "
//                        + mCities.get(options1).get(option2)+"  "
//                        + mDistricts.get(options1).get(option2).get(options3);
//
//                txtAddress.setText(addresss);
//
//            }
//        });


    }

    /**
     * 初始化省市县三级数据（全国）
     * 存放于：assets/province_data.xml,按照字母排序
     */
    protected void initProvinceDatas()
    {

        //获取AssetManager对象
        AssetManager asset = getAssets();
        try {
            //利用asset.open()创建输入流对象
            InputStream input = asset.open("province_data.xml");

            // (1)创建SAX解析器的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // (2)通过工厂对象来创建SAXParser对象
            SAXParser parser = spf.newSAXParser();
            //(3)自定义DefaultHandler对象
            XmlParserHandler handler = new XmlParserHandler();
            //(4)用SAXParser的parse(InputStream is , DefaultHandler handler)
            parser.parse(input, handler);
            //(5)关闭流
            input.close();
            // 获取解析出来的数据
            mProvinces = handler.getDataList();

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }

        //判断解析后获取的数据
        //判断省集合是否为空：
        if(mProvinces !=null){

            //遍历省集合的每个省份
            for (ProvinceModel p :mProvinces){

                //获取每个省份下面的所有城市
                List<CityModel> cities =  p.getCityList();
                //创建一个用于存放省份下面城市的ArrayList
                ArrayList<String> cityStrs = new ArrayList<>(cities.size()); //城市List

                //遍历城市集合，访问每个城市
                for (CityModel c :cities){

                    //把城市的名字添加到城市cityStrs
                    cityStrs.add(c.getName()); // 把城市名称放入 cityStrs

                    //
                    ArrayList<ArrayList<String>> dts = new ArrayList<>(); // 地区 List

                    //获取每个城市下面的所有镇区
                    List<DistrictModel> districts = c.getDistrictList();
                    //用于存储每个镇区的ArrayList
                    ArrayList<String> districtStrs = new ArrayList<>(districts.size());

                    //遍历每个镇区：添加到刚刚districtStrs
                    for (DistrictModel d : districts){
                        districtStrs.add(d.getName()); // 把城市名称放入 districtStrs
                    }
                    //把districtStrs添加到dts中
                    dts.add(districtStrs);

                    //把dts添加到
                    mDistricts.add(dts);
                }

                mCities.add(cityStrs); // 组装城市数据

            }
        }



    }

}
