package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
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


    @GetMapping("/userStatistics")
    @ApiOperation("用户数据统计")
    public Result<UserReportVO>  userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        UserReportVO userStatistics = reportService.getUserStatistics(begin,end);
        return Result.success(userStatistics);
    }


}
