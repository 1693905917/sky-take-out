package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.task
 * @Author: ASUS
 * @CreateTime: 2023-08-04  09:15
 * @Description: TODO  定时任务类，定时处理订单状态
 * @Version: 1.0
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /*
     * @description:处理超时订单的方法
     * @author:  HZP
     * @date: 2023/8/4 9:17
     * @param:
     * @return:
     **/
    @Scheduled(cron = "0 * * * * ?")//每分钟触发一次
   // @Scheduled(cron = "1/5 * * * * ?")//每天凌晨1点执行一次
    public void processTimeoutOrder(){
        log.info("定时处理超时订单:{}", LocalDateTime.now());

        //我们的思路：将当前时间-15分钟所得到的时间前如果有订单还是处于支付状态就视为超时订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        //select * from orders where status=? and order_time<(当前时间-15分钟)
        List<Orders> ordersList= orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,time);
        if(ordersList!=null&& ordersList.size()>0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);//自动取消订单
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /*
     * @description:派送中状态的订单处理
     * @author:  HZP
     * @date: 2023/8/4 9:17
     * @param:
     * @return:对于一直处于派送中状态的订单，自动修改状态为 [已完成]
     **/
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
    //@Scheduled(cron = "0/5 * * * * ?")//每天凌晨1点执行一次
    public void processDeliveryOrder(){
        log.info("开始进行未完成订单状态处理:{}", LocalDateTime.now());
        //我们的思路：将当前时间-60分钟所得到的时间前如果有订单还是处于派送中状态则修改为完成状态
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        //select * from orders where status=? and order_time<(当前时间-60分钟)
        List<Orders> ordersList= orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,time);
        if(ordersList!=null&& ordersList.size()>0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);//自动完成
                orderMapper.update(orders);
            }
        }
    }


}
