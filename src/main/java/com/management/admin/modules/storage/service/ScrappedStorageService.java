package com.management.admin.modules.storage.service;

import com.management.admin.modules.storage.dao.ScrappedStorageDao;
import com.management.admin.modules.storage.entity.ConfidentialStorage;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScrappedStorageService {
    @Autowired
    private ScrappedStorageDao scrappedStorageDao;

    public List<String> getSubFromDict(String param) {
        return scrappedStorageDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return scrappedStorageDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return scrappedStorageDao.getDeptSub(id);
    }

    public List<ConfidentialStorage> selectDictListByPage(ConfidentialStorage confidentialStorage) {
        return scrappedStorageDao.selectDictListByPage(confidentialStorage);
    }

    public int selectSearchCount(ConfidentialStorage confidentialStorage) {
        return scrappedStorageDao.selectSearchCount(confidentialStorage);
    }

    public boolean scrap(ConfidentialStorage confidentialStorage) {
        //设报废标记
        confidentialStorage.setScrapped_flag(1);

        //判断报废时间
        Date date = confidentialStorage.getScrap_time();
        System.out.println(date);
        confidentialStorage.preScrap();
        if (date != null)
            confidentialStorage.setScrap_time(date);

        //找到报废状态字典id
        confidentialStorage.setUse_situation(scrappedStorageDao.getScrap());

        //报废
        return scrappedStorageDao.scrapStorage(confidentialStorage) == 1;
    }
}
