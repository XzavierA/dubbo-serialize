package com.github.xzaviera.dubbo.serialize.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Protostuff Serializer.
 *
 * @author gaolong.
 * @date 2018-05-28.
 */
class ProtostuffSerializer {

    /**
     * Serialize&Deserialize wrapper class.
     */
    private static final Class<ProtostuffSerializeWrapper> WRAPPER_CLASS = ProtostuffSerializeWrapper.class;

    /**
     * Serialize&Deserialize schema.
     */
    private static final Schema<ProtostuffSerializeWrapper> WRAPPER_SCHEMA = RuntimeSchema.createFrom(WRAPPER_CLASS);

    /**
     * Classes that protostuff cannot directly serialize&deserialize.
     */
    private static final Set<Class<?>> WRAPPER_SET = new HashSet<>();

    static {
        WRAPPER_SET.add(Long.class);
        WRAPPER_SET.add(Integer.class);
        WRAPPER_SET.add(BigDecimal.class);
        WRAPPER_SET.add(List.class);
        WRAPPER_SET.add(ArrayList.class);
        WRAPPER_SET.add(CopyOnWriteArrayList.class);
        WRAPPER_SET.add(LinkedList.class);
        WRAPPER_SET.add(Stack.class);
        WRAPPER_SET.add(Vector.class);

        WRAPPER_SET.add(Set.class);
        WRAPPER_SET.add(HashSet.class);
        WRAPPER_SET.add(TreeSet.class);

        WRAPPER_SET.add(Map.class);
        WRAPPER_SET.add(HashMap.class);
        WRAPPER_SET.add(TreeMap.class);
        WRAPPER_SET.add(Hashtable.class);
        WRAPPER_SET.add(SortedMap.class);

        WRAPPER_SET.add(Object.class);
    }

    /**
     * Serialize object into byte array.
     */
    @SuppressWarnings("unchecked")
    <T> byte[] serialize(T obj) {
        Schema schema;
        Object objectToSerialize;

        Class<T> clazz = (Class<T>) obj.getClass();
        if (!WRAPPER_SET.contains(clazz)) {
            schema = RuntimeSchema.getSchema(clazz);
            objectToSerialize = obj;
        } else {
            schema = WRAPPER_SCHEMA;
            objectToSerialize = new ProtostuffSerializeWrapper(obj);
        }
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return ProtostuffIOUtil.toByteArray(objectToSerialize, schema, buffer);
    }

    /**
     * Deserialize byte array into object.
     */
    <T> T deserialize(byte[] data, Class<T> clazz) {
        if (!WRAPPER_SET.contains(clazz)) {
            Schema<T> schema = RuntimeSchema.getSchema(clazz);
            T obj = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(data, obj, schema);
            return obj;
        } else {
            ProtostuffSerializeWrapper<T> wrapper = new ProtostuffSerializeWrapper<>();
            ProtostuffIOUtil.mergeFrom(data, wrapper, WRAPPER_SCHEMA);
            return wrapper.getData();
        }
    }
}
