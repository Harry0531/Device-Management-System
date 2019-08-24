package com.management.admin.modules.usb.dao;

import com.management.admin.modules.sys.entity.Dept;
import com.management.admin.modules.usb.entity.Usb;

import java.util.List;

public interface UsbDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    int insertUsb(Usb usb);
    int updateUsb(Usb usb);
    List<Usb> selectDictListByPage(Usb usb);
    int selectSearchCount(Usb usb);
    int deleteListByIds(List<Usb> list);
    String getScrap();
}
