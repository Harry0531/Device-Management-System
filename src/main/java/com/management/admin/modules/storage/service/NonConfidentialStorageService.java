package com.management.admin.modules.storage.service;

import com.management.admin.modules.storage.dao.NonConfidentialStorageDao;
import com.management.admin.modules.storage.entity.NonConfidentialStorage;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NonConfidentialStorageService {
    @Autowired
    private NonConfidentialStorageDao nonConfidentialStorageDao;

    public List<String> getSubFromDict(String param) {
        return nonConfidentialStorageDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return nonConfidentialStorageDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return nonConfidentialStorageDao.getDeptSub(id);
    }

    public boolean insertStorage(NonConfidentialStorage nonConfidentialStorage) {
        nonConfidentialStorage.preInsert();
        nonConfidentialStorage.setSecret_level(nonConfidentialStorageDao.getNonConfidential());
        return nonConfidentialStorageDao.insertStorage(nonConfidentialStorage) == 1;
    }

    public boolean updateStorage(NonConfidentialStorage nonConfidentialStorage) {
        nonConfidentialStorage.preUpdate();
        return nonConfidentialStorageDao.updateStorage(nonConfidentialStorage) == 1;
    }

    public List<NonConfidentialStorage> selectDictListByPage(NonConfidentialStorage nonConfidentialStorage) {
        return nonConfidentialStorageDao.selectDictListByPage(nonConfidentialStorage);
    }

    public int selectSearchCount(NonConfidentialStorage nonConfidentialStorage) {
        return nonConfidentialStorageDao.selectSearchCount(nonConfidentialStorage);
    }

    public boolean deleteListByIds(List<NonConfidentialStorage> list) {
        return list.size() == 0 || nonConfidentialStorageDao.deleteListByIds(list) == list.size();
    }
}
