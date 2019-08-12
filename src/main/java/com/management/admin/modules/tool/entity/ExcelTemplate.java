package com.management.admin.modules.tool.entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;

import java.util.List;

@Data
public class ExcelTemplate extends DataEntity<ExcelTemplate> {

    private String templateName;    // 模板名（导入方案名）
    private String tableName;       // 目标表名
    private String excelName;       // excel模板文件的名字(默认存放src/main/webapp/file/template)

    // 此为临时数据，不存在表中，而是来自客户端
    private String excelDataName;   // 存放数据的excel文件的名字(默认存放在/src/main/webapp/file/temporary)

    private List<ColumnMapField> columnMapFieldList;     // excel列到table字段的映射(以field为基准)
}
