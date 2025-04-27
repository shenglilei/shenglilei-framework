package com.dofun.shenglilei.framework.redis.service.impl;

import com.dofun.shenglilei.framework.common.utils.JniInvokeUtils;
import com.dofun.shenglilei.framework.redis.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/9/17
 * Time:19:29
 */
@Slf4j
public class RedisServiceImpl implements RedisService {
    private static final String CACHE_PREFIX_KEY = "prefix";
    /**
     * key 最大长度
     */
    private static final int MAXLENGTH = 20;
    private final ObjectMapper mapper;
    private final RedisTemplate<String, Object> redisTemplate;
    /***针对空值的缓存设置一个过期时间***/
    @Value("${cache.nullValueExpire:10}")
    private long nullValueExpire;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.mapper = objectMapper;
    }

    public long getNullValueExpire() {
        return nullValueExpire;
    }

    /**
     * 构建redis key值
     *
     * @param keys 多个key
     * @return key
     */
    private String buildRedisKey(String... keys) {
        if (keys == null) {
            throw new IllegalArgumentException("keys 不能为空.");
        }
        int length = keys.length;
        if (length == 0) {
            throw new IllegalArgumentException("keys 不能为空.");
        }
        if (length == 1) {
            return keys[0];
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(keys[i]);
            if (i != length - 1) {
                builder.append(":");
            }
        }
        return builder.toString();
    }

    /**
     * 保存前缀到sorted set
     *
     * @param prefix prefix
     * @param key    key
     * @return false=操作失败，true=操作成功
     */
    private boolean savePrefix2Set(String prefix, String key, long score) {
        if (StringUtils.hasLength(prefix)) {
            throw new IllegalArgumentException("prefix 不能为空.");
        }
        if (StringUtils.hasLength(key)) {
            throw new IllegalArgumentException("key 不能为空.");
        }
        String redisKey = buildRedisKey(prefix, key);
        //将expireAtTime作为score，不设置过期时间的，取0
        Boolean isAddSuccess = redisTemplate.opsForZSet().add(buildRedisKey(CACHE_PREFIX_KEY, prefix), redisKey, score >= 0 ? JniInvokeUtils.currentTimeMillis() + score : -1);
        return isAddSuccess != null && isAddSuccess;
    }


    /**
     * 按照前缀保存kv，使用String，不设置有效期
     *
     * @param prefix 前缀
     * @param key    key
     * @param value  value
     * @return false=操作失败，true=操作成功
     */
    @Override
    public boolean savePrefix(String prefix, String key, Object value) {
        if (!savePrefix2Set(prefix, key, -1)) {
            return false;
        }
        redisTemplate.opsForValue().set(buildRedisKey(prefix, key), value == null ? "" : value.toString());
        return true;
    }

    /**
     * 按照前缀保存kv，使用String
     *
     * @param prefix 前缀
     * @param key    key
     * @param value  value
     * @param ttl    ttl 单位为毫秒
     * @return false=操作失败，true=操作成功
     */
    @Override
    public boolean savePrefix(String prefix, String key, Object value, Long ttl) {
        if (ttl == null || ttl <= 0) {
            return savePrefix(prefix, key, value);
        }
        if (!savePrefix2Set(prefix, key, ttl)) {
            return false;
        }
        redisTemplate.opsForValue().set(buildRedisKey(prefix, key), value == null ? "" : value.toString(), ttl, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * 按照前缀保存kv，使用Hash，不设置过期时间
     *
     * @param prefix 前缀
     * @param key    key
     * @param value  value
     * @return false=操作失败，true=操作成功
     */
    @Override
    public boolean savePrefix(String prefix, String key, Map<String, Object> value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value 不能为空.");
        }
        if (!savePrefix2Set(prefix, key, -1)) {
            return false;
        }
        redisTemplate.opsForHash().putAll(buildRedisKey(prefix, key), value);
        return true;
    }

    /**
     * 按照前缀保存kv，使用Hash
     *
     * @param prefix 前缀
     * @param key    key
     * @param value  value
     * @return false=操作失败，true=操作成功
     */
    @Override
    public boolean savePrefix(String prefix, String key, Map<String, Object> value, Long ttl) {
        if (ttl == null || ttl <= 0) {
            return savePrefix(prefix, key, value);
        }
        if (!savePrefix2Set(prefix, key, ttl)) {
            return false;
        }
        String finalKey = buildRedisKey(prefix, key);
        redisTemplate.opsForHash().putAll(finalKey, value);
        Boolean isExpireSuccess = redisTemplate.expire(finalKey, ttl, TimeUnit.MILLISECONDS);
        return isExpireSuccess != null && isExpireSuccess;
    }

    /**
     * 删除指定前缀的所有数据
     *
     * @param prefix 前缀
     * @return 成功删除的数据条数
     */
    @Override
    public int removePrefix(String prefix) {
        if (StringUtils.hasLength(prefix)) {
            throw new IllegalArgumentException("prefix 不能为空.");
        }
        log.info("按前缀删除，prefix：{}", prefix);
        long maxEndIndex = Long.MAX_VALUE;
        long startIndex = -1;
        long length = 100;
        String zKey = buildRedisKey(CACHE_PREFIX_KEY, prefix);
        int removeCount = 0;
        Set<Object> set = redisTemplate.opsForZSet().rangeByScore(zKey, startIndex, maxEndIndex, 0, length);
        Set<String> keys;
        String key;
        String[] keysArray;
        while (set != null && !set.isEmpty()) {
            keys = new HashSet<>(set.size());
            for (Object obj : set) {
                if (obj != null) {
                    key = obj.toString();
                    keys.add(key);
                    redisTemplate.delete(key);
                    removeCount++;
                }
            }
            if (!keys.isEmpty()) {
                keysArray = new String[keys.size()];
                keysArray = keys.toArray(keysArray);
                redisTemplate.opsForZSet().remove(zKey, (Object[]) keysArray);
                log.info("按前缀删除，prefix：{}，影响到的数据条数：{}，数据项为：{}", prefix, set.size(), set.toString());
            }
            set = redisTemplate.opsForZSet().rangeByScore(zKey, startIndex, maxEndIndex, 0, length);
        }
        log.info("按前缀删除，prefix：{}，影响到的总数据条数：{}", prefix, removeCount);
        return removeCount;
    }


    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    @Override
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     * @return true=操作成功，false=操作失败
     */
    @Override
    public boolean remove(final String key) {
        if (StringUtils.hasLength(key)) {
            return false;
        }
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {

            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 判断缓存中是否有对应的value
     */
    @Override
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     */
    @Override
    @SuppressWarnings("all")
    public String get(final String key) {
        Object result;
        try {
            result = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {

            log.error(e.getMessage(), e);
            return null;
        }
        return result == null ? null : result.toString();
    }

    @Override
    public <T> T getObject(String key, Class<T> type) {
        Object result;
        result = redisTemplate.opsForValue().get(key);
        if (result == null) {
            return null;
        }
        String expectClass = type.toString();
        String actualClass = JniInvokeUtils.getClass(result).toString();
        if (!expectClass.equals(actualClass)) {
            log.error("class not match.expect class:" + expectClass + ",actualClass:" + actualClass);
            return null;
        }
        try {
            return this.mapper.readValue(this.mapper.writeValueAsBytes(result), type);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


    @Override
    public String get(final String key, String defaultValue) {
        Object result;
        try {
            result = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {

            log.error(e.getMessage(), e);
            return defaultValue;
        }
        return Optional.ofNullable(result).orElse(defaultValue).toString();
    }

    @Override
    public boolean set(String key, Set<Object> value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet = new HashSet<>();
        for (Object object : value) {
            if (object != null) {
                typedTupleSet.add(new DefaultTypedTuple<>(object, 0.0));
            }
        }
        try {
            Long ret = redisTemplate.opsForZSet().add(key, typedTupleSet);
            return ret != null && ret > 0;
        } catch (Exception e) {

            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean set(String key, List<Object> value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            Long ret = redisTemplate.opsForList().rightPushAll(key, value);
            return ret != null && ret > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


    @Override
    public boolean setNX(String key, Object value) {
        boolean isSuccess;
        try {
            isSuccess = redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {

            log.error(e.getMessage(), e);
            return false;
        }
        return isSuccess;
    }

    /**
     * 写入缓存
     *
     * @param expireTime 单位秒
     * @return true=操作成功，false=操作失败
     */
    @Override
    public boolean set(final String key, Object value, Long expireTime) {
        try {
            redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }


    /**
     * 给缓存key设置一个null值，并设置失效时间，预防缓存击穿
     */
    @Override
    public void setNull(String key) {

        if (StringUtils.hasLength(key)) {
            redisTemplate.opsForValue().set(key, "", this.nullValueExpire, TimeUnit.MINUTES);
        }
    }

    /**
     * 设置缓存，如果缓存为null，给null设置缓存失效时间。
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, Object value) {
        if (StringUtils.hasLength(key)) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 设置缓存，如果缓存为null，给null设置缓存失效时间。
     *
     * @param key
     * @param value
     * @param time  超时时间，单位分钟
     */
    @Override
    public void set(String key, Object value, long time) {
        if (StringUtils.hasLength(key)) {
            set(key, value, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置缓存，如果缓存为null，给null设置缓存失效时间。
     *
     * @param key
     * @param value
     * @param time     超时时间
     * @param timeUnit 单位
     */
    @Override
    public void set(String key, Object value, long time, TimeUnit timeUnit) {
        if (StringUtils.hasLength(key)) {
            redisTemplate.opsForValue().set(key, value, time, timeUnit);
        }
    }

    @Override
    public void delete(String key) {
        if (StringUtils.hasLength(key)) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public void delete(Set<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public void deleteByPattern(String pattern) {
        Set<String> keys = keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public Set<String> keys(String key) {
        if (StringUtils.hasLength(key)) {
            return redisTemplate.keys(key);
        }
        return new HashSet<>();
    }

    @Override
    public long getExpire(String key) {
        if (StringUtils.hasLength(key)) {
            return redisTemplate.getExpire(key);
        }
        return 0;
    }

    @Override
    public DataType type(String key) {
        if (StringUtils.hasLength(key)) {
            return redisTemplate.type(key);
        }
        return DataType.NONE;
    }

    @Override
    public void expire(String key, long time, TimeUnit timeUnit) {
        if (StringUtils.hasLength(key)) {
            redisTemplate.expire(key, time, timeUnit);
        }
    }

    @Override
    public boolean expireAt(String key, Date date) {
        if (StringUtils.hasLength(key) || null == date) {
            return false;
        }
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 批量添加
     *
     * @param maps
     */
    @Override
    public void multiSet(Map<String, String> maps) {
        redisTemplate.opsForValue().multiSet(maps);
    }

    /**
     * 批量添加
     *
     * @param maps
     */
    @Override
    public void multiSetObject(Map<String, Object> maps) {
        if (maps != null && !maps.isEmpty()) {
            redisTemplate.opsForValue().multiSet(maps);
        }
    }

    @Override
    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }

    @Override
    public Long incr(String key, Long incrValue) {
        return redisTemplate.opsForValue().increment(key, incrValue);
    }

    @Override
    public <T> T getSet(String key, T value, Class<T> type) {
        Object result;
        result = redisTemplate.opsForValue().getAndSet(key, value);
        if (result == null) {
            return null;
        }
        String expectClass = JniInvokeUtils.getClass(value).toString();
        String actualClass = JniInvokeUtils.getClass(result).toString();
        if (!expectClass.equals(actualClass)) {
            log.error("class not match.expect class:" + expectClass + ",actualClass:" + actualClass);
            return null;
        }
        try {
            return this.mapper.readValue(this.mapper.writeValueAsBytes(result), type);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public <T> Long leftPush(String key, T value) {
        try {
            return redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1L;
        }
    }

    @Override
    public <T> Long leftPushAll(String key, Collection<T> values) {
        try {
            return redisTemplate.opsForList().leftPushAll(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1L;
        }
    }

    @Override
    public <T> T leftPop(String key, Class<T> type) {
        Object result;
        result = redisTemplate.opsForList().leftPop(key);
        if (result == null) {
            return null;
        }
        String expectClass = type.toString();
        String actualClass = JniInvokeUtils.getClass(result).toString();
        if (!expectClass.equals(actualClass)) {
            log.error("class not match.expect class:" + expectClass + ",actualClass:" + actualClass);
            return null;
        }
        try {
            return this.mapper.readValue(this.mapper.writeValueAsBytes(result), type);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public <T> T rightPop(String key, Class<T> type, long timeout, TimeUnit unit) {
        Object result;
        result = redisTemplate.opsForList().rightPop(key, timeout, unit);
        if (result == null) {
            return null;
        }

        String expectClass = type.toString();
        String actualClass = JniInvokeUtils.getClass(result).toString();
        if (!expectClass.equals(actualClass)) {
            log.error("class not match.expect class:" + expectClass + ",actualClass:" + actualClass);
            return null;
        }

        try {
            return this.mapper.readValue(this.mapper.writeValueAsBytes(result), type);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key
     * @return
     */
    @Override
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key
     * @param fields
     * @return
     */
    @Override
    public List<Object> hMultiGet(String key, Collection<Object> fields) {
        return redisTemplate.opsForHash().multiGet(key, fields);
    }

    @Override
    public void hPut(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void hPutAll(String key, Map<String, String> maps) {
        redisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * 删除一个或多个哈希表字段
     *
     * @param key
     * @param fields
     * @return
     */
    @Override
    public Long hDelete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public boolean hExists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 查询list结构内，元素的数量值
     *
     * @param key
     */
    @Override
    public long sizeForList(String key) {
        if (StringUtils.hasLength(key)) {
            return -1;
        }
        Long s = redisTemplate.opsForList().size(key);
        return s == null ? -1 : s;
    }
}
