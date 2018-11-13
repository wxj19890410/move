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
@Table(name="ignore_users")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class IgnoreUsers implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    

	@Column(name = "edit_date")
	private Date editDate;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "ignore_flag")
	private String ignoreFlag;
	
	
	private String userid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getIgnoreFlag() {
		return ignoreFlag;
	}

	public void setIgnoreFlag(String ignoreFlag) {
		this.ignoreFlag = ignoreFlag;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
