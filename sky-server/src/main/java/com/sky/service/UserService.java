package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {
    /*
     * @description:根据微信授权码实现微信登录
     * @author:  HZP
     * @date: 2023/7/31 14:39
     * @param: 
     * @return: 
     **/
    User wxLogin(UserLoginDTO userLoginDTO);
}
