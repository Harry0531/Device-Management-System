package com.management.admin.modules.sys.dao;

import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface DeptDao {
    List<Dept> getSchoolList();

    List<Dept> selectListByPage(Dept dept);

    int selectSearchCount(Dept dept);

    int searchEntry(Dept dept);

    int insertDept(Dept dept);

    int updateDept(Dept dept);

    int deleteDictByIds(List<Dept> list);

    int changeDisable(String id, Integer flag);
}
