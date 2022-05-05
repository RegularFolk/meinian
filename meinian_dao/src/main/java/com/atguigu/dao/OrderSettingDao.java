package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {

    void add(OrderSetting orderSetting);

    int findOrderSettingByOrderDate(Date orderDate);

    void edit(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(@Param("startDate") String startDate,@Param("endDate") String endDate);

    OrderSetting getOrderSettingByOrderDate(Date date);

    void editReservationsByDate(OrderSetting orderSetting);
}
