package com.xecoder.model.business;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2015/11/30-13:20
 * habit-team-server.com.imakehabits.model.embedded
 * 用户好友分组
 */
@Document(collection = "friend_grouping")
public class FriendGrouping {
    @Id
    private String id;

    /**
     * 用户id
     */
    @Field(value = "user_id")
    private String userId;

    /**
     * 分组
     */
    @Field(value = "grouping")
    private String grouping;


    /**
     * 排序
     */
    @Field(value = "sort")
    private int sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
