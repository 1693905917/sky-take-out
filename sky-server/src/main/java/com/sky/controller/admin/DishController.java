package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.controller.admin
 * @Author: ASUS
 * @CreateTime: 2023-07-28  15:14
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    
    
    /*
     * @description:批量清理符合pattern的key缓存数据
     * @author:  HZP
     * @date: 2023/8/1 9:18
     * @param: 
     * @return: 
     **/
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
    

    /*
     * @description:新增菜品
     * @author:  HZP
     * @date: 2023/7/28 16:14
     * @param:
     * @return:
     **/
    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        Long categoryId = dishDTO.getCategoryId();
        String key="dish_"+categoryId;
        cleanCache(key);
        return Result.success();
    }

    /*
     * @description:菜品分页查询
     * @author:  HZP
     * @date: 2023/7/28 16:43
     * @param:
     * @return:
     **/
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询:{}",dishPageQueryDTO);
       PageResult pageResult= dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    /*
     * @description:菜品批量删除
     * @author:  HZP
     * @date: 2023/7/28 18:48
     * @param:
     * @return:
     **/
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    ///admin/dish?ids=1,2,3  可以用springMVC的@RequestParam注解
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除:{}",ids);
        dishService.deleteBatch(ids);
        //删除所有的菜品的缓存数据
        cleanCache("dish_*");
        return Result.success();
    }

    /*
     * @description:根据id查询菜品和关联的口味数据
     * @author:  HZP
     * @date: 2023/7/28 19:28
     * @param:
     * @return:
     **/
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品和关联的口味数据")
    public Result<DishVO> getById(@PathVariable Long id){
        return Result.success(dishService.getByIdWithFlavor(id));
    }

    /*
     * @description:修改菜品
     * @author:  HZP
     * @date: 2023/7/28 19:48
     * @param:
     * @return:
     **/
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);
        //删除所有的菜品的缓存数据
        cleanCache("dish_*");
        return Result.success();
    }

    /*
     * @description:菜品起售、停售
     * @author:  HZP
     * @date: 2023/7/29 8:12
     * @param:
     * @return:
     **/
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售、停售")
    public Result<String>  startOrStop(@PathVariable Integer status,Long id){
        log.info("菜品起售、停售：{}",status);
        dishService.startOrStop(status,id);
        //删除所有的菜品的缓存数据
        cleanCache("dish_*");
        return Result.success();
    }


    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list= dishService.list(categoryId);
        return Result.success(list);
    }


}
