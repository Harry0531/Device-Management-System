package com.management.admin.modules.sys.entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;

@Data
public class Dict extends DataEntity<Dict> {
    //字典项所属类型
    public  String typeId;
    //字典项所属类型名称
    public  String typeName;

    //字段对应英文名字
    public String enName;
    //字典中文属性
    public  String dicProperty;
    //字典值
    public  String dicValue;
    //备注
    public  String remark;
    //父字典id
    public  String fatherId;
    //父字典名字
    public  String fatherName;
}
