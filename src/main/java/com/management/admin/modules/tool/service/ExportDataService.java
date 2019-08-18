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

    @Autowired
    DictService dictService;

    @Autowired
    ImportDataDao importDataDao;

    @Autowired
    ColumnMapFieldDao columnMapFieldDao;

    private List<PartInfo> partInfos;
    private boolean isScrapped=false;

    public ExportExcelData ExportToExcel(ExportExcel exportExcel){
        //设置查询字段SQL
        String sql = "@x:=ifnull(@x,0)+1 as \"number\"";
        List<TableField> fieldList=exportExcel.getFieldList();
        for (TableField tableField : fieldList) {
            sql += ",`" + tableField.getFieldType() + "`";
        }
        exportExcel.setFieldSQL(sql);

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
                    temp.set(j+1,getCodeByUUID(temp.get(j+1).toString())+" "+getNameByUUID(temp.get(j+1).toString()));
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

        return  excelData;

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
