package com.management.admin.modules.securityProduct.service;

import com.management.admin.modules.securityProduct.dao.SecurityProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityProductService {
    @Autowired
    private SecurityProductDao securityProductDao;

}
