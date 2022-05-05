package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TravelItemService.class) //发布服务，注册到zk中心
@Transactional //声明式事务,所有方法都增加事务
public class TravelItemServiceImpl implements TravelItemService {

    @Autowired
    TravelItemDao travelItemDao;

    @Override
    public void add(TravelItem travelItem) {
        try {
            travelItemDao.add(travelItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //分页插件
        //limit (currentPage - 1)*pageSize , pageSize
        PageHelper.startPage(currentPage, pageSize);//limit ?,? 第一个问号表示开始的索引,第二个问号表示查询的条数
        Page page = travelItemDao.findPage(queryString); //返回当前页的数据
        return new PageResult(page.getTotal(), page.getResult()); //第一个参数代表总记录数,第二个参数是分页数据的集合
    }

    @Override
    public void delete(Integer id) {
        //查询自由行是否存在关联数据，是则抛出异常
        long count = travelItemDao.findCountByItemId(id);
        if (count > 0) {//存在关联数据
            throw new RuntimeException("删除自由行失败，因为存在关联数据");
        }
        travelItemDao.delete(id);
    }

    @Override
    public TravelItem getById(Integer id) {
        return travelItemDao.getById(id);
    }

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }


}
