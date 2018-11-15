package com.move.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "data_result")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class DataResult extends BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "file_id")
	private Integer fileId;

	private Integer value1;

	private Integer value2;

	private Integer value3;

	private Integer value4;

	private Integer value5;

	private Integer value6;

	private String month;

	private String userid;

	@Column(name = "relation_id")
	private Integer relationId;

	@Column(name = "relation_type")
	private String relationType;

	@Column(name = "person_nub")
	private Integer personNub;

	private Integer total;

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

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Integer getValue1() {
		return value1;
	}

	public void setValue1(Integer value1) {
		this.value1 = value1;
	}

	public Integer getValue2() {
		return value2;
	}

	public void setValue2(Integer value2) {
		this.value2 = value2;
	}

	public Integer getValue3() {
		return value3;
	}

	public void setValue3(Integer value3) {
		this.value3 = value3;
	}

	public Integer getValue4() {
		return value4;
	}

	public void setValue4(Integer value4) {
		this.value4 = value4;
	}

	public Integer getValue5() {
		return value5;
	}

	public void setValue5(Integer value5) {
		this.value5 = value5;
	}

	public Integer getValue6() {
		return value6;
	}

	public void setValue6(Integer value6) {
		this.value6 = value6;
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
}
