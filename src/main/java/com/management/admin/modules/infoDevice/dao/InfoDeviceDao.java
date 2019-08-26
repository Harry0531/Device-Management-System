package com.management.admin.modules.infoDevice.dao;

import com.management.admin.modules.infoDevice.entity.InfoDevice;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface InfoDeviceDao {
    List<String> getDeviceName(String type);
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    int insertInfo(InfoDevice infoDevice);
    int updateInfo(InfoDevice infoDevice);
    List<InfoDevice> selectDictListByPage(InfoDevice infoDevice);
    int selectSearchCount(InfoDevice infoDevice);
    int deleteListByIds(List<InfoDevice> list);
    String getScrap();
    String findLabel(String id);
}
