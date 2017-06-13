package testsdcard.com.maiyu.shopapp.http;


import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 一个抽象回调类：定义了基本的回调方法
 * @param <T>
 */
public abstract class BaseCallback <T> {


    //定义Type类型
    public   Type mType;

    /**
     * 把Type转换成对应的类型
     * @param subclass
     * @return
     */
    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    /**
     * 构造时获取type的class对象
     */
    public BaseCallback()
    {
        mType = getSuperclassTypeParameter(getClass());
    }


    /**
     * 请求之前回调的接口
     * @param request
     */
    public  abstract void onBeforeRequest(Request request);

    /**
     * 请求失败的回调
     * @param request
     * @param e
     */
    public abstract  void onFailure(Request request, Exception e) ;


    /**
     *请求成功时调用此方法
     * @param response
     */
    public abstract  void onResponse(Response response);

    /**
     *
     * 状态码大于200，小于300 时调用此方法
     * @param response
     * @param t
     * @throws IOException
     */
    public abstract void onSuccess(Response response,T t) ;

    /**
     * 状态码400，404，403，500等时调用此方法
     * @param response
     * @param code
     * @param e
     */
    public abstract void onError(Response response, int code,Exception e) ;

    /**
     * 验证失败：token为401,402,403时调用此方法
     * @param response
     * @param code
     */
    public abstract void onTokenError(Response response, int code) ;

}
