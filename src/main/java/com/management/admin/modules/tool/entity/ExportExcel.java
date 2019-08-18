package com.management.admin.modules.tool.entity;

import com.management.admin.modules.tool.entity.tiny.TableField;
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
    private String templateId;
    //是否是报废表
    private boolean iScrapped;
    //导出的字段名
    private List<TableField> fieldList;
    //筛选条件
    private List<String> conditionsList;
    //选中多项导出
    private List<String> idList;

    private String tableName;
    /**
     * 用来生成sql查询语句
    * */
    private String fieldSQL;

}
