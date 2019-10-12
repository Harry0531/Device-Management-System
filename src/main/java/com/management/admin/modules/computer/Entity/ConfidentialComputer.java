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

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_secret_level() {
        return _secret_level;
    }

    public void set_secret_level(String _secret_level) {
        this._secret_level = _secret_level;
    }

    public String get_usage() {
        return _usage;
    }

    public void set_usage(String _usage) {
        this._usage = _usage;
    }

    public String get_use_situation() {
        return _use_situation;
    }

    public void set_use_situation(String _use_situation) {
        this._use_situation = _use_situation;
    }

    public String get_os_version() {
        return _os_version;
    }

    public void set_os_version(String _os_version) {
        this._os_version = _os_version;
    }

    public String get_cd_drive() {
        return _cd_drive;
    }

    public void set_cd_drive(String _cd_drive) {
        this._cd_drive = _cd_drive;
    }

    private String os_version;
    private String os_install_time;
    private String mac_address;
    private String cd_drive;


    private int scrapped_flag;
    private String startTime;
    private String endTime;

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

    //以下为uuid转换后的中文
    private String _type;
    private String _secret_level;
    private String _usage;
    private String _use_situation;
    private String _os_version;
    private String _cd_drive;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSecret_number() {
        return secret_number;
    }

    public void setSecret_number(String secret_number) {
        this.secret_number = secret_number;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getSecret_level() {
        return secret_level;
    }

    public void setSecret_level(String secret_level) {
        this.secret_level = secret_level;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getPlace_location() {
        return place_location;
    }

    public void setPlace_location(String place_location) {
        this.place_location = place_location;
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

    public String getAsset_number() {
        return asset_number;
    }

    public void setAsset_number(String asset_number) {
        this.asset_number = asset_number;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getOs_install_time() {
        return os_install_time;
    }

    public void setOs_install_time(String os_install_time) {
        this.os_install_time = os_install_time;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getCd_drive() {
        return cd_drive;
    }

    public void setCd_drive(String cd_drive) {
        this.cd_drive = cd_drive;
    }
}
