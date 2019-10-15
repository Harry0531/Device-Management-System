package com.management.admin.modules.tool.dao;

import com.management.admin.modules.tool.entity.ColumnMapField;
import com.management.admin.modules.tool.entity.DynamicInsertParam;
import com.management.admin.modules.tool.entity.ExcelTemplate;
import com.management.admin.modules.tool.entity.tiny.TableField;
import com.management.admin.modules.tool.entity.tiny.TemplateType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExcelTemplateDao {

    List<TemplateType> getTypeList();
    //获取所有表名
    List<String> getTableList();
    //获取表内所有字段名
    List<String> getTableColumnList(String tableName);

    //插入模版
    void insertOrUpdate(ExcelTemplate excelTemplate);


    //查询所有模版名称
    List<ExcelTemplate> selectAllTemplate(ExcelTemplate condition);

    //查询单个模版信息
    ExcelTemplate selectById(String id);

    public void deleteByIds(List<String> ids);
    public void deleteByIds2(List<String> ids);



}
