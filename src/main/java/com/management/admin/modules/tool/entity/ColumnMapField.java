package com.management.admin.modules.tool.entity;

import com.management.admin.common.persistence.DataEntity;
import com.management.admin.modules.tool.entity.tiny.TableField;
import lombok.Data;

@Data
public class ColumnMapField extends DataEntity<ColumnMapField> {
    // 所属模板
    private String templateId;
    // 字段信息
    private String tableColumnName;       // 字段名  也叫fieldName
    private String fieldType;       // 字段类型(varchar - String)
    // 列信息
    private String columnName;      // 列名
    private Integer columnIndex; //列序号
    //是否用字典
    private Boolean isDict;
    private String dict;


    public ColumnMapField(TableField tableField) {
        tableColumnName = tableField.getFieldName();
        fieldType = tableField.getFieldType();
        columnIndex = -1;
    }
    public ColumnMapField() {

    }



}
