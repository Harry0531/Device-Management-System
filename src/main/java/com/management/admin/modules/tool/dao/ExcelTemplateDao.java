package com.management.admin.modules.tool.dao;

import com.management.admin.modules.tool.entity.ColumnMapField;
import com.management.admin.modules.tool.entity.DynamicInsertParam;
import com.management.admin.modules.tool.entity.ExcelTemplate;
import com.management.admin.modules.tool.entity.tiny.TableField;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExcelTemplateDao {

    //获取所有表名
    List<String> getTableList();
    //获取表内所有字段名
    List<String> getTableColumnList(String tableName);

    //插入模版
    void insertOrUpdate(ExcelTemplate excelTemplate);

    void insertList(List<ColumnMapField> lists);

    //查询所有模版名称
    List<ExcelTemplate> selectAllTemplate(ExcelTemplate condition);

    //查询单个模版信息
    ExcelTemplate selectById(String id);

    //根据模版id查询map信息
    List<ColumnMapField> selectByTemplateId(String templateId);



    // 获取指定表中的所有列信息
    List<TableField> selectFieldListByTableName(@Param("tableName") String tableName);
    //根据Excel数据插入数据库
    int dynamicInsert(DynamicInsertParam dynamicInsertParam);

}
