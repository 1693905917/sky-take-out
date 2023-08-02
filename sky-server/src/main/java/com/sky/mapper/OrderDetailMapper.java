package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /*
     * @description:批量插入订单明细数据
     * @author:  HZP
     * @date: 2023/8/2 11:25
     * @param: 
     * @return: 
     **/
    void insertBatch(List<OrderDetail> orderDetailList);
}
