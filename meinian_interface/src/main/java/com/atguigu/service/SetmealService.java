package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    void add(Integer[] travelGroupIds, Setmeal formData);

    PageResult findPage(String queryString, Integer pageSize, Integer currentPage);

    void delete(Integer id);

    Setmeal getById(Integer id);

    List<Integer> getGroupIdsByMealId(Integer id);

    void edit(Integer[] ids, Setmeal setmeal);

    List<Setmeal> getAll();

    Setmeal getWholeById(Integer id);

    List<Map<String, Object>> getSetmealCount();
}
