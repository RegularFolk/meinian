package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.interim.SetmealAndTravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void addSetmealAndTravelGroup(SetmealAndTravelGroup setmealAndTravelGroup);

    void add(Setmeal formData);

    Page findPage(@Param("queryString") String queryString);

    long findCountByMealId(Integer id);

    void delete(Integer id);

    Setmeal getById(Integer id);

    List<Integer> getGroupIdsByMealId(Integer id);

    void edit(Setmeal setmeal);

    void deleteInterim(Integer id);

    List<Setmeal> getAll();

    Setmeal getWholeById(Integer id);

    List<Map<String, Object>> getSetmealCount();
}
