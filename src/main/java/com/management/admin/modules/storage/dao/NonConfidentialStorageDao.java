package com.management.admin.modules.storage.dao;

import com.management.admin.modules.storage.entity.NonConfidentialStorage;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface NonConfidentialStorageDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    int insertStorage(NonConfidentialStorage nonConfidentialStorage);
    int updateStorage(NonConfidentialStorage nonConfidentialStorage);
    List<NonConfidentialStorage> selectDictListByPage(NonConfidentialStorage nonConfidentialStorage);
    int selectSearchCount(NonConfidentialStorage nonConfidentialStorage);
    int deleteListByIds(List<NonConfidentialStorage> list);
    String getNonConfidential();
}
