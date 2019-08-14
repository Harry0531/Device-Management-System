package com.management.admin.modules.storage.service;

import com.management.admin.modules.storage.dao.ConfidentialStorageDao;
import com.management.admin.modules.storage.entity.ConfidentialStorage;
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

    public List<String> getSubFromDept() {
        return confidentialStorageDao.getSubFromDept();
    }

    public boolean insertStorage(ConfidentialStorage confidentialStorage) {
        confidentialStorage.preInsert();
        confidentialStorage.setDepartment_code("000");
        confidentialStorage.setSubject_code("000");
        System.out.println(confidentialStorage.getId());
        return confidentialStorageDao.insertStorage(confidentialStorage) == 1;
    }

    public boolean updateStorage(ConfidentialStorage confidentialStorage){
        confidentialStorage.preUpdate();
        confidentialStorage.setDepartment_code("000");
        confidentialStorage.setSubject_code("000");
        return confidentialStorageDao.updateStorage(confidentialStorage) == 1;
    }

    public List<ConfidentialStorage> selectDictListByPage(ConfidentialStorage confidentialStorage){
        return confidentialStorageDao.selectDictListByPage(confidentialStorage);
    }

    public int selectSearchCount(ConfidentialStorage confidentialStorage){
        return confidentialStorageDao.selectSearchCount(confidentialStorage);
    }

    public boolean deleteListByIds(List<ConfidentialStorage> list){
        return list.size()==0 || confidentialStorageDao.deleteListByIds(list)==list.size();
    }
}
