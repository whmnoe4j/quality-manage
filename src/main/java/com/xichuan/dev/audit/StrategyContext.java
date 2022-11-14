package com.xichuan.dev.audit;


import java.util.HashMap;
import java.util.Map;

/**
 * @Author Xichuan
 * @Date 2022/4/13 9:55
 * @Description
 */

/**
 * 稽核策略上下文
 */
public class StrategyContext {
    //策略集合
    private static final Map<String, Strategy> registerMap = new HashMap<>();

    /**
     * 注册稽核策略
     * @param rewardType 策略名称
     * @param strategy 策略
     */
    public static void registerStrategy(String rewardType, Strategy strategy) {
        registerMap.putIfAbsent(rewardType, strategy);
    }

    /**
     * 获取稽核策略
     * @param rewardType
     * @return
     */
    public static Strategy getStrategy(String rewardType) {
        return registerMap.get(rewardType);
    }
}
