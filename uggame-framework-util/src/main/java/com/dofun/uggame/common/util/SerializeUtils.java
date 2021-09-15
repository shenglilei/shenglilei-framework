package com.dofun.uggame.common.util;

import java.io.*;

public class SerializeUtils {

    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    public static byte[] toBytes(Serializable obj) throws IOException {


        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        ) {
            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        }
    }


    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static Serializable toObj(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ) {
            Object o = objectInputStream.readObject();
            return (Serializable) o;
        }
    }
}
