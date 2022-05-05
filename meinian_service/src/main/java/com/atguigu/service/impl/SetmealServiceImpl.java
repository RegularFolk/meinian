package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.interim.SetmealAndTravelGroup;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    private void setSetMealAndTravelGroup(Integer[] travelGroupIds, Integer id) {
        if (travelGroupIds != null && travelGroupIds.length > 0) {
            for (Integer travelGroupId : travelGroupIds) {
                setmealDao.addSetmealAndTravelGroup(new SetmealAndTravelGroup(id, travelGroupId));
            }
        }
    }

    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    @Override
    public void add(Integer[] travelGroupIds, Setmeal formData) {
        setmealDao.add(formData);
        //往中间表存入数据,记得主键回填
        setSetMealAndTravelGroup(travelGroupIds, formData.getId());

        //*********新增代码 处理垃圾图片******
        savePic2Redis(formData.getImg());
        //********************************
    }

    @Override
    public PageResult findPage(String queryString, Integer pageSize, Integer currentPage) {
        PageHelper.startPage(currentPage, pageSize);
        Page page = setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delete(Integer id) {
        long count = setmealDao.findCountByMealId(id);
        if (count > 0) {
            throw new RuntimeException("删除失败，存在关联数据！");
        }
        setmealDao.delete(id);
    }

    @Override
    public Setmeal getById(Integer id) {
        return setmealDao.getById(id);
    }

    @Override
    public List<Integer> getGroupIdsByMealId(Integer id) {
        return setmealDao.getGroupIdsByMealId(id);
    }

    @Override
    public void edit(Integer[] ids, Setmeal setmeal) {
        setmealDao.edit(setmeal);
        Integer id = setmeal.getId();
        setmealDao.deleteInterim(id);
        setSetMealAndTravelGroup(ids, id);
    }

    @Override
    public List<Setmeal> getAll() {
        return setmealDao.getAll();
    }

    @Override
    public Setmeal getWholeById(Integer id) {
        return setmealDao.getWholeById(id);
    }

    @Override
    public List<Map<String, Object>> getSetmealCount() {
        return setmealDao.getSetmealCount();
    }

}
