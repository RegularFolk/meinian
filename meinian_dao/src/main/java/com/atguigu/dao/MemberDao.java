package com.atguigu.dao;

import com.atguigu.pojo.Member;

public interface MemberDao {
    Member getMemberByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(String month);

    int getTodayNewMember(String reportDate);

    int getTotalMember();

    int getThisWeekAndMonthNewMember(String weekMonday);
}
