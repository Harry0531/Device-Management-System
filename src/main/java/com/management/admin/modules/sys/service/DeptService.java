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

    public List<Dept> getSchoolList(){
        return deptDao.getSchoolList();
    }
}
