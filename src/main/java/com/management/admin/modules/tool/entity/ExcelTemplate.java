package com.management.admin.modules.tool.entity;

import com.management.admin.common.persistence.DataEntity;
import com.management.admin.modules.sys.service.DictService;
import lombok.Data;

import java.util.List;

@Data
public class ExcelTemplate extends DataEntity<ExcelTemplate> {


    private String templateName;    // 模板名（导入方案名）
    private String tableName;       // 目标表名
    private String filePath;       // excel模板文件的名字(默认存放src/main/webapp/file/template)
    private String typeId;     //模板所属类型的uuid

    private String typeName;

    private List<ColumnMapField> columnMapFieldList;     // excel列到table字段的映射(以field为基准)

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<ColumnMapField> getColumnMapFieldList() {
        return columnMapFieldList;
    }

    public void setColumnMapFieldList(List<ColumnMapField> columnMapFieldList) {
        this.columnMapFieldList = columnMapFieldList;
    }
}
