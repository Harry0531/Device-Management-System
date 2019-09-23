package com.management.admin.modules.tool.dao;

import java.util.List;
import java.util.Map;

public interface BackUpDao {

    List<Map<String,Object>> getTableData(String field,String tableName,String department);

    void recover(String sql);

}
