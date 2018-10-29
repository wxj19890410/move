package com.move.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="user_data")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class OrgGroup {
}
