package com.move.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="org_group")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class OrgGroup extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private  String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
