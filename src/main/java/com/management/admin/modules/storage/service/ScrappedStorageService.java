package com.management.admin.modules.storage.service;

import com.management.admin.modules.storage.dao.ConfidentialStorageDao;
import com.management.admin.modules.storage.dao.ScrappedStorageDao;
import com.management.admin.modules.storage.entity.ConfidentialStorage;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ScrappedStorageService {
    @Autowired
    private ScrappedStorageDao scrappedStorageDao;
    @Autowired
    private ConfidentialStorageDao confidentialStorageDao;

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

    public boolean deleteListByIds(List<ConfidentialStorage> list) {
        return list.size() == 0 || scrappedStorageDao.deleteListByIds(list) == list.size();
    }

    public boolean scrap(String id, String scrapTime, String remarks) throws ParseException {
        ConfidentialStorage confidentialStorage=confidentialStorageDao.getConfidentialStorageById(id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date scrap_time = sdf.parse(scrapTime);

        confidentialStorage.preUpdate();
        confidentialStorage.setScrapped_flag(1);
        confidentialStorage.setScrap_time(scrap_time);
        confidentialStorage.setUse_situation(confidentialStorageDao.getScrap());
        confidentialStorage.setRemarks(remarks);
        return confidentialStorageDao.updateStorage(confidentialStorage) == 1;
    }
}
