package com.management.admin.modules.tool.controller;

import com.management.admin.common.utils.Excel.ExcelUtils;
import com.management.admin.common.utils.Excel.ExportExcelData;
import com.management.admin.common.utils.SystemPath;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.sys.service.DictService;
import com.management.admin.modules.tool.entity.ExportExcel;
import com.management.admin.modules.tool.entity.ImportExcel;
import com.management.admin.modules.tool.entity.ExcelTemplate;
import com.management.admin.modules.tool.entity.tiny.ExcelColumn;
import com.management.admin.modules.tool.service.ExcelService;
import com.management.admin.modules.tool.service.ExportDataService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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
    @Autowired
    DictService dictService;
    @Autowired
    ExportDataService exportDataService;

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
        List<ExcelTemplate> excelTemplateList = excelService.selectAllTemplate(excelTemplate);
        for (ExcelTemplate e :excelTemplateList){
            e.setTypeName(dictService.getValue(e.getTypeId()));
        }
        return retMsg.Set(MsgType.SUCCESS, excelTemplateList);
    }

    @RequestMapping(value = "downloadExcelTemplate", method = RequestMethod.GET)
    public ResponseEntity downloadExcelTemplate(
            @RequestParam("excelName") String excelName,
            @RequestParam("downloadName") String downloadName,
            HttpServletResponse response) throws IOException {
        File excelTemplate = new File(SystemPath.getRootPath() + SystemPath.getExcelTemplatePath() + excelName);
        downloadName = new String((downloadName + "." + FilenameUtils.getExtension(excelName)).
                getBytes("UTF-8"), "iso-8859-1");
        HttpHeaders headers = new HttpHeaders();
        byte[] returnFile;
        try {
            returnFile = org.apache.commons.io.FileUtils.readFileToByteArray(excelTemplate);
        } catch (IOException e) {
            response.sendRedirect("/404");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", downloadName);
        return new ResponseEntity<>(returnFile, headers, HttpStatus.OK);
    }

    /**
     * @param importExcel include template params and data file's name in server
     * @return isSuccess
     */
    @RequestMapping(value = "importExcelToTable", method = RequestMethod.POST)
    @ResponseBody
    public Object importExcelToTable(@RequestBody ImportExcel importExcel) {
        try {
            excelService.importExcelToTable(importExcel);
            return retMsg.Set(MsgType.SUCCESS);
        } catch (IOException e) {
            return retMsg.Set(MsgType.ERROR);
        }
    }


    @RequestMapping(value = "exportDataToExcel")
    @ResponseBody
    public Object exportExcelToTable(
            @RequestParam String fileName,
            @RequestParam String tableName,
            @RequestParam List<String> fieldList,
            @RequestParam List<String> conditionsList,
            @RequestParam List<String> idList,
            HttpServletResponse response
            )throws Exception{
        ExportExcel exportExcel = new ExportExcel();
        exportExcel.setFileName(fileName);
        exportExcel.setTableName(tableName);
        exportExcel.setFieldList(fieldList);
        exportExcel.setConditionsList(conditionsList);
        exportExcel.setIdList(idList);
        ExportExcelData excelData = exportDataService.ExportToExcel(exportExcel);

        ExcelUtils.exportExcel(response,exportExcel.getFileName(),excelData);
        return fileName;
    }


    //获取表名
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
            List<ExcelColumn>excelColumnList = null;
            List<String>tableColumnList = null;

            if(tableName!=null && !tableName.equals("")) {
                tableColumnList = excelService.getTableColumnList(tableName);
            }
            if(ExcelName!=null && !ExcelName.equals("")){
                excelColumnList= excelService.getExcelColumnList(ExcelName);
            }

            HashMap<String ,Object> data =new HashMap<>();
            data.put("tableColumnList",tableColumnList);
            data.put("excelColumnList",excelColumnList);
            return retMsg.Set(MsgType.SUCCESS,data);
    }
}
