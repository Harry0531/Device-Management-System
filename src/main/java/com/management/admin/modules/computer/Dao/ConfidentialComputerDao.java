package com.management.admin.modules.computer.Dao;

import com.management.admin.modules.computer.Entity.ConfidentialComputer;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface ConfidentialComputerDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    int insertComputer(ConfidentialComputer confidentialComputer);
    int updateComputer(ConfidentialComputer confidentialComputer);
    List<ConfidentialComputer> selectDictListByPage(ConfidentialComputer confidentialComputer);
    int selectSearchCount(ConfidentialComputer confidentialComputer);
    int deleteListByIds(List<ConfidentialComputer> list);
    String getScrap();
    ConfidentialComputer getComputerById(String id);
}
