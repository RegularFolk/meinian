package com.atguigu.service;

import com.atguigu.entity.Result;

import java.util.Map;

public interface OrderService {
    Result saveOrder(Map<String, Object> map) throws Exception;

    Map<String, Object> findById(Integer id) throws Exception;
}
