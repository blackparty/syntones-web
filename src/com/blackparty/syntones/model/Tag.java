package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;

@Entity
@Table(name="tag_tbl")
public class Tag {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="tag")
	private String tag;
	
	public Tag(){
		
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
	
	
	
	
	
	
}
