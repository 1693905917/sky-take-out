package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;





    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        //EmployeeLoginVO是封装好数据给前端来使用的
        //EmployeeLoginVO之前我们喜欢new对象，但是这里可以有另外一种方法就是EmployeeLoginVO.builder().属性名()也是与new对象作用一样
        //但是前提是需要在EmployeeLoginVO的POJO类中使用@Builder注解
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        //Result的作用，就是将我们EmployeeLoginVO再一次进行封装
        //我们后端给前端响应数据，统一用Result
        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /*
     * @description:新增员工信息
     * @author:  HZP
     * @date: 2023/7/27 15:09
     * @param:
     * @return:
     **/
    @PostMapping
    @ApiOperation("新增员工信息")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO){
        //Log.info中的{}是个占位符，employeeDTO将会在{}这个位置展示数据
        log.info("新增员工:{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /*
     * @description:分页查询员工信息
     * @author:  HZP
     * @date: 2023/7/27 16:02
     * @param:
     * @return:
     **/
    @GetMapping("/page")
    @ApiOperation("分页查询员工信息")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("分页查询：{}",employeePageQueryDTO);
        PageResult pageResult= employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /*
     * @description:启用禁用员工账号
     * @author:  HZP
     * @date: 2023/7/27 16:02
     * @param:
     * @return:
     **/
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result<String> startOrStop(@PathVariable("status") Integer status,Long id){
        log.info("启用禁用员工账号：{}，{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    /*
     * @description:根据ID查询员工信息
     * @author:  HZP
     * @date: 2023/7/27 19:04
     * @param:
     * @return:
     **/
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询员工信息")
    public Result<Employee> getById(@PathVariable("id") Long id){
        return Result.success(employeeService.getById(id));
    }

    /*
     * @description:编辑员工信息
     * @author:  HZP
     * @date: 2023/7/27 19:18
     * @param:
     * @return:
     **/
    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result<String> update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }




}
