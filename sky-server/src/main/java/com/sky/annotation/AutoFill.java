package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @description:自定义注解，用于标识某个方法需要进行功能字段自动填充处理
 * @author:  HZP
 * @date: 2023/7/28 9:29
 * @param: 
 * @return: 
 **/
//@Target:表明这个注解只能用在方法上
@Target(ElementType.METHOD)
//@Retention设置该注解的生命周期
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //数据库操作类型：UPDATE INSERT
    OperationType value();//也就是给这个注解设置属性参数
}
