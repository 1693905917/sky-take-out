package com.sky.mapper;

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
}
