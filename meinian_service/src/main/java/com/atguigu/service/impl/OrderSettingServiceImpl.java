package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void addBatch(List<OrderSetting> listData) {
        //如果日期对应的设置存在就修改，否则就添加
        listData.forEach(orderSetting -> {
            int count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());
            if (count > 0) { //预约设置存在
                orderSettingDao.edit(orderSetting);
            } else {
                orderSettingDao.add(orderSetting);
            }
        });
    }

    @Override
    public List<Map<String, Object>> getOrderSettingByMonth(String date) {
        String startDate = date + "-1";
        String endDate = date + "-31";
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(startDate, endDate);
        List<Map<String, Object>> listData = new ArrayList<>();
        list.forEach(orderSetting -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", orderSetting.getOrderDate().getDate());//仅返回当前日期是在本月中的第几天
            map.put("number", orderSetting.getNumber());
            map.put("reservation", orderSetting.getReservations());
            listData.add(map);
        });
        return listData;
    }

    @Override
    public void editNumByOrderDate(OrderSetting orderSetting) {
        int count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            orderSettingDao.edit(orderSetting);
        } else {
            orderSettingDao.add(orderSetting);
        }
    }
}
