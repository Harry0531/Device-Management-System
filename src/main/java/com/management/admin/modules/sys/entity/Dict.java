package com.management.admin.modules.sys.entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;

@Data
public class Dict extends DataEntity<Dict> {

    //字段对应英文名字
    public String enName;
    //字典中文属性
    public  String dicProperty;
    //字典值
    public  String dicValue;
    //备注
    public  String remark;

    //父字典名字
    public  String fatherProperty;
    //排序等级
    public Integer sort;


}
