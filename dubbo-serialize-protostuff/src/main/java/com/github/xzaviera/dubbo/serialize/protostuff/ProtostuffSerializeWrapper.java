package com.github.xzaviera.dubbo.serialize.protostuff;

import java.io.Serializable;

/**
 * Serialize&Deserialize wrapper class.
 *
 * @author gaolong.
 * @date 2018-05-28.
 */
class ProtostuffSerializeWrapper<T> implements Serializable {

    private static final long serialVersionUID = 7172853930933465892L;

    private T data;

    ProtostuffSerializeWrapper() {

    }

    ProtostuffSerializeWrapper(T data) {
        this.data = data;
    }

    T getData() {
        return data;
    }

    void setData(T data) {
        this.data = data;
    }
}
