package com.github.eokasta.testeredis.redis;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RequiredArgsConstructor
public class JedisSerializer {

    private final JedisPool jedisPool;
    private final Gson gson = new Gson();

    protected void putObject(String key, Object value) throws IllegalAccessException {
        try (final Jedis jedis = jedisPool.getResource()) {
            String code = jedis.set(key, gson.toJson(value));

            if (!"OK".equalsIgnoreCase(code))
                throw new IllegalAccessException("Put object to redis failed!");

        }
    }

    public void putPatternObject(String pattern, String key, Object value) {
        try (final Jedis jedis = jedisPool.getResource()) {
            jedis.hset(pattern, key, gson.toJson(value));
        }
    }

    public Map<String, String> getPatternObjects(String pattern) {
        try (final Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(pattern);
        }
    }

    public String getPatternValue(String pattern, String key) {
        try (final Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(pattern, key);
        }
    }

    public <T> T getPatternObject(String pattern, String key, Class<T> clazz) {
        try (final Jedis jedis = jedisPool.getResource()) {
            return gson.fromJson(getPatternValue(pattern, key), clazz);
        }
    }

    public void removePatternObject(String pattern, String key) {
        try (final Jedis jedis = jedisPool.getResource()) {
            jedis.hdel(pattern, key);
        }
    }

    public void putObjectSafe(String key, Object value) {
        try {
            putObject(key, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        try (final Jedis jedis = jedisPool.getResource()) {
            return gson.fromJson(jedis.get(key), clazz);
        }
    }

    public void remove(String key) {
        try (final Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        }
    }

}
