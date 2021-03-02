package com.yht.redis.pubSub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author YudaBao
 * @date 2021/2/28 19:39
 * 消息发布 service
 */


@Service
public class PublishMessageService {

    @Autowired
    @Qualifier("redisTemplate")   // 指定使用自定义的 redisTemplate 装配
    private RedisTemplate redisTemplate;

    public String publishMessage(String message) {
        redisTemplate.convertAndSend("channel",message);
        return "SUCCESS";
    }

}
