package com.management.admin.modules.computer.Service;


import com.management.admin.modules.computer.Dao.NoneConfidentialComputerDao;
import com.management.admin.modules.computer.Entity.NoneConfidentialComputer;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoneConfidentialComputerService {
    @Autowired
    private NoneConfidentialComputerDao noneConfidentialComputerDao;

    public List<String> getSubFromDict(String param) {
        return noneConfidentialComputerDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return noneConfidentialComputerDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return noneConfidentialComputerDao.getDeptSub(id);
    }

    public boolean insertComputer(NoneConfidentialComputer noneConfidentialComputer) {
        noneConfidentialComputer.preInsert();
        return noneConfidentialComputerDao.insertComputer(noneConfidentialComputer) == 1;
    }

    public boolean updateComputer(NoneConfidentialComputer noneConfidentialComputer) {
        noneConfidentialComputer.preUpdate();
        return noneConfidentialComputerDao.updateComputer(noneConfidentialComputer) == 1;
    }

    public List<NoneConfidentialComputer> selectDictListByPage(NoneConfidentialComputer noneConfidentialComputer) {
        return noneConfidentialComputerDao.selectDictListByPage(noneConfidentialComputer);
    }

    public int selectSearchCount(NoneConfidentialComputer noneConfidentialComputer) {
        return noneConfidentialComputerDao.selectSearchCount(noneConfidentialComputer);
    }

    public boolean deleteListByIds(List<NoneConfidentialComputer> list) {
        return list.size() == 0 || noneConfidentialComputerDao.deleteListByIds(list) == list.size();
    }


}
