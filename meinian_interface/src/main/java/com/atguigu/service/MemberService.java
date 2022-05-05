package com.atguigu.service;

import com.atguigu.pojo.Member;

import java.util.List;

public interface MemberService {
    Member getByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> months);
}
