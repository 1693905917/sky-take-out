package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /*
     * @description:添加购物车
     * @author:  HZP
     * @date: 2023/8/1 12:56
     * @param: 
     * @return: 
     **/
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /*
     * @description:查看购物车
     * @author:  HZP
     * @date: 2023/8/1 13:38
     * @param: 
     * @return: 
     **/
    List<ShoppingCart> showShoppingCart();
}
