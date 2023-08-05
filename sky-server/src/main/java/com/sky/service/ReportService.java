package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;


public interface ReportService {

    /*
     * @description:根据时间区间统计营业额
     * @author:  HZP
     * @date: 2023/8/4 21:08
     * @param: 
     * @return:
     **/
    TurnoverReportVO getTurnover(LocalDate begin, LocalDate end);

    /*
     * @description:根据时间区间统计用户数量
     * @author:  HZP
     * @date: 2023/8/4 22:30
     * @param: 
     * @return: 
     **/
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /*
     * @description:根据时间区间统计订单数量
     * @author:  HZP
     * @date: 2023/8/5 9:13
     * @param: 
     * @return: 
     **/
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /*
     * @description:查询指定时间区间内的销量排名top10
     * @author:  HZP
     * @date: 2023/8/5 10:12
     * @param: 
     * @return: 
     **/
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
}
