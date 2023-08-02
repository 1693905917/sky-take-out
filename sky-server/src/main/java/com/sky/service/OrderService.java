package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {
    /*
     * @description:用户下单
     * @author:  HZP
     * @date: 2023/8/2 11:14
     * @param: 
     * @return: 
     **/
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
