package com.move.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "data_result")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class DataResult extends BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "file_id")
	private Integer fileId;

	private BigDecimal value1;

	private BigDecimal value2;

	private BigDecimal value3;

	private BigDecimal value4;

	private BigDecimal value5;

	private BigDecimal value6;

	private String month;

	private String userid;

	@Column(name = "relation_id")
	private Integer relationId;

	@Column(name = "relation_type")
	private String relationType;

	@Column(name = "person_nub")
	private Integer personNub;

	private BigDecimal total;

	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public Integer getPersonNub() {
		return personNub;
	}

	public void setPersonNub(Integer personNub) {
		this.personNub = personNub;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public BigDecimal getValue1() {
		return value1;
	}

	public void setValue1(BigDecimal value1) {
		this.value1 = value1;
	}

	public BigDecimal getValue2() {
		return value2;
	}

	public void setValue2(BigDecimal value2) {
		this.value2 = value2;
	}

	public BigDecimal getValue3() {
		return value3;
	}

	public void setValue3(BigDecimal value3) {
		this.value3 = value3;
	}

	public BigDecimal getValue4() {
		return value4;
	}

	public void setValue4(BigDecimal value4) {
		this.value4 = value4;
	}

	public BigDecimal getValue5() {
		return value5;
	}

	public void setValue5(BigDecimal value5) {
		this.value5 = value5;
	}

	public BigDecimal getValue6() {
		return value6;
	}

	public void setValue6(BigDecimal value6) {
		this.value6 = value6;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
