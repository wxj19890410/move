package com.move.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="org_department")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class OrgDepartment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    private  Integer id;

    private  String name;

    private  Integer parentid;

    
    @Column(name = "order_nub")
    private  Long order;
    
    
    @Column(name = "create_date")
    private  Date createDate;
    
    
    
    
    public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

    
    

}
