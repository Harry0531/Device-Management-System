package com.management.admin.modules.storage.dao;

import com.management.admin.modules.storage.entity.ConfidentialStorage;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface ScrappedStorageDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    List<ConfidentialStorage> selectDictListByPage(ConfidentialStorage confidentialStorage);
    int selectSearchCount(ConfidentialStorage confidentialStorage);
    String getScrap();
    int scrapStorage(ConfidentialStorage confidentialStorage);
}
