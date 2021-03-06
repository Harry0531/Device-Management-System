package com.management.admin.modules.usb.service;

import com.management.admin.modules.sys.entity.Dept;
import com.management.admin.modules.usb.dao.UsbDao;
import com.management.admin.modules.usb.entity.Usb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UsbService {
    @Autowired
    private UsbDao usbDao;

    public List<String> getSubFromDict(String param) {
        return usbDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return usbDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return usbDao.getDeptSub(id);
    }

    public boolean insertUsb(Usb usb) {
        usb.preInsert();
        return usbDao.insertUsb(usb) == 1;
    }

    public boolean updateUsb(Usb usb) {
        usb.preUpdate();
        return usbDao.updateUsb(usb) == 1;
    }

    public List<Usb> selectDictListByPage(Usb usb) {
        return usbDao.selectDictListByPage(usb);
    }

    public int selectSearchCount(Usb usb) {
        return usbDao.selectSearchCount(usb);
    }

    public boolean deleteListByIds(List<Usb> list) {
//        return list.size() == 0 || usbDao.deleteListByIds(list) == list.size();
        for(Usb usb:list){
            if(usb.get_secret_level().equals("非密"))
                usbDao.remove(usb);
            else
                usbDao.delete(usb);
        }
        return true;
    }

    public boolean scrap(String id, String scrapTime, String remarks) throws ParseException {
        Usb usb = usbDao.getUsbById(id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date scrap_time = sdf.parse(scrapTime);

        usb.preUpdate();
        usb.setScrapped_flag(1);
        usb.setScrap_time(scrap_time);
        usb.setUse_situation(usbDao.getScrap());
        usb.setRemarks(remarks);
        return usbDao.updateUsb(usb) == 1;
    }
}
