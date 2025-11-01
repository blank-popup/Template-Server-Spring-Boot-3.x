package org.duckdns.ahamike.rollbook.config.redis;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceRedis {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String KEY_PREFIX;
    private final long expireSeconds;
    private final String KEY_DELIMITER;

    public ServiceRedis(RedisTemplate<String, Object> redisTemplate,
            @Value("${server.servlet.context-path}") String contextPath,
            @Value("${auth.jwt.expiration-access}") long expireMilliSeconds,
            @Value("${spring.data.redis.key_delimiter}")String key_delimiter) {
        this.redisTemplate = redisTemplate;
        if (contextPath == null) {
            this.KEY_PREFIX = "[NULL]";
        }
        else {
            if ("/".equals(contextPath) == true) {
                this.KEY_PREFIX = "[ROOT]";
            }
            else {
                this.KEY_PREFIX = contextPath.substring(1);
            }
        }

        this.expireSeconds = expireMilliSeconds / 1000;
        this.KEY_DELIMITER = key_delimiter;
    }


    public String buildKey(String key) {
        return this.KEY_PREFIX + this.KEY_DELIMITER + key;
    }

    public String buildKey(String group, String key) {
        return this.KEY_PREFIX + this.KEY_DELIMITER + group + this.KEY_DELIMITER + key;
    }

    public String buildKey(String group0, String group1, String key) {
        return this.KEY_PREFIX + this.KEY_DELIMITER + group0 + this.KEY_DELIMITER + group1 + this.KEY_DELIMITER + key;
    }

    public String buildKey(String group0, String group1, String group2, String key) {
        return this.KEY_PREFIX + this.KEY_DELIMITER + group0 + this.KEY_DELIMITER + group1 + this.KEY_DELIMITER + group2 + this.KEY_DELIMITER + key;
    }


    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(buildKey(key), value);
    }

    public void setValue(String key, Object value, long expireSeconds) {
        redisTemplate.opsForValue().set(buildKey(key), value, Duration.ofSeconds(expireSeconds));
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(buildKey(key));
    }

    public Boolean removeValue(String key) {
        return redisTemplate.delete(buildKey(key));
    }


    public void setValue(String group0, String key, Object value) {
        redisTemplate.opsForValue().set(buildKey(group0, key), value);
    }

    public void setValue(String group0, String key, Object value, long expireSeconds) {
        redisTemplate.opsForValue().set(buildKey(group0, key), value, Duration.ofSeconds(expireSeconds));
    }

    public Object getValue(String group0, String key) {
        return redisTemplate.opsForValue().get(buildKey(group0, key));
    }

    public Boolean removeValue(String group0, String key) {
        return redisTemplate.delete(buildKey(group0, key));
    }


    public void setValue(String group0, String group1, String key, Object value) {
        redisTemplate.opsForValue().set(buildKey(group0, group1, key), value);
    }

    public void setValue(String group0, String group1, String key, Object value, long expireSeconds) {
        redisTemplate.opsForValue().set(buildKey(group0, group1, key), value, Duration.ofSeconds(expireSeconds));
    }

    public Object getValue(String group0, String group1, String key) {
        return redisTemplate.opsForValue().get(buildKey(group0, group1, key));
    }

    public Boolean removeValue(String group0, String group1, String key) {
        return redisTemplate.delete(buildKey(group0, group1, key));
    }


    public void setValue(String group0, String group1, String group2, String key, Object value) {
        redisTemplate.opsForValue().set(buildKey(group0, group1, group2, key), value);
    }

    public void setValue(String group0, String group1, String group2, String key, Object value, long expireSeconds) {
        redisTemplate.opsForValue().set(buildKey(group0, group1, group2, key), value, Duration.ofSeconds(expireSeconds));
    }

    public Object getValue(String group0, String group1, String group2, String key) {
        return redisTemplate.opsForValue().get(buildKey(group0, group1, group2, key));
    }

    public Boolean removeValue(String group0, String group1, String group2, String key) {
        return redisTemplate.delete(buildKey(group0, group1, group2, key));
    }


    public void setOValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setOValue(String key, Object value, long expireSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(expireSeconds));
    }

    public Object getOValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean removeOValue(String key) {
        return redisTemplate.delete(key);
    }


    public List<String> findKeysByPattern(String keyPattern) {
        List<String> keys = new ArrayList<>();

        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        try {
            RedisKeyCommands keyCommands = connection.keyCommands();

            Set<byte[]> rawKeys = keyCommands.keys(keyPattern.getBytes(StandardCharsets.UTF_8));
 
            for (byte[] key : rawKeys) {
                keys.add(new String(key, StandardCharsets.UTF_8));
            }
        } finally {
            connection.close();
        }

        return keys;
    }


    public String getKEY_PREFIX() {
        return KEY_PREFIX;
    }

    public long getExpireSeconds() {
        return expireSeconds;
    }

    public String getKEY_DELIMITER() {
        return KEY_DELIMITER;
    }
}
