package com.management.admin.modules.infoDevice.entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;

@Data
public class NonConfidentialInfoDevice extends DataEntity<NonConfidentialInfoDevice> {
    private String department;
    private String subject;
    private String number;
    private String asset_number;
    private String type;
    private String device_name;
    private String person;
    private String secret_level;
    private String model;
    private String device_number;
    private String disk_number;
    private String usage;
    private String place_location;
    private String enablation_time;
    private String use_situation;
    private String remarks;
    private String startTime;
    private String endTime;

    //前端显示给用户
    private String department_code;
    private String department_name;
    private String subject_code;
    private String subject_name;
    private String _type;
    private String _secret_level;
    private String _usage;
    private String _device_name;
    private String _use_situation;
}
