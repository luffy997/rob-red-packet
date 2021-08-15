package com.dev.service;


import com.dev.constant.RedRobConstant;
import com.dev.pojo.RedPacketDto;
import com.dev.utils.RedPacketUtil;
import com.dev.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: Luffy
 * @date: 2021/08/10
 */
@Transactional
@Service
public class RedPacketService implements IRedPacketService {

    private static final Logger log= LoggerFactory.getLogger(RedPacketService.class);

    private final SnowFlake snowFlake = new SnowFlake();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IRedService redService;


    /**
     * 发红包
     * @throws Exception
     */
    @Override
    public String handOut(RedPacketDto dto) throws Exception {
        if (dto.getTotal()>0 && dto.getAmount()>0){
            //生成随机金额
            List<Integer> list= RedPacketUtil.divideRedPackage(dto.getAmount(),dto.getTotal());
            //log.info("二倍均值法获得的红包结果："+list);
            //生成红包全局唯一标识,并将随机金额、个数入缓存
            //String timestamp=String.valueOf(System.nanoTime());
            //log.info("当前系统时间戳："+timestamp);
            //使用雪花算法生成ID
            long snowFlakeID = snowFlake.nextId();
            dto.setId(snowFlakeID);
            String redId = new StringBuffer(RedRobConstant.KEY_PREFIX).append(dto.getUserId()).append(":").append(snowFlakeID).toString();
            redisTemplate.opsForList().leftPushAll(redId,list);

            String redTotalKey = redId+RedRobConstant.TOTAL;
            redisTemplate.opsForValue().set(redTotalKey,dto.getTotal());

            //异步记录红包发出的记录-包括个数与随机金额
            redService.recordRedPacket(dto,redId,list);

            return redId;
        }else{
            throw new Exception(RedRobConstant.PARAMETER_ILLEGAL);
        }
    }

    /**
     * 加分布式锁的情况
     * 抢红包-分“点”与“抢”处理逻辑
     * @throws Exception
     */
    @Override
    public BigDecimal rob(Integer userId,String redId) throws Exception {
        ValueOperations valueOperations=redisTemplate.opsForValue();

        //用户是否抢过该红包
        Object obj=valueOperations.get(redId+userId+RedRobConstant.ROB);
        if (obj!=null){
            return new BigDecimal(obj.toString());
        }

        //"点红包"
        Boolean res=click(redId);
        if (res){
            //上锁:一个红包每个人只能抢一次随机金额；一个人每次只能抢到红包的一次随机金额  即要永远保证 1对1 的关系
            final String lockKey=redId+userId+RedRobConstant.LOCK;
            //加入分布式锁，防止同一个用户多抢红包
            Boolean lock=valueOperations.setIfAbsent(lockKey,redId);
            //设置锁超时参数防止宕机锁无法释放
            redisTemplate.expire(lockKey,24L,TimeUnit.HOURS);
            try {
                if (lock) {

                    //"抢红包"-且红包有钱
                    //右边弹出
                    //只能说这是最后的稻草了，假如没这个判断，程序百分百报错
                    Object value=redisTemplate.opsForList().rightPop(redId);
                    if (value!=null){
                        //红包个数减一
                        String redTotalKey = redId+RedRobConstant.TOTAL;

                        //Integer currTotal=valueOperations.get(redTotalKey)!=null? (Integer) valueOperations.get(redTotalKey) : 0;
                        //这里不是-1 不是原子性操作，所以会发生在多线程环境下，即使红包抢完了，红包总数并不是0
                        valueOperations.increment(redTotalKey, -1);
                        //valueOperations.set(redTotalKey,currTotal-1);
                        log.info("剩余红包数："+(valueOperations.get(redTotalKey)));

                        //将红包金额返回给用户的同时，将抢红包记录入数据库与缓存
                        BigDecimal result = new BigDecimal(value.toString());
                        redService.recordRobRedPacket(userId,redId,new BigDecimal(value.toString()));

                        valueOperations.set(redId+userId+RedRobConstant.ROB,result,RedRobConstant.EXPIRE_TIME,TimeUnit.HOURS);

                        log.info("当前用户抢到红包了：userId={} key={} 金额={} ",userId,redId,result);
                        return result;
                    }

                }
            }catch (Exception e){
                throw new Exception(RedRobConstant.LOCK_FAILED);
            }
        }
        return null;
    }

    /**
     * 不加分布式锁的情况
     * 抢红包-分“点”与“抢”处理逻辑
     * @param userId
     * @param redId
     * @return
     * @throws Exception
     */
//    @Override
//    public BigDecimal rob(Integer userId,String redId) throws Exception {
//        ValueOperations valueOperations=redisTemplate.opsForValue();
//
//        //用户是否抢过该红包
//        Object obj=valueOperations.get(redId+userId+":rob");
//        if (obj!=null){
//            return new BigDecimal(obj.toString());
//        }
//
//        //"点红包"
//        Boolean res=click(redId);
//        if (res){
//            //"抢红包"-且红包有钱
//            Object value=redisTemplate.opsForList().rightPop(redId);
//            if (value!=null){
//                //红包个数减一
//                String redTotalKey = redId+":total";
//
//                Integer currTotal=valueOperations.get(redTotalKey)!=null? (Integer) valueOperations.get(redTotalKey) : 0;
//                valueOperations.set(redTotalKey,currTotal-1);
//
//
//                //将红包金额返回给用户的同时，将抢红包记录入数据库与缓存
//                BigDecimal result = new BigDecimal(value.toString()).divide(new BigDecimal(100));
//                redService.recordRobRedPacket(userId,redId,new BigDecimal(value.toString()));
//
//                valueOperations.set(redId+userId+":rob",result,24L,TimeUnit.HOURS);
//
//                log.info("当前用户抢到红包了：userId={} key={} 金额={} ",userId,redId,result);
//                return result;
//            }
//
//        }
//        return null;
//    }


    /**
     * 点红包-返回true，则代表红包还有，个数>0
     * @throws Exception
     */
    private Boolean click(String redId) throws Exception{
        ValueOperations valueOperations=redisTemplate.opsForValue();

        String redTotalKey = redId+RedRobConstant.TOTAL;
        Object total=valueOperations.get(redTotalKey);
        if (total!=null && Integer.valueOf(total.toString())>0){
            return true;
        }
        return false;
    }
}