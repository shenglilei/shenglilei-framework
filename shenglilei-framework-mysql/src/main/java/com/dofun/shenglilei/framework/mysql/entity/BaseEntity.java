package com.dofun.shenglilei.framework.mysql.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/15
 * Time:10:56
 */
@Data
public class BaseEntity implements Serializable {

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
