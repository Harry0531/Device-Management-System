package com.management.admin.modules.infoDevice.dao;

import com.management.admin.modules.infoDevice.entity.InfoDevice;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface ScrappedInfoDeviceDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    List<InfoDevice> selectDictListByPage(InfoDevice infoDevice);
    int selectSearchCount(InfoDevice infoDevice);
    String getScrap();
    int scrapInfo(InfoDevice infoDevice);
}
