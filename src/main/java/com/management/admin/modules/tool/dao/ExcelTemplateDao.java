package com.management.admin.modules.tool.dao;

import com.management.admin.modules.tool.entity.ColumnMapField;
import com.management.admin.modules.tool.entity.ExcelTemplate;
import java.util.List;

public interface ExcelTemplateDao {

    //获取所有表名
    List<String> getTableList();
    //获取表内所有字段名
    List<String> getTableColumnList(String tableName);

    void insertOrUpdate(ExcelTemplate excelTemplate);

    void insertList(List<ColumnMapField> lists);
}
