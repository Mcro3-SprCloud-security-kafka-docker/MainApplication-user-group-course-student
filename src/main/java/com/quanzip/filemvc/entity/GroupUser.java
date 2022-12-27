package com.quanzip.filemvc.entity;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
@Table(name = "group_user")
@EnableJpaAuditing
public class GroupUser extends BaseEntity{
    private Long groupId;
    private Long userId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
