package com.management.admin.modules.tool.dao;

import com.management.admin.modules.tool.entity.ColumnMapField;

import java.util.List;

public interface ColumnMapFieldDao {

    void insertList(List<ColumnMapField> lists);


    //根据模版id查询map信息
    List<ColumnMapField> selectByTemplateId(String templateId);
}
