package com.yht.redis.query;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author YudaBao
 * @date 2021/2/25 11:12
 * 存如入 List 的详细排队信息
 */

public class QueryDetail {

    private String queryName;  // 队列名
    private String queryNo;  // 排队号码
    private String queryTime;  // 开始排队时间
    private String queryUserId;  // 排队用户

    public QueryDetail() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.queryTime = sdf.format(new Date());
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryNo() {
        return queryNo;
    }

    public void setQueryNo(String queryNo) {
        this.queryNo = queryNo;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getQueryUserId() {
        return queryUserId;
    }

    public void setQueryUserId(String queryUserId) {
        this.queryUserId = queryUserId;
    }
}
