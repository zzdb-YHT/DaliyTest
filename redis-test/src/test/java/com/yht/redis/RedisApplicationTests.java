package com.yht.redis;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Iterator;
import java.util.Set;

@SpringBootTest
class RedisApplicationTests {

    @Autowired
    @Qualifier("redisTemplate")   // 指定使用自定义的 redisTemplate 装配
    RedisTemplate redisTemplate;

//    @Autowired
//    RedisUtil redisUtil;

    @Test
    void contextLoads() {

//        redisTemplate.opsForValue().set("redis","Hello World");
//        System.out.println(redisTemplate.opsForValue().get("redis"));
//
//        User user = new User("Tony", 3);
//        String setUserStr = JSONObject.toJSON(user).toString();
//        redisTemplate.opsForValue().set("user",setUserStr);
//        String getUserStr = (String) redisTemplate.opsForValue().get(set"user");
//        System.out.println(getUserStr);
//        JSONObject userJson = (JSONObject) JSONObject.parse(getUserStr);
//        System.out.println(userJson.get("name"));
//        System.out.println(userJson.get("age"));

        // 倒序排序获取RedisZSetCommands.Tuples的从给定下标和给定长度分值区间值。

        System.out.println(redisTemplate.opsForSet().add("myset","ttt"));


    }

}
