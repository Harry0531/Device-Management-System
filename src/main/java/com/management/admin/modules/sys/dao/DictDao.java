package com.management.admin.modules.sys.dao;

import com.management.admin.modules.sys.entity.Dict;
import com.management.admin.modules.sys.entity.DictType;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface DictDao {

    int insertDictType(DictType dictType);

    List<DictType> selectDictTypeList();

    //关于字典操作
    List<Dict> selectDictListByPage(Dict dict);
    int selectSearchCount(Dict dict);

    int insertDict(Dict dict);

    int deleteDictByIds(List<Dict> dicts);

    int updateDict(Dict dict);
}
