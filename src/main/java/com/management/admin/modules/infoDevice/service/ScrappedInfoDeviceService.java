package com.management.admin.modules.infoDevice.service;

import com.management.admin.modules.infoDevice.dao.InfoDeviceDao;
import com.management.admin.modules.infoDevice.dao.ScrappedInfoDeviceDao;
import com.management.admin.modules.infoDevice.entity.InfoDevice;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ScrappedInfoDeviceService {
    @Autowired
    private ScrappedInfoDeviceDao scrappedInfoDeviceDao;
    @Autowired
    private InfoDeviceDao infoDeviceDao;

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

    public boolean scrap(String id, String scrapTime, String remarks) throws ParseException {
        InfoDevice infoDevice = infoDeviceDao.getInfoDeviceById(id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date scrap_time = sdf.parse(scrapTime);

        infoDevice.preUpdate();
        infoDevice.setScrapped_flag(1);
        infoDevice.setScrap_time(scrap_time);
        infoDevice.setUse_situation(infoDeviceDao.getScrap());
        infoDevice.setRemarks(remarks);
        return infoDeviceDao.updateInfo(infoDevice) == 1;
    }

    public boolean deleteListByIds(List<InfoDevice> list) {
        return list.size() == 0 || scrappedInfoDeviceDao.deleteListByIds(list) == list.size();
    }
}
