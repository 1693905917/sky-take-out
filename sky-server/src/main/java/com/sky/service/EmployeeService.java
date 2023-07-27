package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /*
     * @description:新增员工
     * @author:  HZP
     * @date: 2023/7/27 11:21
     * @param:
     * @return:
     **/
    void save(EmployeeDTO employeeDTO);

    /*
     * @description:分页查询
     * @author:  HZP
     * @date: 2023/7/27 16:02
     * @param: 
     * @return: 
     **/
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /*
     * @description:启动禁用员工账号
     * @author:  HZP
     * @date: 2023/7/27 17:37
     * @param:
     * @return:
     **/
    void startOrStop(Integer status, Long id);

    /*
     * @description:根据id查询员工
     * @author:  HZP
     * @date: 2023/7/27 19:07
     * @param: 
     * @return: 
     **/
    Employee getById(Long id);

    /*
     * @description:根据id修改员工信息
     * @author:  HZP
     * @date: 2023/7/27 19:18
     * @param: 
     * @return: 
     **/
    void update(EmployeeDTO employeeDTO);
}
