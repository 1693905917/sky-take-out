package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.service.impl
 * @Author: ASUS
 * @CreateTime: 2023-07-31  14:19
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //微信访问接口
    private static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private WeChatProperties weChatProperties;

    /*
     * @description:调用微信接口服务，获取当前微信用户的openid
     * @author:  HZP
     * @date: 2023/7/31 14:46
     * @param:
     * @return:
     **/
    private String getOpenid(String code){
        //请求参数封装
        HashMap<String,String> map = new HashMap();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        //调用工具类，向微信接口服务发送请求
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        log.info("微信登录返回结果:{}",json);
        //解析json字符串
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }

    /*
     * @description:根据微信授权码实现微信登录
     * @author:  HZP
     * @date: 2023/7/31 14:42
     * @param: 
     * @return: 
     **/
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //授权码
        String code = userLoginDTO.getCode();
        String openid = getOpenid(code);

        //判断openid,是否为空，如果为空表示登录失败，抛出业务异常
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);
        //如果是新用户，自动完成注册，也就是将新用户信息存储到数据库中
        if(user==null){
            //先把知道的值给设置出来，然后我们来将其其他User属性值在个人中心部分进行填充
            user= User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //返回这个用户对象
        return user;
    }

    
}
