package com.management.admin.modules.computer.Dao;


import com.management.admin.modules.computer.Entity.NoneConfidentialComputer;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface NoneConfidentialComputerDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    int insertComputer(NoneConfidentialComputer noneConfidentialComputer);
    int updateComputer(NoneConfidentialComputer noneConfidentialComputer);
    List<NoneConfidentialComputer> selectDictListByPage(NoneConfidentialComputer noneConfidentialComputer);
    int selectSearchCount(NoneConfidentialComputer noneConfidentialComputer);
    int deleteListByIds(List<NoneConfidentialComputer> list);

}
