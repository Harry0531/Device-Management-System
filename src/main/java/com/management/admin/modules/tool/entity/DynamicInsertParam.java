package com.management.admin.modules.tool.entity;

import lombok.Data;

import java.util.List;

/**
 * 用于动态插入语句的参数
 */
@Data
public class DynamicInsertParam {

    private String tableName;
    private List<String> fieldList;
    private List<List<Object>> data;

}
