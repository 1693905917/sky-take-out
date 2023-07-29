package com.sky.service;

import com.sky.dto.SetMealDTO;

public interface SetMealService {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setMealDTO
     */
    void saveWithDish(SetMealDTO setMealDTO);
}
