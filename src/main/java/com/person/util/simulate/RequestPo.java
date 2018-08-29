package com.person.util.simulate;

import java.io.Serializable;

/**
 * Created by huangchangling on 2018/8/28.
 */
public class RequestPo implements Serializable {
    private Long productId;
    private Long destId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getDestId() {
        return destId;
    }

    public void setDestId(Long destId) {
        this.destId = destId;
    }
}
