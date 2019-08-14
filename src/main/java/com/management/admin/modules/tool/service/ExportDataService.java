package com.management.admin.modules.tool.service;

import com.management.admin.common.utils.Excel.ExcelUtils;
import com.management.admin.common.utils.Excel.ExportExcelData;
import com.management.admin.modules.tool.dao.ExportDataDao;
import com.management.admin.modules.tool.entity.ExportExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExportDataService {

    @Autowired
    ExportDataDao exportDataDao;

    public  void ExportToExcel(HttpServletResponse response,ExportExcel exportExcel){
        //设置查询字段SQL
        String sql = "";
        List<String> fieldList=exportExcel.getFieldList();
        for(int i =0; i<fieldList.size();i++){
            if(i==0)
                sql+=fieldList.get(i);
            else
                sql += ","+fieldList.get(i);
        }
        exportExcel.setFieldSQL(sql);

        List<Map<String,Object>>  dataList = exportDataDao.getDataList(exportExcel);

        //将得到的数据转为导出excel需要的格式
        List<List<Object>> data =new ArrayList<>();
        for(Map<String,Object> i :dataList){
            List<Object> temp=new ArrayList<>(i.values());
            data.add(temp);
        }

        //准备要导入的数据格式
        ExportExcelData excelData=new ExportExcelData();
        //设置文件名
        excelData.setName(exportExcel.getFileName());
        //设置表头
        excelData.setTitles(fieldList);
        //设置数据
        excelData.setRows(data);
        try {
            ExcelUtils.exportExcel(response, exportExcel.getFileName(), excelData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
