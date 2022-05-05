package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.interim.GroupAndItem;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {

    @Autowired
    TravelGroupDao travelGroupDao;


    private void setTravelGroupAndTravelItem(Integer travelGroupId, Integer[] travelItemIds) {
        if (travelItemIds != null && travelItemIds.length > 0) {//必须选定自由行
            //准备dao层需要的参数，使用一个简单的封装类封装参数
            for (Integer travelItemId : travelItemIds) {
                travelGroupDao.addTravelGroupAndTravelItem(new GroupAndItem(travelGroupId, travelItemId));
            }
        }
    }

    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.add(travelGroup); //此处数据库分配一个id
        Integer id = travelGroup.getId(); //需要进行主键回填，否则无法取出id
        setTravelGroupAndTravelItem(id, travelItemIds);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page page = travelGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult()); //数据封装
    }

    @Override
    public TravelGroup getById(Integer id) {
        return travelGroupDao.getById(id);
    }

    @Override
    public List<Integer> getItemIdsByGroupId(Integer id) {
        return travelGroupDao.getItemIdsByGroupId(id);
    }

    @Override
    public void edit(Integer[] ids, TravelGroup travelGroup) {
        travelGroupDao.edit(travelGroup);
        //先删除中间表的关联数据，再重新增加关联数据
        Integer id = travelGroup.getId();
        travelGroupDao.deleteInterim(id);
        setTravelGroupAndTravelItem(id, ids);
    }

    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();
    }

    @Override
    public void delete(Integer id) {
        //查询关联数据
        long count = travelGroupDao.findCountByGroupId(id);
        if (count > 0) {
            throw new RuntimeException("存在关联数据，删除失败！");
        }
        travelGroupDao.delete(id);
    }

}
