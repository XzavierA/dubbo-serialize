package com.github.xzaviera.dubbo.serialize.protostuff;

import com.alibaba.dubbo.common.serialize.ObjectInput;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Serializer.
 *
 * @author XzavierA.
 * @date 2018-11-27.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class ProtostuffObjectInput implements ObjectInput {

    private ProtostuffSerializer protostuffSerializer = new ProtostuffSerializer();

    private InputStream inputStream;

    ProtostuffObjectInput(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public Object readObject() throws IOException {
        return readObject(Object.class);
    }

    @Override
    public <T> T readObject(Class<T> clazz) throws IOException {
        return deserializeObject(clazz);
    }

    @Override
    public <T> T readObject(Class<T> clazz, Type type) throws IOException {
        return readObject(clazz);
    }

    @Override
    public boolean readBool() throws IOException {
        int read = inputStream.read();
        return read == 1;
    }

    @Override
    public byte readByte() throws IOException {
        return (byte) inputStream.read();
    }

    @Override
    public short readShort() throws IOException {
        byte[] bytes = new byte[2];
        inputStream.read(bytes);

        short s0 = (short) (bytes[0] & 0xff);
        short s1 = (short) (bytes[1] & 0xff);
        s1 <<= 8;
        return (short) (s0 | s1);
    }

    @Override
    public int readInt() throws IOException {
        byte[] bytes = new byte[4];
        inputStream.read(bytes);

        int i = bytes[0] & 0xFF;
        i |= ((bytes[1] << 8) & 0xFF00);
        i |= ((bytes[2] << 16) & 0xFF0000);
        i |= ((bytes[3] << 24) & 0xFF000000);
        return i;
    }

    @Override
    public long readLong() throws IOException {
        byte[] bytes = new byte[8];
        inputStream.read(bytes);

        long s0 = bytes[0] & 0xff;
        long s1 = bytes[1] & 0xff;
        long s2 = bytes[2] & 0xff;
        long s3 = bytes[3] & 0xff;
        long s4 = bytes[4] & 0xff;
        long s5 = bytes[5] & 0xff;
        long s6 = bytes[6] & 0xff;
        long s7 = bytes[7] & 0xff;
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        return s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
    }

    @Override
    public float readFloat() throws IOException {
        return readObject(Float.class);
    }

    @Override
    public double readDouble() throws IOException {
        return readObject(Double.class);
    }

    @Override
    public String readUTF() throws IOException {
        return readObject(String.class);
    }

    @Override
    public byte[] readBytes() throws IOException {
        int length = readInt();
        if (length < 0) {
            return null;
        } else if (length == 0) {
            return new byte[]{};
        } else {
            byte[] bytes = new byte[length];
            inputStream.read(bytes);
            return bytes;
        }
    }

    private <T> T deserializeObject(Class<T> clazz) throws IOException {
        int length = readInt();
        if (length < 0) {
            return null;
        }
        byte[] bytes = new byte[length];
        inputStream.read(bytes);
        return protostuffSerializer.deserialize(bytes, clazz);
    }
}
