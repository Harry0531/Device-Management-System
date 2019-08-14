package com.management.admin.modules.tool.entity;

import com.management.admin.common.persistence.DataEntity;
import javafx.scene.chart.PieChart;
import lombok.Data;

import java.util.List;

@Data
public class ExcelData extends DataEntity<ExcelData> {

    private String templateName;    // 模板名（导入方案名）
    private String tableName;       // 目标表名
    private String filePath;       // excel模板文件的名字(默认存放src/main/webapp/file/template)
    private String excelDataName; //数据excel文件名
    private String typeId;  //模板所属类型的uuid

    private List<ColumnMapField> columnMapFieldList;     // excel列到table字段的映射(以field为基准)
}
