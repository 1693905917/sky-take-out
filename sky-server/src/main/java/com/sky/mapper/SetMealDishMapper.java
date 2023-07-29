package com.sky.mapper;

import com.sky.entity.SetMealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {


    /*
     * @description:根据菜品id查询关联的套餐id
     * @author:  HZP
     * @date: 2023/7/28 18:56
     * @param: 
     * @return: 
     **/
    //sql语句的格式：select setmeal id from setmeal dish where dish id in (1,2,3,4)
    List<Long> getSetMealIdsByDishIds(List<Long> ids);

    /**
     * 批量保存套餐和菜品的关联关系
     * @param setMealDishes
     */
    void insertBatch(List<SetMealDish> setMealDishes);
}
