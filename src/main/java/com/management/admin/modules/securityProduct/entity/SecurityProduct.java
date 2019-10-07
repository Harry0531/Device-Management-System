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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSecret_number() {
        return secret_number;
    }

    public void setSecret_number(String secret_number) {
        this.secret_number = secret_number;
    }

    public String getAsset_number() {
        return asset_number;
    }

    public void setAsset_number(String asset_number) {
        this.asset_number = asset_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct_version() {
        return product_version;
    }

    public void setProduct_version(String product_version) {
        this.product_version = product_version;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCertificate_number() {
        return certificate_number;
    }

    public void setCertificate_number(String certificate_number) {
        this.certificate_number = certificate_number;
    }

    public String getCertificate_validity() {
        return certificate_validity;
    }

    public void setCertificate_validity(String certificate_validity) {
        this.certificate_validity = certificate_validity;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getSecret_level() {
        return secret_level;
    }

    public void setSecret_level(String secret_level) {
        this.secret_level = secret_level;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPlace_location() {
        return place_location;
    }

    public void setPlace_location(String place_location) {
        this.place_location = place_location;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public String getEnablation_time() {
        return enablation_time;
    }

    public void setEnablation_time(String enablation_time) {
        this.enablation_time = enablation_time;
    }

    public String getUse_situation() {
        return use_situation;
    }

    public void setUse_situation(String use_situation) {
        this.use_situation = use_situation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getScrapped_flag() {
        return scrapped_flag;
    }

    public void setScrapped_flag(int scrapped_flag) {
        this.scrapped_flag = scrapped_flag;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDepartment_code() {
        return department_code;
    }

    public void setDepartment_code(String department_code) {
        this.department_code = department_code;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_scope() {
        return _scope;
    }

    public void set_scope(String _scope) {
        this._scope = _scope;
    }

    public String get_secret_level() {
        return _secret_level;
    }

    public void set_secret_level(String _secret_level) {
        this._secret_level = _secret_level;
    }

    public String get_use_situation() {
        return _use_situation;
    }

    public void set_use_situation(String _use_situation) {
        this._use_situation = _use_situation;
    }
}
