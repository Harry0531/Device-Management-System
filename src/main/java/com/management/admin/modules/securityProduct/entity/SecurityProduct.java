package com.management.admin.modules.securityProduct.entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;


@Data
public class SecurityProduct extends DataEntity<SecurityProduct> {
    private String department;
    private String subject;
    private String secret_number;
    private String asset_number;
    private String type;
    private String product_version;
    private String manufacturer;
    private String certificate_number;
    private String certificate_validity;
    private String serial_number;
    private String secret_level;
    private String person;
    private String place_location;
    private String scope;
    private String buy_time;
    private String enablation_time;
    private String use_situation;
    private String remarks;
    private int scrapped_flag;
    private String startTime;
    private String endTime;

    //以下为显示给用户看的，需要从其他表查得
    private String department_code;
    private String department_name;
    private String subject_code;
    private String subject_name;
    private String _type;
    private String _scope;
    private String _secret_level;
    private String _use_situation;
}
