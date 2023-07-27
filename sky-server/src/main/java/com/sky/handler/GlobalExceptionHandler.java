package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /*
     * @description:处理SQL异常
     * @author:  HZP
     * @date: 2023/7/27 11:32
     * @param:
     * @return:
     **/
    //java.sql.SQLIntegrityConstraintViolationException:Duplicate entry 'zhangsan' for key employee.idx_username
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error("异常信息：{}", ex.getMessage());
        //ex.getMessage()获取的值是:Duplicate entry 'zhangsan' for key employee.idx_username
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            //将Duplicate entry 'zhangsan' for key employee.idx_username以空格将信息拆分成数组类型
            String[] split = message.split(" ");
            String name = split[2];//split[2]:动态获取对应哪个名字是已存在的 zhangsan
            return Result.error(name + MessageConstant.ALREADY_EXISTS);//MessageConstant.ALREADY_EXISTS="已存在"
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);//MessageConstant.UNKNOWN_ERROR="未知错误"
    }

}
