package com.move.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="org_department")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class OrgDepartment extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private  String name;

    @Column(name = "parent_id")
    private  Integer parentId;

    @Column(name = "d_level")
    private  Integer dLevel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getdLevel() {
        return dLevel;
    }

    public void setdLevel(Integer dLevel) {
        this.dLevel = dLevel;
    }
}
