package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.aspect
 * @Author: ASUS
 * @CreateTime: 2023-07-28  09:35
 * @Description: TODO 自定义切面，实现公共字段自动填充处理逻辑
 * @Version: 1.0
 */
@Aspect  //将AutoFillAspect设置为切面
@Component //交给spring容器管理
@Slf4j //方便我们记录一些日志信息
public class AutoFillAspect {
    /*
     * @description:切入点
     * @author:  HZP
     * @date: 2023/7/28 9:37
     * @param:
     * @return:
     **/
    // @Pointcut对于com.sky.mapper下并且被AutoFill注解所标识的所有方法进行拦截
    @Pointcut("execution(* com.sky.mapper.*.*(..))&& @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}
    
    /*
     * @description:前置通知，在通知中进行公共字段的赋值
     * @author:  HZP
     * @date: 2023/7/28 9:41
     * @param: 
     * @return: 
     **/
    //Before:就是被autoFillPointCut拦截到方法，在执行目标方法前，先执行autoFill()方法
    @Before("autoFillPointCut()")
    //JoinPoint参数可以将拦截的方法的所有信息都获取到包括该方法被什么注解，该方法的参数名等等
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自动填充...");
        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();//获取数据库操作类型
        //获取到当前被拦截的方法的参数-实体对象
        Object[] args = joinPoint.getArgs();
        //防止拦截方法的参数为空
        if(args==null || args.length==0){
            return;
        }
        //我们约定好，在Mapper中的所有SQL方法，只要是被AutoFill注解标识的，第一个参数必须是有4个公共字段等属性的对象
        Object entity = args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //根据当前不同的操作类型，为对应的属性通过反射来赋值
       if(operationType==OperationType.INSERT){
           //为4个公共字段赋值
           try {
               Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
               Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
               Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
               Method setUpdateUser =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

               //通过反射为对象属性赋值
               setCreateTime.invoke(entity,now);
               setCreateUser.invoke(entity,currentId);
               setUpdateTime.invoke(entity,now);
               setUpdateUser.invoke(entity,currentId);

           } catch (Exception e) {
               log.error("公共字段自动填充失败：{}", e.getMessage());
           }
       }else if(operationType==OperationType.UPDATE){
           //为2个公共字段赋值
           try {
               Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
               Method setUpdateUser =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

               setUpdateTime.invoke(entity,now);
               setUpdateUser.invoke(entity,currentId);
           } catch (Exception e) {
               log.error("公共字段自动填充失败：{}", e.getMessage());
           }
       }
    }
    
}
