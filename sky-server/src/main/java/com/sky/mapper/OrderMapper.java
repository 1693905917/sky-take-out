package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /*
     * @description:分页条件查询并按下单时间排序
     * @author:  HZP
     * @date: 2023/8/3 9:17
     * @param: 
     * @return: 
     **/
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /*
     * @description:根据id查询订单
     * @author:  HZP
     * @date: 2023/8/3 9:30
     * @param: 
     * @return: 
     **/
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);
}
