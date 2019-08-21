package com.management.admin.modules.securityProduct.dao;

import com.management.admin.modules.securityProduct.entity.SecurityProduct;
import com.management.admin.modules.sys.entity.Dept;

import java.util.List;

public interface ScrappedProductDao {
    List<String> getSubFromDict(String param);
    List<Dept> getSubFromDept();
    List<Dept> getDeptSub(String id);
    List<SecurityProduct> selectDictListByPage(SecurityProduct securityProduct);
    int selectSearchCount(SecurityProduct securityProduct);
    String getScrap();
    int scrapProduct(SecurityProduct securityProduct);
}
