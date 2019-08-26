package com.management.admin.modules.infoDevice.service;

import com.management.admin.modules.infoDevice.dao.NonConfidentialInfoDeviceDao;
import com.management.admin.modules.infoDevice.entity.NonConfidentialInfoDevice;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NonConfidentialInfoDeviceService {
    @Autowired
    private NonConfidentialInfoDeviceDao nonConfidentialInfoDeviceDao;

    public List<String> getDeviceName(String type){
        return nonConfidentialInfoDeviceDao.getDeviceName(type);
    }

    public List<String> getSubFromDict(String param) {
        return nonConfidentialInfoDeviceDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return nonConfidentialInfoDeviceDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return nonConfidentialInfoDeviceDao.getDeptSub(id);
    }

    public boolean insertInfo(NonConfidentialInfoDevice nonConfidentialInfoDevice) {
        nonConfidentialInfoDevice.preInsert();
        return nonConfidentialInfoDeviceDao.insertInfo(nonConfidentialInfoDevice) == 1;
    }

    public boolean updateInfo(NonConfidentialInfoDevice nonConfidentialInfoDevice) {
        nonConfidentialInfoDevice.preUpdate();
        return nonConfidentialInfoDeviceDao.updateInfo(nonConfidentialInfoDevice) == 1;
    }

    public List<NonConfidentialInfoDevice> selectDictListByPage(NonConfidentialInfoDevice nonConfidentialInfoDevice) {
        return nonConfidentialInfoDeviceDao.selectDictListByPage(nonConfidentialInfoDevice);
    }

    public int selectSearchCount(NonConfidentialInfoDevice nonConfidentialInfoDevice) {
        return nonConfidentialInfoDeviceDao.selectSearchCount(nonConfidentialInfoDevice);
    }

    public boolean deleteListByIds(List<NonConfidentialInfoDevice> list) {
        return list.size() == 0 || nonConfidentialInfoDeviceDao.deleteListByIds(list) == list.size();
    }

    public String findLabel(String id){
        return nonConfidentialInfoDeviceDao.findLabel(id);
    }
}
