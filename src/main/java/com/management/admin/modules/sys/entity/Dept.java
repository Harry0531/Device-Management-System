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
    private int sort;

    //前缀“_”为显示给用户的
    private String _dept_type;
    private String _dept_attach;

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getDept_code() {
        return dept_code;
    }

    public void setDept_code(String dept_code) {
        this.dept_code = dept_code;
    }

    public int getDept_type() {
        return dept_type;
    }

    public void setDept_type(int dept_type) {
        this.dept_type = dept_type;
    }

    public String getDept_attach() {
        return dept_attach;
    }

    public void setDept_attach(String dept_attach) {
        this.dept_attach = dept_attach;
    }

    public String getDept_property() {
        return dept_property;
    }

    public void setDept_property(String dept_property) {
        this.dept_property = dept_property;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String get_dept_type() {
        return _dept_type;
    }

    public void set_dept_type(String _dept_type) {
        this._dept_type = _dept_type;
    }

    public String get_dept_attach() {
        return _dept_attach;
    }

    public void set_dept_attach(String _dept_attach) {
        this._dept_attach = _dept_attach;
    }
}
