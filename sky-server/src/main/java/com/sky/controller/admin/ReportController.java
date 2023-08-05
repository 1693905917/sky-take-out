package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.controller.admin
 * @Author: ASUS
 * @CreateTime: 2023-08-04  21:04
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags="统计报表相关接口")
public class ReportController {

    @Autowired
    private ReportService reportService;
    /*
     * @description:营业额数据统计
     * @author:  HZP
     * @date: 2023/8/4 21:09
     * @param:
     * @return: 
     **/
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额数据统计")
    public Result<TurnoverReportVO>  turnoverStatistics(
            //由于我们前端转入的日期数据格式：2022-10-01,2022-10-02,2022-10-03
            //所以要规定好我们后端接受日期数据的格式： @DateTimeFormat(pattern = "yyyy-MM-dd")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        TurnoverReportVO turnover = reportService.getTurnover(begin, end);
        return Result.success(turnover);
    }


    /*
     * @description:根据时间区间统计用户数量
     * @author:  HZP
     * @date: 2023/8/4 22:30
     * @param:
     * @return:
     **/
    @GetMapping("/userStatistics")
    @ApiOperation("用户数据统计")
    public Result<UserReportVO>  userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        UserReportVO userStatistics = reportService.getUserStatistics(begin,end);
        return Result.success(userStatistics);
    }

    /*
     * @description:订单数据统计
     * @author:  HZP
     * @date: 2023/8/5 9:12
     * @param: 
     * @return: 
     **/
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单数据统计")
    public Result<OrderReportVO>  ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        OrderReportVO orderReportVO = reportService.getOrderStatistics(begin,end);
        return Result.success(orderReportVO);
    }

    /*
     * @description:销量排名统计
     * @author:  HZP
     * @date: 2023/8/5 9:12
     * @param:
     * @return:
     **/
    @GetMapping("/top10")
    @ApiOperation("销量排名统计")
    public Result<SalesTop10ReportVO>  top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        SalesTop10ReportVO salesTop10ReportVO = reportService.getSalesTop10(begin,end);
        return Result.success(salesTop10ReportVO);
    }

    /*
     * @description:导出运营数据报表
     * @author:  HZP
     * @date: 2023/8/5 15:29
     * @param: 
     * @return: 
     **/
    @GetMapping("/export")
    @ApiOperation("导出运营数据报表")
    //我们通过response将数据已经封装好的Excel文件下载到客服端
    public void export(HttpServletResponse response){
        reportService.exportBusinessData(response);
    }




}
