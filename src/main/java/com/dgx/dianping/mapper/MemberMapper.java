package com.dgx.dianping.mapper;

import java.util.List;

import com.dgx.dianping.pojo.Member;

public interface MemberMapper {

	/**
	 * 根据查询条件查询会员列表
	 * @param member
	 * @return
	 */
	List<Member> select(Member member);
}
