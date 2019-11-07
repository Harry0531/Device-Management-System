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

    public Boolean getIsDict() {
        return isDict;
    }

    public ColumnMapField(TableField tableField) {
        tableColumnName = tableField.getFieldName();
        fieldType = tableField.getFieldType();
        columnIndex = -1;
    }
    public ColumnMapField() {

    }


    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTableColumnName() {
        return tableColumnName;
    }

    public void setTableColumnName(String tableColumnName) {
        this.tableColumnName = tableColumnName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Boolean getDict() {
        return isDict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public void setDict(Boolean dict) {
        isDict = dict;
    }


}
