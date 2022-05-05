package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    JedisPool jedisPool;

    @Reference
    MemberService memberService;

    @RequestMapping("/check")
    public Result check(@RequestBody Map<String, Object> loginInfo, HttpServletResponse response) {
        String telephone = (String) loginInfo.get("telephone");
        String validateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCode == null || !validateCode.equals(loginInfo.get("validateCode"))) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Member member = memberService.getByTelephone(telephone);
        if (member == null) { //用户不存在则自动创建一个用户
            member = new Member();
            member.setRegTime(new Date("yy-MM-dd"));
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }
        //登陆成功,可以移除redis中存储的验证码
        jedisPool.getResource().del(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //写入Cookie，跟踪用户
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setPath("/");//路径
        cookie.setMaxAge(60 * 60 * 24 * 30);//有效期30天（单位秒）
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
