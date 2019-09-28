package com.management.admin.modules.sys.service;

import com.management.admin.modules.sys.dao.DeptDao;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptService {
    @Autowired
    private DeptDao deptDao;

    public List<Dept> getSchoolList() {
        return deptDao.getSchoolList();
    }

    public List<Dept> selectListByPage(Dept dept) {
        List<Dept> list = deptDao.selectListByPage(dept);
        for (Dept d : list) {
            switch (d.getDept_type()) {
                case 0:
                    d.set_dept_type("部门/学院");
                    break;
                case 1:
                    d.set_dept_type("科室/课题组");
                    break;
            }
        }
        return list;
    }

    public int selectSearchCount(Dept dept) {
        return deptDao.selectSearchCount(dept);
    }

    public boolean searchEntry(Dept dept) {
        return deptDao.searchEntry(dept) == 0;
    }

    public boolean insertDept(Dept dept) {
        dept.preInsert();
        dept.setDept_property("部门");
        return deptDao.insertDept(dept) == 1;
    }

    public boolean updateDept(Dept dept) {
        dept.preUpdate();
        return deptDao.updateDept(dept) == 1;
    }

    public boolean deleteListByIds(List<Dept> list) {
        return list.size() == 0 || deptDao.deleteDictByIds(list) == list.size();
    }

    public boolean changeDisable(String id, Integer flag) {
        return deptDao.changeDisable(id, flag) == 1;
    }
}
