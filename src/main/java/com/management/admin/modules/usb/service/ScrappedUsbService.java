package com.management.admin.modules.usb.service;

import com.management.admin.modules.sys.entity.Dept;
import com.management.admin.modules.usb.dao.ScrappedUsbDao;
import com.management.admin.modules.usb.entity.Usb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScrappedUsbService {
    @Autowired
    private ScrappedUsbDao scrappedUsbDao;

    public List<String> getSubFromDict(String param) {
        return scrappedUsbDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return scrappedUsbDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return scrappedUsbDao.getDeptSub(id);
    }

    public List<Usb> selectDictListByPage(Usb usb) {
        return scrappedUsbDao.selectDictListByPage(usb);
    }

    public int selectSearchCount(Usb usb) {
        return scrappedUsbDao.selectSearchCount(usb);
    }

    public boolean scrap(Usb usb) {
        //设报废标记
        usb.setScrapped_flag(1);

        //判断报废时间
        Date date = usb.getScrap_time();
        System.out.println(date);
        usb.preScrap();
        if (date != null)
            usb.setScrap_time(date);

        //找到报废状态字典id
       usb.setUse_situation(scrappedUsbDao.getScrap());

        //报废
        return scrappedUsbDao.scrapUsb(usb) == 1;
    }
}
