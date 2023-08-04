package com.sky.service;

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

}
