package com.management.admin.modules.securityProduct.dao;

import com.management.admin.modules.securityProduct.entity.SecurityProduct;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface SecurityProductDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    int insertProduct(SecurityProduct securityProduct);
    int updateProduct(SecurityProduct securityProduct);
    List<SecurityProduct> selectDictListByPage(SecurityProduct securityProduct);
    int selectSearchCount(SecurityProduct securityProduct);
    int deleteListByIds(List<SecurityProduct> list);
    String getScrap();
    SecurityProduct getSecurityProductById(String id);
}
