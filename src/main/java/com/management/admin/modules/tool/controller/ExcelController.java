package com.management.admin.modules.tool.controller;

import com.management.admin.common.utils.FileUtils;
import com.management.admin.common.utils.SystemPath;
import com.management.admin.common.web.BaseApi;
import com.management.admin.modules.tool.service.ExcelService;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
      * @Description EXCEL模板上傳
      * @author wh
      * @date 2019/8/11 20:18
      */
@Controller
@RequestMapping("/api/tool/excel")
public class ExcelController extends BaseApi {

    @Autowired
    ExcelService excelService;


    @RequestMapping(value="test")
    public Object test(){
        return "1";
    }


    @RequestMapping(value ="uploadTemFile",method = RequestMethod.POST)
    public Object uploadTemFile(
            @RequestParam MultipartFile file,
            HttpServletRequest request
            )throws  Exception{
        return FileUtils.saveUploadFileToTempDir(file);
    }

}
