package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetMealDTO;
import com.sky.dto.SetMealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.SetMeal;
import com.sky.entity.SetMealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetMealVO;
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

    /**
     * 分页查询
     * @param setMealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetMealPageQueryDTO setMealPageQueryDTO) {

        int pageNum = setMealPageQueryDTO.getPage();
        int pageSize = setMealPageQueryDTO.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<SetMealVO> page = setMealMapper.pageQuery(setMealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            SetMeal setMeal = setMealMapper.getById(id);
            if(StatusConstant.ENABLE == setMeal.getStatus()){
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setMealId -> {
            //删除套餐表中的数据
            setMealMapper.deleteById(setMealId);
            //删除套餐菜品关系表中的数据
            setMealMapper.deleteBySetMealId(setMealId);
        });
    }

    /**
     * 根据id查询套餐和套餐菜品关系
     *
     * @param id
     * @return
     */
    @Override
    public SetMealVO getByIdWithDish(Long id) {
        SetMeal setmeal = setMealMapper.getById(id);
        List<SetMealDish> setMealDishes = setMealDishMapper.getBySetMealId(id);

        SetMealVO setmealVO = new SetMealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setMealDishes);

        return setmealVO;
    }

    /**
     * 修改套餐
     *
     * @param setMealDTO
     */
    @Override
    @Transactional
    public void update(SetMealDTO setMealDTO) {
        SetMeal setmeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setmeal);

        //1、修改套餐表，执行update
        setMealMapper.update(setmeal);

        //套餐id
        Long setMealId = setMealDTO.getId();

        //2、删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
        setMealDishMapper.deleteBySetMealId(setMealId);

        List<SetMealDish> setMealDishes = setMealDTO.getSetmealDishes();
        setMealDishes.forEach(setMealDish -> {
            setMealDish.setSetmealId(setMealId);
        });
        //3、重新插入套餐和菜品的关联关系，操作setmeal_dish表，执行insert
        setMealDishMapper.insertBatch(setMealDishes);
    }

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        //起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        if(status == StatusConstant.ENABLE){
            //select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = ?
            List<Dish> dishList = dishMapper.getBySetMealId(id);
            if(dishList != null && dishList.size() > 0){
                dishList.forEach(dish -> {
                    if(StatusConstant.DISABLE == dish.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        SetMeal setMeal = SetMeal.builder()
                .id(id)
                .status(status)
                .build();
        setMealMapper.update(setMeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<SetMeal> list(SetMeal setmeal) {
        List<SetMeal> list = setMealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setMealMapper.getDishItemBySetMealId(id);
    }


}
