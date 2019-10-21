package com.management.admin.modules.tool.service;

import com.management.admin.common.utils.Excel.ExcelUtils;
import com.management.admin.common.utils.Excel.ExportExcelData;
import com.management.admin.modules.sys.service.DictService;
import com.management.admin.modules.tool.dao.ColumnMapFieldDao;
import com.management.admin.modules.tool.dao.ExportDataDao;
import com.management.admin.modules.tool.dao.ImportDataDao;
import com.management.admin.modules.tool.entity.ColumnMapField;
import com.management.admin.modules.tool.entity.ExportExcel;
import com.management.admin.modules.tool.entity.tiny.DictInfo;
import com.management.admin.modules.tool.entity.tiny.PartInfo;
import com.management.admin.modules.tool.entity.tiny.TableField;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ExportDataService {

    @Autowired
    ExportDataDao exportDataDao;

    @Autowired
    DictService dictService;

    @Autowired
    ImportDataDao importDataDao;

    @Autowired
    ColumnMapFieldDao columnMapFieldDao;

    private List<PartInfo> partInfos;
    private boolean isScrapped=false;

    public ExportExcelData ExportToExcel(ExportExcel exportExcel,HttpServletResponse httpServletResponse){
        isScrapped=false;
        //设置查询字段SQL
        String sql = "@x:=ifnull(@x,0)+1 as \"num\"";
        List<TableField> fieldList=exportExcel.getFieldList();
        for (TableField tableField : fieldList) {
            sql += ",`" + tableField.getFieldType() + "`";
        }
        exportExcel.setFieldSQL(sql);
        //设置查询的id
        List<String>  idList=exportExcel.getIdList();
        if(idList.size()!=0) {
            sql = "(";
            for (String i : idList) {
                if (idList.indexOf(i) == 0) {
                    sql += "\""+ i+"\"";
                } else {
                    sql += ",\"" + i+"\"";
                }
            }
            sql+=")";
            exportExcel.setSelectSql(sql);
        }
        List<ColumnMapField> columnMapFields = columnMapFieldDao.selectByTemplateId(exportExcel.getTemplateId());
        List<Boolean> isDict = new ArrayList<>();
        for(int i = 0 ; i <fieldList.size();i++){
            TableField tableField =fieldList.get(i);
            Boolean flag=true;
            for(ColumnMapField columnMapField:columnMapFields){
                if(columnMapField.getIsDict()&&tableField.getFieldName().equals(columnMapField.getColumnName())){
                    isDict.add(true);
                    flag=false;
                    break;
                }
            }
            if(flag) isDict.add(false);
        }
        List<DictInfo>dictInfos =importDataDao.selectAllDictInfo();
        partInfos =  importDataDao.getPartList();


        List<Map<String,Object>>  dataList = exportDataDao.getDataList(exportExcel);

        //将得到的数据转为导出excel需要的格式
        List<List<Object>> data =new ArrayList<>();
        for(Map<String,Object> i :dataList){
            List<Object> temp=new ArrayList<>(i.values());
            for(int j =0;j<fieldList.size();j++){
                if(isDict.get(j)){//如果使用了字典
                    if(fieldList.get(j).getFieldName().equals("单位")||fieldList.get(j).getFieldName().equals("科室/课题组"))
                       // temp.set(j+1,getCodeByUUID(temp.get(j+1).toString())+" "+getNameByUUID(temp.get(j+1).toString()));
                        temp.set(j+1,getNameByUUID(temp.get(j+1).toString()));
                    else{
                        for(DictInfo dictInfo:dictInfos){
                            if(dictInfo.getId().equals(temp.get(j+1).toString())) {
                                temp.set(j+1, dictInfo.getDicValue());
                                break;
                            }
                        }
                    }
                }
            }
            data.add(temp);
        }

        //准备要导入的数据格式
        ExportExcelData excelData=new ExportExcelData();
        //设置文件名
        excelData.setName(exportExcel.getFileName());
        //设置表头
        List<String> title=new ArrayList<>();
        title.add("序号");
        for(TableField tableField:fieldList){
            title.add(tableField.getFieldName());
        }
        excelData.setTitles(title);
        //设置数据
        excelData.setRows(data);

        //生成excel

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(exportExcel.getFileName());
        XSSFCellStyle style = wb.createCellStyle();

        // 设置居中样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        // 设置合计样式
        XSSFCellStyle styleLeft = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗体
        font.setFontHeightInPoints((short) 12); //设置字体大小

//        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        styleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        styleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

        XSSFCellStyle styleRight = wb.createCellStyle();
        styleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        //第一行
        XSSFRow row = sheet.createRow((int) 0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("北京理工大学"+exportExcel.getFileName()+"统计表");
        cell.setCellStyle(style);

        row = sheet.createRow((int)1);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY年MM月dd日");
        String dateStr = dateFormat.format(date);
        int cnt1=0,cnt2=0;
        if("confidential_computer".equals(exportExcel.getTableName())&& !exportExcel.isIScrapped()){//涉密计算机
            cell = row.createCell(0);

            for(List<Object>rowData:data) {
                for (Object cellData : rowData) {
                    if("台式机".equals(cellData.toString())) cnt1++;
                    else if("便携机".equals(cellData.toString())) cnt2++;
                }
            }
            cell.setCellValue("单位（盖章）：                 合计：台式机 "+cnt1+" 台      便携机 "+cnt2+" 台       共 "+data.size()+" 台              ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(11);
            cell.setCellValue("填表人（签字）：              填表日期："+dateStr);
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 18));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 10));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,11 , 18));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50,
                    50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("confidential_computer".equals(exportExcel.getTableName())&& exportExcel.isIScrapped()){//报废涉密计算机
            cell = row.createCell(0);
            for(List<Object>rowData:data) {
                for (Object cellData : rowData) {
                    if("台式机".equals(cellData.toString())) cnt1++;
                    else if("便携机".equals(cellData.toString())) cnt2++;
                }
            }
            cell.setCellValue("单位（盖章）：                 合计：台式机 "+cnt1+" 台      便携机 "+cnt2+" 台       共 "+data.size()+" 台              ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(11);
            String startTime = exportExcel.getConditionsList().get(0);
            String endTime = exportExcel.getConditionsList().get(1);
            String sD=" 年 月 日";
            if(startTime.length()>0)
                sD=startTime.split("-")[0]+" 年 "+ startTime.split("-")[1]+" 月 "+startTime.split("-")[2]+" 日";
            String eD=" 年 月 日";
            if(endTime.length()>0)
                eD=endTime.split("-")[0]+" 年 "+ endTime.split("-")[1]+" 月 "+endTime.split("-")[2]+" 日";
            cell.setCellValue(String.format("填表人（签字）：              统计区间： " + sD+ " 至 " + eD));
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 19));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 10));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,11 , 19));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50,
                    50,50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("non_confidential_computer".equals(exportExcel.getTableName())){//非涉密计算机
            cell = row.createCell(0);
            for(List<Object>rowData:data) {
                for (Object cellData : rowData) {
                    if("台式机".equals(cellData.toString())) cnt1++;
                    else if("便携机".equals(cellData.toString())) cnt2++;
                }
            }
            cell.setCellValue("单位（盖章）：                 合计：台式机 "+cnt1+" 台      便携机 "+cnt2+" 台       共 "+data.size()+" 台              ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(11);
            cell.setCellValue("填表人（签字）：              填表日期："+dateStr);
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 19));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 10));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,11 , 19));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50,
                    50,50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }if("non_confidential_intermediary".equals(exportExcel.getTableName())){//非涉密中间机
            cell = row.createCell(0);
            cell.setCellValue("单位（盖章）：                 合计：共 "+data.size()+" 台                                 ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(11);
            cell.setCellValue("填表人（签字）：              填表日期："+dateStr);
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 18));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 10));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,11 , 18));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50,
                    50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("confidential_information_device".equals(exportExcel.getTableName())&& !exportExcel.isIScrapped()){//涉密信息设备
            cell = row.createCell(0);
            for(List<Object>rowData:data) {
                for (Object cellData : rowData) {
                    if("打印机".equals(cellData.toString())) cnt1++;
                    else if("复印机".equals(cellData.toString())) cnt2++;
                }
            }
            cell.setCellValue("单位（盖章）：                 合计：打印机 "+cnt1+" 台      复印机 "+cnt2+" 台   其他"+(data.size()-cnt1-cnt2)+"台  共 "+data.size()+" 台              ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(11);
            cell.setCellValue("填表人（签字）：              填表日期："+dateStr);
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 17));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 10));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,11 , 17));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50,
                    50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("non_confidential_information_device".equals(exportExcel.getTableName())){//非涉密信息设备
            cell = row.createCell(0);
            for(List<Object>rowData:data) {
                for (Object cellData : rowData) {
                    if("打印机".equals(cellData.toString())) cnt1++;
                    else if("复印机".equals(cellData.toString())) cnt2++;
                }
            }
            cell.setCellValue("单位（盖章）：                 合计：打印机 "+cnt1+" 台      复印机 "+cnt2+" 台   其他"+(data.size()-cnt1-cnt2)+"台  共 "+data.size()+" 台              ");

            cell.setCellStyle(styleLeft);

            cell = row.createCell(10);
            cell.setCellValue("填表人（签字）：              填表日期："+dateStr);
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,10 , 16));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50,
                    50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("confidential_information_device".equals(exportExcel.getTableName())&& exportExcel.isIScrapped()){//报废涉密信息设备
            cell = row.createCell(0);
            for(List<Object>rowData:data) {
                for (Object cellData : rowData) {
                    if("打印机".equals(cellData.toString())) cnt1++;
                    else if("复印机".equals(cellData.toString())) cnt2++;
                }
            }
            cell.setCellValue("单位（盖章）：                 合计：打印机 "+cnt1+" 台      复印机 "+cnt2+" 台   其他"+(data.size()-cnt1-cnt2)+"台  共 "+data.size()+" 台              ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(11);
            String startTime = exportExcel.getConditionsList().get(0);
            String endTime = exportExcel.getConditionsList().get(1);
            String sD=" 年 月 日";
            if(startTime.length()>0)
                sD=startTime.split("-")[0]+" 年 "+ startTime.split("-")[1]+" 月 "+startTime.split("-")[2]+" 日";
            String eD=" 年 月 日";
            if(endTime.length()>0)
                eD=endTime.split("-")[0]+" 年 "+ endTime.split("-")[1]+" 月 "+endTime.split("-")[2]+" 日";
            cell.setCellValue(String.format("填表人（签字）：              统计区间： " + sD+ " 至 " + eD));
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 18));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 10));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,11 , 18));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50,
                    50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("confidential_storage_device".equals(exportExcel.getTableName())&& !exportExcel.isIScrapped()){//涉密存储介质
            cell = row.createCell(0);
            for(List<Object>rowData:data) {
                for (Object cellData : rowData) {
                    if("U盘".equals(cellData.toString())) cnt1++;
                    else if("硬盘".equals(cellData.toString())) cnt2++;
                }
            }
            cell.setCellValue("单位（盖章）：                 合计：U盘 "+cnt1+" 个      硬盘 "+cnt2+" 个   其他"+(data.size()-cnt1-cnt2)+"个  共 "+data.size()+" 个              ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(10);
            cell.setCellValue("填表人：              填表日期："+dateStr);
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,10 , 14));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("confidential_storage_device".equals(exportExcel.getTableName())&& exportExcel.isIScrapped()){//报废涉密存储介质
            cell = row.createCell(0);
            for(List<Object>rowData:data) {
                for (Object cellData : rowData) {
                    if("U盘".equals(cellData.toString())) cnt1++;
                    else if("硬盘".equals(cellData.toString())) cnt2++;
                }
            }
            cell.setCellValue("单位（盖章）：                 合计：U盘 "+cnt1+" 个      硬盘 "+cnt2+" 个   其他"+(data.size()-cnt1-cnt2)+"个  共 "+data.size()+" 个              ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(10);
            String startTime = exportExcel.getConditionsList().get(0);
            String endTime = exportExcel.getConditionsList().get(1);
            String sD=" 年 月 日";
            if(startTime.length()>0)
                sD=startTime.split("-")[0]+" 年 "+ startTime.split("-")[1]+" 月 "+startTime.split("-")[2]+" 日";
            String eD=" 年 月 日";
            if(endTime.length()>0)
                eD=endTime.split("-")[0]+" 年 "+ endTime.split("-")[1]+" 月 "+endTime.split("-")[2]+" 日";
            cell.setCellValue(String.format("填表人（签字）：              统计区间： " + sD+ " 至 " + eD));
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,10 , 15));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("non_confidential_storage_device".equals(exportExcel.getTableName())){//非涉密存储介质
            cell = row.createCell(0);
            for(List<Object>rowData:data) {
                for (Object cellData : rowData) {
                    if("U盘".equals(cellData.toString())) cnt1++;
                    else if("硬盘".equals(cellData.toString())) cnt2++;
                }
            }
            cell.setCellValue("单位（盖章）：                 合计：U盘 "+cnt1+" 个      硬盘 "+cnt2+" 个   其他"+(data.size()-cnt1-cnt2)+"个  共 "+data.size()+" 个              ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(10);
            cell.setCellValue("填表人：              填表日期："+dateStr);
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,10 , 14));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("usb_key".equals(exportExcel.getTableName()) &&!exportExcel.isIScrapped()){//USBKey
            cell = row.createCell(0);
            cell.setCellValue("单位（盖章）：                    合计：   共 "+data.size()+" 个 ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(8);
            cell.setCellValue("填表人：              填表日期："+dateStr);
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,8 , 15));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("usb_key".equals(exportExcel.getTableName()) &&exportExcel.isIScrapped()){//报废USB key
            cell = row.createCell(0);
            cell.setCellValue("单位（盖章）：                    合计：   共"+data.size()+"个 ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(8);
            String startTime = exportExcel.getConditionsList().get(0);
            String endTime = exportExcel.getConditionsList().get(1);
            String sD=" 年 月 日";
            if(startTime.length()>0)
                sD=startTime.split("-")[0]+" 年 "+ startTime.split("-")[1]+" 月 "+startTime.split("-")[2]+" 日";
            String eD=" 年 月 日";
            if(endTime.length()>0)
                eD=endTime.split("-")[0]+" 年 "+ endTime.split("-")[1]+" 月 "+endTime.split("-")[2]+" 日";
            cell.setCellValue(String.format("填表人：              统计区间： " + sD+ " 至 " + eD));
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,8 , 16));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50,
                    50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("security_products".equals(exportExcel.getTableName()) &&!exportExcel.isIScrapped()){//安全保密产品
            cell = row.createCell(0);
            cell.setCellValue("单位（盖章）：      ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(10);
            cell.setCellValue("填表人：              填表日期："+dateStr);
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 18));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,10 , 18));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50,
                    50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }else if("security_products".equals(exportExcel.getTableName()) &&exportExcel.isIScrapped()){//报废安全保密产品
            cell = row.createCell(0);
            cell.setCellValue("单位（盖章）：  ");
            cell.setCellStyle(styleLeft);

            cell = row.createCell(10);
            String startTime = exportExcel.getConditionsList().get(0);
            String endTime = exportExcel.getConditionsList().get(1);
            String sD=" 年 月 日";
            if(startTime.length()>0)
                sD=startTime.split("-")[0]+" 年 "+ startTime.split("-")[1]+" 月 "+startTime.split("-")[2]+" 日";
            String eD=" 年 月 日";
            if(endTime.length()>0)
                eD=endTime.split("-")[0]+" 年 "+ endTime.split("-")[1]+" 月 "+endTime.split("-")[2]+" 日";
            cell.setCellValue(String.format("填表人（签字）：              统计区间： " + sD+ " 至 " + eD));
            cell.setCellStyle(styleRight);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 19));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
            sheet.addMergedRegion(new CellRangeAddress(1, 1,10 , 19));
            int[] excelHeaderWidth = {
                    50,100,100,50,50,
                    50,50,50,50,50,
                    50,50,50,50,50,
                    50,50,50,50
            };
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i,50*excelHeaderWidth[i]);
            }
        }



        String pattern="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29) ";
        int colIndex = 0,rowIndex=3;
        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        // dataFont.setFontHeightInPoints((short) 14);
        dataFont.setColor(IndexedColors.BLACK.index);
        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        //设置表头
        Row dataRow=sheet.createRow(2);
        for(String tit:title){
            Cell cell2 = dataRow.createCell(colIndex);
            cell2.setCellValue(tit);
            cell2.setCellStyle(dataStyle);
            colIndex++;
        }

            for (List<Object> rowData : data) {
                 dataRow = sheet.createRow(rowIndex);
                // dataRow.setHeightInPoints(25);
                colIndex = 0;

                for (Object cellData : rowData) {
                    Cell cell3 = dataRow.createCell(colIndex);
                    if (cellData != null) {
                        cell3.setCellValue(cellData.toString());
                    } else {
                        cell3.setCellValue("");
                    }
                    if (Pattern.matches(pattern, cellData.toString())) {
                        DataFormat format = wb.createDataFormat();
                        dataStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
                    }
                    cell3.setCellStyle(dataStyle);
                    colIndex++;
                }
                rowIndex++;
            }


        httpServletResponse.setContentType("application/vnd.ms-excel");
        //注意此处文件名称如果想使用中文的话，要转码new String( "中文".getBytes( "gb2312" ), "ISO8859-1" )
        try {
            httpServletResponse.setHeader("Content-disposition",
                    "attachment;filename=" + new String(("北京理工大学"+exportExcel.getFileName()+"统计结果").getBytes("gb2312"), "ISO8859-1")
                            + ".xlsx");
            OutputStream ouputStream = httpServletResponse.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNameByUUID(String UUid){
        for(int i=0;i<partInfos.size();i++){
            PartInfo partInfo = partInfos.get(i);
            if(partInfo.getId().equals(UUid)){
                return partInfo.getName();
            }
        }
        return "未找到对应字典项";
    }

    public String getCodeByUUID(String UUid){
        for(int i=0;i<partInfos.size();i++){
            PartInfo partInfo = partInfos.get(i);
            if(partInfo.getId().equals(UUid)){
                return partInfo.getCode();
            }
        }
        return "未找到对应字典项";
    }

}
