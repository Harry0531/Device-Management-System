package com.management.admin.modules.tool.service;

import com.management.admin.common.utils.Excel.ExportExcelData;
import com.management.admin.common.utils.ExcelUtils;
import com.management.admin.common.utils.SystemPath;
import com.management.admin.modules.tool.dao.BackUpDao;
import com.management.admin.modules.tool.entity.tiny.TableField;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.awt.image.PNGImageDecoder;

import java.awt.*;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;

@Service
public class BackUpService {

    @Autowired
    BackUpDao backUpDao;

    public void backup(String department,HttpServletResponse response){
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称

        String filename="备份数据";
        try {
            response.addHeader("content-disposition", "attachment;filename=" + URLEncoder.encode( filename+".xlsx", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        List<String> tables = new ArrayList<String>(Arrays.asList("confidential_computer","confidential_information_device","confidential_storage_device","data_dictionary","data_dictionary_type","department","excel_template","excel_template_map","non_confidential_computer","non_confidential_information_device","non_confidential_intermediary","non_confidential_storage_device","security_products","usb_key"));

        List<List<String>> fields = new ArrayList<>();
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`secret_number`","`asset_number`","`person`","`secret_level`","`model`","`os_version`","`os_install_time`","`serial_number`","`mac_address`","`cd_drive`","`usage`","`place_location`","`enablation_time`","`use_situation`","IFNULL(remarks, \"null\")","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`","IFNULL(scrap_time, \"null\")")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`secret_number`","`asset_number`","`device_name`","`person`","`secret_level`","`connect_number`","`model`","`device_number`","`disk_number`","`usage`","`place_location`","`enablation_time`","`use_situation`","IFNULL(remarks, \"null\")","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`","IFNULL(scrap_time, \"null\")")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`secret_number`","`person`","`secret_level`","`model`","`serial_number`","`usage`","`place_location`","`scope`","`enablation_time`","`use_situation`","IFNULL(remarks, \"null\")","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`","IFNULL(scrap_time, \"null\")")));

        fields.add(new ArrayList<String>(Arrays.asList("`id`","`dic_property`","IFNULL(`dic_en_name`, \"null\")","`dic_value`","IFNULL(`father`, \"null\")","IFNULL(`sort`, \"null\")","IFNULL(`remark`, \"null\")","`create_time`","`modify_time`","`del_flag`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`table_name`","`type_name`","`create_time`","`modify_time`","`del_flag`")));

        fields.add(new ArrayList<String>(Arrays.asList("`id`","`dept_name`","`dept_code`","`dept_type`","`dept_attach`","`dept_property`","`create_time`","`modify_time`","`del_flag`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`type_id`","`template_name`","`table_name`","`excel_name`","`create_time`","`modify_time`","`del_flag`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`template_id`","`field_name`","IFNULL(column_index, \"null\")","IFNULL(column_name, \"null\")","`dict_flag`","IFNULL(dict_name, \"null\")","`create_time`","`modify_time`","`del_flag`")));

        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`number`","`asset_number`","`person`","`secret_level`","`model`","`os_version`","`os_install_time`","`serial_number`","`ip_address`","`mac_address`","`cd_drive`","`usage`","`place_location`","`enablation_time`","`use_situation`","IFNULL(remarks, \"null\")","`create_time`","`modify_time`","`del_flag`","IFNULL(scrapped_flag, \"null\")")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`asset_number`","`device_name`","`person`","`secret_level`","`model`","`device_number`","`disk_number`","`usage`","`place_location`","`enablation_time`","`use_situation`","IFNULL(remarks, \"null\")","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`number`","`asset_number`","`person`","`secret_level`","`model`","`os_version`","`os_install_time`","`serial_number`","`mac_address`","`cd_drive`","`usage`","`place_location`","`enablation_time`","`use_situation`","IFNULL(remarks, \"null\")","`create_time`","`modify_time`","`del_flag`","IFNULL(scrapped_flag, \"null\")")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`number`","`person`","`secret_level`","`model`","`serial_number`","`usage`","`place_location`","`scope`","`enablation_time`","`use_situation`","IFNULL(remarks, \"null\")","`create_time`","`modify_time`","`del_flag`","IFNULL(scrapped_flag, \"null\")")));

        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`secret_number`","`asset_number`","`product_version`","`manufacturer`","`certificate_number`","`certificate_validity`","`serial_number`","`person`","`secret_level`","`place_location`","`scope`","`buy_time`","`enablation_time`","`use_situation`","IFNULL(remarks, \"null\")","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`","IFNULL(scrap_time, \"null\")")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`secret_number`","`person`","`secret_level`","`model`","`serial_number`","`scope`","`usage`","`place_location`","connect_number","`enablation_time`","`use_situation`","IFNULL(remarks, \"null\")","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`","IFNULL(scrap_time, \"null\")")));



        XSSFWorkbook wb = new XSSFWorkbook();

        ExportExcelData excelData=new ExportExcelData();
        //设置文件名
        excelData.setName("备份数据.xlsx");
        //设置表头
        List<String> title=new ArrayList<>();
        excelData.setTitles(title);

        for(int z=0;z<tables.size();z++){
            String field="";
            for(String wh:fields.get(z)){
                if(fields.get(z).indexOf(wh)==0){
                    field+=wh;
                }else field+=","+wh;
            }
            List<Map<String,Object>>  dataList =new ArrayList<>();
            if(tables.get(z).equals("data_dictionary")||tables.get(z).equals("data_dictionary_type")||tables.get(z).equals("department")||tables.get(z).equals("excel_template")||tables.get(z).equals("excel_template_map")){
                if(tables.get(z).equals("department")){
                    dataList =  backUpDao.getTableData(field,tables.get(z),department,"yes");
                }else{
                    dataList =  backUpDao.getTableData(field,tables.get(z),"","no");
                }

            }else{
                dataList = backUpDao.getTableData(field,tables.get(z),department,"no");
            }
            List<List<Object>> data =new ArrayList<>();
            for(Map<String,Object> i :dataList){
                List<Object> temp=new ArrayList<>(i.values());
                data.add(temp);
            }
            XSSFSheet sheet = wb.createSheet(tables.get(z));
            excelData.setTitles(fields.get(z));
            excelData.setRows(data);
            writeExcel(wb, sheet, excelData);
        }
        try {
            OutputStream out =response.getOutputStream();
            wb.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recover(String fileName){
        String dataExcelPath= SystemPath.getRootPath()+SystemPath.getTemporaryPath()+fileName;

        List<String> tables = new ArrayList<String>(Arrays.asList("confidential_computer","confidential_information_device","confidential_storage_device","data_dictionary","data_dictionary_type","department","excel_template","excel_template_map","non_confidential_computer","non_confidential_information_device","non_confidential_intermediary","non_confidential_storage_device","security_products","usb_key"));

        List<List<String>> fields = new ArrayList<>();
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`secret_number`","`asset_number`","`person`","`secret_level`","`model`","`os_version`","`os_install_time`","`serial_number`","`mac_address`","`cd_drive`","`usage`","`place_location`","`enablation_time`","`use_situation`","`remarks`","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`","`scrap_time`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`secret_number`","`asset_number`","`device_name`","`person`","`secret_level`","`connect_number`","`model`","`device_number`","`disk_number`","`usage`","`place_location`","`enablation_time`","`use_situation`","`remarks`","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`","`scrap_time`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`secret_number`","`person`","`secret_level`","`model`","`serial_number`","`usage`","`place_location`","`scope`","`enablation_time`","`use_situation`","`remarks`","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`","`scrap_time`")));


        fields.add(new ArrayList<String>(Arrays.asList("`id`","`dic_property`","`dic_en_name`","`dic_value`","`father`","`sort`","`remark`","`create_time`","`modify_time`","`del_flag`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`table_name`","`type_name`","`create_time`","`modify_time`","`del_flag`")));

        fields.add(new ArrayList<String>(Arrays.asList("`id`","`dept_name`","`dept_code`","`dept_type`","`dept_attach`","`dept_property`","`create_time`","`modify_time`","`del_flag`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`type_id`","`template_name`","`table_name`","`excel_name`","`create_time`","`modify_time`","`del_flag`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`template_id`","`field_name`","`column_index`","`column_name`","`dict_flag`","`dict_name`","`create_time`","`modify_time`","`del_flag`")));

        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`number`","`asset_number`","`person`","`secret_level`","`model`","`os_version`","`os_install_time`","`serial_number`","`ip_address`","`mac_address`","`cd_drive`","`usage`","`place_location`","`enablation_time`","`use_situation`","`remarks`","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`asset_number`","`device_name`","`person`","`secret_level`","`model`","`device_number`","`disk_number`","`usage`","`place_location`","`enablation_time`","`use_situation`","`remarks`","`create_time`","`modify_time`","`status`","`del_flag`","`scrapped_flag`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`number`","`asset_number`","`person`","`secret_level`","`model`","`os_version`","`os_install_time`","`serial_number`","`mac_address`","`cd_drive`","`usage`","`place_location`","`enablation_time`","`use_situation`","`remarks`","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`number`","`person`","`secret_level`","`model`","`serial_number`","`usage`","`place_location`","`scope`","`enablation_time`","`use_situation`","`remarks`","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`")));

        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`secret_number`","`asset_number`","`product_version`","`manufacturer`","`certificate_number`","`certificate_validity`","`serial_number`","`person`","`secret_level`","`place_location`","`scope`","`buy_time`","`enablation_time`","`use_situation`","`remarks`","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`","`scrap_time`")));
        fields.add(new ArrayList<String>(Arrays.asList("`id`","`department`","`subject`","`type`","`secret_number`","`person`","`secret_level`","`model`","`serial_number`","`scope`","`usage`","`place_location`","connect_number","`enablation_time`","`use_situation`","`remarks`","`create_time`","`modify_time`","`del_flag`","`scrapped_flag`","`scrap_time`")));


        ;
        for(int i=0;i<tables.size();i++){
            String allSql="";
            try {
                List<String> fieldList = fields.get(i);
                String Hsql = "";
                Hsql = "insert into "+tables.get(i)+" (";
                for (String w : fieldList) {
                    if (fieldList.indexOf(w) == 0) {
                        Hsql +=w;
                    } else {
                        Hsql += "," + w;
                    }
                }
                Hsql+=")values(";

                File file = new File(dataExcelPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Sheet sheet = ExcelUtils.getSheet(file,i);//获取excel表格
                String sheetSql="";
                for(int rowIndex = 1; rowIndex <=sheet.getLastRowNum();rowIndex++) { //忽略第一行
                    List<Object> row = new ArrayList<>();
                    String rowSqlHead=Hsql;
                    String rowSqlTail="";
                    Row dataRow = sheet.getRow(rowIndex);   //获取一行的数据
                    for(int j=0;j<dataRow.getLastCellNum();j++) {
                        Cell cell = dataRow.getCell(j);
                        String cellValue = ExcelUtils.getCellValueByFieldType(cell, "varchar").toString();
                    if(cellValue.equals("null")||fieldList.get(j).equals("`del_flag`")||fieldList.get(j).equals("`scrapped_flag`")||fieldList.get(j).equals("`sort`")||fieldList.get(j).equals("`dept_code`")||fieldList.get(j).equals("`dept_type`")||fieldList.get(j).equals("`column_index`")||fieldList.get(j).equals("`dict_flag`")){
                            if(j==0) {
                                rowSqlHead+=cellValue;
                                rowSqlTail+=fieldList.get(j)+"="+cellValue;
                            }
                            else{
                                rowSqlHead+=","+cellValue;
                                rowSqlTail+=","+fieldList.get(j)+"="+cellValue;
                            }
                        }else{
                            if(j==0) {
                                rowSqlHead+="\""+cellValue+"\"";
                                rowSqlTail+=fieldList.get(j)+"="+"\""+cellValue+"\"";
                            }else{
                                rowSqlHead+=",\""+cellValue+"\"";
                                rowSqlTail+=","+fieldList.get(j)+"="+"\""+cellValue+"\"";
                            }
                        }
                    }
                    sheetSql+=rowSqlHead+") on duplicate key update "+rowSqlTail+";";
                }
                allSql+=sheetSql;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!allSql.equals("")){
                backUpDao.recover(allSql);
            }
        }
    }

    private static void writeExcel(XSSFWorkbook wb, Sheet sheet, ExportExcelData data) {

        int rowIndex = 0;

        rowIndex = writeTitlesToExcel(wb, sheet, data.getTitles());
        writeRowsToExcel(wb, sheet, data.getRows(), rowIndex);
        autoSizeColumns(sheet, data.getTitles().size() + 1);

    }

    private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles) {
        int rowIndex = 0;
        int colIndex = 0;

        Font titleFont = wb.createFont();
        titleFont.setFontName("simsun");
        //titleFont.setBoldweight(Short.MAX_VALUE);
        // titleFont.setFontHeightInPoints((short) 14);
        titleFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setFillForegroundColor(new XSSFColor(new Color(182, 184, 192)));
        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        setBorder(titleStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));

        Row titleRow = sheet.createRow(rowIndex);
        // titleRow.setHeightInPoints(25);
        colIndex = 0;

        for (String field : titles) {
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }

        rowIndex++;
        return rowIndex;
    }

    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<List<Object>> rows, int rowIndex) {
        int colIndex = 0;

        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        // dataFont.setFontHeightInPoints((short) 14);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        dataStyle.setFont(dataFont);
        setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));

        String pattern="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29) ";

        for (List<Object> rowData : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            // dataRow.setHeightInPoints(25);
            colIndex = 0;

            for (Object cellData : rowData) {
                Cell cell = dataRow.createCell(colIndex);
                if (cellData != null) {
                    cell.setCellValue(cellData.toString());
                } else {
                    cell.setCellValue("");
                }
                if(Pattern.matches(pattern,cellData.toString())){
                    DataFormat format=wb.createDataFormat();
                    dataStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
                }

                cell.setCellStyle(dataStyle);
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    private static void autoSizeColumns(Sheet sheet, int columnNumber) {

        for (int i = 0; i < columnNumber; i++) {
            int orgWidth = sheet.getColumnWidth(i);
            sheet.autoSizeColumn(i, true);
            int newWidth = (int) (sheet.getColumnWidth(i) + 100);
            if (newWidth > orgWidth) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, orgWidth);
            }
        }
    }

    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
    }
}
