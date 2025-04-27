package com.dofun.shenglilei.framework.redis.service;

import org.springframework.data.redis.connection.DataType;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/9/17
 * Time:19:29
 */
public interface RedisService {
    boolean savePrefix(String prefix, String key, Object value);

    boolean savePrefix(String prefix, String key, Object value, Long ttl);

    boolean savePrefix(String prefix, String key, Map<String, Object> value);

    boolean savePrefix(String prefix, String key, Map<String, Object> value, Long ttl);

    int removePrefix(String prefix);

    void remove(String... keys);

    boolean remove(String key);

    boolean exists(String key);

    String get(String key);

    <T> T getObject(String key, Class<T> type);

    String get(String key, String defaultValue);

    boolean set(String key, Set<Object> value);

    boolean set(String key, List<Object> value);

    boolean setNX(String key, Object value);

    boolean set(String key, Object value, Long expireTime);

    void setNull(String key);

    void set(String key, Object value);

    void set(String key, Object value, long time);

    void set(String key, Object value, long time, TimeUnit timeUnit);

    void delete(String key);

    void delete(Set<String> key);

    void deleteByPattern(String pattern);

    Set<String> keys(String key);

    long getExpire(String key);


    DataType type(String key);

    void expire(String key, long time, TimeUnit timeUnit);

    boolean expireAt(String key, Date date);

    void multiSet(Map<String, String> maps);

    void multiSetObject(Map<String, Object> maps);

    Long incr(String key);


    Long incr(String key, Long incValue);

    <T> T getSet(String key, T value, Class<T> type);

    <T> Long leftPush(String key, T value);

    <T> Long leftPushAll(String key, Collection<T> values);

    <T> T leftPop(String key, Class<T> type);

    <T> T rightPop(String key, Class<T> type, long timeout, TimeUnit unit);


    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key
     * @param field
     * @return
     */
    Object hGet(String key, String field);

    /**
     * 获取所有给定字段的值
     *
     * @param key
     * @return
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * 获取所有给定字段的值
     *
     * @param key
     * @param fields
     * @return
     */
    List<Object> hMultiGet(String key, Collection<Object> fields);

    void hPut(String key, String hashKey, String value);

    void hPutAll(String key, Map<String, String> maps);

    /**
     * 删除一个或多个哈希表字段
     *
     * @param key
     * @param fields
     * @return
     */
    Long hDelete(String key, Object... fields);

    /**
     * 查看哈希表 key 中，指定的字段是否存在
     *
     * @param key
     * @param field
     * @return
     */
    boolean hExists(String key, String field);

    /**
     * 查询list结构内，元素的数量值
     */
    long sizeForList(String key);
}
