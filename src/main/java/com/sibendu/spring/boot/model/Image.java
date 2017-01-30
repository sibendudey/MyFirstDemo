package com.sibendu.spring.boot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Image {

	@Id @GeneratedValue
	private Long _id;
	
	private String name;
	
	@SuppressWarnings("unused")
	public Image() 	{
		
	}

	public Image(String name) {
		this.name = name;
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
