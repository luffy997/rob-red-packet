package com.dev.robredpacket;


import com.dev.controller.RedPacketController;
import com.dev.pojo.RedPacketDto;
import com.dev.service.RedPacketService;
import com.dev.utils.RedPacketUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RobRedPacketApplicationTests {
    @Autowired
    private RedPacketService redPacketService;

    @Autowired
    RedPacketController redPacketController;

    @Test
    public void test01(){
        List<Integer> list = RedPacketUtil.divideRedPackage(12, 10);

        //雪花算法
//        SnowFlake snowFlake = new SnowFlake();
//        System.out.println("雪花算法生成的ID："+snowFlake.nextId());
        System.out.println(list);
    }

    @Test
    public void testHandOutRed() throws Exception {
        RedPacketDto redPacketDto = new RedPacketDto();
        redPacketDto.setUserId(7777777);
        //100块
        redPacketDto.setAmount(100);
        //10人抢
        redPacketDto.setTotal(10);
        redPacketService.handOut(redPacketDto);
    }
}

