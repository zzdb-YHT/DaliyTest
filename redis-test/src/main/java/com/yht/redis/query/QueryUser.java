package com.yht.redis.query;

/**
 * @author YudaBao
 * @date 2021/2/25 9:00
 * 返回给用户的 排队信息
 */

public class QueryUser {

    private String queryNo;
    private String queryIndex;
    private String totalNum;
    private String beforeNum;

    public String getQueryNo() {
        return queryNo;
    }

    public void setQueryNo(String queryNo) {
        this.queryNo = queryNo;
    }

    public String getQueryIndex() {
        return queryIndex;
    }

    public void setQueryIndex(String queryIndex) {
        this.queryIndex = queryIndex;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(String beforeNum) {
        this.beforeNum = beforeNum;
    }
}
