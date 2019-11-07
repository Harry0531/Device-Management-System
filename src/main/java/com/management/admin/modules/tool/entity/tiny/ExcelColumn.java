package com.management.admin.modules.tool.entity.tiny;

import lombok.Data;

@Data
public class ExcelColumn {
    private String columnName;      // 列名
    private int columnIndex;     // 列序号

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }
}
