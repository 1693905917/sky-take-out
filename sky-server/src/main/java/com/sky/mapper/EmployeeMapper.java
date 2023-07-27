package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /*
     * @description:插入单个员工信息数据
     * @author:  HZP
     * @date: 2023/7/27 11:27
     * @param:
     * @return:
     **/
    @Insert("insert into employee" +
            "(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)" +
            "VALUES " +"(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime},#{updateTime},#{createUser}, #{updateUser})")
    //注意：数据库的id_number.与employee类中的idNumber不一样
    void insert(Employee employee);

    //由于我们需要用到动态sql，所以我们就不用注解方式，而是将sql语句写到（EmployeeMapper.xml）映射文件中
    /*
     * @description:分页查询
     * @author:  HZP
     * @date: 2023/7/27 17:41
     * @param: [employeePageQueryDTO]
     * @return: com.github.pagehelper.Page<com.sky.entity.Employee>
     **/
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    //由于我们需要用到动态sql，所以我们就不用注解方式，而是将sql语句写到（EmployeeMapper.xml）映射文件中
    /*
     * @description:根据id修改员工信息
     * @author:  HZP
     * @date: 2023/7/27 17:40
     * @param: [employee]
     * @return: void
     **/
    void update(Employee employee);

    /*
     * @description:根据id查询员工
     * @author:  HZP
     * @date: 2023/7/27 19:09
     * @param: 
     * @return: 
     **/
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}
