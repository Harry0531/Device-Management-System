package com.management.admin.modules.sys.service;

import com.management.admin.modules.sys.dao.DictDao;
import com.management.admin.modules.sys.entity.Dict;
import com.management.admin.modules.sys.entity.DictType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DictService {

    @Autowired
    DictDao dictDao;
    //关于字典类型
    public boolean insertDictType(DictType dictType){
        dictType.preInsert();
        return dictDao.insertDictType(dictType) == 1;
    }
    public List<DictType> selectDictTypeList(){
        return dictDao.selectDictTypeList();
    }

    //关于字典操作
    public List<Dict> selectDictListByPage(Dict dict){
        return dictDao.selectDictListByPage(dict);
    }
    public int selectSearchCount(Dict dict) {
        return dictDao.selectSearchCount(dict);
    }

    public  boolean insertDict(Dict dict){
        dict.preInsert();
        return dictDao.insertDict(dict) == 1;
    }
    public boolean deleteDictByIds(List<Dict> dicts){
        return dicts.size()==0 || dictDao.deleteDictByIds(dicts)==dicts.size();
    }
    public  boolean updateDict(Dict dict){
        dict.preUpdate();
        return dictDao.updateDict(dict) == 1;
    }

    //根据类型和字段名判断是否用了字典
    public  boolean isUseDict(String typeId,String fieldName){
        return dictDao.isUseDict(typeId,fieldName) >= 1 ;
    }
    //三个参数确定唯一字典 d
    /**
     * @param enName  对应字段英文名称
     * @param typeId 对应字段字典类别
     * @param value 对应字典值
     */
    public String getUUID(String typeId,String enName,String value){
        return dictDao.getUUid(typeId,enName,value);
    }

       /**
            * @param UUid 根据uuid获取对应值
            */
    public String getValue(String UUid){
        return dictDao.getValue(UUid);
    }
}
