package org.linlinjava.litemall.db.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheManager {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public void set(String key,String value,long expire){

        stringRedisTemplate.opsForValue().set(key,value,expire, TimeUnit.SECONDS);
    }

    public String get(String key){

        return stringRedisTemplate.opsForValue().get(key);
    }


    public Boolean delete(String key){

       return stringRedisTemplate.delete(key);
    }

    public boolean hasKey(String key) {

        return stringRedisTemplate.hasKey(key);
    }
}
