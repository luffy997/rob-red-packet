package com.dev.controller;


import com.dev.api.BaseResponse;
import com.dev.api.StatusCode;
import com.dev.constant.RedRobConstant;
import com.dev.pojo.RedPacketDto;
import com.dev.service.IRedPacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


/**
 * @author: Luffy
 * @date: 2021/08/10
 */
@RestController
public class RedPacketController {

    private static final Logger log= LoggerFactory.getLogger(RedPacketController.class);

    @Autowired
    private IRedPacketService redPacketService;


    /**
     * 发
     */
    @RequestMapping(value = RedRobConstant.MAPPING_PREFIX +"/hand/out",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse handOut(@RequestBody RedPacketDto dto, BindingResult result){
       if (result.hasErrors()){
           return new BaseResponse(StatusCode.InvalidParams);
       }
       BaseResponse response=new BaseResponse(StatusCode.Success);
       try {
            String redId=redPacketService.handOut(dto);
            response.setData(redId);

       }catch (Exception e){
           log.error("发红包发生异常：dto={} ",dto,e.fillInStackTrace());
           response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
       }
       return response;
    }


    /**
     * 抢
     */
    @RequestMapping(value = RedRobConstant.MAPPING_PREFIX+"/rob",method = RequestMethod.GET)
    public BaseResponse rob(@RequestParam Integer userId, @RequestParam String redId){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            BigDecimal result=redPacketService.rob(userId,redId);
            if (result!=null){
                response.setData(result);
            }else{
                response=new BaseResponse(StatusCode.Fail.getCode(),RedRobConstant.REDROB_IS_EMPTY);
            }
        }catch (Exception e){
            log.error("抢红包发生异常：userId={} redId={}",userId,redId,e.fillInStackTrace());
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}