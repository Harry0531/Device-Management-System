package com.management.admin.modules.tool.controller;

import com.management.admin.modules.tool.entity.ExcelTemplate;
import com.management.admin.modules.tool.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
      * @Description  excel模板設置
      * @author wh
      * @date 2019/8/11 21:26
      */
@RequestMapping("api/tool/excel")
public class ExcelController {

    @Autowired
    ExcelService excelService;

    @RequestMapping(value="insertOrUpdate",method = RequestMethod.POST)
    @ResponseBody
    public  Object insertOrUpdate(
            @RequestBody ExcelTemplate excelTemplate,
            HttpServletRequest request
    )throws Exception{
        if (excelTemplate.getId() == null){

        }
        return null;
    }

}
