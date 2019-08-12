package com.management.admin.modules.sys.entity;

import com.management.admin.common.persistence.DataEntity;
import lombok.Data;

   /**
        *type_name 字典项所属类型名称
        */
@Data
public class DictType extends DataEntity<DictType> {
   public String  typeName;
}
