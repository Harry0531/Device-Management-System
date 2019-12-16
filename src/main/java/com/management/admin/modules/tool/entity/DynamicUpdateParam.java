package com.management.admin.modules.tool.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用于动态更新语句的参数
 */
@Data
public class DynamicUpdateParam {

    private String tableName;
    private String primaryKey;
    private List<String> fieldList;
    private List<Object> data;
    private String primaryName;
    private List<String> value;
    private Date date;

    public void makeValue(){
        this.value = new ArrayList<>();
        for(int i = 0; i < fieldList.size(); i++) {
            if(this.data.get(i) == null || this.fieldList.get(i).equals("create_time") || this.fieldList.get(i).equals("modify_time")){
                continue;
            }
            this.value.add(this.fieldList.get(i) + " = '" + this.data.get(i).toString() + "'");
        }
    }
}
