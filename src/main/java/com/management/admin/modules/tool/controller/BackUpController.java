package com.management.admin.modules.tool.controller;

import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.tool.service.BackUpService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("api/tool/backup")
public class BackUpController extends BaseApi {

    @Autowired
    BackUpService backUpService;

    @ResponseBody
    @RequestMapping("backup")
    public  void backup(
            HttpServletResponse response,
            @RequestParam String department
    ){
        backUpService.backup(department,response);
    }

    @ResponseBody
    @RequestMapping("recover")
    public Object recover(@RequestParam String fileName){
        if (fileName ==null ||fileName.equals(""))   return retMsg.Set(MsgType.ERROR);
       try {
           backUpService.recover(fileName);
           return retMsg.Set(MsgType.SUCCESS);
       }catch (Exception e){
           return retMsg.Set(MsgType.ERROR,e);
       }

    }
}
