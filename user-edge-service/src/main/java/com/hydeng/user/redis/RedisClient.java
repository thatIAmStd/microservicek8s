package com.hydeng.user.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-06-30
 */
@Component
public class RedisClient {

    @Autowired
    private RedisTemplate redisTemplate;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key,Object value,long second){
        redisTemplate.opsForValue().set(key,value, second,TimeUnit.SECONDS);
    }

    public <T> T get(String key) {
        return (T)redisTemplate.opsForValue().get(key);
    }

    public boolean expire(String key){
        return redisTemplate.expire(key,3600,TimeUnit.SECONDS);
    }

    public void del(String key){
        redisTemplate.delete(key);
    }

}
