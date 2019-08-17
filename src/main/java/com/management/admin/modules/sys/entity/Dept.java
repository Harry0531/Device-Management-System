package com.management.admin.modules.sys.entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;

@Data
public class Dept extends DataEntity<Dept> {
    private String dept_name;
    private String dept_code;
    private int dept_type;
    private String dept_attach;
    private String dept_property;

    //前缀“_”为显示给用户的
    private String _dept_type;
    private String _dept_attach;
}
