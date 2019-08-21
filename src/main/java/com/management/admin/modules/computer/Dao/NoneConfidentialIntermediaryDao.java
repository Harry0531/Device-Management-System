package com.management.admin.modules.computer.Dao;

import com.management.admin.modules.computer.Entity.NoneConfidentialIntermediary;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface NoneConfidentialIntermediaryDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    int insertComputer(NoneConfidentialIntermediary noneConfidentialIntermediary);
    int updateComputer(NoneConfidentialIntermediary noneConfidentialIntermediary);
    List<NoneConfidentialIntermediary> selectDictListByPage(NoneConfidentialIntermediary noneConfidentialIntermediary);
    int selectSearchCount(NoneConfidentialIntermediary noneConfidentialIntermediary);
    int deleteListByIds(List<NoneConfidentialIntermediary> list);
}
