package com.yht.redis.pubSub;

import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @author YudaBao
 * @date 2021/2/28 19:47
 * 消息处理器
 */

@Component
public class MessageHandler {

    public void receiveMessage(String message) {
        // TODO 这里是收到通道的消息之后执行的方法
        System.out.println("收到来自channel的消息---" + message);
    }
}
