package com.management.admin.modules.tool.service;

import com.management.admin.common.utils.ExcelUtils;
import com.management.admin.common.utils.SystemPath;
import com.management.admin.modules.tool.dao.ColumnMapFieldDao;
import com.management.admin.modules.tool.dao.ExcelTemplateDao;
import com.management.admin.modules.tool.entity.ColumnMapField;
import com.management.admin.modules.tool.entity.ExcelTemplate;
import com.management.admin.modules.tool.entity.tiny.ExcelColumn;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

@Service
public class ExcelService {

    @Autowired
    ExcelTemplateDao excelTemplateDao;

    @Autowired
    ColumnMapFieldDao columnMapFieldDao;

    /**
     * @param excelTemplate include all info
     * @return is successful
     * @apiNote 1st: copy the file in temp dir to excelTemplate dir
     * 2nd: insertOrUpdate ExcelTemplate
     * 3rd: insertOrUpdate ColumnMapFieldList
     */
    @Transactional
    public boolean insert(ExcelTemplate excelTemplate) {
        // 1st step: copy the file in temp dir to excelTemplate dir
        File srcFile = new File(SystemPath.getRootPath() + SystemPath.getTemporaryPath() + excelTemplate.getFilePath());
        File targetDir = new File(SystemPath.getRootPath() + SystemPath.getExcelTemplatePath());
        try {
            FileUtils.copyFileToDirectory(srcFile, targetDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2nd step: insertOrUpdate ExcelTemplate
        excelTemplate.preInsert();
        excelTemplateDao.insertOrUpdate(excelTemplate);
        // 3rd step: insertOrUpdate ColumnMapFieldList
        for (ColumnMapField columnMapField : excelTemplate.getColumnMapFieldList()) {
            columnMapField.preInsert();
            columnMapField.setTemplateId(excelTemplate.getId());
        }
        if (excelTemplate.getColumnMapFieldList().size() > 0)
            excelTemplateDao.insertList(excelTemplate.getColumnMapFieldList());
        return true;
    }

    public  List<String> getTableList(){
        return excelTemplateDao.getTableList();
    }

    public List<String>  getTableColumnList(String tableName){
        List<String>data = excelTemplateDao.getTableColumnList(tableName);
        //去掉不必要的字段
        List<String> result=new ArrayList<>();
        for(String i :data){
            if(i.equals("id")||i.equals("create_time")||i.equals("modify_time")||i.equals("del_flag")){
                continue;
            }else{
                result.add(i);
            }
        }
        return result;
    }

    public List<ExcelColumn> getExcelColumnList(String ExcelName){

        String fileName=SystemPath.getRootPath()+SystemPath.getTemporaryPath()+ExcelName;
        Sheet sheet = null;
        try {
            sheet = ExcelUtils.getSheet(new File(fileName),0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Row firstRow = sheet.getRow(0);
        List<ExcelColumn> excelColumnList = new ArrayList<>();
        for (int colIndex = 0; colIndex <= firstRow.getLastCellNum(); colIndex++) {
            Cell cell = firstRow.getCell(colIndex);
            if (cell == null) continue; // 略过列名为空的项
            ExcelColumn excelColumn = new ExcelColumn();
            excelColumn.setColumnIndex(colIndex);
            excelColumn.setColumnName(cell.getStringCellValue());
            excelColumnList.add(excelColumn);
        }
        return excelColumnList;
    }
}
