package com.management.admin.modules.storage.dao;

import com.management.admin.modules.storage.entity.ConfidentialStorage;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface ConfidentialStorageDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    int insertStorage(ConfidentialStorage confidentialStorage);
    int updateStorage(ConfidentialStorage confidentialStorage);
    List<ConfidentialStorage> selectDictListByPage(ConfidentialStorage confidentialStorage);
    int selectSearchCount(ConfidentialStorage confidentialStorage);
    int deleteListByIds(List<ConfidentialStorage> list);
    String getScrap();
    ConfidentialStorage getConfidentialStorageById(String id);
}
