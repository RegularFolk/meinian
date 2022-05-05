package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/upload")//参数名要和el-upload中的name属性设置的一致,或者使用@RequestParam
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            //1.获取原始文件名称
            String originalFilename = imgFile.getOriginalFilename();

            //2.生成新的文件的名称(防止上传同名文件被覆盖)
            assert originalFilename != null;
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));//获取文件格式
            String newName = UUID.randomUUID() + substring;

            //3.调用工具类上传图片到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), newName);

            //***************补充代码,解决七牛云上垃圾图片的问题*********************
            //将上传图片名称存入jedis，基于jedis的set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, newName);
            //*****************************************************************



            //4.返回结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, newName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(Integer[] travelGroupIds, @RequestBody Setmeal formData) {
        try {
            setmealService.add(travelGroupIds, formData);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.findPage(queryPageBean.getQueryString(), queryPageBean.getPageSize(), queryPageBean.getCurrentPage());
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            setmealService.delete(id);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/getById")
    public Result getById(Integer id) {
        try {
            Setmeal setmeal = setmealService.getById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/getGroupIdsByMealId")
    public Result getGroupIdsByMealId(Integer id) {
        try {
            List<Integer> ids = setmealService.getGroupIdsByMealId(id);
            return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] ids, @RequestBody Setmeal setmeal) {
        try {
            setmealService.edit(ids, setmeal);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }

    }
}
