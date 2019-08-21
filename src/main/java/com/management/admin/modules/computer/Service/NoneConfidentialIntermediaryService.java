package com.management.admin.modules.computer.Service;


import com.management.admin.modules.computer.Dao.NoneConfidentialIntermediaryDao;
import com.management.admin.modules.computer.Entity.NoneConfidentialIntermediary;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoneConfidentialIntermediaryService {
    @Autowired
    private NoneConfidentialIntermediaryDao noneConfidentialIntermediaryDao;

    public List<String> getSubFromDict(String param) {
        return noneConfidentialIntermediaryDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return noneConfidentialIntermediaryDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return noneConfidentialIntermediaryDao.getDeptSub(id);
    }

    public boolean insertComputer(NoneConfidentialIntermediary noneConfidentialIntermediary) {
        noneConfidentialIntermediary.preInsert();
        return noneConfidentialIntermediaryDao.insertComputer(noneConfidentialIntermediary) == 1;
    }

    public boolean updateComputer(NoneConfidentialIntermediary noneConfidentialIntermediary) {
        noneConfidentialIntermediary.preUpdate();
        return noneConfidentialIntermediaryDao.updateComputer(noneConfidentialIntermediary) == 1;
    }

    public List<NoneConfidentialIntermediary> selectDictListByPage(NoneConfidentialIntermediary noneConfidentialIntermediary) {
        return noneConfidentialIntermediaryDao.selectDictListByPage(noneConfidentialIntermediary);
    }

    public int selectSearchCount(NoneConfidentialIntermediary noneConfidentialIntermediary) {
        return noneConfidentialIntermediaryDao.selectSearchCount(noneConfidentialIntermediary);
    }

    public boolean deleteListByIds(List<NoneConfidentialIntermediary> list) {
        return list.size() == 0 || noneConfidentialIntermediaryDao.deleteListByIds(list) == list.size();
    }


}
