package com.management.admin.modules.usb.entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;

@Data
public class Usb extends DataEntity<Usb> {
    private String department;
    private String subject;
    private String secret_number;
    private String type;
    private String model;
    private String person;
    private String secret_level;
    private String serial_number;
    private String place_location;
    private String usage;
    private String scope;
    private String connect_number;
    private String enablation_time;
    private String use_situation;
    private String remarks;
    private int scrapped_flag;
    private String startTime;
    private String endTime;
    //前端给用户看
    private String department_name;
    private String department_code;
    private String subject_name;
    private String subject_code;
    private String _type;
    private String _secret_level;
    private String _usage;
    private String _scope;
    private String _use_situation;
}
