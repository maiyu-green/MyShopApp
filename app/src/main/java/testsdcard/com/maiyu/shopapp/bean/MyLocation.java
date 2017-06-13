package testsdcard.com.maiyu.shopapp.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**收货信息的bean类
 * Created by maiyu on 2017/4/10.
 */

public class MyLocation  implements Serializable , Comparable<MyLocation>{
    //用户id
    private Long id;
    //地址
    private String addr;
    //手机号码
    private String phone;
    //邮政编码
    private String zipCode;
    //用户名字
    private String consignee;
    //是否默认
    private Boolean isDefault;

    public MyLocation(){};

    public MyLocation(String addr, String phone, String zipCode, String consignee) {
        this.addr = addr;
        this.phone = phone;
        this.zipCode = zipCode;
        this.consignee = consignee;
    }

    public String getConsignee() {

        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public int compareTo(@NonNull MyLocation location) {

        if(location.getDefault() != null && this.getDefault() != null) {
            return location.getDefault().compareTo(this.getDefault());
        }
        return -1;
    }
}
