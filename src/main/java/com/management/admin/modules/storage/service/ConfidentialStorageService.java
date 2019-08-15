package com.management.admin.modules.storage.service;

import com.management.admin.modules.storage.dao.ConfidentialStorageDao;
import com.management.admin.modules.storage.entity.ConfidentialStorage;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfidentialStorageService {
    @Autowired
    private ConfidentialStorageDao confidentialStorageDao;

    public List<String> getSubFromDict(String param) {
        return confidentialStorageDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return confidentialStorageDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return confidentialStorageDao.getDeptSub(id);
    }

    public boolean insertStorage(ConfidentialStorage confidentialStorage) {
        confidentialStorage.preInsert();
        return confidentialStorageDao.insertStorage(confidentialStorage) == 1;
    }

    public boolean updateStorage(ConfidentialStorage confidentialStorage) {
        confidentialStorage.preUpdate();
        return confidentialStorageDao.updateStorage(confidentialStorage) == 1;
    }

    public List<ConfidentialStorage> selectDictListByPage(ConfidentialStorage confidentialStorage) {
        return confidentialStorageDao.selectDictListByPage(confidentialStorage);
    }

    public int selectSearchCount(ConfidentialStorage confidentialStorage) {
        return confidentialStorageDao.selectSearchCount(confidentialStorage);
    }

    public boolean deleteListByIds(List<ConfidentialStorage> list) {
        return list.size() == 0 || confidentialStorageDao.deleteListByIds(list) == list.size();
    }

    public boolean scrap(ConfidentialStorage confidentialStorage) {
        confidentialStorage.preScrap();
        confidentialStorage.setScrapped_flag(1);
        confidentialStorage.setUse_situation(confidentialStorageDao.getScrap());
        return confidentialStorageDao.updateStorage(confidentialStorage) == 1;
    }
}
