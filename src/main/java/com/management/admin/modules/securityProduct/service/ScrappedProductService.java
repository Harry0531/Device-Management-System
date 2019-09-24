package com.management.admin.modules.securityProduct.service;

import com.management.admin.modules.securityProduct.dao.ScrappedProductDao;
import com.management.admin.modules.securityProduct.dao.SecurityProductDao;
import com.management.admin.modules.securityProduct.entity.SecurityProduct;
import com.management.admin.modules.storage.dao.ScrappedStorageDao;
import com.management.admin.modules.sys.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ScrappedProductService {
    @Autowired
    private ScrappedProductDao scrappedProductDao;
    @Autowired
    private SecurityProductDao securityProductDao;

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

    public boolean deleteListByIds(List<SecurityProduct> list) {
        return list.size() == 0 || scrappedProductDao.deleteListByIds(list) == list.size();
    }

    public boolean scrap(String id, String scrapTime, String remarks) throws ParseException {
        SecurityProduct securityProduct = securityProductDao.getSecurityProductById(id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date scrap_time = sdf.parse(scrapTime);

        securityProduct.preUpdate();
        securityProduct.setScrapped_flag(1);
        securityProduct.setScrap_time(scrap_time);
        securityProduct.setUse_situation(securityProductDao.getScrap());
        securityProduct.setRemarks(remarks);
        return securityProductDao.updateProduct(securityProduct) == 1;
    }
}
