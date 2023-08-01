package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetMeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.Cacheable;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.service.impl
 * @Author: ASUS
 * @CreateTime: 2023-07-28  15:19
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    /*
     * @description:新增菜品
     * @author:  HZP
     * @date: 2023/7/28 15:20
     * @param: [dishDTO]
     * @return: void
     **/
    @Transactional  //事务管理：保证要么提交  要么不提交
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品表dish插入1条数据
        dishMapper.insert(dish);
        //获取菜品的主键值
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){
            //向口味表dish_flavor插入n条
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //批量插入
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    /*
     * @description:菜品分页查询
     * @author:  HZP
     * @date: 2023/7/28 16:43
     * @param: [dishPageQueryDTO]
     * @return: com.sky.result.PageResult
     **/
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO>page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /*
     * @description:菜品批量删除
     * @author:  HZP
     * @date: 2023/7/28 19:59
     * @param:
     * @return:
     **/
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id->{
            Dish dish=dishMapper.getById(id);
            //判断当前要删除的菜品状态是否为起售中
            if(dish.getStatus() == StatusConstant.ENABLE){
                //如果是起售中，抛出业务异常
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });
        //判断当前要删除的菜品是否被套餐关联了
       List<Long> setMealIds= setMealDishMapper.getSetMealIdsByDishIds(ids);
       if(setMealIds!=null && setMealIds.size()>0){
           //如果关联了，抛出业务异常
           throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
       }

        //删除菜品表中的数据
        for (Long id : ids) {
            dishMapper.deleteById(id);
            //删除口味表中的数据
            dishFlavorMapper.deleteByDishId(id);
        }
    }

    /*
     * @description:根据id查询菜品和关联的口味数据
     * @author:  HZP
     * @date: 2023/7/28 19:59
     * @param:
     * @return:
     **/
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //查询菜品表
        Dish dish = dishMapper.getById(id);

        //查询关联的口味
        List<DishFlavor> dishFlavorList=dishFlavorMapper.getByDishId(id);

        //将上面查询到的数据封装DishVO中
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavorList);
        return dishVO;
    }

    /*
     * @description:根据id修改菜品和关联的口味
     * @author:  HZP
     * @date: 2023/7/28 19:58
     * @param: 
     * @return: 
     **/
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        //修改菜品表基本信息,也就是除了口味字段,为了方便我们用Dish类，而比赛DishDTO
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //修改菜品表基本信息
        dishMapper.update(dish);
        //对于删除口味数据，有三种情况：1.在原本口味数据上删除 2.我全部删除  3.我在原本口味数据上添加
        //其实对于上面这三种情况：我们都可以使用：先把原始口味数据都清干净 然后重新获取一遍口味数据即可
        //删除原有的口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        //重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /*
     * @description:菜品起售停售
     * @author:  HZP
     * @date: 2023/7/29 8:32
     * @param: 
     * @return: 
     **/
    @Override
    @Transactional
    public void startOrStop(Integer status,Long id) {
        Dish dish = Dish.builder().id(id).status(status).build();
        dishMapper.update(dish);
        // 如果是停售操作，还需要将包含当前菜品的套餐也停售
        if (status == StatusConstant.DISABLE) {
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setMealDishMapper.getSetMealIdsByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    SetMeal setmeal = SetMeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setMealMapper.update(setmeal);
                }
            }
        }
    }

    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
