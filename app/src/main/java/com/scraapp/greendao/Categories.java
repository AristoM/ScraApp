package com.scraapp.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Categories {

    @Id
    private Long primaryKey;

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @Generated(hash = 726340467)
    public Categories(Long primaryKey, @NotNull String id, @NotNull String name,
            @NotNull String description) {
        this.primaryKey = primaryKey;
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Generated(hash = 267348489)
    public Categories() {
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrimaryKey() {
        return this.primaryKey;
    }

    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }
}
