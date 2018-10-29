package com.move.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="user_data")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class UserData extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private  String name;

    private  String phone;

    @Column(name = "open_id")
    private  String OpenID;

    @Column(name = "pass_word")
    private  String passWord;

    private  String account;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenID() {
        return OpenID;
    }

    public void setOpenID(String openID) {
        OpenID = openID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
