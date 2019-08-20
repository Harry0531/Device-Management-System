package com.management.admin.modules.securityProduct.controller;

import com.management.admin.common.web.BaseApi;
import com.management.admin.modules.securityProduct.entity.SecurityProduct;
import com.management.admin.modules.securityProduct.service.SecurityProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/sys/product/product")
@Controller
public class SecurityProductController extends BaseApi {
    @Autowired
    private SecurityProductService securityProductService;
}
