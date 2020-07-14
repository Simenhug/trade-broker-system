package com.simen.tradesystem.user;

import com.simen.tradesystem.core.BaseEntity;

import javax.persistence.Entity;

@Entity
public class Role extends BaseEntity{
    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
