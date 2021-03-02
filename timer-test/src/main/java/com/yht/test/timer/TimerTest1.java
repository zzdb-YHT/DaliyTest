package com.yht.test.timer;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * @author YudaBao
 * @date 2021/3/1 15:10
 */

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class TimerTest1 {

//    @Scheduled(cron = "0/10 * * * * ?")
//    public void task1() {
//        System.out.println(new Date() + "--" + "定时任务");
//    }

}
