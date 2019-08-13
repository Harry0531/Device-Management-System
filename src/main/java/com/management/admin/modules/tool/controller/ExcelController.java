package com.management.admin.modules.tool.controller;

import com.management.admin.common.utils.ExcelUtils;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.tool.entity.ExcelTemplate;
import com.management.admin.modules.tool.entity.tiny.ExcelColumn;
import com.management.admin.modules.tool.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import  java.util.List;

/**
      * @Description  excel模板設置
      * @author wh
      * @date 2019/8/11 21:26
      */
@RequestMapping("api/tool/excel")
@Controller
public class ExcelController extends BaseApi {

    @Autowired
    ExcelService excelService;

    @RequestMapping(value="insertExcelTemplate",method = RequestMethod.POST)
    @ResponseBody
    public  Object insertOrUpdate(
            @RequestBody ExcelTemplate excelTemplate,
            HttpServletRequest request
    )throws Exception{
        if (excelTemplate.getId() == null){
            if(excelService.insert(excelTemplate)){
                return retMsg.Set(MsgType.SUCCESS);
            }else
                return retMsg.Set(MsgType.ERROR);
        }
        return null;
    }

    @RequestMapping(value="selectAllTemplate",method = RequestMethod.POST)
    @ResponseBody
    public Object selectAllTemplate(
            @RequestBody ExcelTemplate excelTemplate
            )throws Exception{

    }



    @RequestMapping(value="getTableList",method = RequestMethod.GET)
    @ResponseBody
    public  Object getTableList()throws Exception{
        return retMsg.Set(MsgType.SUCCESS,excelService.getTableList());
    }

    @RequestMapping(value="getColumnInTableAndExcel",method = RequestMethod.POST)
    @ResponseBody
    public Object getColumnInTableAndExcel(
            @RequestParam String tableName,
            @RequestParam String ExcelName
    )throws Exception{
            List<String>tableColumnList = excelService.getTableColumnList(tableName);
            List<ExcelColumn>excelColumnList= excelService.getExcelColumnList(ExcelName);
            HashMap<String ,Object> data =new HashMap<>();
            data.put("tableColumnList",tableColumnList);
            data.put("excelColumnList",excelColumnList);
            return retMsg.Set(MsgType.SUCCESS,data);
    }
}
