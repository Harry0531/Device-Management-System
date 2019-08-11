package com.management.admin.common.persistence;

import com.management.admin.common.utils.IdGen;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 所有表格的对应实体类的基类
 * 提供了基础的通用属性
 */
@MappedSuperclass
public class DataEntity<T> {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "create_time")
    private Date createDate;        // 创建日期
    @Column(name = "modify_time")
    private Date modifyDate;        // 最后修改日期
    @Column(name = "del_flag")
    private boolean delFlag;        // 是否被删除

    @Transient
    private Page<T> page; // 分页对象

    public DataEntity() {

    }

    /**
     * 插入之前手动调用
     */
    public void preInsert() {
        id = IdGen.uuid();
        createDate = new Date();
        modifyDate = createDate;
        delFlag = false;
    }

    /**
     * 更新之前手动调用
     */
    public void preUpdate() {
        modifyDate = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }


    public boolean isDelFlag() {
        return delFlag;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }



    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
