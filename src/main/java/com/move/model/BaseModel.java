package com.move.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class BaseModel implements Serializable {
    @Id
    @GeneratedValue
    private  Integer id;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "del_flag")
    private Date delFlag;


    public BaseModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
