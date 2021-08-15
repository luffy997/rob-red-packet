package com.dev.service;




import com.dev.pojo.RedPacketDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 红包记录服务
 * @author: Luffy
 * @date: 2021/08/10
 */
public interface IRedService {

    void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception;

    void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception;

}
