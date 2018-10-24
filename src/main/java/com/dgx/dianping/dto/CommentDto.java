package com.dgx.dianping.dto;

import com.dgx.dianping.pojo.Comment;

public class CommentDto extends Comment{

	/**
     * 隐藏中间4位的手机号
     */
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
