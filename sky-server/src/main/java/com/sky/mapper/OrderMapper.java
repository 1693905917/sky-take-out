package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /*
     * @description:根据订单状态和下单时间查询订单
     * @author:  HZP
     * @date: 2023/8/4 9:23
     * @param: 
     * @return: 
     **/
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime time);

    /*
     * @description:根据动态条件统计营业额
     * @author:  HZP
     * @date: 2023/8/4 21:30
     * @param: 
     * @return: 
     **/
    Double sumByMap(Map map);

    /*
     * @description:根据动态条件统计订单数量
     * @author:  HZP
     * @date: 2023/8/5 9:16
     * @param: 
     * @return: 
     **/
    Integer countByMap(Map map);


    /*
     * @description:查询商品销量排名
     * @author:  HZP
     * @date: 2023/8/5 10:15
     * @param:
     * @return:
     **/
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}

