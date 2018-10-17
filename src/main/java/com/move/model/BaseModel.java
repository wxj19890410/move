package com.move.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class BaseModel implements Serializable {
    @Id
    @GeneratedValue
    private  long id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "del_flag")
    private Date delFlag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Date delFlag) {
        this.delFlag = delFlag;
    }
}
