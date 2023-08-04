package com.sky.service;

import com.sky.vo.TurnoverReportVO;

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

}
