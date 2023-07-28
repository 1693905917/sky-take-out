package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    /*
     * @description:新增员工
     * @author:  HZP
     * @date: 2023/7/27 11:22
     * @param: [employeeDTO]
     * @return: void
     **/
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //将前端封装好的employeeDTO数据提交给我们的employee类对象
        //利用employee.set进行一个一个属性赋值操作很繁琐，这里教个更简便的：
        //利用spring自带的工具进行对象属性拷贝：前提是employeeDTO.与employee属性名要相同
        BeanUtils.copyProperties(employeeDTO,employee);

        //设置账号的状态，默认正常状态为1    1表示正常，0表示锁定
        employee.setStatus(StatusConstant.ENABLE);

        //设置密码，默认密码是123456，同时记得进行MD5加密   PasswordConstant.DEFAULT_PASSWORD=123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //设置当前记录的创建时间与修改时间
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        //设置当前记录创建人ID与修改人ID
        //employee.setCreateUser(BaseContext.getCurrentId());
        //employee.setUpdateUser(BaseContext.getCurrentId());

        //然后教给Mapper层进行数据的交互
        employeeMapper.insert(employee);

    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //select * from employee limit 10,20
        //基于PageHelper插件实现动态分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    /*
     * @description:启动禁用员工账号
     * @author:  HZP
     * @date: 2023/7/27 17:38
     * @param: [status, id]
     * @return: void
     **/
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();
        employeeMapper.update(employee);
    }

    @Override
    /*
     * @description:根据id查询员工
     * @author:  HZP
     * @date: 2023/7/27 19:07
     * @param: [id]
     * @return: com.sky.entity.Employee
     **/
    public Employee getById(Long id) {
        Employee employee=employeeMapper.getById(id);
        //由于通过employeeMapper.getById(id);也同时会获取到密码，所以我们为了保险起见，通过****覆盖真实密码
        employee.setPassword("****");
        return employee;
    }

    @Override
    /*
     * @description:修改员工信息
     * @author:  HZP
     * @date: 2023/7/27 19:19
     * @param: [employeeDTO]
     * @return: void
     **/
    public void update(EmployeeDTO employeeDTO) {
        // update employee set ... where id = ?
        //由于我们在修改员工状态时,employeeMapper.update()是通用的修改方法，
        // 并且传入的参数是Employee类，所以要进行类的属性转发，利用 BeanUtils.copyProperties()
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        //设置修改人和修改时间
        //BaseContext.getCurrentId()获取当时登录人的id信息
        //employee.setUpdateUser(BaseContext.getCurrentId());
        //employee.setUpdateTime(LocalDateTime.now());
        employeeMapper.update(employee);
    }

}
