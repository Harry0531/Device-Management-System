package com.management.admin.modules.infoDevice.service;

import com.management.admin.modules.infoDevice.dao.InfoDeviceDao;
import com.management.admin.modules.infoDevice.dao.ScrappedInfoDeviceDao;
import com.management.admin.modules.infoDevice.entity.InfoDevice;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScrappedInfoDeviceService {
    @Autowired
    private ScrappedInfoDeviceDao scrappedInfoDeviceDao;

    public List<String> getSubFromDict(String param) {
        return scrappedInfoDeviceDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return scrappedInfoDeviceDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return scrappedInfoDeviceDao.getDeptSub(id);
    }

    public List<InfoDevice> selectDictListByPage(InfoDevice infoDevice) {
        return scrappedInfoDeviceDao.selectDictListByPage(infoDevice);
    }

    public int selectSearchCount(InfoDevice infoDevice) {
        return scrappedInfoDeviceDao.selectSearchCount(infoDevice);
    }

    public boolean scrap(InfoDevice infoDevice) {
        //设报废标记
        infoDevice.setScrapped_flag(1);

        //判断报废时间
        Date date = infoDevice.getScrap_time();
        System.out.println(date);
        infoDevice.preScrap();
        if (date != null)
            infoDevice.setScrap_time(date);

        //找到报废状态字典id
        infoDevice.setUse_situation(scrappedInfoDeviceDao.getScrap());

        //报废
        return scrappedInfoDeviceDao.scrapInfo(infoDevice) == 1;
    }
}
