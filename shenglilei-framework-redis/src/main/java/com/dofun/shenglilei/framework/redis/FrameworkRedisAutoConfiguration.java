package com.dofun.shenglilei.framework.redis;


import com.dofun.shenglilei.framework.redis.lock.RedisLockAspect;
import com.dofun.shenglilei.framework.redis.service.RedisService;
import com.dofun.shenglilei.framework.redis.service.impl.RedisServiceImpl;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;


/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/9/18
 * Time:13:26
 *
 * @author steven
 */
@Slf4j
@EnableCaching
@Configuration
@AutoConfigureBefore(RedissonAutoConfiguration.class)
public class FrameworkRedisAutoConfiguration {

    private final ObjectMapper objectMapper = getObjectMapper();

    private ObjectMapper getObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 解决jackson2无法反序列化LocalDateTime的问题
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        om.findAndRegisterModules();
        return om;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //key 值序列化方式
        RedisSerializer<String> stringSerializer = template.getStringSerializer();
        RedisSerializer<Object> jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        //设置key 的序列化方式
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        //设置值 的序列化方式
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        //设置默认的序列化方式
        template.setDefaultSerializer(stringSerializer);
        template.afterPropertiesSet();
        log.info("RedisTemplate is ready to inject.");
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存有效期一小时
                .entryTtl(Duration.ofHours(1));

        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean
    public RedisService redisService(RedisTemplate<String, Object> redisTemplate) {
        log.info("RedisService is ready to inject.");
        return new RedisServiceImpl(redisTemplate, objectMapper);
    }

    @Bean
    public RedisLockAspect createRedisLockAspect() {
        log.info("RedisLockAspect is ready to inject.");
        return new RedisLockAspect();
    }
}
