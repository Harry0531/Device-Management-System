package com.management.admin.modules.securityProduct.service;

import com.management.admin.modules.securityProduct.dao.ScrappedProductDao;
import com.management.admin.modules.securityProduct.entity.SecurityProduct;
import com.management.admin.modules.storage.dao.ScrappedStorageDao;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScrappedProductService {
    @Autowired
    private ScrappedProductDao scrappedProductDao;

    public List<String> getSubFromDict(String param) {
        return scrappedProductDao.getSubFromDict(param);
    }

    public List<Dept> getSubFromDept() {
        return scrappedProductDao.getSubFromDept();
    }

    public List<Dept> getDeptSub(String id) {
        return scrappedProductDao.getDeptSub(id);
    }

    public List<SecurityProduct> selectDictListByPage(SecurityProduct securityProduct) {
        return scrappedProductDao.selectDictListByPage(securityProduct);
    }

    public int selectSearchCount(SecurityProduct securityProduct) {
        return scrappedProductDao.selectSearchCount(securityProduct);
    }

    public boolean scrap(SecurityProduct securityProduct) {
        //设报废标记
        securityProduct.setScrapped_flag(1);

        //判断报废时间
        Date date = securityProduct.getScrap_time();
        System.out.println(date);
        securityProduct.preScrap();
        if (date != null)
            securityProduct.setScrap_time(date);

        //找到报废状态字典id
        securityProduct.setUse_situation(scrappedProductDao.getScrap());

        //报废
        return scrappedProductDao.scrapProduct(securityProduct) == 1;
    }
}
