package com.github.xzaviera.dubbo.serialize.protostuff;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.Serialization;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Protostuff serialization which implements Dubbo interface.
 *
 * @author XzavierA.
 * @date 2018-11-27.
 */
public class ProtostuffSerialization implements Serialization {

    @Override
    public byte getContentTypeId() {
        return 11;
    }

    @Override
    public String getContentType() {
        return "x-application/Protostuff";
    }

    @Override
    public ObjectOutput serialize(URL url, OutputStream outputStream) {
        return new ProtostuffObjectOutput(outputStream);
    }

    @Override
    public ObjectInput deserialize(URL url, InputStream inputStream) {
        return new ProtostuffObjectInput(inputStream);
    }
}
