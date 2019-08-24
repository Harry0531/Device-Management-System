package com.management.admin.modules.tool.controller;

import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.tool.service.BackUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("api/tool/backup")
public class BackUpController extends BaseApi {

    @Autowired
    BackUpService backUpService;

    @ResponseBody
    @RequestMapping("backup")
    public  void backup(
            HttpServletResponse response
    ){
        backUpService.backup(response);
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
