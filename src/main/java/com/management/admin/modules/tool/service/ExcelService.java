//package com.management.admin.modules.tool.service;
//
//import com.management.admin.common.utils.SystemPath;
//import com.management.admin.modules.tool.dao.ColumnMapFieldDao;
//import com.management.admin.modules.tool.dao.ExcelTemplateDao;
//import com.management.admin.modules.tool.entity.ColumnMapField;
//import com.management.admin.modules.tool.entity.ExcelTemplate;
//import org.apache.commons.io.FileUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.File;
//import java.io.IOException;
//
//@Service
//public class ExcelService {
//
//    @Autowired
//    ExcelTemplateDao excelTemplateDao;
//
//    @Autowired
//    ColumnMapFieldDao columnMapFieldDao;
//
//    /**
//     * @param excelTemplate include all info
//     * @return is successful
//     * @apiNote 1st: copy the file in temp dir to excelTemplate dir
//     * 2nd: insertOrUpdate ExcelTemplate
//     * 3rd: insertOrUpdate ColumnMapFieldList
//     */
//    @Transactional
//    public boolean insert(ExcelTemplate excelTemplate) {
//        // 1st step: copy the file in temp dir to excelTemplate dir
//        File srcFile = new File(SystemPath.getRootPath() + SystemPath.getTemporaryPath() + excelTemplate.getExcelName());
//        File targetDir = new File(SystemPath.getRootPath() + SystemPath.getExcelTemplatePath());
//        try {
//            FileUtils.copyFileToDirectory(srcFile, targetDir);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // 2nd step: insertOrUpdate ExcelTemplate
//        excelTemplate.preInsert();
////        excelTemplateDao.insertOrUpdate(excelTemplate);
//        // 3rd step: insertOrUpdate ColumnMapFieldList
//        for (ColumnMapField columnMapField : excelTemplate.getColumnMapFieldList()) {
//            columnMapField.preInsert();
//            columnMapField.setTemplateId(excelTemplate.getId());
//        }
//        if (excelTemplate.getColumnMapFieldList().size() > 0)
////            columnMapFieldDao.insertList(excelTemplate.getColumnMapFieldList());
//        return true;
//    }
//}
