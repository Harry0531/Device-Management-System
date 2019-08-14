package com.management.admin.modules.storage.dao;

import com.management.admin.modules.storage.entity.ConfidentialStorage;

import java.util.List;

public interface ConfidentialStorageDao {
    List<String> getSubFromDict(String param);
    List<String> getSubFromDept();
    int insertStorage(ConfidentialStorage confidentialStorage);
    int updateStorage(ConfidentialStorage confidentialStorage);
    List<ConfidentialStorage> selectDictListByPage(ConfidentialStorage confidentialStorage);
    int selectSearchCount(ConfidentialStorage confidentialStorage);
    int deleteListByIds(List<ConfidentialStorage> list);
}
