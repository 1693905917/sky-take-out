package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    /*
     * @description:插入订单数据
     * @author:  HZP
     * @date: 2023/8/2 11:25
     * @param: 
     * @return: 
     **/
    void insert(Orders order);
}

