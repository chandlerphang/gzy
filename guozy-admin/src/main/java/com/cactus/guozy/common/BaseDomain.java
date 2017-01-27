package com.cactus.guozy.common;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 实体基类
 * @author Chaven Peng
 */
public class BaseDomain implements Serializable {

    private static final long serialVersionUID = -5386818304619762871L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * 获取主键ID
     *
     * @return id - ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }
}
