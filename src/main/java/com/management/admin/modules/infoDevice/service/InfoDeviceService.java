package com.management.admin.modules.infoDevice.service;

import com.management.admin.modules.infoDevice.dao.InfoDeviceDao;
import com.management.admin.modules.infoDevice.entity.InfoDevice;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class InfoDeviceService {
    @Autowired
    private InfoDeviceDao infoDeviceDao;

    public List<String> getDeviceName(String type){
        return infoDeviceDao.getDeviceName(type);
    }

    public List<String> getSubFromDict(String param) {
        return infoDeviceDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return infoDeviceDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return infoDeviceDao.getDeptSub(id);
    }

    public boolean insertInfo(InfoDevice infoDevice) {
        infoDevice.preInsert();
        return infoDeviceDao.insertInfo(infoDevice) == 1;
    }

    public boolean updateInfo(InfoDevice infoDevice) {
        infoDevice.preUpdate();
        return infoDeviceDao.updateInfo(infoDevice) == 1;
    }

    public List<InfoDevice> selectDictListByPage(InfoDevice infoDevice) {
        return infoDeviceDao.selectDictListByPage(infoDevice);
    }

    public int selectSearchCount(InfoDevice infoDevice) {
        return infoDeviceDao.selectSearchCount(infoDevice);
    }

    public boolean deleteListByIds(List<InfoDevice> list) {
        return list.size() == 0 || infoDeviceDao.deleteListByIds(list) == list.size();
    }

    public boolean scrap(InfoDevice infoDevice) {
        infoDevice.preScrap();
        infoDevice.setScrapped_flag(1);
        infoDevice.setUse_situation(infoDeviceDao.getScrap());
        return infoDeviceDao.updateInfo(infoDevice) == 1;
    }

    public String findLabel(String id){
        return infoDeviceDao.findLabel(id);
    }
}
