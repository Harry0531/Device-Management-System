package com.management.admin.modules.tool.entity.tiny;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
public class PartInfo {
    String name;
    String code;
    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
