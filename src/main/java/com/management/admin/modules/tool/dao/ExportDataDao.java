package com.management.admin.modules.tool.dao;

import com.management.admin.modules.tool.entity.ExportExcel;

import java.util.Map;
import java.util.List;

public interface ExportDataDao {
    // TODO 替换字典项
    public List<Map<String,Object>> getDataList(ExportExcel exportExcel);

    public  String getTypeIdByTableName(String tableName);
}
