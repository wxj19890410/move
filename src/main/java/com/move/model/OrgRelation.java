package com.move.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="org_relation")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class OrgRelation  extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "open_id")
    private  String openId;

    @Column(name = "group_id")
    private  Integer groupId;

    @Column(name = "dept_id")
    private  Integer deptId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}
