package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import com.github.pagehelper.PageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    AddressService addressService;

    @RequestMapping("/findAllMaps")
    public Map<String, Object> findAllMaps() {
        Map<String, Object> map = new HashMap<>();

        List<Address> list = addressService.findAllMaps();

        List<Map<String, String>> gridMaps = new ArrayList<>(); //标记地址的经纬度
        List<Map.Entry<String, String>> nameMaps = new ArrayList<>();//标记地址的名称

        list.forEach(address -> {
            Map.Entry<String, String> mapName = new AbstractMap.SimpleEntry<>("addressName", address.getAddressName());
            nameMaps.add(mapName);
            Map<String, String> gridMap = new HashMap<>();
            gridMap.put("lng", address.getLng());
            gridMap.put("lat", address.getLat());
            gridMaps.add(gridMap);
        });

        map.put("gridMaps", gridMaps);
        map.put("nameMaps", nameMaps);
        return map;
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return addressService.findPage(queryPageBean);
    }

    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address) {
        try {
            addressService.add(address);
            return new Result(true, MessageConstant.ADD_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ADDRESS_FAIL);
        }
    }

    @RequestMapping("/deleteById")
    public Result deleteById(Integer id) {
        try {
            addressService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ADDRESS_FAIL);
        }
    }
}
