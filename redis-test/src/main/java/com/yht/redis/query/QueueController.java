package com.yht.redis.query;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YudaBao
 * @date 2021/2/23 18:03
 */

@RestController
public class QueueController {


    @Autowired
    private RedisQueryService redisQueryService;

    @RequestMapping("push")
    public JSONObject push(String storeId, String userId) {
        return redisQueryService.push(storeId,userId);
    }

    @RequestMapping("/pop")
    public JSONObject pop(String storeId, String userId) {
        return  redisQueryService.pop(storeId,userId);
    }


    @RequestMapping("/realTimeQuery")
    public JSONObject realTimeQuery(String storeId, String userId) {
        return  redisQueryService.realTimeQuery(storeId,userId);
    }

    @RequestMapping(value = "/findByPage")
    public JSONObject findByPage(Integer page, Integer limit, String storeId) {
        return redisQueryService.findByPage(page, limit,storeId);
    }

    @RequestMapping(value = "/emptyQuery")
    public JSONObject emptyQuery(String storeId) {
        return redisQueryService.emptyQuery(storeId);
    }


}
