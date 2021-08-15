package com.dev.constant;

/**
 * 常量
 * @author 路飞
 * @create 2021/8/14 19:33
 */
public class RedRobConstant {

    //存入Redis的Sting和list前缀
    public static final String KEY_PREFIX = "REDIS:RED:PACKET:";

    public static final String TOTAL = ":TOTAL";

    public static final String ROB = ":ROB";

    public static final String LOCK = "-LOCK";

    //控制器路径前缀
    public static final String MAPPING_PREFIX="red/packet";

    //缓存过期时间
    public static final Long EXPIRE_TIME = 24L;


    //异常信息
    public static final String REDROB_IS_EMPTY = "红包被抢完了！";

    public static final String  LOCK_FAILED = "系统异常-抢红包-加分布式锁失败!";

    public static final String  PARAMETER_ILLEGAL = "系统异常-分发红包-参数不合法!";



}
