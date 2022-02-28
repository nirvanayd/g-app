package com.nelly.application.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CacheTemplate {

    private final StringRedisTemplate stringRedisTemplate;

    private final String PREFIX = ":";
    private final String WILDCARD = "*";

    public void putValue(final String key, final String value, final String cacheName) {
        putValue(concatenateCacheNameWithKey(key, cacheName), value);
    }

    public void putValue(final String key, final String value, final String cacheName, long expire, final TimeUnit unit) {
        putValue(concatenateCacheNameWithKey(key, cacheName), value, expire, unit);
    }

    public void putValue(final int key, final int value, final String cacheName, long expire, final TimeUnit unit) {
        String stringValue = String.valueOf(value);
        putValue(concatenateCacheNameWithKey(key, cacheName), stringValue, expire, unit);
    }

    public void putValue(final String key, final String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
        } catch (Exception ex) {
            System.out.println("Exception::" + ex);
        }
    }

    public void putValue(final String key, final String value, long expire, final TimeUnit unit) {
        try {
            stringRedisTemplate.opsForValue().set(key, value, expire, unit);
        } catch (Exception ex) {
            System.out.println("Exception::" + ex);
        }
    }

    public String getValue(final String key, final String cacheName) {
        return getValue(concatenateCacheNameWithKey(key, cacheName));
    }

    public String getValue(final String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception ex) {
            System.out.println("Exception::" + ex);
        }
        return null;
    }

    public Long getExpire(final String key, final String cacheName) {
        return getExpire(concatenateCacheNameWithKey(key, cacheName));
    }

    public Long getExpire(final String key) {
        try {
            return stringRedisTemplate.getExpire(key);
        } catch (Exception ex) {
            System.out.println("Exception::" + ex);
        }
        return null;
    }

    public void deleteCache(final String key, final String cacheName) {
        deleteCache(concatenateCacheNameWithKey(key, cacheName));
    }

    public void deleteCache(final String key) {
        stringRedisTemplate.delete(key);
    }

    private String concatenateCacheNameWithKey(final String key, final String cacheName) {
        String result = "";
        if (cacheName == null && key == null) {
            throw new NullPointerException();
        } else {
            if (cacheName.isBlank()) {
                result = key;
            } else if (key == null || key.isBlank()) {
                result = cacheName;
            } else {
                result = cacheName + PREFIX + key;
            }
        }
        return result;
    }

    public void incrValue(final String key, final String cacheName) {
        incrValue(concatenateCacheNameWithKey(key, cacheName));
    }

    public void decrValue(final String key, final String cacheName) {
        decrValue(concatenateCacheNameWithKey(key, cacheName));
    }

    public void incrValue(final String key) {
        try {
            stringRedisTemplate.opsForValue().increment(key);
        } catch (Exception ex) {
            System.out.println("Exception::" + ex);
        }
    }

    public void decrValue(final String key) {
        try {
            stringRedisTemplate.opsForValue().decrement(key);
        } catch (Exception ex) {
            System.out.println("Exception::" + ex);
        }
    }

    private String concatenateCacheNameWithKey(final int key, final String cacheName) {
        String stringKey = String.valueOf(key);
        return this.concatenateCacheNameWithKey(stringKey, cacheName);
    }

    public Set<String> getKeys(String cacheName) {
        return stringRedisTemplate.keys(cacheName + PREFIX + WILDCARD);
    }

    public HashMap<String, String> parseCashNameKey(String cashNameKey) {
        HashMap<String, String> map = new HashMap<>();
        if (!cashNameKey.contains(PREFIX)) {
            map.put("cacheName", "");
            map.put("key", cashNameKey);
            return map;
        }

        String [] array = cashNameKey.split(PREFIX);
        map.put("cacheName", array[0]);
        map.put("key", array[1]);
        return map;
    }
}

