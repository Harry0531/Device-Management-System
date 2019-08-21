package com.management.admin.modules.computer.Service;

import com.management.admin.modules.computer.Dao.ConfidentialComputerDao;

import com.management.admin.modules.computer.Entity.ConfidentialComputer;
import com.management.admin.modules.storage.entity.ConfidentialStorage;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfidentialComputerService {
    @Autowired
    private ConfidentialComputerDao confidentialComputerDao;

    public List<String> getSubFromDict(String param) {
        return confidentialComputerDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return confidentialComputerDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return confidentialComputerDao.getDeptSub(id);
    }

    public boolean insertComputer(ConfidentialComputer confidentialComputer) {
        confidentialComputer.preInsert();
        return confidentialComputerDao.insertComputer(confidentialComputer) == 1;
    }

    public boolean updateComputer(ConfidentialComputer confidentialComputer) {
        confidentialComputer.preUpdate();
        return confidentialComputerDao.updateComputer(confidentialComputer) == 1;
    }

    public List<ConfidentialComputer> selectDictListByPage(ConfidentialComputer confidentialComputer) {
        return confidentialComputerDao.selectDictListByPage(confidentialComputer);
    }

    public int selectSearchCount(ConfidentialComputer confidentialComputer) {
        return confidentialComputerDao.selectSearchCount(confidentialComputer);
    }

    public boolean deleteListByIds(List<ConfidentialComputer> list) {
        return list.size() == 0 || confidentialComputerDao.deleteListByIds(list) == list.size();
    }

    public boolean scrap(ConfidentialComputer confidentialComputer) {
        confidentialComputer.preUpdate();
        confidentialComputer.setScrapped_flag(1);
        confidentialComputer.setUse_situation(confidentialComputerDao.getScrap());
        return confidentialComputerDao.updateComputer(confidentialComputer) == 1;
    }
}
