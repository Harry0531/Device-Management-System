package com.management.admin.modules.tool.entity.tiny;

import lombok.Data;

@Data
public class DictInfo {
    String dicProperty;
    String dicValue;
    String id;

    public String getDicProperty() {
        return dicProperty;
    }

    public void setDicProperty(String dicProperty) {
        this.dicProperty = dicProperty;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
