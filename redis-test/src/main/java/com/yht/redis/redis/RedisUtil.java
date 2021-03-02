package com.yht.redis.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yht.redis.redis.RedisConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author YudaBao
 * @date 2021/2/15 16:53
 */

@Component
public class RedisUtil {

    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;

    /**
     * 设置key失效时间
     */
    public boolean expire(String key, long time) {
        try {
            if(time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取key的失效时间
     * key 不为空
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 判断 key 是否存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除一个或多个 key
     */
    public void del(String... key) {
        if(key != null && key.length > 0) {
            if(key.length == 1) {
                redisTemplate.delete(key[0]);
            } else  {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }



    /**
     * 获取 value
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置 key
     */
    public boolean set(String key,Object value) {
        try {
            if(key != null) {
                redisTemplate.opsForValue().set(key,value);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 设置 key，并设置过期时间
     */
    public boolean set(String key,Object value, long time) {
        try {
            if(time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e)  {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置 key递增
     */
    public long incr(String key, long number) {
        if(number < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key,number);
    }

    /**
     * 设置 key递减
     */
    public long decr(String key, long number) {
        if(number < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key,number);
    }

    /**
     * hash get
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key,item);
    }

    /**
     * 获取 hash 的所有键值
     */
    public Map<Object,Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 设置hash的多个键值
     */
    public boolean hmset(String key, Map<String,Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置hash的多个键值,并设置过期时间
     */
    public boolean hmset(String key, Map<String,Object> map,long time) {
        try {
            redisTemplate.opsForHash().putAll(key,map);
            if(time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一个 hash 中放入数据，不存在时创建
     */
    public boolean hset(String key, String item, Object value)  {
        try {
            redisTemplate.opsForHash().put(key,item,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一个 hash 中放入数据,并设置过期时间，不存在时创建
     */
    public boolean hset(String key, String item, Object value, long time)  {
        try {
            redisTemplate.opsForHash().put(key,item,value);
            if(time > 0)  {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
