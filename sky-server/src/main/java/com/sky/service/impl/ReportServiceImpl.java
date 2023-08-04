package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.service.impl
 * @Author: ASUS
 * @CreateTime: 2023-08-04  21:08
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    /*
     * @description:根据时间区间统计营业额
     * @author:  HZP
     * @date: 2023/8/4 21:14
     * @param: 
     * @return:
     **/
    @Override
    public TurnoverReportVO getTurnover(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内的每天的日期
        ArrayList<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        //日期计算，获得指定日期后1天的日期
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        //将dateList集合中从begin到end范围内的每天的日期通过","进行拼接：2022-10-01,2022-10-02,2022-10-03
        String dateJoin = StringUtils.join(dateList, ",");

        //存放每天的营业额
        ArrayList<Double> turnoverList = new ArrayList<>();

        //查询date日期对应的营业额数据，营业额是指：当日状态为“已完成“的订单金额合计
        //当日是从：凌晨0:00:00~晚上24:59:59的订单金额合计
        for (LocalDate date : dateList) {
            LocalDateTime beginToday = LocalDateTime.of(date, LocalTime.MIN);//凌晨0:00:00
            LocalDateTime endToday = LocalDateTime.of(date, LocalTime.MAX);//晚上24:59:59

            //select sum(amount) from orders where order_time>beginToday and order_time<endToday and status=5
            //我们将需要插入sql语句的参数封装到map中
            Map map = new HashMap();
            map.put("begin",beginToday);
            map.put("end",endToday);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            //由于orderMapper.sumByMap(map);没查到的数据会返回null，而不是0
            turnover=turnover==null?0.0:turnover;
            turnoverList.add(turnover);
        }
        //将turnoverList集合中从日期对应的营业额数据通过","进行拼接：406.0,1520.0,75.0
        String turnoverJoin = StringUtils.join(turnoverList, ",");

        TurnoverReportVO build = TurnoverReportVO.builder()
                .dateList(dateJoin)
                .turnoverList(turnoverJoin)
                .build();
        return build;
    }
}
