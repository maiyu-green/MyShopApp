package testsdcard.com.maiyu.shopapp.bean;

import java.io.Serializable;

/**
 * Created by maiyu on 2017/6/13.
 */

public class OrderItem implements Serializable {



    private Long id;
    private Float amount;

    public Wares getWares() {
        return wares;
    }

    public void setWares(Wares wares) {
        this.wares = wares;
    }

    private Wares wares;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }


}
