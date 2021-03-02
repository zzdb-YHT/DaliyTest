package com.yht.redis.pubSub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YudaBao
 * @date 2021/2/28 19:37
 * 消息发布controller
 */

@RestController
public class PublishController {

    @Autowired
    private PublishMessageService publishMessageService;

    @RequestMapping("/publishMessage")
    private String publishMessage(String message) {
        return publishMessageService.publishMessage(message);
    }

}
