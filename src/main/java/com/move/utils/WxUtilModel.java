package com.move.utils;

import java.util.List;

import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
import com.move.model.UserData;

public class WxUtilModel {
	private String errcode;

	private String access_token;
	
	private String errmsg;
	
	private List<OrgDepartment> department;
	
	private List<OrgGroup> taglist;
	
	private List<UserData> userlist;
	
	

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}


	public List<OrgDepartment> getDepartment() {
		return department;
	}

	public void setDepartment(List<OrgDepartment> department) {
		this.department = department;
	}

	public List<OrgGroup> getTaglist() {
		return taglist;
	}

	public void setTaglist(List<OrgGroup> taglist) {
		this.taglist = taglist;
	}

	public List<UserData> getUserlist() {
		return userlist;
	}

	public void setUserlist(List<UserData> userlist) {
		this.userlist = userlist;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
