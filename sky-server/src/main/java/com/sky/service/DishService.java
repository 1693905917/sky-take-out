package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /*
     * @description:新增菜品
     * @author:  HZP
     * @date: 2023/7/28 16:14
     * @param:
     * @return:
     **/
    void saveWithFlavor(DishDTO dishDTO);

    /*
     * @description:菜品分页查询
     * @author:  HZP
     * @date: 2023/7/28 16:43
     * @param:
     * @return:
     **/
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
     * @description:菜品批量删除
     * @author:  HZP
     * @date: 2023/7/28 18:48
     * @param:
     * @return:
     **/
    void deleteBatch(List<Long> ids);

    /*
     * @description:根据id查询菜品和关联的口味数据
     * @author:  HZP
     * @date: 2023/7/28 19:29
     * @param: 
     * @return: 
     **/
    DishVO getByIdWithFlavor(Long id);

    /*
     * @description:修改菜品
     * @author:  HZP
     * @date: 2023/7/28 19:48
     * @param:
     * @return:
     **/
    void updateWithFlavor(DishDTO dishDTO);
}
