package com.sky.service;

import com.sky.dto.SetMealDTO;
import com.sky.dto.SetMealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetMealVO;

import java.util.List;

public interface SetMealService {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setMealDTO
     */
    void saveWithDish(SetMealDTO setMealDTO);

    /**
     * 分页查询
     * @param setMealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetMealPageQueryDTO setMealPageQueryDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐和关联的菜品数据
     * @param id
     * @return
     */
    SetMealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     * @param setMealDTO
     */
    void update(SetMealDTO setMealDTO);

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
