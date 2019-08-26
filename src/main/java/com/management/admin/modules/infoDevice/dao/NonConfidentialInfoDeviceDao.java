package com.management.admin.modules.infoDevice.dao;

import com.management.admin.modules.infoDevice.entity.NonConfidentialInfoDevice;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface NonConfidentialInfoDeviceDao {
    List<String> getDeviceName(String type);
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    int insertInfo(NonConfidentialInfoDevice nonConfidentialInfoDevice);
    int updateInfo(NonConfidentialInfoDevice nonConfidentialInfoDevice);
    List<NonConfidentialInfoDevice> selectDictListByPage(NonConfidentialInfoDevice nonConfidentialInfoDevice);
    int selectSearchCount(NonConfidentialInfoDevice nonConfidentialInfoDevice);
    int deleteListByIds(List<NonConfidentialInfoDevice> list);
    String findLabel(String id);
}
