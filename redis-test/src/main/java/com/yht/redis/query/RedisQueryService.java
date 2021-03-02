package com.yht.redis.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author YudaBao
 * @date 2021/2/28 16:14
 *
 * 使用redis实现的商家排队功能
 */

@Service
public class RedisQueryService {


    @Autowired
    @Qualifier("redisTemplate")   // 指定使用自定义的 redisTemplate 装配
    private RedisTemplate redisTemplate;

    /**
     * 根据StoreId获取该店排队的Zset名
     * @param storeId
     * @return
     */
    public String getQueryZsetName(String storeId) {
        return "query" + "-" + "zset" + "-" + storeId;
    }

    /**
     * 根据StoreId获取排队的list名
     * @param storeId
     * @return
     */
    public String getQueryListName(String storeId) {
        return "query" + "-" + "list" + "-" + storeId;
    }


    /**
     * 判断Zset集合中是否有某个元素
     * @return
     */
    public boolean isExistZset(String zsetName,String value) {
        boolean result = false;
        if(redisTemplate.opsForZSet().score(zsetName,value) != null)
            result = true;
        return result;
    }


    /**
     * 获取当前队列的最大score
     * @param storeId
     * @return
     */
    public double getMaxScore(String storeId) {
        //返回1个set，只有1个元素
        double maxScore = 0.0;
        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(getQueryZsetName(storeId), 0, 0);
        Iterator<ZSetOperations.TypedTuple<String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
            maxScore = typedTuple.getScore();
        }
        return maxScore;
    }

    /**
     * 获取队列当前排队总人数
     * @param storeId
     * @return
     */
    public long curQueryNumber(String queryZsetName) {
        return redisTemplate.opsForZSet().zCard(queryZsetName);
    }

    /**
     * 入队操作
     * @param storeId 店铺Id
     * @param userId  排队用户Id
     * @return 用户实时排队情况
     */
    public JSONObject push(String storeId, String userId) {

        String queryZsetName = getQueryZsetName(storeId);
        String queryListName = getQueryListName(storeId);
        QueryDetail queryDetail = null;
        QueryUser queryUser = null;
        JSONObject resultJson = new JSONObject();

        if(storeId != null && !storeId.equals("") && userId != null && !userId.equals("")) {

            if(! isExistZset(queryZsetName,userId)) {
                double score = getMaxScore(storeId) + 1.0;
                redisTemplate.opsForZSet().add(queryZsetName,userId,score);
                queryDetail = new QueryDetail();
                queryDetail.setQueryName(queryZsetName);
                queryDetail.setQueryNo(String.valueOf((int) score));
                queryDetail.setQueryUserId(userId);
                String queryDetailStr =  JSONObject.toJSON(queryDetail).toString();
                redisTemplate.opsForList().rightPush(queryListName,queryDetailStr);

                queryUser = new QueryUser();
                queryUser.setQueryNo(String.valueOf((int) score));
                queryUser.setTotalNum(String.valueOf(curQueryNumber(queryZsetName)));
                queryUser.setQueryIndex(String.valueOf(redisTemplate.opsForZSet().rank(queryZsetName,userId) + 1));
                queryUser.setBeforeNum(String.valueOf(redisTemplate.opsForZSet().rank(queryZsetName,userId)));
                resultJson = (JSONObject) JSONObject.toJSON(queryUser);
                return resultJson;
            } else {
                resultJson.put("msg","该用户已在队列中，请勿重新排队！");
                return  resultJson;
            }
        } else {
            resultJson.put("msg","店铺ID和用户ID不能为空！");
            return  resultJson;
        }
    }

    /**
     * 出队-可随机从任意位置出队
     * @param storeId
     * @param userId
     * @return 返回用户的详细排队数据
     */

    /**
     * 实时排队情况
     * @param storeId
     * @param userId
     * @return
     */
    public JSONObject realTimeQuery(String storeId,String userId) {
        String queryZsetName = getQueryZsetName(storeId);
        QueryUser queryUser = null;
        JSONObject resultJson = new JSONObject();

        if(storeId != null && !storeId.equals("") && userId != null && !userId.equals("")) {

            if(isExistZset(queryZsetName,userId)) {
                queryUser = new QueryUser();
                double score = redisTemplate.opsForZSet().score(queryZsetName,userId);
                queryUser.setTotalNum(String.valueOf(curQueryNumber(queryZsetName)));
                queryUser.setQueryIndex(String.valueOf(redisTemplate.opsForZSet().rank(queryZsetName,userId) + 1));
                queryUser.setBeforeNum(String.valueOf(redisTemplate.opsForZSet().rank(queryZsetName,userId)));
                queryUser.setQueryNo(String.valueOf((int) score));
                resultJson = (JSONObject) JSONObject.toJSON(queryUser);
                return resultJson;
            } else {
                resultJson.put("msg","未查到该用户的排队情况，请先排队！");
                return resultJson;
            }
        } else {
            resultJson.put("msg","店铺ID和用户ID不能为空！");
            return  resultJson;
        }
    }

    public JSONObject pop(String storeId, String userId) {

        JSONObject resultJson = new JSONObject();
        if(storeId != null && !storeId.trim().equals("") && userId != null && !userId.trim().equals("")) {
            String queryZsetName = getQueryZsetName(storeId);
            String queryListName = getQueryListName(storeId);

            if(isExistZset(queryZsetName,userId)) {
                long currIndex = redisTemplate.opsForZSet().rank(queryZsetName,userId);
                String removeValue = (String) redisTemplate.opsForList().index(queryListName,currIndex);
                redisTemplate.opsForZSet().remove(queryZsetName,userId);  // 移除Zset的用户排队数据
                redisTemplate.opsForList().remove(queryListName,1,removeValue); // 移除list中的用户排队详情
                resultJson = JSONObject.parseObject(removeValue);
                return resultJson;
            } else {
                resultJson.put("msg","未查到该用户的排队情况，请先排队！");
                return resultJson;
            }
        } else {
            resultJson.put("msg","店铺ID和用户ID不能为空！");
            return resultJson;
        }
    }

    /**
     * 清空一家店铺的排队
     * @param storeId
     * @return
     */
    public JSONObject emptyQuery(String storeId) {
        JSONObject resultJson = new JSONObject();
        if(storeId != null && !storeId.equals("")) {
            String queryZsetName = getQueryZsetName(storeId);
            String queryListName = getQueryListName(storeId);

            boolean result1 = redisTemplate.delete(queryZsetName);
            boolean result2 = redisTemplate.delete(queryListName);

            if(result1 && result2) {
                resultJson.put("msg","排队数据已清空！");
                return resultJson;
            } else {
                resultJson.put("msg","该商家的无排队数据！");
                return resultJson;
            }
        } else {
            resultJson.put("msg","店铺ID不能为空！");
            return resultJson;
        }
    }

    /**
     * 排队情况分页查询
     * @param page
     * @param limit
     * @param storeId
     * @return
     */
    public JSONObject findByPage(Integer page, Integer limit, String storeId) {
        JSONObject resultJson = new JSONObject();
        JSONArray dataJson = new JSONArray();
        Integer currentPage = (page==null||page<1)?1:page;
        Integer pageSize = (limit==null||limit<1)?10:limit;

        if(storeId != null && !storeId.trim().equals("")) {
            String queryListName = getQueryListName(storeId);
            long totalNum = redisTemplate.opsForList().size(queryListName);

            Integer startIndex = pageSize * (currentPage - 1);
            Integer endIndex = pageSize * currentPage - 1;

            List<String> queryDetailStrList = redisTemplate.opsForList().range(queryListName,startIndex,endIndex);
            List<QueryDetail> queryDetailList = new ArrayList<>();
            for(int i = 0; i < queryDetailStrList.size(); i++) {
                JSONObject queryDetailJson = JSONObject.parseObject(queryDetailStrList.get(i));
                QueryDetail queryDetail = new QueryDetail();
                queryDetail.setQueryName(queryDetailJson.get("queryName").toString());
                queryDetail.setQueryUserId(queryDetailJson.get("queryUserId").toString());
                queryDetail.setQueryNo(queryDetailJson.get("queryNo").toString());
                queryDetail.setQueryTime(queryDetailJson.get("queryTime").toString());
                queryDetailList.add(queryDetail);
            }

            dataJson = JSONArray.parseArray(JSON.toJSONString(queryDetailList));
            resultJson.put("msg","查询成功！");
            resultJson.put("total",queryDetailList.size());
            resultJson.put("data",dataJson);
            return resultJson;
        } else {
            resultJson.put("msg","查询失败,店铺ID不能为空！");
            resultJson.put("total",0);
            resultJson.put("data",null);
            return resultJson;
        }
    }


}
