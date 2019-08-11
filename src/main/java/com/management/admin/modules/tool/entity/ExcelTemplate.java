package com.management.admin.modules.tool.entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;

@Data
public class ExcelTemplate extends DataEntity<ExcelTemplate> {

    private String templateName;    // 模板名（导入方案名）
    private String tableName;       // 目标表名
    private String excelName;       // excel模板文件的名字(默认存放/WEB-INF/excelTemplate)

}
