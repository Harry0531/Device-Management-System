package com.management.admin.modules.tool.entity;

import lombok.Data;

import java.util.List;
@Data
public class ExportExcel {

    /**
     * 前端需要传入的参数
     * 筛选条件设置 TODO
    */
    //导出的文件名
    private String fileName;
    //导出的数据库表名
    private String tableName;
    //导出的字段名
    private List<String> fieldList;
    //筛选条件
    private List<String> conditionList;
    //选中多项导出
    private List<String> idList;

    /**
     * 用来生成sql查询语句
    * */
    private String fieldSQL;

}
