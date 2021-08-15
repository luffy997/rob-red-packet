package com.dev.service;




import com.dev.pojo.RedPacketDto;

import java.math.BigDecimal;

/**
 * @author: Luffy
 * @date: 2021/08/10
 */
public interface IRedPacketService {

    String handOut(RedPacketDto dto) throws Exception;

    BigDecimal rob(Integer userId, String redId) throws Exception;
}
