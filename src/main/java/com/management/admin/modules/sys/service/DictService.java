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
}
