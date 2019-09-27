package com.management.admin.modules.usb.dao;

import com.management.admin.modules.sys.entity.Dept;
import com.management.admin.modules.usb.entity.Usb;

import java.util.List;

public interface ScrappedUsbDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    List<Usb> selectDictListByPage(Usb usb);
    int selectSearchCount(Usb usb);
    String getScrap();
    int scrapUsb(Usb usb);
    int deleteListByIds(List<Usb> list);
    int remove(Usb usb);
    int delete(Usb usb);
}
