package com.management.admin.modules.tool.entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;

@Data
public class ColumnMapField extends DataEntity<ColumnMapField> {
    // 所属模板
    private String templateId;
    // 字段信息
    private String fieldName;       // 字段名
    // 列信息
    private String columnName;      // 列名



}
