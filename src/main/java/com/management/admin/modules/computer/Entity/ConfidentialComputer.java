package com.management.admin.modules.computer.Entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;

@Data
public class ConfidentialComputer extends DataEntity<ConfidentialComputer> {
    private String department;
    private String subject;
    private String type;
    private String secret_number;
    private String person;
    private String secret_level;
    private String model;
    private String serial_number;
    private String usage;
    private String place_location;

    private String department_code;
    private String department_name;
    private String subject_code;
    private String subject_name;
    private String enablation_time;
    private String use_situation;
    private String remarks;
        //新添
    private String asset_number;
    private String os_version;
    private String os_install_time;
    private String mac_address;
    private String cd_drive;


    private int scrapped_flag;
    private String startTime;
    private String endTime;

    //以下为uuid转换后的中文
    private String _type;
    private String _secret_level;
    private String _usage;
    private String _use_situation;
    private String _os_version;
    private String _cd_drive;
}
