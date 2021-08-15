package com.dev.service;


import com.dev.mapper.RedDetailMapper;
import com.dev.mapper.RedRecordMapper;
import com.dev.mapper.RedRobRecordMapper;
import com.dev.pojo.RedDetail;
import com.dev.pojo.RedPacketDto;
import com.dev.pojo.RedRecord;
import com.dev.pojo.RedRobRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@EnableAsync
public class RedService implements IRedService {

    private static final Logger log= LoggerFactory.getLogger(RedService.class);

    private static final byte FINISH =(int) 1;

    @Autowired
    private RedRecordMapper redRecordMapper;

    @Autowired
    private RedDetailMapper redDetailMapper;

    @Autowired
    private RedRobRecordMapper redRobRecordMapper;


    /**
     * 发红包记录
     * @param dto
     * @param redId
     * @param list
     * @throws Exception
     */
    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception {
        RedRecord redRecord=new RedRecord();
        redRecord.setId(dto.getId());
        redRecord.setUserId(dto.getUserId());
        redRecord.setRedPacket(redId);
        redRecord.setTotal(dto.getTotal());
        redRecord.setAmount(BigDecimal.valueOf(dto.getAmount()));
        redRecord.setCreateTime(new Date());
        redRecordMapper.insertSelective(redRecord);

        RedDetail detail;
        for (Integer i:list){
            detail=new RedDetail();
            detail.setRecordId(redRecord.getId());
            detail.setAmount(BigDecimal.valueOf(i));
            detail.setCreateTime(new Date());
            redDetailMapper.insertSelective(detail);
        }
    }

    /**
     * 抢红包记录
     * @param userId
     * @param redId
     * @param amount
     * @throws Exception
     */
    @Override
    @Async
    public void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception {
        RedRobRecord redRobRecord=new RedRobRecord();
        redRobRecord.setUserId(userId);
        redRobRecord.setRedPacket(redId);
        redRobRecord.setAmount(amount);
        redRobRecord.setRobTime(new Date());
        redRobRecordMapper.insertSelective(redRobRecord);
    }
}









































