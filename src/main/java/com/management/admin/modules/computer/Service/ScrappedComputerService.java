package com.management.admin.modules.computer.Service;

import com.management.admin.modules.computer.Dao.ScrappedComputerDao;


import com.management.admin.modules.computer.Entity.ConfidentialComputer;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScrappedComputerService {
    @Autowired
    private ScrappedComputerDao scrappedComputerDao;

    public List<String> getSubFromDict(String param) {
        return scrappedComputerDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return scrappedComputerDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return scrappedComputerDao.getDeptSub(id);
    }

    public boolean insertComputer(ConfidentialComputer confidentialComputer) {
        confidentialComputer.preInsert();
        return scrappedComputerDao.insertComputer(confidentialComputer) == 1;
    }

    public boolean updateComputer(ConfidentialComputer confidentialComputer) {
        confidentialComputer.preUpdate();
        return scrappedComputerDao.updateComputer(confidentialComputer) == 1;
    }

    public List<ConfidentialComputer> selectDictListByPage(ConfidentialComputer confidentialComputer) {
        return scrappedComputerDao.selectDictListByPage(confidentialComputer);
    }

    public int selectSearchCount(ConfidentialComputer confidentialComputer) {
        return scrappedComputerDao.selectSearchCount(confidentialComputer);
    }

    public boolean deleteListByIds(List<ConfidentialComputer> list) {
        return list.size() == 0 || scrappedComputerDao.deleteListByIds(list) == list.size();
    }


}
