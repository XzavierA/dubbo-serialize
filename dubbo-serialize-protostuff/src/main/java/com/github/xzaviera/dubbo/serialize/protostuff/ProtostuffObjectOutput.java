package com.github.xzaviera.dubbo.serialize.protostuff;

import com.alibaba.dubbo.common.serialize.ObjectOutput;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Deserializer.
 *
 * @author XzavierA.
 * @date 2018-11-27.
 */
public class ProtostuffObjectOutput implements ObjectOutput {

    private ProtostuffSerializer protostuffSerializer = new ProtostuffSerializer();

    private OutputStream outputStream;

    ProtostuffObjectOutput(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void writeObject(Object object) throws IOException {
        outputStream.write(serializeObject(object));
    }

    @Override
    public void writeBool(boolean bool) throws IOException {
        outputStream.write(bool ? 1 : 0);
    }

    @Override
    public void writeByte(byte b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void writeShort(short s) throws IOException {
        int temp = s;
        byte[] bytes = new byte[2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = new Integer(temp & 0xff).byteValue();
            temp = temp >> 8;
        }

        outputStream.write(bytes);
    }

    @Override
    public void writeInt(int i) throws IOException {
        byte[] bytes = serializeInt(i);

        outputStream.write(bytes);
    }

    private byte[] serializeInt(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (0xff & i);
        bytes[1] = (byte) ((0xff00 & i) >> 8);
        bytes[2] = (byte) ((0xff0000 & i) >> 16);
        bytes[3] = (byte) ((0xff000000 & i) >> 24);
        return bytes;
    }

    @Override
    public void writeLong(long l) throws IOException {
        byte[] bytes = new byte[8];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = new Long(l & 0xff).byteValue();
            l = l >> 8;
        }

        outputStream.write(bytes);
    }

    @Override
    public void writeFloat(float f) throws IOException {
        outputStream.write(serializeObject(f));
    }

    @Override
    public void writeDouble(double d) throws IOException {
        outputStream.write(serializeObject(d));
    }

    @Override
    public void writeUTF(String string) throws IOException {
        outputStream.write(serializeObject(string));
    }

    @Override
    public void writeBytes(byte[] bytes) throws IOException {
        if (bytes == null) {
            outputStream.write(serializeInt(-1));
        } else {
            writeBytes(bytes, 0, bytes.length);
        }
    }

    @Override
    public void writeBytes(byte[] bytes, int offset, int length) throws IOException {
        if (bytes == null) {
            outputStream.write(serializeInt(-1));
        } else {
            outputStream.write(serializeInt(length));
            outputStream.write(bytes, offset, length);
        }
    }

    @Override
    public void flushBuffer() throws IOException {
        outputStream.flush();
    }

    private byte[] serializeObject(Object object) {
        if (object == null) {
            return serializeInt(-1);
        }
        byte[] serializedArray = protostuffSerializer.serialize(object);
        byte[] serializedArrayLength = serializeInt(serializedArray.length);

        byte[] serializedArrayWithLength = new byte[serializedArrayLength.length + serializedArray.length];
        System.arraycopy(serializedArrayLength, 0, serializedArrayWithLength, 0, serializedArrayLength.length);
        System.arraycopy(serializedArray, 0, serializedArrayWithLength, serializedArrayLength.length, serializedArray.length);
        return serializedArrayWithLength;
    }
}
