package com.management.admin.modules.securityProduct.service;

import com.management.admin.modules.securityProduct.dao.SecurityProductDao;
import com.management.admin.modules.securityProduct.entity.SecurityProduct;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityProductService {
    @Autowired
    private SecurityProductDao securityProductDao;

    public List<String> getSubFromDict(String param) {
        return securityProductDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return securityProductDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return securityProductDao.getDeptSub(id);
    }

    public boolean insertProduct(SecurityProduct securityProduct) {
        securityProduct.preInsert();
        return securityProductDao.insertProduct(securityProduct) == 1;
    }

    public boolean updateProduct(SecurityProduct securityProduct) {
        securityProduct.preUpdate();
        return securityProductDao.updateProduct(securityProduct) == 1;
    }

    public List<SecurityProduct> selectDictListByPage(SecurityProduct securityProduct) {
        return securityProductDao.selectDictListByPage(securityProduct);
    }

    public int selectSearchCount(SecurityProduct securityProduct) {
        return securityProductDao.selectSearchCount(securityProduct);
    }

    public boolean deleteListByIds(List<SecurityProduct> list) {
        return list.size() == 0 || securityProductDao.deleteListByIds(list) == list.size();
    }

    public boolean scrap(SecurityProduct securityProduct) {
        securityProduct.preScrap();
        securityProduct.setScrapped_flag(1);
        securityProduct.setUse_situation(securityProductDao.getScrap());
        return securityProductDao.updateProduct(securityProduct) == 1;
    }
}
