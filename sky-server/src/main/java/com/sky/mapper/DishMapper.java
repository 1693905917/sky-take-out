package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /*
     * @description:插入菜品数据
     * @author:  HZP
     * @date: 2023/7/28 15:26
     * @param: 
     * @return: 
     **/
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /*
     * @description:菜品分页查询
     * @author:  HZP
     * @date: 2023/7/28 16:46
     * @param: 
     * @return: 
     **/
    Page<DishVO>pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
     * @description:根据主键查询菜品数据
     * @author:  HZP
     * @date: 2023/7/28 18:53
     * @param: 
     * @return: 
     **/
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /*
     * @description:根据主键删除菜品数据
     * @author:  HZP
     * @date: 2023/7/28 18:58
     * @param: 
     * @return: 
     **/
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /*
     * @description:根据主键修改菜品信息
     * @author:  HZP
     * @date: 2023/7/28 19:57
     * @param: 
     * @return: 
     **/
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 动态条件查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     * @param setMealId
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setMealId}")
    List<Dish> getBySetMealId(Long setMealId);
}
