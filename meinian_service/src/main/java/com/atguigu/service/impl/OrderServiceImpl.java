package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderSettingDao orderSettingDao;

    /**
     * 判断当前日期是否可预约
     * 判断当前日期是否已约满
     * 判断是否是会员
     * 如果是，防止重复插入
     * 如果不是，自动注册会员
     * 进行预约
     * 向t_order中插入一条记录
     * t_orderSetting表里预约人数加一
     */
    @Override
    public Result saveOrder(Map<String, Object> map) throws Exception {
        Date date = DateUtils.parseString2Date((String) map.get("orderDate"));
        Integer setmealId = Integer.parseInt((String) map.get("setmealId"));
        OrderSetting orderSetting = orderSettingDao.getOrderSettingByOrderDate(date);
        if (orderSetting == null) {//日期不能进行预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {//预约已满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        String telephone = (String) map.get("telephone");
        Member member = memberDao.getMemberByTelephone(telephone);
        if (member == null) {//不存在当前会员，快速注册
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setIdCard((String) map.get("idCard"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member); //要进行主键回填
        } else {//是会员，检查防止重复预约
            //通用方法，实现动态sql，根据输入的条件不同进行不同的查询
            Order orderParam = new Order();
            orderParam.setOrderDate(date);
            orderParam.setSetmealId(setmealId);
            orderParam.setMemberId(member.getId());
            List<Order> orders = orderDao.findOrderByCondition(orderParam);
            if (orders != null && orders.size() > 0) { //存在重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setOrderType("微信预约");
        order.setOrderStatus("未出游");
        order.setSetmealId(setmealId);
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    @Override
    public Map<String, Object> findById(Integer id) throws Exception {
        Map<String, Object> map = orderDao.findOrderInfoById(id);
        //处理一下日期
        Date orderDate = (Date) map.get("orderDate");
        String parseDate2String = DateUtils.parseDate2String(orderDate, "yy-MM-dd");
        map.put("orderDate", parseDate2String);
        return map;
    }
}
