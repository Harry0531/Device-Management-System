package com.management.admin.modules.tool.service;

import com.management.admin.common.utils.ExcelUtils;
import com.management.admin.common.utils.IdGen;
import com.management.admin.common.utils.SystemPath;
import com.management.admin.modules.sys.service.DictService;
import com.management.admin.modules.tool.dao.ColumnMapFieldDao;
import com.management.admin.modules.tool.dao.ExcelTemplateDao;
import com.management.admin.modules.tool.dao.ImportDataDao;
import com.management.admin.modules.tool.entity.ColumnMapField;
import com.management.admin.modules.tool.entity.DynamicInsertParam;
import com.management.admin.modules.tool.entity.ImportExcel;
import com.management.admin.modules.tool.entity.ExcelTemplate;
import com.management.admin.modules.tool.entity.tiny.ExcelColumn;
import com.management.admin.modules.tool.entity.tiny.TableField;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class ExcelService {

    @Autowired
    ExcelTemplateDao excelTemplateDao;

    @Autowired
    ColumnMapFieldDao columnMapFieldDao;

    @Autowired
    ImportDataDao importDataDao;

    @Autowired
    DictService dictService;

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
            columnMapFieldDao.insertList(excelTemplate.getColumnMapFieldList());
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

    public List<ExcelTemplate> selectAllTemplate(ExcelTemplate conditions) {
        return excelTemplateDao.selectAllTemplate(conditions);
    }

    public boolean importExcelToTable(ImportExcel importExcel) throws IOException {
        //数据excel名字
        String dataExcelPath=SystemPath.getRootPath()+SystemPath.getTemporaryPath()+ importExcel.getExcelDataName();

        //获得模版信息
        ExcelTemplate excelTemplate = excelTemplateDao.selectById(importExcel.getId());

        //获得字段对应列名map信息
        List<ColumnMapField> columnMapFieldList = columnMapFieldDao.selectByTemplateId(importExcel.getId());



//        1.准备数据，来自excel表格之外
        List<List<Object>> data = new ArrayList<>();

        Sheet sheet = ExcelUtils.getSheet(new File(dataExcelPath),0);//获取excel表格
        Date now = new Date();//当前时间

//            2.获取数据库字段信息
        // add info from table field and reorder
        List<TableField> tableFieldList = getTableFieldList(importExcel.getTableName(), false);
        List<ColumnMapField> columnMapFieldList2 = new ArrayList<>();
        for (TableField tableField : tableFieldList) {
            for (ColumnMapField columnMapField : columnMapFieldList) {
                if (tableField.getFieldName().equals(columnMapField.getTableColumnName())) {
                    columnMapField.setFieldType(tableField.getFieldType());
                    columnMapFieldList2.add(columnMapField);
                    break;
                }
            }
        }

        //数据库字段头序列
        List<String> fieldList = new ArrayList<>();
        fieldList.add("id");
        fieldList.add("create_time");
        fieldList.add("modify_time");
        fieldList.add("del_flag");
        for (ColumnMapField columnMapField : columnMapFieldList2) {
            fieldList.add(columnMapField.getTableColumnName());
        }

        //判断字段是否用了数据字典
        List<Boolean> isDict = new ArrayList<>();
        for(int i = 4 ; i <fieldList.size();i++){ //跳过前4个固定
            String fieldName=fieldList.get(i);
            isDict.add(dictService.isUseDict(importExcel.getTypeId(),fieldName));
        }


        DynamicInsertParam dynamicInsertParam = new DynamicInsertParam();
        //设置插入的数据库表名
        dynamicInsertParam.setTableName(importExcel.getTableName());
        //设置插入字段
        dynamicInsertParam.setFieldList(fieldList);

//        3.处理表格数据
        for(int rowIndex = 1; rowIndex <=sheet.getLastRowNum();rowIndex++){ //忽略第一行
            Row dataRow = sheet.getRow(rowIndex);   //获取一行的数据

            //对应前面四个固定值
            List<Object> row = new ArrayList<>();
            row.add(IdGen.uuid());
            row.add(now);
            row.add(now);
            row.add(0);

            //处理每个map对应关系
            for(int i=0;i<columnMapFieldList2.size();i++){
                ColumnMapField columnMapField = columnMapFieldList2.get(i);  //取得单个映射
                Object cellValue = null;
                if(columnMapField.getColumnIndex()!= null){   //如果存在映射关系
                        int tmp=columnMapField.getColumnIndex();
                        Cell cell =dataRow.getCell(tmp);
                    cellValue = ExcelUtils.getCellValueByFieldType(cell, columnMapField.getFieldType());

                    if(isDict.get(i)){
                        cellValue = dictService.getUUID(importExcel.getTypeId(),fieldList.get(i+4),(String)cellValue);
                    }
                }
                row.add(cellValue);
            }
            data.add(row);
        }//for
        dynamicInsertParam.setData(data);
        // 4.进行插入，并返回是否成功
        return importDataDao.dynamicInsert(dynamicInsertParam) == dynamicInsertParam.getData().size();
    }

    /**
     * @param tableName table's name in database
     * @return all fields' info except 6 automatic field
     */
    public List<TableField> getTableFieldList(String tableName, boolean isAll) {
        List<TableField> tableFieldList = importDataDao.selectFieldListByTableName(tableName);
        if (isAll)
            return tableFieldList;
        return tableFieldList.stream()
                .filter(columnMapField -> isFieldRetained(columnMapField.getFieldName()))
                .collect(Collectors.toList());
    }

    /**
     * @return is field retained
     */
    private boolean isFieldRetained(String fieldName) {
        String[] excludeColumns = new String[]{"id",  "create_date",
                 "modify_date", "del_flag"};
        for (String s : excludeColumns) {
            if (s.equals(fieldName))
                return false;
        }
        return true;
    }
}
