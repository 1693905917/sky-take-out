package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.mapper
 * @Author: ASUS
 * @CreateTime: 2023-07-28  15:28
 * @Description: TODO
 * @Version: 1.0
 */
@Mapper
public interface DishFlavorMapper {

    /*
     * @description:批量插入口味数据
     * @author:  HZP
     * @date: 2023/7/28 15:29
     * @param: 
     * @return: 
     **/
    void insertBatch(List<DishFlavor> flavors);

    /*
     * @description:根据菜品id删除口味数据
     * @author:  HZP
     * @date: 2023/7/28 18:58
     * @param: 
     * @return: 
     **/
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long id);

    /*
     * @description:根据菜品id查询对应的口味
     * @author:  HZP
     * @date: 2023/7/28 19:32
     * @param: 
     * @return: 
     **/
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long id);
}
