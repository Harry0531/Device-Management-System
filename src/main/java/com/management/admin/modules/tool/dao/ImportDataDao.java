package com.management.admin.modules.tool.dao;

import com.management.admin.modules.tool.entity.DynamicInsertParam;
import com.management.admin.modules.tool.entity.DynamicUpdateParam;
import com.management.admin.modules.tool.entity.tiny.DictInfo;
import com.management.admin.modules.tool.entity.tiny.PartInfo;
import com.management.admin.modules.tool.entity.tiny.TableField;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImportDataDao {

    //根据Excel数据插入数据库
    int dynamicInsert(DynamicInsertParam dynamicInsertParam);

    int dynamicUpdate(DynamicUpdateParam dynamicUpdateParam);

    // 获取指定表中的所有列信息
    List<TableField> selectFieldListByTableName(@Param("tableName") String tableName);

    List<DictInfo> selectAllDictInfo();

    List<PartInfo> getPartList();

    List<String> getNumber(@Param("tableName") String tableName);

    List<String> getSecretNumber(@Param("tableName") String tableName);
}
