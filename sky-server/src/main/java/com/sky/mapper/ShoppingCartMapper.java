package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /*
     * @description:条件查询
     * @author:  HZP
     * @date: 2023/8/1 13:01
     * @param: 
     * @return: 
     **/
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /*
     * @description:更新商品数量
     * @author:  HZP
     * @date: 2023/8/1 13:02
     * @param: 
     * @return: 
     **/
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /*
     * @description:插入购物车数据
     * @author:  HZP
     * @date: 2023/8/1 13:02
     * @param: 
     * @return: 
     **/
    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            " values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /*
     * @description:根据用户id删除购物车数据
     * @author:  HZP
     * @date: 2023/8/1 13:43
     * @param: 
     * @return: 
     **/
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long currentId);
}
