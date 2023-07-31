package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.mapper
 * @Author: ASUS
 * @CreateTime: 2023-07-31  14:51
 * @Description: TODO
 * @Version: 1.0
 */
@Mapper
public interface UserMapper {

    /*
     * @description:插入用户信息
     * @author:  HZP
     * @date: 2023/7/31 14:53
     * @param: 
     * @return: 
     **/
    void insert(User user);

    /*
     * @description:根据openid查询用户
     * @author:  HZP
     * @date: 2023/7/31 14:53
     * @param: 
     * @return: 
     **/
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

}
