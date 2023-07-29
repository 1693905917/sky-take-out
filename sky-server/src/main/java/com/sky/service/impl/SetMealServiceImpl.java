package com.sky.service.impl;

import com.sky.dto.SetMealDTO;
import com.sky.entity.SetMeal;
import com.sky.entity.SetMealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.service.impl
 * @Author: ASUS
 * @CreateTime: 2023-07-29  09:16
 * @Description: TODO
 * @Version: 1.0
 */
/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setMealDTO
     */
    @Transactional
    public void saveWithDish(SetMealDTO setMealDTO) {
        SetMeal setmeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setmeal);

        //向套餐表插入数据
        setMealMapper.insert(setmeal);

        //获取生成的套餐id
        Long setMealId = setmeal.getId();

        List<SetMealDish> setMealDishes = setMealDTO.getSetmealDishes();
        setMealDishes.forEach(setMealDish -> {
            setMealDish.setSetmealId(setMealId);
        });

        //保存套餐和菜品的关联关系
        setMealDishMapper.insertBatch(setMealDishes);
    }
}
