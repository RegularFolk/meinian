package com.atguigu.dao;

import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface TravelItemDao {

    void add(TravelItem travelItem);

    Page findPage(String queryString);

    void delete(Integer id);


    TravelItem getById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();

    long findCountByItemId(Integer id);

    /**
     * 帮助封装跟团游的travelItems属性方法 id为跟团游id 返回group对应的所有items
     */
    List<TravelItem> findItemIdsByGroupId(Integer id);

}
