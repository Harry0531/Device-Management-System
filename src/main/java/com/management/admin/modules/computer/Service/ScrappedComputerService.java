package com.management.admin.modules.computer.Service;

import com.management.admin.modules.computer.Dao.ConfidentialComputerDao;
import com.management.admin.modules.computer.Dao.ScrappedComputerDao;


import com.management.admin.modules.computer.Entity.ConfidentialComputer;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ScrappedComputerService {
    @Autowired
    private ScrappedComputerDao scrappedComputerDao;

    @Autowired
    private ConfidentialComputerDao confidentialComputerDao;

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

    public boolean scrap(String id, String scrapTime, String remarks) throws ParseException {
        ConfidentialComputer computer = confidentialComputerDao.getComputerById(id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date scrap_time = sdf.parse(scrapTime);

        computer.preUpdate();
        computer.setScrapped_flag(1);
        computer.setScrap_time(scrap_time);
        computer.setUse_situation(confidentialComputerDao.getScrap());
        computer.setRemarks(remarks);
        return scrappedComputerDao.updateComputer(computer) == 1;
    }
}
