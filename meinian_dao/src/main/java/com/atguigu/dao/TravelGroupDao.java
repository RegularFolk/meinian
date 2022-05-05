package com.atguigu.dao;

import com.atguigu.pojo.interim.GroupAndItem;
import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface TravelGroupDao {
    void add(TravelGroup travelGroup);

    void addTravelGroupAndTravelItem(GroupAndItem groupAndItem);

    Page findPage(@Param("queryString") String queryString);

    TravelGroup getById(Integer id);

    List<Integer> getItemIdsByGroupId(Integer id);

    void edit(TravelGroup travelGroup);

    void deleteInterim(Integer id);

    List<TravelGroup> findAll();

    long findCountByGroupId(Integer id);

    void delete(Integer id);

    /**
     * 帮助套餐查询关联数据
     */
    List<TravelGroup> findGroupsBySetMealId(Integer id);
}
