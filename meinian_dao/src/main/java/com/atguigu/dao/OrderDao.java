package com.atguigu.dao;

import com.atguigu.pojo.Order;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    void add(Order order);

    List<Order> findOrderByCondition(Order orderParam);

    @MapKey("member")
    Map<String, Object> findOrderInfoById(Integer id);

    int getTodayOrderNumber(String reportDate);

    int getTodayVisitsNumber(String reportDate);

    int getThisWeekAndMonthOrderNumber(Map<String, Object> paramWeek);

    int getThisWeekAndMonthVisitsNumber(Map<String, Object> paramWeekVisit);

    @MapKey("name")
    List<Map<String, Object>> findHotSetmeal();

}
